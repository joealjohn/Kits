/*    */ package dev.continuum.kits.parser.menu;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.item.ItemBuilder;
/*    */ import java.util.List;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class MenuParserItem
/*    */ {
/*    */   private final ItemBuilder item;
/*    */   private final boolean enabled;
/*    */   private final List<Integer> slots;
/*    */   
/*    */   public MenuParserItem(@NotNull ItemBuilder item, @NotNull List<Integer> slots) {
/* 14 */     this.item = item;
/* 15 */     this.enabled = true;
/* 16 */     this.slots = slots;
/*    */   }
/*    */ 
/*    */   
/*    */   public MenuParserItem(@NotNull ItemBuilder item, @NotNull List<Integer> slots, boolean enabled) {
/* 21 */     this.item = item;
/* 22 */     this.enabled = enabled;
/* 23 */     this.slots = slots;
/*    */   }
/*    */   @NotNull
/*    */   public static MenuParserItem of(@NotNull ItemBuilder item, @NotNull List<Integer> slots) {
/* 27 */     return new MenuParserItem(item, slots);
/*    */   }
/*    */   @NotNull
/*    */   public static MenuParserItem of(@NotNull ItemBuilder item, @NotNull List<Integer> slots, boolean enabled) {
/* 31 */     return new MenuParserItem(item, slots, enabled);
/*    */   }
/*    */   
/*    */   public ItemBuilder item() {
/* 35 */     return this.item;
/*    */   }
/*    */   
/*    */   public boolean enabled() {
/* 39 */     return this.enabled;
/*    */   }
/*    */   
/*    */   public List<Integer> slots() {
/* 43 */     return this.slots;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\parser\menu\MenuParserItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */