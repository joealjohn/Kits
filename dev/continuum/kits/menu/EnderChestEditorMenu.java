/*     */ package dev.continuum.kits.menu;
/*     */ 
/*     */ import dev.continuum.kits.ContinuumKits;
/*     */ import dev.continuum.kits.api.event.impl.save.EnderChestSaveEvent;
/*     */ import dev.continuum.kits.config.Messages;
/*     */ import dev.continuum.kits.database.DatabaseProvider;
/*     */ import dev.continuum.kits.inventory.EnderChestMapping;
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.cachable.impl.CachableImpl;
/*     */ import dev.continuum.kits.libs.utils.misc.ObjectUtils;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import dev.continuum.kits.parser.Menus;
/*     */ import dev.continuum.kits.parser.menu.MenuParser;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.kyori.adventure.audience.Audience;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.HandlerList;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ public class EnderChestEditorMenu implements Listener {
/*     */   private final Player player;
/*     */   
/*     */   public EnderChestEditorMenu(@NotNull Player player, int kit) {
/*  34 */     this.player = player;
/*  35 */     this.kit = kit;
/*  36 */     this.parser = MenuParser.wrap(Menus.of("ender_chest_editor"));
/*  37 */     this.parser.menu();
/*  38 */     init();
/*  39 */     Bukkit.getPluginManager().registerEvents(this, (Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*  40 */     this.parser.menu().open(player);
/*     */   }
/*     */   private final MenuParser parser; private final int kit;
/*     */   public void init() {
/*  44 */     data();
/*     */     
/*  46 */     this.parser.placeItem(this.parser.normalItem("border"), event -> event.setCancelled(true));
/*     */     
/*  48 */     this.parser.placeItem(this.parser.normalItem("save"), event -> {
/*     */           event.setCancelled(true);
/*     */           
/*     */           this.player.closeInventory();
/*     */         });
/*  53 */     this.parser.placeItem(this.parser.normalItem("import"), event -> {
/*     */           Iterator<Integer> iterator = EnderChestMapping.playerSlots().iterator();
/*     */           while (iterator.hasNext()) {
/*     */             int playerSlot = ((Integer)iterator.next()).intValue();
/*     */             int menuSlot = EnderChestMapping.playerToMenuSlot(playerSlot);
/*     */             if (menuSlot == -1)
/*     */               continue; 
/*     */             ItemStack item = this.player.getOpenInventory().getBottomInventory().getItem(playerSlot);
/*     */             if (item == null)
/*     */               item = new ItemStack(Material.AIR); 
/*     */             this.parser.menu().getInventory().setItem(menuSlot, item);
/*     */           } 
/*     */           event.setCancelled(true);
/*     */         });
/*  67 */     this.parser.placeItem(this.parser.normalItem("reset"), event -> {
/*     */           data();
/*     */           
/*     */           Messages.findAndSend("ender_chest_resetted", (Audience)this.player);
/*     */           
/*     */           event.setCancelled(true);
/*     */         });
/*  74 */     this.parser.placeCustomItems();
/*     */   }
/*     */   
/*     */   public void data() {
/*  78 */     List<Integer> toClear = List.of(new Integer[] { 
/*  79 */           Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), 
/*  80 */           Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), 
/*  81 */           Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26) });
/*     */ 
/*     */     
/*  84 */     for (Iterator<Integer> iterator = toClear.iterator(); iterator.hasNext(); ) { int slot = ((Integer)iterator.next()).intValue();
/*  85 */       this.parser.menu().getInventory().setItem(slot, new ItemStack(Material.AIR)); }
/*     */ 
/*     */ 
/*     */     
/*  89 */     Cachable<Integer, ItemStack> data = DatabaseProvider.ecDatabase().data(this.player.getUniqueId(), this.kit);
/*     */     
/*  91 */     if (data == null || data.cached() == 0)
/*     */       return; 
/*  93 */     for (int i = 0; i < 27; i++) {
/*  94 */       ItemStack val = (ItemStack)ObjectUtils.defaultIfNull(data.val(Integer.valueOf(i)), new ItemStack(Material.AIR));
/*  95 */       this.parser.menu().getInventory().setItem(i, val);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onClick(@NotNull InventoryClickEvent event) {
/* 101 */     if (!event.getInventory().equals(this.parser.menu().getInventory()))
/*     */       return; 
/* 103 */     int slot = event.getRawSlot();
/*     */     
/* 105 */     List<Integer> list = List.of(new Integer[] { 
/* 106 */           Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), 
/* 107 */           Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), 
/* 108 */           Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26) });
/*     */ 
/*     */     
/* 111 */     if (list.contains(Integer.valueOf(slot))) event.setCancelled(true); 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onClose(@NotNull InventoryCloseEvent event) {
/* 116 */     if (!event.getInventory().equals(this.parser.menu().getInventory()))
/*     */       return; 
/* 118 */     ItemStack[] contents = event.getInventory().getContents();
/*     */     
/* 120 */     CachableImpl cachableImpl = Cachable.of();
/*     */     
/* 122 */     for (int slot = 0; slot < 27; slot++) {
/* 123 */       cachableImpl.cache(Integer.valueOf(slot), ObjectUtils.defaultIfNull(contents[slot], new ItemStack(Material.AIR)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     EnderChestSaveEvent saveEvent = new EnderChestSaveEvent(this.player, this.kit, (Cachable)cachableImpl);
/* 130 */     if (!saveEvent.fire())
/*     */       return; 
/* 132 */     DatabaseProvider.ecDatabase().save(this.player.getUniqueId(), this.kit, (Cachable)cachableImpl, (result, user) -> Messages.findAndSend(result.booleanValue() ? "saved_ender_chest" : "failed_to_save_ender_chest", (Audience)this.player));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     Cachable<Integer, ItemStack> dataTest = DatabaseProvider.ecDatabase().data(this.player.getUniqueId(), this.kit);
/*     */     
/* 139 */     if (dataTest == null || dataTest.cached() == 0)
/*     */       return; 
/* 141 */     Schedulers.sync().execute(() -> new KitMainMenu(this.player), 10);
/*     */ 
/*     */ 
/*     */     
/* 145 */     Schedulers.async().execute(() -> HandlerList.unregisterAll(this), 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\menu\EnderChestEditorMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */