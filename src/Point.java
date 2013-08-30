

/**
 * A point represents a data item in n-dimensional space.
 * @author duncan
 *
 */
public class Point {
	
	double[] features;
	
	public Point(int k) {
		features = new double[k];
	}
	
	/**
	 * Creates a new point from the data
	 * @param data
	 */
	public Point(double ... data) {
		features = new double[data.length];
		for(int i = 0; i < data.length; i++)
			features[i] = data[i];
	}
	
	/**
	 * Copy constructor
	 * @param p
	 */
	public Point(Point p) {
		features = new double[p.length()];
		for(int i = 0; i < p.length(); i++)
			features[i] = p.feature(i);
	}
	
	/**
	 * Finds the ith feature of the Point
	 * @param i index value
	 * @return the feature value or throws exception
	 */
	public double feature(int i) {
		if(i >= 0 && i < features.length)
			return features[i];
		else throw new RuntimeException(i + " is not a valid index");
	}
	
	/**
	 * The length of the feature vector
	 * @return
	 */
	public int length() {
		return features.length;
	}
	
	/**
	 * Finds the Euclidean distance between this and another point
	 * Override this method in a child class to specify a different distance metric
	 * @param p point object
	 * @return
	 */
	public double distance(Point p) {
		if(length() != p.length())
			throw new RuntimeException("Cannot compare points of difference dimensions");
		double sum = 0;
		for(int i = 0; i < length(); i++) {
			sum += Math.pow(feature(i) - p.feature(i), 2);
		}
		return Math.sqrt(sum);
	}
	
	public String toString() {
		String s = "";
		for(int i = 0; i < features.length; i++) {
			if(i == 0)
				s += features[i] + " ";
			else
				s += ", " + features[i];
		}
		return "(" + s + ")";
	}
	
}
