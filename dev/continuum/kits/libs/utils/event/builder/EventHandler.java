/*    */ package dev.continuum.kits.libs.utils.event.builder;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import dev.continuum.kits.libs.utils.library.Utils;
/*    */ import java.util.List;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.EventPriority;
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
/*    */ public class EventHandler<T extends Event>
/*    */ {
/*    */   final List<Object> actionList;
/*    */   final Class<T> eventType;
/*    */   EventPriority eventPriority;
/*    */   boolean ignoreCancelled;
/*    */   
/*    */   EventHandler(@NotNull EventBuilder<T> builder) {
/* 29 */     if (builder.actionList.isEmpty()) {
/* 30 */       throw new UnsupportedOperationException("No actions defined");
/*    */     }
/*    */     
/* 33 */     this.actionList = (List<Object>)ImmutableList.copyOf(builder.actionList);
/* 34 */     this.eventType = builder.eventType;
/* 35 */     this.eventPriority = EventPriority.NORMAL;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public EventCallback<T> register(@NotNull JavaPlugin plugin) {
/* 45 */     return new EventCallback<>(plugin, this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public EventCallback<T> register() {
/* 54 */     return new EventCallback<>(Utils.plugin(), this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public EventHandler<T> ignoreCancelled(boolean ignoreCancelled) {
/* 64 */     this.ignoreCancelled = ignoreCancelled;
/* 65 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public EventHandler<T> priority(@NotNull EventPriority eventPriority) {
/* 75 */     this.eventPriority = eventPriority;
/* 76 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public EventHandler<T> priority(int eventPriority) {
/* 86 */     switch (eventPriority) { case 1: case 10: case 100: case 1000: case 2: case 20: case 200: case 2000: case 3: case 30: case 300: case 3000: case 4: case 40: case 400: case 4000: case 5: case 50: case 500: case 5000: case 6: case 60: case 600: case 6000:  }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 94 */       this;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\event\builder\EventHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */