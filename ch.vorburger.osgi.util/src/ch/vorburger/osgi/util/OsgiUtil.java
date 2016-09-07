package ch.vorburger.osgi.util;

import java.io.Closeable;
import java.io.IOException;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Utilities for OSGi.
 *  
 * @author Michael Vorburger
 */
public class OsgiUtil {

	public static BundleContext getBundleContext(Class<?> clazz) {
		Bundle bundle = FrameworkUtil.getBundle(clazz);
		if (bundle == null)
			throw new IllegalStateException("No Bundle for class (are you running in Java SE standalone instead of OSGi?): " + clazz);
		return bundle.getBundleContext();
	}

	public static <T> CloseableService<T> getService(BundleContext bundleContext, Class<T> clazz) throws InterruptedException {
		ServiceTracker<T,T> serviceTracker = new ServiceTracker<>(bundleContext, clazz, null);
		return new CloseableService<>(serviceTracker, clazz.toString());
	}

	public static class CloseableService<T> implements Closeable {

		private final ServiceTracker<T, T> serviceTracker;
		private final String description;

		public CloseableService(ServiceTracker<T, T> serviceTracker, String description) {
			super();
			this.serviceTracker = serviceTracker;
			this.description = description;
		}

		public T get() {
			serviceTracker.open();
			try {
				T service = serviceTracker.waitForService(1000);
				if (service == null)
					throw new IllegalStateException("No Service found (did you perhaps forget to add your API's and/or a DS provider impl, such as felix.scr, bundle(s) to Run?): " + description);
				return service;
			} catch (InterruptedException e) {
				throw new IllegalStateException("Interrupted during service look-up: " + description, e);
			}
		}
		
		@Override
		public void close() throws IOException {
			serviceTracker.close();
		}
		
	}
	
}
