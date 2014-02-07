package fr.epsi.i4.bookmark;

public class InvalidBookmarkException extends Exception {

	private static final long serialVersionUID = -9158442902635974449L;

	public InvalidBookmarkException() {
		super();
	}

	public InvalidBookmarkException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidBookmarkException(String message) {
		super(message);
	}

	public InvalidBookmarkException(Throwable cause) {
		super(cause);
	}

}
