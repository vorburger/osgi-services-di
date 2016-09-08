package ch.vorburger.osgi.examples.greeter.dinoodlez.provider;

import ch.vorburger.osgi.examples.greeter.api.GreetPrefixer;
import ch.vorburger.osgi.examples.greeter.api.Greeter;

public class GreeterImpl3 implements Greeter {

	private final GreetPrefixer greetPrefixer; 
	private final GreetHelper greetHelper;
	
	public GreeterImpl3(GreetPrefixer greetPrefixer, GreetHelper greetHelper) {
		this.greetPrefixer = greetPrefixer;
		this.greetHelper = greetHelper;
	}
	
	@Override
	public String greet(String who) {
		String greeting = greetPrefixer.prefix("hello, " + who);
		return greetHelper.complete(greeting);
	}

}
