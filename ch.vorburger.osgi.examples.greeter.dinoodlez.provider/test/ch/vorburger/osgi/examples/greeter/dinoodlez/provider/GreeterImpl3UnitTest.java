package ch.vorburger.osgi.examples.greeter.dinoodlez.provider;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.vorburger.osgi.examples.greeter.api.GreetPrefixer;
import ch.vorburger.osgi.examples.greeter.api.Greeter;

public class GreeterImpl3UnitTest {

	GreetPrefixer testGreetPrefixer = greeting -> "3. " + greeting;

	@Test
	public void testWithGreetHelperImpl() {
		Greeter greeter = new WiringModule(testGreetPrefixer).greeter();
		assertEquals("3. hello, world...", greeter.greet("world"));
	}

	@Test
	public void testWithAlternativeSimplerGreetHelper() {
		Greeter greeter = new TestAlternativeWiringModule(testGreetPrefixer).greeter();
		assertEquals("3. hello, world!!!", greeter.greet("world"));
	}
	
	static class TestAlternativeWiringModule extends WiringModule {
		
		TestAlternativeWiringModule(GreetPrefixer greetPrefixer) {
			super(greetPrefixer);
		}

		@Override
		GreetHelper greetHelper() {
			return greeting -> greeting + "!!!";
		}
	}

}
