import unittest
import matchingUtility as mU
import featureUtility as fU


class TestColorMatcherMethods(unittest.TestCase):

    # when given a single target color, should return the source with closest color
    def test_single_color(self):
        target_redux_dict = {0: [0, 0, 0]}
        source_redux_dict = {0: [1, 1, 1], 1: [0, 0, 0], 2: [1, 1, 1]}
        result = mU.mosaic_color_matcher(target_redux_dict, source_redux_dict, fU.distance)
        self.assertTrue(result == {0: 1})

    # when given a multiple target colors, should return the sources with closest colors
    def test_multiple_colors(self):
        target_redux_dict = {0: [0, 0, 0], 1: [1, 1, 1]}
        source_redux_dict = {0: [1, 1, 1], 1: [0, 0, 0]}
        result = mU.mosaic_color_matcher(target_redux_dict, source_redux_dict, fU.distance)
        self.assertTrue(result == {0: 1, 1: 0})

    # when given a single target color with multiple matches, should return first one
    def test_multiple_match(self):
        target_redux_dict = {0: [0, 0, 0]}
        source_redux_dict = {0: [0, 0, 0], 1: [0, 0, 0]}
        result = mU.mosaic_color_matcher(target_redux_dict, source_redux_dict, fU.distance)
        self.assertTrue(result == {0: 0})

    # when given no data, returns empty dictionary
    def test_no_data(self):
        target_redux_dict = {}
        source_redux_dict = {0: [0, 0, 0], 1: [0, 0, 0]}
        result = mU.mosaic_color_matcher(target_redux_dict, source_redux_dict, fU.distance)
        self.assertTrue(result == {})

    # when given invalid object instead of dictionary of data
    def test_invalid_data(self):
        with self.assertRaises(TypeError):
            target_redux_dict = "string is an invalid input"
            source_redux_dict = {0: [0, 0, 0], 1: [0, 0, 0]}
            mU.mosaic_color_matcher(target_redux_dict, source_redux_dict, fU.distance)

    # when given invalid distance function
    def test_invalid_distance(self):
        with self.assertRaises(TypeError):
            target_redux_dict = {0: [0, 0, 0]}
            source_redux_dict = {0: [0, 0, 0], 1: [0, 0, 0]}
            mU.mosaic_color_matcher(target_redux_dict, source_redux_dict, "invalid distance")

    # when given two identical RGB values, distance should be zero
    def test_distance_for_identical(self):
        rgb1 = [0.5, 0.5, 0.5]
        rgb2 = [0.5, 0.5, 0.5]
        result = fU.distance(rgb1, rgb2)
        self.assertTrue(result == 0)

    # when given two sets of RGB values, closer ones should have smaller distance
    def test_distance_for_different(self):
        rgb1 = [0.5, 0.5, 0.5]
        rgb2 = [0.7, 0.7, 0.7]
        rgb3 = [0, 0, 0]  # further away
        d1 = fU.distance(rgb1, rgb2)
        d2 = fU.distance(rgb1, rgb3)
        self.assertTrue(d1 < d2)

    # when given two sets of colors of different lengths, should throw error
    def test_invalid_colors(self):
        with self.assertRaises(ValueError):
            rgb1 = [0.5, 0.5, 0.5]
            rgb2 = [0.5, 0.5]
            fU.distance(rgb1, rgb2)


if __name__ == '__main__':
    unittest.main()
