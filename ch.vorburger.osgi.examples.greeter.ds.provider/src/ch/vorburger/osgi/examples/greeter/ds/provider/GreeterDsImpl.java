package ch.vorburger.osgi.examples.greeter.ds.provider;

import org.osgi.service.component.annotations.*;

import ch.vorburger.osgi.examples.greeter.api.GreetPrefixer;
import ch.vorburger.osgi.examples.greeter.api.Greeter;

@Component
public class GreeterDsImpl implements Greeter {

	// NB: Shame that we cannot use constructor injection with OSGi DS.. :-(
	
	private @Reference GreetPrefixer greetPrefixer; 
	
	@Override
	public String greet(String who) {
		return greetPrefixer.prefix("hello, " + who);
	}

	// This setter isn't used by DS, but by our unit test  
	void setGreetPrefixer(GreetPrefixer greetPrefixer) {
		this.greetPrefixer = greetPrefixer;
	}
}
