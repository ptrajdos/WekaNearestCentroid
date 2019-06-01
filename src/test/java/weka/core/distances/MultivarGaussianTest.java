package weka.core.distances;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.tools.SerialCopier;

public class MultivarGaussianTest {

	@Test
	public void test() {
		double[][] testData = new double[][] {
			{-3},{-2},{-1},{0},{1},{2},{3}
		};
		MultivarGaussian gauss = new MultivarGaussian();
		gauss.estimate(testData, null);
		
		assertEquals(0, gauss.getMahalanobisDistToCenter(new double[] {0}), 1e-6);
		
		double expVal1 = Math.sqrt(1.0/(4));
		assertEquals(expVal1, gauss.getMahalanobisDistToCenter(new double[] {1}), 1e-6);
		assertEquals(expVal1, gauss.getMahalanobisDistToCenter(new double[] {-1}), 1e-6);
		assertEquals(expVal1, gauss.getMahalanobisDist(new double[] {0.5}, new double[] {-0.5}), 1e-6);
		double expVal2 = Math.sqrt(16.0/(4));
		assertEquals(expVal2, gauss.getMahalanobisDistToCenter(new double[] {4}), 1e-6);
		assertEquals(expVal2, gauss.getMahalanobisDistToCenter(new double[] {-4}), 1e-6);
		assertEquals(expVal2, gauss.getMahalanobisDist(new double[] {2}, new double[] {-2}), 1e-6);
		
		try {
			MultivarGaussian copy = (MultivarGaussian) SerialCopier.makeCopy(gauss);
		}catch(Exception e) {
			e.printStackTrace();
			fail("No serialization is possible");//TODO sth is wrong!
		}
	}

}
