package org.sobotics.heatdetector.rest.security;

public class RateLimitException extends Exception {

	private static final long serialVersionUID = -7373956915574152653L;

	public RateLimitException(String message){
		super(message);
	}
	
}
