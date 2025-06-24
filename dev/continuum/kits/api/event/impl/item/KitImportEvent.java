/*    */ package dev.continuum.kits.api.event.impl.item;
/*    */ 
/*    */ import dev.continuum.kits.api.event.CancellableEvent;
/*    */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class KitImportEvent extends CancellableEvent {
/*    */   private final int kit;
/*    */   private final Cachable<Integer, ItemStack> imported;
/*    */   
/*    */   public KitImportEvent(@NotNull Player player, int kit, @NotNull Cachable<Integer, ItemStack> imported) {
/* 14 */     super(player);
/* 15 */     this.kit = kit;
/* 16 */     this.imported = imported;
/*    */   }
/*    */   
/*    */   public int kit() {
/* 20 */     return this.kit;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public Cachable<Integer, ItemStack> imported() {
/* 25 */     return this.imported;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\api\event\impl\item\KitImportEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */