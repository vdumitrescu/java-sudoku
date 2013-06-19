package com.bebelici.sudoku;

public class InvalidValueException extends Exception {

	private static final long serialVersionUID = 8954759113725562418L;

	public InvalidValueException() {
		super();
	}

	public InvalidValueException(String arg0) {
		super(arg0);
	}

	public InvalidValueException(Throwable arg0) {
		super(arg0);
	}

	public InvalidValueException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
