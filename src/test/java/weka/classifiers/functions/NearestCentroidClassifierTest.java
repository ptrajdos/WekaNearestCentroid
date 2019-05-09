package weka.classifiers.functions;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;
import weka.tools.SerialCopier;

public class NearestCentroidClassifierTest extends AbstractClassifierTest {

  public NearestCentroidClassifierTest(String name) { super(name);  }

  /** Creates a default Classifier */
  public Classifier getClassifier() {
    return new NearestCentroidClassifier();
  }

  public static Test suite() {
    return new TestSuite(NearestCentroidClassifierTest.class);
  }
  
  public void testSerialCopy() {
	  Classifier cla = this.getClassifier();
	  try {
		Classifier copy = (Classifier) SerialCopier.makeCopy(cla);
	} catch (Exception e) {
		e.printStackTrace();
		fail("Copy by serialization has failed.");
	}
  }
  
  public void testForName() {
	  NearestCentroidClassifier cla = (NearestCentroidClassifier) this.getClassifier();
	  String[] options = cla.getOptions();
	  System.out.println("options: " + Arrays.toString(options));
	  try {
		cla.setOptions(options);
		String clName = "weka.classifiers.functions.NearestCentroidClassifier";
		System.out.println("Classifier class name: " + clName);
		AbstractClassifier.forName(clName, options);
	} catch (Exception e) {
		e.printStackTrace();
		fail("Exception");
	}
  }

  public static void main(String[] args){
    junit.textui.TestRunner.run(suite());
  }
  
  

}
 
