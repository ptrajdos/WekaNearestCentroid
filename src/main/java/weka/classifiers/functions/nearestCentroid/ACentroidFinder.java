/**
 * 
 */
package weka.classifiers.functions.nearestCentroid;

import java.io.Serializable;

import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.CapabilitiesHandler;

/**
 * @author pawel trajdos
 * @since 2.0.0
 * @version 2.0.0
 *
 */
public abstract class ACentroidFinder implements ICentroidFinder, Serializable, CapabilitiesHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3958773174084641925L;

	
	/* (non-Javadoc)
	 * @see weka.core.CapabilitiesHandler#getCapabilities()
	 */
	@Override
	public Capabilities getCapabilities() {
		Capabilities caps = new Capabilities(this);
		caps.disableAll();
		
		
	    caps.enable(Capability.NOMINAL_CLASS);
	    caps.enable(Capability.MISSING_CLASS_VALUES);
	    
	    caps.enable(Capability.NUMERIC_ATTRIBUTES);
	    caps.enable(Capability.BINARY_ATTRIBUTES);
	    // instances
	    caps.setMinimumNumberInstances(2);
	    
		return caps; 
	}

}
