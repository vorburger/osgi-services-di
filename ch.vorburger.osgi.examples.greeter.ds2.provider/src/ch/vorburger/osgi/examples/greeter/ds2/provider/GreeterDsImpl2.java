package ch.vorburger.osgi.examples.greeter.ds2.provider;

import org.osgi.service.component.annotations.*;

import ch.vorburger.osgi.examples.greeter.api.GreetPrefixer;
import ch.vorburger.osgi.examples.greeter.api.Greeter;

@Component
public class GreeterDsImpl2 implements Greeter {

	// NB: Shame that we cannot use constructor injection with OSGi DS.. :-(
	private @Reference GreetPrefixer greetPrefixer; 
	private final GreetHelper greetHelper;
	
	public GreeterDsImpl2() {
		// NB: Having to do this here isn't great...
		this(new GreetHelperImpl(), null);
	}
	
	// This constructor isn't used by DS, but by our unit test  
	GreeterDsImpl2(GreetHelper greetHelper, GreetPrefixer greetPrefixer) {
		this.greetHelper = greetHelper;
		// NB: The trick with null above and if here is ugly!
		if (greetPrefixer != null)
			this.greetPrefixer = greetPrefixer;
	}

	@Override
	public String greet(String who) {
		String greeting = greetPrefixer.prefix("hello, " + who);
		return greetHelper.complete(greeting);
	}

}
