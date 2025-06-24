/*     */ package dev.continuum.kits.libs.utils.menu;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.item.ItemBuilder;
/*     */ import dev.continuum.kits.libs.utils.menu.listener.CloseListener;
/*     */ import dev.continuum.kits.libs.utils.menu.listener.DragListener;
/*     */ import dev.continuum.kits.libs.utils.menu.paginated.PaginatedSlot;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
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
/*     */ public interface MenuBase<T>
/*     */ {
/*     */   @NotNull
/*     */   T type();
/*     */   
/*     */   @NotNull
/*     */   T onClose(@Nullable CloseListener paramCloseListener);
/*     */   
/*     */   @NotNull
/*     */   T onDrag(@Nullable DragListener paramDragListener);
/*     */   
/*     */   @NotNull
/*     */   default T button(int slot, Button button) {
/*  46 */     return type();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default T item(int slot, ItemBuilder item) {
/*  57 */     return type();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int size();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Component title();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void open(Player paramPlayer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   T border(Button paramButton, String... paramVarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   T fill(Object paramObject, String... paramVarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Inventory inventory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default T button(PaginatedSlot where, Button button) {
/* 114 */     return type();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default T item(PaginatedSlot where, ItemBuilder item) {
/* 125 */     return type();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void open(Player player, int page) {
/* 135 */     open(player);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\menu\MenuBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */