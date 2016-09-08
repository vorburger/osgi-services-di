package ch.vorburger.osgi.examples.greeter.dinoodlez.provider;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.vorburger.osgi.examples.greeter.api.GreetPrefixer;
import ch.vorburger.osgi.examples.greeter.api.Greeter;

public class GreeterImpl3UnitTest {

	@Test
	public void testWithGreetHelperImpl() {
		Greeter greeter = new TestWiringModule().greeter();
		assertEquals("3. hello, world...", greeter.greet("world"));
	}

	static class TestWiringModule extends WiringModule {
		
		@Override
		GreetPrefixer greetPrefixer() {
			return greeting -> "3. " + greeting;
		}
	}

	@Test
	public void testWithAlternativeSimplerGreetHelper() {
		Greeter greeter = new TestAlternativeWiringModule().greeter();
		assertEquals("3. hello, world!!!", greeter.greet("world"));
	}
	
	static class TestAlternativeWiringModule extends TestWiringModule {
		
		@Override
		GreetHelper greetHelper() {
			return greeting -> greeting + "!!!";
		}
	}

}
