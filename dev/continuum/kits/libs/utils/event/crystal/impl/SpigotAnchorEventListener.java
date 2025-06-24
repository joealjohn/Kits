/*    */ package dev.continuum.kits.libs.utils.event.crystal.impl;
/*    */ import dev.continuum.kits.libs.utils.event.crystal.PlayerAnchorDeathEvent;
/*    */ import dev.continuum.kits.libs.utils.library.Utils;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.block.data.BlockData;
/*    */ import org.bukkit.block.data.type.RespawnAnchor;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.entity.EntityType;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.entity.EntityDamageByBlockEvent;
/*    */ import org.bukkit.event.entity.EntityDamageEvent;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class SpigotAnchorEventListener implements Listener {
/* 24 */   private static final Map<Location, UUID> DETONATOR_MAP = new HashMap<>();
/*    */   @EventHandler
/*    */   public void onInteract(PlayerInteractEvent event) {
/*    */     try {
/*    */       RespawnAnchor anchorBlock;
/* 29 */       if (event.getClickedBlock() == null || !event.hasBlock() || event.getMaterial() != Material.RESPAWN_ANCHOR)
/*    */         return; 
/* 31 */       BlockData blockData = event.getClickedBlock().getBlockData(); if (blockData instanceof RespawnAnchor) { anchorBlock = (RespawnAnchor)blockData; }
/*    */       else
/*    */       { return; }
/*    */       
/* 35 */       ItemStack itemHand = (event.getItem() == null) ? new ItemStack(Material.AIR) : event.getItem();
/*    */       
/* 37 */       if (anchorBlock.getCharges() > 3)
/* 38 */         return;  if (itemHand.getType() == Material.GLOWSTONE && anchorBlock.getCharges() != 0)
/*    */         return; 
/* 40 */       DETONATOR_MAP.put(event.getClickedBlock().getLocation(), event.getPlayer().getUniqueId());
/* 41 */     } catch (Exception e) {
/* 42 */       RespawnAnchor anchorBlock; HandlerList.unregisterAll(this);
/*    */     } 
/*    */   }
/*    */   @EventHandler
/*    */   public void onAnchorDamage(EntityDamageByBlockEvent event) {
/*    */     try {
/*    */       Player victim;
/* 49 */       if (event.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
/* 50 */         return;  Entity entity = event.getEntity(); if (entity instanceof Player) { victim = (Player)entity; if (event.getEntityType() != EntityType.PLAYER)
/* 51 */           return;  } else { return; }  if (victim.getHealth() > event.getDamage())
/*    */         return; 
/* 53 */       if (victim.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING)
/* 54 */         return;  if (victim.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING)
/*    */         return; 
/* 56 */       if (event.getDamagerBlockState() == null || event.getDamagerBlockState().getType() != Material.RESPAWN_ANCHOR) {
/*    */         return;
/*    */       }
/* 59 */       if (victim.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING)
/* 60 */         return;  if (victim.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING)
/*    */         return; 
/* 62 */       UUID detonator = DETONATOR_MAP.get(new Location(victim
/* 63 */             .getWorld(), event
/* 64 */             .getDamagerBlockState().getX(), event
/* 65 */             .getDamagerBlockState().getY(), event
/* 66 */             .getDamagerBlockState().getZ()));
/*    */ 
/*    */       
/* 69 */       if (detonator == null)
/*    */         return; 
/* 71 */       Player playerDetonator = PlayerUtils.player(detonator);
/*    */       
/* 73 */       if (playerDetonator == null) {
/*    */         return;
/*    */       }
/* 76 */       PlayerAnchorDeathEvent calledEvent = new PlayerAnchorDeathEvent(event.getDamagerBlockState(), victim, playerDetonator);
/*    */ 
/*    */       
/* 79 */       Utils.plugin().getServer().getPluginManager().callEvent((Event)calledEvent);
/*    */       
/* 81 */       DETONATOR_MAP.remove(event.getDamagerBlockState().getLocation());
/* 82 */     } catch (Exception e) {
/* 83 */       Player victim; HandlerList.unregisterAll(this);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\event\crystal\impl\SpigotAnchorEventListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */