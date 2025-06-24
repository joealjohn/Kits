/*    */ package dev.continuum.kits.libs.utils.command.impl.suggestions;
/*    */ 
/*    */ import net.kyori.adventure.text.Component;
/*    */ import net.kyori.adventure.text.format.NamedTextColor;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Suggestion
/*    */ {
/*    */   private final String text;
/* 29 */   private final Component tooltip = (Component)Component.empty();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   private final NamedTextColor color = NamedTextColor.AQUA;
/*    */   
/*    */   private Suggestion(@NotNull String text) {
/* 38 */     this.text = text;
/*    */   }
/*    */   
/*    */   private Suggestion(@NotNull String text, @NotNull Component tooltip) {
/* 42 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   private Suggestion(@NotNull String text, @NotNull Component tooltip, @NotNull NamedTextColor color) {
/* 46 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static Suggestion text(@NotNull String text) {
/* 55 */     return new Suggestion(text);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static Suggestion number(int number) {
/* 64 */     return text(String.valueOf(number));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public String text() {
/* 73 */     return this.text;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public Component tooltip() {
/* 84 */     return (Component)Component.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public NamedTextColor color() {
/* 96 */     return NamedTextColor.AQUA;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\impl\suggestions\Suggestion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */