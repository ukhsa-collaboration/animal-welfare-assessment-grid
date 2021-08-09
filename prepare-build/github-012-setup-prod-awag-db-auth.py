from uk_gov_phe_erdst_sc import PayaraConfiguration
from uk_gov_phe_erdst_sc import PayaraApplicationServer
from uk_gov_phe_erdst_sc import JdbcPool
import os



def setup_application_connections():

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