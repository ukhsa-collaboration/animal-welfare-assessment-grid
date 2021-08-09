import os

class Constants:

    class Payara:
        DEFAULT_HTTP_LISTENER_PORT = 8080
        DEFAULT_ADMIN_LISTENER_PORT = 4848
        DEFAULT_JDBC_REALM = 'jdbcRealm'
        DEFAULT_LDAP_REALM_NAME = 'ldapRealm'
        DEFAULT_DOMAIN_NAME = 'domain1'

    class Postgres:
        DEFAULT_DB_PORT = 5432


    DEFAULT_HTTP = 'http'
    DEFAULT_HOSTNAME = 'localhost'
    WIN_OS_PROGRAM_EXT = '.bat'
    DEFAULT_LDAP_PORT_NUMBER = 389
    AUTHTYPE_DATABASE = 'database'
    AUTHTYPE_LDAP = 'ldap'
    PSQL = 'psql'
    GIT = 'git'
    MAVEN = 'mvn' if os.sep == '/' else 'mvn.cmd'
    NPM = 'npm' if os.sep == '/' else 'npm.cmd'
    WHICH = 'which' if os.sep == '/' else 'where'
    UNC_PREFIX = '' if os.sep == '/' else '\\\\?\\'
    ASADMIN = 'asadmin' if os.sep == '/' else 'asadmin.bat'
    EMPTY_STRING = ''

    WEB_INF_FOLDER = f'src{os.sep}main{os.sep}webapp{os.sep}WEB-INF'
    META_INF_FOLDER = f'src{os.sep}main{os.sep}resources{os.sep}META-INF{os.sep}'
    PERSISTENCE_XML_FILE = f'{META_INF_FOLDER}persistence.xml'
    WEB_XML_FILE = f'{WEB_INF_FOLDER}{os.sep}web.xml'
    GLASSFISH_WEB_XML_FILE = f'{WEB_INF_FOLDER}{os.sep}glassfish-web.xml'

    PERSISTENCE_DEFAULT = '<properties>\n' + \
            '\t\t\t<property ' + \
            'name="eclipselink.target-database" ' + \
            'value="PostgreSQL" />\n' + \
            '\t\t\t<property ' + \
            'name="eclipselink.ddl-generation" ' + \
            'value="none" />\n' + \
            '\t\t</properties>\n'