import unittest
import numpy as np
import config as c
import isolationForestUtility as iFU
import speedCalculator as sC


class TestAnomalySpeedMethods(unittest.TestCase):

    class TestVideo:
        def get_meta_data(self):
            return {'nframes': 10, 'fps': 50}

        def get_data(self, n):
            if n == 1:
                return np.array([[[10], [10], [10], [10], [10], [10]],
                                 [[10], [10], [10], [10], [10], [10]],
                                 [[10], [10], [10], [10], [10], [10]],
                                 [[10], [10], [10], [10], [10], [10]],
                                 [[10], [10], [10], [10], [10], [10]],
                                 [[10], [10], [10], [10], [10], [10]],
                                 [[10], [10], [10], [10], [10], [10]],
                                 [[10], [10], [10], [10], [10], [10]],
                                 [[10], [10], [10], [10], [10], [10]],
                                 [[10], [10], [10], [10], [10], [10]],
                                 [[10], [10], [10], [10], [10], [10]],
                                 [[10], [10], [10], [10], [10], [10]],
                                 [[10], [10], [10], [10], [10], [10]]])

    video = TestVideo
    config = c.Config(video)

    testData = [
        [100, 200, 300],
        [100, 200, 300],
        [102, 198, 301],
        [99,  199, 299],
        [100, 200, 300],
        [300, 200, 100],
        [105, 200, 300],
        [107, 202, 304],
        [111, 198, 301],
        [88,  169, 229],
        [100, 260, 380],
        [305, 203, 106],
        [100, 200, 300],
        [100, 200, 300],
        [100, 200, 300],
        [100, 200, 300]
    ]

    subsamplingSize = 5
    numberOfTrees = 100
    rng = np.random.RandomState(1)

    # train forest
    forest = iFU.build_isolation_forest(testData, numberOfTrees, subsamplingSize, rng)

    # when tree given an anomaly, should return score >0.5
    def test_detect_anomaly(self):
        newDatum = [0, 0, 0]
        score = iFU.evaluate_isolation_forest([newDatum], self.forest, self.subsamplingSize, len(self.testData))
        print(score)
        self.assertTrue(score > 0.5)

    # when tree given a non-anomaly, should return score <0.5
    def test_detect_non_anomaly(self):
        newDatum = [100, 200, 300]
        score = iFU.evaluate_isolation_forest([newDatum], self.forest, self.subsamplingSize, len(self.testData))
        print(score)
        self.assertTrue(score < 0.5)

    # when given data with no firings, should ask whether there was a car
    def test_no_car(self):
        anomalies = [{'y_pred_test': np.array([0.0, 0.0, 0.0])},
                     {'y_pred_test': np.array([0.0, 0.0, 0.0])},
                     {'y_pred_test': np.array([0.0, 0.0, 0.0])},
                     {'y_pred_test': np.array([0.0, 0.0, 0.0])},
                     {'y_pred_test': np.array([0.0, 0.0, 0.0])},
                     {'y_pred_test': np.array([0.0, 0.0, 0.0])}]
        with self.assertRaises(ValueError):
            sC.SpeedCalculator(self.video, self.config).evaluate_speed(anomalies)

    # when given data with firings, should calculate non-zero speed
    def test_some_car(self):
        anomalies = [{'y_pred_test': np.array([1.0, 1.0, 1.0])},
                     {'y_pred_test': np.array([1.0, 1.0, 0.0])},
                     {'y_pred_test': np.array([0.0, 1.0, 1.0])},
                     {'y_pred_test': np.array([0.0, 1.0, 1.0])},
                     {'y_pred_test': np.array([0.0, 0.0, 1.0])},
                     {'y_pred_test': np.array([0.0, 0.0, 1.0])}]
        speed = sC.SpeedCalculator(self.video, self.config).evaluate_speed(anomalies)
        self.assertTrue(speed > 0.0)

    # when given data with missing firings, should ignore negative deltas
    def test_missing_firings(self):
        anomalies = [{'y_pred_test': np.array([1.0, 1.0, 1.0])},
                     {'y_pred_test': np.array([0.0, 0.0, 0.0])},
                     {'y_pred_test': np.array([0.0, 1.0, 1.0])},
                     {'y_pred_test': np.array([0.0, 1.0, 1.0])},
                     {'y_pred_test': np.array([0.0, 0.0, 1.0])},
                     {'y_pred_test': np.array([0.0, 0.0, 1.0])}]
        speed = sC.SpeedCalculator(self.video, self.config).evaluate_speed(anomalies)
        self.assertTrue(speed > 0.0)


if __name__ == '__main__':
    unittest.main()
