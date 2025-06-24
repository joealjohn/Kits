/*     */ package dev.continuum.kits.libs.hikari.pool;
/*     */ 
/*     */ import dev.continuum.kits.libs.hikari.SQLExceptionOverride;
/*     */ import dev.continuum.kits.libs.hikari.util.FastList;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Savepoint;
/*     */ import java.sql.Statement;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public abstract class ProxyConnection
/*     */   implements Connection
/*     */ {
/*     */   static final int DIRTY_BIT_READONLY = 1;
/*     */   static final int DIRTY_BIT_AUTOCOMMIT = 2;
/*     */   static final int DIRTY_BIT_ISOLATION = 4;
/*     */   static final int DIRTY_BIT_CATALOG = 8;
/*     */   static final int DIRTY_BIT_NETTIMEOUT = 16;
/*     */   static final int DIRTY_BIT_SCHEMA = 32;
/*  69 */   private static final Logger LOGGER = LoggerFactory.getLogger(ProxyConnection.class);
/*     */   
/*  71 */   private static final Set<String> ERROR_STATES = new HashSet<>(); static {
/*  72 */     ERROR_STATES.add("0A000");
/*  73 */     ERROR_STATES.add("57P01");
/*  74 */     ERROR_STATES.add("57P02");
/*  75 */     ERROR_STATES.add("57P03");
/*  76 */     ERROR_STATES.add("01002");
/*  77 */     ERROR_STATES.add("JZ0C0");
/*  78 */     ERROR_STATES.add("JZ0C1");
/*     */   }
/*  80 */   protected Connection delegate; private final PoolEntry poolEntry; private final ProxyLeakTask leakTask; private static final Set<Integer> ERROR_CODES = new HashSet<>(); private final FastList<Statement> openStatements; private int dirtyBits; private boolean isCommitStateDirty; static {
/*  81 */     ERROR_CODES.add(Integer.valueOf(500150));
/*  82 */     ERROR_CODES.add(Integer.valueOf(2399));
/*  83 */     ERROR_CODES.add(Integer.valueOf(1105));
/*     */   }
/*     */   private boolean isReadOnly; private boolean isAutoCommit;
/*     */   private int networkTimeout;
/*     */   private int transactionIsolation;
/*     */   private String dbcatalog;
/*     */   private String dbschema;
/*     */   
/*     */   protected ProxyConnection(PoolEntry poolEntry, Connection connection, FastList<Statement> openStatements, ProxyLeakTask leakTask, boolean isReadOnly, boolean isAutoCommit) {
/*  92 */     this.poolEntry = poolEntry;
/*  93 */     this.delegate = connection;
/*  94 */     this.openStatements = openStatements;
/*  95 */     this.leakTask = leakTask;
/*  96 */     this.isReadOnly = isReadOnly;
/*  97 */     this.isAutoCommit = isAutoCommit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 104 */     return getClass().getSimpleName() + "@" + getClass().getSimpleName() + " wrapping " + System.identityHashCode(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean getAutoCommitState() {
/* 113 */     return this.isAutoCommit;
/*     */   }
/*     */ 
/*     */   
/*     */   final String getCatalogState() {
/* 118 */     return this.dbcatalog;
/*     */   }
/*     */ 
/*     */   
/*     */   final String getSchemaState() {
/* 123 */     return this.dbschema;
/*     */   }
/*     */ 
/*     */   
/*     */   final int getTransactionIsolationState() {
/* 128 */     return this.transactionIsolation;
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean getReadOnlyState() {
/* 133 */     return this.isReadOnly;
/*     */   }
/*     */ 
/*     */   
/*     */   final int getNetworkTimeoutState() {
/* 138 */     return this.networkTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final PoolEntry getPoolEntry() {
/* 147 */     return this.poolEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final SQLException checkException(SQLException sqle) {
/* 153 */     boolean evict = false;
/* 154 */     SQLException nse = sqle;
/* 155 */     SQLExceptionOverride exceptionOverride = (this.poolEntry.getPoolBase()).exceptionOverride;
/* 156 */     for (int depth = 0; this.delegate != ClosedConnection.CLOSED_CONNECTION && nse != null && depth < 10; depth++) {
/* 157 */       String sqlState = nse.getSQLState();
/* 158 */       if ((sqlState != null && sqlState.startsWith("08")) || nse instanceof java.sql.SQLTimeoutException || ERROR_STATES
/*     */         
/* 160 */         .contains(sqlState) || ERROR_CODES
/* 161 */         .contains(Integer.valueOf(nse.getErrorCode()))) {
/*     */         
/* 163 */         if (exceptionOverride != null && exceptionOverride.adjudicate(nse) == SQLExceptionOverride.Override.DO_NOT_EVICT) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 168 */         evict = true;
/*     */         
/*     */         break;
/*     */       } 
/* 172 */       nse = nse.getNextException();
/*     */     } 
/*     */ 
/*     */     
/* 176 */     if (evict) {
/* 177 */       SQLException exception = (nse != null) ? nse : sqle;
/* 178 */       LOGGER.warn("{} - Connection {} marked as broken because of SQLSTATE({}), ErrorCode({})", new Object[] { this.poolEntry
/* 179 */             .getPoolName(), this.delegate, exception.getSQLState(), Integer.valueOf(exception.getErrorCode()), exception });
/* 180 */       this.leakTask.cancel();
/* 181 */       this.poolEntry.evict("(connection is broken)");
/* 182 */       this.delegate = ClosedConnection.CLOSED_CONNECTION;
/*     */     } 
/*     */     
/* 185 */     return sqle;
/*     */   }
/*     */ 
/*     */   
/*     */   final synchronized void untrackStatement(Statement statement) {
/* 190 */     this.openStatements.remove(statement);
/*     */   }
/*     */ 
/*     */   
/*     */   final void markCommitStateDirty() {
/* 195 */     if (!this.isAutoCommit) {
/* 196 */       this.isCommitStateDirty = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void cancelLeakTask() {
/* 202 */     this.leakTask.cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized <T extends Statement> T trackStatement(T statement) {
/* 207 */     this.openStatements.add(statement);
/*     */     
/* 209 */     return statement;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void closeStatements() {
/* 215 */     int size = this.openStatements.size();
/* 216 */     if (size > 0) {
/* 217 */       for (int i = 0; i < size && this.delegate != ClosedConnection.CLOSED_CONNECTION; i++) { try {
/* 218 */           Statement ignored = (Statement)this.openStatements.get(i);
/*     */           
/* 220 */           if (ignored != null) ignored.close(); 
/* 221 */         } catch (SQLException e) {
/* 222 */           LOGGER.warn("{} - Connection {} marked as broken because of an exception closing open statements during Connection.close()", this.poolEntry
/* 223 */               .getPoolName(), this.delegate);
/* 224 */           this.leakTask.cancel();
/* 225 */           this.poolEntry.evict("(exception closing Statements during Connection.close())");
/* 226 */           this.delegate = ClosedConnection.CLOSED_CONNECTION;
/*     */         }  }
/*     */ 
/*     */       
/* 230 */       this.openStatements.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void close() throws SQLException {
/* 243 */     closeStatements();
/*     */     
/* 245 */     if (this.delegate != ClosedConnection.CLOSED_CONNECTION) {
/* 246 */       this.leakTask.cancel();
/*     */       
/*     */       try {
/* 249 */         if (this.isCommitStateDirty && !this.isAutoCommit) {
/* 250 */           this.delegate.rollback();
/* 251 */           LOGGER.debug("{} - Executed rollback on connection {} due to dirty commit state on close().", this.poolEntry.getPoolName(), this.delegate);
/*     */         } 
/*     */         
/* 254 */         if (this.dirtyBits != 0) {
/* 255 */           this.poolEntry.resetConnectionState(this, this.dirtyBits);
/*     */         }
/*     */         
/* 258 */         this.delegate.clearWarnings();
/*     */       }
/* 260 */       catch (SQLException e) {
/*     */         
/* 262 */         if (!this.poolEntry.isMarkedEvicted()) {
/* 263 */           throw checkException(e);
/*     */         }
/*     */       } finally {
/*     */         
/* 267 */         this.delegate = ClosedConnection.CLOSED_CONNECTION;
/* 268 */         this.poolEntry.recycle();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() throws SQLException {
/* 278 */     return (this.delegate == ClosedConnection.CLOSED_CONNECTION);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement createStatement() throws SQLException {
/* 285 */     return ProxyFactory.getProxyStatement(this, trackStatement(this.delegate.createStatement()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement createStatement(int resultSetType, int concurrency) throws SQLException {
/* 292 */     return ProxyFactory.getProxyStatement(this, trackStatement(this.delegate.createStatement(resultSetType, concurrency)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement createStatement(int resultSetType, int concurrency, int holdability) throws SQLException {
/* 299 */     return ProxyFactory.getProxyStatement(this, trackStatement(this.delegate.createStatement(resultSetType, concurrency, holdability)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql) throws SQLException {
/* 307 */     return ProxyFactory.getProxyCallableStatement(this, trackStatement(this.delegate.prepareCall(sql)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql, int resultSetType, int concurrency) throws SQLException {
/* 314 */     return ProxyFactory.getProxyCallableStatement(this, trackStatement(this.delegate.prepareCall(sql, resultSetType, concurrency)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql, int resultSetType, int concurrency, int holdability) throws SQLException {
/* 321 */     return ProxyFactory.getProxyCallableStatement(this, trackStatement(this.delegate.prepareCall(sql, resultSetType, concurrency, holdability)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql) throws SQLException {
/* 328 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(sql)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
/* 335 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(sql, autoGeneratedKeys)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int resultSetType, int concurrency) throws SQLException {
/* 342 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(sql, resultSetType, concurrency)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int resultSetType, int concurrency, int holdability) throws SQLException {
/* 349 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(sql, resultSetType, concurrency, holdability)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
/* 356 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(sql, columnIndexes)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
/* 363 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(sql, columnNames)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatabaseMetaData getMetaData() throws SQLException {
/* 370 */     markCommitStateDirty();
/* 371 */     return ProxyFactory.getProxyDatabaseMetaData(this, this.delegate.getMetaData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commit() throws SQLException {
/* 378 */     this.delegate.commit();
/* 379 */     this.isCommitStateDirty = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollback() throws SQLException {
/* 386 */     this.delegate.rollback();
/* 387 */     this.isCommitStateDirty = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollback(Savepoint savepoint) throws SQLException {
/* 394 */     this.delegate.rollback(savepoint);
/* 395 */     this.isCommitStateDirty = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoCommit(boolean autoCommit) throws SQLException {
/* 402 */     this.delegate.setAutoCommit(autoCommit);
/* 403 */     this.isAutoCommit = autoCommit;
/* 404 */     this.dirtyBits |= 0x2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadOnly(boolean readOnly) throws SQLException {
/* 411 */     this.delegate.setReadOnly(readOnly);
/* 412 */     this.isReadOnly = readOnly;
/* 413 */     this.isCommitStateDirty = false;
/* 414 */     this.dirtyBits |= 0x1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransactionIsolation(int level) throws SQLException {
/* 421 */     this.delegate.setTransactionIsolation(level);
/* 422 */     this.transactionIsolation = level;
/* 423 */     this.dirtyBits |= 0x4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatalog(String catalog) throws SQLException {
/* 430 */     this.delegate.setCatalog(catalog);
/* 431 */     this.dbcatalog = catalog;
/* 432 */     this.dirtyBits |= 0x8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
/* 439 */     this.delegate.setNetworkTimeout(executor, milliseconds);
/* 440 */     this.networkTimeout = milliseconds;
/* 441 */     this.dirtyBits |= 0x10;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSchema(String schema) throws SQLException {
/* 448 */     this.delegate.setSchema(schema);
/* 449 */     this.dbschema = schema;
/* 450 */     this.dirtyBits |= 0x20;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 457 */     return (iface.isInstance(this.delegate) || (this.delegate != null && this.delegate.isWrapperFor(iface)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T unwrap(Class<T> iface) throws SQLException {
/* 465 */     if (iface.isInstance(this.delegate)) {
/* 466 */       return (T)this.delegate;
/*     */     }
/* 468 */     if (this.delegate != null) {
/* 469 */       return this.delegate.unwrap(iface);
/*     */     }
/*     */     
/* 472 */     throw new SQLException("Wrapped connection is not an instance of " + iface);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ClosedConnection
/*     */   {
/* 481 */     static final Connection CLOSED_CONNECTION = getClosedConnection();
/*     */ 
/*     */     
/*     */     private static Connection getClosedConnection() {
/* 485 */       InvocationHandler handler = (proxy, method, args) -> {
/*     */           String methodName = method.getName();
/*     */           
/*     */           if ("isClosed".equals(methodName)) {
/*     */             return Boolean.TRUE;
/*     */           }
/*     */           
/*     */           if ("isValid".equals(methodName)) {
/*     */             return Boolean.FALSE;
/*     */           }
/*     */           if ("abort".equals(methodName)) {
/*     */             return void.class;
/*     */           }
/*     */           if ("close".equals(methodName)) {
/*     */             return void.class;
/*     */           }
/*     */           if ("toString".equals(methodName)) {
/*     */             return ClosedConnection.class.getCanonicalName();
/*     */           }
/*     */           throw new SQLException("Connection is closed");
/*     */         };
/* 506 */       return (Connection)Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, handler);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\pool\ProxyConnection.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */