/*     */ package dev.continuum.kits.libs.utils.registration;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.command.args.Argument;
/*     */ import dev.continuum.kits.libs.utils.command.impl.CommandResultWrapper;
/*     */ import dev.continuum.kits.libs.utils.command.impl.Commands;
/*     */ import dev.continuum.kits.libs.utils.command.impl.CommandsRegistrar;
/*     */ import dev.continuum.kits.libs.utils.command.impl.dispatcher.CommandContext;
/*     */ import dev.continuum.kits.libs.utils.command.impl.suggestions.Suggestions;
/*     */ import dev.continuum.kits.libs.utils.library.Utils;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandMap;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.command.PluginCommand;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Registrar
/*     */ {
/*     */   public static void events(@NotNull JavaPlugin plugin, @NotNull Listener listener) {
/*  33 */     plugin.getServer().getPluginManager().registerEvents(listener, (Plugin)plugin);
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
/*     */   public static void command(@NotNull JavaPlugin plugin, @NotNull String commandName, @NotNull CommandExecutor executor) {
/*  45 */     Objects.requireNonNull(plugin, "Plugin cannot be null");
/*  46 */     Objects.requireNonNull(executor, "CommandExecutor cannot be null");
/*     */     
/*  48 */     ((PluginCommand)Objects.<PluginCommand>requireNonNull(plugin.getCommand(commandName))).setExecutor(executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static CommandMap commandMap(@NotNull JavaPlugin plugin) {
/*  58 */     return plugin.getServer().getCommandMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void commandMap(@NotNull JavaPlugin plugin, @NotNull String commandName, @NotNull Command command) {
/*  69 */     ((CommandMap)Objects.<CommandMap>requireNonNull(commandMap(plugin))).register(plugin.getName(), command);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void events(@NotNull Listener listener) {
/*  78 */     events(Utils.plugin(), listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void command(@NotNull String commandName, @NotNull CommandExecutor executor) {
/*  89 */     command(Utils.plugin(), commandName, executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static CommandMap commandMap() {
/*  98 */     return commandMap(Utils.plugin());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void commandMap(@NotNull String commandName, @NotNull Command command) {
/* 108 */     commandMap(Utils.plugin(), commandName, command);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void commandMap(CommandsRegistrar handler) {
/* 113 */     final Commands builder = handler.commandBuilder();
/*     */     
/* 115 */     Command command = new Command(builder.name(), builder.description(), builder.usage(), builder.aliases().aliases())
/*     */       {
/*     */         public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args)
/*     */         {
/* 119 */           CommandContext ctx = CommandContext.context(sender, this, label, args);
/* 120 */           ctx.args().clear();
/*     */           
/* 122 */           for (Argument<?> arg : (Iterable<Argument<?>>)builder.args()) ctx.args().add(arg); 
/* 123 */           for (Predicate<CommandContext> filter : (Iterable<Predicate<CommandContext>>)builder.requirements()) { if (filter.test(ctx)) return true;  }
/*     */           
/* 125 */           return CommandResultWrapper.unwrap(builder.executes().run(ctx));
/*     */         }
/*     */         
/*     */         @NotNull
/*     */         public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
/* 130 */           CommandContext ctx = CommandContext.context(sender, this, builder.name(), args);
/*     */           
/* 132 */           for (Argument<?> arg : (Iterable<Argument<?>>)builder.args()) {
/* 133 */             if (arg.suggestions() == null || ctx.argPos(arg.identifier()) == -1 || ctx.args().size() != ctx.argPos(arg.identifier()))
/* 134 */               continue;  if (arg.suggestions().suggest(ctx) == null) return List.of();
/*     */             
/* 136 */             return arg.suggestions().suggest(ctx).unwrap();
/*     */           } 
/*     */           
/* 139 */           Suggestions suggestions = builder.completes().suggest(ctx);
/*     */           
/* 141 */           if (suggestions == null) return List.of(); 
/* 142 */           return suggestions.unwrap();
/*     */         }
/*     */       };
/*     */     
/* 146 */     command.setName(builder.bukkitCommand().getName());
/* 147 */     command.setAliases(builder.bukkitCommand().getAliases());
/* 148 */     command.setDescription(builder.bukkitCommand().getDescription());
/*     */     
/* 150 */     if (builder.bukkitCommand().getPermission() != null) {
/* 151 */       command.setPermission(builder.bukkitCommand().getPermission());
/*     */     }
/*     */     
/* 154 */     command.setUsage(builder.bukkitCommand().getUsage());
/*     */     
/* 156 */     if (builder.bukkitCommand().permissionMessage() != null) {
/* 157 */       command.permissionMessage(builder.bukkitCommand().permissionMessage());
/*     */     }
/*     */     
/* 160 */     commandMap().register(builder.bukkitCommand().getName(), builder.namespace(), command);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\registration\Registrar.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */