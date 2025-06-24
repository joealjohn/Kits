/*     */ package dev.continuum.kits.libs.utils.library.event;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.event.builder.EventBuilder;
/*     */ import dev.continuum.kits.libs.utils.event.builder.EventCallback;
/*     */ import dev.continuum.kits.libs.utils.event.builder.EventHandler;
/*     */ import java.util.function.Consumer;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.HandlerList;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluginEvents
/*     */ {
/*     */   public static PluginEvents handler() {
/*  23 */     return new PluginEvents();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends org.bukkit.event.Event> void register(@NotNull Class<E> eventClazz, @NotNull Consumer<E> consumer) {
/*  34 */     register(eventClazz, consumer, EventPriority.NORMAL);
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
/*     */   public <E extends org.bukkit.event.Event> void register(@NotNull Class<E> eventClazz, @NotNull Consumer<E> consumer, @NotNull EventPriority priority) {
/*  48 */     register(eventClazz, consumer, priority, false);
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
/*     */   public <E extends org.bukkit.event.Event> void register(@NotNull Class<E> eventClazz, @NotNull Consumer<E> consumer, @NotNull EventPriority priority, boolean ignoreCancelled) {
/*  63 */     EventBuilder.event(eventClazz)
/*  64 */       .execute(consumer)
/*  65 */       .build()
/*  66 */       .ignoreCancelled(ignoreCancelled)
/*  67 */       .priority(priority)
/*  68 */       .register();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(@NotNull Listener listener) {
/*  77 */     HandlerList.unregisterAll(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(@NotNull EventBuilder<?> eventBuilder) {
/*  86 */     HandlerList.unregisterAll((Listener)eventBuilder.build().register());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(@NotNull EventHandler<?> eventHandler) {
/*  95 */     HandlerList.unregisterAll((Listener)eventHandler.register());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(@NotNull EventCallback<?> eventCallback) {
/* 104 */     HandlerList.unregisterAll((Listener)eventCallback);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\library\event\PluginEvents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */