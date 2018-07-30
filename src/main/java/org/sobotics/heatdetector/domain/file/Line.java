package org.sobotics.heatdetector.domain.file;

public class Line {
	
	private int number;
	private String text;
	
	public Line(int number, String text){
		this.number = number;
		this.text = text;
		
	}

	public int getNumber() {
		return number;
	}

	public String getText() {
		return text;
	}

}
