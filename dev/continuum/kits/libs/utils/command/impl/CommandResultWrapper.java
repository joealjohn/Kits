/*    */ package dev.continuum.kits.libs.utils.command.impl;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.command.CommandResult;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandResultWrapper
/*    */ {
/*    */   public static boolean unwrap(@NotNull CommandResult result) {
/* 16 */     if (result.ordinal() > 1) {
/* 17 */       throw new IllegalStateException("Unexpected value: " + result.ordinal());
/*    */     }
/*    */     
/* 20 */     switch (result.ordinal()) { case 0:
/*    */       
/*    */       case 1:
/* 23 */        }  throw new IllegalStateException("Unexpected value: " + result.ordinal());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static CommandResult wrap(boolean result) {
/* 33 */     return result ? CommandResult.done() : CommandResult.fail();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int unwrap(@NotNull Class<Integer> clazz, @NotNull CommandResult result) {
/* 42 */     if (result.ordinal() > 1) {
/* 43 */       throw new IllegalStateException("Unexpected value: " + result.ordinal());
/*    */     }
/*    */     
/* 46 */     switch (result.ordinal()) { case 0:
/*    */       
/*    */       case 1:
/* 49 */        }  throw new IllegalStateException("Unexpected value: " + result.ordinal());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static CommandResult wrap(int result) {
/* 59 */     return (result == -1) ? CommandResult.done() : CommandResult.fail();
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\impl\CommandResultWrapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */