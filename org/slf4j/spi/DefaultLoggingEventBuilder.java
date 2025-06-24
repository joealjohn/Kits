/*     */ package org.slf4j.spi;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.event.DefaultLoggingEvent;
/*     */ import org.slf4j.event.KeyValuePair;
/*     */ import org.slf4j.event.Level;
/*     */ import org.slf4j.event.LoggingEvent;
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
/*     */ public class DefaultLoggingEventBuilder
/*     */   implements LoggingEventBuilder, CallerBoundaryAware
/*     */ {
/*  44 */   static String DLEB_FQCN = DefaultLoggingEventBuilder.class.getName();
/*     */   
/*     */   protected DefaultLoggingEvent loggingEvent;
/*     */   protected Logger logger;
/*     */   
/*     */   public DefaultLoggingEventBuilder(Logger logger, Level level) {
/*  50 */     this.logger = logger;
/*  51 */     this.loggingEvent = new DefaultLoggingEvent(level, logger);
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
/*     */   public LoggingEventBuilder addMarker(Marker marker) {
/*  63 */     this.loggingEvent.addMarker(marker);
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder setCause(Throwable t) {
/*  69 */     this.loggingEvent.setThrowable(t);
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addArgument(Object p) {
/*  75 */     this.loggingEvent.addArgument(p);
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addArgument(Supplier<?> objectSupplier) {
/*  81 */     this.loggingEvent.addArgument(objectSupplier.get());
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCallerBoundary(String fqcn) {
/*  87 */     this.loggingEvent.setCallerBoundary(fqcn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log() {
/*  92 */     log((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder setMessage(String message) {
/*  97 */     this.loggingEvent.setMessage(message);
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public LoggingEventBuilder setMessage(Supplier<String> messageSupplier) {
/* 102 */     this.loggingEvent.setMessage(messageSupplier.get());
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message) {
/* 108 */     this.loggingEvent.setMessage(message);
/* 109 */     log((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object arg) {
/* 114 */     this.loggingEvent.setMessage(message);
/* 115 */     this.loggingEvent.addArgument(arg);
/* 116 */     log((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object arg0, Object arg1) {
/* 121 */     this.loggingEvent.setMessage(message);
/* 122 */     this.loggingEvent.addArgument(arg0);
/* 123 */     this.loggingEvent.addArgument(arg1);
/* 124 */     log((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object... args) {
/* 129 */     this.loggingEvent.setMessage(message);
/* 130 */     this.loggingEvent.addArguments(args);
/*     */     
/* 132 */     log((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(Supplier<String> messageSupplier) {
/* 137 */     if (messageSupplier == null) {
/* 138 */       log((String)null);
/*     */     } else {
/* 140 */       log(messageSupplier.get());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void log(LoggingEvent aLoggingEvent) {
/* 145 */     setCallerBoundary(DLEB_FQCN);
/* 146 */     if (this.logger instanceof LoggingEventAware) {
/* 147 */       ((LoggingEventAware)this.logger).log(aLoggingEvent);
/*     */     } else {
/* 149 */       logViaPublicSLF4JLoggerAPI(aLoggingEvent);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void logViaPublicSLF4JLoggerAPI(LoggingEvent aLoggingEvent) {
/* 154 */     Object[] argArray = aLoggingEvent.getArgumentArray();
/* 155 */     int argLen = (argArray == null) ? 0 : argArray.length;
/*     */     
/* 157 */     Throwable t = aLoggingEvent.getThrowable();
/* 158 */     int tLen = (t == null) ? 0 : 1;
/*     */     
/* 160 */     String msg = aLoggingEvent.getMessage();
/*     */     
/* 162 */     Object[] combinedArguments = new Object[argLen + tLen];
/*     */     
/* 164 */     if (argArray != null) {
/* 165 */       System.arraycopy(argArray, 0, combinedArguments, 0, argLen);
/*     */     }
/* 167 */     if (t != null) {
/* 168 */       combinedArguments[argLen] = t;
/*     */     }
/*     */     
/* 171 */     msg = mergeMarkersAndKeyValuePairs(aLoggingEvent, msg);
/*     */     
/* 173 */     switch (aLoggingEvent.getLevel()) {
/*     */       case TRACE:
/* 175 */         this.logger.trace(msg, combinedArguments);
/*     */         break;
/*     */       case DEBUG:
/* 178 */         this.logger.debug(msg, combinedArguments);
/*     */         break;
/*     */       case INFO:
/* 181 */         this.logger.info(msg, combinedArguments);
/*     */         break;
/*     */       case WARN:
/* 184 */         this.logger.warn(msg, combinedArguments);
/*     */         break;
/*     */       case ERROR:
/* 187 */         this.logger.error(msg, combinedArguments);
/*     */         break;
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
/*     */   private String mergeMarkersAndKeyValuePairs(LoggingEvent aLoggingEvent, String msg) {
/* 202 */     StringBuilder sb = null;
/*     */     
/* 204 */     if (aLoggingEvent.getMarkers() != null) {
/* 205 */       sb = new StringBuilder();
/* 206 */       for (Marker marker : aLoggingEvent.getMarkers()) {
/* 207 */         sb.append(marker);
/* 208 */         sb.append(' ');
/*     */       } 
/*     */     } 
/*     */     
/* 212 */     if (aLoggingEvent.getKeyValuePairs() != null) {
/* 213 */       if (sb == null) {
/* 214 */         sb = new StringBuilder();
/*     */       }
/* 216 */       for (KeyValuePair kvp : aLoggingEvent.getKeyValuePairs()) {
/* 217 */         sb.append(kvp.key);
/* 218 */         sb.append('=');
/* 219 */         sb.append(kvp.value);
/* 220 */         sb.append(' ');
/*     */       } 
/*     */     } 
/*     */     
/* 224 */     if (sb != null) {
/* 225 */       sb.append(msg);
/* 226 */       return sb.toString();
/*     */     } 
/* 228 */     return msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addKeyValue(String key, Object value) {
/* 236 */     this.loggingEvent.addKeyValue(key, value);
/* 237 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addKeyValue(String key, Supplier<Object> value) {
/* 242 */     this.loggingEvent.addKeyValue(key, value.get());
/* 243 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\org\slf4j\spi\DefaultLoggingEventBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */