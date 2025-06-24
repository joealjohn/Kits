/*    */ package dev.continuum.kits.inventory;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.elements.Elements;
/*    */ import dev.continuum.kits.libs.utils.misc.ObjectUtils;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class EnderChestMapping {
/*  8 */   private static final Elements<Integer> playerSlots = (Elements<Integer>)Elements.of((Object[])new Integer[] { 
/*  9 */         Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), 
/* 10 */         Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26), 
/* 11 */         Integer.valueOf(27), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30), Integer.valueOf(31), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35) });
/*    */ 
/*    */   
/* 14 */   private static final Elements<Integer> menuSlots = (Elements<Integer>)Elements.of((Object[])new Integer[] { 
/* 15 */         Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), 
/* 16 */         Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), 
/* 17 */         Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26) });
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static Elements<Integer> playerSlots() {
/* 22 */     return playerSlots;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public static Elements<Integer> menuSlots() {
/* 27 */     return menuSlots;
/*    */   }
/*    */   
/*    */   public static int playerToMenuSlot(int playerSlot) {
/* 31 */     if (!playerSlots().has(Integer.valueOf(playerSlot))) throw new IllegalArgumentException("Invalid player slot: " + playerSlot);
/*    */     
/* 33 */     int index = playerSlots.at(Integer.valueOf(playerSlot));
/* 34 */     if (index == -1) return -1;
/*    */     
/* 36 */     return ((Integer)ObjectUtils.defaultIfNull(menuSlots.element(index), Integer.valueOf(-1))).intValue();
/*    */   }
/*    */   
/*    */   public static int menuToPlayerSlot(int menuSlot) {
/* 40 */     if (!menuSlots().has(Integer.valueOf(menuSlot))) throw new IllegalArgumentException("Invalid menu slot: " + menuSlot);
/*    */     
/* 42 */     int index = menuSlots.at(Integer.valueOf(menuSlot));
/* 43 */     if (index == -1) return -1;
/*    */     
/* 45 */     return ((Integer)ObjectUtils.defaultIfNull(playerSlots.element(index), Integer.valueOf(-1))).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\inventory\EnderChestMapping.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */