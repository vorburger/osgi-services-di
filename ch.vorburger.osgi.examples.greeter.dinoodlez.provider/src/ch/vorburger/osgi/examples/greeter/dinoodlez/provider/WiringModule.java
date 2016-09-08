package ch.vorburger.osgi.examples.greeter.dinoodlez.provider;

import ch.vorburger.osgi.examples.greeter.api.Greeter;

/**
 * Object wiring internal to this bundle.
 *
 * Currently manual here, but if bigger, this could also be implemented with any
 * Dependency Injection (DI) framework.
 */
public class WiringModule {

	Greeter greeter() {
		return new GreeterImpl3(greetHelper());
	}

	GreetHelper greetHelper() {
		return new GreetHelperImpl();
	}
	
}
