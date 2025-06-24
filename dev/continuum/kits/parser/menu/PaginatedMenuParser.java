/*     */ package dev.continuum.kits.parser.menu;
/*     */ 
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import dev.continuum.kits.libs.utils.item.ItemBuilder;
/*     */ import dev.continuum.kits.libs.utils.menu.Button;
/*     */ import dev.continuum.kits.libs.utils.menu.paginated.PaginatedMenu;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import dev.continuum.kits.libs.utils.text.color.TextStyle;
/*     */ import dev.continuum.kits.parser.item.ItemParser;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ public class PaginatedMenuParser {
/*     */   private final FileConfiguration configuration;
/*     */   private PaginatedMenu menu;
/*     */   
/*     */   public PaginatedMenuParser(@NotNull FileConfiguration configuration) {
/*  24 */     this.configuration = configuration;
/*     */   }
/*     */   @NotNull
/*     */   public static PaginatedMenuParser wrap(@NotNull FileConfiguration configuration) {
/*  28 */     return new PaginatedMenuParser(configuration);
/*     */   }
/*     */   
/*     */   public int size() {
/*  32 */     int size = this.configuration.getInt("size");
/*  33 */     if (size == 0) return 54; 
/*  34 */     return size;
/*     */   }
/*     */   @NotNull
/*     */   public Component title() {
/*  38 */     String raw = this.configuration.getString("title");
/*  39 */     if (raw == null) return (Component)Component.text("Failed to load title"); 
/*  40 */     return TextStyle.style(raw);
/*     */   }
/*     */   @NotNull
/*     */   public ConfigurationSection itemsSection() {
/*  44 */     ConfigurationSection items = this.configuration.getConfigurationSection("items");
/*  45 */     if (items == null) return this.configuration.createSection("items"); 
/*  46 */     return items;
/*     */   }
/*     */   @NotNull
/*     */   public ConfigurationSection customItemsSection() {
/*  50 */     ConfigurationSection items = this.configuration.getConfigurationSection("custom_items");
/*  51 */     if (items == null) return this.configuration.createSection("custom_items"); 
/*  52 */     return items;
/*     */   }
/*     */   @NotNull
/*     */   public MenuParserItem normalItem(@NotNull String name) {
/*  56 */     ConfigurationSection section = itemsSection().getConfigurationSection(name);
/*  57 */     if (section == null) return MenuParserItem.of(
/*  58 */           ItemBuilder.item(Material.AIR), List.of(Integer.valueOf(10)), false);
/*     */ 
/*     */     
/*  61 */     boolean enabled = section.getBoolean("enabled");
/*  62 */     List<Integer> slots = section.getIntegerList("slots");
/*     */     
/*  64 */     ItemBuilder builder = ItemParser.parse(this.configuration, "items." + name + ".item");
/*     */ 
/*     */ 
/*     */     
/*  68 */     return MenuParserItem.of(builder, slots, enabled);
/*     */   }
/*     */   @NotNull
/*     */   public MenuParserItem customItem(@NotNull String name) {
/*  72 */     ConfigurationSection section = customItemsSection().getConfigurationSection(name);
/*  73 */     if (section == null) return MenuParserItem.of(
/*  74 */           ItemBuilder.item(Material.AIR), List.of(Integer.valueOf(10)), false);
/*     */ 
/*     */     
/*  77 */     List<Integer> slots = section.getIntegerList("slots");
/*     */     
/*  79 */     ItemBuilder builder = ItemParser.parse(this.configuration, "custom_items." + name + ".item");
/*     */ 
/*     */ 
/*     */     
/*  83 */     return MenuParserItem.of(builder, slots, true);
/*     */   }
/*     */   
/*     */   public void placeItem(@NotNull MenuParserItem item, @NotNull Button.ButtonListener listener) {
/*  87 */     List<Integer> slots = item.slots();
/*  88 */     boolean enabled = item.enabled();
/*  89 */     ItemBuilder builder = item.item();
/*     */     
/*  91 */     if (!enabled)
/*     */       return; 
/*  93 */     for (Iterator<Integer> iterator = slots.iterator(); iterator.hasNext(); ) { int slot = ((Integer)iterator.next()).intValue();
/*  94 */       menu().stickyButton(slot, Button.button()
/*  95 */           .item(builder)
/*  96 */           .onClick(listener)); }
/*     */   
/*     */   }
/*     */   
/*     */   public void placeItem(@NotNull MenuParserItem item) {
/* 101 */     placeItem(item, event -> event.setCancelled(true));
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public PaginatedMenu menu() {
/* 106 */     if (this.menu == null) {
/* 107 */       this.menu = PaginatedMenu.menu(title(), size());
/*     */     }
/*     */     
/* 110 */     return this.menu;
/*     */   }
/*     */   
/*     */   public FileConfiguration configuration() {
/* 114 */     return this.configuration;
/*     */   }
/*     */   @NotNull
/*     */   public MenuParserItem normalItem(@NotNull String name, @NotNull List<Tuple<String, String>> replacements) {
/* 118 */     ConfigurationSection section = itemsSection().getConfigurationSection(name);
/* 119 */     if (section == null) return MenuParserItem.of(
/* 120 */           ItemBuilder.item(Material.AIR), List.of(Integer.valueOf(10)), false);
/*     */ 
/*     */     
/* 123 */     boolean enabled = section.getBoolean("enabled");
/* 124 */     List<Integer> slots = section.getIntegerList("slots");
/*     */     
/* 126 */     ItemBuilder builder = ItemParser.parse(this.configuration, "items." + name + ".item", replacements);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     return MenuParserItem.of(builder, slots, enabled);
/*     */   }
/*     */   
/*     */   public void placeCustomItems() {
/* 136 */     ConfigurationSection section = customItemsSection();
/*     */     
/* 138 */     for (String key : section.getKeys(false)) {
/* 139 */       if (section.isConfigurationSection(key))
/* 140 */         placeItem(customItem(key)); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\parser\menu\PaginatedMenuParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */