/*     */ package dev.continuum.kits.libs.utils.command.impl.dispatcher;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.command.args.Argument;
/*     */ import dev.continuum.kits.libs.utils.command.args.custom.CustomArgument;
/*     */ import dev.continuum.kits.libs.utils.command.args.custom.CustomListArgument;
/*     */ import dev.continuum.kits.libs.utils.command.args.exception.ArgumentParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
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
/*     */ public class CommandContext
/*     */ {
/*     */   private final CommandSender sender;
/*     */   private final Command command;
/*     */   private final String name;
/*     */   private final List<String> rawArgs;
/*     */   private final List<Argument<?>> args;
/*     */   
/*     */   public CommandContext(@NotNull CommandSender sender, @NotNull Command command, @NotNull String name, @NotNull String[] rawArgs) {
/*  33 */     this.sender = sender;
/*  34 */     this.command = command;
/*  35 */     this.name = name;
/*  36 */     this.rawArgs = Arrays.<String>stream(rawArgs).toList();
/*  37 */     this.args = new ArrayList<>();
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
/*     */   public static CommandContext context(@NotNull CommandSender sender, @NotNull Command command, @NotNull String name, @NotNull String[] rawArgs) {
/*  49 */     return new CommandContext(sender, command, name, rawArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public CommandSender sender() {
/*  58 */     return this.sender;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Player player() {
/*  67 */     return (Player)this.sender;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean senderInstanceOfPlayer() {
/*  76 */     return sender() instanceof Player;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean senderIsPlayer() {
/*  85 */     return senderInstanceOfPlayer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Command command() {
/*  94 */     return this.command;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Command cmd() {
/* 103 */     return this.command;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public String name() {
/* 112 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public String alias() {
/* 121 */     return name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public String commandRan() {
/* 130 */     return alias();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public List<String> rawArgs() {
/* 139 */     return this.rawArgs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String rawArgAt(int index) {
/* 149 */     return this.rawArgs.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CustomArgument<?> argTypeAt(int index) {
/* 159 */     return ((Argument)this.args.get(index)).type();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CustomArgument<?> argTypeAt(@NotNull String identifier) {
/* 169 */     for (Argument<?> arg : this.args) {
/* 170 */       if (arg.identifier().equalsIgnoreCase(identifier)) {
/* 171 */         return ((Argument)this.args.get(this.args.indexOf(arg))).type();
/*     */       }
/*     */     } 
/*     */     
/* 175 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object argAt(@NotNull String identifier) {
/* 185 */     for (Argument<?> arg : this.args) {
/* 186 */       if (arg.identifier().equalsIgnoreCase(identifier)) {
/* 187 */         return argAt(this.args.indexOf(arg));
/*     */       }
/*     */     } 
/*     */     
/* 191 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object argAt(int index) {
/* 201 */     Argument<?> argument = this.args.get(index);
/*     */     
/* 203 */     if (argument == null || this.rawArgs == null || this.rawArgs.isEmpty()) return null;
/*     */     
/* 205 */     if (this.rawArgs.size() <= index || rawArgAt(index) == null) {
/*     */       try {
/* 207 */         CustomArgument customArgument = argument.type(); if (customArgument instanceof CustomListArgument) { CustomListArgument<?> arg = (CustomListArgument)customArgument; return arg.parse(this, 0); }
/* 208 */          if (argument.defaultVal() == null) return null;
/*     */         
/* 210 */         return argument.type().parse(this, argument.defaultVal());
/* 211 */       } catch (ArgumentParseException err) {
/* 212 */         if (argument.onError() != null) { argument.onError().apply(this, err.type()); }
/* 213 */         else { throw new RuntimeException(err.getMessage()); } 
/*     */       } 
/* 215 */     } else if (rawArgAt(index) != null) {
/*     */       try {
/* 217 */         CustomArgument customArgument = argument.type(); if (customArgument instanceof CustomListArgument) { CustomListArgument<?> arg = (CustomListArgument)customArgument; return arg.parse(this, index); }
/*     */         
/* 219 */         return argument.type().parse(this, rawArgAt(index));
/* 220 */       } catch (ArgumentParseException e) {
/* 221 */         if (argument.onError() != null) { argument.onError().apply(this, e.type()); }
/* 222 */         else { throw new RuntimeException(e.getMessage()); }
/*     */       
/*     */       } 
/*     */     } 
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T argAt(@NotNull String identifier, @NotNull Class<T> type) {
/* 238 */     for (Argument<?> arg : this.args) {
/* 239 */       if (arg.identifier().equalsIgnoreCase(identifier)) {
/* 240 */         return argAt(this.args.indexOf(arg), type);
/*     */       }
/*     */     } 
/*     */     
/* 244 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T argAt(int index, @NotNull Class<T> type) {
/* 257 */     Argument<?> argument = this.args.get(index);
/*     */     
/* 259 */     if (argument == null || this.rawArgs == null || this.rawArgs.isEmpty()) return null;
/*     */     
/* 261 */     if (this.rawArgs.size() <= index || rawArgAt(index) == null) {
/*     */       try {
/* 263 */         CustomArgument customArgument = argument.type(); if (customArgument instanceof CustomListArgument) { CustomListArgument<?> arg = (CustomListArgument)customArgument; return (T)arg.parse(this, 0); }
/* 264 */          if (argument.defaultVal() == null) return null;
/*     */         
/* 266 */         return type.cast(argument.type().parse(this, argument.defaultVal()));
/* 267 */       } catch (ArgumentParseException err) {
/* 268 */         if (argument.onError() != null) { argument.onError().apply(this, err.type()); }
/* 269 */         else { throw new RuntimeException(err.getMessage()); } 
/*     */       } 
/* 271 */     } else if (rawArgAt(index) != null) {
/*     */       try {
/* 273 */         CustomArgument customArgument = argument.type(); if (customArgument instanceof CustomListArgument) { CustomListArgument<?> arg = (CustomListArgument)customArgument; return (T)arg.parse(this, index); }
/*     */         
/* 275 */         return type.cast(argument.type().parse(this, rawArgAt(index)));
/* 276 */       } catch (ArgumentParseException e) {
/* 277 */         if (argument.onError() != null) { argument.onError().apply(this, e.type()); }
/* 278 */         else { throw new RuntimeException(e.getMessage()); }
/*     */       
/*     */       } 
/*     */     } 
/* 282 */     return null;
/*     */   }
/*     */   
/*     */   public int argPos(String identifier) {
/* 286 */     for (Argument<?> arg : this.args) {
/* 287 */       if (arg.identifier().equalsIgnoreCase(identifier)) {
/* 288 */         return this.args.indexOf(arg);
/*     */       }
/*     */     } 
/*     */     
/* 292 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public List<Argument<?>> args() {
/* 301 */     return this.args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int argSize() {
/* 310 */     return rawArgs().size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 319 */     return argSize();
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\impl\dispatcher\CommandContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */