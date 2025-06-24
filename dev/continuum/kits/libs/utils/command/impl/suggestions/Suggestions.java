/*     */ package dev.continuum.kits.libs.utils.command.impl.suggestions;
/*     */ 
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.elements.impl.ElementsImpl;
/*     */ import dev.continuum.kits.libs.utils.server.Servers;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Suggestions
/*     */ {
/*     */   private final ElementsImpl<Suggestion> internal;
/*     */   
/*     */   private Suggestions() {
/*  22 */     this.internal = Elements.of(Suggestion.class);
/*     */   }
/*     */   
/*     */   private Suggestions(@NotNull ElementsImpl<Suggestion> suggestions) {
/*  26 */     this.internal = suggestions;
/*     */   }
/*     */   @NotNull
/*     */   public static Suggestions wrap(@Nullable List<String> toWrap) {
/*  30 */     if (toWrap == null) {
/*  31 */       toWrap = new ArrayList<>();
/*     */     }
/*     */     
/*  34 */     List<Suggestion> suggestions = new ArrayList<>();
/*     */     
/*  36 */     for (String string : toWrap) {
/*  37 */       suggestions.add(Suggestion.text(string));
/*     */     }
/*     */     
/*  40 */     return of(suggestions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions empty() {
/*  48 */     return new Suggestions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions of() {
/*  56 */     return empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions of(@NotNull Suggestion suggestion) {
/*  65 */     return new Suggestions(Elements.of(List.of(suggestion)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions of(@NotNull String rawSuggestion) {
/*  74 */     return new Suggestions(Elements.of(List.of(Suggestion.text(rawSuggestion))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions of(@NotNull List<Suggestion> suggestions) {
/*  83 */     return new Suggestions(Elements.of(suggestions));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions of(@NotNull Collection<Suggestion> suggestions) {
/*  92 */     return new Suggestions(Elements.of(suggestions));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions of(@NotNull Suggestion... suggestions) {
/* 101 */     return new Suggestions(Elements.of((Object[])suggestions));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions of(@NotNull String... rawSuggestions) {
/* 110 */     List<Suggestion> suggestions = new ArrayList<>();
/*     */     
/* 112 */     for (String string : rawSuggestions) {
/* 113 */       suggestions.add(Suggestion.text(string));
/*     */     }
/*     */     
/* 116 */     return of(suggestions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions players() {
/* 124 */     List<Suggestion> suggestionsList = new ArrayList<>();
/*     */     
/* 126 */     for (String name : Servers.onlineNames()) {
/* 127 */       suggestionsList.add(Suggestion.text(name));
/*     */     }
/*     */     
/* 130 */     return of(suggestionsList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions playersWithout(@NotNull Player player) {
/* 139 */     return playersWithout(player.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions playersWithout(@NotNull String player) {
/* 148 */     List<Suggestion> suggestionsList = new ArrayList<>();
/*     */     
/* 150 */     for (String name : Servers.onlineNames()) {
/* 151 */       suggestionsList.add(Suggestion.text(name));
/*     */     }
/*     */     
/* 154 */     suggestionsList.remove(Suggestion.text(player));
/*     */     
/* 156 */     return of(suggestionsList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions playersWithPermission(@NotNull String permission) {
/* 165 */     List<Suggestion> suggestionsList = new ArrayList<>();
/*     */     
/* 167 */     for (Player player : Servers.online()) {
/* 168 */       if (player.hasPermission(permission)) {
/* 169 */         suggestionsList.add(Suggestion.text(player.getName()));
/*     */       }
/*     */     } 
/*     */     
/* 173 */     return of(suggestionsList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions numbers(int max) {
/* 182 */     List<Integer> integerList = new ArrayList<>();
/* 183 */     List<Suggestion> suggestionsList = new ArrayList<>();
/*     */     
/* 185 */     for (int number = 0; number <= max; number++) {
/* 186 */       integerList.add(Integer.valueOf(number));
/*     */     }
/*     */     
/* 189 */     for (Iterator<Integer> iterator = integerList.iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/* 190 */       suggestionsList.add(Suggestion.number(i)); }
/*     */ 
/*     */     
/* 193 */     return of(suggestionsList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Suggestions numbers(int min, int max) {
/* 203 */     List<Integer> integerList = new ArrayList<>();
/* 204 */     List<Suggestion> suggestionsList = new ArrayList<>();
/*     */     
/* 206 */     for (int number = min; number <= max; number++) {
/* 207 */       integerList.add(Integer.valueOf(number));
/*     */     }
/*     */     
/* 210 */     for (Iterator<Integer> iterator = integerList.iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/* 211 */       suggestionsList.add(Suggestion.number(i)); }
/*     */ 
/*     */     
/* 214 */     return of(suggestionsList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @NotNull
/*     */   public Suggestions add(@NotNull Suggestion... suggestions) {
/* 224 */     for (Suggestion suggestion : suggestions) {
/* 225 */       add(suggestion);
/*     */     }
/*     */     
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @NotNull
/*     */   public Suggestions add(@NotNull Collection<Suggestion> suggestions) {
/* 238 */     return add(suggestions.<Suggestion>toArray(new Suggestion[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @NotNull
/*     */   public Suggestions add(@NotNull Suggestion suggestion) {
/* 248 */     this.internal.element(suggestion);
/* 249 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public List<Suggestion> asList() {
/* 258 */     List<Suggestion> suggestions = new ArrayList<>();
/*     */     
/* 260 */     for (Suggestion suggestion : this.internal) {
/* 261 */       suggestions.add(suggestion);
/*     */     }
/*     */     
/* 264 */     return suggestions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public List<String> unwrap() {
/* 273 */     List<Suggestion> suggestions = asList();
/* 274 */     List<String> rawList = new ArrayList<>();
/*     */     
/* 276 */     for (Suggestion suggestion : suggestions) {
/* 277 */       rawList.add(suggestion.text());
/*     */     }
/*     */     
/* 280 */     return rawList;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\impl\suggestions\Suggestions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */