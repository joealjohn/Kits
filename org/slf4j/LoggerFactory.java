/*     */ package org.slf4j;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import org.slf4j.event.LoggingEvent;
/*     */ import org.slf4j.event.SubstituteLoggingEvent;
/*     */ import org.slf4j.helpers.NOP_FallbackServiceProvider;
/*     */ import org.slf4j.helpers.SubstituteLogger;
/*     */ import org.slf4j.helpers.SubstituteServiceProvider;
/*     */ import org.slf4j.helpers.Util;
/*     */ import org.slf4j.spi.SLF4JServiceProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LoggerFactory
/*     */ {
/*     */   static final String CODES_PREFIX = "https://www.slf4j.org/codes.html";
/*     */   static final String NO_PROVIDERS_URL = "https://www.slf4j.org/codes.html#noProviders";
/*     */   static final String IGNORED_BINDINGS_URL = "https://www.slf4j.org/codes.html#ignoredBindings";
/*     */   static final String MULTIPLE_BINDINGS_URL = "https://www.slf4j.org/codes.html#multiple_bindings";
/*     */   static final String VERSION_MISMATCH = "https://www.slf4j.org/codes.html#version_mismatch";
/*     */   static final String SUBSTITUTE_LOGGER_URL = "https://www.slf4j.org/codes.html#substituteLogger";
/*     */   static final String LOGGER_NAME_MISMATCH_URL = "https://www.slf4j.org/codes.html#loggerNameMismatch";
/*     */   static final String REPLAY_URL = "https://www.slf4j.org/codes.html#replay";
/*     */   static final String UNSUCCESSFUL_INIT_URL = "https://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory in failed state. Original exception was thrown EARLIER. See also https://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   public static final String PROVIDER_PROPERTY_KEY = "slf4j.provider";
/*     */   static final int UNINITIALIZED = 0;
/*     */   static final int ONGOING_INITIALIZATION = 1;
/*     */   static final int FAILED_INITIALIZATION = 2;
/*     */   static final int SUCCESSFUL_INITIALIZATION = 3;
/*     */   static final int NOP_FALLBACK_INITIALIZATION = 4;
/*  98 */   static volatile int INITIALIZATION_STATE = 0;
/*  99 */   static final SubstituteServiceProvider SUBST_PROVIDER = new SubstituteServiceProvider();
/* 100 */   static final NOP_FallbackServiceProvider NOP_FALLBACK_SERVICE_PROVIDER = new NOP_FallbackServiceProvider();
/*     */   
/*     */   static final String DETECT_LOGGER_NAME_MISMATCH_PROPERTY = "slf4j.detectLoggerNameMismatch";
/*     */   
/*     */   static final String JAVA_VENDOR_PROPERTY = "java.vendor.url";
/*     */   
/* 106 */   static boolean DETECT_LOGGER_NAME_MISMATCH = Util.safeGetBooleanSystemProperty("slf4j.detectLoggerNameMismatch");
/*     */   
/*     */   static volatile SLF4JServiceProvider PROVIDER;
/*     */ 
/*     */   
/*     */   static List<SLF4JServiceProvider> findServiceProviders() {
/* 112 */     List<SLF4JServiceProvider> providerList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/* 116 */     ClassLoader classLoaderOfLoggerFactory = LoggerFactory.class.getClassLoader();
/*     */     
/* 118 */     SLF4JServiceProvider explicitProvider = loadExplicitlySpecified(classLoaderOfLoggerFactory);
/* 119 */     if (explicitProvider != null) {
/* 120 */       providerList.add(explicitProvider);
/* 121 */       return providerList;
/*     */     } 
/*     */ 
/*     */     
/* 125 */     ServiceLoader<SLF4JServiceProvider> serviceLoader = getServiceLoader(classLoaderOfLoggerFactory);
/*     */     
/* 127 */     Iterator<SLF4JServiceProvider> iterator = serviceLoader.iterator();
/* 128 */     while (iterator.hasNext()) {
/* 129 */       safelyInstantiate(providerList, iterator);
/*     */     }
/* 131 */     return providerList;
/*     */   }
/*     */   
/*     */   private static ServiceLoader<SLF4JServiceProvider> getServiceLoader(ClassLoader classLoaderOfLoggerFactory) {
/*     */     ServiceLoader<SLF4JServiceProvider> serviceLoader;
/* 136 */     SecurityManager securityManager = System.getSecurityManager();
/* 137 */     if (securityManager == null) {
/* 138 */       serviceLoader = ServiceLoader.load(SLF4JServiceProvider.class, classLoaderOfLoggerFactory);
/*     */     } else {
/* 140 */       PrivilegedAction<ServiceLoader<SLF4JServiceProvider>> action = () -> ServiceLoader.load(SLF4JServiceProvider.class, classLoaderOfLoggerFactory);
/* 141 */       serviceLoader = AccessController.<ServiceLoader<SLF4JServiceProvider>>doPrivileged(action);
/*     */     } 
/* 143 */     return serviceLoader;
/*     */   }
/*     */   
/*     */   private static void safelyInstantiate(List<SLF4JServiceProvider> providerList, Iterator<SLF4JServiceProvider> iterator) {
/*     */     try {
/* 148 */       SLF4JServiceProvider provider = iterator.next();
/* 149 */       providerList.add(provider);
/* 150 */     } catch (ServiceConfigurationError e) {
/* 151 */       Util.report("A SLF4J service provider failed to instantiate:\n" + e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 160 */   private static final String[] API_COMPATIBILITY_LIST = new String[] { "2.0" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void reset() {
/* 178 */     INITIALIZATION_STATE = 0;
/*     */   }
/*     */   
/*     */   private static final void performInitialization() {
/* 182 */     bind();
/* 183 */     if (INITIALIZATION_STATE == 3) {
/* 184 */       versionSanityCheck();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final void bind() {
/*     */     try {
/* 190 */       List<SLF4JServiceProvider> providersList = findServiceProviders();
/* 191 */       reportMultipleBindingAmbiguity(providersList);
/* 192 */       if (providersList != null && !providersList.isEmpty()) {
/* 193 */         PROVIDER = providersList.get(0);
/*     */         
/* 195 */         PROVIDER.initialize();
/* 196 */         INITIALIZATION_STATE = 3;
/* 197 */         reportActualBinding(providersList);
/*     */       } else {
/* 199 */         INITIALIZATION_STATE = 4;
/* 200 */         Util.report("No SLF4J providers were found.");
/* 201 */         Util.report("Defaulting to no-operation (NOP) logger implementation");
/* 202 */         Util.report("See https://www.slf4j.org/codes.html#noProviders for further details.");
/*     */         
/* 204 */         Set<URL> staticLoggerBinderPathSet = findPossibleStaticLoggerBinderPathSet();
/* 205 */         reportIgnoredStaticLoggerBinders(staticLoggerBinderPathSet);
/*     */       } 
/* 207 */       postBindCleanUp();
/* 208 */     } catch (Exception e) {
/* 209 */       failedBinding(e);
/* 210 */       throw new IllegalStateException("Unexpected initialization failure", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static SLF4JServiceProvider loadExplicitlySpecified(ClassLoader classLoader) {
/* 215 */     String explicitlySpecified = System.getProperty("slf4j.provider");
/* 216 */     if (null == explicitlySpecified || explicitlySpecified.isEmpty()) {
/* 217 */       return null;
/*     */     }
/*     */     try {
/* 220 */       String message = String.format("Attempting to load provider \"%s\" specified via \"%s\" system property", new Object[] { explicitlySpecified, "slf4j.provider" });
/* 221 */       Util.report(message);
/* 222 */       Class<?> clazz = classLoader.loadClass(explicitlySpecified);
/* 223 */       Constructor<?> constructor = clazz.getConstructor(new Class[0]);
/* 224 */       Object provider = constructor.newInstance(new Object[0]);
/* 225 */       return (SLF4JServiceProvider)provider;
/* 226 */     } catch (ClassNotFoundException|NoSuchMethodException|InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 227 */       String message = String.format("Failed to instantiate the specified SLF4JServiceProvider (%s)", new Object[] { explicitlySpecified });
/* 228 */       Util.report(message, e);
/* 229 */       return null;
/* 230 */     } catch (ClassCastException e) {
/* 231 */       String message = String.format("Specified SLF4JServiceProvider (%s) does not implement SLF4JServiceProvider interface", new Object[] { explicitlySpecified });
/* 232 */       Util.report(message, e);
/* 233 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void reportIgnoredStaticLoggerBinders(Set<URL> staticLoggerBinderPathSet) {
/* 238 */     if (staticLoggerBinderPathSet.isEmpty()) {
/*     */       return;
/*     */     }
/* 241 */     Util.report("Class path contains SLF4J bindings targeting slf4j-api versions 1.7.x or earlier.");
/*     */     
/* 243 */     for (URL path : staticLoggerBinderPathSet) {
/* 244 */       Util.report("Ignoring binding found at [" + path + "]");
/*     */     }
/* 246 */     Util.report("See https://www.slf4j.org/codes.html#ignoredBindings for an explanation.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Set<URL> findPossibleStaticLoggerBinderPathSet() {
/* 258 */     Set<URL> staticLoggerBinderPathSet = new LinkedHashSet<>(); try {
/*     */       Enumeration<URL> paths;
/* 260 */       ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
/*     */       
/* 262 */       if (loggerFactoryClassLoader == null) {
/* 263 */         paths = ClassLoader.getSystemResources("org/slf4j/impl/StaticLoggerBinder.class");
/*     */       } else {
/* 265 */         paths = loggerFactoryClassLoader.getResources("org/slf4j/impl/StaticLoggerBinder.class");
/*     */       } 
/* 267 */       while (paths.hasMoreElements()) {
/* 268 */         URL path = paths.nextElement();
/* 269 */         staticLoggerBinderPathSet.add(path);
/*     */       } 
/* 271 */     } catch (IOException ioe) {
/* 272 */       Util.report("Error getting resources from path", ioe);
/*     */     } 
/* 274 */     return staticLoggerBinderPathSet;
/*     */   }
/*     */   
/*     */   private static void postBindCleanUp() {
/* 278 */     fixSubstituteLoggers();
/* 279 */     replayEvents();
/*     */     
/* 281 */     SUBST_PROVIDER.getSubstituteLoggerFactory().clear();
/*     */   }
/*     */   
/*     */   private static void fixSubstituteLoggers() {
/* 285 */     synchronized (SUBST_PROVIDER) {
/* 286 */       SUBST_PROVIDER.getSubstituteLoggerFactory().postInitialization();
/* 287 */       for (SubstituteLogger substLogger : SUBST_PROVIDER.getSubstituteLoggerFactory().getLoggers()) {
/* 288 */         Logger logger = getLogger(substLogger.getName());
/* 289 */         substLogger.setDelegate(logger);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void failedBinding(Throwable t) {
/* 296 */     INITIALIZATION_STATE = 2;
/* 297 */     Util.report("Failed to instantiate SLF4J LoggerFactory", t);
/*     */   }
/*     */   
/*     */   private static void replayEvents() {
/* 301 */     LinkedBlockingQueue<SubstituteLoggingEvent> queue = SUBST_PROVIDER.getSubstituteLoggerFactory().getEventQueue();
/* 302 */     int queueSize = queue.size();
/* 303 */     int count = 0;
/* 304 */     int maxDrain = 128;
/* 305 */     List<SubstituteLoggingEvent> eventList = new ArrayList<>(128);
/*     */     while (true) {
/* 307 */       int numDrained = queue.drainTo(eventList, 128);
/* 308 */       if (numDrained == 0)
/*     */         break; 
/* 310 */       for (SubstituteLoggingEvent event : eventList) {
/* 311 */         replaySingleEvent(event);
/* 312 */         if (count++ == 0)
/* 313 */           emitReplayOrSubstituionWarning(event, queueSize); 
/*     */       } 
/* 315 */       eventList.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emitReplayOrSubstituionWarning(SubstituteLoggingEvent event, int queueSize) {
/* 320 */     if (event.getLogger().isDelegateEventAware()) {
/* 321 */       emitReplayWarning(queueSize);
/* 322 */     } else if (!event.getLogger().isDelegateNOP()) {
/*     */ 
/*     */       
/* 325 */       emitSubstitutionWarning();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void replaySingleEvent(SubstituteLoggingEvent event) {
/* 330 */     if (event == null) {
/*     */       return;
/*     */     }
/* 333 */     SubstituteLogger substLogger = event.getLogger();
/* 334 */     String loggerName = substLogger.getName();
/* 335 */     if (substLogger.isDelegateNull()) {
/* 336 */       throw new IllegalStateException("Delegate logger cannot be null at this state.");
/*     */     }
/*     */     
/* 339 */     if (!substLogger.isDelegateNOP())
/*     */     {
/* 341 */       if (substLogger.isDelegateEventAware()) {
/* 342 */         if (substLogger.isEnabledForLevel(event.getLevel())) {
/* 343 */           substLogger.log((LoggingEvent)event);
/*     */         }
/*     */       } else {
/* 346 */         Util.report(loggerName);
/*     */       }  } 
/*     */   }
/*     */   
/*     */   private static void emitSubstitutionWarning() {
/* 351 */     Util.report("The following set of substitute loggers may have been accessed");
/* 352 */     Util.report("during the initialization phase. Logging calls during this");
/* 353 */     Util.report("phase were not honored. However, subsequent logging calls to these");
/* 354 */     Util.report("loggers will work as normally expected.");
/* 355 */     Util.report("See also https://www.slf4j.org/codes.html#substituteLogger");
/*     */   }
/*     */   
/*     */   private static void emitReplayWarning(int eventCount) {
/* 359 */     Util.report("A number (" + eventCount + ") of logging calls during the initialization phase have been intercepted and are");
/* 360 */     Util.report("now being replayed. These are subject to the filtering rules of the underlying logging system.");
/* 361 */     Util.report("See also https://www.slf4j.org/codes.html#replay");
/*     */   }
/*     */   
/*     */   private static final void versionSanityCheck() {
/*     */     try {
/* 366 */       String requested = PROVIDER.getRequestedApiVersion();
/*     */       
/* 368 */       boolean match = false;
/* 369 */       for (String aAPI_COMPATIBILITY_LIST : API_COMPATIBILITY_LIST) {
/* 370 */         if (requested.startsWith(aAPI_COMPATIBILITY_LIST)) {
/* 371 */           match = true;
/*     */         }
/*     */       } 
/* 374 */       if (!match) {
/* 375 */         Util.report("The requested version " + requested + " by your slf4j provider is not compatible with " + 
/* 376 */             Arrays.<String>asList(API_COMPATIBILITY_LIST).toString());
/* 377 */         Util.report("See https://www.slf4j.org/codes.html#version_mismatch for further details.");
/*     */       } 
/* 379 */     } catch (NoSuchFieldError noSuchFieldError) {
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 384 */     catch (Throwable e) {
/*     */       
/* 386 */       Util.report("Unexpected problem occurred during version sanity check", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isAmbiguousProviderList(List<SLF4JServiceProvider> providerList) {
/* 391 */     return (providerList.size() > 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void reportMultipleBindingAmbiguity(List<SLF4JServiceProvider> providerList) {
/* 400 */     if (isAmbiguousProviderList(providerList)) {
/* 401 */       Util.report("Class path contains multiple SLF4J providers.");
/* 402 */       for (SLF4JServiceProvider provider : providerList) {
/* 403 */         Util.report("Found provider [" + provider + "]");
/*     */       }
/* 405 */       Util.report("See https://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void reportActualBinding(List<SLF4JServiceProvider> providerList) {
/* 411 */     if (!providerList.isEmpty() && isAmbiguousProviderList(providerList)) {
/* 412 */       Util.report("Actual provider is of type [" + providerList.get(0) + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(String name) {
/* 425 */     ILoggerFactory iLoggerFactory = getILoggerFactory();
/* 426 */     return iLoggerFactory.getLogger(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(Class<?> clazz) {
/* 451 */     Logger logger = getLogger(clazz.getName());
/* 452 */     if (DETECT_LOGGER_NAME_MISMATCH) {
/* 453 */       Class<?> autoComputedCallingClass = Util.getCallingClass();
/* 454 */       if (autoComputedCallingClass != null && nonMatchingClasses(clazz, autoComputedCallingClass)) {
/* 455 */         Util.report(String.format("Detected logger name mismatch. Given name: \"%s\"; computed name: \"%s\".", new Object[] { logger.getName(), autoComputedCallingClass
/* 456 */                 .getName() }));
/* 457 */         Util.report("See https://www.slf4j.org/codes.html#loggerNameMismatch for an explanation");
/*     */       } 
/*     */     } 
/* 460 */     return logger;
/*     */   }
/*     */   
/*     */   private static boolean nonMatchingClasses(Class<?> clazz, Class<?> autoComputedCallingClass) {
/* 464 */     return !autoComputedCallingClass.isAssignableFrom(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ILoggerFactory getILoggerFactory() {
/* 476 */     return getProvider().getLoggerFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static SLF4JServiceProvider getProvider() {
/* 486 */     if (INITIALIZATION_STATE == 0) {
/* 487 */       synchronized (LoggerFactory.class) {
/* 488 */         if (INITIALIZATION_STATE == 0) {
/* 489 */           INITIALIZATION_STATE = 1;
/* 490 */           performInitialization();
/*     */         } 
/*     */       } 
/*     */     }
/* 494 */     switch (INITIALIZATION_STATE) {
/*     */       case 3:
/* 496 */         return PROVIDER;
/*     */       case 4:
/* 498 */         return (SLF4JServiceProvider)NOP_FALLBACK_SERVICE_PROVIDER;
/*     */       case 2:
/* 500 */         throw new IllegalStateException("org.slf4j.LoggerFactory in failed state. Original exception was thrown EARLIER. See also https://www.slf4j.org/codes.html#unsuccessfulInit");
/*     */ 
/*     */       
/*     */       case 1:
/* 504 */         return (SLF4JServiceProvider)SUBST_PROVIDER;
/*     */     } 
/* 506 */     throw new IllegalStateException("Unreachable code");
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\org\slf4j\LoggerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */