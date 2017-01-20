package application;

import org.glassfish.jersey.server.ResourceConfig;

public class APIApplication extends ResourceConfig{
	public APIApplication() {
		packages(true, "application.api");
	}
}
