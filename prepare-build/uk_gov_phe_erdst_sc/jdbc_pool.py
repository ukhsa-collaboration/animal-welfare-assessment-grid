""" Payara Postgres database connection pool configuration """

from uk_gov_phe_erdst_sc.constants import Constants


class JdbcPool:

    """ Payara application server JDBC pool """

    DEFAULT_POSTGRES_PORT_NUMBER = 5432
    DEFAULT_MSSQL_PORT_NUMBER = 1433
    SQL_DRIVER_TYPE_UNKNOWN = 0
    SQL_DRIVER_TYPE_POSTGRES = 1
    SQL_DRIVER_TYPE_MSSQL = 2


    def __init__(self,
                 host=str(),
                 username=str(),
                 password=str(),
                 actual_database_name=str(),
                 connection_pool_name=str(),
                 port=DEFAULT_POSTGRES_PORT_NUMBER,
                 authorisation=False,
                 sql_driver_type=SQL_DRIVER_TYPE_POSTGRES,
                 suffix=Constants.EMPTY_STRING):

        self.host = host
        self.username = username + suffix
        self.password = password
        self.actual_database_name = actual_database_name + suffix
        self.connection_pool_name = connection_pool_name + suffix
        self.port = str(port)
        self.authorisation = authorisation
        self.sql_driver_type = sql_driver_type


    def get_connection_string(self):
        """ Connection string including user, password, host and port to connect the database """

        if self.sql_driver_type == self.SQL_DRIVER_TYPE_MSSQL:
            return \
                '"user=' + \
                self.username + \
                ':password=' + \
                self.password + \
                r':URL=jdbc\:sqlserver\://' + \
                self.host + \
                r';instanceName\=SQLEXPRESS' + \
                r';portNumber\=1433' + \
                r';database\=' + \
            self.actual_database_name + \
                r';integratedSecurity\=true;' + \
                '"'

        return \
            '"User=' + \
            self.username + \
            ':Password=' + \
            self.password + \
            r':URL=jdbc\:postgresql\://' + \
            self.host + \
            r'\:' + \
            self.port + \
            '/' + \
            self.actual_database_name + \
            '"'

    def get_connection_pool_name(self):
        return self.connection_pool_name


    def is_authorised(self):
        return self.authorisation


    """ Database tables realm connection string """
    def get_db_realm_string(self):
        if not self.authorisation:
            return None

        return \
            '"jaas-context=jdbcRealm' + \
            ':encoding=HEX:password-column=password:datasource-jndi=jdbc/' + \
            self.connection_pool_name + \
            ':group-table=users_groups' + \
            ':group-name-column=group_name' + \
            ':user-table=users' + \
            ':user-name-column=user_name' + \
            ':group-table-user-name-column=user_name' + \
            ':charset=UTF-8' + \
            ':digestrealm-password-enc-algorithm=AES' + \
            ':digest-algorithm=SHA-256"'


