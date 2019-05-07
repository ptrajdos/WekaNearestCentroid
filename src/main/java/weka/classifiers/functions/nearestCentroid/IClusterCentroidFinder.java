/**
 * 
 */
package weka.classifiers.functions.nearestCentroid;

import weka.core.Instance;
import weka.core.Instances;

/**
 * Interface for objects responsible for finding cluster prototype
 * @author pawel trajdos
 * @since 3.0.0
 * @version 3.0.0
 *
 */
public interface IClusterCentroidFinder {
	
	/**
	 * Find central instance (centroid) of a cluster
	 * The centroid is found using given data set
	 * @param data -- data set
	 * @return centroid
	 * @throws Exception if something goes wrong
	 */
	public Instance findCentroid(Instances data)throws Exception;

}
