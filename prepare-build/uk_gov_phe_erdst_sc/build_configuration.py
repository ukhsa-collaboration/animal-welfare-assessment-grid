from .configuration import Configuration
import os

class BuildConfiguration(Configuration):

    def __init__(self):
        super().__init__()


    def get_build_name(self, project_type):
        assert project_type is not None

        _formattedDate = Configuration.getFormattedDate()
        return f'{project_type}-{_formattedDate}'


    def get_build_path(self, target_folder, project_name, build_name):
        assert target_folder is not None
        assert project_name is not None
        assert build_name is not None

        _formattedMonthYear = Configuration.getFormattedMonthYear()
        return f'{target_folder}{os.sep}{project_name}{os.sep}{_formattedMonthYear}{os.sep}{build_name}'


    environment_production = False
    environment_staging = False
    build_parent_path = None
    build_name = None
