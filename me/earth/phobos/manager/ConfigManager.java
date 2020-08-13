/*     */ package me.earth.phobos.manager;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Scanner;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.render.XRay;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.EnumConverter;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.Util;
/*     */ 
/*     */ public class ConfigManager
/*     */   implements Util {
/*  38 */   public ArrayList<Feature> features = new ArrayList<>();
/*  39 */   public String config = "phobos/config/";
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadConfig(String name) {
/*  44 */     List<File> files = (List<File>)Arrays.<Object>stream((Object[])Objects.requireNonNull((new File("phobos")).listFiles())).filter(File::isDirectory).collect(Collectors.toList());
/*  45 */     if (files.contains(new File("phobos/" + name + "/"))) {
/*  46 */       this.config = "phobos/" + name + "/";
/*     */     } else {
/*  48 */       this.config = "phobos/config/";
/*     */     } 
/*  50 */     Phobos.friendManager.onLoad();
/*  51 */     for (Feature feature : this.features) {
/*     */       try {
/*  53 */         loadSettings(feature);
/*  54 */       } catch (IOException e) {
/*  55 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*  58 */     saveCurrentConfig();
/*     */   }
/*     */   
/*     */   public void saveConfig(String name) {
/*  62 */     this.config = "phobos/" + name + "/";
/*  63 */     File path = new File(this.config);
/*  64 */     if (!path.exists()) {
/*  65 */       path.mkdir();
/*     */     }
/*  67 */     Phobos.friendManager.saveFriends();
/*  68 */     for (Feature feature : this.features) {
/*     */       try {
/*  70 */         saveSettings(feature);
/*  71 */       } catch (IOException e) {
/*  72 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*  75 */     saveCurrentConfig();
/*     */   }
/*     */   
/*     */   public void saveCurrentConfig() {
/*  79 */     File currentConfig = new File("phobos/currentconfig.txt");
/*     */     try {
/*  81 */       if (currentConfig.exists()) {
/*  82 */         FileWriter writer = new FileWriter(currentConfig);
/*  83 */         String tempConfig = this.config.replaceAll("/", "");
/*  84 */         writer.write(tempConfig.replaceAll("phobos", ""));
/*  85 */         writer.close();
/*     */       } else {
/*  87 */         currentConfig.createNewFile();
/*  88 */         FileWriter writer = new FileWriter(currentConfig);
/*  89 */         String tempConfig = this.config.replaceAll("/", "");
/*  90 */         writer.write(tempConfig.replaceAll("phobos", ""));
/*  91 */         writer.close();
/*     */       } 
/*  93 */     } catch (Exception e) {
/*  94 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String loadCurrentConfig() {
/*  99 */     File currentConfig = new File("phobos/currentconfig.txt");
/* 100 */     String name = "config";
/*     */     try {
/* 102 */       if (currentConfig.exists()) {
/* 103 */         Scanner reader = new Scanner(currentConfig);
/* 104 */         while (reader.hasNextLine()) {
/* 105 */           name = reader.nextLine();
/*     */         }
/* 107 */         reader.close();
/*     */       } 
/* 109 */     } catch (Exception e) {
/* 110 */       e.printStackTrace();
/*     */     } 
/* 112 */     return name;
/*     */   }
/*     */   
/*     */   public void resetConfig(boolean saveConfig, String name) {
/* 116 */     for (Feature feature : this.features) {
/* 117 */       feature.reset();
/*     */     }
/* 119 */     if (saveConfig) saveConfig(name); 
/*     */   }
/*     */   
/*     */   public void saveSettings(Feature feature) throws IOException {
/* 123 */     JsonObject object = new JsonObject();
/* 124 */     File directory = new File(this.config + getDirectory(feature));
/* 125 */     if (!directory.exists()) {
/* 126 */       directory.mkdir();
/*     */     }
/* 128 */     String featureName = this.config + getDirectory(feature) + feature.getName() + ".json";
/* 129 */     Path outputFile = Paths.get(featureName, new String[0]);
/* 130 */     if (!Files.exists(outputFile, new java.nio.file.LinkOption[0])) {
/* 131 */       Files.createFile(outputFile, (FileAttribute<?>[])new FileAttribute[0]);
/*     */     }
/* 133 */     Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
/* 134 */     String json = gson.toJson((JsonElement)writeSettings(feature));
/* 135 */     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile, new java.nio.file.OpenOption[0])));
/* 136 */     writer.write(json);
/* 137 */     writer.close();
/*     */   }
/*     */   
/*     */   public static void setValueFromJson(Feature feature, Setting setting, JsonElement element) {
/*     */     String str;
/* 142 */     switch (setting.getType()) {
/*     */       case "Boolean":
/* 144 */         setting.setValue(Boolean.valueOf(element.getAsBoolean()));
/*     */         return;
/*     */       case "Double":
/* 147 */         setting.setValue(Double.valueOf(element.getAsDouble()));
/*     */         return;
/*     */       case "Float":
/* 150 */         setting.setValue(Float.valueOf(element.getAsFloat()));
/*     */         return;
/*     */       case "Integer":
/* 153 */         setting.setValue(Integer.valueOf(element.getAsInt()));
/*     */         return;
/*     */       case "String":
/* 156 */         str = element.getAsString();
/* 157 */         setting.setValue(str.replace("_", " "));
/*     */         return;
/*     */       case "Bind":
/* 160 */         setting.setValue((new Bind.BindConverter()).doBackward(element));
/*     */         return;
/*     */       case "Enum":
/*     */         try {
/* 164 */           EnumConverter converter = new EnumConverter(((Enum)setting.getValue()).getClass());
/* 165 */           Enum value = converter.doBackward(element);
/* 166 */           setting.setValue((value == null) ? setting.getDefaultValue() : value);
/*     */         }
/* 168 */         catch (Exception e) {}
/*     */         return;
/*     */     } 
/*     */     
/* 172 */     Phobos.LOGGER.error("Unknown Setting type for: " + feature.getName() + " : " + setting.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/* 178 */     this.features.addAll(Phobos.moduleManager.modules);
/* 179 */     this.features.add(Phobos.friendManager);
/*     */     
/* 181 */     String name = loadCurrentConfig();
/* 182 */     loadConfig(name);
/* 183 */     Phobos.LOGGER.info("Config loaded.");
/*     */   }
/*     */   
/*     */   private void loadSettings(Feature feature) throws IOException {
/* 187 */     String featureName = this.config + getDirectory(feature) + feature.getName() + ".json";
/* 188 */     Path featurePath = Paths.get(featureName, new String[0]);
/* 189 */     if (!Files.exists(featurePath, new java.nio.file.LinkOption[0])) {
/*     */       return;
/*     */     }
/* 192 */     loadPath(featurePath, feature);
/*     */   }
/*     */   
/*     */   private void loadPath(Path path, Feature feature) throws IOException {
/* 196 */     InputStream stream = Files.newInputStream(path, new java.nio.file.OpenOption[0]);
/*     */     try {
/* 198 */       loadFile((new JsonParser()).parse(new InputStreamReader(stream)).getAsJsonObject(), feature);
/* 199 */     } catch (IllegalStateException e) {
/* 200 */       Phobos.LOGGER.error("Bad Config File for: " + feature.getName() + ". Resetting...");
/* 201 */       loadFile(new JsonObject(), feature);
/*     */     } 
/* 203 */     stream.close();
/*     */   }
/*     */   
/*     */   private static void loadFile(JsonObject input, Feature feature) {
/* 207 */     for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)input.entrySet()) {
/* 208 */       String settingName = entry.getKey();
/* 209 */       JsonElement element = entry.getValue();
/* 210 */       if (feature instanceof FriendManager) {
/*     */         try {
/* 212 */           Phobos.friendManager.addFriend(new FriendManager.Friend(element.getAsString(), UUID.fromString(settingName)));
/* 213 */         } catch (Exception e) {
/* 214 */           e.printStackTrace();
/*     */         } 
/*     */       } else {
/* 217 */         boolean settingFound = false;
/* 218 */         for (Setting setting : feature.getSettings()) {
/* 219 */           if (settingName.equals(setting.getName())) {
/*     */             try {
/* 221 */               setValueFromJson(feature, setting, element);
/* 222 */             } catch (Exception e) {
/* 223 */               e.printStackTrace();
/*     */             } 
/* 225 */             settingFound = true;
/*     */           } 
/*     */         } 
/*     */         
/* 229 */         if (settingFound) {
/*     */           continue;
/*     */         }
/*     */       } 
/*     */       
/* 234 */       if (feature instanceof XRay) {
/* 235 */         feature.register(new Setting(settingName, Boolean.valueOf(true), v -> ((Boolean)((XRay)feature).showBlocks.getValue()).booleanValue()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public JsonObject writeSettings(Feature feature) {
/* 241 */     JsonObject object = new JsonObject();
/* 242 */     JsonParser jp = new JsonParser();
/* 243 */     for (Setting setting : feature.getSettings()) {
/* 244 */       if (setting.isEnumSetting()) {
/* 245 */         EnumConverter converter = new EnumConverter(((Enum)setting.getValue()).getClass());
/* 246 */         object.add(setting.getName(), converter.doForward((Enum)setting.getValue()));
/*     */         
/*     */         continue;
/*     */       } 
/* 250 */       if (setting.isStringSetting()) {
/* 251 */         String str = (String)setting.getValue();
/* 252 */         setting.setValue(str.replace(" ", "_"));
/*     */       } 
/*     */       
/*     */       try {
/* 256 */         object.add(setting.getName(), jp.parse(setting.getValueAsString()));
/* 257 */       } catch (Exception e) {
/* 258 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */     
/* 262 */     return object;
/*     */   }
/*     */   
/*     */   public String getDirectory(Feature feature) {
/* 266 */     String directory = "";
/* 267 */     if (feature instanceof Module) {
/* 268 */       directory = directory + ((Module)feature).getCategory().getName() + "/";
/*     */     }
/* 270 */     return directory;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\ConfigManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */