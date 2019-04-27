/**
 * 
 */
package weka.classifiers.functions;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.nearestCentroid.ICentroidFinder;
import weka.classifiers.functions.nearestCentroid.prototypeFinders.MeanCentroidFinder;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.DenseInstance;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.NormalizableDistance;
import weka.core.Option;
import weka.core.RevisionUtils;
import weka.core.Utils;
import weka.core.WeightedInstancesHandler;

/**
 * @author Pawel Trajdos
 * @since 0.0.1
 * @version 2.0.0
 *
 */
public class NearestCentroidClassifier extends AbstractClassifier implements WeightedInstancesHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8462836067571523903L;
	
	protected DistanceFunction distFun = null;
	protected ICentroidFinder centFinder;
	/**
	 * 
	 */
	public NearestCentroidClassifier() {
		
		EuclideanDistance tmpDist = new EuclideanDistance();
		tmpDist.setDontNormalize(true);
		this.distFun = tmpDist;
		this.centFinder = new MeanCentroidFinder();

	}

	/**
	 * Builds the classifier.
	 * @param data -- training set
	 */
	@Override
	public void buildClassifier(Instances data) throws Exception {
		this.getCapabilities().testWithFail(data);
		this.distFun.setInstances(data);
		this.centFinder.findCentroids(data);
	}
	
	
	
	

	/* (non-Javadoc)
	 * @see weka.classifiers.AbstractClassifier#distributionForInstance(weka.core.Instance)
	 */
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		
		int numClasses = this.centFinder.getCentroidNum();
		double[] distribution = new double[numClasses];
	
		double distSum =0;
		double tmp=0;
		int maxIdx =0;
		double max = -Double.MAX_VALUE;
		for(int c =0;c<numClasses;c++){
			tmp = this.distFun.distance(this.centFinder.getCentroid(c), instance);
			if(tmp > max){
				max = tmp;
				maxIdx = c;
			}
			tmp = Math.exp(-2*tmp);
			if(this.centFinder.isCentroidActive(c)) {
				distribution[c] = tmp;
				distSum+=tmp;
			}
		}
		boolean err =false;
		
		err = Utils.eq(distSum, 0)? true:false;
		
		if(!err)
		for(int c =0;c<numClasses;c++){
			distribution[c]/=distSum;
			if(Utils.isMissingValue(distribution[c])){
				err=true;
				break;
			}
		}
		if(err){
			distribution = new double[numClasses];
			distribution[maxIdx] =1.0;
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
		 
		 newVector.addElement(new Option(
			      "\tThe centroid calculator object to use "+
		          "(default: weka.classifiers.functions.nearestCentroid.prototypeFinders.MeanCentroidFinder).\n",
			      "CF", 0, "-CF"));
		 
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
	    
	    options.add("-CF");
	    options.add(this.centFinder.getClass().getName()+" "+Utils.joinOptions(this.centFinder.getOptions()));
	    
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
	   *  <pre> -CF
	   *  The centroid finder object to use. </pre>
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
	    
	    String centroidFinderString = Utils.getOption("CF", options);
	    if(centroidFinderString.length() !=0) {
	    	String[] centroidFinderOptions = Utils.splitOptions(centroidFinderString);
	    	if(centroidFinderOptions.length == 0) { 
		        throw new Exception("Invalid Distance function " +
		                            "specification string."); 
		      }
		      String className = centroidFinderOptions[0];
		      centroidFinderOptions[0] = "";
		      this.setCentFinder( (ICentroidFinder) Utils.forName(ICentroidFinder.class, className, centroidFinderOptions));
	    }else
	    	this.setCentFinder(new MeanCentroidFinder());
		
		super.setOptions(options);
		
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.AbstractClassifier#getCapabilities()
	 */
	@Override
	public Capabilities getCapabilities() {
		Capabilities caps = super.getCapabilities();
		caps.disableAll();
		
		
	    caps.enable(Capability.NOMINAL_CLASS);
	    caps.enable(Capability.MISSING_CLASS_VALUES);
	    
	    caps.enable(Capability.NUMERIC_ATTRIBUTES);
	    caps.enable(Capability.BINARY_ATTRIBUTES);
	    
	    

	    // instances
	    caps.setMinimumNumberInstances(2);
	    
		return caps; 
	}
	
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		if(this.centFinder.isModelBuilt()){
			return "The model has not been built yet.";
		}
		StringBuffer result = new StringBuffer();
		try {
		result.append("Nearest Centroid Classifier: \n\nCentroids:\n");
		Attribute classAttrib = this.centFinder.getCentroid(0).classAttribute();
		int numCents = this.centFinder.getCentroidNum();
		for(int c=0;c<numCents;c++){
			result.append("Class " + classAttrib.value(c) +":" + this.centFinder.getCentroid(c)+"\n");
		}
		}catch(Exception e) {
			//NO exception mey be thrown
		}
		
		return result.toString();
	}

	/**
	 * 
	 * @return The array of calculated centroids
	 */
	public Instance[] getCentroids() {
		if(!this.centFinder.isModelBuilt())return null;
		int nCentrs = this.centFinder.getCentroidNum();
		Instance[] centrs = new Instance[nCentrs];
		try {
		for(int i=0;i<nCentrs;i++)
			centrs[i] = this.centFinder.getCentroid(i);
		}catch(Exception e) {/*No exceptions here*/}
		return centrs;
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
	
	public String distFunTipText(){
		return "Distance function to use with the classifier";
	}
	
	
	
	/**
	 * @return the centFinder
	 */
	public ICentroidFinder getCentFinder() {
		return this.centFinder;
	}

	/**
	 * @param centFinder the centFinder to set
	 */
	public void setCentFinder(ICentroidFinder centFinder) {
		this.centFinder = centFinder;
	}
	
	public String centFinderTipText(){
		return "Centroid finder to use with the classifier";
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.AbstractClassifier#getRevision()
	 */
	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 2 $");
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		runClassifier(new NearestCentroidClassifier(), args);

	}

	public String globalInfo(){
		return "Performs the Nearest Centroid classification";
	}

	
	
	

}
