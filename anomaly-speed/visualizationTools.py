import matplotlib.pyplot as plt
import pylab


def visualize_frame(video, frame_num):
    """
    prints the frame image from a video at a given frame_num
    """

    image = video.get_data(frame_num)
    fig = pylab.figure()
    timestamp = float(frame_num) / video.get_meta_data()['fps'] # frames per second
    fig.suptitle('Frame #{}; Timestamp {}s'.format(frame_num, round(timestamp, 2)), fontsize=18)
    pylab.imshow(image)

    pylab.show()

# for tileIdx in range(0, 1): #tileNum
#     X_test = getFeatureByTileAndFrames(tileIdx, 0, config.nframes)

#     plt.plot(range(0,len(X_test)), X_test[:, 0], c='red', alpha = 0.6)
#     plt.plot(range(0,len(X_test)), X_test[:, 1], c='green', alpha = 0.6)
#     plt.plot(range(0,len(X_test)), X_test[:, 2], c='blue', alpha = 0.6)

#     plt.show()

# for tileIdx in range(0, config.tileNum):
#     isolationTreeData = isolationTreesByTile[tileIdx]
#
#     X_train = isolationTreeData['X_train']
#     y_pred_train = isolationTreeData['y_pred_train']
#
#     plt.scatter(range(0,len(X_train)), y_pred_train, alpha = 0.6)
#
# plt.show()
#
# for tileIdx in range(0, 1): #tileNum
#     isolationTreeData = anomaliesByTile[tileIdx]
#
#     X_test = isolationTreeData['X_test']
#     y_pred_test = isolationTreeData['y_pred_test']
#
#     plt.scatter(range(0,len(X_test)), y_pred_test, alpha = 0.6, c=y_pred_test)
#     plt.show()
#
# def getAllChildren(tree, depth):
#     if (len(tree.root.getChildren()) == 0):
#         return
#     else:
#         for child in tree.root.getChildren():
#             node = child.get().root.get()
#             print(depth * "-" + str(node))
#             print(getAllChildren(child.get(), depth + 1))
#
#
# def printTree(tree):
#     print(str(tree.root.get()))
#     return getAllChildren(tree, 1)
