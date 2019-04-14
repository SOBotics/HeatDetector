package org.sobotics.heatdetector.domain.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is very basic file editor read file, edit/add/delete line.
 * Probably I should search for a lib (I don't like to rewrite file everytime
 *
 * @author Petter Friberg
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
		try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
			String line = br.readLine();
			int i = 1;
			while (line != null) {
				if (line.trim().length() > 0) {
					this.lines.add(new Line(i, line));
					i++;
				}
				line = br.readLine();
			}
		}
	}
	
	public List<Line> getLines() {
		return lines;
	}
	
	
	/**
	 * Adds a new line at the end of the textfile
	 * @param text Text of the line to add
	 */
	public synchronized void addLine(String text) {
		Line line = new Line(lines.size() + 1, text);
		lines.add(line);
	}
	
	/**
	 * Adds a new line at a given index (don't know if needed)
	 * @param index Index to insert at
	 * @param text Text of the line
	 */
	public synchronized void addLine(int index, String text) {
		Line line = new Line(index, text);
		lines.add(index - 1, line);
		updateIndices();
	}
	
	/**
	 * Edits a line at a given index with a given text
	 * @param index Index to edit
	 * @param text new Text
	 */
	public synchronized void editLine(int index, String text) {
		Line line = new Line(index, text);
		lines.set(index - 1, line);
	}
	
	/**
	 * Deletes a line at a given index
	 * @param index index to remove
	 */
	public synchronized void deleteLine(int index) {
		lines.remove(index - 1);
		updateIndices();
	}
	
	/**
	 * Updates the linenumbers of the lines in the lines list.
	 */
	private synchronized void updateIndices() {
		IntStream.range(0, lines.size())
				.mapToObj(index -> new Line(index + 1, lines.get(index).getText()))
				.forEach(line -> lines.set(line.getNumber() - 1, line));
	}
	
}
