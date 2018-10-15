Welcome to anomaly-speed!

This program calculates the speed of a car in a segment of video footage.

USER:
You can configure (1) which video file to use, (2) which frames of the video to train on, (3) which frames of the video to calculate speed on, (3) the size and location of the tiles, and (4) the number of tiles in the config.py file. Then simply run the program using the run script, and run tests using the run-tests script.

DEVS:
The primary files in the program are main.py, which runs the entire program, and config.py, which provides configurations. It is useful to look at this first to see how the program is organized. It calls the different modules:
- isolationForest (the core implementation to enable anomaly detection)
- featureExtractor (which features to extract from the video and train the tree on)
- speedCalculator (performs the final conversion from “anomalies” to speed)

Tests are contained in:
- tests

All other files are utility files, including:
- dataReader
- isolationForestUtility
- mathUtility
- visualizationTools