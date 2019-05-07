package weka.classifiers.functions.nearestCentroid.clusterCentroidFinders;


import junit.framework.Test;
import junit.framework.TestSuite;
import weka.core.OptionHandlersTest.OptionHandlerTest;

public class ClusterCentroidFinderMeanOrMode_OH_Test extends OptionHandlerTest {

	public ClusterCentroidFinderMeanOrMode_OH_Test(String name, String classname) {
		super(name, classname);
	}
	
	public ClusterCentroidFinderMeanOrMode_OH_Test(String name) {
		super(name, ClusterCentroidFinderMeanOrMode.class.toString());
	}

	public static Test suite() {
	    return new TestSuite(ClusterCentroidFinderMeanOrMode_OH_Test.class);
	  }

	public static void main(String[] args){
	    junit.textui.TestRunner.run(suite());
	  }
}
