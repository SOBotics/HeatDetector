package org.sobotics.heatdetector.domain;

public class RegexHit {
	
	private String regex;
	private int type;
	
	public RegexHit(){
		super();
	}
	
	public RegexHit(String regex, int type) {
		super();
		this.regex = regex;
		this.type = type;
		
	}
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

}
