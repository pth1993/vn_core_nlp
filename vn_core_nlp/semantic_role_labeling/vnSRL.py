from subprocess import Popen
import os


path = os.path.dirname(os.path.realpath(__file__)) + '/src/vnSRL-1.0.0'


class vnSRL(object):
    def predict(self, input_dir, output_dir, ilp, embedding):
        Popen(['python', 'vnSRL.py',  input_dir, ilp, embedding, output_dir], cwd=path).communicate()
