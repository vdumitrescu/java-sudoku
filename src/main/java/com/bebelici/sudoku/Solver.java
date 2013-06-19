package com.bebelici.sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Solver {

	private static final Logger LOG = Logger.getLogger(Solver.class);
	private static final String EXTRA_HARD_HINTED = "src/main/resources/puzzles/extra_hard_hinted";
	private static final String EXTRA_HARD        = "src/main/resources/puzzles/extra_hard";
	private static final String HARD              = "src/main/resources/puzzles/hard";
	private static final String EASY              = "src/main/resources/puzzles/easy";
	private static final String MEGA              = "src/main/resources/puzzles/mega";
	private static final String NEW               = "src/main/resources/puzzles/new";
	private static final String EVIL              = "src/main/resources/puzzles/evil";

	protected static final String[] PUZZLES = {
		EVIL, EXTRA_HARD_HINTED, EXTRA_HARD, HARD, EASY, MEGA, NEW
	};
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
		try {
			Board b = makeBoard(HARD);
			boolean solved = b.solve();
			if (!solved) {
				LOG.info("This is the best I could do:");
			} else {
				LOG.info("I found the solution:");
			}
			b.print(true);
		} catch (InvalidValueException e) {
			LOG.error("ERROR: " + e.getMessage());
		} catch (DuplicateValueException e) {
			LOG.error("ERROR: " + e.getMessage());
		} catch (IOException e) {
			LOG.error(e);
		}
	}

	private static final List<String> readFile(String filename) throws IOException {
		List<String> result = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line;
		while ((line = reader.readLine()) != null) {
			result.add(line);
		}
		return result;
	}
	
	private static Board makeBoard(String filename) throws IOException {
		List<String> lines = readFile(filename);
		Board b = new Board();
		for (int i=0; i<9; i++) {
			String line = lines.get(i);
			for (int j=0; j<9; j++) {
				char c = line.charAt(j);
				if (c != '.') {
					int x = (c - '0');
					b.addMove(new Move(i, j, x));
				}
			}
		}
		return b;
	}	
}
