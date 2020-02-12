package weka.classifiers.functions.nearestCentroid.prototypes;

import weka.classifiers.functions.nearestCentroid.IClusterPrototype;

public class MahalanobisPrototypeTest extends AbstractPrototypeTest {

	@Override
	public IClusterPrototype getPrototype() {
		return new MahalanobisPrototype();
	}


}
