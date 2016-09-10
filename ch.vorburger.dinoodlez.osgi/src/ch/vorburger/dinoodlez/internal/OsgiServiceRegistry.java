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

		final List<IndexedServiceTracker<?, ?>> serviceTrackers;
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
				IndexedServiceTracker<?, ?> serviceTracker = getServiceTracker(bundleContext, i, serviceRequirement);
				serviceTracker.open();
				serviceTrackers.add(serviceTracker);
			}
		}

		IndexedServiceTracker<?,?> getServiceTracker(BundleContext bundleContext, int i, ServiceRequirement serviceRequirement) {
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

			if (filter != null) {
				return new IndexedServiceTracker<>(i, bundleContext, filter); 
			} else {
				return new IndexedServiceTracker<>(i, bundleContext, serviceRequirement.getClassName()); 
			}
		}
		
		void checkIfAllAreAvailable(int i, Object newServiceInstanceAtIndex) {
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
/*				
				if (serviceInstance == null) {
					log.error("getService() unexpectedly returned null, serviceTracker #" + serviceTracker.index + "'s serviceReference = " + serviceTracker.getServiceReference());
					available.set(false);
				}
*/				
				return serviceInstance;
			}).collect(Collectors.toList());
			serviceInstances.set(i, newServiceInstanceAtIndex);
			if (!available.get())
				return;
			availableCallback.onAvailable(serviceInstances);
		}
		
		void unavailable() {
			removedCallback.onRemoved();
			serviceTrackers.forEach(serviveTracker -> serviveTracker.unget());
		}
		
		@Override
		public void close() throws Exception {
			unavailable();
			for (ServiceTracker<?, ?> serviceTracker : serviceTrackers) {
				serviceTracker.close();
			}
			requirements.remove(this);
		}
		
		class IndexedServiceTracker<S, T> extends ServiceTracker<S, T> {

			final int index;
			
			public IndexedServiceTracker(int index, BundleContext context, Class<S> clazz /*, ServiceTrackerCustomizer<S, T> customizer */) {
				super(context, clazz, null);
				this.index = index;
			}

			public IndexedServiceTracker(int index, BundleContext context, Filter filter /*, ServiceTrackerCustomizer<S, T> customizer */) {
				super(context, filter, null);
				this.index = index;
			}

			public IndexedServiceTracker(int index, BundleContext context, ServiceReference<S> reference /*, ServiceTrackerCustomizer<S, T> customizer */) {
				super(context, reference, null);
				this.index = index;
			}

			public IndexedServiceTracker(int index, BundleContext context, String clazz /*, ServiceTrackerCustomizer customizer */) {
				super(context, clazz, null);
				this.index = index;
			}

			@Override
			public T addingService(ServiceReference<S> reference) {
				T service = super.addingService(reference);
				trackerAvailability.set(index, Boolean.TRUE);
				checkIfAllAreAvailable(index, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<S> reference, T service) {
				// super.modifiedService(reference, service);
				// TODO Is this correct, and good idea??
				//   removedService(reference, service);
				//   addingService(reference);
			}

			@Override
			public void removedService(ServiceReference<S> reference, T service) {
				super.removedService(reference, service);
				trackerAvailability.set(index, Boolean.FALSE);
				unavailable();
			}
			
			public void unget() {
				// TODO is this smart?
				close();
				open();
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
