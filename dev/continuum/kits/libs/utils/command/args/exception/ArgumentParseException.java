/*    */ package dev.continuum.kits.libs.utils.command.args.exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArgumentParseException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final ArgumentExType type;
/*    */   
/*    */   public ArgumentParseException(String message, ArgumentExType type) {
/* 16 */     super(message);
/* 17 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ArgumentExType type() {
/* 26 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\args\exception\ArgumentParseException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */