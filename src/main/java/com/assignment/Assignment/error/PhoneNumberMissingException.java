package com.assignment.Assignment.error;

public class PhoneNumberMissingException extends Exception {
	public PhoneNumberMissingException () {
		super();
	}

	public PhoneNumberMissingException (String message) {
		super(message);
	}

	public PhoneNumberMissingException (String message, Throwable cause) {
		super(message, cause);
	}

	public PhoneNumberMissingException (Throwable cause) {
		super(cause);
	}

	protected PhoneNumberMissingException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

