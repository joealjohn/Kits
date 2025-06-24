/*    */ package dev.continuum.kits.libs.utils.command.impl;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.registration.Registrar;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CommandsRegistrar
/*    */ {
/*    */   @NotNull
/*    */   private final Commands commandBuilder;
/*    */   
/*    */   public CommandsRegistrar(@NotNull Commands commandBuilder) {
/* 17 */     this.commandBuilder = commandBuilder;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void register() {
/* 25 */     Registrar.commandMap(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public Commands commandBuilder() {
/* 33 */     return this.commandBuilder;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\impl\CommandsRegistrar.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */