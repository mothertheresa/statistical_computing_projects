import numpy as np
import mathUtility as mU


class SpeedCalculator:
    """
    calculates the speed in miles per hour given anomalies by tile
    and instantiated with a particular video and config
    """

    def __init__(self, video, config):
        self.video = video
        self.config = config

    def evaluate_speed(self, anomalies_by_tile):
        first_firing_indices = np.array([])

        for tileIdx in range(0, self.config.tileNum):
            anomalies = np.where(anomalies_by_tile[tileIdx]['y_pred_test'] > 0.6)[0] # todo: magic number
            if len(anomalies) > 0:
                first_firing_indices = np.append(first_firing_indices, anomalies[0])
            else:
                first_firing_indices = np.append(first_firing_indices, 1e6)

        if len(first_firing_indices[first_firing_indices != 1e6]) == 0:
            raise ValueError("No anomalies were detected in your test data. Are you sure a car is passing by?")

        nonnegative_deltas = np.diff(first_firing_indices)[np.diff(first_firing_indices) > 0]

        avg_firing_diff = np.mean(nonnegative_deltas) / self.video.get_meta_data()['fps']
        avg_pix_per_second = self.config.tileWidth / avg_firing_diff
        miles_per_hour = mU.speed_conversion(avg_pix_per_second, self.config.inchesPerPixel)

        return miles_per_hour
