/*     */ package dev.continuum.kits.libs.hikari.util;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class UtilityElf
/*     */ {
/*     */   public static String getNullIfEmpty(String text) {
/*  42 */     return (text == null) ? null : (text.trim().isEmpty() ? null : text.trim());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void quietlySleep(long millis) {
/*     */     try {
/*  53 */       Thread.sleep(millis);
/*     */     }
/*  55 */     catch (InterruptedException e) {
/*     */       
/*  57 */       Thread.currentThread().interrupt();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean safeIsAssignableFrom(Object obj, String className) {
/*     */     try {
/*  69 */       Class<?> clazz = Class.forName(className);
/*  70 */       return clazz.isAssignableFrom(obj.getClass());
/*  71 */     } catch (ClassNotFoundException ignored) {
/*  72 */       return false;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T createInstance(String className, Class<T> clazz, Object... args) {
/*  88 */     if (className == null) {
/*  89 */       return null;
/*     */     }
/*     */     
/*     */     try {
/*  93 */       Class<?> loaded = UtilityElf.class.getClassLoader().loadClass(className);
/*  94 */       if (args.length == 0) {
/*  95 */         return clazz.cast(loaded.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
/*     */       }
/*     */       
/*  98 */       Class<?>[] argClasses = new Class[args.length];
/*  99 */       for (int i = 0; i < args.length; i++) {
/* 100 */         argClasses[i] = args[i].getClass();
/*     */       }
/* 102 */       Constructor<?> constructor = loaded.getConstructor(argClasses);
/* 103 */       return clazz.cast(constructor.newInstance(args));
/*     */     }
/* 105 */     catch (Exception e) {
/* 106 */       throw new RuntimeException(e);
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
/*     */ 
/*     */   
/*     */   public static ThreadPoolExecutor createThreadPoolExecutor(int queueSize, String threadName, ThreadFactory threadFactory, RejectedExecutionHandler policy) {
/* 121 */     return createThreadPoolExecutor(new LinkedBlockingQueue<>(queueSize), threadName, threadFactory, policy);
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
/*     */   public static ThreadPoolExecutor createThreadPoolExecutor(BlockingQueue<Runnable> queue, String threadName, ThreadFactory threadFactory, RejectedExecutionHandler policy) {
/* 135 */     if (threadFactory == null) {
/* 136 */       threadFactory = new DefaultThreadFactory(threadName);
/*     */     }
/*     */     
/* 139 */     ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, queue, threadFactory, policy);
/* 140 */     executor.allowCoreThreadTimeOut(true);
/* 141 */     return executor;
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
/*     */   public static int getTransactionIsolation(String transactionIsolationName) {
/* 156 */     if (transactionIsolationName != null) {
/*     */       
/*     */       try {
/* 159 */         String upperCaseIsolationLevelName = transactionIsolationName.toUpperCase(Locale.ENGLISH);
/* 160 */         return IsolationLevel.valueOf(upperCaseIsolationLevelName).getLevelId();
/* 161 */       } catch (IllegalArgumentException e) {
/*     */         
/*     */         try {
/* 164 */           int level = Integer.parseInt(transactionIsolationName);
/* 165 */           for (IsolationLevel iso : IsolationLevel.values()) {
/* 166 */             if (iso.getLevelId() == level) {
/* 167 */               return iso.getLevelId();
/*     */             }
/*     */           } 
/*     */           
/* 171 */           throw new IllegalArgumentException("Invalid transaction isolation value: " + transactionIsolationName);
/*     */         }
/* 173 */         catch (NumberFormatException nfe) {
/* 174 */           throw new IllegalArgumentException("Invalid transaction isolation value: " + transactionIsolationName, nfe);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 179 */     return -1;
/*     */   }
/*     */   
/*     */   public static class CustomDiscardPolicy
/*     */     implements RejectedExecutionHandler
/*     */   {
/*     */     public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {}
/*     */   }
/*     */   
/*     */   public static final class DefaultThreadFactory
/*     */     implements ThreadFactory
/*     */   {
/*     */     private final String threadName;
/*     */     private final boolean daemon;
/*     */     
/*     */     public DefaultThreadFactory(String threadName) {
/* 195 */       this.threadName = threadName;
/* 196 */       this.daemon = true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Thread newThread(Runnable r) {
/* 202 */       Thread thread = new Thread(r, this.threadName);
/* 203 */       thread.setDaemon(this.daemon);
/* 204 */       return thread;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikar\\util\UtilityElf.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */