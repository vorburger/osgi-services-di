package ch.vorburger.osgi.examples.greeter.api;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface GreetPrefixer {

	String prefix(String greeting);

}
