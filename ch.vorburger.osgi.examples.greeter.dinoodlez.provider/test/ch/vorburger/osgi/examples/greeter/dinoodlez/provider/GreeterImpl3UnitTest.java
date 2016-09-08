package ch.vorburger.osgi.examples.greeter.dinoodlez.provider;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.vorburger.osgi.examples.greeter.api.Greeter;

public class GreeterImpl3UnitTest {

	@Test
	public void testWithGreetHelperImpl() {
		Greeter greeter = new WiringModule().greeter();
		assertEquals("hello, world...", greeter.greet("world"));
	}

	@Test
	public void testWithAlternativeSimplerGreetHelper() {
		Greeter greeter = new TestWiringModule().greeter();
		assertEquals("hello, world!!!", greeter.greet("world"));
	}
	
	static class TestWiringModule extends WiringModule {
		@Override
		GreetHelper greetHelper() {
			return greeting -> greeting + "!!!";
		}
	}

}
