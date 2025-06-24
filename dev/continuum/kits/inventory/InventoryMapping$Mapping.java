/*     */ package dev.continuum.kits.inventory;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import java.util.Map;
/*     */ import org.jetbrains.annotations.NotNull;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Mapping
/*     */ {
/*     */   private final Cachable<Integer, Integer> mappings;
/*     */   
/*     */   public Mapping(@NotNull Cachable<Integer, Integer> mappings) {
/*  57 */     this.mappings = mappings;
/*     */   }
/*     */   
/*     */   public Mapping() {
/*  61 */     this.mappings = (Cachable<Integer, Integer>)Cachable.of(Integer.class, Integer.class);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public static Mapping of(@NotNull String... mappings) {
/*  66 */     Mapping mapping = new Mapping();
/*     */     
/*  68 */     for (String mappingText : mappings) {
/*  69 */       String[] parts = mappingText.split(":");
/*     */       
/*  71 */       if (parts.length == 2) {
/*     */         try {
/*  73 */           int playerSlot = Integer.parseInt(parts[0]);
/*  74 */           int customSlot = Integer.parseInt(parts[1]);
/*     */           
/*  76 */           mapping.mappings.cache(Integer.valueOf(playerSlot), Integer.valueOf(customSlot));
/*  77 */         } catch (NumberFormatException numberFormatException) {}
/*     */       }
/*     */     } 
/*     */     
/*  81 */     return mapping;
/*     */   }
/*     */   
/*     */   public int convertToPlayerSlot(int customSlot) {
/*  85 */     for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)this.mappings.snapshot().asMap().entrySet()) {
/*  86 */       if (((Integer)entry.getValue()).intValue() == customSlot) {
/*  87 */         return ((Integer)entry.getKey()).intValue();
/*     */       }
/*     */     } 
/*     */     
/*  91 */     return -1;
/*     */   }
/*     */   
/*     */   public int convertToCustomSlot(int playerSlot) {
/*  95 */     return ((Integer)this.mappings.snapshot().asMap().getOrDefault(Integer.valueOf(playerSlot), Integer.valueOf(-1))).intValue();
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Cachable<Integer, Integer> mappings() {
/* 100 */     return this.mappings;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\inventory\InventoryMapping$Mapping.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */