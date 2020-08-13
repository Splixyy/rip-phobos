/*    */ package me.earth.phobos.util;
/*    */ 
/*    */ import java.lang.reflect.AccessibleObject;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Member;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReflectionUtil
/*    */ {
/*    */   public static <F, T extends F> void copyOf(F from, T to, boolean ignoreFinal) throws NoSuchFieldException, IllegalAccessException {
/* 13 */     Objects.requireNonNull(from);
/* 14 */     Objects.requireNonNull(to);
/*    */     
/* 16 */     Class<?> clazz = from.getClass();
/*    */     
/* 18 */     for (Field field : clazz.getDeclaredFields()) {
/* 19 */       makePublic(field);
/*    */       
/* 21 */       if (!isStatic(field) && (!ignoreFinal || !isFinal(field))) {
/*    */ 
/*    */         
/* 24 */         makeMutable(field);
/*    */ 
/*    */         
/* 27 */         field.set(to, field.get(from));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static <F, T extends F> void copyOf(F from, T to) throws NoSuchFieldException, IllegalAccessException {
/* 33 */     copyOf(from, to, false);
/*    */   }
/*    */   
/*    */   public static boolean isStatic(Member instance) {
/* 37 */     return ((instance.getModifiers() & 0x8) != 0);
/*    */   }
/*    */   
/*    */   public static boolean isFinal(Member instance) {
/* 41 */     return ((instance.getModifiers() & 0x10) != 0);
/*    */   }
/*    */   
/*    */   public static void makeAccessible(AccessibleObject instance, boolean accessible) {
/* 45 */     Objects.requireNonNull(instance);
/* 46 */     instance.setAccessible(accessible);
/*    */   }
/*    */   
/*    */   public static void makePublic(AccessibleObject instance) {
/* 50 */     makeAccessible(instance, true);
/*    */   }
/*    */   
/*    */   public static void makePrivate(AccessibleObject instance) {
/* 54 */     makeAccessible(instance, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void makeMutable(Member instance) throws NoSuchFieldException, IllegalAccessException {
/* 59 */     Objects.requireNonNull(instance);
/* 60 */     Field modifiers = Field.class.getDeclaredField("modifiers");
/* 61 */     makePublic(modifiers);
/* 62 */     modifiers.setInt(instance, instance.getModifiers() & 0xFFFFFFEF);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void makeImmutable(Member instance) throws NoSuchFieldException, IllegalAccessException {
/* 67 */     Objects.requireNonNull(instance);
/* 68 */     Field modifiers = Field.class.getDeclaredField("modifiers");
/* 69 */     makePublic(modifiers);
/* 70 */     modifiers.setInt(instance, instance.getModifiers() & 0x10);
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobo\\util\ReflectionUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */