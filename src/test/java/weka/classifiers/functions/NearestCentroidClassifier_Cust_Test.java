/**
 * 
 */
package weka.classifiers.functions;

import weka.classifiers.Classifier;
import weka.classifiers.functions.nearestCentroid.prototypes.CustomizablePrototype;

/**
 * @author pawel
 *
 */
public class NearestCentroidClassifier_Cust_Test extends NearestCentroidClassifierTest {

	/**
	 * @param name
	 */
	public NearestCentroidClassifier_Cust_Test(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Classifier getClassifier() {
		NearestCentroidClassifier nc = new NearestCentroidClassifier();
		nc.setClusProto(new CustomizablePrototype());
		return nc;
	}
	
	

}
