/*    */ package dev.continuum.kits.parser;
/*    */ 
/*    */ import dev.continuum.kits.ContinuumKits;
/*    */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*    */ import dev.continuum.kits.parser.menu.MenuParser;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public abstract class ParsedMenu implements Listener {
/*    */   protected final Player player;
/*    */   
/*    */   public ParsedMenu(@NotNull Player player, @NotNull String fileName) {
/* 19 */     this.player = player;
/* 20 */     this.parser = MenuParser.wrap(Menus.of(fileName));
/* 21 */     this.parser.menu();
/* 22 */     init();
/* 23 */     Bukkit.getPluginManager().registerEvents(this, (Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/* 24 */     this.parser.menu().open(player);
/*    */   }
/*    */   
/*    */   protected final MenuParser parser;
/*    */   
/*    */   @EventHandler
/*    */   public final void execute(@NotNull InventoryCloseEvent event) {
/* 31 */     if (!event.getInventory().equals(this.parser.menu().getInventory()))
/*    */       return; 
/* 33 */     onClose(event);
/*    */     
/* 35 */     Schedulers.async().execute(() -> HandlerList.unregisterAll(this), 10);
/*    */   }
/*    */   
/*    */   public void onClose(@NotNull InventoryCloseEvent event) {}
/*    */   
/*    */   @NotNull
/*    */   public final Player player() {
/* 42 */     return this.player;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public final MenuParser parser() {
/* 47 */     return this.parser;
/*    */   }
/*    */   
/*    */   public abstract void init();
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\parser\ParsedMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */