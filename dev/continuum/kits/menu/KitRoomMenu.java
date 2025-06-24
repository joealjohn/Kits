/*     */ package dev.continuum.kits.menu;
/*     */ import dev.continuum.kits.api.event.impl.menu.KitRoomMenuEvent;
/*     */ import dev.continuum.kits.category.KitRoomCategories;
/*     */ import dev.continuum.kits.category.KitRoomCategory;
/*     */ import dev.continuum.kits.config.Messages;
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.cachable.impl.CachableImpl;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.item.ItemBuilder;
/*     */ import dev.continuum.kits.libs.utils.misc.ObjectUtils;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import dev.continuum.kits.parser.ParsedMenu;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.kyori.adventure.audience.Audience;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ public class KitRoomMenu extends ParsedMenu {
/*  26 */   private KitRoomCategory currentCategory = KitRoomCategories.crystalPvP();
/*     */   private boolean isEditing = false;
/*     */   private boolean newCategory = false;
/*     */   
/*     */   public KitRoomMenu(@NotNull Player player) {
/*  31 */     super(player, "kit_room");
/*     */     
/*  33 */     KitRoomMenuEvent event = new KitRoomMenuEvent(player, this);
/*  34 */     if (!event.fire()) player.closeInventory();
/*     */   
/*     */   }
/*     */   
/*     */   public void init() {
/*  39 */     if (this.currentCategory == null) this.currentCategory = KitRoomCategories.crystalPvP();
/*     */     
/*  41 */     List<Integer> intList = List.of(new Integer[] { 
/*  42 */           Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), 
/*  43 */           Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), 
/*  44 */           Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26), 
/*  45 */           Integer.valueOf(27), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30), Integer.valueOf(31), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35), 
/*  46 */           Integer.valueOf(36), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), Integer.valueOf(40), Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43), Integer.valueOf(44) });
/*     */     
/*     */     Iterator<Integer> iterator;
/*  49 */     for (iterator = intList.iterator(); iterator.hasNext(); ) { int num = ((Integer)iterator.next()).intValue();
/*  50 */       this.parser.menu().inventory().setItem(num, new ItemStack(Material.AIR)); }
/*     */ 
/*     */     
/*  53 */     if (!this.newCategory) {
/*  54 */       this.parser.placeItem(this.parser.normalItem("border"), event -> event.setCancelled(true));
/*     */       
/*  56 */       if (this.player.hasPermission("kits.admin")) {
/*  57 */         this.parser.placeItem(this.parser.normalItem("edit_button"), event -> {
/*     */               event.setCancelled(true);
/*     */               if (!this.player.hasPermission("kits.admin")) {
/*     */                 return;
/*     */               }
/*     */               this.isEditing = true;
/*     */               Messages.findAndSend("editing_kit_room", (Audience)this.player);
/*     */             });
/*     */       }
/*  66 */       this.parser.placeItem(this.parser.normalItem("refill_button"), event -> {
/*     */             event.setCancelled(true);
/*     */             
/*     */             init();
/*     */           });
/*  71 */       for (iterator = KitRoomCategories.categories().iterator(); iterator.hasNext(); ) { KitRoomCategory category = (KitRoomCategory)iterator.next();
/*  72 */         this.parser.menu().button(category.at().intValue(), Button.button()
/*  73 */             .item(ItemBuilder.item(category.parsed()))
/*  74 */             .onClick(event -> {
/*     */                 event.setCancelled(true);
/*     */                 
/*     */                 this.currentCategory = category;
/*     */                 
/*     */                 this.newCategory = true;
/*     */                 init();
/*     */               })); }
/*     */     
/*     */     } 
/*  84 */     KitRoomCategories.items(this.currentCategory).thenAccept(contents -> {
/*     */           for (int key = 0; key < 45; key++) {
/*     */             ItemStack value = (ItemStack)ObjectUtils.defaultIfNull(contents.val(Integer.valueOf(key)), new ItemStack(Material.AIR));
/*     */             this.parser.menu().inventory().setItem(key, value);
/*     */           } 
/*     */           this.parser.placeCustomItems();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onClose(@NotNull InventoryCloseEvent event) {
/*  97 */     if (this.isEditing) {
/*  98 */       ItemStack[] contentsArray = event.getInventory().getContents();
/*  99 */       List<ItemStack> contentsList = new ArrayList<>(Arrays.asList(contentsArray));
/*     */       
/* 101 */       CachableImpl cachableImpl = Cachable.of();
/* 102 */       for (int index = 0; index < contentsList.size(); index++) {
/* 103 */         ItemStack item = (ItemStack)ObjectUtils.defaultIfNull(contentsList
/* 104 */             .get(index), new ItemStack(Material.AIR));
/*     */ 
/*     */         
/* 107 */         cachableImpl.cache(Integer.valueOf(index), item);
/*     */       } 
/*     */       
/* 110 */       KitRoomCategories.items(this.currentCategory, (Cachable)cachableImpl);
/* 111 */       Messages.findAndSend("saved_kit_room", (Elements)Elements.of((Object[])new Tuple[] { Tuple.tuple("category", category().rawName()) }), (Audience)this.player);
/*     */     } 
/*     */     
/* 114 */     Schedulers.sync().execute(() -> new KitMainMenu(this.player), 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public KitRoomCategory category() {
/* 120 */     return this.currentCategory;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\menu\KitRoomMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */