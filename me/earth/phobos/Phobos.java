/*     */ package me.earth.phobos;
/*     */ 
/*     */ import me.earth.phobos.manager.ColorManager;
/*     */ import me.earth.phobos.manager.CommandManager;
/*     */ import me.earth.phobos.manager.ConfigManager;
/*     */ import me.earth.phobos.manager.EventManager;
/*     */ import me.earth.phobos.manager.FileManager;
/*     */ import me.earth.phobos.manager.FriendManager;
/*     */ import me.earth.phobos.manager.HoleManager;
/*     */ import me.earth.phobos.manager.InventoryManager;
/*     */ import me.earth.phobos.manager.ModuleManager;
/*     */ import me.earth.phobos.manager.NotificationManager;
/*     */ import me.earth.phobos.manager.PacketManager;
/*     */ import me.earth.phobos.manager.PositionManager;
/*     */ import me.earth.phobos.manager.PotionManager;
/*     */ import me.earth.phobos.manager.ReloadManager;
/*     */ import me.earth.phobos.manager.RotationManager;
/*     */ import me.earth.phobos.manager.ServerManager;
/*     */ import me.earth.phobos.manager.SpeedManager;
/*     */ import me.earth.phobos.manager.TextManager;
/*     */ import me.earth.phobos.manager.TimerManager;
/*     */ import me.earth.phobos.manager.TotemPopManager;
/*     */ import net.minecraftforge.fml.common.Mod;
/*     */ import net.minecraftforge.fml.common.Mod.EventHandler;
/*     */ import net.minecraftforge.fml.common.Mod.Instance;
/*     */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.opengl.Display;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Mod(modid = "earthhack", name = "3arthh4ck", version = "1.3.3")
/*     */ public class Phobos
/*     */ {
/*     */   public static final String MODID = "earthhack";
/*     */   public static final String MODNAME = "3arthh4ck";
/*     */   public static final String MODVER = "1.3.3";
/*  40 */   public static final Logger LOGGER = LogManager.getLogger("3arthh4ck"); public static final String NAME_UNICODE = "3ᴀʀᴛʜʜ4ᴄᴋ"; public static final String PHOBOS_UNICODE = "ᴘʜᴏʙᴏꜱ";
/*     */   public static final String CHAT_SUFFIX = " ⏐ 3ᴀʀᴛʜʜ4ᴄᴋ";
/*     */   public static final String PHOBOS_SUFFIX = " ⏐ ᴘʜᴏʙᴏꜱ";
/*     */   public static ModuleManager moduleManager;
/*     */   public static SpeedManager speedManager;
/*     */   public static PositionManager positionManager;
/*     */   public static RotationManager rotationManager;
/*     */   public static CommandManager commandManager;
/*     */   public static EventManager eventManager;
/*     */   public static ConfigManager configManager;
/*     */   public static FileManager fileManager;
/*     */   public static FriendManager friendManager;
/*     */   public static TextManager textManager;
/*     */   public static ColorManager colorManager;
/*     */   public static ServerManager serverManager;
/*     */   public static PotionManager potionManager;
/*     */   public static InventoryManager inventoryManager;
/*     */   public static TimerManager timerManager;
/*     */   public static PacketManager packetManager;
/*     */   public static ReloadManager reloadManager;
/*     */   public static TotemPopManager totemPopManager;
/*     */   public static HoleManager holeManager;
/*     */   public static NotificationManager notificationManager;
/*     */   private static boolean unloaded = false;
/*     */   @Instance
/*     */   public static Phobos INSTANCE;
/*     */   
/*     */   @EventHandler
/*     */   public void preInit(FMLPreInitializationEvent event) {
/*  69 */     LOGGER.info("ohare is cute!!!");
/*  70 */     LOGGER.info("faggot above - 3vt");
/*  71 */     LOGGER.info("megyn wins again");
/*  72 */     LOGGER.info("gtfo my logs - 3arth");
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void init(FMLInitializationEvent event) {
/*  77 */     Display.setTitle("3arthh4ck - v.1.3.3");
/*  78 */     load();
/*     */   }
/*     */   
/*     */   public static void load() {
/*  82 */     LOGGER.info("\n\nLoading 3arthh4ck 1.3.3");
/*  83 */     unloaded = false;
/*  84 */     if (reloadManager != null) {
/*  85 */       reloadManager.unload();
/*  86 */       reloadManager = null;
/*     */     } 
/*     */     
/*  89 */     totemPopManager = new TotemPopManager();
/*  90 */     timerManager = new TimerManager();
/*  91 */     packetManager = new PacketManager();
/*  92 */     serverManager = new ServerManager();
/*  93 */     colorManager = new ColorManager();
/*  94 */     textManager = new TextManager();
/*  95 */     moduleManager = new ModuleManager();
/*  96 */     speedManager = new SpeedManager();
/*  97 */     rotationManager = new RotationManager();
/*  98 */     positionManager = new PositionManager();
/*  99 */     commandManager = new CommandManager();
/* 100 */     eventManager = new EventManager();
/* 101 */     configManager = new ConfigManager();
/* 102 */     fileManager = new FileManager();
/* 103 */     friendManager = new FriendManager();
/* 104 */     potionManager = new PotionManager();
/* 105 */     inventoryManager = new InventoryManager();
/* 106 */     holeManager = new HoleManager();
/* 107 */     notificationManager = new NotificationManager();
/* 108 */     LOGGER.info("Initialized Managers");
/*     */     
/* 110 */     moduleManager.init();
/* 111 */     LOGGER.info("Modules loaded.");
/* 112 */     configManager.init();
/* 113 */     eventManager.init();
/* 114 */     LOGGER.info("EventManager loaded.");
/* 115 */     textManager.init(true);
/* 116 */     moduleManager.onLoad();
/* 117 */     totemPopManager.init();
/* 118 */     timerManager.init();
/* 119 */     LOGGER.info("3arthh4ck initialized!\n");
/*     */   }
/*     */   
/*     */   public static void unload(boolean unload) {
/* 123 */     LOGGER.info("\n\nUnloading 3arthh4ck 1.3.3");
/* 124 */     if (unload) {
/* 125 */       reloadManager = new ReloadManager();
/* 126 */       reloadManager.init((commandManager != null) ? commandManager.getPrefix() : ".");
/*     */     } 
/* 128 */     onUnload();
/* 129 */     eventManager = null;
/* 130 */     holeManager = null;
/* 131 */     timerManager = null;
/* 132 */     moduleManager = null;
/* 133 */     totemPopManager = null;
/* 134 */     serverManager = null;
/* 135 */     colorManager = null;
/* 136 */     textManager = null;
/* 137 */     speedManager = null;
/* 138 */     rotationManager = null;
/* 139 */     positionManager = null;
/* 140 */     commandManager = null;
/* 141 */     configManager = null;
/* 142 */     fileManager = null;
/* 143 */     friendManager = null;
/* 144 */     potionManager = null;
/* 145 */     inventoryManager = null;
/* 146 */     notificationManager = null;
/* 147 */     LOGGER.info("3arthh4ck unloaded!\n");
/*     */   }
/*     */   
/*     */   public static void reload() {
/* 151 */     unload(false);
/* 152 */     load();
/*     */   }
/*     */   
/*     */   public static void onUnload() {
/* 156 */     if (!unloaded) {
/* 157 */       eventManager.onUnload();
/* 158 */       moduleManager.onUnload();
/* 159 */       configManager.saveConfig(configManager.config.replaceFirst("phobos/", ""));
/* 160 */       moduleManager.onUnloadPost();
/* 161 */       timerManager.unload();
/* 162 */       unloaded = true;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\Phobos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */