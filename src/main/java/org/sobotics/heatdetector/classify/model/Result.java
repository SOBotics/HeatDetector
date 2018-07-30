package org.sobotics.heatdetector.classify.model;

import org.sobotics.heatdetector.domain.RegexHit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Result {
	private long id;

	private double nb;
	private double op;
	
	private RegexHit bad;
	private RegexHit good;
	private RegexHit track;
	
	private int score;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	
	public double getNb() {
		return nb;
	}
	public void setNb(double nb) {
		this.nb = nb;
	}
	public double getOp() {
		return op;
	}
	public void setOp(double op) {
		this.op = op;
	}
	
	public RegexHit getBad() {
		return bad;
	}
	public void setBad(RegexHit bad) {
		this.bad = bad;
	}
	public RegexHit getGood() {
		return good;
	}
	public void setGood(RegexHit good) {
		this.good = good;
	}
	public RegexHit getTrack() {
		return track;
	}
	public void setTrack(RegexHit track) {
		this.track = track;
	}
}
