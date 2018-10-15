def mosaic_color_matcher(target_redux_dict, source_redux_dict, distance):
    """
    given target and source, finds source that matches each
    component of target the best based on distance measure
    """
    matches_dict = {}

    for index in range(0, len(target_redux_dict)):

        source_dist_dict = {}
        for key, redux in source_redux_dict.items():
            source_dist_dict[key] = distance(target_redux_dict[index], redux)

        best_source = min(source_dist_dict, key=source_dist_dict.get)
        matches_dict[index] = best_source

    return matches_dict
