package ch.vorburger.osgi.examples.greeter.ds2.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;

import ch.vorburger.osgi.examples.greeter.api.Greeter;
import ch.vorburger.osgi.util.OsgiUtil;
import ch.vorburger.osgi.util.OsgiUtil.CloseableService;

@RunWith(MockitoJUnitRunner.class) // Do NOT remove this (even if not using Mockito); see https://github.com/bndtools/bndtools/issues/1527
public class GreeterDsImpl2IntegrationTest {

	private final BundleContext bundleContext = OsgiUtil.getBundleContext(GreeterDsImpl2IntegrationTest.class);

	@Test
	public void greeterViaDeclarativeService() throws Exception {
		try (CloseableService<Greeter> greeter = OsgiUtil.getService(bundleContext, Greeter.class)) {
			assertEquals("hello, world.", greeter.get().greet("world"));
		}
	}

}