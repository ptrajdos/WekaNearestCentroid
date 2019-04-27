package weka.classifiers.functions.nearestCentroid;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.OptionHandler;

/**
 * Responsible for finding class prototypes
 * @author pawel trajdos
 * @since 2.0.0
 * @version 2.0.0
 *
 */
public interface ICentroidFinder extends OptionHandler {
	
	/**
	 * Find prototypes
	 * @param trainingSet
	 * @throws Exception -- when something goes wrong
	 */
	public void findCentroids(Instances trainingSet) throws Exception;
	
	/**
	 * Returns found centroid.
	 * @param classIdx -- class index of the centroid
	 * @return Centroid of given class
	 * @exception  Exception If no centroids have been created
	 */
	public Instance getCentroid(int classIdx)throws Exception;
	
	/**
	 * Indicates if there was enough instances to create a valid centroid for given class.
	 * @param classIdx
	 * @return
	 * @throws Exception If no centroids have been created.
	 */
	public boolean isCentroidActive(int classIdx)throws Exception;
	
	/**
	 * Returns the number of centroids found.
	 * @return
	 */
	public int getCentroidNum();
	
	/**
	 * Indicates whether the centroids are yet found.
	 * @return
	 */
	public boolean isModelBuilt();
	
	
	
	
	
	

}
