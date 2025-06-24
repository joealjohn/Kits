/*    */ package dev.continuum.kits.config;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class TimeUnitParser
/*    */ {
/*    */   @NotNull
/*    */   public static TimeUnit parse(@NotNull String text) {
/* 10 */     switch (text) { case "hours": case "hour": case "h": case "minutes": case "minute": case "min": case "m":  }  return 
/*    */ 
/*    */       
/* 13 */       TimeUnit.SECONDS;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\config\TimeUnitParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */