/*    */ package dev.continuum.kits.api.event.impl.edit;
/*    */ 
/*    */ import dev.continuum.kits.api.event.CancellableEvent;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class KitEditEvent extends CancellableEvent {
/*    */   private final int kit;
/*    */   
/*    */   public KitEditEvent(@NotNull Player player, int kit) {
/* 11 */     super(player);
/* 12 */     this.kit = kit;
/*    */   }
/*    */   
/*    */   public int kit() {
/* 16 */     return this.kit;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\api\event\impl\edit\KitEditEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */