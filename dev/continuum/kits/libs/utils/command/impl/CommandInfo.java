/*     */ package dev.continuum.kits.libs.utils.command.impl;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ public class CommandInfo
/*     */ {
/*     */   @NotNull
/*     */   final Commands builder;
/*     */   @Nullable
/*     */   private String namespace;
/*     */   @Nullable
/*     */   private String description;
/*     */   @Nullable
/*     */   private String permission;
/*     */   @Nullable
/*     */   private Component permissionMessage;
/*     */   @Nullable
/*     */   private String usage;
/*     */   @Nullable
/*     */   private List<String> aliases;
/*     */   
/*     */   public CommandInfo(@NotNull Commands builder) {
/*  28 */     this.builder = builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String namespace() {
/*  37 */     return this.namespace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandInfo namespace(String namespace) {
/*  47 */     this.namespace = namespace;
/*  48 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String description() {
/*  57 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandInfo description(String description) {
/*  67 */     this.description = description;
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String permission() {
/*  77 */     return this.permission;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandInfo permission(String permission) {
/*  87 */     this.permission = permission;
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandInfo permission(String prefix, String suffix) {
/*  99 */     this.permission = prefix + "." + prefix;
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Component permissionMessage() {
/* 109 */     return this.permissionMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandInfo permissionMessage(Component permissionMessage) {
/* 119 */     this.permissionMessage = permissionMessage;
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String usage() {
/* 129 */     return this.usage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandInfo usage(String usage) {
/* 139 */     this.usage = usage;
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> aliases() {
/* 149 */     return this.aliases;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandInfo aliases(List<String> aliases) {
/* 159 */     this.aliases = aliases;
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandInfo aliases(String... aliases) {
/* 170 */     this.aliases = Arrays.asList(aliases);
/* 171 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\impl\CommandInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */