/*    */ package dev.continuum.kits.api.event.impl.edit;
/*    */ 
/*    */ import dev.continuum.kits.api.event.CancellableEvent;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class EnderChestEditEvent extends CancellableEvent {
/*    */   private final int enderChest;
/*    */   
/*    */   public EnderChestEditEvent(@NotNull Player player, int enderChest) {
/* 11 */     super(player);
/* 12 */     this.enderChest = enderChest;
/*    */   }
/*    */   
/*    */   public int enderChest() {
/* 16 */     return this.enderChest;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\api\event\impl\edit\EnderChestEditEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */