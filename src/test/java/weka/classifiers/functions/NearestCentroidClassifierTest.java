package weka.classifiers.functions;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;

public class NearestCentroidClassifierTest extends AbstractClassifierTest {

  public NearestCentroidClassifierTest(String name) { super(name);  }

  /** Creates a default Classifier */
  public Classifier getClassifier() {
    return new NearestCentroidClassifier();
  }

  public static Test suite() {
    return new TestSuite(NearestCentroidClassifierTest.class);
  }

  public static void main(String[] args){
    junit.textui.TestRunner.run(suite());
  }
  
  

}
 
