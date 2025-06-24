/*     */ package dev.continuum.kits.config;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.misc.ObjectUtils;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import java.time.Duration;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import net.kyori.adventure.audience.Audience;
/*     */ import net.kyori.adventure.sound.Sound;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import net.kyori.adventure.title.Title;
/*     */ import net.kyori.adventure.title.TitlePart;
/*     */ import org.bukkit.Sound;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ public class Messages
/*     */ {
/*     */   @NotNull
/*     */   public static CompletableFuture<FileConfiguration> config() {
/*  24 */     return Files.config(Files.create(Files.file(new String[] { "messages.yml" })));
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public static CompletableFuture<TextMessage> of(@NotNull String name) {
/*  29 */     return of(name, (Elements<Tuple<String, String>>)Elements.of());
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   private static TextMessageType type(@NotNull String type) {
/*  34 */     TextMessageType textMessageType = TextMessageType.CHAT;
/*     */     
/*  36 */     if (type.equalsIgnoreCase("action bar") || type.equalsIgnoreCase("action_bar")) {
/*  37 */       textMessageType = TextMessageType.ACTION_BAR;
/*  38 */     } else if (type.equalsIgnoreCase("both")) {
/*  39 */       textMessageType = TextMessageType.BOTH;
/*  40 */     } else if (type.equalsIgnoreCase("title")) {
/*  41 */       textMessageType = TextMessageType.TITLE;
/*     */     } 
/*     */     
/*  44 */     return textMessageType;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public static CompletableFuture<TextMessage> of(@NotNull String name, @NotNull Elements<Tuple<String, String>> replacements) {
/*  49 */     return config().thenApply(config -> {
/*     */           String soundsKey = name + ".sounds";
/*     */           String soundKey = soundsKey + ".sound";
/*     */           String pitchKey = soundsKey + ".pitch";
/*     */           String volumeKey = soundsKey + ".volume";
/*     */           Object rawText = config.get(name + ".text");
/*     */           String rawType = config.getString(name + ".type", "chat");
/*     */           TextMessageType type = type(rawType);
/*     */           if (!((Boolean)config.get(name + ".enabled", Boolean.valueOf(true))).booleanValue()) {
/*     */             return new TextMessage(type, "ZGlzYWJsZWQ=");
/*     */           }
/*     */           if (rawText == null && type != TextMessageType.TITLE) {
/*     */             return new TextMessage(type, "", replacements);
/*     */           }
/*     */           if (type == TextMessageType.TITLE) {
/*     */             String soundRaw = config.getString(soundKey);
/*     */             double pitch = config.getDouble(pitchKey);
/*     */             double volume = config.getDouble(volumeKey);
/*     */             TextMessage message = new TextMessage(type, "", replacements);
/*     */             if (soundRaw != null && pitch != -1.0D && volume != -1.0D) {
/*     */               message.sound(soundRaw, pitch, volume);
/*     */             }
/*     */             message.title(config.getString(name + "title", ""), config.getString(name + "subtitle", ""), Duration.of(config.getInt(name + "fade_in.time", 0), TimeUnitParser.parse(config.getString(name + "fade_in.unit", "seconds")).toChronoUnit()), Duration.of(config.getInt(name + "stay.time", 1), TimeUnitParser.parse(config.getString(name + "stay.unit", "seconds")).toChronoUnit()), Duration.of(config.getInt(name + "fade_out.time", 0), TimeUnitParser.parse(config.getString(name + "fade_out.unit", "seconds")).toChronoUnit()));
/*     */             return message;
/*     */           } 
/*     */           if (rawText instanceof List) {
/*     */             List<?> list = (List)rawText;
/*     */             String soundRaw = config.getString(soundKey);
/*     */             double pitch = config.getDouble(pitchKey);
/*     */             double volume = config.getDouble(volumeKey);
/*     */             TextMessage message = new TextMessage(type, (List)list, replacements);
/*     */             if (soundRaw != null && pitch != -1.0D && volume != -1.0D) {
/*     */               message.sound(soundRaw, pitch, volume);
/*     */             }
/*     */             return message;
/*     */           } 
/*     */           if (rawText instanceof String) {
/*     */             String string = (String)rawText;
/*     */             String soundRaw = config.getString(soundKey);
/*     */             double pitch = config.getDouble(pitchKey);
/*     */             double volume = config.getDouble(volumeKey);
/*     */             TextMessage message = new TextMessage(type, string, replacements);
/*     */             if (soundRaw != null && pitch != -1.0D && volume != -1.0D) {
/*     */               message.sound(soundRaw, pitch, volume);
/*     */             }
/*     */             return message;
/*     */           } 
/*     */           return new TextMessage(TextMessageType.CHAT, config.getString(name, ""), replacements);
/*     */         });
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
/*     */   public static void findAndSend(@NotNull String name, @NotNull Audience audience) {
/* 116 */     findAndSend(name, (Elements<Tuple<String, String>>)Elements.of(), audience);
/*     */   }
/*     */   
/*     */   public static void findAndSend(@NotNull String name, @NotNull Elements<Tuple<String, String>> replacements, @NotNull Audience audience) {
/* 120 */     of(name, replacements).thenAccept(message -> {
/*     */           if (message.replacedRaw().equals("ZGlzYWJsZWQ=")) {
/*     */             return;
/*     */           }
/*     */           Schedulers.sync().execute(());
/*     */         });
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
/*     */   public static void message(@NotNull String key, @NotNull Audience audience, @NotNull PlaceholderReplacements replacements) {
/* 166 */     of(key, replacements.replacements((Elements<Tuple<String, String>>)Elements.of())).thenAccept(message -> findAndSend(key, replacements.replacements((Elements<Tuple<String, String>>)Elements.of()), audience));
/*     */   }
/*     */   
/*     */   public static void message(@NotNull String key, @NotNull Audience audience) {
/* 170 */     message(key, audience, elements -> elements);
/*     */   }
/*     */   
/*     */   public static void message(@NotNull String key, @NotNull Audience audience, @NotNull Elements<Tuple<String, String>> replacements) {
/* 174 */     message(key, audience, elements -> replacements);
/*     */   }
/*     */   
/*     */   public static interface PlaceholderReplacements {
/*     */     @NotNull
/*     */     Elements<Tuple<String, String>> replacements(@NotNull Elements<Tuple<String, String>> param1Elements);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\config\Messages.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */