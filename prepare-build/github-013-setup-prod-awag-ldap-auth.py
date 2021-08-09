from uk_gov_phe_erdst_sc import PayaraConfiguration
from uk_gov_phe_erdst_sc import PayaraApplicationServer
from uk_gov_phe_erdst_sc import JdbcPool
import os


def setup_application_connections():

    keys = {
        'LDAP Service account' : '',
        'AWAG LDAP Host' : '',
        'AWAG Search Bind DN' : '',
        'AWAG LDAP Host Port' : '',
        'AWAG Base DN' : ''
    }
    config = PayaraConfiguration()
    if os.sep == '/':
        config.payara_root_folder = f"{os.getenv('HOME')}{os.sep}awag{os.sep}payara5{os.sep}"
    else:
        config.payara_root_folder = f'C:{os.sep}awag{os.sep}payara5{os.sep}'

    config.domain_name = 'prod'
    config.output_command_to_file = False
    config.execute_command = True
    config.output_to_console = True
    config.recreate_network_listener = False
    config.ldap_bind_password = keys['LDAP Service account']
    config.ldap_host = keys['AWAG LDAP Host']
    config.ldap_search_bind_dn = keys['AWAG Search Bind DN']
    config.ldap_host_port_number = keys['AWAG LDAP Host Port']
    config.ldap_base_dn = keys['AWAG Base DN']
    config.ldap_realm_name = 'ldapRealm'
    config.jdbc_pools = [
        JdbcPool("localhost",
                 "awag",
                 "changeit",
                 "awdatabase",
                 "awDatabase",
                 5432,
                 suffix=config.suffix),
        JdbcPool("localhost",
                 "awag",
                 "changeit",
                 "awauth",
                 "awAuth",
                 5432,
                 authorisation=True,
                 suffix=config.suffix)]
    config.validate()

    payara_server = PayaraApplicationServer(config)
    payara_server.stop_all_domains()
    payara_server.start()
    payara_server.setup()
    payara_server.stop()


setup_application_connections()