/*    */ package dev.continuum.kits.libs.utils.elements;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.consumers.PairConsumer;
/*    */ import dev.continuum.kits.libs.utils.elements.impl.ElementsImpl;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.function.Consumer;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
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
/*    */ public interface Elements<E>
/*    */   extends Iterable<E>
/*    */ {
/*    */   @NotNull
/*    */   static <E> ElementsImpl<E> of() {
/* 26 */     return new ElementsImpl();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   static <E> ElementsImpl<E> of(@NotNull Class<E> type) {
/* 38 */     return new ElementsImpl();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   static <E> ElementsImpl<E> of(@Nullable Collection<E> elements) {
/* 49 */     ElementsImpl<E> elementsImpl = new ElementsImpl();
/* 50 */     elementsImpl.elements(elements);
/*    */     
/* 52 */     return elementsImpl;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @SafeVarargs
/*    */   @NotNull
/*    */   static <E> ElementsImpl<E> of(@NotNull E... elements) {
/* 64 */     ElementsImpl<E> elementsImpl = new ElementsImpl();
/* 65 */     elementsImpl.elements((Object[])elements);
/*    */     
/* 67 */     return elementsImpl;
/*    */   }
/*    */   
/*    */   void elements(@NotNull Collection<E> paramCollection);
/*    */   
/*    */   void elements(@NotNull E... paramVarArgs);
/*    */   
/*    */   void element(@Nullable E paramE);
/*    */   
/*    */   void element(int paramInt, @Nullable E paramE);
/*    */   
/*    */   @Nullable
/*    */   E element(int paramInt);
/*    */   
/*    */   int at(@NotNull E paramE);
/*    */   
/*    */   void del(@NotNull E paramE);
/*    */   
/*    */   void del(int paramInt);
/*    */   
/*    */   void del(int paramInt, @NotNull E paramE);
/*    */   
/*    */   void delAll(@NotNull Collection<E> paramCollection);
/*    */   
/*    */   void delAll(@NotNull E... paramVarArgs);
/*    */   
/*    */   boolean has(@NotNull E paramE);
/*    */   
/*    */   void forEach(Consumer<? super E> paramConsumer);
/*    */   
/*    */   void forEach(@NotNull PairConsumer<Integer, E> paramPairConsumer);
/*    */   
/*    */   @NotNull
/*    */   Iterator<E> iterator();
/*    */   
/*    */   @NotNull
/*    */   Collection<E> elements();
/*    */   
/*    */   @NotNull
/*    */   E[] elementsArray();
/*    */   
/*    */   void clear();
/*    */   
/*    */   int size();
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\elements\Elements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */