/*     */ package dev.continuum.kits.libs.utils.serializers;
/*     */ 
/*     */ import com.google.common.io.ByteArrayDataInput;
/*     */ import com.google.common.io.ByteArrayDataOutput;
/*     */ import com.google.common.io.ByteStreams;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.inventory.ItemStack;
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
/*     */ public class ByteSerializer
/*     */ {
/*     */   public byte[] serialize(ItemStack item) {
/*  26 */     return item.serializeAsBytes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] serialize(List<ItemStack> items) {
/*  36 */     return serialize(items.<ItemStack>toArray(new ItemStack[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] serialize(ItemStack[] items) {
/*  46 */     ByteArrayDataOutput stream = ByteStreams.newDataOutput();
/*     */     
/*  48 */     for (ItemStack item : items) {
/*  49 */       byte[] data = (item == null || item.getType() == Material.AIR) ? new byte[0] : item.serializeAsBytes();
/*     */       
/*  51 */       stream.writeInt(data.length);
/*  52 */       stream.write(data);
/*     */     } 
/*     */     
/*  55 */     stream.writeInt(-1);
/*  56 */     return stream.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack deserialize(byte[] bytes) {
/*  66 */     return ItemStack.deserializeBytes(bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack[] deserializeArray(byte[] bytes) {
/*  76 */     List<ItemStack> items = new ArrayList<>();
/*  77 */     ByteArrayDataInput inputStream = ByteStreams.newDataInput(bytes);
/*  78 */     int length = inputStream.readInt();
/*     */     
/*  80 */     while (length != -1) {
/*  81 */       if (length == 0) {
/*  82 */         items.add(null);
/*     */       } else {
/*  84 */         byte[] data = new byte[length];
/*     */         
/*  86 */         inputStream.readFully(data, 0, data.length);
/*  87 */         items.add(ItemStack.deserializeBytes(data));
/*     */       } 
/*     */       
/*  90 */       length = inputStream.readInt();
/*     */     } 
/*     */     
/*  93 */     return items.<ItemStack>toArray(new ItemStack[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ItemStack> deserializeList(byte[] bytes) {
/* 103 */     return Arrays.asList(deserializeArray(bytes));
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\serializers\ByteSerializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */