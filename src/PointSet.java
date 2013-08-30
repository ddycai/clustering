


import java.util.HashSet;

/**
 * A set of points with the added restriction that contained points must all have the same dimension.
 * @author duncan
 *
 */
public class PointSet extends HashSet<Point> {

	private static final long serialVersionUID = 1L;
	private int dimension = -1;
	
	public boolean add(Point p) {
		if(dimension == -1)
			dimension = p.length();
		else if(p.length() != dimension)
			throw new RuntimeException("Point " + p + " must contain " + dimension + " features");
		return super.add(p);
	}
	
	public int dimension() {
		return dimension;
	}
	
}
