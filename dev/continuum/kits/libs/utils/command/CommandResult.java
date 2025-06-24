/*    */ package dev.continuum.kits.libs.utils.command;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandResult
/*    */ {
/*    */   private final int ordinal;
/*    */   private final String identifier;
/* 13 */   public static final CommandResult SUCCESS = result(0, "success");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final CommandResult FAILURE = result(1, "failure");
/*    */   
/*    */   private CommandResult(int ordinal, String identifier) {
/* 21 */     this.ordinal = ordinal;
/* 22 */     this.identifier = identifier;
/*    */   }
/*    */   
/*    */   public static CommandResult result(int ordinal, String identifier) {
/* 26 */     return new CommandResult(ordinal, identifier);
/*    */   }
/*    */   
/*    */   public static CommandResult success() {
/* 30 */     return SUCCESS;
/*    */   }
/*    */   
/*    */   public static CommandResult finish() {
/* 34 */     return success();
/*    */   }
/*    */   
/*    */   public static CommandResult done() {
/* 38 */     return finish();
/*    */   }
/*    */   
/*    */   public static CommandResult stop() {
/* 42 */     return done();
/*    */   }
/*    */   
/*    */   public static CommandResult failure() {
/* 46 */     return FAILURE;
/*    */   }
/*    */   
/*    */   public static CommandResult fail() {
/* 50 */     return failure();
/*    */   }
/*    */   
/*    */   public static CommandResult error() {
/* 54 */     return fail();
/*    */   }
/*    */   
/*    */   public static CommandResult usage() {
/* 58 */     return error();
/*    */   }
/*    */   
/*    */   public int ordinal() {
/* 62 */     return this.ordinal;
/*    */   }
/*    */   
/*    */   public String identifier() {
/* 66 */     return this.identifier;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\CommandResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */