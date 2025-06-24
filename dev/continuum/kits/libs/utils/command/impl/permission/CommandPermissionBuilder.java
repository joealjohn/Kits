/*     */ package dev.continuum.kits.libs.utils.command.impl.permission;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.command.impl.Commands;
/*     */ import net.kyori.adventure.text.Component;
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
/*     */ public class CommandPermissionBuilder
/*     */ {
/*     */   private final Commands commandBuilder;
/*     */   private CommandPermission type;
/*     */   private String custom;
/*     */   private Component message;
/*     */   
/*     */   public CommandPermissionBuilder(@NotNull Commands commandBuilder) {
/*  23 */     this.commandBuilder = commandBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public CommandPermissionBuilder type(@NotNull CommandPermission type) {
/*  33 */     this.type = type;
/*  34 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public CommandPermissionBuilder message(@Nullable Component message) {
/*  44 */     this.message = message;
/*  45 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public CommandPermissionBuilder custom(@NotNull String custom) {
/*  55 */     this.custom = custom;
/*  56 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Commands commandBuilder() {
/*  65 */     return this.commandBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CommandPermission type() {
/*  74 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String custom() {
/*  83 */     return this.custom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Component message() {
/*  92 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public CommandPermissionBuilder custom(@NotNull String prefix, @NotNull String suffix) {
/* 103 */     this.custom = prefix + "." + prefix;
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Commands build() {
/* 113 */     if (type() == CommandPermission.OP || 
/* 114 */       type() == CommandPermission.OPERATOR || 
/* 115 */       type() == CommandPermission.SERVER_OPERATOR || 
/* 116 */       type() == CommandPermission.SERVER_OP) {
/*     */       
/* 118 */       commandBuilder().bukkitCommand().setPermission("minecraft.admin");
/* 119 */       return this.commandBuilder;
/*     */     } 
/*     */     
/* 122 */     commandBuilder().bukkitCommand().setPermission(custom());
/*     */     
/* 124 */     if (message() != null) {
/* 125 */       commandBuilder().bukkitCommand().permissionMessage(message());
/*     */     }
/*     */     
/* 128 */     return this.commandBuilder;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\impl\permission\CommandPermissionBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */