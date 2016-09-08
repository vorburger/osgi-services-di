# osgi-services-di

This is NYADIF - "Not Yet Another [Dependency Injection](https://en.wikipedia.org/wiki/Dependency_injection) (DI) Framework".  

For now, best just to look at the code to understand:

1. Start with the API defined in _interface TODO_.
 

## Background

Just pick your-preferred-DI-today; e.g. [Guice](https://github.com/google/guice/wiki/Motivation), or perhaps [Dagger &#x2021; / 2](http://google.github.io/dagger/users-guide), or maybe even still [Spring Framework Core](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html#beans-introduction).  Or even CDI or [HK2](https://hk2.java.net), or back to where it all started with [PicoContainer](http://picocontainer.com), etc.  You can even use this library without any DI framework, and use good old simple manual wiring code - if you only have a few objects collaborating, what's often not a bad choice at all.

Then use this very small "glue" library to get whatever existing established DI framework you chose for wiring inside your modules (e.g. OSGi bundles) to fit nicely into your overall dynamic environment, such as e.g. OSGi (although the core API here actually does not depend on OSGi).  A dynamic environment is one where dependent services can come and go at runtime, and you register, and un-register and re-register your own globally visible shared services as and when the ones you depend on come and go.

Using any standard established DI framework makes it easy to write tests for non-trivial modules like OSGi bundles which have "internal" wiring, some of which you'd like to override with stubs in integration end-to-end tests.


## Related

* OSGi raw Service APIs
* OSGi Declarative Services (DS)
* OSGi Blueprint (BP) 
* [Weld OSGi](https://github.com/arcane86/weld-osgi-cdi), appears to have been [replaced by Pax CDI](https://docs.jboss.org/weld/reference/latest/en-US/html/environments.html#_osgi) 
* [OPS4J Pax CDI](https://ops4j1.jira.com/wiki/display/PAXCDI/Pax+CDI)
* [OPS4J Peaberry for Guice](https://github.com/ops4j/peaberry)
* _FTR: Spring Dynamic Modules became Eclipse Gemini Blueprint which became OSGi Blueprint implemented in Apache Aries_

### Why not OSGi Blueprint (BP)

BP got it all wrong.  It's basically Spring forced onto a dynamic environment with "damping" - JDK proxies that throw exceptions or block when dependent services have disappeared (or block and eventually fail after graceperiod timeout when they are not yet available); that just can't go well. - Also conceptually like CDI, too automagical and not explicit enough.

### Why not OSGi Declarative Services (DS)

DS got it right, and when used with annotations is neat. However it wants complete control over all of your objects wiring, and it seems difficult to marry it nicely with bundle "internal" wiring, and then write good tests.  (If it had an extension model or I knew how to nicely integrate what I'm doing in the approach in this library with DS, that could be interesting... but DS appears too closed, you cannot really hook into the object creation AFAIK.)  

### Why not Peaberry for Guice

Not actively mainainted anymore, tied to Guice, and sync instead of async Service Registry API approach. 

### Why not CDI via Pax CDI (formerly Weld OSGi?)

IMHO CDI got it all wrong.  Way too much magic auto wiring based on auto discovery, instead of simply explicit wiring.  The need for having an explicit @Alternative annotation says it all.  If you're fine with that, go for it.  You could certainly even use this library together with CDI.

### Why not simply OSGi's Service APIs

That's basically exactly what this small framework does!  It's really just a thin wrapper around [org.osgi.util.tracker.ServiceTracker](https://osgi.org/javadoc/r6/core/index.html?org/osgi/util/tracker/ServiceTracker.html) & [org.osgi.framework.BundleContext](https://osgi.org/javadoc/r6/core/index.html?org/osgi/framework/BundleContext.html)'s registerService().


## Not directly related

* [Sisu](https://www.eclipse.org/sisu/)
* [Mycila Guice Extensions](http://code.mycila.com/guice/)
* [Netflix Governator](https://github.com/Netflix/governator)
* [Netflix Karyon (retired)](https://github.com/Netflix/karyon)
* [Apache Onami Guice extensions (retired)](http://onami.apache.org)


