/*     */ package dev.continuum.kits.libs.utils.command.impl.requirements;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.command.impl.dispatcher.CommandContext;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Requirements
/*     */ {
/*     */   @NotNull
/*     */   public static Predicate<CommandContext> playerOnly() {
/*  21 */     return ctx -> !ctx.senderInstanceOfPlayer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Predicate<CommandContext> playerOnly(@NotNull Component message) {
/*  31 */     return playerOnly(ctx -> ctx.sender().sendMessage(message));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Predicate<CommandContext> playerOnly(@NotNull Consumer<CommandContext> executeIfNotMet) {
/*  41 */     return ctx -> {
/*     */         if (!ctx.senderInstanceOfPlayer()) {
/*     */           executeIfNotMet.accept(ctx);
/*     */           return true;
/*     */         } 
/*     */         return false;
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Predicate<CommandContext> playerOnly(@NotNull Runnable executeIfNotMet) {
/*  58 */     return playerOnly(ctx -> executeIfNotMet.run());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Predicate<CommandContext> argSize(int required) {
/*  68 */     return ctx -> (ctx.size() != required);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Predicate<CommandContext> argSize(int required, @NotNull Component message) {
/*  79 */     return argSize(required, ctx -> ctx.sender().sendMessage(message));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Predicate<CommandContext> argSize(int required, @NotNull Consumer<CommandContext> executeIfNotMet) {
/*  90 */     return ctx -> {
/*     */         if (ctx.size() != required) {
/*     */           executeIfNotMet.accept(ctx);
/*     */           return true;
/*     */         } 
/*     */         return false;
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Predicate<CommandContext> argSize(int required, @NotNull Runnable executeIfNotMet) {
/* 108 */     return argSize(required, ctx -> executeIfNotMet.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\impl\requirements\Requirements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */