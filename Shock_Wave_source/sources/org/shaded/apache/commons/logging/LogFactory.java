package org.shaded.apache.commons.logging;

import gnu.kawa.functions.GetNamedPart;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import org.shaded.apache.http.protocol.HTTP;

public abstract class LogFactory {
    public static final String DIAGNOSTICS_DEST_PROPERTY = "org.shaded.apache.commons.logging.diagnostics.dest";
    public static final String FACTORY_DEFAULT = "org.shaded.apache.commons.logging.impl.LogFactoryImpl";
    public static final String FACTORY_PROPERTIES = "commons-logging.properties";
    public static final String FACTORY_PROPERTY = "org.shaded.apache.commons.logging.LogFactory";
    public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.shaded.apache.commons.logging.LogFactory.HashtableImpl";
    public static final String PRIORITY_KEY = "priority";
    protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
    public static final String TCCL_KEY = "use_tccl";
    private static final String WEAK_HASHTABLE_CLASSNAME = "org.shaded.apache.commons.logging.impl.WeakHashtable";
    static Class class$java$lang$Thread;
    static Class class$org$apache$commons$logging$LogFactory;
    private static String diagnosticPrefix;
    private static PrintStream diagnosticsStream = null;
    protected static Hashtable factories;
    protected static LogFactory nullClassLoaderFactory = null;
    private static ClassLoader thisClassLoader;

    public abstract Object getAttribute(String str);

    public abstract String[] getAttributeNames();

    public abstract Log getInstance(Class cls) throws LogConfigurationException;

    public abstract Log getInstance(String str) throws LogConfigurationException;

    public abstract void release();

    public abstract void removeAttribute(String str);

    public abstract void setAttribute(String str, Object obj);

    static void access$000(String x0) {
        logDiagnostic(x0);
    }

    static {
        Class cls;
        Class cls2;
        factories = null;
        if (class$org$apache$commons$logging$LogFactory == null) {
            cls = class$(FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = cls;
        } else {
            cls = class$org$apache$commons$logging$LogFactory;
        }
        thisClassLoader = getClassLoader(cls);
        initDiagnostics();
        if (class$org$apache$commons$logging$LogFactory == null) {
            cls2 = class$(FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = cls2;
        } else {
            cls2 = class$org$apache$commons$logging$LogFactory;
        }
        logClassLoaderEnvironment(cls2);
        factories = createFactoryStore();
        if (isDiagnosticsEnabled()) {
            logDiagnostic("BOOTSTRAP COMPLETED");
        }
    }

    protected LogFactory() {
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v6, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.util.Hashtable} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final java.util.Hashtable createFactoryStore() {
        /*
            r3 = 0
            java.lang.String r6 = "org.shaded.apache.commons.logging.LogFactory.HashtableImpl"
            r7 = 0
            java.lang.String r4 = getSystemProperty(r6, r7)     // Catch:{ SecurityException -> 0x0020 }
        L_0x0008:
            if (r4 != 0) goto L_0x000c
            java.lang.String r4 = "org.shaded.apache.commons.logging.impl.WeakHashtable"
        L_0x000c:
            java.lang.Class r2 = java.lang.Class.forName(r4)     // Catch:{ Throwable -> 0x0023 }
            java.lang.Object r6 = r2.newInstance()     // Catch:{ Throwable -> 0x0023 }
            r0 = r6
            java.util.Hashtable r0 = (java.util.Hashtable) r0     // Catch:{ Throwable -> 0x0023 }
            r3 = r0
        L_0x0018:
            if (r3 != 0) goto L_0x001f
            java.util.Hashtable r3 = new java.util.Hashtable
            r3.<init>()
        L_0x001f:
            return r3
        L_0x0020:
            r1 = move-exception
            r4 = 0
            goto L_0x0008
        L_0x0023:
            r5 = move-exception
            java.lang.String r6 = "org.shaded.apache.commons.logging.impl.WeakHashtable"
            boolean r6 = r6.equals(r4)
            if (r6 != 0) goto L_0x0018
            boolean r6 = isDiagnosticsEnabled()
            if (r6 == 0) goto L_0x0038
            java.lang.String r6 = "[ERROR] LogFactory: Load of custom hashtable failed"
            logDiagnostic(r6)
            goto L_0x0018
        L_0x0038:
            java.io.PrintStream r6 = java.lang.System.err
            java.lang.String r7 = "[ERROR] LogFactory: Load of custom hashtable failed"
            r6.println(r7)
            goto L_0x0018
        */
        throw new UnsupportedOperationException("Method not decompiled: org.shaded.apache.commons.logging.LogFactory.createFactoryStore():java.util.Hashtable");
    }

    private static String trim(String src) {
        if (src == null) {
            return null;
        }
        return src.trim();
    }

    public static LogFactory getFactory() throws LogConfigurationException {
        BufferedReader rd;
        String useTCCLStr;
        ClassLoader contextClassLoader = getContextClassLoaderInternal();
        if (contextClassLoader == null && isDiagnosticsEnabled()) {
            logDiagnostic("Context classloader is null.");
        }
        LogFactory factory = getCachedFactory(contextClassLoader);
        if (factory != null) {
            return factory;
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("[LOOKUP] LogFactory implementation requested for the first time for context classloader ").append(objectId(contextClassLoader)).toString());
            logHierarchy("[LOOKUP] ", contextClassLoader);
        }
        Properties props = getConfigurationFile(contextClassLoader, FACTORY_PROPERTIES);
        ClassLoader baseClassLoader = contextClassLoader;
        if (!(props == null || (useTCCLStr = props.getProperty(TCCL_KEY)) == null || Boolean.valueOf(useTCCLStr).booleanValue())) {
            baseClassLoader = thisClassLoader;
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic("[LOOKUP] Looking for system property [org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
        }
        try {
            String factoryClass = getSystemProperty(FACTORY_PROPERTY, (String) null);
            if (factoryClass != null) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("[LOOKUP] Creating an instance of LogFactory class '").append(factoryClass).append("' as specified by system property ").append(FACTORY_PROPERTY).toString());
                }
                factory = newFactory(factoryClass, baseClassLoader, contextClassLoader);
            } else if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] No system property [org.apache.commons.logging.LogFactory] defined.");
            }
        } catch (SecurityException e) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [").append(trim(e.getMessage())).append("]. Trying alternative implementations...").toString());
            }
        } catch (RuntimeException e2) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("[LOOKUP] An exception occurred while trying to create an instance of the custom factory class: [").append(trim(e2.getMessage())).append("] as specified by a system property.").toString());
            }
            throw e2;
        }
        if (factory == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] Looking for a resource file of name [META-INF/services/org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
            }
            try {
                InputStream is = getResourceAsStream(contextClassLoader, SERVICE_ID);
                if (is != null) {
                    try {
                        rd = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8));
                    } catch (UnsupportedEncodingException e3) {
                        rd = new BufferedReader(new InputStreamReader(is));
                    }
                    String factoryClassName = rd.readLine();
                    rd.close();
                    if (factoryClassName != null && !"".equals(factoryClassName)) {
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(new StringBuffer().append("[LOOKUP]  Creating an instance of LogFactory class ").append(factoryClassName).append(" as specified by file '").append(SERVICE_ID).append("' which was present in the path of the context").append(" classloader.").toString());
                        }
                        factory = newFactory(factoryClassName, baseClassLoader, contextClassLoader);
                    }
                } else if (isDiagnosticsEnabled()) {
                    logDiagnostic("[LOOKUP] No resource file with name 'META-INF/services/org.apache.commons.logging.LogFactory' found.");
                }
            } catch (Exception ex) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [").append(trim(ex.getMessage())).append("]. Trying alternative implementations...").toString());
                }
            }
        }
        if (factory == null) {
            if (props != null) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("[LOOKUP] Looking in properties file for entry with key 'org.apache.commons.logging.LogFactory' to define the LogFactory subclass to use...");
                }
                String factoryClass2 = props.getProperty(FACTORY_PROPERTY);
                if (factoryClass2 != null) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file specifies LogFactory subclass '").append(factoryClass2).append("'").toString());
                    }
                    factory = newFactory(factoryClass2, baseClassLoader, contextClassLoader);
                } else if (isDiagnosticsEnabled()) {
                    logDiagnostic("[LOOKUP] Properties file has no entry specifying LogFactory subclass.");
                }
            } else if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] No properties file available to determine LogFactory subclass from..");
            }
        }
        if (factory == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] Loading the default LogFactory implementation 'org.apache.commons.logging.impl.LogFactoryImpl' via the same classloader that loaded this LogFactory class (ie not looking in the context classloader).");
            }
            factory = newFactory(FACTORY_DEFAULT, thisClassLoader, contextClassLoader);
        }
        if (factory != null) {
            cacheFactory(contextClassLoader, factory);
            if (props != null) {
                Enumeration names = props.propertyNames();
                while (names.hasMoreElements()) {
                    String name = (String) names.nextElement();
                    factory.setAttribute(name, props.getProperty(name));
                }
            }
        }
        return factory;
    }

    public static Log getLog(Class clazz) throws LogConfigurationException {
        return getFactory().getInstance(clazz);
    }

    public static Log getLog(String name) throws LogConfigurationException {
        return getFactory().getInstance(name);
    }

    public static void release(ClassLoader classLoader) {
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Releasing factory for classloader ").append(objectId(classLoader)).toString());
        }
        synchronized (factories) {
            if (classLoader != null) {
                LogFactory factory = (LogFactory) factories.get(classLoader);
                if (factory != null) {
                    factory.release();
                    factories.remove(classLoader);
                }
            } else if (nullClassLoaderFactory != null) {
                nullClassLoaderFactory.release();
                nullClassLoaderFactory = null;
            }
        }
    }

    public static void releaseAll() {
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Releasing factory for all classloaders.");
        }
        synchronized (factories) {
            Enumeration elements = factories.elements();
            while (elements.hasMoreElements()) {
                ((LogFactory) elements.nextElement()).release();
            }
            factories.clear();
            if (nullClassLoaderFactory != null) {
                nullClassLoaderFactory.release();
                nullClassLoaderFactory = null;
            }
        }
    }

    protected static ClassLoader getClassLoader(Class clazz) {
        try {
            return clazz.getClassLoader();
        } catch (SecurityException ex) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("Unable to get classloader for class '").append(clazz).append("' due to security restrictions - ").append(ex.getMessage()).toString());
            }
            throw ex;
        }
    }

    protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
        return directGetContextClassLoader();
    }

    private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                return LogFactory.directGetContextClassLoader();
            }
        });
    }

    protected static ClassLoader directGetContextClassLoader() throws LogConfigurationException {
        Class cls;
        Class cls2;
        try {
            if (class$java$lang$Thread == null) {
                Class class$ = class$("java.lang.Thread");
                class$java$lang$Thread = class$;
                cls2 = class$;
            } else {
                cls2 = class$java$lang$Thread;
            }
            return (ClassLoader) cls2.getMethod("getContextClassLoader", (Class[]) null).invoke(Thread.currentThread(), (Object[]) null);
        } catch (IllegalAccessException e) {
            throw new LogConfigurationException("Unexpected IllegalAccessException", e);
        } catch (InvocationTargetException e2) {
            if (e2.getTargetException() instanceof SecurityException) {
                return null;
            }
            throw new LogConfigurationException("Unexpected InvocationTargetException", e2.getTargetException());
        } catch (NoSuchMethodException e3) {
            if (class$org$apache$commons$logging$LogFactory == null) {
                cls = class$(FACTORY_PROPERTY);
                class$org$apache$commons$logging$LogFactory = cls;
            } else {
                cls = class$org$apache$commons$logging$LogFactory;
            }
            return getClassLoader(cls);
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private static LogFactory getCachedFactory(ClassLoader contextClassLoader) {
        if (contextClassLoader == null) {
            return nullClassLoaderFactory;
        }
        return (LogFactory) factories.get(contextClassLoader);
    }

    private static void cacheFactory(ClassLoader classLoader, LogFactory factory) {
        if (factory == null) {
            return;
        }
        if (classLoader == null) {
            nullClassLoaderFactory = factory;
        } else {
            factories.put(classLoader, factory);
        }
    }

    protected static LogFactory newFactory(String factoryClass, ClassLoader classLoader, ClassLoader contextClassLoader) throws LogConfigurationException {
        Object result = AccessController.doPrivileged(new PrivilegedAction(factoryClass, classLoader) {
            private final ClassLoader val$classLoader;
            private final String val$factoryClass;

            {
                this.val$factoryClass = val$factoryClass;
                this.val$classLoader = val$classLoader;
            }

            public Object run() {
                return LogFactory.createFactory(this.val$factoryClass, this.val$classLoader);
            }
        });
        if (result instanceof LogConfigurationException) {
            LogConfigurationException ex = (LogConfigurationException) result;
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("An error occurred while loading the factory class:").append(ex.getMessage()).toString());
            }
            throw ex;
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Created object ").append(objectId(result)).append(" to manage classloader ").append(objectId(contextClassLoader)).toString());
        }
        return (LogFactory) result;
    }

    protected static LogFactory newFactory(String factoryClass, ClassLoader classLoader) {
        return newFactory(factoryClass, classLoader, (ClassLoader) null);
    }

    protected static Object createFactory(String factoryClass, ClassLoader classLoader) {
        Class cls;
        String msg;
        Class cls2;
        Class cls3;
        Class cls4;
        Class logFactoryClass = null;
        if (classLoader != null) {
            try {
                logFactoryClass = classLoader.loadClass(factoryClass);
                if (class$org$apache$commons$logging$LogFactory == null) {
                    cls3 = class$(FACTORY_PROPERTY);
                    class$org$apache$commons$logging$LogFactory = cls3;
                } else {
                    cls3 = class$org$apache$commons$logging$LogFactory;
                }
                if (cls3.isAssignableFrom(logFactoryClass)) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(new StringBuffer().append("Loaded class ").append(logFactoryClass.getName()).append(" from classloader ").append(objectId(classLoader)).toString());
                    }
                } else if (isDiagnosticsEnabled()) {
                    StringBuffer append = new StringBuffer().append("Factory class ").append(logFactoryClass.getName()).append(" loaded from classloader ").append(objectId(logFactoryClass.getClassLoader())).append(" does not extend '");
                    if (class$org$apache$commons$logging$LogFactory == null) {
                        cls4 = class$(FACTORY_PROPERTY);
                        class$org$apache$commons$logging$LogFactory = cls4;
                    } else {
                        cls4 = class$org$apache$commons$logging$LogFactory;
                    }
                    logDiagnostic(append.append(cls4.getName()).append("' as loaded by this classloader.").toString());
                    logHierarchy("[BAD CL TREE] ", classLoader);
                }
                return (LogFactory) logFactoryClass.newInstance();
            } catch (ClassNotFoundException ex) {
                if (classLoader == thisClassLoader) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(new StringBuffer().append("Unable to locate any class called '").append(factoryClass).append("' via classloader ").append(objectId(classLoader)).toString());
                    }
                    throw ex;
                }
            } catch (NoClassDefFoundError e) {
                if (classLoader == thisClassLoader) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(new StringBuffer().append("Class '").append(factoryClass).append("' cannot be loaded").append(" via classloader ").append(objectId(classLoader)).append(" - it depends on some other class that cannot").append(" be found.").toString());
                    }
                    throw e;
                }
            } catch (ClassCastException e2) {
                if (classLoader == thisClassLoader) {
                    boolean implementsLogFactory = implementsLogFactory(logFactoryClass);
                    StringBuffer append2 = new StringBuffer().append("The application has specified that a custom LogFactory implementation should be used but Class '").append(factoryClass).append("' cannot be converted to '");
                    if (class$org$apache$commons$logging$LogFactory == null) {
                        cls = class$(FACTORY_PROPERTY);
                        class$org$apache$commons$logging$LogFactory = cls;
                    } else {
                        cls = class$org$apache$commons$logging$LogFactory;
                    }
                    String msg2 = append2.append(cls.getName()).append("'. ").toString();
                    if (implementsLogFactory) {
                        msg = new StringBuffer().append(msg2).append("The conflict is caused by the presence of multiple LogFactory classes in incompatible classloaders. ").append("Background can be found in http://commons.apache.org/logging/tech.html. ").append("If you have not explicitly specified a custom LogFactory then it is likely that ").append("the container has set one without your knowledge. ").append("In this case, consider using the commons-logging-adapters.jar file or ").append("specifying the standard LogFactory from the command line. ").toString();
                    } else {
                        msg = new StringBuffer().append(msg2).append("Please check the custom implementation. ").toString();
                    }
                    String msg3 = new StringBuffer().append(msg).append("Help can be found @http://commons.apache.org/logging/troubleshooting.html.").toString();
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(msg3);
                    }
                    throw new ClassCastException(msg3);
                }
            } catch (Exception e3) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("Unable to create LogFactory instance.");
                }
                if (logFactoryClass != null) {
                    if (class$org$apache$commons$logging$LogFactory == null) {
                        cls2 = class$(FACTORY_PROPERTY);
                        class$org$apache$commons$logging$LogFactory = cls2;
                    } else {
                        cls2 = class$org$apache$commons$logging$LogFactory;
                    }
                    if (!cls2.isAssignableFrom(logFactoryClass)) {
                        return new LogConfigurationException("The chosen LogFactory implementation does not extend LogFactory. Please check your configuration.", e3);
                    }
                }
                return new LogConfigurationException((Throwable) e3);
            }
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Unable to load factory class via classloader ").append(objectId(classLoader)).append(" - trying the classloader associated with this LogFactory.").toString());
        }
        return (LogFactory) Class.forName(factoryClass).newInstance();
    }

    private static boolean implementsLogFactory(Class logFactoryClass) {
        boolean implementsLogFactory = false;
        if (logFactoryClass != null) {
            try {
                ClassLoader logFactoryClassLoader = logFactoryClass.getClassLoader();
                if (logFactoryClassLoader == null) {
                    logDiagnostic("[CUSTOM LOG FACTORY] was loaded by the boot classloader");
                } else {
                    logHierarchy("[CUSTOM LOG FACTORY] ", logFactoryClassLoader);
                    implementsLogFactory = Class.forName(FACTORY_PROPERTY, false, logFactoryClassLoader).isAssignableFrom(logFactoryClass);
                    if (implementsLogFactory) {
                        logDiagnostic(new StringBuffer().append("[CUSTOM LOG FACTORY] ").append(logFactoryClass.getName()).append(" implements LogFactory but was loaded by an incompatible classloader.").toString());
                    } else {
                        logDiagnostic(new StringBuffer().append("[CUSTOM LOG FACTORY] ").append(logFactoryClass.getName()).append(" does not implement LogFactory.").toString());
                    }
                }
            } catch (SecurityException e) {
                logDiagnostic(new StringBuffer().append("[CUSTOM LOG FACTORY] SecurityException thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: ").append(e.getMessage()).toString());
            } catch (LinkageError e2) {
                logDiagnostic(new StringBuffer().append("[CUSTOM LOG FACTORY] LinkageError thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: ").append(e2.getMessage()).toString());
            } catch (ClassNotFoundException e3) {
                logDiagnostic("[CUSTOM LOG FACTORY] LogFactory class cannot be loaded by classloader which loaded the custom LogFactory implementation. Is the custom factory in the right classloader?");
            }
        }
        return implementsLogFactory;
    }

    private static InputStream getResourceAsStream(ClassLoader loader, String name) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction(loader, name) {
            private final ClassLoader val$loader;
            private final String val$name;

            {
                this.val$loader = val$loader;
                this.val$name = val$name;
            }

            public Object run() {
                if (this.val$loader != null) {
                    return this.val$loader.getResourceAsStream(this.val$name);
                }
                return ClassLoader.getSystemResourceAsStream(this.val$name);
            }
        });
    }

    private static Enumeration getResources(ClassLoader loader, String name) {
        return (Enumeration) AccessController.doPrivileged(new PrivilegedAction(loader, name) {
            private final ClassLoader val$loader;
            private final String val$name;

            {
                this.val$loader = val$loader;
                this.val$name = val$name;
            }

            public Object run() {
                try {
                    if (this.val$loader != null) {
                        return this.val$loader.getResources(this.val$name);
                    }
                    return ClassLoader.getSystemResources(this.val$name);
                } catch (IOException e) {
                    if (!LogFactory.isDiagnosticsEnabled()) {
                        return null;
                    }
                    LogFactory.access$000(new StringBuffer().append("Exception while trying to find configuration file ").append(this.val$name).append(":").append(e.getMessage()).toString());
                    return null;
                } catch (NoSuchMethodError e2) {
                    return null;
                }
            }
        });
    }

    private static Properties getProperties(URL url) {
        return (Properties) AccessController.doPrivileged(new PrivilegedAction(url) {
            private final URL val$url;

            {
                this.val$url = val$url;
            }

            public Object run() {
                try {
                    InputStream stream = this.val$url.openStream();
                    if (stream != null) {
                        Properties props = new Properties();
                        props.load(stream);
                        stream.close();
                        return props;
                    }
                } catch (IOException e) {
                    if (LogFactory.isDiagnosticsEnabled()) {
                        LogFactory.access$000(new StringBuffer().append("Unable to read URL ").append(this.val$url).toString());
                    }
                }
                return null;
            }
        });
    }

    private static final Properties getConfigurationFile(ClassLoader classLoader, String fileName) {
        Properties props = null;
        double priority = 0.0d;
        URL propsUrl = null;
        try {
            Enumeration urls = getResources(classLoader, fileName);
            if (urls == null) {
                return null;
            }
            while (urls.hasMoreElements()) {
                URL url = (URL) urls.nextElement();
                Properties newProps = getProperties(url);
                if (newProps != null) {
                    if (props == null) {
                        propsUrl = url;
                        props = newProps;
                        String priorityStr = props.getProperty(PRIORITY_KEY);
                        priority = 0.0d;
                        if (priorityStr != null) {
                            priority = Double.parseDouble(priorityStr);
                        }
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file found at '").append(url).append("'").append(" with priority ").append(priority).toString());
                        }
                    } else {
                        String newPriorityStr = newProps.getProperty(PRIORITY_KEY);
                        double newPriority = 0.0d;
                        if (newPriorityStr != null) {
                            newPriority = Double.parseDouble(newPriorityStr);
                        }
                        if (newPriority > priority) {
                            if (isDiagnosticsEnabled()) {
                                logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file at '").append(url).append("'").append(" with priority ").append(newPriority).append(" overrides file at '").append(propsUrl).append("'").append(" with priority ").append(priority).toString());
                            }
                            propsUrl = url;
                            props = newProps;
                            priority = newPriority;
                        } else if (isDiagnosticsEnabled()) {
                            logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file at '").append(url).append("'").append(" with priority ").append(newPriority).append(" does not override file at '").append(propsUrl).append("'").append(" with priority ").append(priority).toString());
                        }
                    }
                }
            }
            if (isDiagnosticsEnabled()) {
                if (props == null) {
                    logDiagnostic(new StringBuffer().append("[LOOKUP] No properties file of name '").append(fileName).append("' found.").toString());
                } else {
                    logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file of name '").append(fileName).append("' found at '").append(propsUrl).append('\"').toString());
                }
            }
            return props;
        } catch (SecurityException e) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("SecurityException thrown while trying to find/read config files.");
            }
        }
    }

    private static String getSystemProperty(String key, String def) throws SecurityException {
        return (String) AccessController.doPrivileged(new PrivilegedAction(key, def) {
            private final String val$def;
            private final String val$key;

            {
                this.val$key = val$key;
                this.val$def = val$def;
            }

            public Object run() {
                return System.getProperty(this.val$key, this.val$def);
            }
        });
    }

    private static void initDiagnostics() {
        String classLoaderName;
        try {
            String dest = getSystemProperty(DIAGNOSTICS_DEST_PROPERTY, (String) null);
            if (dest != null) {
                if (dest.equals("STDOUT")) {
                    diagnosticsStream = System.out;
                } else if (dest.equals("STDERR")) {
                    diagnosticsStream = System.err;
                } else {
                    try {
                        diagnosticsStream = new PrintStream(new FileOutputStream(dest, true));
                    } catch (IOException e) {
                        return;
                    }
                }
                try {
                    ClassLoader classLoader = thisClassLoader;
                    if (thisClassLoader == null) {
                        classLoaderName = "BOOTLOADER";
                    } else {
                        classLoaderName = objectId(classLoader);
                    }
                } catch (SecurityException e2) {
                    classLoaderName = "UNKNOWN";
                }
                diagnosticPrefix = new StringBuffer().append("[LogFactory from ").append(classLoaderName).append("] ").toString();
            }
        } catch (SecurityException e3) {
        }
    }

    protected static boolean isDiagnosticsEnabled() {
        return diagnosticsStream != null;
    }

    private static final void logDiagnostic(String msg) {
        if (diagnosticsStream != null) {
            diagnosticsStream.print(diagnosticPrefix);
            diagnosticsStream.println(msg);
            diagnosticsStream.flush();
        }
    }

    protected static final void logRawDiagnostic(String msg) {
        if (diagnosticsStream != null) {
            diagnosticsStream.println(msg);
            diagnosticsStream.flush();
        }
    }

    private static void logClassLoaderEnvironment(Class clazz) {
        if (isDiagnosticsEnabled()) {
            try {
                logDiagnostic(new StringBuffer().append("[ENV] Extension directories (java.ext.dir): ").append(System.getProperty("java.ext.dir")).toString());
                logDiagnostic(new StringBuffer().append("[ENV] Application classpath (java.class.path): ").append(System.getProperty("java.class.path")).toString());
            } catch (SecurityException e) {
                logDiagnostic("[ENV] Security setting prevent interrogation of system classpaths.");
            }
            String className = clazz.getName();
            try {
                ClassLoader classLoader = getClassLoader(clazz);
                logDiagnostic(new StringBuffer().append("[ENV] Class ").append(className).append(" was loaded via classloader ").append(objectId(classLoader)).toString());
                logHierarchy(new StringBuffer().append("[ENV] Ancestry of classloader which loaded ").append(className).append(" is ").toString(), classLoader);
            } catch (SecurityException e2) {
                logDiagnostic(new StringBuffer().append("[ENV] Security forbids determining the classloader for ").append(className).toString());
            }
        }
    }

    private static void logHierarchy(String prefix, ClassLoader classLoader) {
        if (isDiagnosticsEnabled()) {
            if (classLoader != null) {
                logDiagnostic(new StringBuffer().append(prefix).append(objectId(classLoader)).append(" == '").append(classLoader.toString()).append("'").toString());
            }
            try {
                ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                if (classLoader != null) {
                    StringBuffer buf = new StringBuffer(new StringBuffer().append(prefix).append("ClassLoader tree:").toString());
                    do {
                        buf.append(objectId(classLoader));
                        if (classLoader == systemClassLoader) {
                            buf.append(" (SYSTEM) ");
                        }
                        try {
                            classLoader = classLoader.getParent();
                            buf.append(" --> ");
                        } catch (SecurityException e) {
                            buf.append(" --> SECRET");
                        }
                    } while (classLoader != null);
                    buf.append("BOOT");
                    logDiagnostic(buf.toString());
                }
            } catch (SecurityException e2) {
                logDiagnostic(new StringBuffer().append(prefix).append("Security forbids determining the system classloader.").toString());
            }
        }
    }

    public static String objectId(Object o) {
        if (o == null) {
            return "null";
        }
        return new StringBuffer().append(o.getClass().getName()).append(GetNamedPart.CAST_METHOD_NAME).append(System.identityHashCode(o)).toString();
    }
}
