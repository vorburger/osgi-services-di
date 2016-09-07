package ch.vorburger.osgi.examples.greeter.ds.provider;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.vorburger.osgi.examples.greeter.api.Greeter;

public class GreeterDsImplUnitTest {

	@Test
	public void greeter() {
		Greeter service = new GreeterDsImpl();
		assertEquals("hello, world", service.greet("world"));
	}

}
