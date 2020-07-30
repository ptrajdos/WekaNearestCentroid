/**
 * 
 */
package weka.classifiers.functions.nearestCentroid.prototypes;

import java.util.ArrayList;
import java.util.LinkedList;

import junit.framework.TestCase;
import weka.classifiers.functions.nearestCentroid.IClusterPrototype;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.tools.SerialCopier;

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
	
	
	public void testPrototype() {
		IClusterPrototype proto = this.getPrototype();
		
		Instances dataset = this.getTestdata();
		
		try {
	    	assertFalse("Is cnetroid invalid", proto.isValid());
			proto.build(dataset);
			assertTrue("Is cnetroid valid", proto.isValid());
			Instance centroid = proto.getCenterPoint();
			
			double distance = proto.distance(dataset.get(0), dataset.get(0));
			assertTrue("The same instances distance", Utils.eq(distance, 0.0));
			
			distance = proto.distance(dataset.get(0), dataset.get(1));
			assertTrue("Different instances distance", Utils.gr(distance, 0.0));
		
			assertTrue("Centroid check ", Utils.eq(centroid.toDoubleArray()[0], 0));
			
			distance = proto.distance(centroid);
			assertTrue("Distance to Centroid (from centroid) ", Utils.eq(distance, 0.0));
			
			distance =  proto.distance(dataset.get(0));
			assertTrue("Distance to Centroid (from different instance) ", Utils.gr(distance, 0.0));
		} catch (Exception e) {
			e.printStackTrace();
			fail("An exception has been caught");
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
