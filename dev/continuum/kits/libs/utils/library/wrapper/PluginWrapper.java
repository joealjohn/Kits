/*     */ package dev.continuum.kits.libs.utils.library.wrapper;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.command.impl.Commands;
/*     */ import dev.continuum.kits.libs.utils.library.Utils;
/*     */ import dev.continuum.kits.libs.utils.library.event.PluginEvents;
/*     */ import dev.continuum.kits.libs.utils.resource.ResourceFile;
/*     */ import dev.continuum.kits.libs.utils.resource.ResourceOptions;
/*     */ import dev.continuum.kits.libs.utils.resource.format.ResourceFormat;
/*     */ import dev.continuum.kits.libs.utils.resource.format.ResourceFormats;
/*     */ import dev.continuum.kits.libs.utils.resource.path.ResourcePath;
/*     */ import dev.continuum.kits.libs.utils.resource.path.ResourcePaths;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.logging.Level;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
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
/*     */ public abstract class PluginWrapper
/*     */   extends JavaPlugin
/*     */   implements Listener
/*     */ {
/*     */   protected void load() {}
/*     */   
/*     */   protected void stop() {}
/*     */   
/*     */   public final void onLoad() {
/*  65 */     load();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void onDisable() {
/*  70 */     stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void onEnable() {
/*  75 */     Utils.init(this);
/*     */     
/*  77 */     start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void log(int priority, @NotNull String text) {
/*  88 */     if (priority >= 0 && priority < 300) {
/*  89 */       getLogger().log(Level.FINEST, text);
/*  90 */     } else if (priority >= 300 && priority < 400) {
/*  91 */       getLogger().log(Level.FINER, text);
/*  92 */     } else if (priority >= 400 && priority < 500) {
/*  93 */       getLogger().log(Level.FINE, text);
/*  94 */     } else if (priority >= 500 && priority < 700) {
/*  95 */       getLogger().log(Level.CONFIG, text);
/*  96 */     } else if (priority >= 700 && priority < 800) {
/*  97 */       getLogger().log(Level.INFO, text);
/*  98 */     } else if (priority >= 800 && priority < 900) {
/*  99 */       getLogger().log(Level.WARNING, text);
/* 100 */     } else if (priority >= 900 && priority < 1000) {
/* 101 */       getLogger().log(Level.SEVERE, text);
/*     */     } else {
/* 103 */       throw new IllegalArgumentException("Invalid priority integer: " + priority + ". Refer to the integer values of java.util.logging.Level");
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
/*     */   public final void log(@NotNull Level level, @NotNull String text) {
/* 116 */     getLogger().log(level, text);
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
/*     */   @NotNull
/*     */   public final ResourceFile resource(String name, ResourceFormat format, ResourcePath path, Consumer<ResourceOptions> options) {
/* 129 */     return ResourceFile.builder()
/* 130 */       .name(name)
/* 131 */       .format(format)
/* 132 */       .path(path)
/* 133 */       .options(options)
/* 134 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public final ResourceFile resource(String name, ResourcePath path, Consumer<ResourceOptions> options) {
/* 146 */     return resource(name, ResourceFormats.yaml(), path, options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public final ResourceFile resource(String name, ResourcePath path) {
/* 157 */     return resource(name, ResourceFormats.yaml(), path, options -> {
/*     */         
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public final ResourceFile resource(String name) {
/* 167 */     return resource(name, ResourceFormats.yaml(), ResourcePaths.plugin(), options -> {
/*     */         
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public final Commands command(String name) {
/* 177 */     return Commands.command(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public final Server server() {
/* 185 */     return getServer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void disable() {
/* 192 */     plugins().disablePlugin((Plugin)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public final PluginManager plugins() {
/* 200 */     return server().getPluginManager();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public final PluginEvents events() {
/* 208 */     return new PluginEvents();
/*     */   }
/*     */   
/*     */   protected abstract void start();
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\library\wrapper\PluginWrapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */