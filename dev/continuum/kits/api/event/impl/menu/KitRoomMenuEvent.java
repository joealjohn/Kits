/*    */ package dev.continuum.kits.api.event.impl.menu;
/*    */ 
/*    */ import dev.continuum.kits.api.event.CancellableEvent;
/*    */ import dev.continuum.kits.menu.KitRoomMenu;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class KitRoomMenuEvent extends CancellableEvent {
/*    */   private final KitRoomMenu menu;
/*    */   
/*    */   public KitRoomMenuEvent(@NotNull Player player, @NotNull KitRoomMenu menu) {
/* 12 */     super(player);
/* 13 */     this.menu = menu;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public KitRoomMenu menu() {
/* 18 */     return this.menu;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\api\event\impl\menu\KitRoomMenuEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */