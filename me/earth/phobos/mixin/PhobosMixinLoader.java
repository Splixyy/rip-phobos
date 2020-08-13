/*    */ package me.earth.phobos.mixin;
/*    */ 
/*    */ import java.util.Map;
/*    */ import me.earth.phobos.Phobos;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
/*    */ import org.spongepowered.asm.launch.MixinBootstrap;
/*    */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*    */ import org.spongepowered.asm.mixin.Mixins;
/*    */ 
/*    */ public class PhobosMixinLoader
/*    */   implements IFMLLoadingPlugin
/*    */ {
/*    */   private static boolean isObfuscatedEnvironment = false;
/*    */   
/*    */   public PhobosMixinLoader() {
/* 16 */     Phobos.LOGGER.info("Phobos mixins initialized");
/* 17 */     MixinBootstrap.init();
/* 18 */     Mixins.addConfiguration("mixins.phobos.json");
/* 19 */     MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
/* 20 */     Phobos.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getASMTransformerClass() {
/* 25 */     return new String[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public String getModContainerClass() {
/* 30 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSetupClass() {
/* 35 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void injectData(Map<String, Object> data) {
/* 40 */     isObfuscatedEnvironment = ((Boolean)data.get("runtimeDeobfuscationEnabled")).booleanValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAccessTransformerClass() {
/* 45 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\PhobosMixinLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */