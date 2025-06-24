/*    */ package dev.continuum.kits.libs.utils.event.crystal.impl;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.event.crystal.PlayerCrystalDeathEvent;
/*    */ import dev.continuum.kits.libs.utils.library.Utils;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.entity.EntityType;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ import org.bukkit.event.entity.PlayerDeathEvent;
/*    */ 
/*    */ public class SpigotCrystalEventListener
/*    */   implements Listener {
/*    */   private Entity crystal;
/*    */   private Player victim;
/*    */   
/*    */   private void reset() {
/* 20 */     this.crystal = null;
/* 21 */     this.victim = null;
/* 22 */     this.killer = null;
/* 23 */     this.playerDamageEvent = null;
/*    */   }
/*    */   private Player killer; private EntityDamageByEntityEvent playerDamageEvent;
/*    */   @EventHandler
/*    */   public void onCrystalExplode(EntityDamageByEntityEvent event) {
/* 28 */     if (event.getEntityType() != EntityType.END_CRYSTAL)
/*    */       return; 
/* 30 */     if (event.getDamager().getType() != EntityType.PLAYER)
/*    */       return; 
/* 32 */     reset();
/* 33 */     this.crystal = event.getEntity();
/* 34 */     this.killer = (Player)event.getDamager();
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onPlayerDamage(EntityDamageByEntityEvent event) {
/* 39 */     if (event.getEntityType() != EntityType.PLAYER)
/*    */       return; 
/* 41 */     if (this.crystal == null) {
/* 42 */       reset();
/*    */       
/*    */       return;
/*    */     } 
/* 46 */     if (event.getDamager() != this.crystal) {
/* 47 */       reset();
/*    */       
/*    */       return;
/*    */     } 
/* 51 */     this.victim = (Player)event.getEntity();
/* 52 */     this.playerDamageEvent = event;
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onPlayerDeath(PlayerDeathEvent event) {
/* 57 */     if (this.crystal == null || this.killer == null || this.victim == null || this.playerDamageEvent == null) {
/*    */       return;
/*    */     }
/*    */     
/* 61 */     if (event.getEntity() != this.victim) {
/*    */       return;
/*    */     }
/*    */     
/* 65 */     PlayerCrystalDeathEvent calledEvent = new PlayerCrystalDeathEvent(this.killer, this.victim, this.crystal, this.playerDamageEvent, event);
/*    */ 
/*    */ 
/*    */     
/* 69 */     Utils.plugin().getServer().getPluginManager().callEvent((Event)calledEvent);
/*    */     
/* 71 */     reset();
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\event\crystal\impl\SpigotCrystalEventListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */