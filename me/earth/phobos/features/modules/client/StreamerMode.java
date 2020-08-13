/*     */ package me.earth.phobos.features.modules.client;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.TextUtil;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ 
/*     */ public class StreamerMode extends Module {
/*  20 */   private SecondScreenFrame window = null; public Setting<Integer> width; public Setting<Integer> height;
/*     */   
/*     */   public StreamerMode() {
/*  23 */     super("StreamerMode", "Displays client info in a second window.", Module.Category.CLIENT, false, false, false);
/*     */ 
/*     */     
/*  26 */     this.width = register(new Setting("Width", Integer.valueOf(600), Integer.valueOf(100), Integer.valueOf(3160)));
/*  27 */     this.height = register(new Setting("Height", Integer.valueOf(900), Integer.valueOf(100), Integer.valueOf(2140)));
/*     */   }
/*     */   
/*     */   public void onEnable() {
/*  31 */     EventQueue.invokeLater(() -> {
/*     */           if (this.window == null) {
/*     */             this.window = new SecondScreenFrame();
/*     */           }
/*     */           this.window.setVisible(true);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  41 */     if (this.window != null) {
/*  42 */       this.window.setVisible(false);
/*     */     }
/*  44 */     this.window = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  49 */     if (this.window != null) {
/*  50 */       ArrayList<String> drawInfo = new ArrayList<>();
/*  51 */       drawInfo.add("Phobos v1.3.3");
/*  52 */       drawInfo.add("");
/*  53 */       drawInfo.add("No Connection.");
/*  54 */       this.window.setToDraw(drawInfo);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUnload() {
/*  60 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLoad() {
/*  65 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  70 */     if (this.window != null) {
/*  71 */       ArrayList<String> drawInfo = new ArrayList<>();
/*  72 */       drawInfo.add("Phobos v1.3.3");
/*  73 */       drawInfo.add("");
/*  74 */       drawInfo.add("Fps: " + Minecraft.field_71470_ab);
/*  75 */       drawInfo.add("TPS: " + Phobos.serverManager.getTPS());
/*  76 */       drawInfo.add("Ping: " + Phobos.serverManager.getPing() + "ms");
/*  77 */       drawInfo.add("Speed: " + Phobos.speedManager.getSpeedKpH() + "km/h");
/*  78 */       drawInfo.add("Time: " + (new SimpleDateFormat("h:mm a")).format(new Date()));
/*  79 */       boolean inHell = mc.field_71441_e.func_180494_b(mc.field_71439_g.func_180425_c()).func_185359_l().equals("Hell");
/*     */       
/*  81 */       int posX = (int)mc.field_71439_g.field_70165_t;
/*  82 */       int posY = (int)mc.field_71439_g.field_70163_u;
/*  83 */       int posZ = (int)mc.field_71439_g.field_70161_v;
/*     */       
/*  85 */       float nether = !inHell ? 0.125F : 8.0F;
/*  86 */       int hposX = (int)(mc.field_71439_g.field_70165_t * nether);
/*  87 */       int hposZ = (int)(mc.field_71439_g.field_70161_v * nether);
/*     */       
/*  89 */       String coordinates = "XYZ " + posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]";
/*  90 */       String text = Phobos.rotationManager.getDirection4D(false);
/*  91 */       drawInfo.add("");
/*  92 */       drawInfo.add(text);
/*  93 */       drawInfo.add(coordinates);
/*  94 */       drawInfo.add("");
/*     */       
/*  96 */       for (Module module : Phobos.moduleManager.sortedModules) {
/*  97 */         String moduleName = TextUtil.stripColor(module.getFullArrayString());
/*  98 */         drawInfo.add(moduleName);
/*     */       } 
/*     */       
/* 101 */       drawInfo.add("");
/* 102 */       for (PotionEffect effect : Phobos.potionManager.getOwnPotions()) {
/* 103 */         String potionText = TextUtil.stripColor(Phobos.potionManager.getColoredPotionString(effect));
/* 104 */         drawInfo.add(potionText);
/*     */       } 
/*     */       
/* 107 */       drawInfo.add("");
/* 108 */       Map<String, Integer> map = EntityUtil.getTextRadarPlayers();
/* 109 */       if (!map.isEmpty()) {
/* 110 */         for (Map.Entry<String, Integer> player : map.entrySet()) {
/* 111 */           String playerText = TextUtil.stripColor(player.getKey());
/* 112 */           drawInfo.add(playerText);
/*     */         } 
/*     */       }
/*     */       
/* 116 */       this.window.setToDraw(drawInfo);
/*     */     } 
/*     */   }
/*     */   
/*     */   public class SecondScreenFrame
/*     */     extends JFrame {
/*     */     private StreamerMode.SecondScreen panel;
/*     */     
/*     */     public SecondScreenFrame() {
/* 125 */       initUI();
/*     */     }
/*     */     
/*     */     private void initUI() {
/* 129 */       this.panel = new StreamerMode.SecondScreen();
/* 130 */       add(this.panel);
/* 131 */       setResizable(true);
/* 132 */       pack();
/* 133 */       setTitle("Phobos - Info");
/* 134 */       setLocationRelativeTo((Component)null);
/* 135 */       setDefaultCloseOperation(2);
/*     */     }
/*     */     
/*     */     public void setToDraw(ArrayList<String> list) {
/* 139 */       this.panel.setToDraw(list);
/*     */     }
/*     */   }
/*     */   
/*     */   public class SecondScreen
/*     */     extends JPanel {
/* 145 */     private final int B_WIDTH = ((Integer)StreamerMode.this.width.getValue()).intValue();
/* 146 */     private final int B_HEIGHT = ((Integer)StreamerMode.this.height.getValue()).intValue();
/*     */     
/* 148 */     private Font font = new Font("Verdana", 0, 20);
/*     */     
/* 150 */     private ArrayList<String> toDraw = new ArrayList<>();
/*     */     
/*     */     public void setToDraw(ArrayList<String> list) {
/* 153 */       this.toDraw = list;
/* 154 */       repaint();
/*     */     }
/*     */     
/*     */     public void setFont(Font font) {
/* 158 */       this.font = font;
/*     */     }
/*     */     
/*     */     public SecondScreen() {
/* 162 */       initBoard();
/*     */     }
/*     */     
/*     */     public void setWindowSize(int width, int height) {
/* 166 */       setPreferredSize(new Dimension(width, height));
/*     */     }
/*     */     
/*     */     private void initBoard() {
/* 170 */       setBackground(Color.black);
/* 171 */       setFocusable(true);
/* 172 */       setPreferredSize(new Dimension(this.B_WIDTH, this.B_HEIGHT));
/*     */     }
/*     */ 
/*     */     
/*     */     public void paintComponent(Graphics g) {
/* 177 */       super.paintComponent(g);
/* 178 */       drawScreen(g);
/*     */     }
/*     */     
/*     */     private void drawScreen(Graphics g) {
/* 182 */       Font small = this.font;
/* 183 */       FontMetrics metr = getFontMetrics(small);
/*     */       
/* 185 */       g.setColor(Color.white);
/* 186 */       g.setFont(small);
/* 187 */       int y = 40;
/* 188 */       for (String msg : this.toDraw) {
/* 189 */         g.drawString(msg, (getWidth() - metr.stringWidth(msg)) / 2, y);
/* 190 */         y += 20;
/*     */       } 
/* 192 */       Toolkit.getDefaultToolkit().sync();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\client\StreamerMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */