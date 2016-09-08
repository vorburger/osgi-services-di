package ch.vorburger.osgi.examples.greeter.ds2.provider;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.vorburger.osgi.examples.greeter.api.GreetPrefixer;
import ch.vorburger.osgi.examples.greeter.api.Greeter;

public class GreeterDsImpl2UnitTest {

	GreetPrefixer testGreetPrefixer = greeting -> "2. " + greeting;
	
	@Test
	public void testWithGreetHelperImpl() {
		// NB: Having to do this here isn't great...
		GreetHelper greetHelper = new GreetHelperImpl();
		Greeter greeter = new GreeterDsImpl2(greetHelper, testGreetPrefixer);
		
		assertEquals("2. hello, world.", greeter.greet("world"));
	}

	@Test
	public void testWithAlternativeSimplerGreetHelper() {
		GreetHelper greetHelper = greeting -> greeting + "!!!";
		// NB: Having to inject this here isn't great... what if other classes need the same - manually wire everywhere :(
		Greeter greeter = new GreeterDsImpl2(greetHelper, testGreetPrefixer);
		
		assertEquals("2. hello, world!!!", greeter.greet("world"));
	}

}
