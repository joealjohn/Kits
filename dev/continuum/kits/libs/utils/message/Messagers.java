/*    */ package dev.continuum.kits.libs.utils.message;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Messagers
/*    */ {
/*    */   @NotNull
/*    */   public static ActionBarMessager actionBar(@NotNull Player player) {
/* 17 */     return new ActionBarMessager(player);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static ChatMessager chat(@NotNull Player player) {
/* 27 */     return new ChatMessager(player);
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\message\Messagers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */