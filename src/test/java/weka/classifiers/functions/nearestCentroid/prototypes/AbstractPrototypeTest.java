/**
 * 
 */
package weka.classifiers.functions.nearestCentroid.prototypes;

import java.util.ArrayList;
import java.util.LinkedList;

import junit.framework.TestCase;
import weka.classifiers.Classifier;
import weka.classifiers.functions.nearestCentroid.IClusterPrototype;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.tools.SerialCopier;
import weka.tools.data.RandomDataGenerator;
import weka.tools.data.RandomDoubleGenerator;
import weka.tools.data.RandomDoubleGeneratorGaussian;
import weka.tools.tests.DistributionChecker;

/**
 * @author pawel trajdos
 * @since 3.1.1
 * @version 3.1.1
 *
 */
public abstract class AbstractPrototypeTest extends TestCase {

	/**
	 * 
	 */
	public AbstractPrototypeTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public AbstractPrototypeTest(String name) {
		super(name);
	}
	
	public abstract IClusterPrototype getPrototype();
	
	protected Instances getTestdata() {
		  
			
		  ArrayList<Attribute> atts = new ArrayList<Attribute>(3);
	      atts.add(new Attribute("X1"));
	      LinkedList<String> valList = new LinkedList<String>();
	      valList.add("1");
	      valList.add("2");
	      atts.add(new Attribute("Class", valList));
	      
	      LinkedList<String> valList2 = new LinkedList<String>();
	      valList2.add("A");
	      valList2.add("B");
	      atts.add(new Attribute("Nomi1", valList2));
	      
	      Instances dataset = new Instances("dataset",atts,1);
	      dataset.setClassIndex(2);
	      
	      
	      dataset.add(new DenseInstance(1.0, new double[] {-3,0,1}));
	      dataset.add(new DenseInstance(1.0, new double[] {-2,1,1}));
	      dataset.add(new DenseInstance(1.0, new double[] {-1,1,1}));
	      dataset.add(new DenseInstance(1.0, new double[] {0,0,1}));
	      dataset.add(new DenseInstance(1.0, new double[] {1,0,0}));
	      dataset.add(new DenseInstance(1.0, new double[] {2,1,0}));
	      dataset.add(new DenseInstance(1.0, new double[] {3,1,0}));

		
		return dataset;
	}
	
	public void checkPrototype(IClusterPrototype proto, Instances dataset) {
		assertTrue("Is cnetroid valid", proto.isValid());
		Instance centroid = proto.getCenterPoint();
		
		double distance = proto.distance(dataset.get(0), dataset.get(0));
		assertTrue("The same instances distance", Utils.eq(distance, 0.0));
		
		distance = proto.distance(dataset.get(0), dataset.get(1));
		assertFalse("Distance not Nan", Double.isNaN(distance));
		assertTrue("Distance finite", Double.isFinite(distance));
		assertTrue("Different instances distance", Utils.grOrEq(distance, 0.0));
	
		
		distance = proto.distance(centroid);
		assertFalse("Distance to centroid from centroid not Nan (general check)", Double.isNaN(distance));
		assertTrue("Distance to centroid from centroid finite (general check)", Double.isFinite(distance));
		assertTrue("Distance to Centroid (from centroid) ", Utils.eq(distance, 0.0));
		
		distance =  proto.distance(dataset.get(0));
		assertFalse("Distance not Nan (from centroid)", Double.isNaN(distance));
		assertTrue("Distance finite (from centroid)", Double.isFinite(distance));
		assertTrue("Distance to Centroid (from different instance) ", Utils.grOrEq(distance, 0.0));
		
		for(Instance instance: dataset) {
			
			distance =  proto.distance(instance);
			
			assertFalse("Distance not Nan (general check)", Double.isNaN(distance));
			assertTrue("Distance finite (general check)", Double.isFinite(distance));
			assertTrue("Distance to Centroid (general check) ", Utils.grOrEq(distance, 0.0));
			
		}
		
	}
	
	public void testPrototype() {
		IClusterPrototype proto = this.getPrototype();
		
		Instances dataset = this.getTestdata();
		
		try {
	    	assertFalse("Is cnetroid invalid", proto.isValid());
			proto.build(dataset);
			
			this.checkPrototype(proto, dataset);
			Instance centroid = proto.getCenterPoint();
			assertTrue("Centroid check ", Utils.eq(centroid.toDoubleArray()[0], 0));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("An exception has been caught");
		}
		
	}
	
	 public void testOnCondensedData() {
		 IClusterPrototype proto = this.getPrototype();
		 
		 RandomDataGenerator gen = new RandomDataGenerator();
		 gen.setNumNominalAttributes(0);
		 gen.setNumStringAttributes(0);
		 gen.setNumDateAttributes(0);
		 RandomDoubleGenerator doubleGen = new RandomDoubleGeneratorGaussian();
		 doubleGen.setDivisor(100000.0);
		 gen.setDoubleGen(doubleGen );
		 
		 Instances dataset = gen.generateData();
		 try {
			 
			 proto.build(dataset);
			 this.checkPrototype(proto, dataset);
						
		} catch (Exception e) {
			fail("An exception has been caught " + e.getMessage());
		}
	 }
	 	 
	 public void testOnNominalConvertedData() {
		 IClusterPrototype proto = this.getPrototype();
		 
		 RandomDataGenerator gen = new RandomDataGenerator();
		 gen.setNumNominalAttributes(20);
		 gen.setNumStringAttributes(0);
		 gen.setNumDateAttributes(0);
		 gen.setNumNumericAttributes(0);
		 gen.setNumClasses(2);
		 gen.setMaxNumNominalValues(5);
		 gen.setNumObjects(100);
		 gen.setAllowUnary(true);
		 
		 Instances dataset = gen.generateData();
		 try {
			 
			 proto.build(dataset);
			 this.checkPrototype(proto, dataset);
						
		} catch (Exception e) {
			fail("An exception has been caught " + e.getMessage());
		}
	 }
	 
	 
	 public void testOnUnaryData() {
		 IClusterPrototype proto = this.getPrototype();
		 
		 RandomDataGenerator gen = new RandomDataGenerator();
		 gen.setNumNominalAttributes(10);
		 gen.setNumStringAttributes(0);
		 gen.setNumDateAttributes(0);
		 gen.setNumNumericAttributes(0);
		 gen.setNumClasses(2);
		 gen.setMaxNumNominalValues(1);
		 gen.setNumObjects(50);
		 gen.setAllowUnary(true);
		 
		 Instances dataset = gen.generateData();
		 try {
			 
			 proto.build(dataset);
			 this.checkPrototype(proto, dataset);
						
		} catch (Exception e) {
			fail("An exception has been caught " + e.getMessage());
		}
	 }
	
	public void testSerialization() {
		try {
			IClusterPrototype proto = this.getPrototype();
			IClusterPrototype ptotoCopy = (IClusterPrototype) SerialCopier.makeCopy(proto);
			assertTrue("Copy not null", ptotoCopy !=null );
		}catch(Exception e) {
			e.printStackTrace();
			fail("Serialization copy failure!");
		}
	}

}
