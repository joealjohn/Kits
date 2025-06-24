/*     */ package dev.continuum.kits.libs.utils.resource.yaml;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.cachable.impl.CachableImpl;
/*     */ import dev.continuum.kits.libs.utils.item.ItemBuilder;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import dev.continuum.kits.libs.utils.resource.ResourceFile;
/*     */ import dev.continuum.kits.libs.utils.text.color.TextStyle;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public final class ResourceConfiguration extends Record {
/*     */   private final ResourceFile resourceFile;
/*     */   
/*  21 */   public ResourceConfiguration(ResourceFile resourceFile) { this.resourceFile = resourceFile; } public ResourceFile resourceFile() { return this.resourceFile; }
/*     */    public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Ldev/continuum/kits/libs/utils/resource/yaml/ResourceConfiguration;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Ldev/continuum/kits/libs/utils/resource/yaml/ResourceConfiguration;
/*     */   } public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Ldev/continuum/kits/libs/utils/resource/yaml/ResourceConfiguration;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Ldev/continuum/kits/libs/utils/resource/yaml/ResourceConfiguration;
/*     */   } public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Ldev/continuum/kits/libs/utils/resource/yaml/ResourceConfiguration;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Ldev/continuum/kits/libs/utils/resource/yaml/ResourceConfiguration;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   } public Object val(String key) {
/*  29 */     return bukkit().get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String valAsString(String key) {
/*  39 */     return bukkit().getString(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean valAsBoolean(String key) {
/*  49 */     return bukkit().getBoolean(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Boolean> valAsBooleanList(String key) {
/*  59 */     return bukkit().getBooleanList(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Byte> valAsByteList(String key) {
/*  69 */     return bukkit().getByteList(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Character> valAsCharacterList(String key) {
/*  79 */     return bukkit().getCharacterList(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color valAsColor(String key) {
/*  89 */     return bukkit().getColor(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double valAsDouble(String key) {
/*  99 */     return bukkit().getDouble(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Double> valAsDoubleList(String key) {
/* 109 */     return bukkit().getDoubleList(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Float> valAsFloatList(String key) {
/* 119 */     return bukkit().getFloatList(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int valAsInt(String key) {
/* 129 */     return bukkit().getInt(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Integer> valAsIntList(String key) {
/* 139 */     return bukkit().getIntegerList(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack valAsItemStack(String key) {
/* 149 */     return bukkit().getItemStack(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<?> valAsList(String key) {
/* 159 */     return bukkit().getList(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Location valAsLocation(String key) {
/* 169 */     return bukkit().getLocation(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long valAsLong(String key) {
/* 179 */     return bukkit().getLong(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Long> valAsLongList(String key) {
/* 189 */     return bukkit().getLongList(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Map<?, ?>> valAsMapList(String key) {
/* 199 */     return bukkit().getMapList(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T val(String key, Class<T> clazz) {
/* 209 */     if (clazz.getCanonicalName().equals(Component.class.getCanonicalName())) {
/* 210 */       return clazz.cast(valAsComponent(key));
/*     */     }
/*     */     
/* 213 */     if (clazz.getCanonicalName().equals(ItemBuilder.class.getCanonicalName())) {
/* 214 */       return clazz.cast(valAsItemBuilder(key));
/*     */     }
/*     */     
/* 217 */     return (T)bukkit().getObject(key, clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OfflinePlayer valAsOfflinePlayer(String key) {
/* 227 */     return bukkit().getOfflinePlayer(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Short> valAsShortList(String key) {
/* 237 */     return bukkit().getShortList(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> valAsStringList(String key) {
/* 247 */     return bukkit().getStringList(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector valAsVector(String key) {
/* 257 */     return bukkit().getVector(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBuilder valAsItemBuilder(String key) {
/* 267 */     return ItemBuilder.item(valAsItemStack(key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Component valAsComponent(String key) {
/* 277 */     return TextStyle.style(valAsString(key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(String key, Object val) {
/* 287 */     bukkit().set(key, val);
/* 288 */     this.resourceFile.saveYml();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(Tuple<String, Object> tuple) {
/* 297 */     if (tuple != null && tuple.key() != null) {
/* 298 */       set(Objects.<String>requireNonNull((String)tuple.key()), tuple.val());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cachable<String, Object> keyVal() {
/* 308 */     CachableImpl cachableImpl = Cachable.of();
/*     */     
/* 310 */     for (String key : keys()) {
/* 311 */       Object val = bukkit().get(key);
/* 312 */       cachableImpl.cache(key, val);
/*     */     } 
/*     */     
/* 315 */     return (Cachable<String, Object>)cachableImpl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> keys() {
/* 324 */     return bukkit().getKeys(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public YamlConfiguration bukkit() {
/* 333 */     return (YamlConfiguration)this.resourceFile.loadYml();
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\resource\yaml\ResourceConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */