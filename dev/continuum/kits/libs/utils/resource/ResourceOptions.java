/*     */ package dev.continuum.kits.libs.utils.resource;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ public class ResourceOptions
/*     */ {
/*     */   private List<String> header;
/*     */   private List<String> footer;
/*     */   private boolean defaults;
/*     */   
/*     */   public ResourceOptions(List<String> header, List<String> footer, boolean defaults) {
/*  22 */     this.header = header;
/*  23 */     this.footer = footer;
/*  24 */     this.defaults = defaults;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceOptions() {
/*  31 */     this.header = null;
/*  32 */     this.footer = null;
/*  33 */     this.defaults = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> header() {
/*  42 */     return this.header;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> footer() {
/*  51 */     return this.footer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean defaults() {
/*  60 */     return this.defaults;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceOptions header(List<String> header) {
/*  70 */     this.header = header;
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceOptions footer(List<String> footer) {
/*  81 */     this.footer = footer;
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceOptions header(String... header) {
/*  92 */     return header(Arrays.asList(header));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceOptions footer(String... footer) {
/* 102 */     return footer(Arrays.asList(footer));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceOptions defaults(boolean defaults) {
/* 112 */     this.defaults = defaults;
/* 113 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\resource\ResourceOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */