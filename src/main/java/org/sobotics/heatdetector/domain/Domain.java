package org.sobotics.heatdetector.domain;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.sobotics.heatdetector.feed.FeedItem;

/**
 * A <code>Domain</code> is keeping the regex files, hence different Domains can
 * have different regex
 * 
 * @author Petter Friberg
 *
 */
public class Domain implements Comparable<Domain> {

	@JsonIgnore
	private File folder;

	@JsonIgnore
	private Queue<FeedItem> allFeed = new LinkedList<>();

	@JsonIgnore
	private Queue<FeedItem> badFeed = new LinkedList<>();

	private Regexen low;
	private Regexen medium;
	private Regexen high;
	private Regexen track;
	private Regexen whitelist;

	public Domain(File folder) {
		if (folder == null) {
			throw new NullPointerException("Domain folder can not be null");
		}
		this.folder = folder;
		low = new Regexen(folder, Regexen.TYPE_LOW);
		medium = new Regexen(folder, Regexen.TYPE_MEDIUM);
		high = new Regexen(folder, Regexen.TYPE_HIGH);
		track = new Regexen(folder, Regexen.TYPE_TRACK);
		whitelist = new Regexen(folder, Regexen.TYPE_WHITELIST);
	}

	public String getName() {
		return folder.getName();
	}

	Regexen getLow() {
		return low;
	}

	Regexen getMedium() {
		return medium;
	}

	Regexen getHigh() {
		return high;
	}

	Regexen getTrack() {
		return track;
	}

	Regexen getWhitelist() {
		return whitelist;
	}

	@Override
	public int compareTo(Domain o) {
		return getName().compareTo(o.getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Domain)) {
			return false;
		}
		return getName().equals(((Domain) o).getName());
	}

	public Regexen getRegexen(int type) {
		switch (type) {
		case Regexen.TYPE_HIGH:
			return getHigh();
		case Regexen.TYPE_MEDIUM:
			return getMedium();
		case Regexen.TYPE_LOW:
			return getLow();
		case Regexen.TYPE_TRACK:
			return getTrack();
		case Regexen.TYPE_WHITELIST:
			return getWhitelist();
		default:
			throw new IllegalArgumentException("The type:" + type + " is not correct");
		}
	}

	public Queue<FeedItem> getAllFeed() {
		return allFeed;
	}

	public Queue<FeedItem> getBadFeed() {
		return badFeed;
	}
}
