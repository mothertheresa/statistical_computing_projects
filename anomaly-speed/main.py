import numpy as np
import dataReader as dR
import config as c
import featureExtractor as fE
import isolationForest as iF
import speedCalculator as sC

# get config & data
video = dR.get_video(c.Input.fileName)
config = c.Config(video)
rng = np.random.RandomState(1)

# find anomalies
print("...beginning featureExtraction")
featureExtraction = fE.FeatureExtractor(video, config)
print("...completing featureExtraction")
print("...beginning isolationForest")
anomalies = iF.IsolationForest(config, featureExtraction, rng).anomalies()
print("...completing isolationForest")

# calculate speed
print("...beginning speedCalculator")
speed = sC.SpeedCalculator(video, config).evaluate_speed(anomalies)

print("That car is going " + str(int(speed)) + " miles per hour.")
