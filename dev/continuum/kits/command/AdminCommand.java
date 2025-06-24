/*    */ package dev.continuum.kits.command;
/*    */ import dev.continuum.kits.config.Messages;
/*    */ import dev.continuum.kits.database.kit.premade.PremadeKit;
/*    */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*    */ import dev.continuum.kits.libs.utils.cachable.impl.CachableImpl;
/*    */ import dev.continuum.kits.libs.utils.command.AbstractCommand;
/*    */ import dev.continuum.kits.libs.utils.command.CommandResult;
/*    */ import dev.continuum.kits.libs.utils.command.impl.CommandInfo;
/*    */ import dev.continuum.kits.libs.utils.command.impl.Commands;
/*    */ import dev.continuum.kits.libs.utils.command.impl.dispatcher.CommandContext;
/*    */ import dev.continuum.kits.libs.utils.command.impl.suggestions.Suggestions;
/*    */ import dev.continuum.kits.libs.utils.misc.ObjectUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.kyori.adventure.audience.Audience;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.Inventory;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ public class AdminCommand extends AbstractCommand {
/* 26 */   private static final CommandResult stop = CommandResult.stop();
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public Commands command() {
/* 31 */     return Commands.command("kitadmin")
/* 32 */       .info(info -> info.permission("kits", "admin"))
/* 33 */       .requirement(Requirements.playerOnly(ctx -> {
/*    */             CommandSender sender = ctx.sender();
/*    */             
/*    */             Messages.findAndSend("player_only", (Audience)sender);
/* 37 */           })).executes((CommandDispatcher)this)
/* 38 */       .completes((SuggestionDispatcher)this);
/*    */   }
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public CommandResult run(@NotNull CommandContext ctx) {
/* 44 */     Player player = ctx.player();
/*    */     
/* 46 */     if (ctx.size() != 1) {
/* 47 */       Messages.findAndSend("kit_admin_usage", (Audience)player);
/* 48 */       return stop;
/*    */     } 
/*    */     
/* 51 */     String action = ctx.rawArgs().get(0);
/*    */     
/* 53 */     if (action == null || action.isEmpty() || action.isBlank()) {
/* 54 */       Messages.findAndSend("kit_admin_usage", (Audience)player);
/* 55 */       return stop;
/*    */     } 
/*    */     
/* 58 */     if (action.equalsIgnoreCase("savepremadekit")) {
/* 59 */       Inventory inventory = player.getOpenInventory().getBottomInventory();
/* 60 */       ItemStack[] contentsArray = inventory.getContents();
/* 61 */       List<ItemStack> contentsList = new ArrayList<>(Arrays.asList(contentsArray));
/* 62 */       CachableImpl cachableImpl = Cachable.of();
/*    */       
/* 64 */       for (int index = 0; index <= 41 && 
/* 65 */         contentsList.size() > index; index++) {
/*    */         
/* 67 */         ItemStack item = (ItemStack)ObjectUtils.defaultIfNull(contentsList
/* 68 */             .get(index), new ItemStack(Material.AIR));
/*    */ 
/*    */ 
/*    */         
/* 72 */         cachableImpl.cache(Integer.valueOf(index), item);
/*    */       } 
/*    */       
/* 75 */       PremadeKit.contents((Cachable)cachableImpl);
/* 76 */       Messages.findAndSend("saved_premade_kit", (Audience)player);
/* 77 */       return stop;
/*    */     } 
/*    */     
/* 80 */     Messages.findAndSend("kit_admin_usage", (Audience)player);
/* 81 */     return stop;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Suggestions suggest(@NotNull CommandContext ctx) {
/* 87 */     if (ctx.sender().hasPermission("kits.admin")) {
/* 88 */       return Suggestions.of("savepremadekit");
/*    */     }
/*    */     
/* 91 */     return Suggestions.of();
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\command\AdminCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */