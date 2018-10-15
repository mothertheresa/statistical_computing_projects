import imageio # you may need to install via: pip install imageio

# credit for this code:
# https://stackoverflow.com/questions/29718238/how-to-read-mp4-video-to-be-processed-by-scikit-image


def get_video(file_name):
    """
    obtains a video given a file name
    """

    # you may need to obtain via: conda install ffmpeg -c conda-forge
    return imageio.get_reader(file_name, 'ffmpeg')