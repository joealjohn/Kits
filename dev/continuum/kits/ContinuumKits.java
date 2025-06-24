/*     */ package dev.continuum.kits;
/*     */ import dev.continuum.kits.api.KitsAPI;
/*     */ import dev.continuum.kits.category.KitRoomCategories;
/*     */ import dev.continuum.kits.command.AdminCommand;
/*     */ import dev.continuum.kits.command.KitCommand;
/*     */ import dev.continuum.kits.config.ConfigHandler;
/*     */ import dev.continuum.kits.config.Messages;
/*     */ import dev.continuum.kits.database.DatabaseProvider;
/*     */ import dev.continuum.kits.database.kit.auto.AutoKits;
/*     */ import dev.continuum.kits.database.kit.messages.KitMessages;
/*     */ import dev.continuum.kits.libs.utils.command.AbstractCommand;
/*     */ import dev.continuum.kits.libs.utils.command.CommandResult;
/*     */ import dev.continuum.kits.libs.utils.command.impl.CommandInfo;
/*     */ import dev.continuum.kits.libs.utils.command.impl.Commands;
/*     */ import dev.continuum.kits.libs.utils.command.impl.dispatcher.CommandContext;
/*     */ import dev.continuum.kits.libs.utils.command.impl.requirements.Requirements;
/*     */ import dev.continuum.kits.libs.utils.command.impl.suggestions.Suggestions;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.library.Utils;
/*     */ import dev.continuum.kits.libs.utils.registration.Registrar;
/*     */ import dev.continuum.kits.libs.utils.server.Servers;
/*     */ import dev.continuum.kits.parser.Menus;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.kyori.adventure.audience.Audience;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ public final class ContinuumKits extends PluginWrapper {
/*  33 */   public static Elements<String> BLOCKED_WORLDS = (Elements<String>)Elements.of(String.class);
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start() {
/*  38 */     if (Utils.plugin() == null) Utils.init((JavaPlugin)this);
/*     */     
/*  40 */     if (!getDataFolder().exists()) getDataFolder().mkdir(); 
/*  41 */     defaults();
/*     */     
/*  43 */     ConfigHandler.config();
/*  44 */     Messages.config();
/*  45 */     KitRoomCategories.config();
/*  46 */     DatabaseProvider.start();
/*  47 */     KitMessages.start();
/*  48 */     AutoKits.start();
/*     */     
/*  50 */     BLOCKED_WORLDS = (Elements<String>)Elements.of(ConfigHandler.asList("blocked_worlds"));
/*     */     
/*  52 */     AbstractCommand.register(KitCommand.class);
/*  53 */     AbstractCommand.register(AdminCommand.class);
/*     */     
/*  55 */     kitCommands();
/*  56 */     ecCommands();
/*     */     
/*  58 */     api().listeners().register(BroadcastListener.class);
/*     */     
/*  60 */     Registrar.events((Listener)new AutoKitListener());
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public KitsAPI api() {
/*  65 */     return KitsAPI.api();
/*     */   }
/*     */   
/*     */   public void kitCommands() {
/*  69 */     List<Integer> normalSlotsList = Menus.of("kit_menu").getIntegerList("kit_button.normal_slots");
/*  70 */     List<Integer> premiumSlotsList = Menus.of("kit_menu").getIntegerList("kit_button.premium_slots");
/*  71 */     List<String> totalKitNumbers = totalKitNumbers(normalSlotsList, premiumSlotsList);
/*     */     
/*  73 */     for (String rawKit : totalKitNumbers) {
/*  74 */       String name = "kit" + rawKit;
/*  75 */       List<String> alias = new ArrayList<>(List.of("k" + rawKit));
/*     */       
/*  77 */       Commands.command(name)
/*  78 */         .info(info -> info.aliases(alias))
/*  79 */         .requirement(Requirements.playerOnly(ctx -> {
/*     */               CommandSender sender = ctx.sender();
/*     */               
/*     */               Messages.findAndSend("player_only", (Audience)sender);
/*  83 */             })).executes(ctx -> {
/*     */             ctx.player().performCommand("kit load kit " + rawKit);
/*     */             
/*     */             return CommandResult.stop();
/*  87 */           }).completes(ctx -> Suggestions.of())
/*  88 */         .register();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void ecCommands() {
/*  93 */     List<Integer> slots = Menus.of("kit_menu").getIntegerList("ender_chest_button.slots");
/*     */     
/*  95 */     for (int rawSlot = 1; rawSlot <= slots.size(); rawSlot++) {
/*  96 */       String name = "enderchest" + rawSlot;
/*  97 */       List<String> alias = new ArrayList<>(List.of("ec" + rawSlot));
/*     */       
/*  99 */       int finalRawSlot = rawSlot;
/* 100 */       Commands.command(name)
/* 101 */         .info(info -> info.aliases(alias))
/* 102 */         .requirement(Requirements.playerOnly(ctx -> {
/*     */               CommandSender sender = ctx.sender();
/*     */               
/*     */               Messages.findAndSend("player_only", (Audience)sender);
/* 106 */             })).executes(ctx -> {
/*     */             ctx.player().performCommand("kit load enderchest " + finalRawSlot);
/*     */             
/*     */             return CommandResult.stop();
/* 110 */           }).completes(ctx -> Suggestions.of())
/* 111 */         .register();
/*     */     } 
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   private static List<String> totalKitNumbers(List<Integer> normalSlotsList, List<Integer> premiumSlotsList) {
/* 117 */     int normalSlotsSize = normalSlotsList.size();
/* 118 */     int premiumSlotsSize = premiumSlotsList.size();
/* 119 */     List<String> normalKitNumbers = new ArrayList<>();
/* 120 */     List<String> premiumKitNumbers = new ArrayList<>();
/*     */     int i;
/* 122 */     for (i = 1; i <= normalSlotsSize; i++) {
/* 123 */       normalKitNumbers.add(String.valueOf(i));
/*     */     }
/*     */     
/* 126 */     for (i = 1; i <= premiumSlotsSize; i++) {
/* 127 */       premiumKitNumbers.add(String.valueOf(i + normalSlotsSize));
/*     */     }
/*     */     
/* 130 */     List<String> totalKitNumbers = new ArrayList<>();
/*     */     
/* 132 */     totalKitNumbers.addAll(normalKitNumbers);
/* 133 */     totalKitNumbers.addAll(premiumKitNumbers);
/* 134 */     return totalKitNumbers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void defaults() {
/* 140 */     if (!(new File(Servers.dataFolder(), "/autokit/")).exists())
/*     */     {
/* 142 */       (new File(Servers.dataFolder(), "/autokit/"))
/* 143 */         .mkdirs();
/*     */     }
/*     */ 
/*     */     
/* 147 */     if (!(new File(Servers.dataFolder(), "/ender_chests/")).exists())
/*     */     {
/* 149 */       (new File(Servers.dataFolder(), "/ender_chests/"))
/* 150 */         .mkdirs();
/*     */     }
/*     */ 
/*     */     
/* 154 */     if (!(new File(Servers.dataFolder(), "/kits/")).exists())
/*     */     {
/* 156 */       (new File(Servers.dataFolder(), "/kits/"))
/* 157 */         .mkdirs();
/*     */     }
/*     */ 
/*     */     
/* 161 */     if (!(new File(new File(Servers.dataFolder(), "/menus/"), "kit_menu.yml")).exists())
/*     */     {
/* 163 */       saveResource("menus/kit_menu.yml", false);
/*     */     }
/*     */ 
/*     */     
/* 167 */     if (!(new File(new File(Servers.dataFolder(), "/menus/"), "kit_room.yml")).exists())
/*     */     {
/* 169 */       saveResource("menus/kit_room.yml", false);
/*     */     }
/*     */ 
/*     */     
/* 173 */     if (!(new File(new File(Servers.dataFolder(), "/menus/"), "kit_editor.yml")).exists())
/*     */     {
/* 175 */       saveResource("menus/kit_editor.yml", false);
/*     */     }
/*     */ 
/*     */     
/* 179 */     if (!(new File(new File(Servers.dataFolder(), "/menus/"), "ender_chest_editor.yml")).exists())
/*     */     {
/* 181 */       saveResource("menus/ender_chest_editor.yml", false);
/*     */     }
/*     */ 
/*     */     
/* 185 */     if (!(new File(Servers.dataFolder(), "/messages.yml")).exists())
/*     */     {
/* 187 */       saveResource("messages.yml", false);
/*     */     }
/*     */ 
/*     */     
/* 191 */     if (!(new File(Servers.dataFolder(), "/categories.yml")).exists())
/*     */     {
/* 193 */       saveResource("categories.yml", false);
/*     */     }
/*     */ 
/*     */     
/* 197 */     if (!(new File(Servers.dataFolder(), "/premade_kit.yml")).exists())
/*     */     {
/* 199 */       saveResource("premade_kit.yml", false);
/*     */     }
/*     */ 
/*     */     
/* 203 */     if (!(new File(Servers.dataFolder(), "/kit_messages.yml")).exists())
/*     */     {
/* 205 */       saveResource("kit_messages.yml", false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop() {
/* 211 */     DatabaseProvider.stop();
/* 212 */     KitMessages.stop();
/* 213 */     AutoKits.stop();
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\ContinuumKits.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */