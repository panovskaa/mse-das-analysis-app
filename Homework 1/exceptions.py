class AlreadyUpdatedException(Exception):
    def __index__(self, message):
        super(message)


class NoOpException(Exception):
    def __init__(self, message):
        super(message)
