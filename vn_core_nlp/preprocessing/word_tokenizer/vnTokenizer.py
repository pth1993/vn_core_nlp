from subprocess import Popen
import os


path = os.path.dirname(os.path.realpath(__file__)) + '/src/vn.hus.nlp.tokenizer-4.1.1-bin'


class vnTokenizer(object):
    def tokenize_file(self, input_dir, output_dir, output_type='', underscore='', sd=''):
        Popen(['./vnTokenizer.sh', '-i',  input_dir, '-o', output_dir, output_type, underscore, sd], cwd=path).communicate()

    def tokenize_directory(self, input_dir, output_dir,  output_type='', underscore='', sd='', extension=''):
        Popen(['./vnTokenizer.sh', '-i',  input_dir, '-o', output_dir, output_type, underscore, sd, extension], cwd=path).communicate()