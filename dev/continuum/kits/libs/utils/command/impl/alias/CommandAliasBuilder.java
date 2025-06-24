/*    */ package dev.continuum.kits.libs.utils.command.impl.alias;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.command.impl.Commands;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandAliasBuilder
/*    */ {
/*    */   private final Commands commandBuilder;
/*    */   private final List<String> aliases;
/*    */   
/*    */   public CommandAliasBuilder(@NotNull Commands commandBuilder) {
/* 22 */     this.commandBuilder = commandBuilder;
/* 23 */     this.aliases = commandBuilder.bukkitCommand().getAliases().isEmpty() ? new ArrayList<>() : commandBuilder.bukkitCommand().getAliases();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public CommandAliasBuilder add(@NotNull String alias) {
/* 33 */     this.aliases.add(alias);
/* 34 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public List<String> aliases() {
/* 43 */     return this.aliases;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public CommandAliasBuilder aliases(@NotNull List<String> aliases) {
/* 53 */     this.aliases.clear();
/*    */     
/* 55 */     for (String alias : aliases) {
/* 56 */       aliases().add(alias);
/*    */     }
/*    */     
/* 59 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public Commands build() {
/* 68 */     if (!this.aliases.isEmpty()) {
/* 69 */       this.commandBuilder.bukkitCommand().setAliases(this.aliases);
/*    */     }
/*    */     
/* 72 */     return this.commandBuilder;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\impl\alias\CommandAliasBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */