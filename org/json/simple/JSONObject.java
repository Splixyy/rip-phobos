/*     */ package org.json.simple;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ public class JSONObject
/*     */   extends HashMap
/*     */   implements Map, JSONAware, JSONStreamAware
/*     */ {
/*     */   private static final long serialVersionUID = -503443796854799292L;
/*     */   
/*     */   public JSONObject() {}
/*     */   
/*     */   public JSONObject(Map map) {
/*  34 */     super(map);
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
/*     */   public static void writeJSONString(Map map, Writer out) throws IOException {
/*  48 */     if (map == null) {
/*  49 */       out.write("null");
/*     */       
/*     */       return;
/*     */     } 
/*  53 */     boolean first = true;
/*  54 */     Iterator iter = map.entrySet().iterator();
/*     */     
/*  56 */     out.write(123);
/*  57 */     while (iter.hasNext()) {
/*  58 */       if (first) {
/*  59 */         first = false;
/*     */       } else {
/*  61 */         out.write(44);
/*  62 */       }  Map.Entry entry = iter.next();
/*  63 */       out.write(34);
/*  64 */       out.write(escape(String.valueOf(entry.getKey())));
/*  65 */       out.write(34);
/*  66 */       out.write(58);
/*  67 */       JSONValue.writeJSONString(entry.getValue(), out);
/*     */     } 
/*  69 */     out.write(125);
/*     */   }
/*     */   
/*     */   public void writeJSONString(Writer out) throws IOException {
/*  73 */     writeJSONString(this, out);
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
/*     */   public static String toJSONString(Map map) {
/*  86 */     if (map == null) {
/*  87 */       return "null";
/*     */     }
/*  89 */     StringBuffer sb = new StringBuffer();
/*  90 */     boolean first = true;
/*  91 */     Iterator iter = map.entrySet().iterator();
/*     */     
/*  93 */     sb.append('{');
/*  94 */     while (iter.hasNext()) {
/*  95 */       if (first) {
/*  96 */         first = false;
/*     */       } else {
/*  98 */         sb.append(',');
/*     */       } 
/* 100 */       Map.Entry entry = iter.next();
/* 101 */       toJSONString(String.valueOf(entry.getKey()), entry.getValue(), sb);
/*     */     } 
/* 103 */     sb.append('}');
/* 104 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String toJSONString() {
/* 108 */     return toJSONString(this);
/*     */   }
/*     */   
/*     */   private static String toJSONString(String key, Object value, StringBuffer sb) {
/* 112 */     sb.append('"');
/* 113 */     if (key == null) {
/* 114 */       sb.append("null");
/*     */     } else {
/* 116 */       JSONValue.escape(key, sb);
/* 117 */     }  sb.append('"').append(':');
/*     */     
/* 119 */     sb.append(JSONValue.toJSONString(value));
/*     */     
/* 121 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 125 */     return toJSONString();
/*     */   }
/*     */   
/*     */   public static String toString(String key, Object value) {
/* 129 */     StringBuffer sb = new StringBuffer();
/* 130 */     toJSONString(key, value, sb);
/* 131 */     return sb.toString();
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
/*     */   public static String escape(String s) {
/* 144 */     return JSONValue.escape(s);
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\org\json\simple\JSONObject.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */