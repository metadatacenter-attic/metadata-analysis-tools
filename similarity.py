import numpy as np
import sklearn.cluster
import distance
import argparse
import editdistance

parser = argparse.ArgumentParser(description='Tool to cluster strings using Affinity Propagation, according to the edit distance between each string')
parser.add_argument('file', nargs='?', type=str, help='File containing list of strings to cluster (one per line)')
args = parser.parse_args()

if(args.file is not None):
	print("Opening file...")
	file = open(args.file)
	lines = file.read().splitlines()
	words = np.asarray(lines)

	print("Computing edit distances...")
	distances = -1*np.array([[editdistance.eval(w1,w2) for w1 in words] for w2 in words]) # distance.levenshtein too slow
	# np.save("distances", distances) # save to disk
else:
	print("Loading distances from file...")
	distances = np.load("distances.npy")

print("Clustering data points...")
affprop = sklearn.cluster.AffinityPropagation(affinity="precomputed", damping=0.5)
affprop.fit(distances)
for cluster_id in np.unique(affprop.labels_):
    exemplar = words[affprop.cluster_centers_indices_[cluster_id]]
    cluster = np.unique(words[np.nonzero(affprop.labels_==cluster_id)])
    cluster_str = ", ".join(cluster)
    print(" - %s: %s" % (exemplar, cluster_str))

cluster_centers_indices = affprop.cluster_centers_indices_
n_clusters_ = len(cluster_centers_indices)
print('Estimated number of clusters: %d' % n_clusters_)
