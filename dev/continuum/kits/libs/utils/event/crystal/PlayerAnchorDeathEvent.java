/*     */ package dev.continuum.kits.libs.utils.event.crystal;
/*     */ 
/*     */ import org.bukkit.block.BlockState;
/*     */ import org.bukkit.block.data.type.RespawnAnchor;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.event.HandlerList;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerAnchorDeathEvent
/*     */   extends Event
/*     */ {
/*  18 */   private static final HandlerList HANDLERS = new HandlerList();
/*     */   
/*     */   @NotNull
/*     */   private final BlockState block;
/*     */   
/*     */   @NotNull
/*     */   private final Player victim;
/*     */   
/*     */   @NotNull
/*     */   private final Player attacker;
/*     */   @Nullable
/*     */   private final RespawnAnchor anchor;
/*     */   
/*     */   public PlayerAnchorDeathEvent(@NotNull BlockState block, @NotNull Player victim, @NotNull Player attacker) {
/*  32 */     this.block = block;
/*  33 */     this.victim = victim;
/*  34 */     this.attacker = attacker;
/*     */     
/*  36 */     BlockState blockState = this.block; if (blockState instanceof RespawnAnchor) { RespawnAnchor respawnAnchor = (RespawnAnchor)blockState;
/*  37 */       this.anchor = respawnAnchor; }
/*     */     else
/*  39 */     { this.anchor = null; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HandlerList getHandlerList() {
/*  49 */     return HANDLERS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public HandlerList getHandlers() {
/*  59 */     return HANDLERS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public HandlerList handlers() {
/*  68 */     return getHandlers();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public String getEventName() {
/*  78 */     return "PlayerAnchorDeathEvent";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public String eventName() {
/*  87 */     return getEventName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public BlockState block() {
/*  96 */     return this.block;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Player victim() {
/* 105 */     return this.victim;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Player attacker() {
/* 114 */     return this.attacker;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public RespawnAnchor anchor() {
/* 123 */     return this.anchor;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\event\crystal\PlayerAnchorDeathEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */