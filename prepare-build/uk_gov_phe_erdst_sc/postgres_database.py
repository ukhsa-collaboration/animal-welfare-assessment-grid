from .database import Database

class PostgresDatabase(Database):

    def build():
        raise NotImplementedError()