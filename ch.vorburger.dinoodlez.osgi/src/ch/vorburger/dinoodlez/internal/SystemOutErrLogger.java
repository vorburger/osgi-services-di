package ch.vorburger.dinoodlez.internal;

public class SystemOutErrLogger implements Logger {

	@Override
	public void error(String message) {
		// fake Exception just to get stack trace
		error(message, new Exception(message));
	}

	@Override
	public void error(String message, Exception e) {
		System.err.println(message);
		e.printStackTrace(System.err);
	}

}
