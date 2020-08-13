/*     */ package me.earth.phobos.manager;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ 
/*     */ public class FileManager
/*     */   extends Feature
/*     */ {
/*     */   private final Path base;
/*     */   private final Path config;
/*     */   
/*     */   private String[] expandPath(String fullPath) {
/*  28 */     return fullPath.split(":?\\\\\\\\|\\/");
/*     */   }
/*     */   
/*     */   private Stream<String> expandPaths(String... paths) {
/*  32 */     return Arrays.<String>stream(paths).map(this::expandPath).flatMap(Arrays::stream);
/*     */   }
/*     */   
/*     */   private Path lookupPath(Path root, String... paths) {
/*  36 */     return Paths.get(root.toString(), paths);
/*     */   }
/*     */   
/*     */   private Path getRoot() {
/*  40 */     return Paths.get("", new String[0]);
/*     */   }
/*     */   
/*     */   private void createDirectory(Path dir) {
/*     */     try {
/*  45 */       if (!Files.isDirectory(dir, new java.nio.file.LinkOption[0])) {
/*  46 */         if (Files.exists(dir, new java.nio.file.LinkOption[0])) {
/*  47 */           Files.delete(dir);
/*     */         }
/*     */         
/*  50 */         Files.createDirectories(dir, (FileAttribute<?>[])new FileAttribute[0]);
/*     */       } 
/*  52 */     } catch (IOException e) {
/*  53 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private Path getMkDirectory(Path parent, String... paths) {
/*  58 */     if (paths.length < 1) {
/*  59 */       return parent;
/*     */     }
/*     */     
/*  62 */     Path dir = lookupPath(parent, paths);
/*  63 */     createDirectory(dir);
/*  64 */     return dir;
/*     */   }
/*     */   
/*     */   public FileManager() {
/*  68 */     this.base = getMkDirectory(getRoot(), new String[] { "phobos" });
/*  69 */     this.config = getMkDirectory(this.base, new String[] { "config" });
/*  70 */     getMkDirectory(this.base, new String[] { "util" });
/*  71 */     for (Module.Category category : Phobos.moduleManager.getCategories()) {
/*  72 */       getMkDirectory(this.config, new String[] { category.getName() });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getBasePath() {
/*  80 */     return this.base;
/*     */   }
/*     */   
/*     */   public Path getBaseResolve(String... paths) {
/*  84 */     String[] names = expandPaths(paths).<String>toArray(x$0 -> new String[x$0]);
/*  85 */     if (names.length < 1) {
/*  86 */       throw new IllegalArgumentException("missing path");
/*     */     }
/*     */     
/*  89 */     return lookupPath(getBasePath(), names);
/*     */   }
/*     */   
/*     */   public Path getMkBaseResolve(String... paths) {
/*  93 */     Path path = getBaseResolve(paths);
/*  94 */     createDirectory(path.getParent());
/*  95 */     return path;
/*     */   }
/*     */   
/*     */   public Path getConfig() {
/*  99 */     return getBasePath().resolve("config");
/*     */   }
/*     */   
/*     */   public Path getCache() {
/* 103 */     return getBasePath().resolve("cache");
/*     */   }
/*     */   
/*     */   public Path getMkBaseDirectory(String... names) {
/* 107 */     return getMkDirectory(getBasePath(), new String[] { expandPaths(names).collect(Collectors.joining(File.separator)) });
/*     */   }
/*     */   
/*     */   public Path getMkConfigDirectory(String... names) {
/* 111 */     return getMkDirectory(getConfig(), new String[] { expandPaths(names).collect(Collectors.joining(File.separator)) });
/*     */   }
/*     */   
/*     */   public static boolean appendTextFile(String data, String file) {
/*     */     try {
/* 116 */       Path path = Paths.get(file, new String[0]);
/* 117 */       Files.write(path, Collections.singletonList(data), StandardCharsets.UTF_8, new OpenOption[] { Files.exists(path, new java.nio.file.LinkOption[0]) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE });
/* 118 */     } catch (IOException e) {
/* 119 */       System.out.println("WARNING: Unable to write file: " + file);
/* 120 */       return false;
/*     */     } 
/* 122 */     return true;
/*     */   }
/*     */   
/*     */   public static List<String> readTextFileAllLines(String file) {
/*     */     try {
/* 127 */       Path path = Paths.get(file, new String[0]);
/* 128 */       return Files.readAllLines(path, StandardCharsets.UTF_8);
/* 129 */     } catch (IOException e) {
/* 130 */       System.out.println("WARNING: Unable to read file, creating new file: " + file);
/* 131 */       appendTextFile("", file);
/*     */       
/* 133 */       return Collections.emptyList();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\FileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */