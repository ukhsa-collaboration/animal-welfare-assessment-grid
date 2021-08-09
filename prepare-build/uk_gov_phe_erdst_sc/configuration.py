from datetime import datetime
from .constants import Constants

class Configuration:

    suffix = Constants.EMPTY_STRING
    _validated = False

    def __init__(self):
        super().__init__()

    def validate(self):
        """ The most class members are asserted """

        if not self._validated:
            raise Exception('Configuration invalid')

    @staticmethod
    def getFormattedMonthYear():
        return datetime.now().strftime("%Y-%m")

    @staticmethod
    def getFormattedDate():
        return datetime.now().strftime("%Y-%m-%d-%H%M%S")
