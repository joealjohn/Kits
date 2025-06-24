/*     */ package dev.continuum.kits.inventory;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import java.util.Map;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InventoryMapping
/*     */ {
/*  11 */   private Mapping armor = Mapping.of(new String[] { "39:0", "38:1", "37:2", "36:3" });
/*     */ 
/*     */   
/*  14 */   private Mapping offhand = Mapping.of(new String[] { "40:4" });
/*     */ 
/*     */   
/*  17 */   private Mapping contents = Mapping.of(new String[] { "9:9", "10:10", "11:11", "12:12", "13:13", "14:14", "15:15", "16:16", "17:17", "18:18", "19:19", "20:20", "21:21", "22:22", "23:23", "24:24", "25:25", "26:26", "27:27", "28:28", "29:29", "30:30", "31:31", "32:32", "33:33", "34:34", "35:35" });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  22 */   private Mapping hotbar = Mapping.of(new String[] { "0:36", "1:37", "2:38", "3:39", "4:40", "5:41", "6:42", "7:43", "8:44" });
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static InventoryMapping defaultMapping() {
/*  28 */     return mapping(
/*  29 */         Mapping.of(new String[] { "39:0", "38:1", "37:2", "36:3" }, ), Mapping.of(new String[] { "40:4"
/*  30 */           }, ), Mapping.of(new String[] { 
/*     */             "9:9", "10:10", "11:11", "12:12", "13:13", "14:14", "15:15", "16:16", "17:17", "18:18",
/*     */             
/*     */             "19:19", "20:20", "21:21", "22:22", "23:23", "24:24", "25:25", "26:26", "27:27", "28:28", 
/*     */             "29:29", "30:30", "31:31", "32:32", "33:33", "34:34", "35:35"
/*  35 */           }, ), Mapping.of(new String[] { "0:36", "1:37", "2:38", "3:39", "4:40", "5:41", "6:42", "7:43", "8:44" }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static InventoryMapping mapping(@NotNull Mapping armor, @NotNull Mapping offhand, @NotNull Mapping contents, @NotNull Mapping hotbar) {
/*  43 */     InventoryMapping mapping = new InventoryMapping();
/*     */     
/*  45 */     mapping.armor = armor;
/*  46 */     mapping.offhand = offhand;
/*  47 */     mapping.contents = contents;
/*  48 */     mapping.hotbar = hotbar;
/*     */     
/*  50 */     return mapping;
/*     */   }
/*     */   
/*     */   public static class Mapping {
/*     */     private final Cachable<Integer, Integer> mappings;
/*     */     
/*     */     public Mapping(@NotNull Cachable<Integer, Integer> mappings) {
/*  57 */       this.mappings = mappings;
/*     */     }
/*     */     
/*     */     public Mapping() {
/*  61 */       this.mappings = (Cachable<Integer, Integer>)Cachable.of(Integer.class, Integer.class);
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public static Mapping of(@NotNull String... mappings) {
/*  66 */       Mapping mapping = new Mapping();
/*     */       
/*  68 */       for (String mappingText : mappings) {
/*  69 */         String[] parts = mappingText.split(":");
/*     */         
/*  71 */         if (parts.length == 2) {
/*     */           try {
/*  73 */             int playerSlot = Integer.parseInt(parts[0]);
/*  74 */             int customSlot = Integer.parseInt(parts[1]);
/*     */             
/*  76 */             mapping.mappings.cache(Integer.valueOf(playerSlot), Integer.valueOf(customSlot));
/*  77 */           } catch (NumberFormatException numberFormatException) {}
/*     */         }
/*     */       } 
/*     */       
/*  81 */       return mapping;
/*     */     }
/*     */     
/*     */     public int convertToPlayerSlot(int customSlot) {
/*  85 */       for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)this.mappings.snapshot().asMap().entrySet()) {
/*  86 */         if (((Integer)entry.getValue()).intValue() == customSlot) {
/*  87 */           return ((Integer)entry.getKey()).intValue();
/*     */         }
/*     */       } 
/*     */       
/*  91 */       return -1;
/*     */     }
/*     */     
/*     */     public int convertToCustomSlot(int playerSlot) {
/*  95 */       return ((Integer)this.mappings.snapshot().asMap().getOrDefault(Integer.valueOf(playerSlot), Integer.valueOf(-1))).intValue();
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public Cachable<Integer, Integer> mappings() {
/* 100 */       return this.mappings;
/*     */     }
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Mapping armor() {
/* 106 */     return this.armor;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Mapping offhand() {
/* 111 */     return this.offhand;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Mapping contents() {
/* 116 */     return this.contents;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Mapping hotbar() {
/* 121 */     return this.hotbar;
/*     */   }
/*     */   
/*     */   public void armor(@NotNull Mapping armor) {
/* 125 */     this.armor = armor;
/*     */   }
/*     */   
/*     */   public void offhand(@NotNull Mapping offhand) {
/* 129 */     this.offhand = offhand;
/*     */   }
/*     */   
/*     */   public void contents(@NotNull Mapping contents) {
/* 133 */     this.contents = contents;
/*     */   }
/*     */   
/*     */   public void hotbar(@NotNull Mapping hotbar) {
/* 137 */     this.hotbar = hotbar;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\inventory\InventoryMapping.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */