/*    */ package dev.continuum.kits;
/*    */ import dev.continuum.kits.api.event.Events.Subscribe;
/*    */ import dev.continuum.kits.api.event.impl.edit.EnderChestEditEvent;
/*    */ import dev.continuum.kits.api.event.impl.edit.KitEditEvent;
/*    */ import dev.continuum.kits.api.event.impl.menu.KitRoomMenuEvent;
/*    */ import dev.continuum.kits.config.Messages;
/*    */ import dev.continuum.kits.libs.utils.elements.Elements;
/*    */ import dev.continuum.kits.libs.utils.model.Tuple;
/*    */ import net.kyori.adventure.audience.Audience;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class BroadcastListener {
/*    */   @Subscribe
/*    */   public void execute(@NotNull KitRoomMenuEvent event) {
/* 16 */     Messages.findAndSend("kit_room_opened_broadcast", (Elements)Elements.of((Object[])new Tuple[] {
/* 17 */             Tuple.tuple("player", event.player().getName())
/* 18 */           }), (Audience)Bukkit.getServer());
/*    */   }
/*    */   
/*    */   @Subscribe
/*    */   public void execute(@NotNull KitEditEvent event) {
/* 23 */     Messages.findAndSend("kit_edit_broadcast", (Elements)Elements.of((Object[])new Tuple[] {
/* 24 */             Tuple.tuple("player", event.player().getName()), 
/* 25 */             Tuple.tuple("kit", String.valueOf(event.kit()))
/* 26 */           }), (Audience)Bukkit.getServer());
/*    */   }
/*    */   
/*    */   @Subscribe
/*    */   public void execute(@NotNull EnderChestEditEvent event) {
/* 31 */     Messages.findAndSend("ender_chest_edit_broadcast", (Elements)Elements.of((Object[])new Tuple[] {
/* 32 */             Tuple.tuple("player", event.player().getName()), 
/* 33 */             Tuple.tuple("enderchest", String.valueOf(event.enderChest()))
/* 34 */           }), (Audience)Bukkit.getServer());
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\BroadcastListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */