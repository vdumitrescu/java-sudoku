package com.bebelici.sudoku;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;

public class Board {
	
	private static final Logger LOG = Logger.getLogger(Board.class);
	private Cell cells[][];
	private Neighbourhood lines[], cols[], groups[];
	private Stack<Move> moves = new Stack<Move>();
	
	public Board() {
		cells = new Cell[9][9];
		lines = new Neighbourhood[9];
		cols = new Neighbourhood[9];
		groups = new Neighbourhood[9];
		
		// create the 81 cells
		for (int i = 0; i < 9; i++) {
			lines[i] = new Neighbourhood(NeighbourhoodType.LINE, i);
			cols[i] = new Neighbourhood(NeighbourhoodType.COLUMN, i);
			groups[i] = new Neighbourhood(NeighbourhoodType.GROUP, i);

			for (int j = 0; j < 9; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}

		// add the cells to neighbourhoods
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				lines[i].addCell(cells[i][j]);
				cols[j].addCell(cells[i][j]);
				int gx = j/3;
				int gy = i/3;
				groups[gx+3*gy].addCell(cells[i][j]);
			}
		}
	}
	
	public void print(boolean printHints) {
		Set<Move> unsolved = new HashSet<Move>();
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < 9; i++) {
			out.append("[");
			for (int j = 0; j < 9; j++) {
				out.append(cells[i][j].print());
				if (cells[i][j].getValue() == null) {
					unsolved.add(new Move(i, j, 0));
				}
			}
			out.append(" ]\n");
		}
		if (printHints) {
			for (Move m : unsolved) {
				out.append(cells[m.getLine()][m.getColumn()].printPossibleValues() + "\n");
			}
		}
		LOG.info("\n" + out.toString());
	}

	public void addMove(Move move) {
		moves.push(move);
	}

	public boolean solve() throws InvalidValueException, DuplicateValueException {
		
		do {
			while (moves.size() > 0) {
				Move m = moves.pop();
				moves.addAll(cells[m.getLine()][m.getColumn()].setValue(m.getValue()));
			}
		} while (findMoreMoves());
		
		return checkSolution();
	}

	private boolean checkSolution() throws DuplicateValueException {
		for (int i = 0; i < 9; i++) {
			if (!lines[i].checkSolved() ||
				!cols[i].checkSolved() ||
				!groups[i].checkSolved()) {
				LOG.debug("Board not solved.");
				return false;
			}
		}
		LOG.debug("Board solved.");
		return true;
	}

	private boolean findMoreMoves() {
		// TODO improve algorithm
		Set<Move> newMoves = new HashSet<Move>();

		newMoves.addAll(findSingles());
		
		if (newMoves.size() == 0) {
			return false;
		}
		moves.addAll(newMoves);
		return true;
	}
	
	private Set<Move> findSingles() {
		Set<Move> newMoves = new HashSet<Move>();
		for (int i=0; i<9; i++) {
			newMoves.addAll(lines[i].findSingles());
			newMoves.addAll(cols[i].findSingles());
			newMoves.addAll(groups[i].findSingles());
		}
		return newMoves;
	}

}
