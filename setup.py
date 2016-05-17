from setuptools import setup, find_packages

packages = find_packages()
setup(name='vn_core_nlp',
      version='0.1.2',
      description='The python package for vietnamese text processing',
      author='Hoang Pham',
      author_email='phamthaihoang.hn@gmail.com',
      packages=packages,
      include_package_data=True,
      zip_safe=False)
