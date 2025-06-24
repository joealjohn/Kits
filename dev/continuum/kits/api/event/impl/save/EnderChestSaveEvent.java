/*    */ package dev.continuum.kits.api.event.impl.save;
/*    */ 
/*    */ import dev.continuum.kits.api.event.CancellableEvent;
/*    */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class EnderChestSaveEvent
/*    */   extends CancellableEvent {
/*    */   private final int enderChest;
/*    */   @NotNull
/*    */   private final Cachable<Integer, ItemStack> contents;
/*    */   
/*    */   public EnderChestSaveEvent(@NotNull Player player, int enderChest, @NotNull Cachable<Integer, ItemStack> contents) {
/* 16 */     super(player);
/* 17 */     this.enderChest = enderChest;
/* 18 */     this.contents = contents;
/*    */   }
/*    */   
/*    */   public int enderChest() {
/* 22 */     return this.enderChest;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public Cachable<Integer, ItemStack> contents() {
/* 27 */     return this.contents;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\api\event\impl\save\EnderChestSaveEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */