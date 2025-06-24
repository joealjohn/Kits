/*     */ package dev.continuum.kits.libs.utils.misc;
/*     */ 
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
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
/*     */ public class ObjectUtils
/*     */ {
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T nonNull(@Nullable T value) {
/*  27 */     if (value == null) throw new NullPointerException(); 
/*  28 */     return value;
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
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T nonNull(@Nullable T value, @NotNull String text) {
/*  42 */     if (value == null) throw new NullPointerException(text); 
/*  43 */     return value;
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
/*     */   public static <T> T defaultIfNull(@Nullable T value, T defaultValue) {
/*  55 */     return (value != null) ? value : defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> void ifNonNull(@Nullable T value, Consumer<T> action) {
/*  66 */     if (value != null) {
/*  67 */       action.accept(value);
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
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static <T, E extends RuntimeException> T nonNull(@Nullable T value, Supplier<E> exceptionSupplier) {
/*  83 */     if (value == null) throw (RuntimeException)exceptionSupplier.get(); 
/*  84 */     return value;
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
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> Collection<T> nonNullElements(@Nullable Collection<T> collection) {
/*  97 */     if (collection == null || collection.contains(null)) throw new NullPointerException("Collection contains null elements"); 
/*  98 */     return collection;
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
/*     */   @CanIgnoreReturnValue
/*     */   public static String nonEmpty(@Nullable String value, @NotNull String fieldName) {
/* 111 */     if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException(fieldName + " must not be empty"); 
/* 112 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Collection<T> emptyIfNull(@Nullable Collection<T> collection) {
/* 123 */     return (collection != null) ? collection : Collections.<T>emptyList();
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
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T check(@Nullable T value, Predicate<T> predicate, String errorMessage) {
/* 138 */     if (!predicate.test(value)) throw new IllegalArgumentException(errorMessage); 
/* 139 */     return value;
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
/*     */   public static int requireNonNegative(int value, String errorMessage) {
/* 151 */     if (value < 0) {
/* 152 */       throw new IllegalArgumentException(errorMessage);
/*     */     }
/* 154 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\misc\ObjectUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */