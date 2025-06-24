/*     */ package dev.continuum.kits.libs.utils.menu;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.item.ItemBuilder;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Button
/*     */ {
/*  24 */   private ItemBuilder item = new ItemBuilder(Material.AIR);
/*     */   private boolean isRefreshingButton = false;
/*  26 */   private long refreshDelay = 0L;
/*  27 */   private long refreshPeriod = 20L;
/*     */ 
/*     */   
/*     */   private boolean isRefreshingAsync = true;
/*     */   
/*     */   private ButtonListener listener;
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Button button() {
/*  37 */     return new Button();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Button button(@Nullable ItemBuilder item) {
/*  47 */     return (new Button())
/*  48 */       .item(item);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Button button(@Nullable ItemBuilder item, @Nullable ButtonListener listener) {
/*  59 */     return (new Button())
/*  60 */       .item(item)
/*  61 */       .onClick(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Button refreshAsync(boolean refreshAsync) {
/*  71 */     this.isRefreshingAsync = refreshAsync;
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Button refresh(boolean refresh) {
/*  82 */     this.isRefreshingButton = refresh;
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Button refreshDelay(long refreshDelay) {
/*  93 */     this.refreshDelay = refreshDelay;
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Button refreshPeriod(long refreshPeriod) {
/* 104 */     this.refreshPeriod = refreshPeriod;
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Button refreshTime(long refreshDelay, long refreshPeriod) {
/* 116 */     this.refreshDelay = refreshDelay;
/* 117 */     this.refreshPeriod = refreshPeriod;
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Button onClick(@Nullable ButtonListener listener) {
/* 128 */     this.listener = listener;
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Button item(@Nullable ItemBuilder item) {
/* 139 */     this.item = item;
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder item() {
/* 149 */     return this.item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonListener listener() {
/* 158 */     return this.listener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRefreshingButton() {
/* 167 */     return this.isRefreshingButton;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long refreshDelay() {
/* 176 */     return this.refreshDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long refreshPeriod() {
/* 185 */     return this.refreshPeriod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRefreshingAsync() {
/* 194 */     return this.isRefreshingAsync;
/*     */   }
/*     */   
/*     */   public static interface ButtonListener {
/*     */     void onClick(@NotNull InventoryClickEvent param1InventoryClickEvent);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\menu\Button.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */