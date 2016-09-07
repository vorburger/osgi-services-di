package ch.vorburger.osgi.examples.greeter.ds.provider;

import org.osgi.service.component.annotations.*;

import ch.vorburger.osgi.examples.greeter.api.Greeter;

@Component
public class GreeterDsImpl implements Greeter {

	@Override
	public String greet(String who) {
		return "hello, " + who;
	}

}
