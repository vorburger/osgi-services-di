package ch.vorburger.osgi.examples.greeter.ds2.provider;

public class GreetHelperImpl implements GreetHelper {

	@Override
	public String complete(String greeting) {
		return greeting + ".";
	}

}
