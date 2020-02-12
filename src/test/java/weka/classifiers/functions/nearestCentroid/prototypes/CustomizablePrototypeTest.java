package weka.classifiers.functions.nearestCentroid.prototypes;

import weka.classifiers.functions.nearestCentroid.IClusterCentroidFinder;
import weka.classifiers.functions.nearestCentroid.IClusterPrototype;
import weka.core.DistanceFunction;

public class CustomizablePrototypeTest extends AbstractPrototypeTest {

	@Override
	public IClusterPrototype getPrototype() {
		return new CustomizablePrototype();
	}
	
	public void testDistanceFunction() {
		CustomizablePrototype proto = (CustomizablePrototype) this.getPrototype();
		DistanceFunction dist = proto.getDistanceFunction();
		assertTrue("Not null distance", dist!=null);
	}
	
	public void testCentroidFinder() {
		CustomizablePrototype proto = (CustomizablePrototype) this.getPrototype();
		IClusterCentroidFinder centF = proto.getCentroidFinder();
		assertTrue("Not null centroid finder", centF!=null);
	}

	

}
