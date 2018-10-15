import numpy as np


class Input:
    """
    input parameters, currenty only includes
    data file to use for the application to run on
    """

    fileName = 'data/cars.mp4'


class Config:  # todo: future extensions for this project would be building beautiful GUI for input/config
    """
    global configuration parameters including:
    - number of tiles
    - tile dimensions
    - tile locations
    - estimate of inches per pixel
    - frames to use for TRAINING
    - frames to use for TESTING
    """

    def __init__(self, video):
        self.nframes = video.get_meta_data()['nframes'] #313
        self.trainRange = [232, self.nframes]  # no cars passing, 232 onwards
        self.testRange = [10, 70]  # a single car passes, ~20 - 60

        shape = video.get_data(1).shape
        print(shape)
        self.imageHeight = shape[0]
        self.imageWidth = shape[1]
        self.pixelDim = shape[2]

        self.tileHeightMin = 280  # this places the tile on the farther lane
        self.tileHeightMax = 320
        self.tileNum = 6
        self.tileWidth = int(np.floor(self.imageWidth / self.tileNum))
        self.tilePixelCount = (self.tileHeightMax - self.tileHeightMin) * self.tileWidth
        self.inchesPerPixel = 71 / self.tileWidth
        # 71 in. ~ 40% of the length of a honda civic hatchback (177.9 in.)
