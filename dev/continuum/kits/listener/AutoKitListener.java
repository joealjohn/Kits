/*    */ package dev.continuum.kits.listener;
/*    */ 
/*    */ import dev.continuum.kits.ContinuumKits;
/*    */ import dev.continuum.kits.api.KitsAPI;
/*    */ import dev.continuum.kits.api.event.KitsEvent;
/*    */ import dev.continuum.kits.api.event.impl.menu.AutoKitEvent;
/*    */ import dev.continuum.kits.database.kit.auto.AutoKit;
/*    */ import dev.continuum.kits.database.kit.auto.AutoKits;
/*    */ import dev.continuum.kits.libs.utils.event.crystal.PlayerAnchorDeathEvent;
/*    */ import dev.continuum.kits.libs.utils.event.crystal.PlayerCrystalDeathEvent;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.entity.PlayerDeathEvent;
/*    */ import org.bukkit.event.player.PlayerRespawnEvent;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class AutoKitListener implements Listener {
/*    */   @EventHandler
/*    */   public void execute(@NotNull PlayerRespawnEvent event) {
/* 22 */     Player victim = event.getPlayer();
/* 23 */     Player attacker = victim.getKiller();
/*    */     
/* 25 */     World world = victim.getWorld();
/*    */     
/* 27 */     if (ContinuumKits.BLOCKED_WORLDS.has(world.getName()))
/*    */       return; 
/* 29 */     AutoKit victimAutoKit = AutoKits.by(victim.getUniqueId());
/* 30 */     if (victimAutoKit == null)
/* 31 */       return;  if (!victimAutoKit.enabled())
/*    */       return; 
/* 33 */     AutoKitEvent victimEvent = new AutoKitEvent(victim, victimAutoKit.kit());
/* 34 */     if (KitsAPI.api().listeners().fire((KitsEvent)victimEvent)) {
/* 35 */       victim.performCommand("kit load kit " + victimAutoKit.kit());
/*    */     }
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void execute(@NotNull PlayerAnchorDeathEvent event) {
/* 41 */     Player attacker = event.attacker();
/*    */     
/* 43 */     if (ContinuumKits.BLOCKED_WORLDS.has(attacker.getWorld().getName()))
/*    */       return; 
/* 45 */     AutoKit autoKit2 = AutoKits.by(attacker.getUniqueId());
/* 46 */     if (autoKit2 == null)
/* 47 */       return;  if (!autoKit2.enabled())
/*    */       return; 
/* 49 */     AutoKitEvent attackerEvent = new AutoKitEvent(attacker, autoKit2.kit());
/* 50 */     if (KitsAPI.api().listeners().fire((KitsEvent)attackerEvent)) {
/* 51 */       attacker.performCommand("kit load kit " + autoKit2.kit());
/*    */     }
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void execute(@NotNull PlayerCrystalDeathEvent event) {
/* 57 */     Player attacker = event.killer();
/*    */     
/* 59 */     if (ContinuumKits.BLOCKED_WORLDS.has(attacker.getWorld().getName()))
/*    */       return; 
/* 61 */     AutoKit autoKit2 = AutoKits.by(attacker.getUniqueId());
/* 62 */     if (autoKit2 == null)
/* 63 */       return;  if (!autoKit2.enabled())
/*    */       return; 
/* 65 */     AutoKitEvent attackerEvent = new AutoKitEvent(attacker, autoKit2.kit());
/* 66 */     if (KitsAPI.api().listeners().fire((KitsEvent)attackerEvent)) {
/* 67 */       attacker.performCommand("kit load kit " + autoKit2.kit());
/*    */     }
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void execute(@NotNull PlayerDeathEvent event) {
/* 73 */     Player killer = event.getPlayer().getKiller();
/* 74 */     if (killer == null)
/*    */       return; 
/* 76 */     AutoKit autoKit2 = AutoKits.by(killer.getUniqueId());
/* 77 */     if (autoKit2 == null)
/* 78 */       return;  if (!autoKit2.enabled())
/*    */       return; 
/* 80 */     AutoKitEvent attackerEvent = new AutoKitEvent(killer, autoKit2.kit());
/* 81 */     if (KitsAPI.api().listeners().fire((KitsEvent)attackerEvent))
/* 82 */       killer.performCommand("kit load kit " + autoKit2.kit()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\listener\AutoKitListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */