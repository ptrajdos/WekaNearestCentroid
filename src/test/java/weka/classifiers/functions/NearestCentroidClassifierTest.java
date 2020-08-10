package weka.classifiers.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.tools.SerialCopier;
import weka.tools.data.RandomDataGenerator;
import weka.tools.tests.DistributionChecker;
import weka.tools.tests.WekaGOEChecker;

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
  
  public void testGlobalInfo() {
	  NearestCentroidClassifier cla = (NearestCentroidClassifier) this.getClassifier();
	  String gInfor = cla.globalInfo();
	  assertTrue("Non-zero description", gInfor.length()>1);
  }
  
  public void testGlobalInfor() {
		WekaGOEChecker check = new WekaGOEChecker();
		check.setObject(this.getClassifier());
		assertTrue("Global info Checker", check.checkCallGlobalInfo());
		assertTrue("TipText checker", check.checkToolTipsCall());
	}
  
  public void testCentroids() {
	  NearestCentroidClassifier classifier = (NearestCentroidClassifier) this.getClassifier();
	  try {
		  Instance[] centrs = classifier.getCentroids();
		  fail("Modes was not trained!");
	  }catch(Exception e) {
		  assertTrue("Classifier not ready!", true);
	  }
	  
	  String notLearnedDescription = classifier.toString();
	  
	  try {
		  Instances data = this.generateData();
		  classifier.buildClassifier(data);
		  Instance[] centrs = classifier.getCentroids();
		  assertTrue("Not null centroids", centrs!=null);
		  assertTrue("Number of centroids", centrs.length == data.numClasses());
		  String descriptionWithCentroids = classifier.toString();
		  assertTrue("Description Length", descriptionWithCentroids.length()> notLearnedDescription.length());
	  }catch(Exception e) {
		  fail("An exception has been caught!");
	  }
	  
  }
  
  public void testOneCentroidActive() {
	  NearestCentroidClassifier classifier = (NearestCentroidClassifier) this.getClassifier();
	  try {
		  Instances data = this.getDataOneProtoInactive();
		  
		  String notLearnedDescription = classifier.toString();
		  
		  classifier.buildClassifier(data);
		  
		  Instance[] centrs = classifier.getCentroids();
		  assertTrue("Not null centroids", centrs!=null);
		  assertTrue("Number of centroids", centrs.length == data.numClasses());
		  String descriptionWithCentroids = classifier.toString();
		  assertTrue("Description Length", descriptionWithCentroids.length()> notLearnedDescription.length());
		  
		  double[] distrib = classifier.distributionForInstance(data.get(0));
		  assertTrue("Not null distribution", distrib !=null);
		  assertTrue("Distribution length", distrib.length == data.numClasses());
		  
		  
	  }catch(Exception e) {
		  fail("An exception has been caught:" + e.getClass().getCanonicalName());
	  }
  }
  
  public void testNoInstancesData() {
	  NearestCentroidClassifier classifier = (NearestCentroidClassifier) this.getClassifier();
	  RandomDataGenerator gen = new RandomDataGenerator();
	  gen.setNumNominalAttributes(0);
	  gen.setNumObjects(0);
	  Instances data = gen.generateData();
	  
	  gen.setNumObjects(10);
	  Instances testData = gen.generateData();
	  Instance testInstance = testData.get(0);
	  
	  
	  try {
		classifier.buildClassifier(data);
		double[] distrib = classifier.distributionForInstance(testInstance);
		assertTrue("Not null distribution", distrib!=null);
		assertTrue("Distribution length", distrib.length == data.numClasses());
		assertTrue("Distribution format",DistributionChecker.checkDistribution(distrib));
		
		Instance[] centroids = classifier.getCentroids();
		assertTrue("Centroids array not null", centroids != null);
		assertTrue("Centroids length,", centroids.length == data.numClasses());
		for(int i=0;i<centroids.length;i++)
			assertTrue("Empty centroid", centroids[i] == null);
		
	} catch (Exception e) {
		fail("An exception has been caught:" + e.getClass().getCanonicalName());
	}
	  
  }
  
  public void testBiggerDataset() {
	  RandomDataGenerator gen = new RandomDataGenerator();
	  gen.setNumNominalAttributes(0);
	  Instances data = gen.generateData();
	  Instance testInstance = data.get(0);
	  
	  NearestCentroidClassifier cent = (NearestCentroidClassifier) this.getClassifier();
	  
	  try {
		cent.buildClassifier(data);
		double[] distribution = cent.distributionForInstance(testInstance);
		  DistributionChecker.checkDistribution(distribution);
	} catch (Exception e) {
		fail("Bigger data fail, Exception has been thrown: " + e.getMessage());
	}
	  
  }
  
  protected Instances generateData() {
	  ArrayList<Attribute> atts = new ArrayList<Attribute>(3);
      atts.add(new Attribute("X1"));
      LinkedList<String> valList = new LinkedList<String>();
      valList.add("1");
      valList.add("2");
      atts.add(new Attribute("Class", valList));
      
      
      Instances dataset = new Instances("daataset",atts,1);
      dataset.setClassIndex(1);
      
      
      dataset.add(new DenseInstance(1.0, new double[] {-3,0}));
      dataset.add(new DenseInstance(1.0, new double[] {-2,1}));
      dataset.add(new DenseInstance(1.0, new double[] {-1,1}));
      dataset.add(new DenseInstance(1.0, new double[] {0,0}));
      dataset.add(new DenseInstance(1.0, new double[] {1,0}));
      dataset.add(new DenseInstance(1.0, new double[] {2,1}));
      dataset.add(new DenseInstance(1.0, new double[] {3,1}));
      
      return dataset;
  }
  
  public Instances getDataOneProtoInactive() {
	  ArrayList<Attribute> atts = new ArrayList<Attribute>(3);
      atts.add(new Attribute("X1"));
      LinkedList<String> valList = new LinkedList<String>();
      valList.add("1");
      valList.add("2");
      atts.add(new Attribute("Class", valList));
      
      
      Instances dataset = new Instances("daataset",atts,1);
      dataset.setClassIndex(1);
      
      
      dataset.add(new DenseInstance(1.0, new double[] {-3,0}));
      dataset.add(new DenseInstance(1.0, new double[] {-2,0}));
      dataset.add(new DenseInstance(1.0, new double[] {-1,0}));
      dataset.add(new DenseInstance(1.0, new double[] {0,0}));
      dataset.add(new DenseInstance(1.0, new double[] {1,0}));
      dataset.add(new DenseInstance(1.0, new double[] {2,0}));
      dataset.add(new DenseInstance(1.0, new double[] {3,0}));
      
      return dataset;
	  
  }

  public static void main(String[] args){
    junit.textui.TestRunner.run(suite());
  }
  
  

}
 
