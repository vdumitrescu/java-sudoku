package com.bebelici.sudoku;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;


public class NeighbourhoodTest {

	private static final NeighbourhoodType SOME_TYPE = NeighbourhoodType.LINE;
	private static final int SOME_ORDER = 4;
	private static final int ONE = 1;
	private static final int SEVEN = 7;

	private static Integer LINE = 4;
	private static Integer COLUMN = 5;
	private static Integer LINE2 = 6;
	private static Integer COLUMN2 = 7;

	@Test
	public void testRemoveValue() {
		Neighbourhood n = new Neighbourhood(SOME_TYPE, SOME_ORDER);
		
		Move m111 = new Move(1, 1, 1);
		Move m122 = new Move(1, 2, 2);
		Move m133 = new Move(1, 3, 3);
		Move m214 = new Move(2, 1, 4);
		Move m225 = new Move(2, 2, 5);
		Move m236 = new Move(2, 3, 6);
		
		Set<Move> someSet = new HashSet<Move>();
		someSet.add(m111);
		someSet.add(m122);
		someSet.add(m133);
		someSet.add(m214);

		Set<Move> anotherSet = new HashSet<Move>();
		anotherSet.add(m225);
		anotherSet.add(m236);

		Cell mockCell1 = EasyMock.createMock(Cell.class);
		Cell mockCell2 = EasyMock.createMock(Cell.class);

		mockCell1.addToNeighbourhood(n);
		mockCell2.addToNeighbourhood(n);
		EasyMock.expect(mockCell1.removeValue(SEVEN)).andReturn(someSet);
		EasyMock.expect(mockCell2.removeValue(SEVEN)).andReturn(anotherSet);

		EasyMock.replay(mockCell1, mockCell2);
		n.addCell(mockCell1);
		n.addCell(mockCell2);
		Set<Move> result = n.removeValue(SEVEN);
		assertEquals(6, result.size());
		EasyMock.verify(mockCell1, mockCell2);
	}
	
	@Test
	public void testFindSinglesAndNoneFound() {
		Neighbourhood n = new Neighbourhood(SOME_TYPE, SOME_ORDER);
		
		Cell mockCell1 = EasyMock.createMock(Cell.class);
		Cell mockCell2 = EasyMock.createMock(Cell.class);

		mockCell1.addToNeighbourhood(n);
		mockCell2.addToNeighbourhood(n);
		EasyMock.expect(mockCell1.isPossible(ONE)).andReturn(true);
		EasyMock.expect(mockCell2.isPossible(ONE)).andReturn(true);
		EasyMock.expect(mockCell1.getLine()).andReturn(LINE).anyTimes();
		EasyMock.expect(mockCell1.getColumn()).andReturn(COLUMN).anyTimes();
		EasyMock.expect(mockCell2.getLine()).andReturn(LINE2).anyTimes();
		EasyMock.expect(mockCell2.getColumn()).andReturn(COLUMN2).anyTimes();

		EasyMock.replay(mockCell1, mockCell2);
		n.addCell(mockCell1);
		n.addCell(mockCell2);
		Set<Move> result = n.findSinglesFor(ONE);
		assertEquals(0, result.size());
		EasyMock.verify(mockCell1, mockCell2);
	}

	@Test
	public void testFindSinglesAndOneFound() {
		Neighbourhood n = new Neighbourhood(SOME_TYPE, SOME_ORDER);
		
		Cell mockCell1 = EasyMock.createMock(Cell.class);
		Cell mockCell2 = EasyMock.createMock(Cell.class);

		mockCell1.addToNeighbourhood(n);
		mockCell2.addToNeighbourhood(n);
		EasyMock.expect(mockCell1.isPossible(SEVEN)).andReturn(true);
		EasyMock.expect(mockCell2.isPossible(SEVEN)).andReturn(false);
		EasyMock.expect(mockCell1.getLine()).andReturn(LINE).anyTimes();
		EasyMock.expect(mockCell1.getColumn()).andReturn(COLUMN).anyTimes();

		EasyMock.replay(mockCell1, mockCell2);
		n.addCell(mockCell1);
		n.addCell(mockCell2);
		Set<Move> result = n.findSinglesFor(SEVEN);
		assertEquals(1, result.size());
		Move move = result.iterator().next();
		assertEquals(LINE.intValue(), move.getLine());
		assertEquals(COLUMN.intValue(), move.getColumn());
		assertEquals(SEVEN, move.getValue());
		EasyMock.verify(mockCell1, mockCell2);
	}
}
