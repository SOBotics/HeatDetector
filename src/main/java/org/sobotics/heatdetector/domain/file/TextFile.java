package org.sobotics.heatdetector.domain.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is very basic file editor read file, edit/add/delete line.
 * Probably I should search for a lib (I don't like to rewrite file everytime
 * @author Petter Friberg
 *
 */

public class TextFile {

	@JsonIgnore
	private File file;
	
	private List<Line> lines;

	public TextFile(File file) throws IOException {
		this.file = file;
		readFile();
	}

	private void readFile() throws IOException {
		this.lines = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(this.file));
		try {
			String line = br.readLine();
			int i = 1;
			while (line != null) {
				if (line.trim().length() > 0) {
					this.lines.add(new Line(i, line));
					i++;
				}
				line = br.readLine();
			}
		} finally {
			br.close();
		}
	}
	
	

	public List<Line> getLines() {
		return lines;
	}

}
