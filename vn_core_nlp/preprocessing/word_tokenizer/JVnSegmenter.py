from subprocess import Popen
import os


path = os.path.dirname(os.path.realpath(__file__)) + '/src/JVnSegmenter'


class JVnSegmenter(object):
    def tokenize_file(self, input_dir):
        Popen(['java', '-classpath', 'dist/JVnSegmenter.jar', 'JVnSegmenter.JVnSegmenter', '-modeldir', './models', '-inputfile', input_dir], cwd=path).communicate()

    def tokenize_directory(self, input_dir):
        Popen(['java', '-classpath', 'dist/JVnSegmenter.jar', 'JVnSegmenter.JVnSegmenter', '-modeldir', './models', '-inputfile', input_dir], cwd=path).communicate()