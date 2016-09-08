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

public class Activator implements BundleActivator {

	private OsgiServiceRegistry serviceRegistry;

	@Override
	public void start(BundleContext context) throws Exception {
		serviceRegistry = new OsgiServiceRegistry();
		ServiceRegistrySingleton.setInstance(serviceRegistry);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		serviceRegistry.close();
		ServiceRegistrySingleton.setInstance(null);
	}

}
