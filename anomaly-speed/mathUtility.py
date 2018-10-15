def speed_conversion(pixels_per_second, inches_per_pixel):
    """
    converts pixels per second to miles per hour using
    specified inches per pixel
    """

    inches_per_second = pixels_per_second * inches_per_pixel

    seconds_per_hour = 60 * 60
    inches_per_mile = 63360

    return inches_per_second * seconds_per_hour / inches_per_mile
