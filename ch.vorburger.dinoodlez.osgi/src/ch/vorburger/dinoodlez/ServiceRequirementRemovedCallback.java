/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package ch.vorburger.dinoodlez;

import org.osgi.annotation.versioning.ConsumerType;

/**
 * Callback informing that (at least) one {@link ServiceRequirement} passed to
 * {@link ServiceRegistry#require(Object, java.util.List, ServiceRegistryRequirementsAvailableCallback, ServiceRegistryRequirementRemovedCallback)}
 * is no longer available.
 * 
 * @author Michael Vorburger.ch
 */
@ConsumerType
@FunctionalInterface
public interface ServiceRequirementRemovedCallback {

    void onRemoved();

}
