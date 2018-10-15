import numpy as np


def split_target(target_image, num_rows, num_cols):
    """
    splits a target_image into 'pixels', which are sub-images when the image is split
    into num_row and num_cols
    """
    num_row_pixels = target_image.shape[0] / num_rows
    num_col_pixels = target_image.shape[1] / num_cols

    result = [target_image[(num_col_pixels * column):(num_col_pixels * column + num_col_pixels),
              (num_row_pixels * row):(num_row_pixels * row + num_row_pixels)]
              for row in range(0, num_rows)
              for column in range(0, num_cols)]

    return np.array(result)


def to_dict(images):
    """
    given set of images, returns index to image dictionary
    """
    return dict(zip(range(0, len(images)), images))


def reshape_to_image(matches_dict, source_images_dict, num_rows, num_cols):
    """
    given set of images, returns index to image dictionary
    """
    total_width = num_cols * 100
    total_height = num_rows * 100
    result = np.array([]).reshape(total_width, 0, 3)

    for row in range(0, total_height):
        row_batch = row / 100
        row_result = np.array([]).reshape(0, 3)
        for index in reversed(range(row_batch * num_rows, row_batch * num_rows + num_rows)):
            source = matches_dict[index]
            image_data = source_images_dict[source][row % 100]
            row_result = np.append(image_data, row_result, axis=0)
        new_row = row_result.reshape(total_width, 1, 3)
        result = np.concatenate((result, new_row), axis=1)

    return result
