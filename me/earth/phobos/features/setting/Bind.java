/*    */ package me.earth.phobos.features.setting;
/*    */ 
/*    */ import com.google.common.base.Converter;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ 
/*    */ public class Bind
/*    */ {
/*    */   private int key;
/*    */   
/*    */   public Bind(int key) {
/* 13 */     this.key = key;
/*    */   }
/*    */   
/*    */   public int getKey() {
/* 17 */     return this.key;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 21 */     return (this.key < 0);
/*    */   }
/*    */   
/*    */   public void setKey(int key) {
/* 25 */     this.key = key;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 30 */     return isEmpty() ? "None" : ((this.key < 0) ? "None" : capitalise(Keyboard.getKeyName(this.key)));
/*    */   }
/*    */   
/*    */   public boolean isDown() {
/* 34 */     return (!isEmpty() && Keyboard.isKeyDown(getKey()));
/*    */   }
/*    */   
/*    */   private String capitalise(String str) {
/* 38 */     if (str.isEmpty()) {
/* 39 */       return "";
/*    */     }
/* 41 */     return Character.toUpperCase(str.charAt(0)) + ((str.length() != 1) ? str.substring(1).toLowerCase() : "");
/*    */   }
/*    */   
/*    */   public static Bind none() {
/* 45 */     return new Bind(-1);
/*    */   }
/*    */   
/*    */   public static class BindConverter
/*    */     extends Converter<Bind, JsonElement> {
/*    */     public JsonElement doForward(Bind bind) {
/* 51 */       return (JsonElement)new JsonPrimitive(bind.toString());
/*    */     }
/*    */ 
/*    */     
/*    */     public Bind doBackward(JsonElement jsonElement) {
/* 56 */       String s = jsonElement.getAsString();
/* 57 */       if (s.equalsIgnoreCase("None")) {
/* 58 */         return Bind.none();
/*    */       }
/*    */       
/* 61 */       int key = -1;
/*    */       try {
/* 63 */         key = Keyboard.getKeyIndex(s.toUpperCase());
/* 64 */       } catch (Exception exception) {}
/*    */ 
/*    */       
/* 67 */       if (key == 0) {
/* 68 */         return Bind.none();
/*    */       }
/* 70 */       return new Bind(key);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\setting\Bind.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */