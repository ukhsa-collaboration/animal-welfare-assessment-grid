from abc import ABC, abstractmethod
from .utils import FileUtils, LoggingUtils
import os
import subprocess

class Build(ABC):

    def __init__(self, configuration):
        super().__init__()
        self.configuration = configuration


    @abstractmethod
    def before_build(self):
        raise NotImplementedError()


    @abstractmethod
    def build(self):
        raise NotImplementedError()


    @abstractmethod
    def after_build(self):
        raise NotImplementedError()


    def build_application(self):
        assert self.configuration.source_folder is not None
        assert self.configuration.build_command is not None

        FileUtils.chdir(self.configuration.source_folder)

        if isinstance(self.configuration.build_command, list):
            _cmdline = self.configuration.build_command
        else:
            _cmdline = self.configuration.build_command.split(' ')
        print(_cmdline)
        subprocess.call(_cmdline)


    def _setup_tokens_before_build(self):
        assert self.configuration.tokens_before_build is not None
        self._tokens(self.configuration.tokens_before_build)


    def _setup_tokens_after_build(self):
        assert self.configuration.tokens_after_build is not None
        self._tokens(self.configuration.tokens_after_build)


    def _create_build_version_folder(self):
        assert self.configuration.build_parent_path is not None
        FileUtils.make_folders(self.configuration.build_parent_path)


    def _tokens(self, tokens, replace_tokens=True):
        for _filename in tokens:
            _source_pathname = f'{self.configuration.source_folder}{os.sep}{_filename}'
            self._revert_git_file(_source_pathname)
            if replace_tokens and tokens[_filename]:
                for _token in tokens[_filename]:
                    self._replace_token_filename_content(_source_pathname, _token, tokens[_filename][_token])


    def _replace_token_filename_content(self, source_filename, token, text_to_insert):
        _start_token = f'// BUILD_START_TOKEN: {token}'
        _end_token = f'// BUILD_END_TOKEN: {token}'
        FileUtils.replace_text_between_tags(
            source_filename,
            _start_token,
            _end_token,
            text_to_insert)


    def _revert_git_file(self, git_filename):
        assert git_filename is not None

        FileUtils.chdir(os.path.dirname(git_filename))
        LoggingUtils.log(f'(Git Reset) {git_filename}')

        _cmd_line = 'git checkout --quiet ' + os.path.basename(git_filename)
        subprocess.call(_cmd_line.split(' '))


    """ Public Members """
    configuration = None
