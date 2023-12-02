class ApiException(Exception):
    def __init__(self, message: str, status_code: int):
        super().__init__(message)
        self._status_code = status_code

    @property
    def status_code(self):
        return self._status_code
