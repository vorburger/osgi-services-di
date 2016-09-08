package ch.vorburger.osgi.examples.greeter.dinoodlez.provider;

public class GreetHelperImpl implements GreetHelper {

	@Override
	public String complete(String greeting) {
		return greeting + "...";
	}

}
