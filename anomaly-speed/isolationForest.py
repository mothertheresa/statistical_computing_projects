import numpy as np
import isolationForestUtility as iFU


class IsolationForest:
    """
    class that organizes testing and training isolation forests on tiles for anomaly
    detection; instantiated with a Config, random state, and a feature extraction
    """

    def __init__(self, config, feature_extraction, random_state):
        self.config = config
        self.featureExtraction = feature_extraction
        self.randomState = random_state

    def train_isolation_forest(self, tile):
        """
        given a tile, returns the isolation forest (and other meta data) trained on that
        tile using configuration parameters defined in Config
        """

        print("...beginning trainIsolationForest for tile:" + str(tile))
        X_train = self.featureExtraction.get_feature_tile_frames(
            tile, self.config.trainRange[0], self.config.trainRange[1]
        )

        training_data_size = len(X_train)
        subsampling_size = min(256, training_data_size)
        number_of_trees = 100
        forest = iFU.build_isolation_forest(X_train, number_of_trees, subsampling_size, rng=self.randomState)

        return {'X_train': X_train, 'tileIdx': tile,
                'training_data_size': training_data_size,
                'subsampling_size': subsampling_size,
                'isolation_forest': forest}

    def test_isolation_forest(self, tile, forest, subsampling_size, training_data_size):
        """
        given a tile and forest, returns the predicted anomaly scores
        """

        X_test = self.featureExtraction.get_feature_tile_frames(
            tile, self.config.testRange[0], self.config.testRange[1]
        )
        y_pred_test = iFU.evaluate_isolation_forest(X_test, forest, subsampling_size, training_data_size)

        return {'X_test': X_test, 'y_pred_test': y_pred_test}

    def anomalies(self):
        """
        calculates all of the anomalies across the entire set of tiles in Config
        """

        isolation_forests_by_tile = [self.train_isolation_forest(tileIdx)
                                     for tileIdx in range(0, self.config.tileNum)]

        result = [self.test_isolation_forest(tileIdx,
                                             isolation_forests_by_tile[tileIdx]['isolation_forest'],
                                             isolation_forests_by_tile[tileIdx]['subsampling_size'],
                                             isolation_forests_by_tile[tileIdx]['training_data_size'])
                  for tileIdx in range(0, self.config.tileNum)]
        return np.array(result)
