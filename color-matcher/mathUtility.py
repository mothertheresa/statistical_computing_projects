import numpy as np


"""
matrix for rgb to yuv conversion
"""
yuv = np.array([[0.299, 0.587, 0.114],
                [-0.14713, -0.28886, 0.436],
                [0.615, -0.51499, -0.10001]])


def rgb_to_yuv(rgb):
    """
    converts rgb vector to yuv vector
    """
    return yuv.dot(rgb)
