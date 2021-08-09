import os
import shutil
from .constants import Constants

class LoggingUtils:

    @staticmethod
    def log(logging_text):
        print(f'>>>> {logging_text}')

class FileUtils:

    @staticmethod
    def is_windows():
        return os.sep == '/'


    @staticmethod
    def make_folders(destination_folder):
        assert destination_folder is not None
        try:
            LoggingUtils.log("> (Create) " + destination_folder)
            os.makedirs(destination_folder, exist_ok=True)
        finally:
            pass


    @staticmethod
    def chdir(destination_folder):
        assert destination_folder is not None
        os.chdir(destination_folder)


    @staticmethod
    def remove_file(remove_filename):
        assert(remove_filename is not None)
        if (os.path.exists(remove_filename)):
            os.remove(remove_filename)


    @staticmethod
    def copyfile(source_path, destination_path):
        shutil.copyfile(source_path, destination_path)


    @staticmethod
    def copy(source_path, destination_path):
        shutil.copy(source_path, destination_path)


    @staticmethod
    def copy_tree(source_path, destination_path):
        _unc_prefix = '' if FileUtils.is_windows() else Constants.UNC_PREFIX
        shutil.copytree(Constants.UNC_PREFIX + source_path, Constants.UNC_PREFIX + destination_path)


    @staticmethod
    def move(source_path, destination_path):
        shutil.move(source_path, destination_path)


    @staticmethod
    def remove(destination_path):
        print(destination_path)
        shutil.rmtree(destination_path)


    @staticmethod
    def get_content_between_tags(file_to_edit,
                                  start_tag,
                                  end_tag):
        file_data = None
        with open(file_to_edit, 'r') as f:
            file_data = f.read()
            f.close()

        start_index = file_data.find(start_tag)
        if (start_index < 0):
            raise Exception
        end_index = file_data.find(end_tag, start_index)
        if (start_index < 0):
            raise Exception

        old_text = file_data[start_index:(end_index + len(end_tag))]
        f.close()
        return old_text


    @staticmethod
    def replace_text(string_text,
                     start_tag,
                     end_tag,
                     replace_text):
        start_index = string_text.find(start_tag)
        if (start_index < 0):
            raise Exception
        end_index = string_text.find(end_tag, start_index)
        if (end_index < 0):
            raise Exception
        old_text = string_text[start_index:(end_index + len(end_tag))]
        file_data = string_text.replace(old_text, replace_text)
        return file_data


    @staticmethod
    def replace_text_between_tags(file_to_edit,
                                  start_tag,
                                  end_tag,
                                  code_to_insert,
                                  utf8_encoding=False,
                                  raise_exceptions=False):

        #LoggingUtils.log("----- Replacing Text Start")
        #LoggingUtils.log("-----")
        #LoggingUtils.log("> (Edit file): " + file_to_edit)
        #LoggingUtils.log("> (Start Tag): " + start_tag)
        #LoggingUtils.log("> (End Tag): " + end_tag)

        file_data = None
        with open(file_to_edit, 'r') as f:
            file_data = f.read()
            f.close()

        #LoggingUtils.log("-----")
        #LoggingUtils.log(file_data)
        #LoggingUtils.log("-----")

        start_index = file_data.find(start_tag)
        if start_index < 0:
            if raise_exceptions:
                raise Exception
            else:
                return None
        end_index = file_data.find(end_tag, start_index)
        if end_index < 0 and raise_exceptions:
            if raise_exceptions:
                raise Exception
            else:
                return None

        #LoggingUtils.log("> (Start Index) " + str(start_index))
        #LoggingUtils.log("> (End Index) " + str(end_index))
        old_text = file_data[start_index:(end_index + len(end_tag))]
        file_data = file_data.replace(old_text, code_to_insert)

        _temp_new_file = file_to_edit + ".new"
        if (os.path.exists(_temp_new_file)):
            os.remove(_temp_new_file)

        if (utf8_encoding):
            with open(_temp_new_file, 'wb') as f:
                encoded_file_data = file_data.encode('utf-8-sig')
                f.write(encoded_file_data)
                f.flush()
                f.close()
        else:
            with open(_temp_new_file, 'w') as f:
                f.write(file_data)
                f.flush()
                f.close()

        os.replace(_temp_new_file, file_to_edit)
        # LoggingUtils.log("-----")
        # LoggingUtils.log("----- Replacing Text End")


    @staticmethod
    def edit_file(file_to_edit,
                    search_text,
                    replace_text,
                    utf8_encoding=False,
                    raise_exceptions=False):

        file_data = None
        with open(file_to_edit, 'r') as f:
            file_data = f.read()
            f.close()

        start_index = file_data.find(search_text)

        if start_index < 0:
            if raise_exceptions:
                raise Exception
            else:
                return None

        end_index = start_index + len(search_text)
        if end_index < 0:
            if raise_exceptions:
                raise Exception
            else:
                return None

        old_text = file_data[start_index:end_index]
        file_data = file_data.replace(old_text, replace_text)

        _temp_new_file = file_to_edit + ".tmp"
        if (os.path.exists(_temp_new_file)):
            os.remove(_temp_new_file)

        if (utf8_encoding):
            with open(_temp_new_file, 'wb') as f:
                encoded_file_data = file_data.encode('utf-8-sig')
                f.write(encoded_file_data)
                f.flush()
                f.close()
        else:
            with open(_temp_new_file, 'w') as f:
                f.write(file_data)
                f.flush()
                f.close()

        os.replace(_temp_new_file, file_to_edit)