package ch.vorburger.osgi.examples.greeter.ds2.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import ch.vorburger.osgi.examples.greeter.api.Greeter;

public class GreeterDsImpl2UnitTest {

	@Test
	public void testWithGreetHelperImpl() {
		GreetHelper greetHelper = new GreetHelperImpl();
		Greeter service = new GreeterDsImpl2(greetHelper);
		
		assertEquals("hello, world.", service.greet("world"));
	}

	@Test
	public void testWithAlternativeSimplerGreetHelper() {
		GreetHelper greetHelper = greeting -> greeting + "!!!";
		Greeter service = new GreeterDsImpl2(greetHelper);
		
		assertEquals("hello, world!!!", service.greet("world"));
	}

}
