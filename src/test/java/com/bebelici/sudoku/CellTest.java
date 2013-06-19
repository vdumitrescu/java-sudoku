package com.bebelici.sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

public class CellTest {
	
	private static Integer ONE = 1;
	private static Integer TWO = 2;

	private static Integer LINE = 4;
	private static Integer COLUMN = 5;

	@Test
	public void testCell() {
		Cell cell = new Cell(LINE, COLUMN);
		for (int i = 1; i < 10; i++) {
			assertTrue(cell.isPossible(i));
		}
		assertFalse(cell.isPossible(0));
		assertFalse(cell.isPossible(10));
	}

	@Test
	public void testSetValueAlreadySet() {
		Cell cell = new Cell(LINE, COLUMN);
		cell.setValueInternally(ONE);
		try {
			cell.setValue(TWO);
			fail("Should have thrown exception, value already set.");
		} catch (InvalidValueException e) {
			assertEquals("Cannot set cell value to " + TWO + ", cell already has value " + ONE, e.getMessage());
		}
	}


	@Test
	public void testSetValue() throws InvalidValueException {
		Neighbourhood mockLine = EasyMock.createMock(Neighbourhood.class);
		Neighbourhood mockColumn = EasyMock.createMock(Neighbourhood.class);
		Neighbourhood mockGroup = EasyMock.createMock(Neighbourhood.class);
		Cell cell = new Cell(LINE, COLUMN);
		cell.addToNeighbourhood(mockLine);
		cell.addToNeighbourhood(mockColumn);
		cell.addToNeighbourhood(mockGroup);
		Set<Move> emptySet = new HashSet<Move>();
		EasyMock.expect(mockLine.removeValue(1)).andReturn(emptySet);
		EasyMock.expect(mockColumn.removeValue(1)).andReturn(emptySet);
		EasyMock.expect(mockGroup.removeValue(1)).andReturn(emptySet);
		
		EasyMock.replay(mockLine, mockColumn, mockGroup);
		cell.setValue(ONE);
		EasyMock.verify(mockLine, mockColumn, mockGroup);
		
		for (int i = 1; i < 10; i++) {
			assertFalse(cell.isPossible(i));
		}
		
		assertEquals(ONE, cell.getValue());
	}

	@Test
	public void testRemoveValueAlreadyKnown() {
		Cell cell = new Cell(LINE, COLUMN);
		cell.setValueInternally(ONE);
		
		Set<Move> result = cell.removeValue(TWO);
		assertEquals(0, result.size());
	}

	@Test
	public void testRemoveValueMultipleRemaining() {
		Cell cell = new Cell(LINE, COLUMN);
		assertEquals(" 123456789", cell.printPossibleValues());
		
		assertEquals(0, cell.removeValue(ONE).size());
		assertEquals(" 23456789", cell.printPossibleValues());

		assertEquals(0, cell.removeValue(TWO).size());
		assertEquals(" 3456789", cell.printPossibleValues());
		
		assertEquals(0, cell.removeValue(5).size());
		assertEquals(" 346789", cell.printPossibleValues());

		assertEquals(0, cell.removeValue(8).size());
		assertEquals(" 34679", cell.printPossibleValues());

		assertEquals(0, cell.removeValue(4).size());
		assertEquals(" 3679", cell.printPossibleValues());

		assertEquals(0, cell.removeValue(9).size());
		assertEquals(" 367", cell.printPossibleValues());
				
		assertEquals(0, cell.removeValue(3).size());
		assertEquals(" 67", cell.printPossibleValues());
				
		Set<Move> result = cell.removeValue(7); 
		assertEquals(1, result.size());
		Move move = result.iterator().next();
		assertEquals(6, move.getValue());
		assertEquals(LINE.intValue(), move.getLine());
		assertEquals(COLUMN.intValue(), move.getColumn());
	}

}
