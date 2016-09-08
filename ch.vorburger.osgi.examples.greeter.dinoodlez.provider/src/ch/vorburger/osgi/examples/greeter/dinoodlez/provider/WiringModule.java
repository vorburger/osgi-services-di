package ch.vorburger.osgi.examples.greeter.dinoodlez.provider;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import ch.vorburger.osgi.examples.greeter.api.GreetPrefixer;
import ch.vorburger.osgi.examples.greeter.api.Greeter;

/**
 * Object wiring internal to this bundle.
 *
 * Currently manual here, but if bigger, this could also be implemented with any
 * Dependency Injection (DI) framework.
 */
public class WiringModule {

	private GreetPrefixer greetPrefixer;

	WiringModule(GreetPrefixer greetPrefixer) {
		this.greetPrefixer = greetPrefixer;
	}

	Greeter greeter() {
		return new GreeterImpl3(greetPrefixer(), greetHelper());
	}

	GreetHelper greetHelper() {
		return new GreetHelperImpl();
	}

	GreetPrefixer greetPrefixer() {
		return greetPrefixer;
	}
	
	BundleContext bundleContext() {
		return FrameworkUtil.getBundle(WiringModule.class).getBundleContext();
	}

}
