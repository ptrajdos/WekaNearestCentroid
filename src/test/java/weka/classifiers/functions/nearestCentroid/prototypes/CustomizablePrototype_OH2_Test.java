package weka.classifiers.functions.nearestCentroid.prototypes;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.CheckOptionHandler;

public class CustomizablePrototype_OH2_Test {

	@Test
	public void test() {
		CheckOptionHandler opCheck = new CheckOptionHandler();
		CustomizablePrototype cust = new CustomizablePrototype();
		
		opCheck.setOptionHandler(cust);
		
		assertTrue("Default Options", opCheck.checkDefaultOptions());
		
		
	}

}
