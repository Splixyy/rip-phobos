/*    */ package me.earth.phobos.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.OpenOption;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.nio.file.StandardOpenOption;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public class FileUtil {
/*    */   public static boolean appendTextFile(String data, String file) {
/*    */     try {
/* 16 */       Path path = Paths.get(file, new String[0]);
/* 17 */       Files.write(path, Collections.singletonList(data), StandardCharsets.UTF_8, new OpenOption[] { Files.exists(path, new java.nio.file.LinkOption[0]) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE });
/* 18 */     } catch (IOException e) {
/* 19 */       System.out.println("WARNING: Unable to write file: " + file);
/* 20 */       return false;
/*    */     } 
/* 22 */     return true;
/*    */   }
/*    */   
/*    */   public static List<String> readTextFileAllLines(String file) {
/*    */     try {
/* 27 */       Path path = Paths.get(file, new String[0]);
/* 28 */       return Files.readAllLines(path, StandardCharsets.UTF_8);
/* 29 */     } catch (IOException e) {
/* 30 */       System.out.println("WARNING: Unable to read file, creating new file: " + file);
/* 31 */       appendTextFile("", file);
/*    */       
/* 33 */       return Collections.emptyList();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobo\\util\FileUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */