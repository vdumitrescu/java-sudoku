package com.bebelici.sudoku;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class Neighbourhood {
	
	private static final Logger LOG = Logger.getLogger(Neighbourhood.class);
	private Set<Cell> cells;
	private final NeighbourhoodType type;
	private final int order;
	
	public Neighbourhood(NeighbourhoodType type, int order) {
		this.cells = new HashSet<Cell>();
		this.type = type;
		this.order = order;
	}
	
	public void addCell(Cell c) {
		cells.add(c);
		c.addToNeighbourhood(this);
	}

	public NeighbourhoodType getType() {
		return type;
	}

	public int getOrder() {
		return order;
	}

	public Set<Move> removeValue(int value) {
		Set<Move> newMoves = new HashSet<Move>();
		for (Cell c : cells) {
			newMoves.addAll(c.removeValue(value));
		}
		return newMoves;
	}

	public Set<Move> findSingles() {
		Set<Move> newMoves = new HashSet<Move>();
		
		// if a value is only possible in one cell, then the value must be in that cell
		for (int i = 1; i < 10; i++) {
			newMoves.addAll(findSinglesFor(i));
		}
		return newMoves;
	}

	protected Set<Move> findSinglesFor(int i) {
		Set<Move> newMoves = new HashSet<Move>();
		int cellsPossible = 0;
		Move move = null;
		for (Cell c : cells) {
			if (c.isPossible(i)) {
				cellsPossible++;
				move = new Move(c.getLine(), c.getColumn(), i);
			}
		}
		if (cellsPossible == 1) {
			newMoves.add(move);
		}
		return newMoves;
	}

	public boolean checkSolved() throws DuplicateValueException {
		boolean solved = true;
		Set<Integer> values = new HashSet<Integer>();
		for (Cell c : cells) {
			if (c.getValue() == null) {
				solved = false;
			} else {
				if (!values.add(c.getValue())) {
					throw new DuplicateValueException("Invalid input data! Puzzle is unsolvable.");
				}
			}
		}
		LOG.debug(type.toString() + " " + order + (solved?"":" not") + " solved.");
		return solved;
	}
}
