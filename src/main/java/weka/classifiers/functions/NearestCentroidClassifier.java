/**
 * 
 */
package weka.classifiers.functions;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.AbstractClassifier;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.DenseInstance;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.NormalizableDistance;
import weka.core.Option;
import weka.core.Utils;

/**
 * @author Pawel Trajdos
 *
 */
public class NearestCentroidClassifier extends AbstractClassifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8462836067571523903L;
	
	protected DistanceFunction distFun = new EuclideanDistance();
	protected Instance[] centroids = null;
	

	/**
	 * 
	 */
	public NearestCentroidClassifier() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Builds the classifier.
	 * @param data -- training set
	 */
	@Override
	public void buildClassifier(Instances data) throws Exception {
		this.getCapabilities().testWithFail(data);
		this.distFun.setInstances(data);
		
		int numClasses = data.numClasses();
		int numAttrs = data.numAttributes();
		this.centroids = new Instance[numClasses];
		
		int classIdx = data.classIndex();
		double[][] centroidsDoubles = new double[numClasses][numAttrs];
		int[] classObjCounts = new int[numClasses];
		
		//Initialise centroids
		for(int i=0;i<numClasses;i++){
			this.centroids[i] = new DenseInstance(numAttrs);
		}
		//calculate centroids
		int numInstances = data.numInstances();
		int classNum = 0;
		double[] instanceRep = null;
		for(int i=0;i<numInstances;i++){
			instanceRep = data.get(i).toDoubleArray();
			classNum = (int) instanceRep[classIdx];
			classObjCounts[classNum]++;
			for(int a=0;a<numAttrs;a++){
				centroidsDoubles[classNum][a] +=instanceRep[a];
			}
		}
		
		for(int c=0;c<numClasses;c++)
			for(int a =0;a<numAttrs;a++){
				centroidsDoubles[c][a]/=classObjCounts[c];
				this.centroids[c].setValue(a, centroidsDoubles[c][a]);
			}
		
	
	}
	
	
	
	

	/* (non-Javadoc)
	 * @see weka.classifiers.AbstractClassifier#distributionForInstance(weka.core.Instance)
	 */
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		
		int numClasses = this.centroids.length;
		double[] distribution = new double[numClasses];
		double distSum =0;
		double tmp=0;
		
		for(int c =0;c<numClasses;c++){
			tmp = this.distFun.distance(this.centroids[c], instance);
			distribution[c] = tmp;
			distSum+=tmp;
		}
		
		double invDistSum=0;
		for(int c =0;c<numClasses;c++){
			distribution[c]=(distSum - distribution[c])/distSum;
			invDistSum+=distribution[c];
		}
		
		for(int c =0;c<numClasses;c++){
			distribution[c]/=invDistSum;
		}
		
		
		return distribution;
		
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.AbstractClassifier#listOptions()
	 */
	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tThe distance function to use "+
		          "(default: weka.core.EuclideanDistance).\n",
			      "A", 0, "-A"));
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}

	/**
	 * Gets current options of NearesCentroidClassifier
	 */
	@Override
	public String[] getOptions() {
		

	    Vector<String> options = new Vector<String>();
	    

	    options.add("-A");
	    options.add(this.distFun.getClass().getName()+" "+Utils.joinOptions(this.distFun.getOptions())); 
	    
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	}

	/** 
	 * Parses and sets options
	 *<!-- options-start -->
	   * <p>Valid options are:</p>
	   *
	   * <pre> -A
	   *  The distance function to use.</pre>
	   *
	   * <!-- options-end -->
	 */
	@Override
	public void setOptions(String[] options) throws Exception {
		
		String distanceFunString = Utils.getOption('A', options);
	    if(distanceFunString.length() != 0) {
	      String distanceFunClassSpec[] = Utils.splitOptions(distanceFunString);
	      if(distanceFunClassSpec.length == 0) { 
	        throw new Exception("Invalid Distance function " +
	                            "specification string."); 
	      }
	      String className = distanceFunClassSpec[0];
	      distanceFunClassSpec[0] = "";

	      this.setDistFun( (DistanceFunction)
	                  Utils.forName( NormalizableDistance.class, 
	                                 className, 
	                                 distanceFunClassSpec)
	                                        );
	    }
	    else 
	      this.setDistFun(new EuclideanDistance());
		
		super.setOptions(options);
		
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.AbstractClassifier#getCapabilities()
	 */
	@Override
	public Capabilities getCapabilities() {
		Capabilities caps = super.getCapabilities(); 
		caps.enable(Capability.NUMERIC_ATTRIBUTES);
		
		// class
	    caps.enable(Capability.NOMINAL_CLASS);
	    caps.enable(Capability.MISSING_CLASS_VALUES);

	    // instances
	    caps.setMinimumNumberInstances(1);
		return caps; 
	}
	
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		if(this.centroids == null){
			return "The model has not been built yet.";
		}
		
		StringBuffer result = new StringBuffer();
		result.append("Nearest Centroid Classifier: \n\nCentroids:\n");
		for(int c=0;c<this.centroids.length;c++){
			result.append("Class " + c +":" + this.centroids[c]+"\n");
		}
		
		
		return result.toString();
	}

	/**
	 * 
	 * @return The array of calculated centroids
	 */
	public Instance[] getCentroids() {
		return this.centroids;
	}

	/**
	 * @return The used distance function
	 */
	public DistanceFunction getDistFun() {
		return this.distFun;
	}

	/**
	 * @param distFun --  the distance funtion to set
	 */
	public void setDistFun(DistanceFunction distFun) {
		this.distFun = distFun;
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		runClassifier(new NearestCentroidClassifier(), args);

	}

}
