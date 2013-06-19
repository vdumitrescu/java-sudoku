package com.bebelici.sudoku;

public class DuplicateValueException extends Exception {

	private static final long serialVersionUID = -1905905147740813329L;

	public DuplicateValueException() {
		super();
	}

	public DuplicateValueException(String arg0) {
		super(arg0);
	}

	public DuplicateValueException(Throwable arg0) {
		super(arg0);
	}

	public DuplicateValueException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
