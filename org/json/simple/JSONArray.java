/*     */ package org.json.simple;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class JSONArray
/*     */   extends ArrayList
/*     */   implements List, JSONAware, JSONStreamAware
/*     */ {
/*     */   private static final long serialVersionUID = 3957988303675231981L;
/*     */   
/*     */   public static void writeJSONString(List list, Writer out) throws IOException {
/*  32 */     if (list == null) {
/*  33 */       out.write("null");
/*     */       
/*     */       return;
/*     */     } 
/*  37 */     boolean first = true;
/*  38 */     Iterator iter = list.iterator();
/*     */     
/*  40 */     out.write(91);
/*  41 */     while (iter.hasNext()) {
/*  42 */       if (first) {
/*  43 */         first = false;
/*     */       } else {
/*  45 */         out.write(44);
/*     */       } 
/*  47 */       Object value = iter.next();
/*  48 */       if (value == null) {
/*  49 */         out.write("null");
/*     */         
/*     */         continue;
/*     */       } 
/*  53 */       JSONValue.writeJSONString(value, out);
/*     */     } 
/*  55 */     out.write(93);
/*     */   }
/*     */   
/*     */   public void writeJSONString(Writer out) throws IOException {
/*  59 */     writeJSONString(this, out);
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
/*     */   public static String toJSONString(List list) {
/*  72 */     if (list == null) {
/*  73 */       return "null";
/*     */     }
/*  75 */     boolean first = true;
/*  76 */     StringBuffer sb = new StringBuffer();
/*  77 */     Iterator iter = list.iterator();
/*     */     
/*  79 */     sb.append('[');
/*  80 */     while (iter.hasNext()) {
/*  81 */       if (first) {
/*  82 */         first = false;
/*     */       } else {
/*  84 */         sb.append(',');
/*     */       } 
/*  86 */       Object value = iter.next();
/*  87 */       if (value == null) {
/*  88 */         sb.append("null");
/*     */         continue;
/*     */       } 
/*  91 */       sb.append(JSONValue.toJSONString(value));
/*     */     } 
/*  93 */     sb.append(']');
/*  94 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String toJSONString() {
/*  98 */     return toJSONString(this);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 102 */     return toJSONString();
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\org\json\simple\JSONArray.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */