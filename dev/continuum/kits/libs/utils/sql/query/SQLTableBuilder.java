/*    */ package dev.continuum.kits.libs.utils.sql.query;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.sql.enums.PrimaryColumn;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SQLTableBuilder
/*    */ {
/*    */   private String tableName;
/* 13 */   private final List<Column> columns = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SQLTableBuilder of() {
/* 21 */     return new SQLTableBuilder();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SQLTableBuilder name(String tableName) {
/* 31 */     this.tableName = tableName;
/* 32 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SQLTableBuilder column(String name, String type, PrimaryColumn isPrimary) {
/* 44 */     Column column = new Column(name, type, isPrimary);
/* 45 */     this.columns.add(column);
/* 46 */     return this;
/*    */   }
/*    */   
/*    */   public SQLTableBuilder column(String name, String type) {
/* 50 */     return column(name, type, PrimaryColumn.FALSE);
/*    */   }
/*    */   
/*    */   public SQLTableBuilder column(String name, String type, boolean isPrimary) {
/* 54 */     return isPrimary ? column(name, type, PrimaryColumn.TRUE) : column(name, type, PrimaryColumn.FALSE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String build() {
/* 63 */     StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
/* 64 */     query.append(this.tableName).append(" (");
/*    */     
/* 66 */     List<String> primaryKeys = new ArrayList<>();
/*    */     
/* 68 */     for (Column column : this.columns) {
/* 69 */       query.append(column.name()).append(" ").append(column.type());
/*    */       
/* 71 */       if (column.isPrimary() == PrimaryColumn.TRUE) {
/* 72 */         primaryKeys.add(column.name());
/*    */       }
/*    */       
/* 75 */       query.append(", ");
/*    */     } 
/*    */     
/* 78 */     query.setLength(query.length() - 2);
/*    */     
/* 80 */     if (!primaryKeys.isEmpty()) {
/* 81 */       query.append(", PRIMARY KEY(");
/*    */       
/* 83 */       for (String pk : primaryKeys) {
/* 84 */         query.append(pk).append(", ");
/*    */       }
/*    */       
/* 87 */       query.setLength(query.length() - 2);
/* 88 */       query.append(")");
/*    */     } 
/*    */     
/* 91 */     query.append(")");
/*    */     
/* 93 */     return query.toString();
/*    */   } public static final class Column extends Record {
/*    */     private final String name; private final String type; private final PrimaryColumn isPrimary; public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Ldev/continuum/kits/libs/utils/sql/query/SQLTableBuilder$Column;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #99	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Ldev/continuum/kits/libs/utils/sql/query/SQLTableBuilder$Column;
/*    */     } public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Ldev/continuum/kits/libs/utils/sql/query/SQLTableBuilder$Column;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #99	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Ldev/continuum/kits/libs/utils/sql/query/SQLTableBuilder$Column;
/* 99 */     } public Column(String name, String type, PrimaryColumn isPrimary) { this.name = name; this.type = type; this.isPrimary = isPrimary; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Ldev/continuum/kits/libs/utils/sql/query/SQLTableBuilder$Column;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #99	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Ldev/continuum/kits/libs/utils/sql/query/SQLTableBuilder$Column;
/* 99 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public String type() { return this.type; } public PrimaryColumn isPrimary() { return this.isPrimary; }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\sql\query\SQLTableBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */