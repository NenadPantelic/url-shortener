from config import config
from requests import post, get
from assertpy import assert_that

API_ENDPOINT = config['api.endpoint']
HEADERS = {'Content-Type': 'application/json'}


def send_shorten_url_request(url):
    return post(API_ENDPOINT, json={'url': url}, headers=HEADERS)


def get_complete_url(url):
    return get(f'{API_ENDPOINT}/{url}')


def test_shorten_and_get_url():
    """
    1. Send the extremely long url to be shortened
    2. Assert it is shortened - status code 201
    3. Send shortened URL to get the complete URL
    4. Verify that URL matches the initial one
    """

    url = 'https://www.google.com/search?q=novak+djokovic&oq=novak+dj&aqs=chrome.0' \
          '.0i355i433i512j46i433i512j69i57j0i512l7&sourceid=chrome&ie=UTF-8 '

    # Verify URL shortening
    response = send_shorten_url_request(url)
    assert_that(response.status_code).is_equal_to(201)
    response_body = response.json()
    assert_that(response_body).contains_key('url')

    shortened_url = response_body.get('url')

    # Verify URL reverse lookup
    response = get(shortened_url)
    assert_that(response.status_code).is_equal_to(200)


def test_shorten_invalid_url():
    """
    1. Send an empty url
    2. Assert the server responded with 400
    """

    url = ''
    response = send_shorten_url_request(url)
    assert_that(response.status_code).is_equal_to(400)


def test_get_non_existent_url():
    """
    1. Get a complete URL from some non-existent short URL
    2. Assert the server responded with 404
    """

    url = 'let-s-try-with-something-that-does-not-exist'
    response = get_complete_url(url)
    assert_that(response.status_code).is_equal_to(404)


tests_to_execute = [
    test_shorten_and_get_url,
    test_shorten_invalid_url,
    test_get_non_existent_url
]

for test in tests_to_execute:
    result = 'N/A'

    try:
        test()
        result = 'SUCCESS'
    except AssertionError as e:
        result = 'FAILED'

    print(f'{test.__name__}: {result}')
