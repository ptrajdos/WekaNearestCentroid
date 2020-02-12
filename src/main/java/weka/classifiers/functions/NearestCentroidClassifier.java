/**
 * 
 */
package weka.classifiers.functions;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.nearestCentroid.IClusterPrototype;
import weka.classifiers.functions.nearestCentroid.prototypes.MahalanobisPrototype;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.RevisionUtils;
import weka.core.Utils;
import weka.core.UtilsPT;
import weka.tools.SerialCopier;
import weka.tools.data.InstancesOperator;

/**
 * Class implementing Nearest centroid classifier
 * @author Pawel Trajdos
 * @since 0.0.1
 * @version 4.0.0
 *
 */
public class NearestCentroidClassifier extends AbstractClassifier{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8462836067571523903L;
	
	/**
	 * Prototype for cluster prototypes
	 */
	protected IClusterPrototype clusProto;
	
	/**
	 * Cluster/Class prototype/centroid
	 */
	protected IClusterPrototype[] prototypes;
	
	protected boolean[] isPrototypeActive;
	/**
	 * 
	 */
	public NearestCentroidClassifier() {
		super();
		this.clusProto = new MahalanobisPrototype();
	}

	/**
	 * Builds the classifier.
	 * @param data -- training set
	 */
	@Override
	public void buildClassifier(Instances data) throws Exception {
		if(!this.m_DoNotCheckCapabilities)
			this.getCapabilities().testWithFail(data);
		
		Instances[] splittedSets = InstancesOperator.classSpecSplit(data);
		
		int numClasses = splittedSets.length;
		this.isPrototypeActive = new boolean[numClasses];
		this.prototypes = new IClusterPrototype[numClasses];
		for(int c =0;c<numClasses;c++) {
			this.prototypes[c] = (IClusterPrototype) SerialCopier.makeCopy(this.clusProto);
			this.prototypes[c].build(splittedSets[c]);
			this.isPrototypeActive[c] = splittedSets[c].numInstances()>0? true:false;
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see weka.classifiers.AbstractClassifier#distributionForInstance(weka.core.Instance)
	 */
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		int numClasses = this.prototypes.length;
		double[] distribution = new double[numClasses];
		
		double sum=0;
		double distance = 0;
		for(int c=0;c<numClasses;c++) {
			distance  = this.isPrototypeActive[c]? this.prototypes[c].distance(instance):Double.POSITIVE_INFINITY;
			distribution[c] = Math.exp(-distance);
			sum+= distribution[c];
		}
		
		if(!Utils.eq(0.0, sum)) 
			Utils.normalize(distribution, sum);
		
		return distribution;
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.AbstractClassifier#listOptions()
	 */
	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tThe cluster prototype to use "+
		          "(default: weka.classifiers.functions.nearestCentroid.prototypes.MahalanobisPrototype).\n",
			      "P", 1, "-P"));
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}

	/**
	 * Gets current options of NearesCentroidClassifier
	 */
	@Override
	public String[] getOptions() {
		

	    Vector<String> options = new Vector<String>();
	    
	    
	    
	    options.add("-P");
	    options.add(UtilsPT.getClassAndOptions(this.getClusProto()));
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	}

	/** 
	 * Parses and sets options
	 *<!-- options-start -->
	   * <p>Valid options are:</p>
	   *
	   * <pre> -P
	   *  The cluster prototype to use.</pre>
	   *
	   * <!-- options-end -->
	 */
	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setClusProto((IClusterPrototype) UtilsPT.parseObjectOptions(options, "P", new MahalanobisPrototype(), IClusterPrototype.class));
		
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
	 
	    // instances
	    caps.setMinimumNumberInstances(2);
	    
		return caps; 
	}
	
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer("Nearest Centroid Classifier\n");
		if(this.prototypes != null) {
			int numClasses = this.prototypes.length;
			for(int c =0;c<numClasses;c++) {
				buff.append(this.prototypes[c].getCenterPoint().toString() + "\n");
			}
		}
		return buff.toString();
	}

	/**
	 * 
	 * @return The array of calculated centroids
	 */
	public Instance[] getCentroids() throws Exception {
		if(this.prototypes == null)
			throw new Exception("No model has been built yet!");
		int numClasses = this.prototypes.length;
		Instance[] insts = new Instance[numClasses];
		for(int c =0;c<numClasses;c++) {
			insts[c] = this.prototypes[c].getCenterPoint();
		}
		return insts;
	}

		
	/* (non-Javadoc)
	 * @see weka.classifiers.AbstractClassifier#getRevision()
	 */
	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 2 $");
	}
	

	
	public String globalInfo(){
		return "Performs the Nearest Centroid classification";
	}

	/**
	 * @return the clusProto
	 */
	public IClusterPrototype getClusProto() {
		return this.clusProto;
	}

	/**
	 * @param clusProto the clusProto to set
	 */
	public void setClusProto(IClusterPrototype clusProto) {
		this.clusProto = clusProto;
	}

	public String clusProtoTipText() {
		return "Set the cluster prototype to be used"; 
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		runClassifier(new NearestCentroidClassifier(), args);

	}


}
