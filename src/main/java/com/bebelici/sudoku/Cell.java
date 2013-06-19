package com.bebelici.sudoku;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class Cell {
	
	private static final Logger LOG = Logger.getLogger(Cell.class);
	
	private Set<Integer> possibleValues;
	private Integer value;
	private Set<Neighbourhood> neighbourhoods;
	private final int line;
	private final int column;
	
	public Cell(int line, int column) {
		this.value = null;
		this.line = line;
		this.column = column;
		possibleValues = new HashSet<Integer>();
		for (int i = 1; i < 10; ++i) {
			possibleValues.add(i);
		}
		neighbourhoods = new HashSet<Neighbourhood>();
	}
	
	public void addToNeighbourhood(Neighbourhood n) {
		neighbourhoods.add(n);
	}

	public String print() {
//		return (value==null ? " ." : " " + value);
		return (value==null ? printPossibleValues() : " " + value);
	}
	
	public String printPossibleValues() {
		StringBuffer out = new StringBuffer();
		out.append(" ");
		for (Integer p : possibleValues) {
			out.append("" + p);
		}
		return out.toString();
	}
	
	public String oldPrintPossibleValues() {
		StringBuffer out = new StringBuffer();
		out.append("Values possible for (" + line + ", " + column + "): ");
		String sep = "";
		for (Integer p : possibleValues) {
			out.append(sep + p);
			sep = ", ";
		}
		return out.toString();
	}

	public Set<Move> setValue(int value) throws InvalidValueException {
		Set<Move> result = new HashSet<Move>();
		
		if (this.value != null) {
			if (this.value != value) {
				throw new InvalidValueException("Cannot set cell value to " + value + ", cell already has value " + this.value);
			}
			
			// value already set
			return result;
		}
		LOG.debug("Setting value at (" + line + ", " + column + ") = " + value);
		
		this.value = new Integer(value);
		this.possibleValues.clear();
		for (Neighbourhood n : neighbourhoods) {
			result.addAll(n.removeValue(value));
		}
		return result;
	}

	public Set<Move> removeValue(int value) {
		Set<Move> result = new HashSet<Move>();
		if (this.value != null) {
			return result;
		}
	
		possibleValues.remove(value);
		if (possibleValues.size() == 1) {
			int last = possibleValues.iterator().next();
			result.add(new Move(this.line, this.column, last));
		}
		return result;
	}

	public boolean isPossible(int i) {
		return possibleValues.contains(i);
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	public Integer getValue() {
		return value;
	}
	
	// for testing only
	protected void setValueInternally(int value) {
		this.value = value;
	}
}
