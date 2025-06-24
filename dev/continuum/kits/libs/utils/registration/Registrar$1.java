/*     */ package dev.continuum.kits.libs.utils.registration;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.command.args.Argument;
/*     */ import dev.continuum.kits.libs.utils.command.impl.CommandResultWrapper;
/*     */ import dev.continuum.kits.libs.utils.command.impl.Commands;
/*     */ import dev.continuum.kits.libs.utils.command.impl.dispatcher.CommandContext;
/*     */ import dev.continuum.kits.libs.utils.command.impl.suggestions.Suggestions;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
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
/*     */ 
/*     */ class null
/*     */   extends Command
/*     */ {
/*     */   null(String name, String description, String usageMessage, List<String> aliases) {
/* 115 */     super(name, description, usageMessage, aliases);
/*     */   }
/*     */   
/*     */   public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
/* 119 */     CommandContext ctx = CommandContext.context(sender, this, label, args);
/* 120 */     ctx.args().clear();
/*     */     
/* 122 */     for (Argument<?> arg : (Iterable<Argument<?>>)builder.args()) ctx.args().add(arg); 
/* 123 */     for (Predicate<CommandContext> filter : (Iterable<Predicate<CommandContext>>)builder.requirements()) { if (filter.test(ctx)) return true;  }
/*     */     
/* 125 */     return CommandResultWrapper.unwrap(builder.executes().run(ctx));
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
/* 130 */     CommandContext ctx = CommandContext.context(sender, this, builder.name(), args);
/*     */     
/* 132 */     for (Argument<?> arg : (Iterable<Argument<?>>)builder.args()) {
/* 133 */       if (arg.suggestions() == null || ctx.argPos(arg.identifier()) == -1 || ctx.args().size() != ctx.argPos(arg.identifier()))
/* 134 */         continue;  if (arg.suggestions().suggest(ctx) == null) return List.of();
/*     */       
/* 136 */       return arg.suggestions().suggest(ctx).unwrap();
/*     */     } 
/*     */     
/* 139 */     Suggestions suggestions = builder.completes().suggest(ctx);
/*     */     
/* 141 */     if (suggestions == null) return List.of(); 
/* 142 */     return suggestions.unwrap();
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\registration\Registrar$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */