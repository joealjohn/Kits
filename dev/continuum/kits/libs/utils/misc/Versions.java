/*     */ package dev.continuum.kits.libs.utils.misc;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.library.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.bukkit.Bukkit;
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
/*     */ public class Versions
/*     */ {
/*     */   public static boolean isVersion(@NotNull String version) {
/*  24 */     return version().equalsIgnoreCase(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isHigherThanOrEqualTo(@NotNull String version) {
/*  34 */     return (isVersion(version) || compareVersions(version(), version) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLowerThan(@NotNull String version) {
/*  44 */     return !isHigherThanOrEqualTo(version);
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
/*     */   private static int compareVersions(@NotNull String versionOne, @NotNull String versionTwo) {
/*  56 */     List<Integer> v1Parts = getVersionParts(versionOne);
/*  57 */     List<Integer> v2Parts = getVersionParts(versionTwo);
/*     */     
/*  59 */     int minLength = Math.min(v1Parts.size(), v2Parts.size());
/*     */     
/*  61 */     for (int i = 0; i < minLength; i++) {
/*  62 */       int partComparison = Integer.compare(((Integer)v1Parts.get(i)).intValue(), ((Integer)v2Parts.get(i)).intValue());
/*  63 */       if (partComparison != 0) {
/*  64 */         return partComparison;
/*     */       }
/*     */     } 
/*     */     
/*  68 */     return Integer.compare(v1Parts.size(), v2Parts.size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   private static List<Integer> getVersionParts(@NotNull String version) {
/*  78 */     return (List<Integer>)Stream.<String>of(version.split("\\."))
/*  79 */       .map(Integer::parseInt)
/*  80 */       .collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static String version() {
/*  89 */     return Bukkit.getMinecraftVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static List<String> minecraft() {
/*  99 */     return new ArrayList<>(List.of(new String[] { "1.20.4", "1.20.3", "1.20.2", "1.20.1", "1.20", "1.19.4", "1.19.3", "1.19.2", "1.19.1", "1.19", "1.18.2", "1.18.1", "1.18", "1.17.1", "1.17", "1.16.5", "1.16.4", "1.16.3", "1.16.2", "1.16.1", "1.16", "1.15.2", "1.15.1", "1.15", "1.14.4", "1.14.3", "1.14.2", "1.14.1", "1.14", "1.13.2", "1.13.1", "1.13", "1.12.2", "1.12.1", "1.12", "1.11.2", "1.11.1", "1.11", "1.10.2", "1.10.1", "1.10", "1.9.4", "1.9.3", "1.9.2", "1.9.1", "1.9", "1.8.9", "1.8.8", "1.8.7", "1.8.6", "1.8.5", "1.8.4", "1.8.3", "1.8.2", "1.8.1", "1.8", "1.7.10", "1.7.9", "1.7.8", "1.7.7", "1.7.6", "1.7.5", "1.7.4", "1.7.3", "1.7.2", "1.6.4", "1.6.2", "1.6.1", "1.5.2", "1.5.1", "1.4.7", "1.4.6", "1.4.5", "1.4.4", "1.4.2", "1.3.2", "1.3.1", "1.2.5", "1.2.4", "1.2.3", "1.2.2", "1.2.1", "1.1", "1.0" }));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static List<String> mc() {
/* 129 */     return minecraft();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String auto() {
/* 139 */     return Utils.plugin().getPluginMeta().getVersion()
/* 140 */       .toLowerCase()
/* 141 */       .replaceAll("\\[", "")
/* 142 */       .replaceAll("]", "")
/* 143 */       .replaceAll("_", ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String of(@NotNull String version, @NotNull Elements<VersionProperty> properties) {
/* 154 */     version = version.replaceAll("_", ".");
/* 155 */     StringBuilder builder = new StringBuilder(version);
/*     */     
/* 157 */     for (VersionProperty property : properties) {
/* 158 */       builder.append(property.value())
/* 159 */         .append("-");
/*     */     }
/*     */     
/* 162 */     builder.deleteCharAt(builder.lastIndexOf("-"));
/* 163 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String of(@NotNull String version, @NotNull VersionProperty... properties) {
/* 174 */     return of(version, (Elements<VersionProperty>)Elements.of((Object[])properties));
/*     */   } public static final class VersionProperty extends Record {
/*     */     private final String key; private final String value; public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Ldev/continuum/kits/libs/utils/misc/Versions$VersionProperty;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #180	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Ldev/continuum/kits/libs/utils/misc/Versions$VersionProperty;
/*     */     } public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Ldev/continuum/kits/libs/utils/misc/Versions$VersionProperty;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #180	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Ldev/continuum/kits/libs/utils/misc/Versions$VersionProperty;
/* 180 */     } public VersionProperty(String key, String value) { this.key = key; this.value = value; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Ldev/continuum/kits/libs/utils/misc/Versions$VersionProperty;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #180	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Ldev/continuum/kits/libs/utils/misc/Versions$VersionProperty;
/* 180 */       //   0	8	1	o	Ljava/lang/Object; } public String key() { return this.key; } public String value() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static VersionProperty of(String key, String value) {
/* 189 */       return new VersionProperty(key, value.replaceAll(" ", "_").toUpperCase());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\misc\Versions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */