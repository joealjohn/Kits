/*     */ package dev.continuum.kits.menu;
/*     */ import dev.continuum.kits.ContinuumKits;
/*     */ import dev.continuum.kits.api.event.impl.save.KitSaveEvent;
/*     */ import dev.continuum.kits.config.Messages;
/*     */ import dev.continuum.kits.database.DatabaseProvider;
/*     */ import dev.continuum.kits.database.kit.copy.CopiedKit;
/*     */ import dev.continuum.kits.inventory.InventoryMapping;
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.cachable.impl.CachableImpl;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.misc.ObjectUtils;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import dev.continuum.kits.parser.menu.MenuParser;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.kyori.adventure.audience.Audience;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.HandlerList;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ public class KitEditorMenu implements Listener {
/*     */   private final Player player;
/*     */   
/*     */   public KitEditorMenu(@NotNull Player player, int kit) {
/*  37 */     this.player = player;
/*  38 */     this.kit = kit;
/*  39 */     this.parser = MenuParser.wrap(Menus.of("kit_editor"));
/*  40 */     this.parser.menu();
/*  41 */     init();
/*  42 */     Bukkit.getPluginManager().registerEvents(this, (Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*  43 */     this.parser.menu().open(player);
/*     */   }
/*     */   private final MenuParser parser; private final int kit;
/*     */   public void init() {
/*  47 */     data();
/*     */     
/*  49 */     this.parser.placeItem(this.parser.normalItem("border"), this::cancel);
/*     */     
/*  51 */     this.parser.placeItem(this.parser.normalItem("save"), event -> {
/*     */           cancel(event);
/*     */           
/*     */           this.player.closeInventory();
/*     */         });
/*  56 */     this.parser.placeItem(this.parser.normalItem("import"), event -> {
/*     */           cancel(event);
/*     */           
/*     */           List<Integer> toClear = List.of(new Integer[] { 
/*     */                 Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), 
/*     */                 Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), 
/*     */                 Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30), Integer.valueOf(31), Integer.valueOf(32), 
/*     */                 Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), Integer.valueOf(40), Integer.valueOf(41), Integer.valueOf(42), 
/*     */                 Integer.valueOf(43), Integer.valueOf(44) });
/*     */           
/*     */           Iterator<Integer> iterator = toClear.iterator();
/*     */           
/*     */           while (iterator.hasNext()) {
/*     */             int toClearSlot = ((Integer)iterator.next()).intValue();
/*     */             
/*     */             this.parser.menu().inventory().setItem(toClearSlot, new ItemStack(Material.AIR));
/*     */           } 
/*     */           
/*     */           ItemStack[] playerContents = this.player.getOpenInventory().getBottomInventory().getContents();
/*     */           
/*     */           List<ItemStack> playerContentsList = new ArrayList<>(Arrays.asList(playerContents));
/*     */           
/*     */           CachableImpl cachableImpl = Cachable.of();
/*     */           
/*     */           for (ItemStack stack : playerContentsList) {
/*     */             if (stack == null) {
/*     */               stack = new ItemStack(Material.AIR);
/*     */             }
/*     */             cachableImpl.cache(Integer.valueOf(cachableImpl.cached()), stack);
/*     */           } 
/*     */           if (cachableImpl.cached() == 0) {
/*     */             return;
/*     */           }
/*     */           InventoryMapping mapping = InventoryMapping.defaultMapping();
/*     */           for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.armor().mappings().snapshot().asMap().entrySet()) {
/*     */             Integer key = entry.getKey();
/*     */             Integer val = entry.getValue();
/*     */             ItemStack item = (ItemStack)cachableImpl.val(key);
/*     */             if (item == null) {
/*     */               item = new ItemStack(Material.AIR);
/*     */             }
/*     */             this.parser.menu().inventory().setItem(val.intValue(), item);
/*     */           } 
/*     */           for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.offhand().mappings().snapshot().asMap().entrySet()) {
/*     */             Integer key = entry.getKey();
/*     */             Integer val = entry.getValue();
/*     */             ItemStack item = (ItemStack)cachableImpl.val(key);
/*     */             if (item == null) {
/*     */               item = new ItemStack(Material.AIR);
/*     */             }
/*     */             this.parser.menu().inventory().setItem(val.intValue(), item);
/*     */           } 
/*     */           for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.contents().mappings().snapshot().asMap().entrySet()) {
/*     */             Integer key = entry.getKey();
/*     */             Integer val = entry.getValue();
/*     */             ItemStack item = (ItemStack)cachableImpl.val(key);
/*     */             if (item == null) {
/*     */               item = new ItemStack(Material.AIR);
/*     */             }
/*     */             this.parser.menu().inventory().setItem(val.intValue(), item);
/*     */           } 
/*     */           for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.hotbar().mappings().snapshot().asMap().entrySet()) {
/*     */             Integer key = entry.getKey();
/*     */             Integer val = entry.getValue();
/*     */             ItemStack item = (ItemStack)cachableImpl.val(key);
/*     */             if (item == null) {
/*     */               item = new ItemStack(Material.AIR);
/*     */             }
/*     */             this.parser.menu().inventory().setItem(val.intValue(), item);
/*     */           } 
/*     */           Messages.findAndSend("imported_contents", (Audience)this.player);
/*     */         });
/* 128 */     this.parser.placeItem(this.parser.normalItem("generate_code"), event -> {
/*     */           cancel(event);
/*     */           
/*     */           CopiedKit copiedKit = CopiedKit.of(this.player.getUniqueId(), this.kit);
/*     */           
/*     */           Messages.findAndSend("generated_kit_code", (Elements)Elements.of((Object[])new Tuple[] { Tuple.tuple("code", copiedKit.id()) }), (Audience)this.player);
/*     */         });
/* 135 */     this.parser.placeItem(this.parser.normalItem("reset"), event -> {
/*     */           cancel(event);
/*     */           
/*     */           List<Integer> toClear = List.of(new Integer[] { 
/*     */                 Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), 
/*     */                 Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), 
/*     */                 Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30), Integer.valueOf(31), Integer.valueOf(32), 
/*     */                 Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), Integer.valueOf(40), Integer.valueOf(41), Integer.valueOf(42), 
/*     */                 Integer.valueOf(43), Integer.valueOf(44) });
/*     */           
/*     */           Iterator<Integer> iterator = toClear.iterator();
/*     */           
/*     */           while (iterator.hasNext()) {
/*     */             int toClearSlot = ((Integer)iterator.next()).intValue();
/*     */             
/*     */             this.parser.menu().inventory().setItem(toClearSlot, new ItemStack(Material.AIR));
/*     */           } 
/*     */           
/*     */           Cachable<Integer, ItemStack> data = (Cachable<Integer, ItemStack>)ObjectUtils.defaultIfNull(DatabaseProvider.database().data(this.player.getUniqueId(), this.kit), Cachable.of(Integer.class, ItemStack.class));
/*     */           
/*     */           InventoryMapping mapping = InventoryMapping.defaultMapping();
/*     */           
/*     */           for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.armor().mappings().snapshot().asMap().entrySet()) {
/*     */             Integer key = entry.getKey();
/*     */             
/*     */             Integer val = entry.getValue();
/*     */             
/*     */             ItemStack item = (ItemStack)data.val(key);
/*     */             
/*     */             if (item == null) {
/*     */               item = new ItemStack(Material.AIR);
/*     */             }
/*     */             
/*     */             this.parser.menu().inventory().setItem(val.intValue(), item);
/*     */           } 
/*     */           
/*     */           for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.offhand().mappings().snapshot().asMap().entrySet()) {
/*     */             Integer key = entry.getKey();
/*     */             Integer val = entry.getValue();
/*     */             ItemStack item = (ItemStack)data.val(key);
/*     */             if (item == null) {
/*     */               item = new ItemStack(Material.AIR);
/*     */             }
/*     */             this.parser.menu().inventory().setItem(val.intValue(), item);
/*     */           } 
/*     */           for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.contents().mappings().snapshot().asMap().entrySet()) {
/*     */             Integer key = entry.getKey();
/*     */             Integer val = entry.getValue();
/*     */             ItemStack item = (ItemStack)data.val(key);
/*     */             if (item == null) {
/*     */               item = new ItemStack(Material.AIR);
/*     */             }
/*     */             this.parser.menu().inventory().setItem(val.intValue(), item);
/*     */           } 
/*     */           for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.hotbar().mappings().snapshot().asMap().entrySet()) {
/*     */             Integer key = entry.getKey();
/*     */             Integer val = entry.getValue();
/*     */             ItemStack item = (ItemStack)data.val(key);
/*     */             if (item == null) {
/*     */               item = new ItemStack(Material.AIR);
/*     */             }
/*     */             this.parser.menu().inventory().setItem(val.intValue(), item);
/*     */           } 
/*     */           Messages.findAndSend("kit_resetted", (Audience)this.player);
/*     */         });
/* 200 */     this.parser.placeCustomItems();
/*     */   }
/*     */ 
/*     */   
/*     */   public void data() {
/* 205 */     Cachable<Integer, ItemStack> data = DatabaseProvider.database().data(this.player.getUniqueId(), this.kit);
/*     */     
/* 207 */     if (data == null || data.cached() == 0)
/*     */       return; 
/* 209 */     InventoryMapping mapping = InventoryMapping.defaultMapping();
/*     */     
/* 211 */     for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.armor().mappings().snapshot().asMap().entrySet()) {
/* 212 */       Integer key = entry.getKey();
/* 213 */       Integer val = entry.getValue();
/*     */       
/* 215 */       ItemStack item = (ItemStack)data.val(key);
/* 216 */       if (item == null) item = new ItemStack(Material.AIR);
/*     */       
/* 218 */       this.parser.menu().inventory().setItem(val.intValue(), item);
/*     */     } 
/*     */     
/* 221 */     for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.offhand().mappings().snapshot().asMap().entrySet()) {
/* 222 */       Integer key = entry.getKey();
/* 223 */       Integer val = entry.getValue();
/*     */       
/* 225 */       ItemStack item = (ItemStack)data.val(key);
/* 226 */       if (item == null) item = new ItemStack(Material.AIR);
/*     */       
/* 228 */       this.parser.menu().inventory().setItem(val.intValue(), item);
/*     */     } 
/*     */     
/* 231 */     for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.contents().mappings().snapshot().asMap().entrySet()) {
/* 232 */       Integer key = entry.getKey();
/* 233 */       Integer val = entry.getValue();
/*     */       
/* 235 */       ItemStack item = (ItemStack)data.val(key);
/* 236 */       if (item == null) item = new ItemStack(Material.AIR);
/*     */       
/* 238 */       this.parser.menu().inventory().setItem(val.intValue(), item);
/*     */     } 
/*     */     
/* 241 */     for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.hotbar().mappings().snapshot().asMap().entrySet()) {
/* 242 */       Integer key = entry.getKey();
/* 243 */       Integer val = entry.getValue();
/*     */       
/* 245 */       ItemStack item = (ItemStack)data.val(key);
/* 246 */       if (item == null) item = new ItemStack(Material.AIR);
/*     */       
/* 248 */       this.parser.menu().inventory().setItem(val.intValue(), item);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cancel(@NotNull InventoryClickEvent event) {
/* 253 */     event.setCancelled(true);
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onClick(@NotNull InventoryClickEvent event) {
/* 259 */     if (!event.getInventory().equals(this.parser.menu().getInventory()))
/*     */       return; 
/* 261 */     int slot = event.getRawSlot();
/*     */     
/* 263 */     List<Integer> list = List.of(new Integer[] { 
/* 264 */           Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), 
/* 265 */           Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), 
/* 266 */           Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26), 
/* 267 */           Integer.valueOf(27), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30), Integer.valueOf(31), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35), 
/* 268 */           Integer.valueOf(36), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), Integer.valueOf(40), Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43), Integer.valueOf(44) });
/*     */ 
/*     */     
/* 271 */     if (list.contains(Integer.valueOf(slot))) event.setCancelled(true); 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public final void onClose(@NotNull InventoryCloseEvent event) {
/* 276 */     if (!event.getInventory().equals(this.parser.menu().getInventory()))
/*     */       return; 
/* 278 */     Inventory inventory = this.parser.menu().inventory();
/*     */     
/* 280 */     InventoryMapping mapping = InventoryMapping.defaultMapping();
/*     */     
/* 282 */     CachableImpl cachableImpl = Cachable.of();
/*     */     
/* 284 */     for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.armor().mappings().snapshot().asMap().entrySet()) {
/* 285 */       int playerSlot = ((Integer)entry.getKey()).intValue();
/* 286 */       int invSlot = ((Integer)entry.getValue()).intValue();
/*     */       
/* 288 */       ItemStack item = (ItemStack)ObjectUtils.defaultIfNull(inventory
/* 289 */           .getItem(invSlot), new ItemStack(Material.AIR));
/*     */ 
/*     */ 
/*     */       
/* 293 */       cachableImpl.cache(Integer.valueOf(playerSlot), item);
/*     */     } 
/*     */     
/* 296 */     for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.offhand().mappings().snapshot().asMap().entrySet()) {
/* 297 */       int playerSlot = ((Integer)entry.getKey()).intValue();
/* 298 */       int invSlot = ((Integer)entry.getValue()).intValue();
/*     */       
/* 300 */       ItemStack item = (ItemStack)ObjectUtils.defaultIfNull(inventory
/* 301 */           .getItem(invSlot), new ItemStack(Material.AIR));
/*     */ 
/*     */ 
/*     */       
/* 305 */       cachableImpl.cache(Integer.valueOf(playerSlot), item);
/*     */     } 
/*     */     
/* 308 */     for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.contents().mappings().snapshot().asMap().entrySet()) {
/* 309 */       int playerSlot = ((Integer)entry.getKey()).intValue();
/* 310 */       int invSlot = ((Integer)entry.getValue()).intValue();
/*     */       
/* 312 */       ItemStack item = (ItemStack)ObjectUtils.defaultIfNull(inventory
/* 313 */           .getItem(invSlot), new ItemStack(Material.AIR));
/*     */ 
/*     */ 
/*     */       
/* 317 */       cachableImpl.cache(Integer.valueOf(playerSlot), item);
/*     */     } 
/*     */     
/* 320 */     for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)mapping.hotbar().mappings().snapshot().asMap().entrySet()) {
/* 321 */       int playerSlot = ((Integer)entry.getKey()).intValue();
/* 322 */       int invSlot = ((Integer)entry.getValue()).intValue();
/*     */       
/* 324 */       ItemStack item = (ItemStack)ObjectUtils.defaultIfNull(inventory
/* 325 */           .getItem(invSlot), new ItemStack(Material.AIR));
/*     */ 
/*     */ 
/*     */       
/* 329 */       cachableImpl.cache(Integer.valueOf(playerSlot), item);
/*     */     } 
/*     */     
/* 332 */     KitSaveEvent saveEvent = new KitSaveEvent(this.player, this.kit, true, (Cachable)cachableImpl);
/* 333 */     if (!saveEvent.fire())
/*     */       return; 
/* 335 */     DatabaseProvider.database().save(this.player.getUniqueId(), this.kit, (Cachable)cachableImpl, (result, user) -> Messages.findAndSend(result.booleanValue() ? "saved" : "failed_to_save", (Audience)this.player));
/*     */ 
/*     */ 
/*     */     
/* 339 */     Schedulers.sync().execute(() -> new KitMainMenu(this.player), 10);
/*     */ 
/*     */ 
/*     */     
/* 343 */     Schedulers.async().execute(() -> HandlerList.unregisterAll(this), 1);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public final Player player() {
/* 348 */     return this.player;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public final MenuParser parser() {
/* 353 */     return this.parser;
/*     */   }
/*     */   
/*     */   public int kit() {
/* 357 */     return this.kit;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\menu\KitEditorMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */