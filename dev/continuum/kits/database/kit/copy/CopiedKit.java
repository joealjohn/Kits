/*     */ package dev.continuum.kits.database.kit.copy;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import dev.continuum.kits.api.event.impl.save.KitSaveEvent;
/*     */ import dev.continuum.kits.config.Messages;
/*     */ import dev.continuum.kits.database.DatabaseProvider;
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.misc.ObjectUtils;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import net.kyori.adventure.audience.Audience;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ public class CopiedKit {
/*  22 */   private static final Cachable<String, CopiedKit> cache = (Cachable<String, CopiedKit>)Cachable.of(String.class, CopiedKit.class);
/*     */   private final String code;
/*     */   private final UUID owner;
/*     */   private final int kit;
/*     */   private final Cachable<Integer, ItemStack> contents;
/*     */   
/*     */   public CopiedKit(@NotNull UUID owner, int kit, @NotNull Cachable<Integer, ItemStack> contents) {
/*  29 */     this.code = generated();
/*  30 */     this.owner = owner;
/*  31 */     this.kit = kit;
/*  32 */     this.contents = contents;
/*     */     
/*  34 */     if (!cache.hasKey(this.code) || !cache.hasVal(this)) cache.cache(this.code, this); 
/*  35 */     startExpire();
/*     */   }
/*     */   
/*     */   public CopiedKit(@NotNull UUID owner, int kit) {
/*  39 */     this(owner, kit, (Cachable<Integer, ItemStack>)ObjectUtils.defaultIfNull(
/*  40 */           DatabaseProvider.database().data(owner, kit), 
/*  41 */           Cachable.of(Integer.class, ItemStack.class)));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static CopiedKit of(@NotNull UUID owner, int kit) {
/*  47 */     return new CopiedKit(owner, kit);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static CopiedKit of(@NotNull UUID owner, int kit, @NotNull Cachable<Integer, ItemStack> contents) {
/*  52 */     return new CopiedKit(owner, kit, contents);
/*     */   }
/*     */   
/*     */   public void startExpire() {
/*  56 */     int expiresIn = ConfigOptions.expiresIn().intValue();
/*     */     
/*  58 */     Schedulers.async().execute(task -> { if (!cache.hasVal(this) || !cache.hasKey(this.code)) { task.cancel(); return; }  CopiedKit kit = (CopiedKit)cache.val(this.code); if (kit == null) { task.cancel(); return; }  if (!((CopiedKit)cache.val(this.code)).equals(this) || !((String)cache.key(this)).equals(this.code)) { task.cancel(); return; }  delete(); }expiresIn, 0);
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
/*     */   @NotNull
/*     */   public static String generated() {
/*  82 */     ThreadLocalRandom random = ThreadLocalRandom.current();
/*     */     
/*  84 */     String everything = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
/*  85 */     int capacity = 8;
/*     */     
/*  87 */     StringBuilder builder = new StringBuilder(8);
/*     */     
/*  89 */     for (int i = 0; i < 8; i++) {
/*  90 */       int randomIndex = random.nextInt("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".length());
/*  91 */       char randomChar = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".charAt(randomIndex);
/*  92 */       builder.append(randomChar);
/*     */     } 
/*     */     
/*  95 */     String generated = builder.toString();
/*  96 */     if (cache.hasKey(generated)) return generated();
/*     */     
/*  98 */     return generated;
/*     */   }
/*     */   
/*     */   public void delete() {
/* 102 */     if (cache.hasVal(this) && cache.hasKey(this.code)) {
/* 103 */       cache.del(this.code, this);
/*     */       
/*     */       return;
/*     */     } 
/* 107 */     if (!cache.hasVal(this) && cache.hasKey(this.code)) {
/* 108 */       cache.del(this.code);
/*     */     }
/*     */   }
/*     */   
/*     */   public void copy(@NotNull Player target, int targetKit) {
/* 113 */     Messages.findAndSend("copying_kit", (Elements)Elements.of((Object[])new Tuple[] { Tuple.tuple("code", this.code) }), (Audience)target);
/*     */     
/* 115 */     KitSaveEvent event = new KitSaveEvent(target, this.kit, true, this.contents);
/* 116 */     if (!event.fire()) {
/* 117 */       delete();
/*     */       
/*     */       return;
/*     */     } 
/* 121 */     DatabaseProvider.database().save(target.getUniqueId(), targetKit, this.contents, (result, optional) -> optional.ifPresent(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     delete();
/*     */   }
/*     */   
/*     */   public static void copy(@NotNull String code, @NotNull Player target, int targetKit) {
/* 135 */     CopiedKit kit = byCode(code);
/*     */     
/* 137 */     if (kit == null) {
/* 138 */       Messages.findAndSend("copy_kit_code_not_found", (Elements)Elements.of((Object[])new Tuple[] { Tuple.tuple("code", code) }), (Audience)target);
/*     */       
/*     */       return;
/*     */     } 
/* 142 */     kit.copy(target, targetKit);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static CopiedKit byCode(@NotNull String code) {
/* 147 */     if (!cache.hasKey(code)) return null; 
/* 148 */     return (CopiedKit)cache.val(code);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public String id() {
/* 153 */     return this.code;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public UUID owner() {
/* 158 */     return this.owner;
/*     */   }
/*     */   
/*     */   public int kit() {
/* 162 */     return this.kit;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Cachable<Integer, ItemStack> contents() {
/* 167 */     return this.contents;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\kit\copy\CopiedKit.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */