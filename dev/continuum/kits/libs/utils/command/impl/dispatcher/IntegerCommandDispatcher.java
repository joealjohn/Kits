/*    */ package dev.continuum.kits.libs.utils.command.impl.dispatcher;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.command.CommandResult;
/*    */ import dev.continuum.kits.libs.utils.command.impl.CommandResultWrapper;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface IntegerCommandDispatcher
/*    */   extends CommandDispatcher
/*    */ {
/*    */   int runInt(@NotNull CommandContext paramCommandContext);
/*    */   
/*    */   @NotNull
/*    */   default CommandResult run(@NotNull CommandContext context) {
/* 18 */     return CommandResultWrapper.wrap(runInt(context));
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\impl\dispatcher\IntegerCommandDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */