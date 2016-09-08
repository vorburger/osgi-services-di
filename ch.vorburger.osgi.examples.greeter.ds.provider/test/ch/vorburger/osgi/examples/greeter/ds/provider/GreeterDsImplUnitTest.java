package ch.vorburger.osgi.examples.greeter.ds.provider;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GreeterDsImplUnitTest {

	@Test
	public void greeter() {
		GreeterDsImpl greeter = new GreeterDsImpl();
		greeter.setGreetPrefixer(greeting -> "1. " + greeting);
		assertEquals("1. hello, world", greeter.greet("world"));
	}

}
