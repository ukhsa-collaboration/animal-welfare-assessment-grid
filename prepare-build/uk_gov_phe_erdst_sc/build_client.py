from uk_gov_phe_erdst_sc.utils import FileUtils
from .build import Build
from .build_configuration import BuildConfiguration
import os

class BuildClientConfiguration(BuildConfiguration):

    def __init__(self):
        super().__init__()


    def validate(self):
        assert self.source_folder is not None
        return super().validate()


    """ Public Members """
    tokens_before_build = None
    tokens_after_build = None
    source_folder = None
    compiled_build_folder = None


class BuildClient(Build):

    configuration = None

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
        if self.configuration.tokens_after_build:
            self._tokens(self.configuration.tokens_after_build, False)


    def copy_build(self):
        _source_file_basename = os.path.basename(self.configuration.compiled_build_folder)
        _destination_file = f'{self.configuration.build_parent_path}{os.sep}{_source_file_basename}'
        FileUtils.copy_tree(self.configuration.compiled_build_folder, _destination_file)
