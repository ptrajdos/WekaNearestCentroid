package weka.core.distances;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class MahalanobisDistanceTest {

	@Test
	public void test() {
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
	      
	      Instances dataset = new Instances("daataset",atts,1);
	      dataset.setClassIndex(2);
	      
	      
	      dataset.add(new DenseInstance(1.0, new double[] {-3,0,1}));
	      dataset.add(new DenseInstance(1.0, new double[] {-2,1,1}));
	      dataset.add(new DenseInstance(1.0, new double[] {-1,1,1}));
	      dataset.add(new DenseInstance(1.0, new double[] {0,0,1}));
	      dataset.add(new DenseInstance(1.0, new double[] {1,0,0}));
	      dataset.add(new DenseInstance(1.0, new double[] {2,1,0}));
	      dataset.add(new DenseInstance(1.0, new double[] {3,1,0}));
	      
	      MahalanobisDistance mahD = new MahalanobisDistance();
	      mahD.setInstances(dataset);
	      
	      Instance t1 = new DenseInstance(1.0, new double[] {3,0,0});
	      assertEquals(0, mahD.distance(t1, t1), 1e-6);
	      
	      t1 = new DenseInstance(1.0, new double[] {5,0,0});
	      assertEquals(0, mahD.distance(t1, t1), 1e-6);
	      
	      Instance t2 = new DenseInstance(1.0, new double[] {5,0,1});
	      assertEquals(0, mahD.distance(t1, t2), 1e-6);
	      
	      t1 = new DenseInstance(1.0, new double[] {4,0,0});
	      double expVal = Math.sqrt(1.0/4);
	      assertEquals(expVal, mahD.distance(t1, t2), 1e-6);
	      
	      t1 = new DenseInstance(1.0, new double[] {4,1,0});
	      assertEquals(expVal, mahD.distance(t1, t2), 1e-6);
	      
	      double[] instanceProto = new double[] {0,Double.NaN,Double.NaN};
	      Instance meanInstance = new DenseInstance(1.0, instanceProto);
	      
	      Instance gettedMean = mahD.getCentroid();
	      double[] instanceRep = gettedMean.toDoubleArray();
	      assertTrue("Centroid", Arrays.equals(instanceRep, instanceProto));
	}
	@Test
	public void test2() {
		ArrayList<Attribute> atts = new ArrayList<Attribute>(3);
		
		LinkedList<String> valList2 = new LinkedList<String>();
	      valList2.add("A");
	      valList2.add("B");
	      atts.add(new Attribute("Nomi1", valList2));
	      
	      atts.add(new Attribute("X1"));
	      LinkedList<String> valList = new LinkedList<String>();
	      valList.add("1");
	      valList.add("2");
	      atts.add(new Attribute("Class", valList));
	      
	      
	      
	      Instances dataset = new Instances("daataset",atts,1);
	      dataset.setClassIndex(0);
	      
	      
	      dataset.add(new DenseInstance(1.0, new double[] {1,-3,1}));
	      dataset.add(new DenseInstance(1.0, new double[] {1,-2,1}));
	      dataset.add(new DenseInstance(1.0, new double[] {1,-1,1}));
	      dataset.add(new DenseInstance(1.0, new double[] {1,0,1}));
	      dataset.add(new DenseInstance(1.0, new double[] {0,1,0}));
	      dataset.add(new DenseInstance(1.0, new double[] {0,2,0}));
	      dataset.add(new DenseInstance(1.0, new double[] {0,3,0}));
	      
	      MahalanobisDistance mahD = new MahalanobisDistance();
	      mahD.setInstances(dataset);
	      
	      Instance t1 = new DenseInstance(1.0, new double[] {0,3,0});
	      assertEquals(0, mahD.distance(t1, t1), 1e-6);
	      
	      t1 = new DenseInstance(1.0, new double[] {0,5,0});
	      assertEquals(0, mahD.distance(t1, t1), 1e-6);
	      
	      Instance t2 = new DenseInstance(1.0, new double[] {0,5,1});
	      assertEquals(0, mahD.distance(t1, t2), 1e-6);
	      
	      t1 = new DenseInstance(1.0, new double[] {0,4,0});
	      double expVal = Math.sqrt(1.0/4);
	      assertEquals(expVal, mahD.distance(t1, t2), 1e-6);
	      
	      t1 = new DenseInstance(1.0, new double[] {0,4,0});
	      assertEquals(expVal, mahD.distance(t1, t2), 1e-6);
	      
	      double[] instanceProto = new double[] {Double.NaN,0,Double.NaN};
	      Instance meanInstance = new DenseInstance(1.0, instanceProto);
	      
	      Instance gettedMean = mahD.getCentroid();
	      double[] instanceRep = gettedMean.toDoubleArray();
	      assertTrue("Centroid", Arrays.equals(instanceRep, instanceProto));
	      
	      
	      
	      
			
	}


}
