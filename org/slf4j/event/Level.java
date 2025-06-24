/*    */ package org.slf4j.event;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum Level
/*    */ {
/* 18 */   ERROR(40, "ERROR"), WARN(30, "WARN"), INFO(20, "INFO"), DEBUG(10, "DEBUG"), TRACE(0, "TRACE");
/*    */   
/*    */   private final String levelStr;
/*    */   private final int levelInt;
/*    */   
/*    */   Level(int i, String s) {
/* 24 */     this.levelInt = i;
/* 25 */     this.levelStr = s;
/*    */   }
/*    */   
/*    */   public int toInt() {
/* 29 */     return this.levelInt;
/*    */   }
/*    */   
/*    */   public static Level intToLevel(int levelInt) {
/* 33 */     switch (levelInt) {
/*    */       case 0:
/* 35 */         return TRACE;
/*    */       case 10:
/* 37 */         return DEBUG;
/*    */       case 20:
/* 39 */         return INFO;
/*    */       case 30:
/* 41 */         return WARN;
/*    */       case 40:
/* 43 */         return ERROR;
/*    */     } 
/* 45 */     throw new IllegalArgumentException("Level integer [" + levelInt + "] not recognized.");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 53 */     return this.levelStr;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\org\slf4j\event\Level.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */