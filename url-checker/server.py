from dotenv import dotenv_values
from flask import Flask, request
from argparse import ArgumentParser

from data import UrlSafetyRequest
from exception import ApiException
from url_checker import UrlChecker

from logconfig import log

# constants
DEV_PROFILE = 'dev'
PRODUCTION_PROFILE = 'prod'

DEV_PROFILE_CONFIG_FILE = '.env.dev'
PROD_PROFILE_CONFIG_FILE = '.env.prod'

# config keys
ENDPOINT_PATH = '/api/v1/url-checker'
SESSION_HEADER_CONFIG_KEY = 'url-checker.session-key'
SESSION_CONFIG_KEY = 'url-checker.session-value'
PORT_CONFIG_KEY = 'url-checker.port'

parser = ArgumentParser()
parser.add_argument('-p', '--profile', help='The profile used to configure the setup of the server.')

args = parser.parse_args()
profile = args.profile

if profile == DEV_PROFILE:
    profile_config_file = DEV_PROFILE_CONFIG_FILE
elif profile == PRODUCTION_PROFILE:
    profile_config_file = PROD_PROFILE_CONFIG_FILE
else:
    log.warning('The profile has not been provided, defaulting to DEV profile...')
    profile_config_file = DEV_PROFILE_CONFIG_FILE

config = dotenv_values(profile_config_file)
SESSION_HEADER_KEY = config[SESSION_HEADER_CONFIG_KEY]
SESSION = config[SESSION_CONFIG_KEY]
PORT = int(config[PORT_CONFIG_KEY])

url_checker = UrlChecker()
app = Flask(__name__)


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


if __name__ == '__main__':
    app.run(port=PORT)
