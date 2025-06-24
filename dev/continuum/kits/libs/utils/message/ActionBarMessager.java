/*    */ package dev.continuum.kits.libs.utils.message;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*    */ import dev.continuum.kits.libs.utils.server.Servers;
/*    */ import dev.continuum.kits.libs.utils.text.color.TextStyle;
/*    */ import net.kyori.adventure.text.Component;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.scheduler.BukkitTask;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ActionBarMessager
/*    */ {
/*    */   private final Player player;
/*    */   
/*    */   public ActionBarMessager(@NotNull Player player) {
/* 22 */     this.player = player;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static ActionBarMessager of(@NotNull Player player) {
/* 32 */     return new ActionBarMessager(player);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void send(@NotNull Component message) {
/* 41 */     this.player.sendActionBar(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void clear() {
/* 48 */     send(TextStyle.component(" "));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void send(@NotNull Component text, int after, int every) {
/* 59 */     Schedulers.async().execute(task -> send(text), after, every);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void broadcast(@NotNull Component text) {
/* 68 */     Servers.online().forEach(to -> to.sendActionBar(text));
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\message\ActionBarMessager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */