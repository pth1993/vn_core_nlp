from setuptools import setup, find_packages

packages = find_packages()
setup(name='vn_core_nlp',
      version='0.2.3',
      description='The python package for vietnamese text processing',
      author='Hoang Pham',
      author_email='phamthaihoang.hn@gmail.com',
      packages=packages,
      include_package_data=True,
      install_requires=[
          'numpy', 'scipy', 'scikit-learn', 'pandas', 'pulp', 'ete2'
      ],
      url='https://github.com/pth1993/vn_core_nlp',
      zip_safe=False)
