/*    */ package dev.continuum.kits.config;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class ConfigOptions
/*    */ {
/*  8 */   private static final Class<Integer> INTEGER = Integer.class;
/*  9 */   private static final Class<Boolean> BOOLEAN = Boolean.class;
/* 10 */   private static final Class<String> STRING = String.class;
/* 11 */   private static final Class<TimeUnit> UNIT = TimeUnit.class;
/*    */   
/*    */   @NotNull
/*    */   public static Integer expiresIn() {
/* 15 */     TimeUnit unit = ConfigHandler.<TimeUnit>as(UNIT, "copy_kit_expiration.unit");
/* 16 */     int time = ((Integer)ConfigHandler.<Integer>as(INTEGER, "copy_kit_expiration.time")).intValue();
/*    */     
/* 18 */     switch (unit) { case SECONDS: 
/*    */       case MINUTES:
/*    */       
/*    */       case HOURS:
/* 22 */        }  throw new IllegalStateException("Unexpected value: " + String.valueOf(unit));
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\config\ConfigOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */