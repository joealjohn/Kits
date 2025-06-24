/*     */ package dev.continuum.kits.libs.utils.elements.impl;
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.consumers.PairConsumer;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ public class ElementsImpl<E> implements Elements<E>, Iterable<E> {
/*  13 */   private final Cachable<Integer, E> internal = (Cachable<Integer, E>)Cachable.of();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void elements(@NotNull Collection<E> elements) {
/*  20 */     for (E element : elements) {
/*  21 */       this.internal.cache(Integer.valueOf(this.internal.cached()), element);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public final void elements(@NotNull E... elements) {
/*  31 */     elements(Arrays.asList(elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void element(@Nullable E element) {
/*  39 */     this.internal.cache(Integer.valueOf(this.internal.cached()), element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void element(int at, @Nullable E element) {
/*  47 */     this.internal.cache(Integer.valueOf(at), element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public E element(int at) {
/*  55 */     return (E)this.internal.val(Integer.valueOf(at));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int at(@NotNull E element) {
/*  63 */     return ((Integer)this.internal.key(element)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void del(@NotNull E element) {
/*  71 */     this.internal.del(this.internal.key(element), element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void del(int at) {
/*  79 */     this.internal.del(Integer.valueOf(at));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void del(int at, @NotNull E element) {
/*  87 */     this.internal.del(Integer.valueOf(at), element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delAll(@NotNull Collection<E> elements) {
/*  95 */     for (E element : elements) {
/*  96 */       this.internal.del(Integer.valueOf(this.internal.cached()), element);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public final void delAll(@NotNull E... elements) {
/* 106 */     delAll(Arrays.asList(elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean has(@NotNull E element) {
/* 114 */     return this.internal.hasVal(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Iterator<E> iterator() {
/* 123 */     return this.internal.snapshot().asMap().values().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> action) {
/* 131 */     for (E element : this.internal.snapshot().asMap().values()) {
/* 132 */       action.accept(element);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEach(@NotNull PairConsumer<Integer, E> elementConsumer) {
/* 141 */     this.internal.forEach(elementConsumer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Collection<E> elements() {
/* 149 */     return this.internal.snapshot().asMap().values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public E[] elementsArray() {
/* 158 */     return (E[])elements().toArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 166 */     this.internal.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 174 */     return this.internal.cached();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delAllNull() {
/* 181 */     forEach((at, what) -> {
/*     */           if (what == null) {
/*     */             del(at.intValue());
/*     */           }
/*     */         });
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
/*     */   public ElementsImpl<E> sortKeys() {
/* 197 */     List<E> sorted = this.internal.snapshot().asMap().entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).map(Map.Entry::getValue).toList();
/*     */     
/* 199 */     return Elements.of(sorted);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\elements\impl\ElementsImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */