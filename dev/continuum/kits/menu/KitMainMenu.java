/*     */ package dev.continuum.kits.menu;
/*     */ import dev.continuum.kits.api.event.impl.load.PremadeKitLoadEvent;
/*     */ import dev.continuum.kits.api.event.impl.menu.KitMenuEvent;
/*     */ import dev.continuum.kits.config.Messages;
/*     */ import dev.continuum.kits.database.kit.premade.PremadeKit;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.item.ItemBuilder;
/*     */ import dev.continuum.kits.libs.utils.menu.Button;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import dev.continuum.kits.parser.Menus;
/*     */ import dev.continuum.kits.parser.ParsedMenu;
/*     */ import dev.continuum.kits.parser.item.ItemParser;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.kyori.adventure.audience.Audience;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.inventory.ClickType;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ public class KitMainMenu extends ParsedMenu {
/*     */   public KitMainMenu(@NotNull Player player) {
/*  28 */     super(player, "kit_menu");
/*     */     
/*  30 */     KitMenuEvent event = new KitMenuEvent(player, this);
/*  31 */     if (!event.fire()) player.closeInventory();
/*     */   
/*     */   }
/*     */   
/*     */   public void init() {
/*  36 */     this.parser.placeItem(this.parser.normalItem("light_border"), event -> event.setCancelled(true));
/*  37 */     this.parser.placeItem(this.parser.normalItem("dark_border"), event -> event.setCancelled(true));
/*     */     
/*  39 */     this.parser.placeItem(this.parser.normalItem("clear_inventory"), event -> {
/*     */           event.setCancelled(true);
/*     */           
/*     */           PlayerInventory inventory = (PlayerInventory)this.player.getOpenInventory().getBottomInventory();
/*     */           
/*     */           ItemStack[] array = inventory.getContents();
/*     */           
/*     */           List<ItemStack> listWithEmptyItems = new ArrayList<>(Arrays.asList(array));
/*     */           
/*     */           List<ItemStack> list = new ArrayList<>();
/*     */           
/*     */           for (ItemStack potentiallyEmpty : listWithEmptyItems) {
/*     */             if (potentiallyEmpty == null || potentiallyEmpty.getType() == Material.AIR || potentiallyEmpty.getAmount() < 1) {
/*     */               continue;
/*     */             }
/*     */             
/*     */             list.add(potentiallyEmpty);
/*     */           } 
/*     */           
/*     */           String cleared = String.valueOf(list.size());
/*     */           
/*     */           Messages.message("cleared_inventory", (Audience)this.player, (Elements)Elements.of((Object[])new Tuple[] { Tuple.tuple("cleared", cleared) }));
/*     */           
/*     */           inventory.clear();
/*     */         });
/*  64 */     this.parser.placeItem(this.parser.normalItem("kit_room"), event -> {
/*     */           event.setCancelled(true);
/*     */           
/*     */           new KitRoomMenu(this.player);
/*     */         });
/*  69 */     this.parser.placeItem(this.parser.normalItem("premade_kit"), event -> {
/*     */           event.setCancelled(true);
/*     */           
/*     */           PremadeKitLoadEvent loadEvent = new PremadeKitLoadEvent(this.player);
/*     */           if (!loadEvent.fire()) {
/*     */             return;
/*     */           }
/*     */           PremadeKit.give(this.player);
/*     */         });
/*  78 */     FileConfiguration config = Menus.of("kit_menu");
/*     */     
/*  80 */     List<Integer> normalSlots = config.getIntegerList("kit_button.normal_slots");
/*  81 */     List<Integer> premiumSlots = config.getIntegerList("kit_button.premium_slots");
/*     */     
/*  83 */     List<Integer> enderChestSlots = config.getIntegerList("ender_chest_button.slots");
/*     */     int kitSlot;
/*  85 */     for (kitSlot = 1; kitSlot <= enderChestSlots.size(); kitSlot++) {
/*  86 */       ItemBuilder builder = ItemParser.parse(config, "ender_chest_button.item", List.of(
/*  87 */             Tuple.tuple("kit", 
/*     */               
/*  89 */               String.valueOf(kitSlot))));
/*     */ 
/*     */ 
/*     */       
/*  93 */       Integer itemSlot = enderChestSlots.get(kitSlot - 1);
/*  94 */       if (itemSlot != null) {
/*     */         
/*  96 */         int copy = kitSlot;
/*  97 */         this.parser.menu().button(itemSlot.intValue(), Button.button()
/*  98 */             .item(builder)
/*  99 */             .onClick(event -> {
/*     */                 event.setCancelled(true);
/*     */                 
/*     */                 if (event.getClick() == ClickType.LEFT) {
/*     */                   this.player.performCommand("kit load enderchest " + copy);
/*     */                 } else if (event.getClick() == ClickType.RIGHT) {
/*     */                   this.player.performCommand("kit edit enderchest " + copy);
/*     */                 } 
/*     */               }));
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     for (kitSlot = 1; kitSlot <= normalSlots.size(); kitSlot++) {
/* 112 */       ItemBuilder builder = ItemParser.parse(config, "kit_button.item.normal", List.of(
/* 113 */             Tuple.tuple("kit", 
/*     */               
/* 115 */               String.valueOf(kitSlot))));
/*     */ 
/*     */ 
/*     */       
/* 119 */       Integer normalSlot = normalSlots.get(kitSlot - 1);
/* 120 */       if (normalSlot != null) {
/*     */         
/* 122 */         int copy = kitSlot;
/* 123 */         this.parser.menu().button(normalSlot.intValue(), Button.button()
/* 124 */             .item(builder)
/* 125 */             .onClick(event -> {
/*     */                 event.setCancelled(true);
/*     */                 
/*     */                 if (event.getClick() == ClickType.LEFT) {
/*     */                   this.player.performCommand("kit load kit " + copy);
/*     */                 } else if (event.getClick() == ClickType.RIGHT) {
/*     */                   new KitEditorMenu(this.player, copy);
/*     */                 } 
/*     */               }));
/*     */       } 
/*     */     } 
/*     */     
/* 137 */     if (this.player.hasPermission("kits.premium")) {
/* 138 */       for (kitSlot = 1; kitSlot <= premiumSlots.size(); kitSlot++) {
/* 139 */         ItemBuilder builder = ItemParser.parse(config, "kit_button.item.premium.unlocked", List.of(
/* 140 */               Tuple.tuple("kit", 
/*     */                 
/* 142 */                 String.valueOf(kitSlot))));
/*     */ 
/*     */ 
/*     */         
/* 146 */         Integer premiumSlot = premiumSlots.get(kitSlot - 1);
/* 147 */         if (premiumSlot != null) {
/*     */           
/* 149 */           int copy = kitSlot;
/* 150 */           this.parser.menu().button(premiumSlot.intValue(), Button.button()
/* 151 */               .item(builder)
/* 152 */               .onClick(event -> {
/*     */                   event.setCancelled(true);
/*     */                   
/*     */                   if (event.getClick() == ClickType.LEFT) {
/*     */                     this.player.performCommand("kit load kit " + copy);
/*     */                   } else if (event.getClick() == ClickType.RIGHT) {
/*     */                     new KitEditorMenu(this.player, copy);
/*     */                   } 
/*     */                 }));
/*     */         } 
/*     */       } 
/*     */     } else {
/* 164 */       for (kitSlot = 1; kitSlot <= premiumSlots.size(); kitSlot++) {
/* 165 */         ItemBuilder builder = ItemParser.parse(config, "kit_button.item.premium.locked", List.of(
/* 166 */               Tuple.tuple("kit", 
/*     */                 
/* 168 */                 String.valueOf(kitSlot))));
/*     */ 
/*     */ 
/*     */         
/* 172 */         Integer premiumSlot = premiumSlots.get(kitSlot - 1);
/* 173 */         if (premiumSlot != null)
/*     */         {
/* 175 */           this.parser.menu().button(premiumSlot.intValue(), Button.button()
/* 176 */               .item(builder)
/* 177 */               .onClick(event -> event.setCancelled(true)));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     this.parser.placeCustomItems();
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\menu\KitMainMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */