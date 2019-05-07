/**
 * 
 */
package weka.classifiers.functions.nearestCentroid.clusterCentroidFinders;

import weka.classifiers.functions.nearestCentroid.AClusterCentroidFinder;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Abstract class  -- a base for ClusterCentroidsFinders
 * @author pawel trajdos
 * @since 3.0.0
 * @version 3.0.0
 *
 */
public class ClusterCentroidFinderMeanOrMode extends AClusterCentroidFinder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4152910412910090276L;

	

	@Override
	public Instance findCentroid(Instances data) throws Exception {
		this.getCapabilities().testWithFail(data);
	
		int numAttrs = data.numAttributes();
		
		
		double[] instRep = new double[numAttrs];
		for(int a =0;a<numAttrs;a++) {
			instRep[a] = data.meanOrMode(a);
		}
		
		Instance resulting = data.get(0).copy(instRep); 
	
	
		return resulting;
	}

}
