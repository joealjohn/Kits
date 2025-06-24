/*    */ package dev.continuum.kits.api.event.impl.save;
/*    */ 
/*    */ import dev.continuum.kits.api.event.CancellableEvent;
/*    */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class KitSaveEvent
/*    */   extends CancellableEvent {
/*    */   private final int kit;
/*    */   private final boolean copiedKit;
/*    */   @NotNull
/*    */   private final Cachable<Integer, ItemStack> contents;
/*    */   
/*    */   public KitSaveEvent(@NotNull Player player, int kit, boolean copiedKit, @NotNull Cachable<Integer, ItemStack> contents) {
/* 17 */     super(player);
/* 18 */     this.kit = kit;
/* 19 */     this.copiedKit = copiedKit;
/* 20 */     this.contents = contents;
/*    */   }
/*    */   
/*    */   public int kit() {
/* 24 */     return this.kit;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public Cachable<Integer, ItemStack> contents() {
/* 29 */     return this.contents;
/*    */   }
/*    */   
/*    */   public boolean copiedKit() {
/* 33 */     return this.copiedKit;
/*    */   }
/*    */   
/*    */   public boolean copied() {
/* 37 */     return this.copiedKit;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\api\event\impl\save\KitSaveEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */