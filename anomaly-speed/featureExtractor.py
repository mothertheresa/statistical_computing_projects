import numpy as np


class FeatureExtractor:
    """
    specifies 'feature' to extract from a given tile and image;
    for example, this can be all of the pixel RGB values (no dimension reduction)
    or as simple as average RGB value
    """

    def __init__(self, video, config):
        self.video = video
        self.config = config

    def get_feature(self, tile_idx, image_idx):
        """
        specifies 'feature' to extract from a given tile and image
        """
        img = self.video.get_data(image_idx)

        tile_width_min = tile_idx * self.config.tileWidth
        tile_width_max = min(self.config.imageWidth, (tile_idx + 1) * self.config.tileWidth)

        tile_height_min = self.config.tileHeightMin
        tile_height_max = self.config.tileHeightMax

        mean = np.mean(img[tile_height_min:tile_height_max, tile_width_min:tile_width_max], axis=(0, 1))
        std = np.std(img[tile_height_min:tile_height_max, tile_width_min:tile_width_max],  axis=(0, 1))

        return np.append(mean, std)

    def get_feature_tile_frames(self, tile_idx, start_frame, end_frame):
        """
        aggregates feature extraction for a specified tile across a range of frames
        """
        nested_array = np.array([self.get_feature(tile_idx, imageIdx) for imageIdx in range(start_frame, end_frame)])
        size = nested_array.shape[0]
        length = nested_array.shape[1]
        return nested_array.reshape(size, length)
