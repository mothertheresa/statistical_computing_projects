import numpy as np
import mathUtility as mU


def reduce_image(image):
    """
    reduces an image to a measure (in this case, average RGB value)
    """
    return np.mean(np.mean(image, axis=0), axis=0)


def distance(rgb1, rgb2):
    """
    defines distance measure between two rgb values
    """
    yuv1 = mU.rgb_to_yuv(rgb1)
    yuv2 = mU.rgb_to_yuv(rgb2)

    return np.linalg.norm(yuv1-yuv2)


def calc_target_redux(pixels):
    """
    applies reduction to each pixel in target image
    returns a dictionary from pixel index to reduction value
    """
    target_redux_dict = {}
    for pixel_index in range(0, pixels.shape[0]):
        target_redux_dict[pixel_index] = reduce_image(pixels[pixel_index])
    return target_redux_dict


def calc_source_redux(source_images_dict):
    """
    applies reduction to each source image
    returns a dictionary from source image index to reduction value
    """
    source_redux_dict = {}
    for key, image in source_images_dict.items():
        source_redux_dict[key] = reduce_image(image)
    return source_redux_dict
