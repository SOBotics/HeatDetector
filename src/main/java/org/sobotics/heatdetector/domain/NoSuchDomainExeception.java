package org.sobotics.heatdetector.domain;

public class NoSuchDomainExeception extends RuntimeException {

	private static final long serialVersionUID = -8053621610595566974L;
	
	public NoSuchDomainExeception(String domain){
		super(domain + " is not an existing domain");
	}

}
