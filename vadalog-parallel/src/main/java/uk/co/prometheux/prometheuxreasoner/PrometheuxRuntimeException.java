package uk.co.prometheux.prometheuxreasoner;

import java.text.MessageFormat;

import org.slf4j.Logger;

import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;
import uk.co.prometheux.prometheuxreasoner.errorMessage.runtime.EvaluationRuntimeErrorMessage;

/**
 *	A generic Prometheux Exception.
 *
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class PrometheuxRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -8113891891474685357L;
	ErrorMessage errorMessage;

	
	public PrometheuxRuntimeException(final String message) {
		super(message);
		this.errorMessage = new EvaluationRuntimeErrorMessage(message);
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
	  
	  public ErrorMessage getErrorMessage() {
		  return this.errorMessage;
	  }
	  
}
