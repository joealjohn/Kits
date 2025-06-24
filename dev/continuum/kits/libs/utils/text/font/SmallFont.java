/*    */ package dev.continuum.kits.libs.utils.text.font;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SmallFont
/*    */ {
/* 13 */   private static final Map<Character, Character> smallFontMap = new HashMap<>();
/*    */ 
/*    */   
/*    */   static {
/* 17 */     smallFontMap.put(Character.valueOf('a'), Character.valueOf('ᴀ'));
/* 18 */     smallFontMap.put(Character.valueOf('b'), Character.valueOf('ʙ'));
/* 19 */     smallFontMap.put(Character.valueOf('c'), Character.valueOf('ᴄ'));
/* 20 */     smallFontMap.put(Character.valueOf('d'), Character.valueOf('ᴅ'));
/* 21 */     smallFontMap.put(Character.valueOf('e'), Character.valueOf('ᴇ'));
/* 22 */     smallFontMap.put(Character.valueOf('f'), Character.valueOf('ꜰ'));
/* 23 */     smallFontMap.put(Character.valueOf('g'), Character.valueOf('ɢ'));
/* 24 */     smallFontMap.put(Character.valueOf('h'), Character.valueOf('ʜ'));
/* 25 */     smallFontMap.put(Character.valueOf('i'), Character.valueOf('ɪ'));
/* 26 */     smallFontMap.put(Character.valueOf('j'), Character.valueOf('ᴊ'));
/* 27 */     smallFontMap.put(Character.valueOf('k'), Character.valueOf('ᴋ'));
/* 28 */     smallFontMap.put(Character.valueOf('l'), Character.valueOf('ʟ'));
/* 29 */     smallFontMap.put(Character.valueOf('m'), Character.valueOf('ᴍ'));
/* 30 */     smallFontMap.put(Character.valueOf('n'), Character.valueOf('ɴ'));
/* 31 */     smallFontMap.put(Character.valueOf('o'), Character.valueOf('ᴏ'));
/* 32 */     smallFontMap.put(Character.valueOf('p'), Character.valueOf('ᴘ'));
/* 33 */     smallFontMap.put(Character.valueOf('q'), Character.valueOf('ꞯ'));
/* 34 */     smallFontMap.put(Character.valueOf('r'), Character.valueOf('ʀ'));
/* 35 */     smallFontMap.put(Character.valueOf('s'), Character.valueOf('ꜱ'));
/* 36 */     smallFontMap.put(Character.valueOf('t'), Character.valueOf('ᴛ'));
/* 37 */     smallFontMap.put(Character.valueOf('u'), Character.valueOf('ᴜ'));
/* 38 */     smallFontMap.put(Character.valueOf('v'), Character.valueOf('ᴠ'));
/* 39 */     smallFontMap.put(Character.valueOf('w'), Character.valueOf('ᴡ'));
/* 40 */     smallFontMap.put(Character.valueOf('x'), Character.valueOf('x'));
/* 41 */     smallFontMap.put(Character.valueOf('y'), Character.valueOf('ʏ'));
/* 42 */     smallFontMap.put(Character.valueOf('z'), Character.valueOf('ᴢ'));
/* 43 */     smallFontMap.put(Character.valueOf('{'), Character.valueOf('{'));
/* 44 */     smallFontMap.put(Character.valueOf('|'), Character.valueOf('|'));
/* 45 */     smallFontMap.put(Character.valueOf('}'), Character.valueOf('}'));
/* 46 */     smallFontMap.put(Character.valueOf('~'), Character.valueOf('˜'));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String convert(String text) {
/* 56 */     text = text.toLowerCase();
/* 57 */     StringBuilder result = new StringBuilder();
/*    */     
/* 59 */     for (char character : text.toCharArray()) {
/* 60 */       if (smallFontMap.containsKey(Character.valueOf(character))) {
/* 61 */         result.append(smallFontMap.get(Character.valueOf(character)));
/*    */       } else {
/* 63 */         result.append(character);
/*    */       } 
/*    */     } 
/*    */     
/* 67 */     return result.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\text\font\SmallFont.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */