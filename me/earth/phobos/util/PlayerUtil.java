/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Scanner;
/*     */ import java.util.UUID;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import net.minecraft.advancements.AdvancementManager;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.json.simple.JSONArray;
/*     */ import org.json.simple.JSONObject;
/*     */ import org.json.simple.JSONValue;
/*     */ 
/*     */ 
/*     */ public class PlayerUtil
/*     */   implements Util
/*     */ {
/*  39 */   public static Timer timer = new Timer();
/*  40 */   private static JsonParser PARSER = new JsonParser();
/*     */   
/*     */   public static String getNameFromUUID(UUID uuid) {
/*     */     try {
/*  44 */       lookUpName process = new lookUpName(uuid);
/*  45 */       Thread thread = new Thread(process);
/*  46 */       thread.start();
/*  47 */       thread.join();
/*  48 */       return process.getName();
/*  49 */     } catch (Exception e) {
/*  50 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getNameFromUUID(String uuid) {
/*     */     try {
/*  56 */       lookUpName process = new lookUpName(uuid);
/*  57 */       Thread thread = new Thread(process);
/*  58 */       thread.start();
/*  59 */       thread.join();
/*  60 */       return process.getName();
/*  61 */     } catch (Exception e) {
/*  62 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static UUID getUUIDFromName(String name) {
/*     */     try {
/*  68 */       lookUpUUID process = new lookUpUUID(name);
/*  69 */       Thread thread = new Thread(process);
/*  70 */       thread.start();
/*  71 */       thread.join();
/*  72 */       return process.getUUID();
/*  73 */     } catch (Exception e) {
/*  74 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class lookUpUUID
/*     */     implements Runnable {
/*     */     private volatile UUID uuid;
/*     */     private final String name;
/*     */     
/*     */     public lookUpUUID(String name) {
/*  84 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       NetworkPlayerInfo profile;
/*     */       try {
/*  91 */         ArrayList<NetworkPlayerInfo> infoMap = new ArrayList<>(((NetHandlerPlayClient)Objects.<NetHandlerPlayClient>requireNonNull(Util.mc.func_147114_u())).func_175106_d());
/*  92 */         profile = infoMap.stream().filter(networkPlayerInfo -> networkPlayerInfo.func_178845_a().getName().equalsIgnoreCase(this.name)).findFirst().orElse(null);
/*  93 */         assert profile != null;
/*  94 */         this.uuid = profile.func_178845_a().getId();
/*  95 */       } catch (Exception e) {
/*  96 */         profile = null;
/*     */       } 
/*     */       
/*  99 */       if (profile == null) {
/* 100 */         Command.sendMessage("Player isn't online. Looking up UUID..");
/* 101 */         String s = PlayerUtil.requestIDs("[\"" + this.name + "\"]");
/* 102 */         if (s == null || s.isEmpty()) {
/* 103 */           Command.sendMessage("Couldn't find player ID. Are you connected to the internet? (0)");
/*     */         } else {
/* 105 */           JsonElement element = (new JsonParser()).parse(s);
/* 106 */           if (element.getAsJsonArray().size() == 0) {
/* 107 */             Command.sendMessage("Couldn't find player ID. (1)");
/*     */           } else {
/*     */             try {
/* 110 */               String id = element.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
/* 111 */               this.uuid = UUIDTypeAdapter.fromString(id);
/* 112 */             } catch (Exception e) {
/* 113 */               e.printStackTrace();
/* 114 */               Command.sendMessage("Couldn't find player ID. (2)");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public UUID getUUID() {
/* 122 */       return this.uuid;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 126 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class lookUpName
/*     */     implements Runnable {
/*     */     private volatile String name;
/*     */     private final String uuid;
/*     */     private final UUID uuidID;
/*     */     
/*     */     public lookUpName(String input) {
/* 137 */       this.uuid = input;
/* 138 */       this.uuidID = UUID.fromString(input);
/*     */     }
/*     */     
/*     */     public lookUpName(UUID input) {
/* 142 */       this.uuidID = input;
/* 143 */       this.uuid = input.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 148 */       this.name = lookUpName();
/*     */     }
/*     */     
/*     */     public String lookUpName() {
/* 152 */       EntityPlayer player = null;
/* 153 */       if (Util.mc.field_71441_e != null) {
/* 154 */         player = Util.mc.field_71441_e.func_152378_a(this.uuidID);
/*     */       }
/*     */       
/* 157 */       if (player == null) {
/* 158 */         String url = "https://api.mojang.com/user/profiles/" + this.uuid.replace("-", "") + "/names";
/*     */         
/*     */         try {
/* 161 */           String nameJson = IOUtils.toString(new URL(url));
/* 162 */           JSONArray nameValue = (JSONArray)JSONValue.parseWithException(nameJson);
/* 163 */           String playerSlot = nameValue.get(nameValue.size() - 1).toString();
/* 164 */           JSONObject nameObject = (JSONObject)JSONValue.parseWithException(playerSlot);
/* 165 */           return nameObject.get("name").toString();
/* 166 */         } catch (IOException|org.json.simple.parser.ParseException e) {
/* 167 */           e.printStackTrace();
/*     */           
/* 169 */           return null;
/*     */         } 
/* 171 */       }  return player.func_70005_c_();
/*     */     }
/*     */     
/*     */     public String getName() {
/* 175 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static String requestIDs(String data) {
/*     */     try {
/* 181 */       String query = "https://api.mojang.com/profiles/minecraft";
/* 182 */       URL url = new URL(query);
/* 183 */       HttpURLConnection conn = (HttpURLConnection)url.openConnection();
/* 184 */       conn.setConnectTimeout(5000);
/* 185 */       conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
/* 186 */       conn.setDoOutput(true);
/* 187 */       conn.setDoInput(true);
/* 188 */       conn.setRequestMethod("POST");
/* 189 */       OutputStream os = conn.getOutputStream();
/* 190 */       os.write(data.getBytes(StandardCharsets.UTF_8));
/* 191 */       os.close();
/* 192 */       InputStream in = new BufferedInputStream(conn.getInputStream());
/* 193 */       String res = convertStreamToString(in);
/* 194 */       in.close();
/* 195 */       conn.disconnect();
/* 196 */       return res;
/* 197 */     } catch (Exception e) {
/* 198 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String convertStreamToString(InputStream is) {
/* 203 */     Scanner s = (new Scanner(is)).useDelimiter("\\A");
/* 204 */     return s.hasNext() ? s.next() : "/";
/*     */   }
/*     */   
/*     */   public static List<String> getHistoryOfNames(UUID id) {
/*     */     try {
/* 209 */       JsonArray array = getResources(new URL("https://api.mojang.com/user/profiles/" + getIdNoHyphens(id) + "/names"), "GET").getAsJsonArray();
/* 210 */       List<String> temp = Lists.newArrayList();
/* 211 */       for (JsonElement e : array) {
/* 212 */         JsonObject node = e.getAsJsonObject();
/* 213 */         String name = node.get("name").getAsString();
/* 214 */         long changedAt = node.has("changedToAt") ? node.get("changedToAt").getAsLong() : 0L;
/* 215 */         temp.add(name + "§8" + (new Date(changedAt)).toString());
/*     */       } 
/* 217 */       Collections.sort(temp);
/* 218 */       return temp;
/* 219 */     } catch (Exception ignored) {
/* 220 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getIdNoHyphens(UUID uuid) {
/* 225 */     return uuid.toString().replaceAll("-", "");
/*     */   }
/*     */   
/*     */   private static JsonElement getResources(URL url, String request) throws Exception {
/* 229 */     return getResources(url, request, (JsonElement)null);
/*     */   }
/*     */   
/*     */   private static JsonElement getResources(URL url, String request, JsonElement element) throws Exception {
/* 233 */     HttpsURLConnection connection = null;
/*     */     
/*     */     try {
/* 236 */       connection = (HttpsURLConnection)url.openConnection();
/* 237 */       connection.setDoOutput(true);
/* 238 */       connection.setRequestMethod(request);
/* 239 */       connection.setRequestProperty("Content-Type", "application/json");
/* 240 */       if (element != null) {
/* 241 */         DataOutputStream output = new DataOutputStream(connection.getOutputStream());
/* 242 */         output.writeBytes(AdvancementManager.field_192783_b.toJson(element));
/* 243 */         output.close();
/*     */       } 
/*     */       
/* 246 */       Scanner scanner = new Scanner(connection.getInputStream());
/* 247 */       StringBuilder builder = new StringBuilder();
/*     */       
/* 249 */       while (scanner.hasNextLine()) {
/* 250 */         builder.append(scanner.nextLine());
/* 251 */         builder.append('\n');
/*     */       } 
/*     */       
/* 254 */       scanner.close();
/* 255 */       String json = builder.toString();
/* 256 */       JsonElement data = PARSER.parse(json);
/* 257 */       return data;
/*     */     } finally {
/* 259 */       if (connection != null)
/* 260 */         connection.disconnect(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobo\\util\PlayerUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */