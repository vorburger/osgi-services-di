/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package ch.vorburger.dinoodlez;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import ch.vorburger.dinoodlez.ServiceRequirement.Builders.ServiceRequirementFilterBuilder;

/**
 * Requirement for a Service.
 * 
 * @author Michael Vorburger.ch
 */
public class ServiceRequirement {

	// TODO set/isMultiple ?
	// TODO set/isOptional ?

	private final String className;
	private final Map<String, ?> properties;
	private final Optional<String> filterString;
	private final Optional<Object> filterObject;

	private ServiceRequirement(String className, Map<String, ?> properties, Optional<String> filterString, Optional<Object> filterObject) { 
		this.className = className;
		this.properties = properties;
		this.filterString = filterString;
		this.filterObject = filterObject;
	}

	public String getClassName() {
		return className;
	}

	public Map<String, ?> getProperties() {
		return properties;
	}

	public Optional<String> getFilterString() {
		return filterString;
	}

	public Optional<Object> getFilterObject() {
		return filterObject;
	}

	@Override
	public int hashCode() {
		return Objects.hash(className, properties, filterString, filterObject);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceRequirement other = (ServiceRequirement) obj;
		if (!className.equals(other.className))
			return false;
		if (!filterObject.equals(other.filterObject))
			return false;
		if (!filterString.equals(other.filterString))
			return false;
		if (!properties.equals(other.properties))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ServiceRequirement{");
		sb.append("className=").append(className);
		if (!properties.isEmpty()) {
			sb.append(", properties=").append(properties);
		}
		if (filterString.isPresent()) {
			sb.append(", filterString=").append(filterString.get());
		}
		if (filterObject.isPresent()) {
			sb.append(", filterObject=").append(filterObject.get().toString());
		}
		return sb.append('}').toString();
	}
	
	public static ServiceRequirementFilterBuilder of(String clazzName) {
		return new ServiceRequirementFilterBuilder(clazzName);
	}

	public static ServiceRequirementFilterBuilder of(Class<?> clazz) {
		return of(clazz.getName());
	}

	protected interface Builders {
        // Inner interface just to group all builders, and make them less visible on Ctrl-Space in IDE

		public static class ServiceRequirementFilterBuilder {
			private final String clazzName;
			
			private ServiceRequirementFilterBuilder(String clazzName) { 
				this.clazzName = clazzName;
			}
			
			public ServiceRequirementFilterPropertyBuilder addProperty(String key, Object value) {
				ServiceRequirementFilterPropertyBuilder builder = new ServiceRequirementFilterPropertyBuilder(clazzName);
				builder.addProperty(key, value);
				return builder;
			}
	
			public ServiceRequirement build() {
				return new ServiceRequirement(clazzName, Collections.emptyMap(), Optional.empty(), Optional.empty());
			}
			
			public ServiceRequirement filter(String filter) {
				return new ServiceRequirement(clazzName, Collections.emptyMap(), Optional.of(filter), Optional.empty());
			}
			
			public ServiceRequirement filter(Object filter) {
				return new ServiceRequirement(clazzName, Collections.emptyMap(), Optional.empty(), Optional.of(filter));
			}
		}
		
		public static class ServiceRequirementFilterPropertyBuilder {
			private final String clazzName;
			private final Map<String, Object> properties = new HashMap<>();
	
			private ServiceRequirementFilterPropertyBuilder(String clazzName) {
				super();
				this.clazzName = clazzName;
			}

			public ServiceRequirementFilterPropertyBuilder addProperty(String key, Object value) {
				properties.put(key, value);
				return this;
			}

			public ServiceRequirement build() {
				return new ServiceRequirement(clazzName, Collections.unmodifiableMap(properties), Optional.empty(), Optional.empty());
			}
	
		}
	}
}
