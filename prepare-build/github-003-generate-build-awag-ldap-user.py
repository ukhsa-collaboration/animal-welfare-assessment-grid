# 1. Install the database schema
# 2. Generate a local build of the AWAG applications for LDAP Authenticated users
# 3. Once the build has deployed http://localhost/animal-welfare-system-client

from build_awag import BuildAWAG
from build_awag import BuildSummaryConfigurationAWAG
from build_awag import ConstantsAWAG
from uk_gov_phe_erdst_sc import Constants
import os


def ldap_user():
    code_path = os.path.abspath(f'{os.getcwd()}{os.sep}..{os.sep}code')
    build_path = os.path.abspath(f'{os.getcwd()}{os.sep}..{os.sep}build')
    configuration_path = os.path.abspath(f'{os.getcwd()}{os.sep}..{os.sep}configuration')

    config = BuildSummaryConfigurationAWAG()
    config.client_part_url = ''
    config.install_basic_template = True
    config.aw_database_name = 'awdatabase'
    config.aw_auth_database_name = 'awauth'
    config.awag_user = 'awag'
    config.new_jta_aw_source_name = 'jdbc/awDatabase'
    config.new_jta_awauth_source_name = 'jdbc/awAuth'
    config.awag_schema = 'awag_schema'
    config.awag_auth_schema = 'awag_auth_schema'
    config.http_front_end_scheme = 'http'
    config.http_front_end_hostname = 'localhost'
    config.database_port = 5432
    #config.http_front_end_port = 80
    config.builds_folder = build_path
    config.server_source_folder = f"{code_path}{os.sep}server"
    config.client_source_folder = f"{code_path}{os.sep}client"
    config.database_source_folder = configuration_path
    config.web_xml_realm_name = 'ldapRealm'
    config.web_xml_auth_type = 'ldap'
    config.security_role_to_groups = {
        'assessmentuser': 'your.LDAP.group.here' }
    config.validate()
    BuildAWAG(config).build()


def prerequisites():
    import subprocess
    import sys
    if sys.version_info.major >= 3:
        if sys.version_info.minor < 7:
            raise Exception()
    output = subprocess.run([Constants.WHICH, Constants.PSQL], capture_output=True).stdout
    if len(output) > 0:
        print(output)
    output = subprocess.run([Constants.WHICH, Constants.MAVEN], capture_output=True).stdout
    if len(output) > 0:
        print(output)
    output = subprocess.run([Constants.WHICH, Constants.GIT], capture_output=True).stdout
    if len(output) > 0:
        print(output)


prerequisites()
ldap_user()