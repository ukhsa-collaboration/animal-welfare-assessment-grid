from .build import Build
from .utils import FileUtils, LoggingUtils
from .build_configuration import BuildConfiguration
from .constants import Constants
import os

class BuildServerConfiguration(BuildConfiguration):

    def __init__(self):
        super().__init__()


    def validate(self):
        assert self.source_folder is not None
        assert self.target_folder is not None
        assert self.project_name is not None
        assert self.project_type is not None
        assert self.build_command is not None

        self._setup_build_name()
        self._setup_build_path()
        self._setup_app_bin_path()
        self._setup_archive_path()
        self._validated = True
        super().validate()


    def _setup_build_name(self):
        if self.build_name is None:
            self.build_name = self.get_build_name(self.project_type)


    def _setup_build_path(self):
        if self.build_parent_path is None:
            self.build_parent_path = self.get_build_path(self.target_folder, self.project_name, self.build_name)


    def _setup_app_bin_path(self):
        self.app_bin_path = f'{self.source_folder}{os.sep}target'


    def _setup_archive_path(self):
        assert self.build_name is not None
        self.archive_war_file = f'{self.app_bin_path}{os.sep}{self.project_name}.war'


    """ Public Members """
    persistence_unit_name = None
    persistence_jta_source_name = None

    source_folder = None
    target_folder = None
    project_name = None
    project_type = None
    persistence = None
    web_xml_realm_name = Constants.Payara.DEFAULT_JDBC_REALM
    web_xml_auth_type = Constants.AUTHTYPE_DATABASE
    tokens_before_build = None
    tokens_after_build = None
    environment_production = False
    environment_staging = False
    security_role_to_groups = None

    """ Members updated on validation """
    build_command = None
    persistence_xml_filename = None
    persistence_tokens = None
    app_bin_path = None
    archive_war_file = None


class BuildServer(Build):

    def __init__(self, configuration):
        super().__init__(configuration)


    def before_build(self):
        self._create_build_version_folder()
        if self.configuration.tokens_before_build:
            self._setup_tokens_before_build()


    def build(self):
        self.before_build()
        self.build_application()
        self.after_build()
        self.clean_up_files()


    def after_build(self):
        if self.configuration.tokens_after_build:
            self._setup_tokens_after_build()
        self.copy_build()


    def clean_up_files(self):
        if self.configuration.tokens_before_build:
            self._tokens(self.configuration.tokens_before_build, False)
        if self.configuration.persistence:
            self._persistence_tokens_2(self.configuration.persistence, False)
        if self.configuration.tokens_after_build:
            self._tokens(self.configuration.tokens_after_build, False)
        if self.configuration.web_xml_realm_name:
            self._web_xml_tokens(False)
        if self.configuration.security_role_to_groups:
            self._glassfish_xml_tokens(False)


    def copy_build(self):
        assert self.configuration.archive_war_file is not None
        assert self.configuration.build_parent_path is not None
        FileUtils.copy(self.configuration.archive_war_file, self.configuration.build_parent_path)


    def _setup_tokens_before_build(self):
        super()._setup_tokens_before_build()
        if self.configuration.persistence:
            self._persistence_tokens_2(self.configuration.persistence)
        if self.configuration.web_xml_realm_name:
            self._web_xml_tokens()
        if self.configuration.security_role_to_groups:
            self._glassfish_xml_tokens()


    def _persistence_tokens_2(self, tokens, replace_tokens=True):
        _persistence_xml_path = f'{self.configuration.source_folder}{os.sep}{Constants.PERSISTENCE_XML_FILE}'
        self._revert_git_file(_persistence_xml_path)

        if replace_tokens:
            for key in tokens:
                _token = tokens[key]
                _persistence_start_token = f'<persistence-unit name="{key}\">'
                _persistence_end_token = '</persistence-unit>'
                _persistence_unit = FileUtils.get_content_between_tags(_persistence_xml_path, _persistence_start_token, _persistence_end_token)

                if _persistence_unit.find('<!-- BUILD_TOKEN: Persistence') >= 0:
                    _persistence_unit = FileUtils.replace_text(_persistence_unit, '<!-- BUILD_TOKEN: Persistence', '-->', Constants.PERSISTENCE_DEFAULT)
                    _persistence_unit = FileUtils.replace_text(_persistence_unit, '<!--', '-->', '')
                else:
                    _persistence_unit = FileUtils.replace_text(_persistence_unit, '<properties>', '</properties>', Constants.PERSISTENCE_DEFAULT)

                if tokens[key]['old-jta-source-name'] and tokens[key]['new-jta-source-name']:
                    _old_jta_source = _token['old-jta-source-name']
                    _new_jta_source = _token['new-jta-source-name']
                    _new_jta_source_old_token = f'<jta-data-source>{_old_jta_source}'
                    _new_jta_source_end_token = f'</jta-data-source>'
                    _new_jta_source_text = f'<jta-data-source>{_new_jta_source}</jta-data-source>'
                    _persistence_unit = FileUtils.replace_text(_persistence_unit, _new_jta_source_old_token, _new_jta_source_end_token, _new_jta_source_text)

                FileUtils.replace_text_between_tags(
                    _persistence_xml_path,
                    _persistence_start_token,
                    _persistence_end_token,
                    _persistence_unit)


    def _web_xml_tokens(self, replace_tokens=True):
        _web_xml_path = f'{self.configuration.source_folder}{os.sep}{Constants.WEB_XML_FILE}'
        self._revert_git_file(_web_xml_path)

        if replace_tokens:
            FileUtils.replace_text_between_tags(_web_xml_path, '<!--', '-->', '', False, True)
            _tokens = {
                'param-value' : self.configuration.web_xml_auth_type,
                'realm-name' : self.configuration.web_xml_realm_name }
            self._xml_tokens(_web_xml_path, _tokens)


    def _glassfish_xml_tokens(self, replace_tokens=True):
        _glassfish_web_xml_path = f'{self.configuration.source_folder}{os.sep}{Constants.GLASSFISH_WEB_XML_FILE}'
        self._revert_git_file(_glassfish_web_xml_path)

        _new_xml = ''
        for key in self.configuration.security_role_to_groups:
            _new_xml += self._get_security_role_mapping(key, self.configuration.security_role_to_groups[key])

        _new_xml = f'<glassfish-web-app>\n{_new_xml}</glassfish-web-app>\n'

        if replace_tokens:
            FileUtils.replace_text_between_tags(_glassfish_web_xml_path, '<!--', '-->', '', False, True)
            _tokens = { 'glassfish-web-app' : _new_xml }
            self._xml_tokens(_glassfish_web_xml_path, _tokens)


    def _xml_tokens(self, _file_path, _tokens):
        for _key in _tokens:
            _start_token = f'<{_key}>'
            _end_token = f'</{_key}>'
            _replace_text = f'{_start_token}{_tokens[_key]}{_end_token}'
            FileUtils.replace_text_between_tags(_file_path, _start_token, _end_token, _replace_text)


    def _move_working_tree_file(self, move_pathname):
        assert move_pathname is not None
        _backup_filename = f'{self.configuration.build_parent_path}{os.sep}' + os.path.basename(move_pathname)
        LoggingUtils.log(_backup_filename)
        FileUtils.remove_file(_backup_filename)
        FileUtils.move(move_pathname, _backup_filename)


    def _get_security_role_mapping(self, role_name, group_name):
        return '\t<security-role-mapping>\n' + \
               f'\t\t<role-name>{role_name}</role-name>\n' + \
               f'\t\t<group-name>{group_name}</group-name>\n' + \
               '\t</security-role-mapping>\n'
