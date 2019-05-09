/**
 * 
 */
package weka.classifiers.functions.nearestCentroid.prototypes;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.functions.nearestCentroid.IClusterCentroidFinder;
import weka.classifiers.functions.nearestCentroid.IClusterPrototype;
import weka.classifiers.functions.nearestCentroid.clusterCentroidFinders.ClusterCentroidFinderMeanOrMode;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.NormalizableDistance;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Utils;

/**
 * A class representing a customizable cluster prototype
 * 
 * @author pawel trajdos
 * @since 3.0.0
 * @version 3.0.0
 *
 */
public class CustomizablePrototype implements IClusterPrototype, Serializable, OptionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3987651059107118999L;
	
	/**
	 * Distance function to tune and use.
	 */
	protected DistanceFunction distanceFunction;
	
	/**
	 * Object that finds cluster centroid
	 */
	protected IClusterCentroidFinder centroidFinder;
	
	/**
	 * The centroid found
	 */
	protected Instance centralPoint;

	/**
	 * Default constructor
	 */
	public CustomizablePrototype() {
		
		this.distanceFunction = new EuclideanDistance();
		this.centroidFinder = new ClusterCentroidFinderMeanOrMode();
		
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.functions.nearestCentroid.IClassPrototype#getCenterPoitn()
	 */
	@Override
	public Instance getCenterPoint() {
		return this.centralPoint;
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.functions.nearestCentroid.IClassPrototype#learn(weka.core.Instances)
	 */
	@Override
	public void build(Instances data)throws Exception {
		this.distanceFunction.setInstances(data);
		this.centralPoint = this.centroidFinder.findCentroid(data);

	}

	/* (non-Javadoc)
	 * @see weka.classifiers.functions.nearestCentroid.IClassPrototype#distance(weka.core.Instance)
	 */
	@Override
	public double distance(Instance inst) {
		return this.distanceFunction.distance(inst, this.centralPoint);
	}
	
	@Override
	public double distance(Instance inst1, Instance inst2) {
		return this.distanceFunction.distance(inst1, inst2);
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.functions.nearestCentroid.IClassPrototype#isValid()
	 */
	@Override
	public boolean isValid() {
		return this.centralPoint != null & this.distanceFunction.getInstances()!=null;
	}
	

	/**
	 * @return the distanceFunction
	 */
	public DistanceFunction getDistanceFunction() {
		return this.distanceFunction;
	}

	/**
	 * @param distanceFunction the distanceFunction to set
	 */
	public void setDistanceFunction(DistanceFunction distanceFunction) {
		this.distanceFunction = distanceFunction;
	}

	/**
	 * @return the centroidFinder
	 */
	public IClusterCentroidFinder getCentroidFinder() {
		return this.centroidFinder;
	}

	/**
	 * @param centroidFinder the centroidFinder to set
	 */
	public void setCentroidFinder(IClusterCentroidFinder centroidFinder) {
		this.centroidFinder = centroidFinder;
	}

	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tThe distance function to use "+
		          "(default: weka.core.EuclideanDistance).\n",
			      "D", 0, "-D"));
		 
		 newVector.addElement(new Option(
			      "\tThe centroid calculator object to use "+
		          "(default: weka.classifiers.functions.nearestCentroid.clusterCentroidFinders.ClusterCentroidFinderMeanOrMode).\n",
			      "CF", 0, "-CF"));
		 
		    
		return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		String distanceFunString = Utils.getOption('D', options);
	    if(distanceFunString.length() != 0) {
	      String distanceFunClassSpec[] = Utils.splitOptions(distanceFunString);
	      if(distanceFunClassSpec.length == 0) { 
	        throw new Exception("Invalid Distance function " +
	                            "specification string."); 
	      }
	      String className = distanceFunClassSpec[0];
	      distanceFunClassSpec[0] = "";

	      this.setDistanceFunction( (DistanceFunction)
	                  Utils.forName( NormalizableDistance.class, 
	                                 className, 
	                                 distanceFunClassSpec)
	                                        );
	    }
	    else 
	      this.setDistanceFunction(new EuclideanDistance());
	    
	    String centroidFinderString = Utils.getOption("CF", options);
	    if(centroidFinderString.length() !=0) {
	    	String[] centroidFinderOptions = Utils.splitOptions(centroidFinderString);
	    	if(centroidFinderOptions.length == 0) { 
		        throw new Exception("Invalid Distance function " +
		                            "specification string."); 
		      }
		      String className = centroidFinderOptions[0];
		      centroidFinderOptions[0] = "";
		      this.setCentroidFinder( (IClusterCentroidFinder) Utils.forName(IClusterCentroidFinder.class, className, centroidFinderOptions));
	    }else
	    	this.setCentroidFinder(new ClusterCentroidFinderMeanOrMode());
		
	}

	@Override
	public String[] getOptions() {
Vector<String> options = new Vector<String>();
	    

	    options.add("-D");
	    options.add(this.distanceFunction.getClass().getName()+" "+Utils.joinOptions(this.distanceFunction.getOptions()).trim()); 
	    
	    options.add("-CF");
	    String finderOptions = this.centralPoint instanceof OptionHandler? " "+Utils.joinOptions(((OptionHandler) this.centroidFinder).getOptions()).trim(): ""; 
	    options.add(this.centroidFinder.getClass().getName()+finderOptions);
	    
	    
	    return options.toArray(new String[0]);
	}

}
