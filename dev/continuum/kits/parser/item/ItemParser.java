/*     */ package dev.continuum.kits.parser.item;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.item.ItemBuilder;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import dev.continuum.kits.libs.utils.text.color.TextStyle;
/*     */ import io.papermc.paper.registry.RegistryAccess;
/*     */ import io.papermc.paper.registry.RegistryKey;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.inventory.ItemFlag;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ public class ItemParser
/*     */ {
/*     */   @NotNull
/*     */   public static ItemBuilder parse(@NotNull FileConfiguration configuration, @NotNull String path) {
/*  22 */     ConfigurationSection section = configuration.getConfigurationSection(path);
/*  23 */     if (section == null) return ItemBuilder.item(Material.AIR); 
/*  24 */     String rawMaterial = section.getString("material");
/*  25 */     if (rawMaterial == null) rawMaterial = "barrier";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  30 */     String stringMaterial = rawMaterial.replaceAll(" ", "_").replaceAll("minecraft:", "").toUpperCase();
/*     */     
/*  32 */     Material material = Material.matchMaterial(stringMaterial);
/*  33 */     if (material == null) material = Material.BARRIER;
/*     */     
/*  35 */     ItemBuilder builder = ItemBuilder.item(material);
/*     */     
/*  37 */     String skull = section.getString("skull");
/*     */     
/*  39 */     if (material == Material.PLAYER_HEAD && skull != null && 
/*  40 */       skull.startsWith("player:")) {
/*  41 */       String skullArg = skull.replaceFirst("player:", "");
/*  42 */       builder.skullOwner(skullArg);
/*     */     } 
/*     */ 
/*     */     
/*  46 */     String name = section.getString("name");
/*  47 */     if (name == null) name = path;
/*     */     
/*  49 */     Component displayName = TextStyle.style(name);
/*     */     
/*  51 */     builder.name(displayName);
/*     */     
/*  53 */     int amount = section.getInt("amount");
/*  54 */     if (amount == 0) amount = 1;
/*     */     
/*  56 */     builder.amount(amount);
/*     */     
/*  58 */     List<String> rawLore = section.getStringList("lore");
/*  59 */     List<Component> componentLore = TextStyle.style(rawLore);
/*     */     
/*  61 */     builder.lore(componentLore);
/*     */     
/*  63 */     boolean glow = section.getBoolean("glow");
/*     */     
/*  65 */     if (glow) { builder.glow(); }
/*  66 */     else { builder.removeGlow(); }
/*     */     
/*  68 */     ConfigurationSection enchantmentSection = section.getConfigurationSection("enchantments");
/*  69 */     if (enchantmentSection != null) {
/*  70 */       List<Tuple<Enchantment, Integer>> enchantmentsToAdd = new ArrayList<>();
/*     */       
/*  72 */       for (String key : enchantmentSection.getKeys(false)) {
/*  73 */         for (Enchantment possible : RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)) {
/*  74 */           if (possible.getKey().value().equals(key.toLowerCase())) {
/*  75 */             enchantmentsToAdd.add((enchantmentSection.getInt(key + ".level") == 0) ? 
/*  76 */                 Tuple.tuple(possible, Integer.valueOf(1)) : 
/*  77 */                 Tuple.tuple(possible, Integer.valueOf(enchantmentSection.getInt(key + ".level"))));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/*  82 */       for (Tuple<Enchantment, Integer> pair : enchantmentsToAdd) {
/*  83 */         Enchantment key = (Enchantment)pair.key();
/*  84 */         Integer val = (Integer)pair.val();
/*  85 */         if (key == null || val == null)
/*     */           continue; 
/*  87 */         builder.addUnsafeEnchantment(key, val.intValue());
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     List<String> rawFlags = section.getStringList("flags");
/*  92 */     for (String rawFlag : rawFlags) {
/*  93 */       ItemFlag flag; if (rawFlag.equals("*")) {
/*  94 */         rawFlags.clear();
/*     */         
/*  96 */         for (ItemFlag itemFlag : ItemFlag.values()) {
/*  97 */           builder.removeFlag(itemFlag);
/*     */         }
/*     */         
/* 100 */         for (ItemFlag itemFlag : ItemFlag.values()) {
/* 101 */           builder.addFlag(itemFlag);
/*     */         }
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 110 */         flag = ItemFlag.valueOf(rawFlag.toUpperCase());
/* 111 */       } catch (IllegalArgumentException e) {
/*     */         continue;
/*     */       } 
/*     */       
/* 115 */       builder.addFlag(flag);
/*     */     } 
/*     */     
/* 118 */     Integer customModelData = (Integer)section.get("custom_model_data");
/* 119 */     if (customModelData != null && section.getKeys(false).contains("custom_model_data")) {
/* 120 */       builder.customModelData(customModelData.intValue());
/*     */     }
/*     */     
/* 123 */     boolean unbreakable = section.getBoolean("unbreakable");
/* 124 */     if (unbreakable) builder.unbreakable();
/*     */     
/* 126 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static ItemBuilder parse(@NotNull FileConfiguration configuration, @NotNull String path, @NotNull List<Tuple<String, String>> replacements) {
/* 133 */     ConfigurationSection section = configuration.getConfigurationSection(path);
/* 134 */     if (section == null) return ItemBuilder.item(Material.AIR); 
/* 135 */     String rawMaterial = section.getString("material");
/* 136 */     if (rawMaterial == null) rawMaterial = "barrier";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     String stringMaterial = rawMaterial.replaceAll(" ", "_").replaceAll("minecraft:", "").toUpperCase();
/*     */     
/* 143 */     Material material = Material.matchMaterial(stringMaterial);
/* 144 */     if (material == null) material = Material.BARRIER;
/*     */     
/* 146 */     ItemBuilder builder = ItemBuilder.item(material);
/*     */     
/* 148 */     String skull = section.getString("skull");
/*     */     
/* 150 */     if (material == Material.PLAYER_HEAD && skull != null && 
/* 151 */       skull.startsWith("uuid:")) {
/* 152 */       String skullArg = skull.replaceAll("uuid:", "");
/*     */       
/* 154 */       for (Tuple<String, String> replacementPair : replacements) {
/* 155 */         String find = (String)replacementPair.key();
/* 156 */         String replace = (String)replacementPair.val();
/*     */         
/* 158 */         if (find == null || replace == null)
/*     */           continue; 
/* 160 */         skullArg = skullArg.replaceAll("<" + find + ">", replace);
/*     */       } 
/*     */       
/* 163 */       builder.skullOwner(skullArg);
/*     */     } 
/*     */ 
/*     */     
/* 167 */     String name = section.getString("name");
/* 168 */     if (name == null) name = path;
/*     */     
/* 170 */     for (Tuple<String, String> replacementPair : replacements) {
/* 171 */       String find = (String)replacementPair.key();
/* 172 */       String replace = (String)replacementPair.val();
/*     */       
/* 174 */       if (find == null || replace == null)
/*     */         continue; 
/* 176 */       name = name.replaceAll("<" + find + ">", replace);
/*     */     } 
/*     */     
/* 179 */     Component displayName = TextStyle.style(name);
/*     */     
/* 181 */     builder.name(displayName);
/*     */     
/* 183 */     int amount = section.getInt("amount");
/* 184 */     if (amount == 0) amount = 1;
/*     */     
/* 186 */     builder.amount(amount);
/*     */     
/* 188 */     List<String> rawLore = section.getStringList("lore");
/* 189 */     List<String> copiedLore = new ArrayList<>();
/*     */     
/* 191 */     for (String line : rawLore) {
/* 192 */       for (Tuple<String, String> replacementPair : replacements) {
/* 193 */         String find = (String)replacementPair.key();
/* 194 */         String replace = (String)replacementPair.val();
/*     */         
/* 196 */         if (find == null || replace == null)
/*     */           continue; 
/* 198 */         line = line.replaceAll("<" + find + ">", replace);
/*     */       } 
/*     */       
/* 201 */       copiedLore.add(line);
/*     */     } 
/*     */     
/* 204 */     List<Component> componentLore = TextStyle.style(copiedLore);
/*     */     
/* 206 */     builder.lore(componentLore);
/*     */     
/* 208 */     boolean glow = section.getBoolean("glow");
/*     */     
/* 210 */     if (glow) { builder.glow(); }
/* 211 */     else { builder.removeGlow(); }
/*     */     
/* 213 */     ConfigurationSection enchantmentSection = section.getConfigurationSection("enchantments");
/* 214 */     if (enchantmentSection != null) {
/* 215 */       List<Tuple<Enchantment, Integer>> enchantmentsToAdd = new ArrayList<>();
/*     */       
/* 217 */       for (String key : enchantmentSection.getKeys(false)) {
/* 218 */         for (Enchantment possible : RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)) {
/* 219 */           if (possible.getKey().value().equals(key.toLowerCase())) {
/* 220 */             enchantmentsToAdd.add((enchantmentSection.getInt(key + ".level") == 0) ? 
/* 221 */                 Tuple.tuple(possible, Integer.valueOf(1)) : 
/* 222 */                 Tuple.tuple(possible, Integer.valueOf(enchantmentSection.getInt(key + ".level"))));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 227 */       for (Tuple<Enchantment, Integer> pair : enchantmentsToAdd) {
/* 228 */         Enchantment key = (Enchantment)pair.key();
/* 229 */         Integer val = (Integer)pair.val();
/* 230 */         if (key == null || val == null)
/*     */           continue; 
/* 232 */         builder.addUnsafeEnchantment(key, val.intValue());
/*     */       } 
/*     */     } 
/*     */     
/* 236 */     List<String> rawFlags = section.getStringList("flags");
/* 237 */     for (String rawFlag : rawFlags) {
/*     */       ItemFlag flag;
/*     */       
/*     */       try {
/* 241 */         flag = ItemFlag.valueOf(rawFlag.toUpperCase());
/* 242 */       } catch (IllegalArgumentException e) {
/*     */         continue;
/*     */       } 
/*     */       
/* 246 */       builder.addFlag(flag);
/*     */     } 
/*     */     
/* 249 */     Integer customModelData = (Integer)section.get("custom_model_data");
/* 250 */     if (customModelData != null && section.getKeys(false).contains("custom_model_data")) {
/* 251 */       builder.customModelData(customModelData.intValue());
/*     */     }
/*     */     
/* 254 */     boolean unbreakable = section.getBoolean("unbreakable");
/* 255 */     if (unbreakable) builder.unbreakable();
/*     */     
/* 257 */     return builder;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\parser\item\ItemParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */