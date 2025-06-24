/*    */ package dev.continuum.kits.category;
/*    */ import dev.continuum.kits.parser.Menus;
/*    */ import dev.continuum.kits.parser.item.ItemParser;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public interface KitRoomCategory {
/*    */   @NotNull
/*    */   default FileConfiguration config() {
/* 11 */     return Menus.of("kit_room");
/*    */   } @NotNull
/*    */   default ItemStack parsed() {
/* 14 */     return ItemParser.parse(config(), "categories." + rawName() + ".item").build();
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   Integer at();
/*    */   
/*    */   @NotNull
/*    */   String rawName();
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\category\KitRoomCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */