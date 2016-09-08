/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package ch.vorburger.dinoodlez.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import ch.vorburger.dinoodlez.ServiceRegistry;
import ch.vorburger.dinoodlez.ServiceRequirement;
import ch.vorburger.dinoodlez.ServiceRequirementRemovedCallback;
import ch.vorburger.dinoodlez.ServiceRequirementsAvailableCallback;

/**
 * Implementation of {@link ServiceRegistry} ServiceRegistry based on OSGi
 * {@link ServiceTracker} & {@link ServiceRegistration}.
 * 
 * @author Michael Vorburger.ch
 */
public class OsgiServiceRegistry implements ServiceRegistry, AutoCloseable {
	// TODO implements ServiceRegistryMonitor
	
	private class Requirement implements AutoCloseable {

		final List<ServiceTracker<?, ?>> serviceTrackers;
		final List<Boolean> trackerAvailability;
		final ServiceRequirementsAvailableCallback availableCallback;
		final ServiceRequirementRemovedCallback removedCallback;
		
		Requirement(BundleContext bundleContext,
				List<ServiceRequirement> requirements,
				ServiceRequirementsAvailableCallback availableCallback,
				ServiceRequirementRemovedCallback removedCallback) 
		{
			this.availableCallback = availableCallback;
			this.removedCallback = removedCallback;

			int n = requirements.size();
			this.trackerAvailability = new CopyOnWriteArrayList<>(new Boolean[n]);
			this.serviceTrackers = new ArrayList<>(n);
			
			for (int i = 0; i < n; i++) {
				ServiceRequirement serviceRequirement = requirements.get(i);
				this.trackerAvailability.set(i, Boolean.FALSE);
				ServiceTracker<?, ?> serviceTracker = getServiceTracker(bundleContext, i, serviceRequirement);
				serviceTracker.open();
				serviceTrackers.add(serviceTracker);
			}
		}

		ServiceTracker<?,?> getServiceTracker(BundleContext bundleContext, int i, ServiceRequirement serviceRequirement) {
			Filter filter = null;
			if (serviceRequirement.getFilterObject().isPresent()) {
				filter = (Filter) serviceRequirement.getFilterObject().get();
			} else {
				String filterString = null;
				if (!serviceRequirement.getProperties().isEmpty()) {
					throw new UnsupportedOperationException("TODO implement transformation of Map to filterString");
				} else if (serviceRequirement.getFilterString().isPresent()) {
					filterString = serviceRequirement.getFilterString().get();
				}
				if (filterString != null) {
					try {
						filter = bundleContext.createFilter(filterString);
					} catch (InvalidSyntaxException e) {
						throw new IllegalArgumentException("Bad filter String: " + filterString, e);
					}
				}
			}

			ServiceTrackerCustomizer<?,?> customizer = new IndexedTracker(i);
			if (filter != null) {
				return new ServiceTracker<>(bundleContext, filter, customizer); 
			} else {
				return new ServiceTracker<>(bundleContext, serviceRequirement.getClassName(), customizer); 
			}
		}
		
		void checkIfAllAreAvailable() {
			AtomicBoolean available = new AtomicBoolean(true);
			for (Boolean trackerAvailability : trackerAvailability) {
				if (!trackerAvailability) {
					available.set(false);
					break;
				}
			}
			if (!available.get())
				return;
			List<Object> serviceInstances = serviceTrackers.stream().map(serviceTracker -> {
				Object serviceInstance = serviceTracker.getService();
				if (serviceInstance == null) {
					log.error("getService() unexpectedly returned null, serviceTracker's serviceReference = " + serviceTracker.getServiceReference());
					available.set(false);
				}
				return serviceInstance;
			}).collect(Collectors.toList());
			if (!available.get())
				return;
			availableCallback.provide(serviceInstances);
		}
		
		void unavailable() {
			removedCallback.removed();
			// TODO ungetService for each serviceTracker 
		}
		
		@Override
		public void close() throws Exception {
			unavailable();
			for (ServiceTracker<?, ?> serviceTracker : serviceTrackers) {
				serviceTracker.close();
			}
			requirements.remove(this);
		}
		
		@SuppressWarnings("rawtypes")
		class IndexedTracker implements ServiceTrackerCustomizer {

			final int index;
			
			protected IndexedTracker(int index) {
				this.index = index;
			}

			@Override
			public Object addingService(ServiceReference reference) {
				trackerAvailability.set(index, Boolean.TRUE);
				checkIfAllAreAvailable();
				return null;
			}

			@Override
			public void modifiedService(ServiceReference reference, Object service) {
				// TODO What do we do in this case?
			}

			@Override
			public void removedService(ServiceReference reference, Object service) {
				trackerAvailability.set(index, Boolean.FALSE);
				unavailable();
			}
		}
	}
	
	private final Logger log;
	
	private final Collection<Requirement> requirements = new ConcurrentLinkedQueue<>(); // CopyOnWriteArrayList ?
	
	public OsgiServiceRegistry(Logger logger) {
		this.log = logger;
	}

	@Override
	public AutoCloseable require(Object context, List<ServiceRequirement> requirements,
			ServiceRequirementsAvailableCallback availableCallback,
			ServiceRequirementRemovedCallback removedCallback) {
		try {
			if (requirements == null)
				throw new NullPointerException("requirements");
			if (requirements.isEmpty())
				throw new IllegalArgumentException("requirements is empty (this call is thus useless)");
			BundleContext bundleContext = getBundleContext(context);
			return new Requirement(bundleContext, requirements, availableCallback, removedCallback);
		} catch (Exception e) {
			log.error("require() failed", e);
			throw e;
		}
	}

	protected BundleContext getBundleContext(Object context) {
		if (context instanceof BundleContext) {
			return (BundleContext) context;
		} else if (context instanceof Class<?>) {
			Class<?> contextClass = (Class<?>) context;
			return getBundleContext(contextClass);

		} else {
			throw new IllegalArgumentException("context is neither a BundleContext nor a class: " + context);
		}
	}

	protected static BundleContext getBundleContext(Class<?> clazz) {
		Bundle bundle = FrameworkUtil.getBundle(clazz);
		if (bundle == null)
			throw new IllegalArgumentException("No Bundle for class (are you running in Java SE standalone instead of OSGi?): " + clazz);
		return bundle.getBundleContext();
	}
	
	@Override
	public void close() throws Exception {
		for (Requirement requirement : requirements) {
			requirement.close();
		}
	}

}
