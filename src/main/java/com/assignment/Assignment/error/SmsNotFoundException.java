package com.assignment.Assignment.error;

public class SmsNotFoundException extends Exception {
	public SmsNotFoundException () {
		super();
	}

	public SmsNotFoundException (String message) {
		super(message);
	}

	public SmsNotFoundException (String message, Throwable cause) {
		super(message, cause);
	}

	public SmsNotFoundException (Throwable cause) {
		super(cause);
	}

	protected SmsNotFoundException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
