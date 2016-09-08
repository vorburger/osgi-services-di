package ch.vorburger.osgi.examples.greeter.prefix.provider;

import ch.vorburger.osgi.examples.greeter.api.GreetPrefixer;

public class GreetPrefixerImpl implements GreetPrefixer {

	@Override
	public String prefix(String greeting) {
		return "[ANNOUNCEMENT] " + greeting;
	}

}
