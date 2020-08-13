/*     */ package org.json.simple;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.Writer;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.json.simple.parser.JSONParser;
/*     */ import org.json.simple.parser.ParseException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSONValue
/*     */ {
/*     */   public static Object parse(Reader in) {
/*     */     try {
/*  41 */       JSONParser parser = new JSONParser();
/*  42 */       return parser.parse(in);
/*     */     }
/*  44 */     catch (Exception e) {
/*  45 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Object parse(String s) {
/*  50 */     StringReader in = new StringReader(s);
/*  51 */     return parse(in);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object parseWithException(Reader in) throws IOException, ParseException {
/*  72 */     JSONParser parser = new JSONParser();
/*  73 */     return parser.parse(in);
/*     */   }
/*     */   
/*     */   public static Object parseWithException(String s) throws ParseException {
/*  77 */     JSONParser parser = new JSONParser();
/*  78 */     return parser.parse(s);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeJSONString(Object value, Writer out) throws IOException {
/*  96 */     if (value == null) {
/*  97 */       out.write("null");
/*     */       
/*     */       return;
/*     */     } 
/* 101 */     if (value instanceof String) {
/* 102 */       out.write(34);
/* 103 */       out.write(escape((String)value));
/* 104 */       out.write(34);
/*     */       
/*     */       return;
/*     */     } 
/* 108 */     if (value instanceof Double) {
/* 109 */       if (((Double)value).isInfinite() || ((Double)value).isNaN()) {
/* 110 */         out.write("null");
/*     */       } else {
/* 112 */         out.write(value.toString());
/*     */       } 
/*     */       return;
/*     */     } 
/* 116 */     if (value instanceof Float) {
/* 117 */       if (((Float)value).isInfinite() || ((Float)value).isNaN()) {
/* 118 */         out.write("null");
/*     */       } else {
/* 120 */         out.write(value.toString());
/*     */       } 
/*     */       return;
/*     */     } 
/* 124 */     if (value instanceof Number) {
/* 125 */       out.write(value.toString());
/*     */       
/*     */       return;
/*     */     } 
/* 129 */     if (value instanceof Boolean) {
/* 130 */       out.write(value.toString());
/*     */       
/*     */       return;
/*     */     } 
/* 134 */     if (value instanceof JSONStreamAware) {
/* 135 */       ((JSONStreamAware)value).writeJSONString(out);
/*     */       
/*     */       return;
/*     */     } 
/* 139 */     if (value instanceof JSONAware) {
/* 140 */       out.write(((JSONAware)value).toJSONString());
/*     */       
/*     */       return;
/*     */     } 
/* 144 */     if (value instanceof Map) {
/* 145 */       JSONObject.writeJSONString((Map)value, out);
/*     */       
/*     */       return;
/*     */     } 
/* 149 */     if (value instanceof List) {
/* 150 */       JSONArray.writeJSONString((List)value, out);
/*     */       
/*     */       return;
/*     */     } 
/* 154 */     out.write(value.toString());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toJSONString(Object value) {
/* 172 */     if (value == null) {
/* 173 */       return "null";
/*     */     }
/* 175 */     if (value instanceof String) {
/* 176 */       return "\"" + escape((String)value) + "\"";
/*     */     }
/* 178 */     if (value instanceof Double) {
/* 179 */       if (((Double)value).isInfinite() || ((Double)value).isNaN()) {
/* 180 */         return "null";
/*     */       }
/* 182 */       return value.toString();
/*     */     } 
/*     */     
/* 185 */     if (value instanceof Float) {
/* 186 */       if (((Float)value).isInfinite() || ((Float)value).isNaN()) {
/* 187 */         return "null";
/*     */       }
/* 189 */       return value.toString();
/*     */     } 
/*     */     
/* 192 */     if (value instanceof Number) {
/* 193 */       return value.toString();
/*     */     }
/* 195 */     if (value instanceof Boolean) {
/* 196 */       return value.toString();
/*     */     }
/* 198 */     if (value instanceof JSONAware) {
/* 199 */       return ((JSONAware)value).toJSONString();
/*     */     }
/* 201 */     if (value instanceof Map) {
/* 202 */       return JSONObject.toJSONString((Map)value);
/*     */     }
/* 204 */     if (value instanceof List) {
/* 205 */       return JSONArray.toJSONString((List)value);
/*     */     }
/* 207 */     return value.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escape(String s) {
/* 216 */     if (s == null)
/* 217 */       return null; 
/* 218 */     StringBuffer sb = new StringBuffer();
/* 219 */     escape(s, sb);
/* 220 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void escape(String s, StringBuffer sb) {
/* 228 */     for (int i = 0; i < s.length(); i++) {
/* 229 */       char ch = s.charAt(i);
/* 230 */       switch (ch) {
/*     */         case '"':
/* 232 */           sb.append("\\\"");
/*     */           break;
/*     */         case '\\':
/* 235 */           sb.append("\\\\");
/*     */           break;
/*     */         case '\b':
/* 238 */           sb.append("\\b");
/*     */           break;
/*     */         case '\f':
/* 241 */           sb.append("\\f");
/*     */           break;
/*     */         case '\n':
/* 244 */           sb.append("\\n");
/*     */           break;
/*     */         case '\r':
/* 247 */           sb.append("\\r");
/*     */           break;
/*     */         case '\t':
/* 250 */           sb.append("\\t");
/*     */           break;
/*     */         case '/':
/* 253 */           sb.append("\\/");
/*     */           break;
/*     */         
/*     */         default:
/* 257 */           if ((ch >= '\000' && ch <= '\037') || (ch >= '' && ch <= '') || (ch >= ' ' && ch <= '⃿')) {
/* 258 */             String ss = Integer.toHexString(ch);
/* 259 */             sb.append("\\u");
/* 260 */             for (int k = 0; k < 4 - ss.length(); k++) {
/* 261 */               sb.append('0');
/*     */             }
/* 263 */             sb.append(ss.toUpperCase());
/*     */             break;
/*     */           } 
/* 266 */           sb.append(ch);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\org\json\simple\JSONValue.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */