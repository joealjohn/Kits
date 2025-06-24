/*     */ package dev.continuum.kits.libs.utils.item;
/*     */ 
/*     */ import com.google.common.collect.Multimap;
/*     */ import dev.continuum.kits.libs.utils.library.Utils;
/*     */ import dev.continuum.kits.libs.utils.player.PlayerUtils;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import dev.continuum.kits.libs.utils.text.color.TextStyle;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.attribute.Attribute;
/*     */ import org.bukkit.attribute.AttributeModifier;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemFlag;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.BannerMeta;
/*     */ import org.bukkit.inventory.meta.BookMeta;
/*     */ import org.bukkit.inventory.meta.Damageable;
/*     */ import org.bukkit.inventory.meta.FireworkMeta;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.inventory.meta.LeatherArmorMeta;
/*     */ import org.bukkit.inventory.meta.SkullMeta;
/*     */ import org.bukkit.persistence.PersistentDataContainer;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.profile.PlayerProfile;
/*     */ 
/*     */ public class ItemBuilder
/*     */ {
/*     */   private final ItemStack itemStack;
/*     */   
/*     */   public ItemBuilder(Material material) {
/*  40 */     this(material, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder(ItemStack itemStack) {
/*  49 */     this.itemStack = itemStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder(Material material, int amount) {
/*  59 */     this.itemStack = new ItemStack(material, amount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemBuilder item(ItemStack itemStack) {
/*  68 */     return new ItemBuilder(itemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemBuilder item(Material material) {
/*  77 */     return new ItemBuilder(material);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemBuilder item(Material material, int amount) {
/*  87 */     return new ItemBuilder(material, amount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder clone() {
/*     */     try {
/*  97 */       ItemBuilder itemBuilder = (ItemBuilder)super.clone();
/*  98 */     } catch (CloneNotSupportedException e) {
/*  99 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 102 */     return new ItemBuilder(this.itemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder name(Component name) {
/* 112 */     ItemMeta itemMeta = this.itemStack.getItemMeta();
/*     */     
/* 114 */     if (itemMeta != null) {
/* 115 */       itemMeta.displayName(name);
/*     */     }
/*     */     
/* 118 */     this.itemStack.setItemMeta(itemMeta);
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   public ItemBuilder name(String name) {
/* 123 */     return name(TextStyle.style(name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder rawName(String name) {
/* 134 */     ItemMeta itemMeta = this.itemStack.getItemMeta();
/*     */     
/* 136 */     if (itemMeta != null) {
/* 137 */       itemMeta.setDisplayName(name);
/*     */     }
/*     */     
/* 140 */     this.itemStack.setItemMeta(itemMeta);
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
/* 151 */     this.itemStack.addUnsafeEnchantment(enchantment, level);
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder removeEnchantment(Enchantment enchantment) {
/* 161 */     this.itemStack.removeEnchantment(enchantment);
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder skullOwner(String playerName) {
/* 173 */     SkullMeta itemMeta = (SkullMeta)this.itemStack.getItemMeta();
/*     */     
/* 175 */     if (itemMeta != null) {
/* 176 */       Schedulers.async().execute(() -> itemMeta.setOwnerProfile((PlayerProfile)Bukkit.getOfflinePlayer(UUID.fromString(playerName)).getPlayerProfile()));
/*     */     }
/*     */     
/* 179 */     this.itemStack.setItemMeta((ItemMeta)itemMeta);
/* 180 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder skullOwner(Player player) {
/* 191 */     SkullMeta itemMeta = (SkullMeta)this.itemStack.getItemMeta();
/*     */     
/* 193 */     if (itemMeta != null) {
/* 194 */       Schedulers.async().execute(() -> itemMeta.setOwnerProfile((PlayerProfile)player.getPlayerProfile()));
/*     */     }
/*     */     
/* 197 */     this.itemStack.setItemMeta((ItemMeta)itemMeta);
/* 198 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder skullOwner(OfflinePlayer player) {
/* 209 */     SkullMeta itemMeta = (SkullMeta)this.itemStack.getItemMeta();
/*     */     
/* 211 */     if (itemMeta != null) {
/*     */ 
/*     */       
/* 214 */       Objects.requireNonNull(itemMeta); player.getPlayerProfile().update().thenAcceptAsync(itemMeta::setOwnerProfile, runnable -> Bukkit.getScheduler().runTask((Plugin)Utils.plugin(), runnable));
/*     */     } 
/*     */ 
/*     */     
/* 218 */     this.itemStack.setItemMeta((ItemMeta)itemMeta);
/* 219 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder skullOwner(UUID uuid) {
/* 229 */     return skullOwner(PlayerUtils.offline(uuid));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
/* 240 */     ItemMeta itemMeta = this.itemStack.getItemMeta();
/*     */     
/* 242 */     if (itemMeta != null) {
/* 243 */       itemMeta.addEnchant(enchantment, level, true);
/*     */     }
/*     */     
/* 246 */     this.itemStack.setItemMeta(itemMeta);
/* 247 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
/* 257 */     this.itemStack.addEnchantments(enchantments);
/* 258 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder lore(Component... lore) {
/* 268 */     ItemMeta itemMeta = this.itemStack.getItemMeta();
/*     */     
/* 270 */     if (itemMeta != null) {
/* 271 */       itemMeta.lore(List.of(lore));
/*     */     }
/*     */     
/* 274 */     this.itemStack.setItemMeta(itemMeta);
/* 275 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder lore(List<Component> lore) {
/* 285 */     ItemMeta itemMeta = this.itemStack.getItemMeta();
/*     */     
/* 287 */     if (itemMeta != null) {
/* 288 */       itemMeta.lore(lore);
/*     */     }
/*     */     
/* 291 */     this.itemStack.setItemMeta(itemMeta);
/* 292 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder amount(int amount) {
/* 302 */     this.itemStack.setAmount(amount);
/* 303 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder durability(int damage) {
/* 313 */     ItemMeta itemMeta = this.itemStack.getItemMeta(); if (itemMeta instanceof Damageable) { Damageable damageable = (Damageable)itemMeta;
/* 314 */       damageable.setDamage(damage);
/* 315 */       this.itemStack.setItemMeta((ItemMeta)damageable); }
/*     */     else
/* 317 */     { throw new IllegalArgumentException("ItemMeta is required to be an instance of Damageable to set the durability!"); }
/*     */ 
/*     */     
/* 320 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder addFlag(ItemFlag flag) {
/* 330 */     ItemMeta itemMeta = this.itemStack.getItemMeta();
/*     */     
/* 332 */     if (itemMeta != null) {
/* 333 */       itemMeta.addItemFlags(new ItemFlag[] { flag });
/* 334 */       this.itemStack.setItemMeta(itemMeta);
/*     */     } 
/*     */     
/* 337 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder removeFlag(ItemFlag flag) {
/* 347 */     ItemMeta itemMeta = this.itemStack.getItemMeta();
/*     */     
/* 349 */     if (itemMeta != null) {
/* 350 */       itemMeta.removeItemFlags(new ItemFlag[] { flag });
/* 351 */       this.itemStack.setItemMeta(itemMeta);
/*     */     } 
/*     */     
/* 354 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder glow() {
/* 363 */     addUnsafeEnchantment(Enchantment.LUCK_OF_THE_SEA, 1);
/* 364 */     addFlag(ItemFlag.HIDE_ENCHANTS);
/* 365 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder removeGlow() {
/* 374 */     removeEnchantment(Enchantment.LUCK_OF_THE_SEA);
/* 375 */     removeFlag(ItemFlag.HIDE_ENCHANTS);
/* 376 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder unbreakable() {
/* 385 */     ItemMeta meta = this.itemStack.getItemMeta();
/*     */     
/* 387 */     if (meta != null) {
/* 388 */       meta.setUnbreakable(true);
/*     */     }
/*     */     
/* 391 */     this.itemStack.setItemMeta(meta);
/* 392 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder breakable() {
/* 401 */     ItemMeta meta = this.itemStack.getItemMeta();
/*     */     
/* 403 */     if (meta != null) {
/* 404 */       meta.setUnbreakable(false);
/*     */     }
/*     */     
/* 407 */     this.itemStack.setItemMeta(meta);
/* 408 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder customModelData(int data) {
/* 418 */     ItemMeta meta = this.itemStack.getItemMeta();
/*     */     
/* 420 */     if (meta != null) {
/* 421 */       meta.setCustomModelData(Integer.valueOf(data));
/*     */     }
/*     */     
/* 424 */     this.itemStack.setItemMeta(meta);
/* 425 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder meta(ItemMeta itemMeta) {
/* 435 */     this.itemStack.setItemMeta(itemMeta);
/* 436 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemMeta meta() {
/* 445 */     return this.itemStack.getItemMeta();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PersistentDataContainer pdc() {
/* 454 */     return this.itemStack.getItemMeta().getPersistentDataContainer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder editMeta(Consumer<ItemMeta> metaConsumer) {
/* 464 */     this.itemStack.editMeta(metaConsumer);
/* 465 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder editStack(Consumer<ItemStack> stackConsumer) {
/* 475 */     stackConsumer.accept(this.itemStack);
/* 476 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder editPdc(Consumer<PersistentDataContainer> pdcConsumer) {
/* 486 */     pdcConsumer.accept(this.itemStack.getItemMeta().getPersistentDataContainer());
/* 487 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder from(ItemStack itemStack) {
/* 497 */     return new ItemBuilder(itemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder bookData(BookMeta bookMeta) {
/* 507 */     this.itemStack.setItemMeta((ItemMeta)bookMeta);
/* 508 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder bannerData(BannerMeta bannerMeta) {
/* 518 */     this.itemStack.setItemMeta((ItemMeta)bannerMeta);
/* 519 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder leatherArmorColor(Color color) {
/* 529 */     LeatherArmorMeta meta = (LeatherArmorMeta)this.itemStack.getItemMeta();
/*     */     
/* 531 */     if (meta != null) meta.setColor(color);
/*     */     
/* 533 */     this.itemStack.setItemMeta((ItemMeta)meta);
/* 534 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder fireworkData(FireworkMeta meta) {
/* 544 */     this.itemStack.setItemMeta((ItemMeta)meta);
/* 545 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder addAttribute(Attribute attribute, AttributeModifier modifier) {
/* 556 */     ItemMeta meta = this.itemStack.getItemMeta();
/*     */     
/* 558 */     if (meta != null) meta.addAttributeModifier(attribute, modifier);
/*     */     
/* 560 */     this.itemStack.setItemMeta(meta);
/* 561 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder clearAttributes() {
/* 570 */     ItemMeta meta = this.itemStack.getItemMeta();
/*     */     
/* 572 */     if (meta != null) {
/* 573 */       Objects.requireNonNull(meta); ((Multimap)Objects.<Multimap>requireNonNull(meta.getAttributeModifiers())).forEach(meta::removeAttributeModifier);
/*     */     } 
/* 575 */     this.itemStack.setItemMeta(meta);
/* 576 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder hideAttributes() {
/* 585 */     ItemMeta meta = this.itemStack.getItemMeta();
/*     */     
/* 587 */     if (meta != null) {
/* 588 */       meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
/*     */     }
/*     */     
/* 591 */     this.itemStack.setItemMeta(meta);
/* 592 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack build() {
/* 601 */     return this.itemStack;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\item\ItemBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */