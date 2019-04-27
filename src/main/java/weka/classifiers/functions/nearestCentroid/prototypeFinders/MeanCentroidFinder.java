/**
 * 
 */
package weka.classifiers.functions.nearestCentroid.prototypeFinders;

import java.util.Enumeration;

import weka.classifiers.functions.nearestCentroid.ACentroidFinder;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.Utils;

/**
 * @author pawel trajdos
 * @since 2.0.0
 * @version 2.0.0
 *
 */
public class MeanCentroidFinder extends ACentroidFinder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7692932301634559415L;
	
	private Instance[] centroids;
	private boolean[] activeCentroids;

	/* (non-Javadoc)
	 * @see weka.classifiers.functions.nearestCentroid.IPrototypeFinder#findPrototypes(weka.core.Instances)
	 */
	@Override
	public void findCentroids(Instances data) throws Exception {
		this.getCapabilities().testWithFail(data);
		
		
		
		int numClasses = data.numClasses();
		int numAttrs = data.numAttributes();
		this.centroids = new Instance[numClasses];
		this.activeCentroids = new boolean[numClasses];
		
		int classIdx = data.classIndex();
		double[][] centroidsDoubles = new double[numClasses][numAttrs];
		double[] classObjCounts = new double[numClasses];
		
		//Initialise centroids
		for(int i=0;i<numClasses;i++){
			this.centroids[i] = new DenseInstance(data.get(0));
			this.centroids[i].setDataset(data);
			this.centroids[i].setClassValue(i);
		}
		//calculate centroids
		int numInstances = data.numInstances();
		int classNum = 0;
		double[] instanceRep = null;
		Instance tmpInstance = null;
		for(int i=0;i<numInstances;i++){
			tmpInstance = data.get(i);
			instanceRep = tmpInstance.toDoubleArray();
			if(Utils.isMissingValue(tmpInstance.classValue()))
				continue;
			double weight = data.get(i).weight();
			classNum = (int) instanceRep[classIdx];
			this.activeCentroids[classNum] = true;
			classObjCounts[classNum]+=weight;
			for(int a=0;a<numAttrs;a++){
				centroidsDoubles[classNum][a] +=instanceRep[a]*weight;
			}
		}
		
		for(int c=0;c<numClasses;c++)
			for(int a =0;a<numAttrs;a++){
				centroidsDoubles[c][a]/=classObjCounts[c];
				this.centroids[c].setValue(a, centroidsDoubles[c][a]);
			}
		
	
		
	}

	@Override
	public Instance getCentroid(int classIdx) throws Exception {
		if(this.centroids == null) throw new Exception("Model is not built");
		return this.centroids[classIdx];
	}

	@Override
	public boolean isCentroidActive(int classIdx) throws Exception {
		if(this.centroids == null) throw new Exception("Model is not built");
		return this.activeCentroids[classIdx];
	}

	@Override
	public int getCentroidNum() {
		return this.centroids.length;
	}

	@Override
	public boolean isModelBuilt() {
		return this.centroids!=null;
	}

	@Override
	public Enumeration<Option> listOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getOptions() {
		return  new String[] {""};
	}



}
