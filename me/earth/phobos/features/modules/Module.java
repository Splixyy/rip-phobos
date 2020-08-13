/*     */ package me.earth.phobos.features.modules;
/*     */ 
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.event.events.Render2DEvent;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ 
/*     */ public class Module
/*     */   extends Feature {
/*     */   private final String description;
/*     */   private final Category category;
/*  18 */   public Setting<Boolean> enabled = register(new Setting("Enabled", Boolean.valueOf(false)));
/*  19 */   public Setting<Boolean> drawn = register(new Setting("Drawn", Boolean.valueOf(true)));
/*  20 */   public Setting<Bind> bind = register(new Setting("Bind", new Bind(-1)));
/*     */   public Setting<String> displayName;
/*     */   public boolean hasListener;
/*     */   public boolean alwaysListening;
/*     */   public boolean hidden;
/*     */   
/*     */   public Module(String name, String description, Category category, boolean hasListener, boolean hidden, boolean alwaysListening) {
/*  27 */     super(name);
/*  28 */     this.displayName = register(new Setting("DisplayName", name));
/*  29 */     this.description = description;
/*  30 */     this.category = category;
/*  31 */     this.hasListener = hasListener;
/*  32 */     this.hidden = hidden;
/*  33 */     this.alwaysListening = alwaysListening;
/*     */   }
/*     */   
/*     */   public enum Category {
/*  37 */     COMBAT("Combat"),
/*  38 */     MISC("Misc"),
/*  39 */     RENDER("Render"),
/*  40 */     MOVEMENT("Movement"),
/*  41 */     PLAYER("Player"),
/*  42 */     CLIENT("Client");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     Category(String name) {
/*  47 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  51 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnable() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onToggle() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onLoad() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTick() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onLogin() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onLogout() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUpdate() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRender2D(Render2DEvent event) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {}
/*     */ 
/*     */   
/*     */   public void onUnload() {}
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 100 */     return null;
/*     */   }
/*     */   public boolean isOn() {
/* 103 */     return ((Boolean)this.enabled.getValue()).booleanValue();
/*     */   } public boolean isOff() {
/* 105 */     return !((Boolean)this.enabled.getValue()).booleanValue();
/*     */   }
/*     */   public void setEnabled(boolean enabled) {
/* 108 */     if (enabled) {
/* 109 */       enable();
/*     */     } else {
/* 111 */       disable();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void enable() {
/* 116 */     this.enabled.setValue(Boolean.valueOf(true));
/* 117 */     onToggle();
/* 118 */     onEnable();
/* 119 */     if (isOn() && this.hasListener && !this.alwaysListening) {
/* 120 */       MinecraftForge.EVENT_BUS.register(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void disable() {
/* 125 */     if (this.hasListener && !this.alwaysListening) {
/* 126 */       MinecraftForge.EVENT_BUS.unregister(this);
/*     */     }
/* 128 */     this.enabled.setValue(Boolean.valueOf(false));
/* 129 */     onToggle();
/* 130 */     onDisable();
/*     */   }
/*     */   
/*     */   public void toggle() {
/* 134 */     ClientEvent event = new ClientEvent(!isEnabled() ? 1 : 0, this);
/* 135 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 136 */     if (!event.isCanceled()) {
/* 137 */       setEnabled(!isEnabled());
/*     */     }
/*     */   }
/*     */   
/*     */   public String getDisplayName() {
/* 142 */     return (String)this.displayName.getValue();
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 146 */     return this.description;
/*     */   }
/*     */   
/*     */   public void setDisplayName(String name) {
/* 150 */     Module module = Phobos.moduleManager.getModuleByDisplayName(name);
/* 151 */     Module originalModule = Phobos.moduleManager.getModuleByName(name);
/* 152 */     if (module == null && originalModule == null) {
/* 153 */       Command.sendMessage(getDisplayName() + ", Original name: " + getName() + ", has been renamed to: " + name);
/* 154 */       this.displayName.setValue(name);
/*     */       return;
/*     */     } 
/* 157 */     Command.sendMessage("§cA module of this name already exists.");
/*     */   }
/*     */   
/*     */   public boolean isDrawn() {
/* 161 */     return ((Boolean)this.drawn.getValue()).booleanValue();
/*     */   }
/*     */   
/*     */   public void setDrawn(boolean drawn) {
/* 165 */     this.drawn.setValue(Boolean.valueOf(drawn));
/*     */   }
/*     */   
/*     */   public Category getCategory() {
/* 169 */     return this.category;
/*     */   }
/*     */   
/*     */   public String getInfo() {
/* 173 */     return null;
/*     */   }
/*     */   
/*     */   public Bind getBind() {
/* 177 */     return (Bind)this.bind.getValue();
/*     */   }
/*     */   
/*     */   public void setBind(int key) {
/* 181 */     this.bind.setValue(new Bind(key));
/*     */   }
/*     */   
/*     */   public boolean listening() {
/* 185 */     return ((this.hasListener && isOn()) || this.alwaysListening);
/*     */   }
/*     */   
/*     */   public String getFullArrayString() {
/* 189 */     return getDisplayName() + "§8" + ((getDisplayInfo() != null) ? (" [§r" + getDisplayInfo() + "§8" + "]") : "");
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\Module.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */