from .server import ApplicationServer
from .constants import Constants
from .jdbc_pool import JdbcPool
import os
import subprocess
import shutil


class PayaraApplicationServer(ApplicationServer):

    """ Basic maintenance for a Payara Server """
    """ Supports for Payara Server Community edition 5.2020+ """

    def __init__(self, configuration):
        super().__init__()
        assert configuration is not None
        self._configuration = configuration
        self._configuration.validate()


    def start(self):
        self.start_with_name(self._configuration.domain_name)


    def stop(self):
        self.stop_with_name(self._configuration.domain_name)


    def create(self):
        _cmd = self.call_asadmin_no_port(
                         ['create-domain',
                          '--nopassword=true',
                          '--adminport',
                          str(self._configuration.default_admin_listener_port),
                          self._configuration.domain_name])
        self.__add_command(_cmd)


    def recreate(self):
        self.stop()
        self.stop_all_domains()
        if self._configuration.delete_default_domain:
            self.__delete(Constants.Payara.DEFAULT_DOMAIN_NAME)
        self.delete()
        self.create()
        self.setup()


    def setup(self):
        if (self._configuration.recreate_network_listener or
            self._configuration.file_jdbc_jar_path):
            self.start()

        if self._configuration.recreate_network_listener:
            self.__configure_network_listener()

        if self._configuration.file_jdbc_jar_path:
            self.__install_jdbc_jar()

        if (self._configuration.recreate_network_listener or
            self._configuration.file_jdbc_jar_path):
            self.stop()

        if (self._configuration.jdbc_pools or
            self._configuration.recreate_bootstrap or
            self._configuration.system_properties):
            self.start()

        if self._configuration.jdbc_pools:
            self.__configure_database_connection()

        if self._configuration.ldap_realm_name:
            self.__configure_ldap_connection()

        if self._configuration.recreate_bootstrap:
            self.__configure_bootstrap_jvm_options()

        if self._configuration.system_properties:
            self.__configure_system_properties()

        if (self._configuration.jdbc_pools or
            self._configuration.recreate_bootstrap or
            self._configuration.system_properties):
            self.stop()


    def delete(self):
        self.__delete(self._configuration.domain_name)


    def create_service(self):
        _cmd = self.call_asadmin(['create-service', self._configuration.domain_name])
        self.__add_command(_cmd)


    def stop_all_domains(self):
        _cmd = self.call_asadmin(['stop-all-domains'])
        self.__add_command(_cmd)


    def stop_with_name(self, domain_name):
        _cmd = self.call_asadmin(['stop-domain', domain_name])
        self.__add_command(_cmd)


    def start_with_name(self, domain_name):
        _cmd = self.call_asadmin(['start-domain', domain_name])
        self.__add_command(_cmd)


    def call_asadmin_no_port(self, cmdlist):
        return [self._configuration.asadmin] + cmdlist


    def call_asadmin(self, cmdlist):
        return [self._configuration.asadmin,
               '--port',
               str(self._configuration.default_admin_listener_port)] + \
               cmdlist


    def __configure_network_listener(self):
        """ Create the network listener configuration for the application server """

        self.__add_command(self.call_asadmin(['delete-network-listener', 'http-listener-1']))
        self.__add_command(self.call_asadmin(['delete-network-listener', 'http-listener-2']))

        _cmd = \
            self.call_asadmin(['create-network-listener',
             '--listenerport',
             str(self._configuration.http_listener_port),
             '--protocol',
             'http-listener-1',
             'http-listener-1'])
        self.__add_command(_cmd)


    def __add_command(self, command_text):
        """ Print, store, execute a system command """
        assert command_text is not None

        self._command_list.append(command_text)

        if self._configuration.output_to_console:
            print(command_text)

        if self._configuration.output_command_to_file:
            _f = open(self._configuration.output_filename, 'a')
            if (isinstance(command_text, list)):
                for command_arg in command_text:
                    _f.write(f'{command_arg} ')
            else:
                _f.write(command_text)
            _f.write(os.linesep)
            _f.close()

        if self._configuration.execute_command:
            subprocess.call(command_text)


    def __delete(self, domain_name):
        _cmd = self.call_asadmin(['delete-domain', domain_name])
        self.__add_command(_cmd)


    def __install_jdbc_jar(self):
        if self._configuration.execute_command:
            shutil.copyfile(self._configuration.file_jdbc_jar_path, self._configuration.domain_lib_path)


    def __configure_database_connection(self):
        """ Create database connection for the primary and/or the user authorisation database """

        # Create the jdbc connection pools and jdbc resources
        _aw_auth_jdbc_realm_property = None
        for jdbc_pool in self._configuration.jdbc_pools:

            if jdbc_pool.sql_driver_type == JdbcPool.SQL_DRIVER_TYPE_MSSQL:
                _cmd = \
                    self.call_asadmin([
                    'create-jdbc-connection-pool',
                    '--ping=true',
                    '--datasourceclassname=com.microsoft.sqlserver.jdbc.SQLServerXADataSource',
                    '--restype=javax.sql.XADataSource',
                    '--property=' + jdbc_pool.get_connection_string(),
                    jdbc_pool.get_connection_pool_name()])
            else:
                _cmd = \
                    self.call_asadmin([
                    'create-jdbc-connection-pool',
                    '--ping=true',
                    '--datasourceclassname=org.postgresql.xa.PGXADataSource',
                    '--restype=javax.sql.XADataSource',
                    '--property=' + jdbc_pool.get_connection_string(),
                    jdbc_pool.get_connection_pool_name()])

            self.__add_command(_cmd)

            _cmd = \
                self.call_asadmin([
                 'create-jdbc-resource',
                 '--connectionpoolid',
                 jdbc_pool.get_connection_pool_name(),
                 'jdbc/' + jdbc_pool.get_connection_pool_name()])
            self.__add_command(_cmd)

            # If a jdbc connection pool has a database connection link to the realm name
            if jdbc_pool.is_authorised():
                _aw_auth_jdbc_realm_property = jdbc_pool.get_db_realm_string()

        # Create the jdbc authentication realm if required
        if _aw_auth_jdbc_realm_property is not None:
            _cmd = \
                self.call_asadmin([
                 'create-auth-realm',
                 '--classname',
                 'com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm',
                 '--property=' + _aw_auth_jdbc_realm_property,
                 self._configuration.jdbc_realm_name])
            self.__add_command(_cmd)


    def __configure_bootstrap_jvm_options(self):
        """ Update the bootstrap options for this application server """
        grizzly_options1 = \
            '-Xbootclasspath/p\\:${com.sun.aas.installRoot}/lib/grizzly-npn-bootstrap-1.6.jar'
        grizzly_options2 = \
            '-Xbootclasspath/p\\:${com.sun.aas.installRoot}/lib/grizzly-npn-bootstrap-1.7.jar'
        grizzly_options3 = \
            '-Xbootclasspath/p\\:${com.sun.aas.installRoot}/lib/grizzly-npn-bootstrap-1.8.jar'
        grizzly_options4 = \
            '-Xbootclasspath/p\\:${com.sun.aas.installRoot}/lib/grizzly-npn-bootstrap-1.8.1.jar'

        self.__add_command(self.call_asadmin(['delete-jvm-options', grizzly_options1, '--target', 'server-config']))
        self.__add_command(self.call_asadmin(['delete-jvm-options', grizzly_options2, '--target', 'server-config']))
        self.__add_command(self.call_asadmin(['delete-jvm-options', grizzly_options3, '--target', 'server-config']))
        self.__add_command(self.call_asadmin(['delete-jvm-options', grizzly_options4, '--target', 'server-config']))

        self.__add_command(self.call_asadmin(['delete-jvm-options', grizzly_options1, '--target', 'default-config']))
        self.__add_command(self.call_asadmin(['delete-jvm-options', grizzly_options2, '--target', 'default-config']))
        self.__add_command(self.call_asadmin(['delete-jvm-options', grizzly_options3, '--target', 'default-config']))
        self.__add_command(self.call_asadmin(['delete-jvm-options', grizzly_options4, '--target', 'default-config']))

        self.__add_command(self.call_asadmin(['create-jvm-options', grizzly_options4, '--target', 'server-config']))
        self.__add_command(self.call_asadmin(['create-jvm-options', grizzly_options4, '--target', 'default-config']))


    def __configure_ldap_connection(self):
        """ Create the user authorisation for an Active Directory/LDAP connection """
        _cmd = self.call_asadmin(
            ['create-auth-realm',
             '--classname',
             'com.sun.enterprise.security.auth.realm.ldap.LDAPRealm',
             f'--property={self._configuration.ldap_realm_property}',
             self._configuration.ldap_realm_name])
        self.__add_command(_cmd)


    def __configure_system_properties(self):
        for _key in self._configuration.system_properties:
            _value = self._configuration.system_properties[_key].replace(':','\:')
            _cmd = ['create-system-properties', '--target', 'server-config', f'{_key}={_value}']
            self.__add_command(self.call_asadmin(_cmd))


    """ Private """
    _configuration = None
