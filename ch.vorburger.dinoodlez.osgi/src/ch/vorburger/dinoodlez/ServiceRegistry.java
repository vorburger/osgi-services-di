/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package ch.vorburger.dinoodlez;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

import ch.vorburger.dinoodlez.internal.ServiceRegistrySingleton;

/**
 * Asynchronous Service Registry.
 *
 * <p>Something to look up a set of Service instances in, and get notified as they become available.
 *
 * @author Michael Vorburger.ch
 */
@ProviderType
public interface ServiceRegistry {
	// TODO file Add Optional services list support 

	
	ServiceRegistry INSTANCE = ServiceRegistrySingleton.getInstance();

	// TODO Add support for Optional services list; or (better) make this part of ServiceDescriptor (and provide an Optional in the callback list)

	AutoCloseable require(Object context,
			List<ServiceRequirement> requirements,
			ServiceRequirementsAvailableCallback availableCallback, 
			ServiceRequirementRemovedCallback removedCallback);
	
	// TODO register as a new method variant of require - useful for monitoring to know what it would create..
	// New method much better than an optional argument to.  Empty requirements not allowed.
	
	// TODO void register(NOT ServiceRequirement but ServiceDescriptor<T> serviceDescriptor, T serviceInstance);
	// TODO   multi-interface: Class<?> lookupType, Class<?> ... lookupTypes ?
    // How to un-register service? Probably by return value from this method?
    // void unregisterService(Object service);

}
