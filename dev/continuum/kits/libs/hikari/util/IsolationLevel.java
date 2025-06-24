/*    */ package dev.continuum.kits.libs.hikari.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum IsolationLevel
/*    */ {
/* 21 */   TRANSACTION_NONE(0),
/* 22 */   TRANSACTION_READ_UNCOMMITTED(1),
/* 23 */   TRANSACTION_READ_COMMITTED(2),
/* 24 */   TRANSACTION_CURSOR_STABILITY(3),
/* 25 */   TRANSACTION_REPEATABLE_READ(4),
/* 26 */   TRANSACTION_LAST_COMMITTED(5),
/* 27 */   TRANSACTION_SERIALIZABLE(8),
/* 28 */   TRANSACTION_SQL_SERVER_SNAPSHOT_ISOLATION_LEVEL(4096);
/*    */   
/*    */   private final int levelId;
/*    */   
/*    */   IsolationLevel(int levelId) {
/* 33 */     this.levelId = levelId;
/*    */   }
/*    */   
/*    */   public int getLevelId() {
/* 37 */     return this.levelId;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikar\\util\IsolationLevel.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */