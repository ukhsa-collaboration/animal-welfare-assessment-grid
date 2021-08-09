import subprocess
from uk_gov_phe_erdst_sc.postgres_database import PostgresDatabase
from uk_gov_phe_erdst_sc.constants import Constants
from uk_gov_phe_erdst_sc.build_summary import BuildSummaryConfiguration
from uk_gov_phe_erdst_sc import BuildSummary
from uk_gov_phe_erdst_sc import BuildServerConfiguration
from uk_gov_phe_erdst_sc import BuildServer
from uk_gov_phe_erdst_sc import BuildClient
from uk_gov_phe_erdst_sc import BuildClientConfiguration
from uk_gov_phe_erdst_sc import BuildSummaryConfiguration
from uk_gov_phe_erdst_sc import FileUtils
from uk_gov_phe_erdst_sc import PostgresDatabase
import os

class ConstantsAWAG:
    DEFAULT_AWDATABASE = 'jdbc/awDatabase'
    DEFAULT_AWAUTHDATABASE = 'jdbc/awAuth'
    DEFAULT_AWAG_SCHEMA = 'awag_schema'
    DEFAULT_AWAG_AUTH_SCHEMA = 'awag_auth_schema'
    DEFAULT_AWAG_SUFFIX = ''
    DEFAULT_CLIENT_PART_URL = ''
    DEFAULT_DATABASE_NAME = 'awdatabase'
    DEFAULT_AUTH_DATABASE_NAME = 'awauth'
    DEFAULT_DATABASE_USER = 'awag'
    DEFAULT_DATABASE_PASSWORD = 'changeit'
    DEFAULT_ADMIN_USER = 'postgres'
    DEFAULT_POSTGRES_PORT = Constants.Postgres.DEFAULT_DB_PORT
    DEFAULT_SECURITY_GROUPS = {
        'admin': 'admin',
        'assessmentuser': 'assessmentuser' }


class BuildSummaryConfigurationAWAG(BuildSummaryConfiguration):
    client_part_url = ConstantsAWAG.DEFAULT_AWDATABASE
    new_jta_aw_source_name = ConstantsAWAG.DEFAULT_AWDATABASE
    new_jta_awauth_source_name = ConstantsAWAG.DEFAULT_AWAUTHDATABASE
    awag_schema = ConstantsAWAG.DEFAULT_AWAG_SCHEMA
    awag_auth_schema = ConstantsAWAG.DEFAULT_AWAG_AUTH_SCHEMA
    http_front_end_scheme = Constants.DEFAULT_HTTP
    http_front_end_hostname = Constants.DEFAULT_HOSTNAME
    http_front_end_port = None
    http_front_end = None
    web_xml_realm_name = Constants.Payara.DEFAULT_JDBC_REALM
    web_xml_auth_type = Constants.AUTHTYPE_DATABASE
    database_port = ConstantsAWAG.DEFAULT_POSTGRES_PORT
    security_role_to_groups = ConstantsAWAG.DEFAULT_SECURITY_GROUPS
    aw_database_name = ConstantsAWAG.DEFAULT_DATABASE_NAME
    aw_auth_database_name = ConstantsAWAG.DEFAULT_AUTH_DATABASE_NAME
    awag_user = ConstantsAWAG.DEFAULT_DATABASE_USER
    install_basic_template = False


    def validate(self):
        self.awag_schema # += self.suffix
        self.awag_auth_schema # += self.suffix
        self.new_jta_aw_source_name += self.suffix
        self.new_jta_awauth_source_name += self.suffix
        self.web_xml_realm_name += self.suffix
        self.aw_database_name += self.suffix
        self.aw_auth_database_name += self.suffix
        self.awag_user += self.suffix
        if self.http_front_end_port:
            self.http_front_end = f'{self.http_front_end_scheme}://{self.http_front_end_hostname}:{self.http_front_end_port}/'
        else:
            self.http_front_end = f'{self.http_front_end_scheme}://{self.http_front_end_hostname}/'


class BuildServerConfigurationAWAG(BuildServerConfiguration):
    suffix = None
    awag_schema = None
    awag_auth_schema = None


class BuildServerAWAG(BuildServer):

    def before_build(self):
        super().before_build()
        if self.configuration.awag_schema or self.configuration.awag_auth_schema:
            self._orm_xml_tokens()


    def clean_up_files(self):
        super().clean_up_files()
        if self.configuration.awag_schema or self.configuration.awag_auth_schema:
            self._orm_xml_tokens(False)


    def _orm_xml_tokens(self, replace_tokens=True):
        _aw_auth_orm_xml_path = f'{self.configuration.source_folder}{os.sep}{Constants.META_INF_FOLDER}awAuthOrm.xml'
        print(_aw_auth_orm_xml_path)
        _aw_database_orm_xml_path = f'{self.configuration.source_folder}{os.sep}{Constants.META_INF_FOLDER}awDatabaseOrm.xml'
        self._revert_git_file(_aw_auth_orm_xml_path)
        self._revert_git_file(_aw_database_orm_xml_path)

        if replace_tokens:
            _tokens = { 'schema' : self.configuration.awag_auth_schema }
            self._xml_tokens(_aw_auth_orm_xml_path, _tokens)
            _tokens = { 'schema' : self.configuration.awag_schema }
            self._xml_tokens(_aw_database_orm_xml_path, _tokens)


class BuildServerAPIDocsAWAG(BuildServer):

    def copy_build(self):
        _api_docs_folder = f'{self.configuration.build_parent_path}{os.sep}api-docs'
        _swagger_json_path = f'{self.configuration.app_bin_path}{os.sep}jaxrs-analyzer{os.sep}swagger.json'
        FileUtils.make_folders(_api_docs_folder)
        FileUtils.copy(_swagger_json_path, _api_docs_folder)


class BuildClientConfigurationAWAG(BuildClientConfiguration):

    def validate(self):
        self._validated = True
        super().validate()


class BuildClientAWAG(BuildClient):

    def build_application(self):
        pass


    def copy_build(self):
        _source_file_basename = os.path.basename(self.configuration.compiled_build_folder)
        _source_path = self.configuration.compiled_build_folder
        _destination_path = f'{self.configuration.build_parent_path}{os.sep}animal-welfare-system-{_source_file_basename}'
        FileUtils.copy_tree(_source_path, _destination_path)


class BuildDatabaseScriptsAWAG(PostgresDatabase):

    build_parent_path = None
    hostname = None
    portname = None
    password = None
    source_folder = None
    _scripts = []
    aw_database_name = ConstantsAWAG.DEFAULT_DATABASE_NAME
    aw_auth_database_name = ConstantsAWAG.DEFAULT_AUTH_DATABASE_NAME
    awag_user = ConstantsAWAG.DEFAULT_DATABASE_USER
    database_port = Constants.Postgres.DEFAULT_DB_PORT
    install_basic_template = False
    execute_command = True
    _commands = []
    suffix = Constants.EMPTY_STRING


    def replace_content(self, script_pathname):
        FileUtils.edit_file(script_pathname, ConstantsAWAG.DEFAULT_AUTH_DATABASE_NAME, self.aw_auth_database_name)
        FileUtils.edit_file(script_pathname, ConstantsAWAG.DEFAULT_DATABASE_NAME, self.aw_database_name)
        FileUtils.edit_file(script_pathname, f'= {ConstantsAWAG.DEFAULT_DATABASE_USER}', f'= {self.awag_user}')
        FileUtils.edit_file(script_pathname, f'USER {ConstantsAWAG.DEFAULT_DATABASE_USER}', f'USER {self.awag_user}')
        FileUtils.edit_file(script_pathname, f' {ConstantsAWAG.DEFAULT_DATABASE_USER};', f' {self.awag_user};')
        FileUtils.edit_file(script_pathname, f'{self.awag_user}_auth_schema', ConstantsAWAG.DEFAULT_AWAG_AUTH_SCHEMA)
        FileUtils.edit_file(script_pathname, f'{self.awag_user}_schema', ConstantsAWAG.DEFAULT_AWAG_SCHEMA)


    def copy_script(self, script_filename):
        _build_db_scripts_folder = f'{self.build_parent_path}{os.sep}database'
        FileUtils.make_folders(_build_db_scripts_folder)
        _source_script_path = f'{self.source_folder}{os.sep}{script_filename}'
        _source_path = f'{_build_db_scripts_folder}{os.sep}{script_filename}'
        FileUtils.copyfile(_source_script_path, _source_path)
        self.replace_content(_source_path)


    def add_admin_script(self, script_filename):
        self.copy_script(script_filename)
        _build_db_scripts_folder = f'{self.build_parent_path}{os.sep}database'
        _source_path = f'{_build_db_scripts_folder}{os.sep}{script_filename}'
        assert os.path.exists(_source_path)
        _item = { 'admin' : True, 'filename' :  _source_path, 'database_name': None }
        self._scripts.append(_item)


    def add_script(self, script_filename, database_name):
        self.copy_script(script_filename)
        _build_db_scripts_folder = f'{self.build_parent_path}{os.sep}database'
        _source_path = f'{_build_db_scripts_folder}{os.sep}{script_filename}'
        assert os.path.exists(_source_path)
        _item = { 'filename' : _source_path, 'database_name' : database_name }
        self._scripts.append(_item)


    def add_psql_command(self, db_user, db_host, db_port, db_name, sql_script_pathname):
        if db_name:
            _cmd = ['psql', f'--username={db_user}', f'--port={db_port}', f'--dbname={db_name}', f'--file={sql_script_pathname}']
        else:
            _cmd = ['psql', f'--username={db_user}', f'--port={db_port}', f'--file={sql_script_pathname}']
        self._commands.append(_cmd)
        if self.execute_command:
            print(_cmd)
            subprocess.call(_cmd)


    def build(self):
        self._create_build_version_folder()
        self.add_admin_script('db-recreate-databases.sql')
        self.add_script('authentication.sql', self.aw_auth_database_name)
        self.add_script('db-init.sql', self.aw_database_name)
        self.add_script('update-awdatabase-r5-r6.sql', self.aw_database_name)
        if self.install_basic_template:
            self.add_script('sample-template-data.sql', self.aw_database_name)

        for _db_item in self._scripts:
            _db_name = _db_item['database_name']
            self.add_psql_command(ConstantsAWAG.DEFAULT_ADMIN_USER, None, self.database_port, _db_name, _db_item['filename'])


    def _create_build_version_folder(self):
        assert self.build_parent_path is not None
        FileUtils.make_folders(self.build_parent_path)


class BuildAWAG(BuildSummary):

    def install_database(self):
        database = BuildDatabaseScriptsAWAG()
        database.database_port = self.summary_config.database_port
        database.build_parent_path = self.build_parent_path
        database.source_folder = self.summary_config.database_source_folder
        database.aw_database_name = self.summary_config.aw_database_name
        database.aw_auth_database_name = self.summary_config.aw_auth_database_name
        database.awag_user = self.summary_config.awag_user
        database.install_basic_template = self.summary_config.install_basic_template
        database.suffix = self.summary_config.suffix
        database.build()


    def client(self):
        _global_config_path = f'js{os.sep}common{os.sep}global-config.js'
        _client_part_url = self.summary_config.client_part_url
        _http_front_end = self.summary_config.http_front_end

        config = BuildClientConfigurationAWAG()
        self._setup_config_defaults(config)
        config.compiled_build_folder = self.summary_config.client_source_folder
        config.source_folder = self.client_source_folder
        print(config.source_folder)

        # Configuration applicable when using a reverse proxy (tested using Apache)
        config.tokens_before_build = {
            _global_config_path :
                { 'serverUrl' : f"serverUrl : '{_http_front_end}{_client_part_url}animal-welfare-system-client/server/'" } }
        config.validate()

        client = BuildClientAWAG(config)
        client.build()


    def server(self):
        _index_html_file = f'src{os.sep}main{os.sep}webapp{os.sep}index.html'
        _client_part_url = self.summary_config.client_part_url
        _http_front_end = self.summary_config.http_front_end
        _web_xml_auth_type = self.summary_config.web_xml_auth_type
        _web_xml_realm_name = self.summary_config.web_xml_realm_name
        _new_jta_aw_source_name = self.summary_config.new_jta_aw_source_name
        _new_jta_awauth_source_name = self.summary_config.new_jta_awauth_source_name

        config = BuildServerConfigurationAWAG()
        self._setup_config_defaults(config)
        config.security_role_to_groups = self.summary_config.security_role_to_groups
        config.suffix = self.summary_config.suffix
        config.awag_auth_schema = self.summary_config.awag_auth_schema
        config.awag_schema = self.summary_config.awag_schema
        config.source_folder = self.server_source_folder
        config.build_command = f'{Constants.MAVEN} clean package -DskipTests -U -q'
        config.tokens_before_build = {
            _index_html_file : {
                'INDEX': f'window.location.assign("{_http_front_end}{_client_part_url}animal-welfare-system-client/index.html");' }
        }
        config.persistence = {
            'aw' :
                { 'old-jta-source-name' : 'jdbc/awDatabase',
                  'new-jta-source-name' : _new_jta_aw_source_name },
            'awauth' :
                { 'old-jta-source-name' : 'jdbc/awAuth',
                  'new-jta-source-name' : _new_jta_awauth_source_name } }
        config.web_xml_auth_type = _web_xml_auth_type
        config.web_xml_realm_name = _web_xml_realm_name
        config.validate()

        api_server = BuildServerAWAG(config)
        api_server.build()


    def api_docs(self):
        config = BuildServerConfigurationAWAG()
        config.project_type = self.project_type
        config.source_folder = self.server_source_folder
        config.target_folder = self.target_folder
        config.build_name = self.build_name
        config.build_parent_path = self.build_parent_path
        config.project_name = self.project_name
        if self.environment_production:
            config.build_command = f'{Constants.MAVEN} compile -Pjax-rs-api-docs-gen -U -q'
        else:
            config.build_command = f'{Constants.MAVEN} compile -Pjax-rs-api-docs-gen -U -q'
        config.update_mail_properties = False
        config.validate()

        api_server = BuildServerAPIDocsAWAG(config)
        api_server.build()


    def build(self):
        self.install_database()
        self.server()
        self.api_docs()
        self.client()


    def _setup_summary(self):
        self.project_name = 'animal-welfare-system'
        self.project_type = 'prod'


    def _setup_config_defaults(self, config):
        config.project_type = self.project_type
        config.project_name = self.project_name
        config.target_folder = self.target_folder
        config.build_name = self.build_name
        config.build_parent_path = self.build_parent_path
        config.environment_production = self.environment_production
        config.environment_staging = self.environment_staging