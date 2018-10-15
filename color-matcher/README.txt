Welcome to color-matcher!

This program creates an image mosaic for a target image from source images.

USER:
You can configure the directory with the source images and the path for the target image.
At the beginning of main.py, you can also specify how many 'pixels' you want to target image to become.

DEVS:
The primary files in the program are main.py, which runs the entire program, and config.py, which provides configurations.
It is useful to look at this first to see how the program is organized. It lays out the following steps:
- read in the data
- split the target image into desired 'pixels'
- extract the feature (e.g. average RGB value) from the source images and the target
- match the feature of each target pixel to a source image
- prepare for rendering by reshaping the array of source images
- render! (this is the most computationally intensive, since I haven't reduced image size yet)

Tests are contained in:
- tests

Important functions:
- FEATURE for extracting a feature from an image is `reduce_image` in featureUtility.py
- DISTANCE for matching is defined as `distance` in featureUtility.py
- MATCHING calls the distance function within `mosaic_color_matcher` in matchingUtility.py