/**
 * 
 */
package weka.core.distances;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import weka.core.DenseInstance;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Range;
import weka.core.RevisionHandler;
import weka.core.Utils;
import weka.core.neighboursearch.PerformanceStats;

/**
 * The class is responsible for calculating the mahalanobis distance in the euclidean space.
 * All <b>nominal/string/date</b> attributes are <b>ignored</b> since for such attributes the variance/covariance cannot be calculated.
 * @author pawel trajdos
 * @since 3.0.0
 * @version 3.1.0
 *
 */
public class MahalanobisDistance implements DistanceFunction, Serializable, OptionHandler, RevisionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2278385414140196375L;
	
	/**
	 * Reference data
	 */
	protected Instances refData;
	
	protected Range attRange;
	
	protected boolean validated=false;
	
	protected boolean[] activeAttrs;
	protected List<Integer> activeIndices;
	
	protected MultivarGaussian gaussEst;
	
	protected boolean noInstances=false;
	
	protected boolean normalize=false;
	

	/**
	 * @return the normalize
	 */
	public boolean isNormalize() {
		return this.normalize;
	}

	/**
	 * @param normalize the normalize to set
	 */
	public void setNormalize(boolean normalize) {
		this.normalize = normalize;
	}

	/**
	 * 
	 */
	public MahalanobisDistance(Instances data) {
		this.setInstances(data);
		this.attRange = new Range("first-last");
		validate();
	}
	
	public MahalanobisDistance() {
		this.attRange = new Range("first-last");
		invalidate();
	}

	/* (non-Javadoc)
	 * @see weka.core.OptionHandler#listOptions()
	 */
	@Override
	public Enumeration<Option> listOptions() {
	    Vector<Option> result = new Vector<Option>();

	    result.addElement(new Option(
	      "\tSpecifies list of columns to used in the calculation of the \n"
	        + "\tdistance. 'first' and 'last' are valid indices.\n"
	        + "\t(default: first-last)", "R", 1, "-R <col1,col2-col4,...>"));

	    result.addElement(new Option("\tInvert matching sense of column indices.",
	      "V", 0, "-V"));
	    
	    result.addElement(new Option("\tDistance normalization.",
	  	      "N", 0, "-N"));

	    return result.elements();
	}

	/* (non-Javadoc)
	 * @see weka.core.OptionHandler#setOptions(java.lang.String[])
	 */
	@Override
	public void setOptions(String[] options) throws Exception {
		 String tmpStr;

		    tmpStr = Utils.getOption('R', options);
		    if (tmpStr.length() != 0) {
		      setAttributeIndices(tmpStr);
		    } else {
		      setAttributeIndices("first-last");
		    }

		    setInvertSelection(Utils.getFlag('V', options));
		    setNormalize(Utils.getFlag('N', options));

	}

	/* (non-Javadoc)
	 * @see weka.core.OptionHandler#getOptions()
	 */
	@Override
	public String[] getOptions() {
	    Vector<String> result;

	    result = new Vector<String>();

	    result.add("-R");
	    result.add(getAttributeIndices());

	    if (getInvertSelection()) {
	      result.add("-V");
	    }
	    if(isNormalize())
	    	result.add("-N");

	    return result.toArray(new String[result.size()]);
	}

	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#setInstances(weka.core.Instances)
	 */
	@Override
	public void setInstances(Instances insts) {
		this.refData = insts;
		invalidate();

	}

	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#getInstances()
	 */
	@Override
	public Instances getInstances() {
		return this.refData;
	}
	/**
	   * Returns the tip text for this property.
	   * 
	   * @return tip text for this property suitable for displaying in the
	   *         explorer/experimenter gui
	   */
	  public String attributeIndicesTipText() {
	    return "Specify range of attributes to act on. "
	      + "This is a comma separated list of attribute indices, with "
	      + "\"first\" and \"last\" valid values. Specify an inclusive "
	      + "range with \"-\". E.g: \"first-3,5,6-10,last\".";
	  }


	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#setAttributeIndices(java.lang.String)
	 */
	@Override
	public void setAttributeIndices(String value) {
		this.attRange.setRanges(value);
		invalidate();
		validate();

	}

	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#getAttributeIndices()
	 */
	@Override
	public String getAttributeIndices() {
		return this.attRange.getRanges();
	}
	
	/**
	   * Returns the tip text for this property.
	   * 
	   * @return tip text for this property suitable for displaying in the
	   *         explorer/experimenter gui
	   */
	  public String invertSelectionTipText() {
	    return "Set attribute selection mode. If false, only selected "
	      + "attributes in the range will be used in the distance calculation; if "
	      + "true, only non-selected attributes will be used for the calculation.";
	  }


	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#setInvertSelection(boolean)
	 */
	@Override
	public void setInvertSelection(boolean value) {
		this.attRange.setInvert(value);
		invalidate();
	}

	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#getInvertSelection()
	 */
	@Override
	public boolean getInvertSelection() {
		return attRange.getInvert();
	}

	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#distance(weka.core.Instance, weka.core.Instance)
	 */
	@Override
	public double distance(Instance first, Instance second) {
		return this.distance(first, second, Double.POSITIVE_INFINITY, null);
	}

	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#distance(weka.core.Instance, weka.core.Instance, weka.core.neighboursearch.PerformanceStats)
	 */
	@Override
	public double distance(Instance first, Instance second, PerformanceStats stats) throws Exception {
		return this.distance(first, second, Double.POSITIVE_INFINITY, stats);
	}

	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#distance(weka.core.Instance, weka.core.Instance, double)
	 */
	@Override
	public double distance(Instance first, Instance second, double cutOffValue) {
		return this.distance(first, second, cutOffValue, null);
	}

	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#distance(weka.core.Instance, weka.core.Instance, double, weka.core.neighboursearch.PerformanceStats)
	 */
	@Override
	public double distance(Instance first, Instance second, double cutOffValue, PerformanceStats stats) {
		validate();
		
		if(this.noInstances) {
			EuclideanDistance eucDist = new EuclideanDistance(this.refData);
			return eucDist.distance(first, second,cutOffValue,stats);
		}
		
		double[] inst1 = this.transformInstance(first);
		double[] inst2 = this.transformInstance(second);
		
		double dist = this.gaussEst.getMahalanobisDist(inst1, inst2);
		dist = dist>cutOffValue? Double.POSITIVE_INFINITY:dist;
		
		if (stats != null) {
	        stats.incrCoordCount();
	      }
		
		if(isNormalize()) {
			int numAttrs = this.activeIndices.size();
			dist/= Math.sqrt(numAttrs);
		}
		
		return dist;
	}

	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#postProcessDistances(double[])
	 */
	@Override
	public void postProcessDistances(double[] distances) {
		// TODO No need for post-processing?

	}

	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#update(weka.core.Instance)
	 */
	@Override
	public void update(Instance ins) {
		// TODO unimplemented yet

	}

	/* (non-Javadoc)
	 * @see weka.core.DistanceFunction#clean()
	 */
	@Override
	public void clean() {
		this.refData = new Instances(this.refData, 0);
		
	}

	@Override
	public String getRevision() {
		return "3.0.5";
	}
	
	protected void invalidate() {
		this.validated = false;
	}
	
	protected void validate() {
		if(!this.validated) {
			initialise();
			this.validated = true;
		}	
	}
	
	protected void initialise() {
		initialiseAttributeIndices();
		initialiseEstimator();
	}
	
	protected void initialiseAttributeIndices() {
		int numAttrs = this.refData.numAttributes();
		
		this.activeIndices = new LinkedList<Integer>();
		
		this.attRange.setUpper(numAttrs -1);
		this.activeAttrs = new boolean[numAttrs];
		for(int i=0;i<numAttrs;i++) {
			if(i == this.refData.classIndex())continue;
			this.activeAttrs[i] = this.attRange.isInRange(i) && this.refData.attribute(i).isNumeric() ;
			if(this.activeAttrs[i])this.activeIndices.add(i);
		}
		
	}
	
	protected void initialiseEstimator() {
		this.gaussEst = new MultivarGaussian();
		
		int numInst = this.refData.numInstances();
		if(numInst ==0) {
			this.noInstances=true;
			return;
		}
		int numValAttrs = this.activeIndices.size();
		double[] instWeights = new double[numInst];
		
		double[][] instRepresentation = new double[numInst][numValAttrs];
		Instance tmpInst;
		double[] iRep;
		for(int i =0;i<numInst;i++) {
			tmpInst =this.refData.get(i); 
			iRep = tmpInst.toDoubleArray();
			instWeights[i] = tmpInst.weight();
			for(int a =0;a<numValAttrs;a++) {
				instRepresentation[i][a] = iRep[this.activeIndices.get(a)];
			}
		}
		try {
		this.gaussEst.estimate(instRepresentation, instWeights);//TODO lack of instances?
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected double[] transformInstance(Instance inst) {
		int numActAttrs = this.activeIndices.size();
		double[] instRepTrans = new double[numActAttrs];
		double[] instRep = inst.toDoubleArray();
		for(int a=0;a<numActAttrs;a++)
			instRepTrans[a] = instRep[this.activeIndices.get(a)];
		
		return instRepTrans;
	}
	
	protected Instance inverseInstanceTransform(double[] vals) {
		int numAttrs = this.activeAttrs.length;
		if(vals.length != this.activeIndices.size())return null;
		
		Instance tmpInstance = this.refData.get(0);
		double[] representation = tmpInstance.toDoubleArray();
		
		int actCounter = 0;
		for(int a=0;a<numAttrs;a++) {
			if(this.activeAttrs[a]) {
				representation[a] = vals[actCounter++];
			}else {
				representation[a] = Double.NaN;
			}
		}
		
		return tmpInstance.copy(representation);
		
	}
	
	public Instance getCentroid() {
		validate();
		if(this.noInstances) {
			int numAttrs = this.refData.numAttributes();
			double[] vals =  new double[numAttrs];
			vals[this.refData.classIndex()] = Double.NaN;
			Instance tmp = new DenseInstance(1.0, vals);
			tmp.setDataset(this.refData);
			return tmp;
		}
		return this.inverseInstanceTransform(this.gaussEst.getMean());
	}

}
