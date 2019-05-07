/**
 * 
 */
package weka.classifiers.functions.nearestCentroid;

import java.io.Serializable;

import weka.core.Capabilities;
import weka.core.CapabilitiesHandler;
import weka.core.Instances;
import weka.core.OptionHandler;

/**
 * @author pawel
 *
 */
public abstract class AClusterCentroidFinder implements IClusterCentroidFinder, Serializable, CapabilitiesHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6496169380872496313L;

	
	/* (non-Javadoc)
	 * @see weka.core.CapabilitiesHandler#getCapabilities()
	 */
	@Override
	public Capabilities getCapabilities() {
		Capabilities caps = new Capabilities(this);
		caps.enableAll();
		return caps;
	}
	
	

}
