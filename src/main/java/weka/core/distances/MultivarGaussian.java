package weka.core.distances;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import weka.estimators.MultivariateGaussianEstimator;

public class MultivarGaussian extends MultivariateGaussianEstimator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3887178148518032934L;

	public MultivarGaussian() {
		super();
	}
	
	public double getMahalanobisDistToCenter(double[] point) {
		Vector x = new DenseVector(point);
	    return Math.sqrt(x.dot(covarianceInverse.mult(x.add(-1.0, mean), new DenseVector(x.size()))));
	}
	
	public double getMahalanobisDist(double[] vec1, double[] vec2) {
		Vector x = new DenseVector(vec1);
		Vector y = new DenseVector(vec2);
		
		return Math.sqrt(x.dot(covarianceInverse.mult(x.add(-1.0, y), new DenseVector(x.size()))));
	}

}
