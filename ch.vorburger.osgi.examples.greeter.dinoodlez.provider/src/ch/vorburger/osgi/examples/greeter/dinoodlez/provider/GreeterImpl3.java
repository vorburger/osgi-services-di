package ch.vorburger.osgi.examples.greeter.dinoodlez.provider;

import ch.vorburger.osgi.examples.greeter.api.Greeter;

public class GreeterImpl3 implements Greeter {

	private final GreetHelper greetHelper;
	
	public GreeterImpl3(GreetHelper greetHelper) {
		this.greetHelper = greetHelper;
	}
	
	@Override
	public String greet(String who) {
		String greeting = "hello, " + who;
		return greetHelper.complete(greeting);
	}

}
