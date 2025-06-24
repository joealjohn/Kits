/*     */ package dev.continuum.kits.libs.utils.serializers;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.item.ItemBuilder;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Base64;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.util.io.BukkitObjectInputStream;
/*     */ import org.bukkit.util.io.BukkitObjectOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Base64Serializer
/*     */ {
/*     */   public String serializeItemStacks(Map<Integer, ItemStack> itemStacks) {
/*  24 */     ItemStack[] items = (ItemStack[])itemStacks.values().toArray((Object[])new ItemStack[0]);
/*     */     
/*  26 */     List<String> serializedItems = new ArrayList<>();
/*  27 */     for (ItemStack item : items) {
/*  28 */       String serialized = serializeItemStack(item);
/*  29 */       serializedItems.add(serialized);
/*     */     } 
/*     */     
/*  32 */     return String.join(";", (Iterable)serializedItems);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String serializeItemBuilders(Map<Integer, ItemBuilder> itemBuilders) {
/*  42 */     Map<Integer, ItemStack> itemStacks = new HashMap<>();
/*     */     
/*  44 */     for (int i = 0; i < itemBuilders.size(); i++) {
/*  45 */       for (ItemBuilder builder : itemBuilders.values()) {
/*  46 */         ItemStack itemStack = builder.build();
/*     */         
/*  48 */         itemStacks.put(Integer.valueOf(i), itemStack);
/*     */       } 
/*     */     } 
/*     */     
/*  52 */     return serializeItemStacks(itemStacks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String serializeItemStacks(List<ItemStack> itemStacks) {
/*  62 */     Map<Integer, ItemStack> itemStacksMap = new HashMap<>();
/*     */     
/*  64 */     for (int i = 0; i < itemStacks.size(); i++) {
/*  65 */       for (ItemStack stack : itemStacks) {
/*  66 */         itemStacksMap.put(Integer.valueOf(i), stack);
/*     */       }
/*     */     } 
/*     */     
/*  70 */     return serializeItemStacks(itemStacksMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String serializeItemBuilders(List<ItemBuilder> itemBuilders) {
/*  80 */     Map<Integer, ItemStack> itemStacksMap = new HashMap<>();
/*     */     
/*  82 */     for (int i = 0; i < itemBuilders.size(); i++) {
/*  83 */       for (ItemBuilder itemBuilder : itemBuilders) {
/*  84 */         ItemStack itemStack = itemBuilder.build();
/*  85 */         itemStacksMap.put(Integer.valueOf(i), itemStack);
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     return serializeItemStacks(itemStacksMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String serializeItemStacks(ItemStack[] itemStacks) {
/*  99 */     List<ItemStack> itemStacksList = Arrays.<ItemStack>stream(itemStacks).toList();
/*     */     
/* 101 */     return serializeItemStacks(itemStacksList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String serializeItemStacks(ItemBuilder[] itemBuilders) {
/* 111 */     List<ItemBuilder> itemBuildersList = Arrays.<ItemBuilder>stream(itemBuilders).toList();
/*     */     
/* 113 */     return serializeItemBuilders(itemBuildersList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Integer, ItemStack> deserializeItemStackMap(String data) {
/* 123 */     Map<Integer, ItemStack> kitContents = new HashMap<>();
/* 124 */     String[] serializedItems = data.split(";");
/*     */     
/* 126 */     for (int i = 0; i < serializedItems.length; i++) {
/* 127 */       ItemStack item = deserialize(serializedItems[i]);
/* 128 */       kitContents.put(Integer.valueOf(i), item);
/*     */     } 
/*     */     
/* 131 */     return kitContents;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Integer, ItemBuilder> deserializeItemBuilderMap(String data) {
/* 141 */     Map<Integer, ItemBuilder> itemBuilders = new HashMap<>();
/*     */     
/* 143 */     for (int i = 0; i < deserializeItemStackMap(data).size(); i++) {
/* 144 */       for (ItemStack itemStack : deserializeItemStackMap(data).values()) {
/* 145 */         itemBuilders.put(Integer.valueOf(i), ItemBuilder.item(itemStack));
/*     */       }
/*     */     } 
/*     */     
/* 149 */     return itemBuilders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ItemStack> deserializeItemStackList(String data) {
/* 159 */     return deserializeItemStackMap(data).values().stream().toList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ItemBuilder> deserializeItemBuilderList(String data) {
/* 169 */     return deserializeItemBuilderMap(data).values().stream().toList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String serializeItemStack(ItemStack itemStack) {
/* 179 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*     */ 
/*     */     
/*     */     try {
/* 183 */       BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
/* 184 */       bukkitObjectOutputStream.writeObject(itemStack);
/* 185 */       bukkitObjectOutputStream.close();
/*     */       
/* 187 */       return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
/* 188 */     } catch (IOException e) {
/* 189 */       e.printStackTrace();
/*     */ 
/*     */       
/* 192 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String serializeItemBuilder(ItemBuilder itemBuilder) {
/* 202 */     return serializeItemStack(itemBuilder.build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack deserialize(String data) {
/* 212 */     byte[] bytes = Base64.getDecoder().decode(data);
/* 213 */     ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
/*     */     
/*     */     try {
/* 216 */       BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);
/* 217 */       ItemStack itemStack = (ItemStack)bukkitObjectInputStream.readObject();
/* 218 */       bukkitObjectInputStream.close();
/*     */       
/* 220 */       return itemStack;
/* 221 */     } catch (IOException|ClassNotFoundException e) {
/* 222 */       e.printStackTrace();
/*     */ 
/*     */       
/* 225 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\serializers\Base64Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */