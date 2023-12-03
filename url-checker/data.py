from dataclasses import dataclass
from enum import Enum


class UrlSafetyStatus(Enum):
    YES = 0
    NO = 1
    UNKNOWN = 2


@dataclass
class UrlSafetyReport:
    url_safety_status: UrlSafetyStatus
    syntax_is_correct: UrlSafetyStatus
    site_is_alive: UrlSafetyStatus

    def to_dict(self):
        return {
            'status': self.url_safety_status.name,
            'safety_params': {
                'syntax_is_correct': self.syntax_is_correct.name,
                'site_is_alive': self.site_is_alive.name
            }
        }


@dataclass
class UrlSafetyRequest:
    url: str
