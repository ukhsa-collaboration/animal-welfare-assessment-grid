from .configuration import Configuration
from .constants import Constants
from datetime import datetime
import os

from uk_gov_phe_erdst_sc import constants

class PayaraConfiguration(Configuration):

    """ Read-only Payara Server Configuration, setup once """

    def __init__(self):
        super().__init__()
        self.output_filename = 'output-' + datetime.now().strftime('%Y-%m-%d-%H%M')


    def validate(self):
        assert self.payara_root_folder is not None
        assert self.domain_name is not None
        self.__setup_folders()
        if self.file_jdbc_jar_path:
            self.__setup_jdbc_path()
        if self.jdbc_pools:
            self.__setup_jdbc_detail()
        if self.ldap_realm_name:
            self.__setup_ldap_realm(
                self.ldap_host,
                self.ldap_base_dn,
                self.ldap_search_bind_dn,
                self.ldap_bind_password,
                self.ldap_host_port_number)

        self._validated = True
        super().validate()


    """ Private methods """
    def __setup_folders(self):
        self.__setup_asadmin()


    def __setup_asadmin(self):
        self.asadmin = f'{self.payara_root_folder}{os.sep}bin{os.sep}{Constants.ASADMIN}'


    def __setup_jdbc_path(self):
        assert os.path.exists(self.file_jdbc_jar_path)
        self.domain_lib_path = \
            f'{self.payara_root_folder}{os.sep}glassfish{os.sep}domains{os.sep}{self.domain_name}{os.sep}lib{os.sep}' + \
            os.path.basename(self.file_jdbc_jar_path)


    def __setup_jdbc_detail(self):
        if self.jdbc_realm_name is None:
            self.jdbc_realm_name = Constants.Payara.DEFAULT_JDBC_REALM + self.suffix


    def __setup_ldap_realm(self,
                           ldap_host=str(),
                           base_dn=str(),
                           search_bind_dn=str(),
                           search_bind_password=str(),
                           ldap_port=Constants.DEFAULT_LDAP_PORT_NUMBER):
        """ LDAP Connection String """

        _ldap_host_and_port = \
            'ldap\\://' + \
            ldap_host + \
            '\\:' + \
            str(ldap_port)

        # The base distinguished name parameter value replaces '=' with '\=' to allow the
        # application to generate the configuration correctly.
        self.ldap_realm_property = \
            'directory=' + \
            _ldap_host_and_port + \
            ':base-dn=' + \
            base_dn.replace('=','\\=') + \
            ':jaas-context=ldapRealm' + \
            ':search-bind-dn=' + \
            search_bind_dn + \
            ':search-bind-password=' + \
            search_bind_password + \
            ':group-search-filter=(&(objectClass\\=group)(member\\=%d))' + \
            ':search-filter=(&(objectClass\\=user)(sAMAccountName\\=%s))' + \
            ':java.naming.referral=ignore'


    """ Public members """
    output_to_console = False
    output_command_to_file = False
    execute_command = True
    delete_default_domain = True
    recreate_network_listener = True
    recreate_bootstrap = False

    default_admin_listener_port = Constants.Payara.DEFAULT_ADMIN_LISTENER_PORT
    http_listener_port = Constants.Payara.DEFAULT_HTTP_LISTENER_PORT
    payara_root_folder = None
    domain_name = None
    output_filename = None
    jdbc_realm_name = None
    ldap_realm_name = None
    jdbc_pools = None
    system_properties = None
    ldap_realm_name = None
    ldap_host = None
    ldap_host_port_number = None
    ldap_base_dn = None
    ldap_search_bind_dn = None
    ldap_bind_password = None

    """ Members updated on validation """
    asadmin = None
    file_jdbc_jar_path = None
    domain_lib_path = None
    ldap_realm_property = None