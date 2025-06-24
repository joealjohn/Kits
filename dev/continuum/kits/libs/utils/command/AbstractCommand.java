/*    */ package dev.continuum.kits.libs.utils.command;
/*    */ 
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import dev.continuum.kits.libs.utils.command.impl.Commands;
/*    */ import dev.continuum.kits.libs.utils.command.impl.dispatcher.CommandContext;
/*    */ import dev.continuum.kits.libs.utils.command.impl.dispatcher.CommandDispatcher;
/*    */ import dev.continuum.kits.libs.utils.command.impl.dispatcher.SuggestionDispatcher;
/*    */ import dev.continuum.kits.libs.utils.command.impl.suggestions.Suggestions;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractCommand
/*    */   implements CommandDispatcher, SuggestionDispatcher
/*    */ {
/*    */   @NotNull
/*    */   public static <T extends AbstractCommand> T instance(@NotNull Class<T> clazz) {
/*    */     try {
/* 35 */       return (T)clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 36 */     } catch (InstantiationException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException e) {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 41 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   @NotNull
/*    */   public static <T extends AbstractCommand> T register(@NotNull Class<T> clazz) {
/* 54 */     instance(clazz).command().build().register();
/* 55 */     return instance(clazz);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public abstract Commands command();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public abstract CommandResult run(@NotNull CommandContext paramCommandContext);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Suggestions suggest(@NotNull CommandContext context) {
/* 75 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\AbstractCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */