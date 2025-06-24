/*    */ package dev.continuum.kits.database;
/*    */ 
/*    */ 
/*    */ public final class DatabaseError extends Record {
/*    */   @NotNull
/*    */   private final String text;
/*    */   
/*  8 */   public DatabaseError(@NotNull String text) { this.text = text; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Ldev/continuum/kits/database/DatabaseError;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Ldev/continuum/kits/database/DatabaseError; } @NotNull public String text() { return this.text; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Ldev/continuum/kits/database/DatabaseError;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Ldev/continuum/kits/database/DatabaseError; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Ldev/continuum/kits/database/DatabaseError;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Ldev/continuum/kits/database/DatabaseError;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public void execute() { ContinuumKits plugin = (ContinuumKits)ContinuumKits.getPlugin(ContinuumKits.class);
/* 11 */     ComponentLogger logger = plugin.getComponentLogger();
/*    */     
/* 13 */     logger.error(TextStyle.style(text())); }
/*    */ 
/*    */   
/*    */   public static void execute(@NotNull String text) {
/* 17 */     (new DatabaseError(text)).execute();
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\DatabaseError.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */