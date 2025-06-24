/*     */ package dev.continuum.kits.libs.utils.resource;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.resource.format.ResourceFormat;
/*     */ import dev.continuum.kits.libs.utils.resource.format.ResourceFormats;
/*     */ import dev.continuum.kits.libs.utils.resource.path.ResourcePath;
/*     */ import dev.continuum.kits.libs.utils.resource.path.ResourcePaths;
/*     */ import java.util.function.Consumer;
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
/*     */ public class ResourceBuilder
/*     */ {
/*  24 */   private ResourceFormat format = ResourceFormats.yaml();
/*  25 */   private final ResourceOptions options = new ResourceOptions();
/*  26 */   private String name = null;
/*  27 */   private ResourcePath path = ResourcePaths.plugin();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ResourceBuilder builder() {
/*  36 */     return new ResourceBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceFormat format() {
/*  45 */     return this.format;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceBuilder format(ResourceFormat format) {
/*  55 */     this.format = format;
/*  56 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceOptions options() {
/*  65 */     return this.options;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceBuilder options(Consumer<ResourceOptions> optionsConsumer) {
/*  75 */     optionsConsumer.accept(this.options);
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/*  85 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceBuilder name(String name) {
/*  95 */     this.name = name;
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePath path() {
/* 105 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceBuilder path(ResourcePath path) {
/* 115 */     this.path = path;
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceFile build() {
/* 125 */     return ResourceFile.resource(this.name, this.format, this.options, this.path);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\resource\ResourceBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */