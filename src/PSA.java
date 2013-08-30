import java.io.*;
import java.util.*;

public class PSA {

	/**
	 * Partitions a set of points into m partitions
	 * @param points the set of points
	 * @param m the number of partitions
	 * @return a list of partitions
	 */
	public static List<Partition> partition(PointSet points, int m) {
		Partition A = new Partition();
		int nFeatures = points.dimension();
		if(m > points.size())
			throw new RuntimeException("Not enough points to make " + m + " partitions");
		for(Point p : points) {
			A.add(p);
		}
		
		List<Partition> S = new ArrayList<>();
		S.add(A);
		calculateDissimilarity(A, nFeatures);
		int i = 1;
		while(i < m) {
			int j = 0;
			for(int k = 1; k < S.size(); k++)
				if(S.get(k).dissimilarity > S.get(j).dissimilarity)
					j = k;
			Partition Aj = S.get(j);
			Partition Aj1 = new Partition();
			Partition Aj2 = new Partition();
			//partition
			for(Point p : Aj) {
				if(p.feature(Aj.pj) <= Aj.apj + Aj.dissimilarity/2)
					Aj1.add(p);
				else
					Aj2.add(p);
			}
			calculateDissimilarity(Aj1, nFeatures);
			calculateDissimilarity(Aj2, nFeatures);
			S.remove(Aj);
			S.add(Aj1);
			S.add(Aj2);
			i++;
		}
		return S;
	}
	
	/**
	 * Calculates and stores the dissimilarity of a partition
	 * @param data the partition
	 * @param nFeatures the number of features
	 */
	private static void calculateDissimilarity(Partition data, int nFeatures) {
		
		double[] a = new double[nFeatures];
		double[] b = new double[nFeatures];
		double[] delta = new double[nFeatures];
		
		for(int j = 0; j < nFeatures; j++) {
			a[j] = Double.MAX_VALUE;
			b[j] = Double.MIN_VALUE;
			for(Point p : data) {
				if(p.feature(j) < a[j])
					a[j] = p.feature(j);
				if(p.feature(j) > b[j])
					b[j] = p.feature(j);
			}
			delta[j] = b[j] - a[j];
		}
		
		int pj = 0;
		for(int j = 1; j < nFeatures; j++) {
			if(delta[j] > delta[pj])
				pj = j;
		}
		
		data.apj = a[pj];
		data.pj = pj;
		data.dissimilarity = delta[pj];
	}
	
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Usage: PSA filename n-partitions");
			System.exit(0);
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
			int m = Integer.parseInt(args[1]);
			PointSet A = new PointSet();
			
			String line;
			while((line = br.readLine()) != null) {
				String[] parts = line.split(" ");
				double[] data = new double[parts.length];
				for(int i = 0; i < parts.length; i++) {
					data[i] = Double.parseDouble(parts[i]);
				}
				A.add(new Point(data));
			}
			
			List<Partition> S = partition(A, m);
			for(Partition part : S)
				System.out.println(part);
			br.close();
		} catch(IOException e) {
			System.out.println(e);
		}
	}
	
}
