import numpy as np
import math

# credit for some of this code:
# https://gist.github.com/jakemmarsh/8273963


class Node:
    """
    a Node element of a Tree which contains a value and potentially has children
    """

    def __init__(self, val):
        self.val = val  # splitAtt $ splitVal, or length
        self.leftChild = None
        self.rightChild = None
    
    def get(self):
        return self.val
        
    def get_children(self):
        children = []
        if self.leftChild is not None:
            children.append(self.leftChild)
        if self.rightChild is not None:
            children.append(self.rightChild)
        return children


class Tree:
    """
    Tree is a collection of Nodes; it has a root Node and the ability to set
    the root and to insert new Nodes
    """

    def __init__(self):
        self.root = None

    def set_root(self, val):
        self.root = Node(val)

    def insert(self, val):
        if self.root is None:
            self.set_root(val)
        else:
            self.insert_node(self.root, val)

    def insert_node(self, current_node, val):
        current_node.leftChild = Node(val['left'])
        current_node.rightChild = Node(val['right'])


def cannot_split_further(sample):
    if len(sample) == 0:
        return True
    else:
        differences = []
        list_of_attributes = list(range(0, len(sample[0])))

        for attribute in list_of_attributes:
            all_values_of_attribute = [sample[i][attribute] for i in range(0, len(sample))]
            attribute_range = max(all_values_of_attribute) - min(all_values_of_attribute)
            differences.append(attribute_range)

        return all(elem == 0 for elem in differences)


def build_isolation_tree(sample):
    """
    given a sample of data, builds an isolation tree from the attributes
    sample must be of the form of a nested array
    """

    tree = Tree()
    
    if cannot_split_further(sample):
        tree.insert({
            'left':   None,
            'right':  None,
            'length': len(sample)})
    
    else:
        list_of_attributes = list(range(0, len(sample[0])))
        random_attribute = np.random.choice(list_of_attributes, 1)[0]

        all_values_of_attribute = [sample[i][random_attribute] for i in range(0, len(sample))]
        minimum = min(all_values_of_attribute)
        maximum = max(all_values_of_attribute)

        split_point = np.random.random() * (maximum - minimum) + minimum
        partitions = list(map(lambda i: i[random_attribute] < split_point, sample))
        left_partitions = list(np.where(partitions)[0])
        right_partitions = list(np.where(list(~np.array(partitions)))[0])

        left_sample = [sample[i] for i in left_partitions]
        rightsample = [sample[i] for i in right_partitions]
        
        if tree.root is None:
            tree.set_root({
                'splitAtt':   random_attribute,
                'splitValue': split_point
            })
        
        tree.insert({
            'left':  build_isolation_tree(left_sample),
            'right': build_isolation_tree(rightsample),
            'splitAtt':   random_attribute,
            'splitValue': split_point
        })
    
    return tree


def get_subsample(subsampling_size, input_data, rng):
    """
    given a sub-sampling size, input data, and a random number generator
    returns a subsample of the input data
    """

    indices = list(range(0, len(input_data)))
    random_indices = rng.choice(indices, subsampling_size, replace=False)
    return [input_data[i] for i in random_indices]


def build_isolation_forest(input_data, number_trees, subsampling_size, rng=np.random):
    """
    builds an entire forest for input data, tree number, and sub-sampling size
    using tree-building function
    """

    trees = np.array([])
    
    for treeIndex in range(0, number_trees):
        subsample = get_subsample(subsampling_size, input_data, rng)
        tree = build_isolation_tree(subsample)
        trees = np.append(trees, tree)
    
    return trees


def tree_external_node(tree):
    """
    returns whether an iTree is an external/terminating node
    """
    return 'length' in tree.root.get().keys()


def est_harmonic_num(x):
    """
    returns the estimated harmonic number
    """
    return math.log(x) + 0.5772156649  # euler's constant


def avg_unsucceful_path_length(length, testing_data_size):
    """
    returns the average path length of unsuccessful searches
    in a binary search tree, as defined by section 10.3.3
    of Preiss 1999
    """
    
    if length > 2:
        return (2 * est_harmonic_num(length - 1)) - (2 * (length - 1) / testing_data_size)
    elif length == 2:
        return 1
    else:
        return 0


def path_length(datum, tree, hlim, training_data_size):
    """
    returns the path length from root to node of a given
    datum in a iTree subject to a maximum height (hlim)
    and the size of the training data for the iTree
    """
    
    initial_path_length = 0
    
    def recursive_path_length(current_tree, current_path_length):
        if tree_external_node(current_tree):
            length = current_tree.root.get()['length']
            return current_path_length + avg_unsucceful_path_length(length, training_data_size)
        
        elif current_path_length >= hlim:
            return current_path_length + avg_unsucceful_path_length(current_path_length, training_data_size)
        
        else:
            split_att = current_tree.root.get()['split_att']
            split_value = current_tree.root.get()['split_value']
            if datum[split_att] < split_value:
                return recursive_path_length(current_tree.root.leftChild.get(), current_path_length + 1)
            else:
                return recursive_path_length(current_tree.root.rightChild.get(), current_path_length + 1)
    
    return recursive_path_length(tree, initial_path_length)


def anomaly_score(path_lengths, subsampling_size, training_data_size):
    """
    using the path lengths over a collection of trees
    (for a single data point), returns a value between 0
    and 1, where values close to 1 indicate anomalies and
    less than 0.5 indicate "normal" values
    """
    
    empirical_average_path_length = np.mean(path_lengths)
    theoretical_average_path_length = avg_unsucceful_path_length(subsampling_size, training_data_size)
    return 2 ** (-empirical_average_path_length / theoretical_average_path_length)


def evaluate_isolation_forest(new_data, forest, subsampling_size, training_data_size):
    """
    evaluates the isolation forest on new data, so returns the aggregated anomaly scores
    across all the new data points from their calculated path lengths
    """

    scores = np.array([])

    for newDatum in new_data:
        path_lengths = [path_length(newDatum, tree, hlim=40, training_data_size=training_data_size) for tree in forest]
        score = anomaly_score(path_lengths, subsampling_size, training_data_size)
        scores = np.append(scores, score)

    return scores
