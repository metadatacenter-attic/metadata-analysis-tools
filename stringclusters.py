#!/usr/bin/env python3
"""Provides StringClusters class.

StringClusters is a tool for comparing strings using Levenshtein edit distance or Euclidean distance (based on word
embeddings), and for clustering strings using affinity propagation according to the pairwise distances between them.
These distances can be computed using one of the provided methods, or using some other distance metric whose weights
can be used by this tool (programmatically) to do the clustering.

Example usage:
    ```
    Cluster the list of strings in a file named "foods.txt" in the current directory:
    >> python stringclusters.py -i foods.txt

    Cluster the list of strings in a file named "foods.txt" and save the clusters to a file named "clusters.json":
    >> python stringclusters.py -i foods.txt -o /home/user/documents/clusters.json

    Cluster the list of strings in a file named "foods.txt" using Euclidean distance:
    >> python stringclusters.py -i foods.txt -d euclidean

    Compare a string "ham pizza" with all strings in a file named "foods.txt" in the current directory:
    >> python stringclusters.py -i foods.txt -s "ham pizza"
    ```
"""

import argparse
import datetime
import json
import operator
import os
import re
import sys
import editdistance
import numpy as np
import sklearn.cluster
import spacy
from enum import Enum

__author__ = "Rafael Gonçalves, Stanford University"


class Distance(Enum):
    EDIT = 'edit'
    EUCLIDEAN = 'euclidean'


class StringClusters:

    def __init__(self):
        # load spacy statistical model. we are interested in the word vectors; 300-dimensional vector representations
        # of words that allow us to determine how similar they are to each other.
        self.nlp = spacy.load('en_core_web_lg')
        self.ap = sklearn.cluster.AffinityPropagation(affinity="precomputed", damping=0.5)
        self.vectors = dict()
        self.pattern = re.compile('([^\s\w]|_)+')  # regular expression to normalize input strings

    # takes a collection of tokens and computes the pairwise Levenshtein edit distance between all tokens
    def get_edit_distances(self, all_tokens):
        distances = -1 * np.array([[editdistance.eval(w1, w2) for w1 in all_tokens] for w2 in all_tokens])
        return distances

    # takes a collection of plain text tokens and computes pairwise similarity between all tokens
    def get_similarity_weights(self, all_tokens):
        vectors = self.get_vectors(all_tokens).values()
        return np.array([[t1.similarity(t2) for t1 in vectors] for t2 in vectors])

    # takes a plain text token and computes its similarity with the plain text tokens in the given collection of tokens
    def get_similar_terms(self, token, all_tokens):
        all_tokens = self.normalize_tokens(all_tokens)
        token = self.normalize_tokens(token)
        vec_token = self.get_vector(token)
        vectors = self.get_vectors(all_tokens)
        similarity_weights = dict()

        for input_token in vectors:
            vec = vectors[input_token]
            similarity_weights[input_token] = vec_token.similarity(vec)  # compute similarity between tokens

        # get a dictionary where the above vectors are sorted in descending order of similarity
        sorted_similarity_weights = sorted(similarity_weights.items(), key=operator.itemgetter(1), reverse=True)
        return sorted_similarity_weights

    # get a dictionary of vectors for the given tokens
    def get_vectors(self, tokens):
        vectors = dict()
        for token in tokens:
            vectors[token] = self.get_vector(token)
        return vectors

    # get vector of the given token
    def get_vector(self, token):
        if token not in self.vectors:
            vector = self.nlp(token)
            self.vectors[token] = vector
            return vector
        else:
            return self.vectors[token]

    # normalize a given set of string tokens
    def normalize_tokens(self, tokens):
        normalized_tokens = list()
        for token in tokens:
            normalized_tokens.append(self.normalize(token))
        return normalized_tokens

    # replace all non-alphanumeric characters with spaces, and trim all extra white spaces and tabs
    def normalize(self, token):
        token = self.pattern.sub(' ', token)
        return re.sub('\s+', ' ', token).strip()

    # cluster the given tokens according to their similarity distances. returns a dictionary that maps each cluster
    # exemplar to an array of cluster elements that the exemplar represents
    def cluster(self, distances, tokens):
        self.ap.fit(distances)
        clusters = dict()
        for cluster_id in np.unique(self.ap.labels_):
            exemplar = tokens[self.ap.cluster_centers_indices_[cluster_id]]
            cluster = np.unique(tokens[np.nonzero(self.ap.labels_ == cluster_id)])
            clusters[exemplar] = cluster.tolist()
        return clusters

    # convenience function to compare tokens using the given distance metric, and to cluster them afterwards
    def compare_and_cluster(self, tokens, distance_metric):
        tokens = self.normalize_tokens(tokens)
        if distance_metric == Distance.EDIT:
            distance_weights = self.get_edit_distances(tokens)
        elif distance_metric == Distance.EUCLIDEAN:
            distance_weights = self.get_similarity_weights(tokens)
        else:
            raise ValueError("Unknown distance metric input: '" + distance_metric + "'. Supported values are: " +
                             str([distance.value for distance in Distance]))
        clusters = self.cluster(distance_weights, np.array(tokens))
        return clusters

    # get a JSON representation of the given dictionary
    def get_dictionary_as_json(self, dictionary):
        return json.dumps(dictionary, sort_keys=True, indent=2)


# Use arparse to get command line arguments
def get_arguments():
    # get timestamp in ISO format, and replace colons with dashes in timestamp to have a valid file name
    timestamp = datetime.datetime.now().isoformat().replace(":", "-")
    default_output_file = "stringclusters_output_" + timestamp + ".json"
    parser = argparse.ArgumentParser(
        description="StringClusters is a tool for comparing strings using Levenshtein edit distance or Euclidean "
                    "distance (based on word embeddings), and for clustering strings using affinity propagation "
                    "according to the pairwise distances between them. These distances can be computed using one of "
                    "the provided methods, or using some other distance metric whose weights can be used by this tool "
                    "(programmatically) to do the clustering.")
    parser.add_argument("-i", "--input_file", required=True, type=str,
                        help="Input file containing list of strings (one per line)")
    parser.add_argument("-o", "--output_file", required=False, type=str, default=default_output_file,
                        help="Output file. By default saves as 'stringclusters_output.json' with a creation timestamp, "
                             "to the current directory")
    parser.add_argument("-d", "--distance_metric", required=False, type=str, default=Distance.EDIT,
                        help="Distance metric (edit | euclidean). By default uses Levenshtein distance ('edit')")
    parser.add_argument("-s", "--single_term", required=False, type=str,
                        help="A single string that will be compared with all the other input strings. The output is a "
                             "JSON file containing the similarity scores between the given string and all strings in "
                             "the input file")
    arguments = parser.parse_args()

    if not os.path.exists(arguments.input_file):
        parser.error("The file '{}' does not exist".format(arguments.input_file))
        sys.exit(1)

    # create output directories if needed
    if os.path.dirname(arguments.output_file):
        os.makedirs(os.path.dirname(arguments.output_file), exist_ok=True)

    return arguments.input_file, arguments.output_file, arguments.distance_metric, arguments.single_term


if __name__ == "__main__":
    args = get_arguments()
    input_file = open(args[0])
    lines = input_file.read().splitlines()  # read in file with list of strings

    str_clusters = StringClusters()
    if args[3] is not None:
        output_dict = str_clusters.get_similar_terms(args[3], lines)
    else:
        output_dict = str_clusters.compare_and_cluster(lines, args[2])

    output_file = open(args[1], "w+")
    output_file.write(str_clusters.get_dictionary_as_json(output_dict))  # save clusters dictionary as a JSON file
    output_file.close()
