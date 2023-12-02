from dotenv import dotenv_values
from flask import Flask, request

from data import UrlSafetyRequest
from exception import ApiException
from url_checker import UrlChecker

from logconfig import log

app = Flask(__name__)

config = dotenv_values(".env.dev")

SESSION_HEADER_KEY = config['url-checker.session-key']
SESSION = config['url-checker.session-value']
PORT = int(config['url-checker.port'])
ENDPOINT_PATH = '/api/v1/url-checker'

url_checker = UrlChecker()


def _check_session(request_):
    session_token = request_.headers.get(SESSION_HEADER_KEY)
    if session_token == SESSION:
        return
    raise ApiException(f'Unauthorized', 401)


def deserialize(payload):
    try:
        return UrlSafetyRequest(payload.get('url'))
    except Exception as e:
        log.error(f'Could not deserialize the request {request} due to {e}')
        raise ApiException('Invalid body syntax', 422)


def context(deser_function, check_auth=True):
    def wrap(f):
        def decorator(*args):
            obj = deser_function(request.json)

            if check_auth:
                _check_session(request)

            return f(obj)

        return decorator

    return wrap


@app.route(ENDPOINT_PATH, methods=['POST'])
@context(deserialize)
def check_url_safety(url_safety_request: UrlSafetyRequest):
    log.info(f'Received a request to check the safety of the url - {url_safety_request}')
    return url_checker.check_url(url_safety_request.url).to_dict(), 200


@app.errorhandler(Exception)
def handle_error(error):
    log.error(f'An error occurred: {error}')
    if isinstance(error, ApiException):
        return {'error': error.message}, error.status_code

    return {'error': 'Internal Server Error'}, 500


if __name__ == "__main__":
    app.run(port=PORT)
