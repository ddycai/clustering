

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the k-means clustering algorithm
 * @author duncan
 *
 */
public class KMeans {
	
	/**
	 * Groups the set of points into k clusters
	 * @param points the set of points
	 * @param k the number of clusters
	 * @param threshold the minimum movement of centroids before the algorithm stops
	 * @param maxIters the maximum number of iterations before the algorithm stops
	 * @param scaling a scaling factor, null for no scaling
	 * @return an array of clusters of size k
	 */
	public static Cluster[] compute(PointSet points, int k, double threshold, int maxIters, double[] scaling) {
		
		int nFeatures = points.dimension();
		if(scaling != null) {
			for(Point p : points) {
				for(int i = 0; i < nFeatures; i++)
					p.features[i] = p.feature(i) * scaling[i];
			}
		}
		
		Cluster[] clusters = new Cluster[k];
		
		//select k random points as centroids
		List<Point> list = new ArrayList<>(points);
		Collections.shuffle(list);
		for(int i = 0; i < k; i++) {
			clusters[i] = new Cluster();
			clusters[i].centroid = new Point(list.get(i));
		}
		
		Cluster[] newClusters;
		int nIters = 0;
		double largestChange;
		do {
			largestChange = 0;
			newClusters = new Cluster[k];
			for(int i = 0; i < k; i++) {
				newClusters[i] = new Cluster();
				newClusters[i].centroid = new Point(nFeatures);
			}
			
			//assign each point to nearest cluster centroid
			for(Point p : points) {
				int best = 0;
				double bestDist = Double.MAX_VALUE;
				for(int i = 0; i < k; i++) {
					double dist = clusters[i].centroid.distance(p);
					if(dist < bestDist) {
						best = i;
						bestDist = dist;
					}
				}
				newClusters[best].add(p);
				for(int i = 0; i < nFeatures; i++) {
					newClusters[best].centroid.features[i] += p.feature(i);
				}
			}
			
			//calculate new centroids
			for(int i = 0; i < k; i++) {
				for(int j = 0; j < nFeatures; j++)
					newClusters[i].centroid.features[j] /= newClusters[i].size();
				double dist = newClusters[i].centroid.distance(clusters[i].centroid);
				if(dist > largestChange)
					largestChange = dist;
			}
			clusters = newClusters;
			nIters++;
			System.out.println(largestChange);
		} while(largestChange >= threshold && nIters <= maxIters);
		
		return clusters;
	}
	
	/**
	 * k-means without a scaling factor
	 */
	public static Cluster[] compute(PointSet points, int k, double threshold, int maxIters) {
		return compute(points, k, threshold, maxIters, null);
	}
	
	/**
	 * Computes a scaling factor array where the factor is 1/max where max
	 * is the maximum feature.
	 * @param points a set of points
	 * @return scaling factor array
	 */
	public static double[] computeScaling(PointSet points) {
		double[] scaling = new double[points.dimension()];
		for(Point p : points) {
			for(int i = 0; i < points.dimension(); i++) {
				if(p.feature(i) > scaling[i])
					scaling[i] = p.feature(i);
			}
		}
		for(int i = 0; i < points.dimension(); i++) {
			scaling[i] = 1/scaling[i];
		}
		return scaling;
	}
	
	public static void main(String[] args) {
		PointSet points = new PointSet();
		points.add(new Point(1, 2));
		points.add(new Point(1, 1));
		points.add(new Point(7, 6));
		points.add(new Point(8, 9));
		Cluster[] clusters = compute(points, 2, 1, 5);
		for(Cluster c : clusters) {
			System.out.println(c + ", centroid: " + c.centroid);
		}
	}
	
}
