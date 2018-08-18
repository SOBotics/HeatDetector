package org.sobotics.heatdetector.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.sobotics.heatdetector.domain.file.Line;
import org.sobotics.heatdetector.domain.file.TextFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Regexen {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Regexen.class);

	public static final int TYPE_LOW = 1;
	public static final int TYPE_MEDIUM = 2;
	public static final int TYPE_HIGH = 3;
	public static final int TYPE_TRACK = 4;
	public static final int TYPE_WHITELIST = 5;

	private TextFile file; // The file
	private int type; // The type of regex (final)

	@JsonIgnore
	private List<Pattern> patterns; // The compiled patters

	public Regexen(File folder, int type) {
		this.type = type;
		try {
			this.file = new TextFile(new File(folder.getAbsolutePath(), getRegexFileName(type)));
		} catch (IOException e) {
			logger.error("Regexen(File, int) {}", e.getMessage());
		}
		compilePatterns();
	}
	
	public String getRegexHit(String classifyText) {
		for (Pattern pattern : patterns) {
			if (pattern.matcher(classifyText).find()) {
				return pattern.toString();
			}
		}
		return null;
	}

	public static String getRegexFileName(int type) {
		switch (type) {
		case TYPE_LOW:
			return "regex_low.txt";
		case TYPE_MEDIUM:
			return "regex_medium.txt";
		case TYPE_HIGH:
			return "regex_high.txt";
		case TYPE_TRACK:
			return "regex_track.txt";
		case TYPE_WHITELIST:
			return "regex_whitelist.txt";
		default:
			throw new IllegalArgumentException("Type " + type + " is unkown");
		}
	}

	public void compilePatterns() {
		this.patterns = new ArrayList<>();
		if (file != null) {
			for (Line line : file.getLines()) {
				Pattern p = Pattern.compile(line.getText());
				patterns.add(p);
			}
		}
	}

	public List<Pattern> getPatterns() {
		if (this.patterns == null) {
			return Collections.emptyList();
		}
		return patterns;
	}

	public int getType() {
		return type;
	}

	public TextFile getFile() {
		return file;
	}

}
