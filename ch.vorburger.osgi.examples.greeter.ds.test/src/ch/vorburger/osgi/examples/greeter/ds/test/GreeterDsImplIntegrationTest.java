package ch.vorburger.osgi.examples.greeter.ds.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import ch.vorburger.osgi.examples.greeter.api.Greeter;

@RunWith(MockitoJUnitRunner.class) // Do NOT remove this (even if not using Mockito); see https://github.com/bndtools/bndtools/issues/1527
public class GreeterDsImplIntegrationTest {

	private final BundleContext bundleContext = getBundleContext(GreeterDsImplIntegrationTest.class);

	@Test
	public void greeterViaDeclarativeService() throws Exception {
		Greeter service = getService(Greeter.class);
		assertEquals("hello, world", service.greet("world"));
	}

	// http://enroute.osgi.org/tutorial_base/600-testing.html
	<T> T getService(Class<T> clazz) throws InterruptedException {
		ServiceTracker<T,T> st = new ServiceTracker<>(bundleContext, clazz, null);
		st.open();
		T service = st.waitForService(1000);
		if (service == null)
			throw new IllegalStateException("No Service for class (did you perhaps forget to add your API's and/or a DS provider impl, such as felix.scr, bundle(s) to Run?): " + clazz);
		return service;
	}
	
	BundleContext getBundleContext(Class<?> clazz) {
		Bundle bundle = FrameworkUtil.getBundle(GreeterDsImplIntegrationTest.class);
		if (bundle == null)
			throw new IllegalStateException("No Bundle for class (are you running in Java SE standalone instead of OSGi?): " + clazz);
		return bundle.getBundleContext();
	}
}