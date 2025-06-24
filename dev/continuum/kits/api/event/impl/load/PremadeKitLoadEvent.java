/*   */ package dev.continuum.kits.api.event.impl.load;
/*   */ 
/*   */ import dev.continuum.kits.api.event.CancellableEvent;
/*   */ import org.bukkit.entity.Player;
/*   */ import org.jetbrains.annotations.NotNull;
/*   */ 
/*   */ public class PremadeKitLoadEvent extends CancellableEvent {
/*   */   public PremadeKitLoadEvent(@NotNull Player player) {
/* 9 */     super(player);
/*   */   }
/*   */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\api\event\impl\load\PremadeKitLoadEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */