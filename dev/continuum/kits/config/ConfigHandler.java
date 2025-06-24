/*    */ package dev.continuum.kits.config;
/*    */ 
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import dev.continuum.kits.libs.utils.library.Utils;
/*    */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*    */ import dev.continuum.kits.libs.utils.text.color.TextStyle;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.function.Function;
/*    */ import net.kyori.adventure.text.Component;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ public class ConfigHandler
/*    */ {
/* 21 */   private static final Map<Class<?>, Function<Object, ?>> typeConverters = new HashMap<>();
/*    */   
/*    */   static {
/* 24 */     typeConverters.put(int.class, object -> {
/*    */           Integer integer = (Integer)object; return (object instanceof Integer) ? integer : Integer.valueOf(Integer.parseInt(object.toString()));
/* 26 */         }); typeConverters.put(Integer.class, object -> {
/*    */           Integer integer = (Integer)object; return (object instanceof Integer) ? integer : Integer.valueOf(Integer.parseInt(object.toString()));
/* 28 */         }); typeConverters.put(Component.class, object -> {
/*    */           Component component = (Component)object; return (object instanceof Component) ? component : TextStyle.style((String)object);
/* 30 */         }); typeConverters.put(String.class, object -> {
/*    */           String string = (String)object; return (object instanceof String) ? string : object;
/* 32 */         }); typeConverters.put(boolean.class, object -> {
/*    */           Boolean bool = (Boolean)object; return (object instanceof Boolean) ? bool : Boolean.valueOf(Boolean.parseBoolean(object.toString()));
/* 34 */         }); typeConverters.put(Boolean.class, object -> {
/*    */           Boolean bool = (Boolean)object; return (object instanceof Boolean) ? bool : Boolean.valueOf(Boolean.parseBoolean(object.toString()));
/* 36 */         }); typeConverters.put(TimeUnit.class, object -> { String string = (String)object; switch (string.toLowerCase()) { case "hours": case "hour":
/*    */             case "h":
/*    */             
/*    */             case "minutes":
/*    */             case "minute":
/*    */             case "min":
/*    */             case "m":
/*    */             
/*    */             default:
/* 45 */               break; }  return (object instanceof String) ? TimeUnit.SECONDS : TimeUnit.SECONDS; }); } @CanIgnoreReturnValue @NotNull public static FileConfiguration config() { return config(Utils.plugin()); }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   @NotNull
/*    */   public static FileConfiguration config(@NotNull JavaPlugin plugin) {
/* 51 */     return (FileConfiguration)Schedulers.async().supply(() -> {
/*    */           plugin.saveDefaultConfig();
/*    */           plugin.reloadConfig();
/*    */           return plugin.getConfig();
/*    */         });
/*    */   }
/*    */   @NotNull
/*    */   public static <T> T as(@Nullable Class<T> type, @NotNull String path) {
/* 59 */     Object value = config().get(path);
/*    */     
/* 61 */     if (type == null) {
/* 62 */       if (value != null) {
/* 63 */         return at(path);
/*    */       }
/*    */       
/* 66 */       return at(path);
/*    */     } 
/*    */     
/* 69 */     if (typeConverters.containsKey(type)) {
/* 70 */       if (value != null) {
/* 71 */         return type.cast(((Function)typeConverters.get(type)).apply(value));
/*    */       }
/*    */       
/* 74 */       return type.cast(new Object() {  });
/*    */     } 
/* 76 */     return Objects.requireNonNull((T)config().getObject(path, type), "Value at path '" + path + "' is null");
/*    */   }
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static <T> T at(@NotNull String path) {
/* 82 */     return Objects.requireNonNull((T)config().get(path));
/*    */   }
/*    */   @NotNull
/*    */   public static List<String> asList(@NotNull String path) {
/* 86 */     FileConfiguration configuration = config();
/* 87 */     return configuration.getStringList(path);
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\config\ConfigHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */