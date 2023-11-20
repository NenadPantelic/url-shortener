from typing import List

from data import UrlSafetyReport, UrlSafetyStatus
from logconfig import log


class UrlChecker:
    def __init__(self):
        pass

    @staticmethod
    def _has_secure_connection(url: str) -> UrlSafetyStatus:
        return UrlSafetyStatus.UNKNOWN

    @staticmethod
    def _is_site_up(url: str) -> UrlSafetyStatus:
        return UrlSafetyStatus.UNKNOWN

    @staticmethod
    def _has_valid_syntax(url: str) -> UrlSafetyStatus:
        return UrlSafetyStatus.UNKNOWN

    @staticmethod
    def check_url(url) -> UrlSafetyReport:
        syntax_is_correct = UrlChecker._has_valid_syntax(url)
        connection_is_secure = UrlChecker._has_secure_connection(url)
        site_is_up = UrlChecker._is_site_up(url)
        url_safety_status = UrlChecker._determine_url_safety_status(
            [connection_is_secure, syntax_is_correct, site_is_up]
        )

        log.info(f'Check url safety report:'
                 f'syntax is correct = {syntax_is_correct}, '
                 f'secure connection = {connection_is_secure}, '
                 f'site is up = {site_is_up}')

        return UrlSafetyReport(
            url_safety_status,
            syntax_is_correct,
            connection_is_secure,
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
