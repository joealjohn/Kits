/*    */ package dev.continuum.kits.database.enderchest;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*    */ import dev.continuum.kits.libs.utils.elements.Elements;
/*    */ import dev.continuum.kits.util.Callback;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ public interface EnderChestDatabase
/*    */ {
/*    */   @NotNull
/*    */   String repr();
/*    */   
/*    */   @NotNull
/*    */   Elements<String> types();
/*    */   
/*    */   @NotNull
/*    */   Cachable<String, Class<?>> requiredDetails();
/*    */   
/*    */   void start(@NotNull Cachable<String, Object> paramCachable);
/*    */   
/*    */   void stop();
/*    */   
/*    */   boolean delete(@NotNull UUID paramUUID, int paramInt);
/*    */   
/*    */   default void delete(@NotNull UUID uuid, int kit, @NotNull Callback<Boolean, Optional<Player>> callback) {
/* 32 */     callback.execute(Boolean.valueOf(delete(uuid, kit)), Optional.ofNullable(Bukkit.getPlayer(uuid)));
/*    */   }
/*    */   
/*    */   boolean save(@NotNull UUID paramUUID, int paramInt, @NotNull Cachable<Integer, ItemStack> paramCachable);
/*    */   
/*    */   default void save(@NotNull UUID uuid, int kit, @NotNull Cachable<Integer, ItemStack> data, @NotNull Callback<Boolean, Optional<Player>> callback) {
/* 38 */     callback.execute(Boolean.valueOf(save(uuid, kit, data)), Optional.ofNullable(Bukkit.getPlayer(uuid)));
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   Cachable<Integer, ItemStack> data(@NotNull UUID paramUUID, int paramInt);
/*    */   
/*    */   boolean hasAny(@NotNull UUID paramUUID);
/*    */   
/*    */   boolean has(@NotNull UUID paramUUID, int paramInt);
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\enderchest\EnderChestDatabase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */