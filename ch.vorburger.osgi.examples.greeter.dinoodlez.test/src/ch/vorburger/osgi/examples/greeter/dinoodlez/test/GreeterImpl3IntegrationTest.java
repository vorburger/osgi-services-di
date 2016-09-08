package ch.vorburger.osgi.examples.greeter.dinoodlez.test;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import ch.vorburger.osgi.examples.greeter.api.Greeter;
import ch.vorburger.osgi.util.OsgiUtil;
import ch.vorburger.osgi.util.OsgiUtil.CloseableService;

@RunWith(MockitoJUnitRunner.class)
public class GreeterImpl3IntegrationTest {

	private final BundleContext bundleContext = FrameworkUtil.getBundle(GreeterImpl3IntegrationTest.class).getBundleContext();

	@Test
	@Ignore
	public void greeter() throws Exception {
		try (CloseableService<Greeter> greeter = OsgiUtil.getService(bundleContext, Greeter.class)) {
			assertEquals("hello, world...", greeter.get().greet("world"));
		}
	}

}