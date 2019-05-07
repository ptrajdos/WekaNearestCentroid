/**
 * 
 */
package weka.classifiers.functions.nearestCentroid.prototypes;

import java.io.Serializable;

import weka.classifiers.functions.nearestCentroid.IClusterPrototype;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.distances.MahalanobisDistance;

/**
 * The class represents a cluster prototype using the mahalanobis distance.
 * @author pawel trajdos
 * @since 3.0.0
 * @version 3.0.0
 *
 */
public class MahalanobisPrototype implements IClusterPrototype, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5919371807807783627L;
	
	/**
	 * Mahalanobis distance object to use 
	 */
	protected MahalanobisDistance mahDist;

	/**
	 * Default constructor
	 */
	public MahalanobisPrototype() {
		this.mahDist = new MahalanobisDistance();
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.functions.nearestCentroid.IClusterPrototype#getCenterPoint()
	 */
	@Override
	public Instance getCenterPoint() {
		return this.mahDist.getCentroid();
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.functions.nearestCentroid.IClusterPrototype#build(weka.core.Instances)
	 */
	@Override
	public void build(Instances data) throws Exception {
		this.mahDist.setInstances(data);

	}

	/* (non-Javadoc)
	 * @see weka.classifiers.functions.nearestCentroid.IClusterPrototype#distance(weka.core.Instance)
	 */
	@Override
	public double distance(Instance inst) {
		return this.mahDist.distance(inst, getCenterPoint());
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.functions.nearestCentroid.IClusterPrototype#distance(weka.core.Instance, weka.core.Instance)
	 */
	@Override
	public double distance(Instance inst1, Instance inst2) {
		return this.mahDist.distance(inst1, inst2);
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.functions.nearestCentroid.IClusterPrototype#isValid()
	 */
	@Override
	public boolean isValid() {
		return this.mahDist.getCentroid() != null;
	}

}
