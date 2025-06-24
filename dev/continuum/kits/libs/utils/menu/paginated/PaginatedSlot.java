/*    */ package dev.continuum.kits.libs.utils.menu.paginated;
/*    */ public final class PaginatedSlot extends Record { private final int slot;
/*    */   private final int page;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Ldev/continuum/kits/libs/utils/menu/paginated/PaginatedSlot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Ldev/continuum/kits/libs/utils/menu/paginated/PaginatedSlot;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Ldev/continuum/kits/libs/utils/menu/paginated/PaginatedSlot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Ldev/continuum/kits/libs/utils/menu/paginated/PaginatedSlot;
/*    */   }
/*    */   
/* 13 */   public PaginatedSlot(int slot, int page) { this.slot = slot; this.page = page; } public int slot() { return this.slot; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Ldev/continuum/kits/libs/utils/menu/paginated/PaginatedSlot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Ldev/continuum/kits/libs/utils/menu/paginated/PaginatedSlot;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public int page() { return this.page; } @NotNull
/*    */   public static PaginatedSlot paginatedSlot(int slot, int page) {
/* 15 */     return new PaginatedSlot(slot, page);
/*    */   } }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\menu\paginated\PaginatedSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */