/*     */ package dev.continuum.kits.libs.utils.resource;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.library.Utils;
/*     */ import dev.continuum.kits.libs.utils.resource.format.ResourceFormat;
/*     */ import dev.continuum.kits.libs.utils.resource.format.ResourceFormats;
/*     */ import dev.continuum.kits.libs.utils.resource.path.ResourcePath;
/*     */ import dev.continuum.kits.libs.utils.resource.yaml.ResourceConfiguration;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.scheduler.BukkitTask;
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
/*     */ public class ResourceFile
/*     */ {
/*     */   private ResourceFormat resourceFormat;
/*     */   private final ResourceOptions recourseOptions;
/*     */   private final ResourcePath resourcePath;
/*     */   private final String name;
/*     */   
/*     */   public ResourceFile(String name, ResourceFormat format, ResourceOptions options, ResourcePath path) {
/*  34 */     this.resourceFormat = format;
/*  35 */     this.recourseOptions = options;
/*  36 */     this.resourcePath = path;
/*  37 */     this.name = name;
/*     */     
/*  39 */     if (!this.resourceFormat.extension().contains(".")) {
/*  40 */       this.resourceFormat = ResourceFormat.format("." + this.resourceFormat.extension());
/*     */     }
/*     */   }
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
/*     */   public static ResourceFile resource(String name, ResourceFormat format, ResourceOptions options, ResourcePath path) {
/*  54 */     return new ResourceFile(name, format, options, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ResourceBuilder builder() {
/*  63 */     return ResourceBuilder.builder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceConfiguration yaml() {
/*  72 */     return new ResourceConfiguration(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileConfiguration loadYml() {
/*  81 */     return (FileConfiguration)Schedulers.async().supply(() -> YamlConfiguration.loadConfiguration(file()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileConfiguration reloadYml() {
/*  90 */     return loadYml();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveYml() {
/*  97 */     if (Objects.equals(format(), ResourceFormats.yaml()) || Objects.equals(format(), ResourceFormats.yml())) {
/*  98 */       Schedulers.async().execute(task -> {
/*     */             try {
/*     */               file().mkdirs();
/*     */               
/*     */               file().createNewFile();
/*     */               YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file());
/*     */               yamlConfiguration.save(file());
/* 105 */             } catch (IOException e) {
/*     */               throw new RuntimeException(e);
/*     */             } 
/*     */           });
/*     */     } else {
/* 110 */       throw new UnsupportedOperationException("only yaml files are supported for saveOrLoad()");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceFile saveOrLoadYml() {
/* 120 */     if (Objects.equals(format(), ResourceFormats.yaml()) || Objects.equals(format(), ResourceFormats.yml())) {
/* 121 */       Schedulers.async().execute(task -> {
/*     */             if (file().exists()) {
/*     */               YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(file());
/*     */               
/*     */               return;
/*     */             } 
/*     */             
/*     */             if (Utils.plugin().getResource(this.name + this.name) != null) {
/*     */               Utils.plugin().saveResource(this.resourcePath.path().replaceAll(Utils.plugin().getDataFolder().getPath(), "") + this.resourcePath.path().replaceAll(Utils.plugin().getDataFolder().getPath(), "") + this.name, false);
/*     */             }
/*     */             try {
/*     */               if (!file().createNewFile()) {
/*     */                 Utils.plugin().getLogger().severe("Failed to create resource: " + file().getPath());
/*     */                 task.cancel();
/*     */               } 
/* 136 */             } catch (IOException e) {
/*     */               throw new RuntimeException(e);
/*     */             } 
/*     */ 
/*     */             
/*     */             YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file());
/*     */ 
/*     */             
/*     */             yamlConfiguration.options().copyDefaults(true).parseComments(true).pathSeparator('.');
/*     */ 
/*     */             
/*     */             if (this.recourseOptions.header() != null) {
/*     */               yamlConfiguration.options().setHeader(this.recourseOptions.header());
/*     */             }
/*     */             
/*     */             if (this.recourseOptions.footer() != null) {
/*     */               yamlConfiguration.options().setFooter(this.recourseOptions.footer());
/*     */             }
/*     */             
/*     */             try {
/*     */               yamlConfiguration.save(file());
/* 157 */             } catch (IOException e) {
/*     */               throw new RuntimeException(e);
/*     */             } 
/*     */           });
/*     */     } else {
/* 162 */       throw new UnsupportedOperationException("only yaml files are supported for saveOrLoad()");
/*     */     } 
/*     */     
/* 165 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File file() {
/* 174 */     return new File(new File(this.resourcePath.path()), this.name + this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceFormat format() {
/* 183 */     return this.resourceFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceOptions options() {
/* 192 */     return this.recourseOptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePath path() {
/* 201 */     return this.resourcePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/* 210 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\resource\ResourceFile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */