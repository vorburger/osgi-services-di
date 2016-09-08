/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package ch.vorburger.dinoodlez.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ch.vorburger.dinoodlez.ServiceRegistry;

/**
 * BundleActivator which installs an {@link OsgiServiceRegistry} into the
 * {@link ServiceRegistrySingleton}, so that {@link ServiceRegistry#INSTANCE} works. 
 * 
 * @author Michael Vorburger
 */
public class Activator implements BundleActivator {

	private static OsgiServiceRegistry serviceRegistry;

	static void start() {
		if (serviceRegistry == null) {
			Logger logger = new SystemOutErrLogger();
			// TODO If available look up, else fallback: http://felix.apache.org/documentation/subprojects/apache-felix-log.html
			// Implement this by eating our own dog food - intro. optional dep. and restart ServiceRegistry if becomes available!
			// TODO Alternatively opt. look up https://ops4j1.jira.com/wiki/display/paxlogging/How+to+use+Pax+Logging+in+my+bundles
			serviceRegistry = new OsgiServiceRegistry(logger);
			ServiceRegistrySingleton.setInstance(serviceRegistry);
		}
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (serviceRegistry != null) {
			serviceRegistry.close();
			serviceRegistry = null;
		}
		ServiceRegistrySingleton.setInstance(null);
	}

}
