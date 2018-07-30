package org.sobotics.heatdetector.classify.model;

import java.util.List;

public class ClassifyResponse {
	
	private String domain;
	private List<Result> result;
	private int backOff;
	
	public ClassifyResponse(){
		super();
	}
	
	public ClassifyResponse(String domain){
		super();
		this.domain = domain;
	}
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public List<Result> getResult() {
		return result;
	}
	public void setResult(List<Result> result) {
		this.result = result;
	}

	public int getBackOff() {
		return backOff;
	}

	public void setBackOff(int backOff) {
		this.backOff = backOff;
	}

}
