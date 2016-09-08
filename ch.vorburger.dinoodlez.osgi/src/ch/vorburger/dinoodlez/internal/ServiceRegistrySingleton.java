/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package ch.vorburger.dinoodlez.internal;

import ch.vorburger.dinoodlez.ServiceRegistry;

public class ServiceRegistrySingleton {

	private static ServiceRegistry instance;
	
	public static ServiceRegistry getInstance() {
		if (instance == null) {
			Activator.start();
			if (instance == null) {
				logAndThrowException("Something should have set instance before this ever got called");
			}
		}
		return instance;
	}

	static void setInstance(ServiceRegistry serviceRegistry) {
		if (instance != null) {
			if (serviceRegistry != null) {
				logAndThrowException("Something already set instance");
			}
		}
		instance = serviceRegistry;
	}

	private static void logAndThrowException(String message) {
		IllegalStateException e = new IllegalStateException(message);
		e.printStackTrace(System.err);
		throw e;
	}
	
}
