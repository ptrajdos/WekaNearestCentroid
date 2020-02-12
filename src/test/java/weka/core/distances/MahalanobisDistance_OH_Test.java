package weka.core.distances;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.core.OptionHandlersTest.OptionHandlerTest;

public class MahalanobisDistance_OH_Test extends OptionHandlerTest {

	public MahalanobisDistance_OH_Test(String name, String classname) {
		super(name, classname);
		// TODO Auto-generated constructor stub
	}
	
	public MahalanobisDistance_OH_Test(String name) {
		super(name,MahalanobisDistance.class.getCanonicalName());
	}
	

	  public static Test suite() {
	    return new TestSuite(MahalanobisDistance_OH_Test.class);
	  }

	public static void main(String[] args){
	    junit.textui.TestRunner.run(suite());
	  }
	
}
