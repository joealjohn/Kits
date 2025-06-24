/*    */ package dev.continuum.kits.libs.utils.event.builder;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.library.Utils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventBuilder<T extends Event>
/*    */ {
/*    */   final List<Object> actionList;
/*    */   final Class<T> eventType;
/*    */   
/*    */   EventBuilder(@NotNull Class<T> eventType) {
/* 28 */     this.actionList = new ArrayList();
/* 29 */     this.eventType = eventType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public EventBuilder<T> execute(@NotNull Consumer<T> consumer) {
/* 39 */     this.actionList.add(consumer);
/* 40 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static <T extends Event> EventBuilder<T> event(@NotNull Class<T> eventType) {
/* 51 */     return new EventBuilder<>(eventType);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public EventHandler<T> build() {
/* 60 */     return new EventHandler<>(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public EventCallback<T> register(@NotNull JavaPlugin plugin) {
/* 70 */     return build().register(plugin);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public EventCallback<T> register() {
/* 80 */     return build().register(Utils.plugin());
/*    */   }
/*    */   
/*    */   public List<Object> actionList() {
/* 84 */     return this.actionList;
/*    */   }
/*    */   
/*    */   public Class<T> eventType() {
/* 88 */     return this.eventType;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\event\builder\EventBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */