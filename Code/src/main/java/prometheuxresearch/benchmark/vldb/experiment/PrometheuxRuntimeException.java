package prometheuxresearch.benchmark.vldb.experiment;

import java.text.MessageFormat;

import org.slf4j.Logger;

/**
 *	A generic Prometheux Exception.
 *
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class PrometheuxRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -8113891891474685357L;
	
	public PrometheuxRuntimeException(final String message) {
		super(message);
	  }
	
	public PrometheuxRuntimeException(final String message, final Logger logger) {
	    super(message);
	    if (logger != null) {
	      logger.error(message);
	    }
	  }
	
	public PrometheuxRuntimeException(final String message, final Logger logger, final Object... arguments) {
	    super(MessageFormat.format(message, arguments));
	    if (logger != null) {
	      logger.error(message, arguments);
	    }
	  }
	
	  public PrometheuxRuntimeException(final String message, final Throwable cause, final Logger logger) {
	    super(message, cause);
	    if (logger != null) {
	      logger.error(message);
	    }
	  }
	  
}
