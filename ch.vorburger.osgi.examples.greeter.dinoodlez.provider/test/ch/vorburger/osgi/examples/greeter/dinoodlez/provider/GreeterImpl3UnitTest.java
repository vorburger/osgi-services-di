package ch.vorburger.osgi.examples.greeter.dinoodlez.provider;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.vorburger.osgi.examples.greeter.api.Greeter;

public class GreeterImpl3UnitTest {

	@Test
	public void testWithGreetHelperImpl() {
		Greeter service = new WiringModule().greeter();
		assertEquals("hello, world...", service.greet("world"));
	}

	@Test
	public void testWithAlternativeSimplerGreetHelper() {
		Greeter service = new TestWiringModule().greeter();
		assertEquals("hello, world!!!", service.greet("world"));
	}
	
	static class TestWiringModule extends WiringModule {
		@Override
		GreetHelper greetHelper() {
			return greeting -> greeting + "!!!";
		}
	}

}
