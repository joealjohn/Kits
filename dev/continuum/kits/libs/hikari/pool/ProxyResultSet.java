/*     */ package dev.continuum.kits.libs.hikari.pool;
/*     */ 
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ProxyResultSet
/*     */   implements ResultSet
/*     */ {
/*     */   protected final ProxyConnection connection;
/*     */   protected final ProxyStatement statement;
/*     */   final ResultSet delegate;
/*     */   
/*     */   protected ProxyResultSet(ProxyConnection connection, ProxyStatement statement, ResultSet resultSet) {
/*  36 */     this.connection = connection;
/*  37 */     this.statement = statement;
/*  38 */     this.delegate = resultSet;
/*     */   }
/*     */ 
/*     */   
/*     */   final SQLException checkException(SQLException e) {
/*  43 */     return this.connection.checkException(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  50 */     return getClass().getSimpleName() + "@" + getClass().getSimpleName() + " wrapping " + System.identityHashCode(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Statement getStatement() throws SQLException {
/*  61 */     return this.statement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateRow() throws SQLException {
/*  68 */     this.connection.markCommitStateDirty();
/*  69 */     this.delegate.updateRow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertRow() throws SQLException {
/*  76 */     this.connection.markCommitStateDirty();
/*  77 */     this.delegate.insertRow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteRow() throws SQLException {
/*  84 */     this.connection.markCommitStateDirty();
/*  85 */     this.delegate.deleteRow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T unwrap(Class<T> iface) throws SQLException {
/*  93 */     if (iface.isInstance(this.delegate)) {
/*  94 */       return (T)this.delegate;
/*     */     }
/*  96 */     if (this.delegate != null) {
/*  97 */       return this.delegate.unwrap(iface);
/*     */     }
/*     */     
/* 100 */     throw new SQLException("Wrapped ResultSet is not an instance of " + iface);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\pool\ProxyResultSet.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */