import matplotlib.image as mpimg
from os import listdir
from os.path import isfile, join


def read_all_images(path_name):
    """
    obtains all files (images) given a directory name
    """

    file_paths = [join(path_name, f) for f in listdir(path_name)
                  if isfile(join(path_name, f)) & f.endswith(".png")]
    images = [read_image(file_path) for file_path in file_paths]
    return images


def read_image(file_path):
    return mpimg.imread(file_path)
