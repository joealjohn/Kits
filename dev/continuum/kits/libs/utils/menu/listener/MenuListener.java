/*     */ package dev.continuum.kits.libs.utils.menu.listener;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.item.ItemBuilder;
/*     */ import dev.continuum.kits.libs.utils.menu.Button;
/*     */ import dev.continuum.kits.libs.utils.menu.normal.Menu;
/*     */ import dev.continuum.kits.libs.utils.menu.paginated.PaginatedMenu;
/*     */ import dev.continuum.kits.libs.utils.menu.paginated.PaginatedSlot;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*     */ import org.bukkit.event.inventory.InventoryDragEvent;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MenuListener
/*     */   implements Listener
/*     */ {
/*     */   @EventHandler
/*     */   public void onClick(InventoryClickEvent event) {
/*  29 */     if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
/*  30 */       return;  Player player = (Player)event.getWhoClicked();
/*     */     
/*  32 */     InventoryHolder inventoryHolder = event.getInventory().getHolder(); if (inventoryHolder instanceof Menu) { Menu menu = (Menu)inventoryHolder;
/*     */       
/*  34 */       Button button = menu.button(event.getSlot());
/*  35 */       if (button != null) {
/*  36 */         Button.ButtonListener listener = button.listener();
/*  37 */         if (listener != null) {
/*  38 */           listener.onClick(event);
/*     */         }
/*     */       }  }
/*     */ 
/*     */     
/*  43 */     inventoryHolder = event.getInventory().getHolder(); if (inventoryHolder instanceof PaginatedMenu) { PaginatedMenu menu = (PaginatedMenu)inventoryHolder;
/*  44 */       Button stickyButton = menu.stickyButton(event.getSlot());
/*     */       
/*  46 */       if (menu.currentPageItemEnabled() && event.getSlot() == menu.currentPageSlot()) {
/*  47 */         event.setCancelled(true);
/*     */       }
/*     */       
/*  50 */       for (Map.Entry<Integer, ItemBuilder> entry : (Iterable<Map.Entry<Integer, ItemBuilder>>)menu.previousButton().entrySet()) {
/*  51 */         if (event.getSlot() == ((Integer)entry.getKey()).intValue()) {
/*  52 */           event.setCancelled(true);
/*     */           
/*  54 */           if (menu.currentPage() > 1) {
/*  55 */             menu.open(player, menu.currentPage() - 1);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/*  60 */       for (Map.Entry<Integer, ItemBuilder> entry : (Iterable<Map.Entry<Integer, ItemBuilder>>)menu.nextButton().entrySet()) {
/*  61 */         if (event.getSlot() == ((Integer)entry.getKey()).intValue()) {
/*  62 */           event.setCancelled(true);
/*     */           
/*  64 */           if (menu.totalPages() > 1) {
/*  65 */             menu.open(player, menu.currentPage() + 1);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/*  70 */       for (Map.Entry<Integer, Button> entry : (Iterable<Map.Entry<Integer, Button>>)menu.stickyButtons().entrySet()) {
/*  71 */         if (event.getSlot() == ((Integer)entry.getKey()).intValue() && 
/*  72 */           stickyButton != null && stickyButton.listener() != null) {
/*  73 */           stickyButton.listener().onClick(event);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*  78 */       for (Map.Entry<PaginatedSlot, Button> entry : (Iterable<Map.Entry<PaginatedSlot, Button>>)menu.buttons().entrySet()) {
/*  79 */         PaginatedSlot slotHolder = entry.getKey();
/*  80 */         if (slotHolder.slot() == event.getSlot() && slotHolder.page() == menu.currentPage()) {
/*  81 */           Button button = entry.getValue();
/*  82 */           if (button != null && button.listener() != null) {
/*  83 */             button.listener().onClick(event);
/*     */           }
/*     */         } 
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onClose(InventoryCloseEvent event) {
/*  97 */     Player player = (Player)event.getPlayer();
/*     */     
/*  99 */     InventoryHolder inventoryHolder = event.getInventory().getHolder(); if (inventoryHolder instanceof Menu) { Menu menu = (Menu)inventoryHolder;
/* 100 */       if (menu.onClose() != null)
/* 101 */         menu.onClose().onClose(event);  }
/*     */     else
/* 103 */     { inventoryHolder = event.getInventory().getHolder(); if (inventoryHolder instanceof PaginatedMenu) { PaginatedMenu menu = (PaginatedMenu)inventoryHolder;
/* 104 */         if (menu.onClose() != null) {
/* 105 */           menu.onClose().onClose(event);
/*     */         } }
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onDrag(InventoryDragEvent event) {
/* 117 */     Player player = (Player)event.getWhoClicked();
/*     */     
/* 119 */     InventoryHolder inventoryHolder = event.getInventory().getHolder(); if (inventoryHolder instanceof Menu) { Menu menu = (Menu)inventoryHolder;
/* 120 */       if (menu.onDrag() != null)
/* 121 */         menu.onDrag().onDrag(event);  }
/*     */     else
/* 123 */     { inventoryHolder = event.getInventory().getHolder(); if (inventoryHolder instanceof PaginatedMenu) { PaginatedMenu menu = (PaginatedMenu)inventoryHolder;
/* 124 */         if (menu.onDrag() != null)
/* 125 */           menu.onDrag().onDrag(event);  }
/*     */        }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\menu\listener\MenuListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */