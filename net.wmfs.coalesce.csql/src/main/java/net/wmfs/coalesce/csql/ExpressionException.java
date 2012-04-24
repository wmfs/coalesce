package net.wmfs.coalesce.csql;

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