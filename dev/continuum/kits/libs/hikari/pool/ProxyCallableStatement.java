/*    */ package dev.continuum.kits.libs.hikari.pool;
/*    */ 
/*    */ import java.sql.CallableStatement;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ProxyCallableStatement
/*    */   extends ProxyPreparedStatement
/*    */   implements CallableStatement
/*    */ {
/*    */   protected ProxyCallableStatement(ProxyConnection connection, CallableStatement statement) {
/* 30 */     super(connection, statement);
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\pool\ProxyCallableStatement.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */