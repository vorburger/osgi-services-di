package ch.vorburger.osgi.examples.greeter.ds.provider;

import static org.junit.Assert.*;

import org.junit.Test;

public class GreeterDsImplUnitTest {

	@Test
	public void test() {
		assertEquals("hello, world", new GreeterDsImpl().greet("world"));
	}

}
