package org.sobotics.heatdetector.classify.model;

import java.util.Collections;
import java.util.List;

public class ClassifyRequest {
	
	private String domain;
	private int minScore=4;
	private List<Content> contents;
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getMinScore() {
		return minScore;
	}
	public void setMinScore(int minScore) {
		this.minScore = minScore;
	}
	public List<Content> getContents() {
		if (contents==null){
			return Collections.emptyList();
		}
		return contents;
	}
	public void setContents(List<Content> contents) {
		this.contents = contents;
	}

}
