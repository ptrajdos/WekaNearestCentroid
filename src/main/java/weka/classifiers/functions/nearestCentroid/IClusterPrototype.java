package weka.classifiers.functions.nearestCentroid;

import weka.core.Instance;
import weka.core.Instances;

/**
 * 
 * @author pawel trajdos
 * An interface for objects representing centroids. 
 * @since 3.0.0
 * @version 3.0.0
 *
 */

public interface IClusterPrototype {
	
	/**
	 * Get central point
	 * @since 3.0.0
	 * @version 3.0.0
	 * 
	 * @return Instance -- central point
	 */
	public Instance getCenterPoint();
	
	/**
	 * Learns the centroid:
	 * + Find the central point
	 * + Tune the distance function if needed
	 * @since 3.0.0
	 * @version 3.0.0
	 * 
	 * @param data
	 * @throws Exception if it is impossible to create the cluster
	 */
	public void build(Instances data)throws Exception;
	
	/**
	 * Calculates distance from the instance to centroid
	 * @since 3.0.0
	 * @version 3.0.0
	 * 
	 * @param inst -- instance
	 * @return
	 */
	public double distance(Instance inst);
	
	/**
	 * Calculates the distance between two points using the distance function tuned for the cluster.
	 * 
	 * @param inst1 -- 1st instance 
	 * @param inst2 -- 2nd instance
	 * @return
	 */
	public double distance(Instance inst1, Instance inst2);
	
	/**
	 * Determines whether the centroid is a valid one.
	 * The centroid is found and the distance is tuned.
	 * @since 3.0.0
	 * @version 3.0.0
	 * 
	 * @return 
	 */
	public boolean isValid();
	

}
