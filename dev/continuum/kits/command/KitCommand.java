/*     */ package dev.continuum.kits.command;
/*     */ import dev.continuum.kits.ContinuumKits;
/*     */ import dev.continuum.kits.api.event.impl.load.EnderChestLoadEvent;
/*     */ import dev.continuum.kits.api.event.impl.load.KitLoadEvent;
/*     */ import dev.continuum.kits.api.event.impl.load.PremadeKitLoadEvent;
/*     */ import dev.continuum.kits.config.Messages;
/*     */ import dev.continuum.kits.database.DatabaseProvider;
/*     */ import dev.continuum.kits.database.kit.auto.AutoKits;
/*     */ import dev.continuum.kits.database.kit.copy.CopiedKit;
/*     */ import dev.continuum.kits.database.kit.messages.KitMessages;
/*     */ import dev.continuum.kits.database.kit.premade.PremadeKit;
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.command.AbstractCommand;
/*     */ import dev.continuum.kits.libs.utils.command.CommandResult;
/*     */ import dev.continuum.kits.libs.utils.command.impl.CommandInfo;
/*     */ import dev.continuum.kits.libs.utils.command.impl.CommandResultWrapper;
/*     */ import dev.continuum.kits.libs.utils.command.impl.Commands;
/*     */ import dev.continuum.kits.libs.utils.command.impl.dispatcher.CommandContext;
/*     */ import dev.continuum.kits.libs.utils.command.impl.requirements.Requirements;
/*     */ import dev.continuum.kits.libs.utils.command.impl.suggestions.Suggestion;
/*     */ import dev.continuum.kits.libs.utils.command.impl.suggestions.Suggestions;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.misc.ObjectUtils;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import dev.continuum.kits.menu.EnderChestEditorMenu;
/*     */ import dev.continuum.kits.menu.KitEditorMenu;
/*     */ import dev.continuum.kits.menu.KitMainMenu;
/*     */ import dev.continuum.kits.menu.KitRoomMenu;
/*     */ import dev.continuum.kits.parser.Menus;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.kyori.adventure.audience.Audience;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ public class KitCommand extends AbstractCommand implements IntegerCommandDispatcher {
/*     */   @NotNull
/*     */   public Commands command() {
/*  46 */     return Commands.command("kit")
/*  47 */       .info(info -> {
/*     */           info.aliases(new String[] { "k" });
/*     */           
/*     */           info.permission("kits", "kit_command");
/*  51 */         }).requirement(Requirements.playerOnly(ctx -> {
/*     */             CommandSender sender = ctx.sender();
/*     */             
/*     */             Messages.findAndSend("player_only", (Audience)sender);
/*  55 */           })).executes((CommandDispatcher)this)
/*  56 */       .completes((SuggestionDispatcher)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int runInt(@NotNull CommandContext ctx) {
/*  61 */     List<String> args = ctx.rawArgs();
/*  62 */     Player player = ctx.player();
/*     */     
/*  64 */     if (ContinuumKits.BLOCKED_WORLDS.has(player.getWorld().getName())) return -1;
/*     */     
/*  66 */     if (args.isEmpty()) {
/*  67 */       new KitMainMenu(player);
/*  68 */       return -1;
/*     */     } 
/*     */     
/*  71 */     if (args.size() == 1) {
/*  72 */       String action = args.get(0);
/*     */       
/*  74 */       if (action.equalsIgnoreCase("room")) {
/*  75 */         new KitRoomMenu(player);
/*  76 */         return -1;
/*  77 */       }  if (action.equalsIgnoreCase("menu")) {
/*  78 */         new KitMainMenu(player);
/*  79 */         return -1;
/*  80 */       }  if (action.equalsIgnoreCase("premade") || action
/*  81 */         .equalsIgnoreCase("premadekit") || action
/*  82 */         .equalsIgnoreCase("default")) {
/*     */         
/*  84 */         PremadeKitLoadEvent event = new PremadeKitLoadEvent(player);
/*  85 */         if (!event.fire()) return -1;
/*     */         
/*  87 */         PremadeKit.give(player);
/*  88 */         return -1;
/*  89 */       }  if (action.equalsIgnoreCase("messages")) {
/*  90 */         if (!KitMessages.isDisabledEntirely()) {
/*  91 */           KitMessages.toggle(player);
/*     */         }
/*     */         
/*  94 */         return -1;
/*     */       } 
/*     */       
/*  97 */       Messages.findAndSend("kit_command.usage", (Audience)player);
/*  98 */       return -1;
/*     */     } 
/*     */     
/* 101 */     List<Integer> normalSlotsList = Menus.of("kit_menu").getIntegerList("kit_button.normal_slots");
/* 102 */     List<Integer> premiumSlotsList = Menus.of("kit_menu").getIntegerList("kit_button.premium_slots");
/* 103 */     int normalSlotsSize = normalSlotsList.size();
/* 104 */     int premiumSlotsSize = premiumSlotsList.size();
/* 105 */     List<String> normalKitNumbers = new ArrayList<>();
/* 106 */     List<String> premiumKitNumbers = new ArrayList<>();
/*     */     int i;
/* 108 */     for (i = 1; i <= normalSlotsSize; i++) {
/* 109 */       normalKitNumbers.add(String.valueOf(i));
/*     */     }
/*     */     
/* 112 */     for (i = 1; i <= premiumSlotsSize; i++) {
/* 113 */       premiumKitNumbers.add(String.valueOf(i + normalSlotsSize));
/*     */     }
/*     */     
/* 116 */     List<String> totalKitNumbers = new ArrayList<>();
/*     */     
/* 118 */     totalKitNumbers.addAll(normalKitNumbers);
/* 119 */     totalKitNumbers.addAll(premiumKitNumbers);
/*     */     
/* 121 */     if (!KitMessages.isDisabledEntirely() && 
/* 122 */       args.size() == 2 && ((String)args.get(0)).equalsIgnoreCase("messages")) {
/* 123 */       String rawArg = args.get(1);
/*     */       
/* 125 */       if (!List.<String>of("on", "off", "yes", "no", "y", "n", "true", "false").contains(rawArg.toLowerCase())) {
/* 126 */         Messages.findAndSend("kit_command.usage", (Audience)player);
/* 127 */         return -1;
/*     */       } 
/*     */       
/* 130 */       if (List.<String>of("on", "yes", "y", "true").contains(rawArg.toLowerCase())) { KitMessages.enable(player); }
/* 131 */       else { KitMessages.disable(player); }
/*     */       
/* 133 */       return -1;
/*     */     } 
/*     */ 
/*     */     
/* 137 */     if (args.size() == 3 && ((String)args.get(0)).equalsIgnoreCase("copy")) {
/* 138 */       int kit; String rawKit = args.get(1);
/* 139 */       String code = args.get(2);
/*     */       
/* 141 */       if (!totalKitNumbers.contains(rawKit)) {
/* 142 */         Messages.findAndSend("kit_command.usage", (Audience)player);
/* 143 */         return -1;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 149 */         kit = Integer.parseInt(rawKit);
/* 150 */       } catch (NumberFormatException e) {
/* 151 */         Messages.findAndSend("kit_command.usage", (Audience)player);
/* 152 */         return -1;
/*     */       } 
/*     */       
/* 155 */       if (premiumKitNumbers.contains(rawKit) && !player.hasPermission("kits.premium")) {
/* 156 */         Messages.findAndSend("locked_kit", (Audience)player);
/* 157 */         return -1;
/*     */       } 
/*     */       
/* 160 */       CopiedKit.copy(code, player, kit);
/* 161 */       return -1;
/*     */     } 
/*     */     
/* 164 */     if (args.size() == 3 && ((String)args.get(0)).equalsIgnoreCase("edit")) {
/* 165 */       String type = args.get(1);
/*     */       
/* 167 */       if (type == null) {
/* 168 */         Messages.findAndSend("kit_command.usage", (Audience)player);
/* 169 */         return -1;
/*     */       } 
/*     */       
/* 172 */       if (!List.<String>of("kit", "enderchest").contains(type)) {
/* 173 */         Messages.findAndSend("kit_command.usage", (Audience)player);
/* 174 */         return -1;
/*     */       } 
/*     */       
/* 177 */       if (type.equalsIgnoreCase("kit")) {
/* 178 */         int kit; String rawKit = args.get(2);
/*     */         
/* 180 */         if (!totalKitNumbers.contains(rawKit)) {
/* 181 */           Messages.findAndSend("kit_command.usage", (Audience)player);
/* 182 */           return -1;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 188 */           kit = Integer.parseInt(rawKit);
/* 189 */         } catch (NumberFormatException e) {
/* 190 */           Messages.findAndSend("kit_command.usage", (Audience)player);
/* 191 */           return -1;
/*     */         } 
/*     */         
/* 194 */         if (premiumKitNumbers.contains(rawKit) && !player.hasPermission("kits.premium")) {
/* 195 */           Messages.findAndSend("locked_kit", (Audience)player);
/* 196 */           return -1;
/*     */         } 
/*     */         
/* 199 */         new KitEditorMenu(player, kit);
/* 200 */         return -1;
/* 201 */       }  if (type.equalsIgnoreCase("enderchest") || type.equalsIgnoreCase("ec")) {
/* 202 */         int kit; List<Integer> slots = Menus.of("kit_menu").getIntegerList("ender_chest_button.slots");
/* 203 */         List<Integer> numbers = new ArrayList<>();
/* 204 */         List<String> rawNumbers = new ArrayList<>();
/*     */         
/* 206 */         for (int rawSlot = 1; rawSlot <= slots.size(); rawSlot++) {
/* 207 */           numbers.add(Integer.valueOf(rawSlot));
/*     */         }
/*     */         
/* 210 */         for (Iterator<Integer> iterator = numbers.iterator(); iterator.hasNext(); ) { int number = ((Integer)iterator.next()).intValue();
/* 211 */           rawNumbers.add(String.valueOf(number)); }
/*     */ 
/*     */         
/* 214 */         String rawKit = args.get(2);
/*     */         
/* 216 */         if (!rawNumbers.contains(rawKit)) {
/* 217 */           Messages.findAndSend("kit_command.usage", (Audience)player);
/* 218 */           return -1;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 224 */           kit = Integer.parseInt(rawKit);
/* 225 */         } catch (NumberFormatException e) {
/* 226 */           Messages.findAndSend("kit_command.usage", (Audience)player);
/* 227 */           return -1;
/*     */         } 
/*     */         
/* 230 */         new EnderChestEditorMenu(player, kit);
/* 231 */         return -1;
/*     */       } 
/* 233 */       Messages.findAndSend("kit_command.usage", (Audience)player);
/* 234 */       return -1;
/*     */     } 
/*     */ 
/*     */     
/* 238 */     if (args.size() == 3 && ((String)args.get(0)).equalsIgnoreCase("load")) {
/* 239 */       String type = args.get(1);
/*     */       
/* 241 */       if (type == null) {
/* 242 */         Messages.findAndSend("kit_command.usage", (Audience)player);
/* 243 */         return -1;
/*     */       } 
/*     */       
/* 246 */       if (!List.<String>of("kit", "enderchest").contains(type)) {
/* 247 */         Messages.findAndSend("kit_command.usage", (Audience)player);
/* 248 */         return -1;
/*     */       } 
/*     */       
/* 251 */       if (type.equalsIgnoreCase("kit")) {
/* 252 */         int kit; String rawKit = args.get(2);
/*     */         
/* 254 */         if (!totalKitNumbers.contains(rawKit)) {
/* 255 */           Messages.findAndSend("kit_command.usage", (Audience)player);
/* 256 */           return -1;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 262 */           kit = Integer.parseInt(rawKit);
/* 263 */         } catch (NumberFormatException e) {
/* 264 */           Messages.findAndSend("kit_command.usage", (Audience)player);
/* 265 */           return -1;
/*     */         } 
/*     */         
/* 268 */         if (premiumKitNumbers.contains(rawKit) && !player.hasPermission("kits.premium")) {
/* 269 */           Messages.findAndSend("locked_kit", (Audience)player);
/* 270 */           return -1;
/*     */         } 
/*     */ 
/*     */         
/* 274 */         Cachable<Integer, ItemStack> data = DatabaseProvider.database().data(player.getUniqueId(), kit);
/*     */         
/* 276 */         if (data == null) {
/* 277 */           Messages.findAndSend("kit_does_not_exist", (Elements)Elements.of((Object[])new Tuple[] {
/* 278 */                   Tuple.tuple("kit", 
/*     */                     
/* 280 */                     String.valueOf(kit))
/*     */                 }), (Audience)player);
/*     */           
/* 283 */           return -1;
/*     */         } 
/*     */         
/* 286 */         KitLoadEvent event = new KitLoadEvent(player, kit);
/* 287 */         if (!event.fire()) return -1;
/*     */         
/* 289 */         player.getOpenInventory().getBottomInventory().clear();
/*     */         
/* 291 */         for (Map.Entry<Integer, ItemStack> entry : (Iterable<Map.Entry<Integer, ItemStack>>)data.snapshot().asMap().entrySet()) {
/* 292 */           int index = ((Integer)entry.getKey()).intValue();
/* 293 */           ItemStack item = (ItemStack)ObjectUtils.defaultIfNull(entry.getValue(), new ItemStack(Material.AIR));
/*     */           
/* 295 */           player.getOpenInventory().getBottomInventory().setItem(index, item);
/*     */         } 
/*     */         
/* 298 */         KitMessages.broadcast(player, kit);
/* 299 */         return -1;
/* 300 */       }  if (type.equalsIgnoreCase("enderchest") || type.equalsIgnoreCase("ec")) {
/* 301 */         int kit; List<Integer> slots = Menus.of("kit_menu").getIntegerList("ender_chest_button.slots");
/* 302 */         List<Integer> numbers = new ArrayList<>();
/* 303 */         List<String> rawNumbers = new ArrayList<>();
/*     */         
/* 305 */         for (int rawSlot = 1; rawSlot <= slots.size(); rawSlot++) {
/* 306 */           numbers.add(Integer.valueOf(rawSlot));
/*     */         }
/*     */         
/* 309 */         for (Iterator<Integer> iterator = numbers.iterator(); iterator.hasNext(); ) { int number = ((Integer)iterator.next()).intValue();
/* 310 */           rawNumbers.add(String.valueOf(number)); }
/*     */ 
/*     */         
/* 313 */         String rawKit = args.get(2);
/*     */         
/* 315 */         if (!rawNumbers.contains(rawKit)) {
/* 316 */           Messages.findAndSend("kit_command.usage", (Audience)player);
/* 317 */           return -1;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 323 */           kit = Integer.parseInt(rawKit);
/* 324 */         } catch (NumberFormatException e) {
/* 325 */           Messages.findAndSend("kit_command.usage", (Audience)player);
/* 326 */           return -1;
/*     */         } 
/*     */ 
/*     */         
/* 330 */         Cachable<Integer, ItemStack> data = DatabaseProvider.ecDatabase().data(player.getUniqueId(), kit);
/*     */         
/* 332 */         if (data == null) {
/* 333 */           Messages.findAndSend("ender_chest_does_not_exist", (Elements)Elements.of((Object[])new Tuple[] {
/* 334 */                   Tuple.tuple("ender_chest", 
/*     */                     
/* 336 */                     String.valueOf(kit))
/*     */                 }), (Audience)player);
/*     */           
/* 339 */           return -1;
/*     */         } 
/*     */         
/* 342 */         EnderChestLoadEvent event = new EnderChestLoadEvent(player, kit);
/* 343 */         if (!event.fire()) return -1;
/*     */         
/* 345 */         Inventory playerEnderChest = player.getEnderChest();
/*     */         
/* 347 */         playerEnderChest.clear();
/*     */         
/* 349 */         for (Map.Entry<Integer, ItemStack> entry : (Iterable<Map.Entry<Integer, ItemStack>>)data.snapshot().asMap().entrySet()) {
/* 350 */           playerEnderChest.setItem(((Integer)entry.getKey()).intValue(), (ItemStack)ObjectUtils.defaultIfNull(entry
/* 351 */                 .getValue(), new ItemStack(Material.AIR)));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 356 */         Messages.findAndSend("equipped_ender_chest", (Elements)Elements.of((Object[])new Tuple[] {
/* 357 */                 Tuple.tuple("ender_chest", rawKit)
/*     */               }), (Audience)player);
/*     */ 
/*     */ 
/*     */         
/* 362 */         return -1;
/*     */       } 
/* 364 */       Messages.findAndSend("kit_command.usage", (Audience)player);
/* 365 */       return -1;
/*     */     } 
/*     */ 
/*     */     
/* 369 */     if (args.size() == 2 && ((String)args.get(0)).equalsIgnoreCase("auto")) {
/* 370 */       int kit; if (((String)args.get(1)).equalsIgnoreCase("on")) {
/* 371 */         AutoKits.on(player);
/* 372 */         Messages.findAndSend("auto_kit_on", (Audience)player);
/* 373 */         return -1;
/* 374 */       }  if (((String)args.get(1)).equalsIgnoreCase("off")) {
/* 375 */         AutoKits.off(player);
/* 376 */         Messages.findAndSend("auto_kit_off", (Audience)player);
/* 377 */         return -1;
/*     */       } 
/* 379 */       String rawKit = args.get(1);
/*     */       
/* 381 */       if (!totalKitNumbers.contains(rawKit)) {
/* 382 */         Messages.findAndSend("kit_command.usage", (Audience)player);
/* 383 */         return -1;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 389 */         kit = Integer.parseInt(rawKit);
/* 390 */       } catch (NumberFormatException e) {
/* 391 */         NumberFormatException numberFormatException1; Messages.findAndSend("kit_command.usage", (Audience)player);
/* 392 */         return -1;
/*     */       } 
/*     */       
/* 395 */       if (premiumKitNumbers.contains(rawKit) && !player.hasPermission("kits.premium")) {
/* 396 */         Messages.findAndSend("locked_kit", (Audience)player);
/* 397 */         return -1;
/*     */       } 
/*     */       
/* 400 */       AutoKits.edit(player, kit);
/* 401 */       Messages.findAndSend("autokit_set", (Elements)Elements.of((Object[])new Tuple[] {
/* 402 */               Tuple.tuple("kit", rawKit)
/*     */             }), (Audience)player);
/*     */ 
/*     */ 
/*     */       
/* 407 */       return -1;
/*     */     } 
/*     */ 
/*     */     
/* 411 */     return -1;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public CommandResult run(@NotNull CommandContext ctx) {
/* 416 */     return CommandResultWrapper.wrap(runInt(ctx));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Suggestions suggest(@NotNull CommandContext ctx) {
/* 422 */     List<Integer> normalSlotsList = Menus.of("kit_menu").getIntegerList("kit_button.normal_slots");
/* 423 */     List<Integer> premiumSlotsList = Menus.of("kit_menu").getIntegerList("kit_button.premium_slots");
/* 424 */     List<String> totalKitNumbers = totalKitNumbers(normalSlotsList, premiumSlotsList);
/*     */     
/* 426 */     List<Integer> slots = Menus.of("kit_menu").getIntegerList("ender_chest_button.slots");
/* 427 */     List<Integer> numbers = new ArrayList<>();
/* 428 */     List<String> rawNumbers = new ArrayList<>();
/*     */     
/* 430 */     for (int rawSlot = 1; rawSlot <= slots.size(); rawSlot++) {
/* 431 */       numbers.add(Integer.valueOf(rawSlot));
/*     */     }
/*     */     
/* 434 */     for (Iterator<Integer> iterator = numbers.iterator(); iterator.hasNext(); ) { int number = ((Integer)iterator.next()).intValue();
/* 435 */       rawNumbers.add(String.valueOf(number)); }
/*     */ 
/*     */     
/* 438 */     if (ctx.size() == 1) {
/* 439 */       if (!KitMessages.isDisabledEntirely()) {
/* 440 */         return Suggestions.of(new String[] { "room", "menu", "premade", "premadekit", "default", "copy", "messages", "edit", "load", "auto" });
/*     */       }
/* 442 */       return Suggestions.of(new String[] { "room", "menu", "premade", "premadekit", "default", "copy", "edit", "load", "auto" });
/*     */     } 
/*     */ 
/*     */     
/* 446 */     if (ctx.size() == 2) {
/* 447 */       if (((String)ctx.rawArgs().get(0)).equals("messages")) {
/* 448 */         return Suggestions.of(new String[] { "on", "off", "yes", "no", "y", "n", "true", "false" });
/*     */       }
/*     */       
/* 451 */       if (((String)ctx.rawArgs().get(0)).equals("auto")) {
/* 452 */         return Suggestions.wrap(totalKitNumbers).add(new Suggestion[] { Suggestion.text("on"), Suggestion.text("off") });
/*     */       }
/*     */     } 
/*     */     
/* 456 */     if (ctx.size() == 2 && ((
/* 457 */       (String)ctx.rawArgs().get(0)).equals("load") || ((String)ctx.rawArgs().get(0)).equals("edit"))) {
/* 458 */       return Suggestions.of(new String[] { "kit", "enderchest", "ec" });
/*     */     }
/*     */ 
/*     */     
/* 462 */     if (ctx.size() == 3 && ((
/* 463 */       (String)ctx.rawArgs().get(0)).equals("load") || ((String)ctx.rawArgs().get(0)).equals("edit"))) {
/* 464 */       return Suggestions.wrap(rawNumbers);
/*     */     }
/*     */ 
/*     */     
/* 468 */     if (ctx.size() == 3 && (
/* 469 */       (String)ctx.rawArgs().get(0)).equals("copy")) {
/* 470 */       return Suggestions.of("<code>");
/*     */     }
/*     */ 
/*     */     
/* 474 */     if (ctx.size() == 2 && (
/* 475 */       (String)ctx.rawArgs().get(0)).equals("copy")) {
/* 476 */       return Suggestions.wrap(totalKitNumbers);
/*     */     }
/*     */ 
/*     */     
/* 480 */     return Suggestions.empty();
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   private static List<String> totalKitNumbers(List<Integer> normalSlotsList, List<Integer> premiumSlotsList) {
/* 485 */     int normalSlotsSize = normalSlotsList.size();
/* 486 */     int premiumSlotsSize = premiumSlotsList.size();
/* 487 */     List<String> normalKitNumbers = new ArrayList<>();
/* 488 */     List<String> premiumKitNumbers = new ArrayList<>();
/*     */     int i;
/* 490 */     for (i = 1; i <= normalSlotsSize; i++) {
/* 491 */       normalKitNumbers.add(String.valueOf(i));
/*     */     }
/*     */ 
/*     */     
/* 495 */     for (i = 1; i <= premiumSlotsSize; i++) {
/* 496 */       premiumKitNumbers.add(String.valueOf(i + normalSlotsSize));
/*     */     }
/*     */     
/* 499 */     List<String> totalKitNumbers = new ArrayList<>();
/*     */     
/* 501 */     totalKitNumbers.addAll(normalKitNumbers);
/* 502 */     totalKitNumbers.addAll(premiumKitNumbers);
/* 503 */     return totalKitNumbers;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\command\KitCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */