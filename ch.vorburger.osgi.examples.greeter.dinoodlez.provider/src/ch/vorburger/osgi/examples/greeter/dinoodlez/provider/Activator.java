package ch.vorburger.osgi.examples.greeter.dinoodlez.provider;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import ch.vorburger.osgi.examples.greeter.api.Greeter;

public class Activator implements BundleActivator {

	// TODO This could later alternatively be replaced with a
	// BundleTracker-based mechanism looking for *Module wiring kind of class in a MANIFEST header ("OSGi Extender"?) 

	private ServiceRegistration<Greeter> greeterServiceRegistration;

	@Override
	public void start(BundleContext context) throws Exception {
		Greeter greeter = new WiringModule().greeter();
		greeterServiceRegistration = context.registerService(Greeter.class, greeter, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		greeterServiceRegistration.unregister();
	}

}
