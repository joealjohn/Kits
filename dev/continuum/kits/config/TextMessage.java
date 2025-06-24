/*     */ package dev.continuum.kits.config;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import dev.continuum.kits.libs.utils.text.font.SmallFont;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import net.kyori.adventure.text.minimessage.Context;
/*     */ import net.kyori.adventure.text.minimessage.MiniMessage;
/*     */ import net.kyori.adventure.text.minimessage.tag.Tag;
/*     */ import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
/*     */ import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
/*     */ import org.bukkit.Sound;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ public class TextMessage {
/*  19 */   private static final MiniMessage miniMessage = MiniMessage.miniMessage();
/*     */   
/*     */   private final TextMessageType type;
/*     */   
/*     */   private final List<String> raw;
/*     */   
/*     */   private final Elements<Tuple<String, String>> replacements;
/*  26 */   private Sound sound = null;
/*  27 */   private double pitch = -1.0D;
/*  28 */   private double volume = -1.0D;
/*     */   
/*     */   private String title;
/*     */   private String subtitle;
/*     */   private Duration fadeIn;
/*     */   private Duration stay;
/*     */   private Duration fadeOut;
/*     */   
/*     */   public TextMessage(@NotNull TextMessageType type, @NotNull String raw, @NotNull Elements<Tuple<String, String>> replacements) {
/*  37 */     this.type = type;
/*  38 */     this.raw = new ArrayList<>(List.of(raw));
/*  39 */     this.replacements = replacements;
/*     */   }
/*     */   
/*     */   public TextMessage(@NotNull TextMessageType type, @NotNull String raw) {
/*  43 */     this(type, raw, (Elements<Tuple<String, String>>)Elements.of());
/*     */   }
/*     */   
/*     */   public TextMessage(@NotNull TextMessageType type, @NotNull List<String> raw, @NotNull Elements<Tuple<String, String>> replacements) {
/*  47 */     this.type = type;
/*  48 */     this.raw = raw;
/*  49 */     this.replacements = replacements;
/*     */   }
/*     */   
/*     */   public TextMessage(@NotNull TextMessageType type, @NotNull List<String> raw) {
/*  53 */     this(type, raw, (Elements<Tuple<String, String>>)Elements.of());
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Component text() {
/*  58 */     TagResolver smallCapsTag = TagResolver.resolver("small_caps", (queue, ctx) -> {
/*     */           String text = queue.popOr("Invalid syntax found. Should be <small_caps:'text to convert'>").value();
/*     */           
/*     */           return Tag.selfClosingInserting((Component)Component.text(SmallFont.convert(text)));
/*     */         });
/*  63 */     return miniMessage.deserialize(replacedRaw(), smallCapsTag);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public String replaced(@NotNull String text) {
/*  68 */     for (Tuple<String, String> tuple : this.replacements) {
/*  69 */       String rawTupleKey = (String)tuple.key();
/*  70 */       if (rawTupleKey == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  74 */       String tupleKey = rawTupleKey.replaceAll("<", "").replaceAll(">", "");
/*     */       
/*  76 */       String key = "<" + tupleKey + ">";
/*  77 */       String val = (String)tuple.val();
/*  78 */       if (val == null)
/*     */         continue; 
/*  80 */       text = text.replaceAll(key, val);
/*     */     } 
/*     */     
/*  83 */     return text;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public String replacedRaw() {
/*  88 */     String rawText = String.join("<newline>", (Iterable)this.raw);
/*     */     
/*  90 */     for (Tuple<String, String> tuple : this.replacements) {
/*  91 */       String rawTupleKey = (String)tuple.key();
/*  92 */       if (rawTupleKey == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  96 */       String tupleKey = rawTupleKey.replaceAll("<", "").replaceAll(">", "");
/*     */       
/*  98 */       String key = "<" + tupleKey + ">";
/*  99 */       String val = (String)tuple.val();
/* 100 */       if (val == null) {
/* 101 */         System.out.println("null");
/*     */         
/*     */         continue;
/*     */       } 
/* 105 */       rawText = rawText.replaceAll(key, val);
/*     */     } 
/*     */     
/* 108 */     return rawText;
/*     */   }
/*     */   
/*     */   public void sound(@NotNull String rawSound, double pitch, double volume) {
/* 112 */     this.sound = Sound.valueOf(rawSound.replaceAll("minecraft:", ""));
/* 113 */     this.pitch = pitch;
/* 114 */     this.volume = volume;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Sound sound() {
/* 119 */     return this.sound;
/*     */   }
/*     */   
/*     */   public double pitch() {
/* 123 */     return this.pitch;
/*     */   }
/*     */   
/*     */   public double volume() {
/* 127 */     return this.volume;
/*     */   }
/*     */   
/*     */   public TextMessageType type() {
/* 131 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void title(@NotNull String title, @NotNull String subtitle, @NotNull Duration fadeIn, @NotNull Duration stay, @NotNull Duration fadeOut) {
/* 140 */     this.title = title;
/* 141 */     this.subtitle = subtitle;
/* 142 */     this.fadeIn = fadeIn;
/* 143 */     this.stay = stay;
/* 144 */     this.fadeOut = fadeOut;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Component title() {
/* 149 */     TagResolver smallCapsTag = TagResolver.resolver("small_caps", (queue, ctx) -> {
/*     */           String text = queue.popOr("Invalid syntax found. Should be <small_caps:'text to convert'>").value();
/*     */           
/*     */           return Tag.selfClosingInserting((Component)Component.text(SmallFont.convert(text)));
/*     */         });
/* 154 */     if (this.title == null || this.title.isEmpty() || this.title.isBlank()) return null; 
/* 155 */     return miniMessage.deserialize(replaced(this.title), smallCapsTag);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Component subtitle() {
/* 160 */     TagResolver smallCapsTag = TagResolver.resolver("small_caps", (queue, ctx) -> {
/*     */           String text = queue.popOr("Invalid syntax found. Should be <small_caps:'text to convert'>").value();
/*     */           
/*     */           return Tag.selfClosingInserting((Component)Component.text(SmallFont.convert(text)));
/*     */         });
/* 165 */     if (this.subtitle == null || this.subtitle.isEmpty() || this.subtitle.isBlank()) return null; 
/* 166 */     return miniMessage.deserialize(replaced(this.subtitle), smallCapsTag);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Duration fadeIn() {
/* 171 */     return this.fadeIn;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Duration stay() {
/* 176 */     return this.stay;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Duration fadeOut() {
/* 181 */     return this.fadeOut;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\config\TextMessage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */