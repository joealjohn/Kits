/*     */ package dev.continuum.kits.libs.utils.event.crystal;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.event.HandlerList;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.PlayerDeathEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerCrystalDeathEvent
/*     */   extends Event
/*     */ {
/*  20 */   private static final HandlerList HANDLERS = new HandlerList();
/*     */ 
/*     */ 
/*     */   
/*     */   private final Player killer;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Player victim;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Entity crystal;
/*     */ 
/*     */   
/*     */   private final EntityDamageByEntityEvent playerDamageEvent;
/*     */ 
/*     */   
/*     */   private final PlayerDeathEvent playerDeathEvent;
/*     */ 
/*     */ 
/*     */   
/*     */   public PlayerCrystalDeathEvent(Player killer, Player victim, Entity crystal, EntityDamageByEntityEvent playerDamageEvent, PlayerDeathEvent playerDeathEvent) {
/*  43 */     this.killer = killer;
/*  44 */     this.victim = victim;
/*  45 */     this.crystal = crystal;
/*  46 */     this.playerDeathEvent = playerDeathEvent;
/*  47 */     this.playerDamageEvent = playerDamageEvent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HandlerList getHandlerList() {
/*  56 */     return HANDLERS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public HandlerList getHandlers() {
/*  66 */     return HANDLERS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public HandlerList handlers() {
/*  75 */     return getHandlers();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public String getEventName() {
/*  83 */     return "PlayerCrystalDeathEvent";
/*     */   }
/*     */   @NotNull
/*     */   public String eventName() {
/*  87 */     return getEventName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deathMessage(Component deathMessage) {
/*  96 */     this.playerDeathEvent.deathMessage(deathMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Component deathMessage() {
/* 105 */     return this.playerDeathEvent.deathMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Player killer() {
/* 114 */     return this.killer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Player victim() {
/* 123 */     return this.victim;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity crystal() {
/* 132 */     return this.crystal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keepInventory() {
/* 141 */     return this.playerDeathEvent.getKeepInventory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void keepInventory(boolean keepInventory) {
/* 150 */     this.playerDeathEvent.setKeepInventory(keepInventory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keepLevel() {
/* 159 */     return this.playerDeathEvent.getKeepLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void keepLevel(boolean keepLevel) {
/* 168 */     this.playerDeathEvent.setKeepLevel(keepLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int newExp() {
/* 177 */     return this.playerDeathEvent.getNewExp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void newExp(int exp) {
/* 186 */     this.playerDeathEvent.setNewExp(exp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int newLevel() {
/* 195 */     return this.playerDeathEvent.getNewLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void newLevel(int level) {
/* 204 */     this.playerDeathEvent.setNewLevel(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int newTotalExp() {
/* 213 */     return this.playerDeathEvent.getNewTotalExp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void newTotalExp(int totalExp) {
/* 222 */     this.playerDeathEvent.setNewTotalExp(totalExp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int droppedExp() {
/* 231 */     return this.playerDeathEvent.getDroppedExp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void droppedExp(int exp) {
/* 240 */     this.playerDeathEvent.setDroppedExp(exp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ItemStack> drops() {
/* 249 */     return this.playerDeathEvent.getDrops();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double damage() {
/* 258 */     return this.playerDamageEvent.getDamage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double finalDamage() {
/* 267 */     return this.playerDamageEvent.getFinalDamage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSuicide() {
/* 276 */     return (this.killer == this.victim);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\event\crystal\PlayerCrystalDeathEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */