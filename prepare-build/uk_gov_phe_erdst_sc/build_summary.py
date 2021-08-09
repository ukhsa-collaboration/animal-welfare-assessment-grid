from abc import ABC, abstractmethod
from uk_gov_phe_erdst_sc.configuration import Configuration
from .build_configuration import BuildConfiguration
from .build_client import BuildClientConfiguration
import os


class BuildSummaryConfiguration(Configuration):
    builds_folder = None
    server_source_folder = None
    client_source_folder = None
    database_source_folder = None
    security_role_to_groups = None


class BuildSummary(ABC):

    def __init__(self, summary_config = None):
        if summary_config:
            self.summary_config = summary_config
            self.target_folder = self.summary_config.builds_folder
            self.server_source_folder = self.summary_config.server_source_folder
            self.client_source_folder = self.summary_config.client_source_folder
        self._setup_summary()
        self._setup_build_path()


    @abstractmethod
    def build(self):
        raise NotImplementedError()


    @abstractmethod
    def _setup_summary(self):
        raise NotImplementedError()


    def _setup_build_path(self):
        self.build_name = BuildConfiguration().get_build_name(self.project_type)
        self.build_parent_path = BuildClientConfiguration().get_build_path(self.target_folder, self.project_name, self.build_name)


    def _compress_folder(self):
        _target_archive = f'{self.build_parent_path}{os.sep}..{os.sep}{self.build_name}.zip'
        _cmdline = f'7z a -tzip {_target_archive} {self.build_parent_path}'
        os.system(_cmdline)

        if self.make_hash_file:
            _target_hash_file = f'{self.build_parent_path}{os.sep}..{os.sep}{self.build_name}.zip.sha256.txt'
            _cmdline = f'checksum --file={_target_archive} --hashtype=sha256 > {_target_hash_file}'
            os.system(_cmdline)


    environment_production = False
    environment_staging = False
    build_name = None
    build_parent_path = None
    target_folder = None
    build_path = None
    project_type = None
    project_name = None
    make_hash_file = False
    summary_config = None
