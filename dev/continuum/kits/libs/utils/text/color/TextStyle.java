/*     */ package dev.continuum.kits.libs.utils.text.color;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import net.kyori.adventure.text.ComponentLike;
/*     */ import net.kyori.adventure.text.JoinConfiguration;
/*     */ import net.kyori.adventure.text.format.TextDecoration;
/*     */ import net.kyori.adventure.text.minimessage.MiniMessage;
/*     */ import net.md_5.bungee.api.ChatColor;
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
/*     */ public class TextStyle
/*     */ {
/*     */   public static String legacyHex(String text) {
/*  30 */     String pattern = "<#([A-Fa-f0-9]{6})>";
/*     */ 
/*     */     
/*  33 */     Pattern colorPattern = Pattern.compile(pattern);
/*  34 */     Matcher matcher = colorPattern.matcher(text);
/*     */ 
/*     */     
/*  37 */     StringBuilder modifiedMessage = new StringBuilder();
/*     */ 
/*     */     
/*  40 */     while (matcher.find()) {
/*  41 */       String hexColor = matcher.group(1);
/*  42 */       ChatColor color = ChatColor.of("#" + hexColor);
/*  43 */       matcher.appendReplacement(modifiedMessage, color.toString());
/*     */     } 
/*     */ 
/*     */     
/*  47 */     matcher.appendTail(modifiedMessage);
/*     */     
/*  49 */     return modifiedMessage.toString();
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
/*     */   public static String legacy(String text) {
/*  64 */     text = text.replaceAll("<black>", ChatColor.BLACK.toString());
/*  65 */     text = text.replaceAll("<dark_blue>", ChatColor.DARK_BLUE.toString());
/*  66 */     text = text.replaceAll("<dark_green>", ChatColor.DARK_GREEN.toString());
/*  67 */     text = text.replaceAll("<dark_aqua>", ChatColor.DARK_AQUA.toString());
/*  68 */     text = text.replaceAll("<dark_light_blue>", ChatColor.DARK_AQUA.toString());
/*  69 */     text = text.replaceAll("<dark_red>", ChatColor.DARK_RED.toString());
/*  70 */     text = text.replaceAll("<dark_purple>", ChatColor.DARK_PURPLE.toString());
/*  71 */     text = text.replaceAll("<dark_pink>", ChatColor.DARK_PURPLE.toString());
/*  72 */     text = text.replaceAll("<gold>", ChatColor.GOLD.toString());
/*  73 */     text = text.replaceAll("<dark_yellow>", ChatColor.GOLD.toString());
/*  74 */     text = text.replaceAll("<gray>", ChatColor.GRAY.toString());
/*  75 */     text = text.replaceAll("<grey>", ChatColor.GRAY.toString());
/*  76 */     text = text.replaceAll("<dark_gray>", ChatColor.DARK_GRAY.toString());
/*  77 */     text = text.replaceAll("<dark_grey>", ChatColor.DARK_GRAY.toString());
/*  78 */     text = text.replaceAll("<blue>", ChatColor.BLUE.toString());
/*  79 */     text = text.replaceAll("<green>", ChatColor.GREEN.toString());
/*  80 */     text = text.replaceAll("<lime>", ChatColor.GREEN.toString());
/*  81 */     text = text.replaceAll("<aqua>", ChatColor.AQUA.toString());
/*  82 */     text = text.replaceAll("<light_blue>", ChatColor.AQUA.toString());
/*  83 */     text = text.replaceAll("<red>", ChatColor.RED.toString());
/*  84 */     text = text.replaceAll("<pink>", ChatColor.LIGHT_PURPLE.toString());
/*  85 */     text = text.replaceAll("<purple>", ChatColor.LIGHT_PURPLE.toString());
/*  86 */     text = text.replaceAll("<light_purple>", ChatColor.LIGHT_PURPLE.toString());
/*  87 */     text = text.replaceAll("<light_pink>", ChatColor.LIGHT_PURPLE.toString());
/*  88 */     text = text.replaceAll("<magenta>", ChatColor.LIGHT_PURPLE.toString());
/*  89 */     text = text.replaceAll("<yellow>", ChatColor.YELLOW.toString());
/*  90 */     text = text.replaceAll("<light_yellow>", ChatColor.YELLOW.toString());
/*  91 */     text = text.replaceAll("<white>", ChatColor.WHITE.toString());
/*     */ 
/*     */     
/*  94 */     text = text.replaceAll("<bold>", ChatColor.BOLD.toString());
/*  95 */     text = text.replaceAll("<reset>", ChatColor.RESET.toString());
/*  96 */     text = text.replaceAll("<underline>", ChatColor.UNDERLINE.toString());
/*  97 */     text = text.replaceAll("<italic>", ChatColor.ITALIC.toString());
/*  98 */     text = text.replaceAll("<strikethrough>", ChatColor.STRIKETHROUGH.toString());
/*  99 */     text = text.replaceAll("<strike>", ChatColor.STRIKETHROUGH.toString());
/* 100 */     text = text.replaceAll("<obfuscated>", ChatColor.MAGIC.toString());
/* 101 */     text = text.replaceAll("<magic>", ChatColor.MAGIC.toString());
/* 102 */     text = text.replaceAll("<random>", ChatColor.MAGIC.toString());
/* 103 */     text = text.replaceAll("<newline>", "\n");
/* 104 */     text = text.replaceAll("<nl>", "\n");
/*     */ 
/*     */     
/* 107 */     text = legacyTranslate(text);
/*     */ 
/*     */     
/* 110 */     text = legacyHex(text);
/*     */     
/* 112 */     return text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Component component(String text) {
/* 122 */     MiniMessage miniMessage = MiniMessage.miniMessage();
/*     */     
/* 124 */     return miniMessage.deserialize(text).decoration(TextDecoration.ITALIC, false);
/*     */   }
/*     */   
/*     */   public static List<Component> component(List<String> texts) {
/* 128 */     List<Component> list = new ArrayList<>();
/*     */     
/* 130 */     for (String text : texts) {
/* 131 */       list.add(component(text));
/*     */     }
/*     */     
/* 134 */     return list;
/*     */   }
/*     */   
/*     */   public static List<Component> component(String... texts) {
/* 138 */     return component(Arrays.asList(texts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Component color(String text) {
/* 148 */     return component(text);
/*     */   }
/*     */   
/*     */   public static List<Component> color(List<String> texts) {
/* 152 */     return component(texts);
/*     */   }
/*     */   
/*     */   public static List<Component> color(String... texts) {
/* 156 */     return component(texts);
/*     */   }
/*     */   
/*     */   public static List<Component> style(List<String> texts) {
/* 160 */     return component(texts);
/*     */   }
/*     */   
/*     */   public static List<Component> style(String... texts) {
/* 164 */     return component(texts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Component style(String text) {
/* 174 */     return component(text);
/*     */   }
/*     */   
/*     */   public Component joined(Collection<Component> values) {
/* 178 */     return Component.join(JoinConfiguration.newlines(), values);
/*     */   }
/*     */   
/*     */   public Component joined(Component... values) {
/* 182 */     return Component.join(JoinConfiguration.newlines(), (ComponentLike[])values);
/*     */   }
/*     */   
/*     */   public Component joined(Iterable<? extends ComponentLike> values) {
/* 186 */     return Component.join(JoinConfiguration.newlines(), values);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String text(Component component) {
/* 196 */     MiniMessage miniMessage = MiniMessage.miniMessage();
/*     */     
/* 198 */     return (String)miniMessage.serialize(component);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String legacyTranslate(String text) {
/* 209 */     return ChatColor.translateAlternateColorCodes('&', text);
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
/*     */   public static ChatColor chatColor(String input) {
/*     */     char character;
/* 222 */     if (input.startsWith("&")) { character = input.charAt(1); }
/* 223 */     else { character = input.charAt(0); }
/*     */     
/* 225 */     return ChatColor.getByChar(character);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String value(ChatColor color) {
/* 236 */     return color.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ChatColor hex(String hex) {
/* 247 */     if (!hex.startsWith("#")) return ChatColor.of("#" + hex);
/*     */     
/* 249 */     return ChatColor.of(hex);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\text\color\TextStyle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */