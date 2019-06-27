package weka.core.distances;

import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.PerformanceStats;
/**
 * This class implements normalized euclidean distance. 
 * Normalized by dividing the distance by the square root of number of attributes
 * 
 * @author pawel trajdos
 * @since 3.1.0
 * @version 3.1.0
 *
 */

public class NormalizedEuclideanDistance extends EuclideanDistance {

	/**
	 *
	 * 
	 */
	private static final long serialVersionUID = -572810679027328671L;

	public NormalizedEuclideanDistance() {
		super();
	}

	public NormalizedEuclideanDistance(Instances data) {
		super(data);
	}
	
	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#distance(weka.core.Instance, weka.core.Instance, double, weka.core.neighboursearch.PerformanceStats)
	 */
	@Override
	public double distance(Instance first, Instance second, double cutOffValue, PerformanceStats stats) {
		double origDist = super.distance( first,  second, cutOffValue,  stats);
		int numAttrs = this.m_Data.numAttributes();
		origDist/= Math.sqrt(numAttrs);
		return origDist;
	}

}
