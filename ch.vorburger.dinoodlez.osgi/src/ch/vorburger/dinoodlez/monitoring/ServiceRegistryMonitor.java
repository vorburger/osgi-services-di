/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package ch.vorburger.dinoodlez.monitoring;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

import ch.vorburger.dinoodlez.ServiceRequirement;
import ch.vorburger.dinoodlez.ServiceRegistry;

/**
 * Monitor the state of the {@link ServiceRegistry}.
 *
 * <p>Obtain an instance of this service by looking it up itself in the ServiceRegistry.
 *
 * @author Michael Vorburger.ch
 */
@ProviderType
public interface ServiceRegistryMonitor {

    /**
     * Is the Service Registry fully resolved?
     *
     * Note that this is a snapshot in time, not necessarily a state that is
     * initially false and then once the system has successfully started up
     * always remains true. In a dynamic service environment such as OSGi, this
     * may be true at some point and then later false again.
     *
     * @return true or false
     */
	default boolean isFullyResolved() {
		return getUnresolvedServiceRequirements().isEmpty();
    }

	// TODO This should eventually return a more useful data structure than just flat List<ServiceRequirement>
	// It should be a tree incl. requester context (i.e. Bundle info; with String getDescription)
	// and sth. like ch.vorburger.inject.ServiceDescription.. promisedServices
    List<ServiceRequirement> getUnresolvedServiceRequirements();

    // There currently is no getResolvedServiceDescriptions(),
    // just because that's the same as the list of currently available OSGi Services,
    // for which there already are monitoring tools such as CLI commands and (Felix) Web UI.
    // If this ever has any use / need outside of OSGi, then add such a method.

}
