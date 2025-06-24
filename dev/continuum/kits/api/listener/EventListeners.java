/*     */ package dev.continuum.kits.api.listener;
/*     */ 
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import dev.continuum.kits.api.event.AllowableEvent;
/*     */ import dev.continuum.kits.api.event.CancellableEvent;
/*     */ import dev.continuum.kits.api.event.Events;
/*     */ import dev.continuum.kits.api.event.KitsEvent;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.elements.impl.ElementsImpl;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Parameter;
/*     */ import java.util.function.Consumer;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventListeners
/*     */ {
/*  25 */   private static final Elements<Tuple<Class<? extends KitsEvent>, Consumer<? extends KitsEvent>>> listeners = (Elements<Tuple<Class<? extends KitsEvent>, Consumer<? extends KitsEvent>>>)new ElementsImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static EventListeners listeners() {
/*  33 */     return new EventListeners();
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
/*     */   public void register(@NotNull Class<?> clazz) {
/*  47 */     for (Method method : clazz.getDeclaredMethods()) {
/*  48 */       if (method.isAnnotationPresent((Class)Events.Subscribe.class)) {
/*  49 */         method.setAccessible(true);
/*  50 */         Parameter[] parameters = method.getParameters();
/*     */         
/*  52 */         if (parameters.length != 1) {
/*  53 */           throw new InvalidListenerException("A method annotated with @Events.Subscribe can only have 1 parameter (an object that extends KitsEvent)");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  58 */         Parameter parameter = parameters[0];
/*  59 */         Class<?> type = parameter.getType();
/*     */         
/*  61 */         if (!KitsEvent.class.isAssignableFrom(type)) {
/*  62 */           throw new InvalidListenerException("Found an invalid event. This method's parameter does not extend KitsEvent");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  67 */         if (!method.getReturnType().getSimpleName().equals(void.class.getSimpleName())) {
/*  68 */           throw new InvalidListenerException("You can only return void in a listener");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  73 */         listeners.element(new Tuple(type, event -> {
/*     */                 try {
/*     */                   try {
/*     */                     method.invoke(clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), new Object[] { event });
/*  77 */                   } catch (InstantiationException|NoSuchMethodException instantiationException) {}
/*  78 */                 } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/*     */                   throw new RuntimeException(e);
/*     */                 } 
/*     */               }));
/*     */       } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends KitsEvent> boolean fire(@NotNull E event) {
/* 102 */     for (Tuple<Class<? extends KitsEvent>, Consumer<? extends KitsEvent>> tuple : listeners) {
/* 103 */       Class<? extends KitsEvent> key = (Class<? extends KitsEvent>)tuple.key();
/* 104 */       Object val = tuple.val();
/*     */       
/* 106 */       if (key == null || val == null)
/*     */         continue; 
/* 108 */       if (key.getSimpleName().equalsIgnoreCase(event.getClass().getSimpleName())) {
/* 109 */         if (event instanceof CancellableEvent) { CancellableEvent cancellable = (CancellableEvent)event;
/* 110 */           ((Consumer<CancellableEvent>)val).accept(cancellable);
/* 111 */           if (cancellable.cancelled()) return false;  continue; }
/* 112 */          if (event instanceof AllowableEvent) { AllowableEvent allowable = (AllowableEvent)event;
/* 113 */           ((Consumer<AllowableEvent>)val).accept(allowable);
/* 114 */           if (!allowable.allowed()) return false;  continue; }
/*     */         
/* 116 */         ((Consumer<E>)val).accept(event);
/* 117 */         return true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 122 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\api\listener\EventListeners.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */