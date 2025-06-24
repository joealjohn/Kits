/*    */ package dev.continuum.kits.api.event.impl.menu;
/*    */ 
/*    */ import dev.continuum.kits.api.event.CancellableEvent;
/*    */ import dev.continuum.kits.menu.KitMainMenu;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class KitMenuEvent extends CancellableEvent {
/*    */   private final KitMainMenu menu;
/*    */   
/*    */   public KitMenuEvent(@NotNull Player player, @NotNull KitMainMenu menu) {
/* 12 */     super(player);
/* 13 */     this.menu = menu;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public KitMainMenu menu() {
/* 18 */     return this.menu;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\api\event\impl\menu\KitMenuEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */