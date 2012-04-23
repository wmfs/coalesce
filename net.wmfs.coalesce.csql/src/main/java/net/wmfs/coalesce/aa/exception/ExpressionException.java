package net.wmfs.coalesce.aa.exception;

public class ExpressionException extends Exception {

	public ExpressionException() {
	}

	public ExpressionException(String message) {
		super(message);
	}

	public ExpressionException(Throwable cause) {
		super(cause);
	}

	public ExpressionException(String message, Throwable cause) {
		super(message, cause);
	}
}