package weka.classifiers.functions.nearestCentroid.prototypes;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.core.OptionHandlersTest.OptionHandlerTest;

public class CustomizablePrototype_OH_Test extends  OptionHandlerTest {

	public CustomizablePrototype_OH_Test(String name, String classname) {
		super(name, classname);
	}
	
	public CustomizablePrototype_OH_Test(String name) {
		super(name, CustomizablePrototype.class.toString());
	}


	public static Test suite() {
	    return new TestSuite(CustomizablePrototype_OH_Test.class);
	  }

	public static void main(String[] args){
	    junit.textui.TestRunner.run(suite());
	  }


}
