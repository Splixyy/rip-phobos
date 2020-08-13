/*    */ package me.earth.phobos.features.setting;
/*    */ 
/*    */ import com.google.common.base.Converter;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ 
/*    */ public class EnumConverter
/*    */   extends Converter<Enum, JsonElement> {
/*    */   private final Class<? extends Enum> clazz;
/*    */   
/*    */   public EnumConverter(Class<? extends Enum> clazz) {
/* 12 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public static int currentEnum(Enum clazz) {
/* 16 */     for (int i = 0; i < ((Enum[])clazz.getClass().getEnumConstants()).length; i++) {
/* 17 */       Enum e = ((Enum[])clazz.getClass().getEnumConstants())[i];
/* 18 */       if (e.name().equalsIgnoreCase(clazz.name())) {
/* 19 */         return i;
/*    */       }
/*    */     } 
/* 22 */     return -1;
/*    */   }
/*    */   
/*    */   public static Enum increaseEnum(Enum clazz) {
/* 26 */     int index = currentEnum(clazz);
/* 27 */     for (int i = 0; i < ((Enum[])clazz.getClass().getEnumConstants()).length; i++) {
/* 28 */       Enum e = ((Enum[])clazz.getClass().getEnumConstants())[i];
/* 29 */       if (i == index + 1) {
/* 30 */         return e;
/*    */       }
/*    */     } 
/* 33 */     return ((Enum[])clazz.getClass().getEnumConstants())[0];
/*    */   }
/*    */   
/*    */   public static String getProperName(Enum clazz) {
/* 37 */     return Character.toUpperCase(clazz.name().charAt(0)) + clazz.name().toLowerCase().substring(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement doForward(Enum anEnum) {
/* 42 */     return (JsonElement)new JsonPrimitive(anEnum.toString());
/*    */   }
/*    */ 
/*    */   
/*    */   public Enum doBackward(JsonElement jsonElement) {
/*    */     try {
/* 48 */       return Enum.valueOf((Class)this.clazz, jsonElement.getAsString());
/* 49 */     } catch (IllegalArgumentException e) {
/* 50 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\setting\EnumConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */