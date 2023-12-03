from typing import List
from urllib.parse import urlparse

import requests

from data import UrlSafetyReport, UrlSafetyStatus
from logconfig import log


class UrlChecker:
    def __init__(self):
        pass

    @staticmethod
    def _is_site_up(url: str) -> UrlSafetyStatus:
        log.info(f'Checking whether the {url} is up and running.')

        try:
            requests.head(url)
            return UrlSafetyStatus.YES
        except Exception as e:
            log.warning(f'The {url} is not up and running. Details: {e}.')
            return UrlSafetyStatus.NO

    @staticmethod
    def _has_valid_syntax(url: str) -> UrlSafetyStatus:
        log.info(f'Checking whether the url {url} has a valid syntax.')

        try:
            parsed_url = urlparse(url)
            has_scheme_and_host = bool(parsed_url.scheme and parsed_url.netloc)
            return UrlSafetyStatus.YES if has_scheme_and_host else UrlSafetyStatus.NO
        except Exception as e:
            log.warning(f'Could not determine whether the {url} has the valid syntax due to {e}.')
            return UrlSafetyStatus.UNKNOWN

    @staticmethod
    def check_url(url: str) -> UrlSafetyReport:
        log.info(f'Checking the safety of the {url}')

        syntax_is_correct = UrlChecker._has_valid_syntax(url)
        site_is_up = UrlChecker._is_site_up(url)
        url_safety_status = UrlChecker._determine_url_safety_status(
            [syntax_is_correct, site_is_up]
        )

        log.info(f'Check url safety report:'
                 f'syntax is correct = {syntax_is_correct}, '
                 f'site is up = {site_is_up}')

        return UrlSafetyReport(
            url_safety_status,
            syntax_is_correct,
            site_is_up
        )

    @staticmethod
    def _determine_url_safety_status(url_safety_param_statuses: List[UrlSafetyStatus]) -> UrlSafetyStatus:
        has_unknown_status = False

        for url_safety_status in url_safety_param_statuses:
            if url_safety_status == UrlSafetyStatus.NO:
                return UrlSafetyStatus.NO

            if url_safety_status == UrlSafetyStatus.UNKNOWN:
                has_unknown_status = True

        return UrlSafetyStatus.UNKNOWN if has_unknown_status else UrlSafetyStatus.YES
