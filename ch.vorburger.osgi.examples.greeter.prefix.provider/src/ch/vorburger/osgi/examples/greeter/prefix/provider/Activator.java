package ch.vorburger.osgi.examples.greeter.prefix.provider;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ch.vorburger.osgi.examples.greeter.api.GreetPrefixer;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		context.registerService(GreetPrefixer.class, new GreetPrefixerImpl(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// NOTE: The service is automatically unregistered
	}

}
