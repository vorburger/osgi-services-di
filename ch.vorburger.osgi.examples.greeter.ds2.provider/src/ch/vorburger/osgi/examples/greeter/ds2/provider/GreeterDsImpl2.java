package ch.vorburger.osgi.examples.greeter.ds2.provider;

import org.osgi.service.component.annotations.*;

import ch.vorburger.osgi.examples.greeter.api.Greeter;

@Component
public class GreeterDsImpl2 implements Greeter {

	private final GreetHelper greetHelper;
	
	public GreeterDsImpl2() {
		// NB: Having to do this here isn't great...
		this(new GreetHelperImpl());
	}
	
	public GreeterDsImpl2(GreetHelper greetHelper) {
		super();
		this.greetHelper = greetHelper;
	}

	@Override
	public String greet(String who) {
		String greeting = "hello, " + who;
		return greetHelper.complete(greeting);
	}

}
