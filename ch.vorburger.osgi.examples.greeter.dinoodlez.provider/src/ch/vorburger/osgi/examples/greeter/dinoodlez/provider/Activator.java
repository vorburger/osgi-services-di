package ch.vorburger.osgi.examples.greeter.dinoodlez.provider;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import ch.vorburger.dinoodlez.ServiceRegistry;
import ch.vorburger.osgi.examples.greeter.api.GreetPrefixer;
import ch.vorburger.osgi.examples.greeter.api.Greeter;

public class Activator implements BundleActivator {

	// TODO This could later alternatively be replaced with a
	// BundleTracker-based mechanism looking for *Module wiring kind of class in a MANIFEST header ("OSGi Extender"?) 

	private ServiceRegistration<Greeter> greeterServiceRegistration;
	private AutoCloseable requirement;

	@Override
	public void start(BundleContext context) throws Exception {
		requirement = ServiceRegistry.INSTANCE.require(Activator.class, null, serviceInstances -> {
			GreetPrefixer greetPrefixer = (GreetPrefixer) serviceInstances.get(0);
			WiringModule wiringModule = new WiringModule(greetPrefixer);
			Greeter greeter = wiringModule.greeter();
			greeterServiceRegistration = context.registerService(Greeter.class, greeter, null);
		}, () -> {
			greeterServiceRegistration.unregister();
		});
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		requirement.close();
	}

}
