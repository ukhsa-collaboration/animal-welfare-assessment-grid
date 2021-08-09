from uk_gov_phe_erdst_sc import PayaraConfiguration
from uk_gov_phe_erdst_sc import PayaraApplicationServer
import os
import urllib.request


def download_postgres_jar():
    urllib.request.urlretrieve('https://jdbc.postgresql.org/download/postgresql-42.2.20.jar', filename='postgresql-42.2.20.jar')


def make_domain():

    """ Setup the integration-tests Payara domain for  testing """

    config = PayaraConfiguration()
    if os.sep == '/':
        config.payara_root_folder = f"{os.getenv('HOME')}{os.sep}awag{os.sep}payara5{os.sep}"
    else:
        config.payara_root_folder = f'C:{os.sep}awag{os.sep}payara5{os.sep}'

    config.domain_name = 'prod'
    config.output_command_to_file = False
    config.execute_command = True
    config.output_to_console = True
    config.file_jdbc_jar_path = f'postgresql-42.2.20.jar'
    config.validate()

    payara_server = PayaraApplicationServer(config)
    payara_server.recreate()


download_postgres_jar()
make_domain()
