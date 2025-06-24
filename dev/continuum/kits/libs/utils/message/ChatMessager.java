/*    */ package dev.continuum.kits.libs.utils.message;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.server.Servers;
/*    */ import net.kyori.adventure.text.Component;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChatMessager
/*    */ {
/*    */   private final Player player;
/*    */   
/*    */   public ChatMessager(@NotNull Player player) {
/* 20 */     this.player = player;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static ChatMessager of(@NotNull Player player) {
/* 30 */     return new ChatMessager(player);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void send(@NotNull Component text) {
/* 39 */     this.player.sendMessage(text);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void broadcast(@NotNull Component text) {
/* 48 */     Servers.online().forEach(to -> to.sendMessage(text));
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\message\ChatMessager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */