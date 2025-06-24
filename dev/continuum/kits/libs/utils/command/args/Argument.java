/*     */ package dev.continuum.kits.libs.utils.command.args;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.command.args.custom.CustomArgument;
/*     */ import dev.continuum.kits.libs.utils.command.args.exception.ArgumentExType;
/*     */ import dev.continuum.kits.libs.utils.command.impl.dispatcher.CommandContext;
/*     */ import dev.continuum.kits.libs.utils.command.impl.dispatcher.SuggestionDispatcher;
/*     */ import java.util.function.BiFunction;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Argument<A extends CustomArgument<?>>
/*     */ {
/*     */   private BiFunction<CommandContext, ArgumentExType, Boolean> onError;
/*     */   private A type;
/*     */   private final String identifier;
/*     */   private SuggestionDispatcher suggestions;
/*     */   private String defaultVal;
/*     */   
/*     */   public Argument(@NotNull String identifier) {
/*  30 */     this.identifier = identifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Argument(@NotNull String identifier, @NotNull A type) {
/*  40 */     this.identifier = identifier;
/*  41 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static <T extends CustomArgument<?>> Argument<T> of(@NotNull String identifier) {
/*  52 */     return new Argument<>(identifier);
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
/*     */   public static <T extends CustomArgument<?>> Argument<T> of(@NotNull String identifier, @NotNull T type) {
/*  64 */     return new Argument<>(identifier, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BiFunction<CommandContext, ArgumentExType, Boolean> onError() {
/*  73 */     return this.onError;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Argument<A> onError(@NotNull BiFunction<CommandContext, ArgumentExType, Boolean> onError) {
/*  83 */     this.onError = onError;
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SuggestionDispatcher suggestions() {
/*  93 */     return this.suggestions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Argument<A> suggestions(@NotNull SuggestionDispatcher suggestions) {
/* 103 */     this.suggestions = suggestions;
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public A type() {
/* 113 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Argument<A> type(@NotNull A type) {
/* 123 */     this.type = type;
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public String identifier() {
/* 133 */     return this.identifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Argument<A> defaultVal(@NotNull String defaultVal) {
/* 143 */     this.defaultVal = defaultVal;
/* 144 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String defaultVal() {
/* 153 */     return this.defaultVal;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\args\Argument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */