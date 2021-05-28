/*
 * Copyright (c) 2004, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jakarta.xml.soap;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


class FactoryFinder {

    private static final Logger logger;

    private static final ServiceLoaderUtil.ExceptionHandler<SOAPException> EXCEPTION_HANDLER =
            new ServiceLoaderUtil.ExceptionHandler<SOAPException>() {
                @Override
                public SOAPException createException(Throwable throwable, String message) {
                    return new SOAPException(message, throwable);
                }
            };

    private final static PrivilegedAction<String> propertyAction = () -> System.getProperty("saaj.debug");

    static {
        logger = Logger.getLogger("jakarta.xml.soap");
        try {
            if (AccessController.doPrivileged(propertyAction) != null) {
                // disconnect the logger from a bigger framework (if any)
                // and take the matters into our own hands
                logger.setUseParentHandlers(false);
                logger.setLevel(Level.ALL);
                ConsoleHandler handler = new ConsoleHandler();
                handler.setLevel(Level.ALL);
                logger.addHandler(handler);
            } else {
                // don't change the setting of this logger
                // to honor what other frameworks
                // have done on configurations.
            }
        } catch (Throwable t) {
            // just to be extra safe. in particular System.getProperty may throw
            // SecurityException.
            logger.log(Level.SEVERE, "Exception during loading the class", t);
        }
    }

    /**
     * Finds the implementation {@code Class} object for the given
     * factory type.  If it fails and {@code tryFallback} is {@code true}
     * finds the {@code Class} object for the given default class name.
     * The arguments supplied must be used in order
     * Note the default class name may be needed even if fallback
     * is not to be attempted in order to check if requested type is fallback.
     * <P>
     * This method is package private so that this code can be shared.
     *
     * @return the {@code Class} object of the specified message factory;
     *         may not be {@code null}
     *
     * @param factoryClass          factory abstract class or interface to be found
     * @param defaultClassName      the implementation class name, which is
     *                              to be used only if nothing else
     *                              is found; {@code null} to indicate
     *                              that there is no default class name
     * @param tryFallback           whether to try the default class as a
     *                              fallback
     * @exception SOAPException if there is a SOAP error
     */
    @SuppressWarnings("unchecked")
    static <T> T find(Class<T> factoryClass,
                      String defaultClassName,
                      boolean tryFallback) throws SOAPException {

        ClassLoader tccl = ServiceLoaderUtil.contextClassLoader(EXCEPTION_HANDLER);
        String factoryId = factoryClass.getName();

        // Use the system property first
        String className = fromSystemProperty(factoryId);
        if (className != null) {
            Object result = newInstance(className, defaultClassName, tccl);
            if (result != null) {
                return (T) result;
            }
        }

        // try to read from $java.home/lib/jaxm.properties
        className = fromJDKProperties(factoryId);
        if (className != null) {
            Object result = newInstance(className, defaultClassName, tccl);
            if (result != null) {
                return (T) result;
            }
        }

        // standard services: java.util.ServiceLoader
        T factory = ServiceLoaderUtil.firstByServiceLoader(
                factoryClass,
                logger,
                EXCEPTION_HANDLER);
        if (factory != null) {
            return factory;
        }

        // handling Glassfish/OSGi (platform specific default)
        if (isOsgi()) {
            Object result = lookupUsingOSGiServiceLoader(factoryId);
            if (result != null) {
                return (T) result;
            }
        }

        // If not found and fallback should not be tried, return a null result.
        if (!tryFallback)
            return null;

        // We didn't find the class through the usual means so try the default
        // (built in) factory if specified.
        if (defaultClassName == null) {
            throw new SOAPException(
                    "Provider for " + factoryId + " cannot be found", null);
        }
        logger.fine("Trying to create the default implementation of the message factory");
        return (T) newInstance(defaultClassName, defaultClassName, tccl);
    }

    private static Object newInstance(String className, String defaultClassName, ClassLoader tccl) throws SOAPException {
        Object newInstance = ServiceLoaderUtil.newInstance(
                className,
                defaultClassName,
                tccl,
                EXCEPTION_HANDLER);

        if (logger.isLoggable(Level.FINE)) {
            // extra check to avoid costly which operation if not logged
            Class<?> newInstanceClass = newInstance.getClass();
            logger.log(
                    Level.FINE,
                    "loaded {0} from {1}", new Object[]{newInstanceClass.getName(), which(newInstanceClass)}
            );
        }
        return newInstance;
    }

    private static String fromJDKProperties(String factoryId) {
        Path path = null;
        try {
            String JAVA_HOME = getSystemProperty("java.home");
            path = Paths.get(JAVA_HOME, "conf", "jaxm.properties");
            logger.log(Level.FINE, "Checking configuration in {0}", path);

            // to ensure backwards compatibility
            if (!Files.exists(path)) {
                path = Paths.get(JAVA_HOME, "lib", "jaxm.properties");
            }

            logger.log(Level.FINE, "Checking configuration in {0}", path);
            if (Files.exists(path)) {
                Properties props = new Properties();
                try (InputStream inputStream = Files.newInputStream(path)) {
                    props.load(inputStream);
                }

                // standard property
                logger.log(Level.FINE, "Checking property {0}", factoryId);
                String factoryClassName = props.getProperty(factoryId);
                logFound(factoryClassName);
                if (factoryClassName != null) {
                    return factoryClassName;
                }

            }
        } catch (Exception ignored) {
            logger.log(Level.SEVERE, "Error reading SAAJ configuration from ["  + path +
                    "] file. Check it is accessible and has correct format.", ignored);
        }
        return null;
    }

    private static String fromSystemProperty(String factoryId) {
        String systemProp = getSystemProperty(factoryId);
        if (systemProp != null) {
            return systemProp;
        }
        return null;
    }

    private static String getSystemProperty(final String property) {
        logger.log(Level.FINE, "Checking system property {0}", property);
        String value = AccessController.doPrivileged(new PrivilegedAction<String>() {
            @Override
            public String run() {
                return System.getProperty(property);
            }
        });
        logFound(value);
        return value;
    }

    private static void logFound(String value) {
        if (value != null) {
            logger.log(Level.FINE, "  found {0}", value);
        } else {
            logger.log(Level.FINE, "  not found");
        }
    }

    private static final String OSGI_SERVICE_LOADER_CLASS_NAME = "org.glassfish.hk2.osgiresourcelocator.ServiceLoader";

    private static boolean isOsgi() {
        try {
            Class.forName(OSGI_SERVICE_LOADER_CLASS_NAME);
            return true;
        } catch (ClassNotFoundException ignored) {
            logger.log(
                    Level.SEVERE,
                    "Class " + OSGI_SERVICE_LOADER_CLASS_NAME + " cannot be loaded",
                    ignored
            );
        }
        return false;
    }

    private static Object lookupUsingOSGiServiceLoader(String factoryId) {
        try {
            logger.fine("Trying to create the provider from the OSGi ServiceLoader");
            // Use reflection to avoid having any dependendcy on HK2 ServiceLoader class
            Class<?> serviceClass = Class.forName(factoryId);
            Class<?>[] args = new Class[]{serviceClass};
            Class<?> target = Class.forName(OSGI_SERVICE_LOADER_CLASS_NAME);
            Method m = target.getMethod("lookupProviderInstances", Class.class);
            Iterator<?> iter = ((Iterable) m.invoke(null, (Object[]) args)).iterator();
            return iter.hasNext() ? iter.next() : null;
        } catch (Exception ignored) {
            // log and continue
            logger.log(
                    Level.SEVERE,
                    "Access to the system property with key " + factoryId + " is not allowed",
                    ignored
            );
            return null;
        }
    }

    /**
     * Get the URL for the Class from it's ClassLoader.
     *
     * Convenience method for {@link #which(Class, ClassLoader)}.
     *
     * Equivalent to calling: which(clazz, clazz.getClassLoader())
     *
     * @param clazz
     *          The class to search for
     * @return
     *          the URL for the class or null if it wasn't found
     */
    static URL which(Class clazz) {
        return which(clazz, getClassClassLoader(clazz));
    }

    /**
     * Search the given ClassLoader for an instance of the specified class and
     * return a string representation of the URL that points to the resource.
     *
     * @param clazz
     *          The class to search for
     * @param loader
     *          The ClassLoader to search.  If this parameter is null, then the
     *          system class loader will be searched
     * @return
     *          the URL for the class or null if it wasn't found
     */
    static URL which(Class clazz, ClassLoader loader) {

        String classnameAsResource = clazz.getName().replace('.', '/') + ".class";

        if (loader == null) {
            loader = getSystemClassLoader();
        }

        return loader.getResource(classnameAsResource);
    }

    private static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        } else {
            return (ClassLoader) java.security.AccessController.doPrivileged(
                    (PrivilegedAction) ClassLoader::getSystemClassLoader);
        }
    }

    @SuppressWarnings("unchecked")
    private static ClassLoader getClassClassLoader(final Class c) {
        if (System.getSecurityManager() == null) {
            return c.getClassLoader();
        } else {
            return (ClassLoader) java.security.AccessController.doPrivileged(
                    (PrivilegedAction) c::getClassLoader);
        }
    }

}
