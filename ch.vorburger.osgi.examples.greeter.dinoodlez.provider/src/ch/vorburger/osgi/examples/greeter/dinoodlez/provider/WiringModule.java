package ch.vorburger.osgi.examples.greeter.dinoodlez.provider;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import ch.vorburger.osgi.examples.greeter.api.GreetPrefixer;
import ch.vorburger.osgi.examples.greeter.api.Greeter;
import ch.vorburger.osgi.util.OsgiUtil;
import ch.vorburger.osgi.util.OsgiUtil.CloseableService;

/**
 * Object wiring internal to this bundle.
 *
 * Currently manual here, but if bigger, this could also be implemented with any
 * Dependency Injection (DI) framework.
 */
public class WiringModule implements AutoCloseable {

	private CloseableService<GreetPrefixer> closeableService;

	Greeter greeter() {
		return new GreeterImpl3(greetPrefixer(), greetHelper());
	}

	GreetHelper greetHelper() {
		return new GreetHelperImpl();
	}

	GreetPrefixer greetPrefixer() {
		try {
			closeableService = OsgiUtil.getService(bundleContext(), GreetPrefixer.class);
			return closeableService.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void close() throws Exception {
		if (closeableService != null)
			closeableService.close();
	}

	BundleContext bundleContext() {
		return FrameworkUtil.getBundle(WiringModule.class).getBundleContext();
	}

}
