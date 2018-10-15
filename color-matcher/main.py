import config as c
import dataReader as dR
import dataUtility as dU
import featureUtility as fU
import matchingUtility as tU
import vizUtility as vT


# just ensure these are even multiples of the target image dimensions
num_rows = 25
num_cols = 25

print("...collecting data")
source_images_dict = dU.to_dict(dR.read_all_images(c.Input.source_path_name))
target_image = dR.read_image(c.Input.target_file_name)


print("...splitting target into 'pixels'")
pixels = dU.split_target(target_image, num_rows, num_cols)


print("...extracting features from source")
target_redux_dict = fU.calc_target_redux(pixels)
source_redux_dict = fU.calc_source_redux(source_images_dict)


print("...matching 'pixel' to source images")
matches_dict = tU.mosaic_color_matcher(target_redux_dict, source_redux_dict, fU.distance)


print("...preparing for rendering")
result = dU.reshape_to_image(matches_dict, source_images_dict, num_rows, num_cols)


print("...rendering result")
vT.show_image(result)
