/*     */ package me.earth.phobos.manager;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import me.earth.phobos.event.events.Render2DEvent;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.ClickGui;
/*     */ import me.earth.phobos.features.modules.client.Components;
/*     */ import me.earth.phobos.features.modules.client.FontMod;
/*     */ import me.earth.phobos.features.modules.client.HUD;
/*     */ import me.earth.phobos.features.modules.client.Managers;
/*     */ import me.earth.phobos.features.modules.client.Notifications;
/*     */ import me.earth.phobos.features.modules.client.StreamerMode;
/*     */ import me.earth.phobos.features.modules.combat.AntiTrap;
/*     */ import me.earth.phobos.features.modules.combat.ArmorMessage;
/*     */ import me.earth.phobos.features.modules.combat.Auto32k;
/*     */ import me.earth.phobos.features.modules.combat.AutoArmor;
/*     */ import me.earth.phobos.features.modules.combat.AutoCrystal;
/*     */ import me.earth.phobos.features.modules.combat.AutoTrap;
/*     */ import me.earth.phobos.features.modules.combat.BedBomb;
/*     */ import me.earth.phobos.features.modules.combat.BowSpam;
/*     */ import me.earth.phobos.features.modules.combat.Crasher;
/*     */ import me.earth.phobos.features.modules.combat.Criticals;
/*     */ import me.earth.phobos.features.modules.combat.HoleFiller;
/*     */ import me.earth.phobos.features.modules.combat.Killaura;
/*     */ import me.earth.phobos.features.modules.combat.Offhand;
/*     */ import me.earth.phobos.features.modules.combat.Selftrap;
/*     */ import me.earth.phobos.features.modules.combat.Surround;
/*     */ import me.earth.phobos.features.modules.combat.Webaura;
/*     */ import me.earth.phobos.features.modules.misc.AntiPackets;
/*     */ import me.earth.phobos.features.modules.misc.AntiVanish;
/*     */ import me.earth.phobos.features.modules.misc.AutoLog;
/*     */ import me.earth.phobos.features.modules.misc.AutoReconnect;
/*     */ import me.earth.phobos.features.modules.misc.AutoRespawn;
/*     */ import me.earth.phobos.features.modules.misc.BetterPortals;
/*     */ import me.earth.phobos.features.modules.misc.BuildHeight;
/*     */ import me.earth.phobos.features.modules.misc.Bypass;
/*     */ import me.earth.phobos.features.modules.misc.ChatModifier;
/*     */ import me.earth.phobos.features.modules.misc.Exploits;
/*     */ import me.earth.phobos.features.modules.misc.ExtraTab;
/*     */ import me.earth.phobos.features.modules.misc.KitDelete;
/*     */ import me.earth.phobos.features.modules.misc.MCF;
/*     */ import me.earth.phobos.features.modules.misc.MobOwner;
/*     */ import me.earth.phobos.features.modules.misc.NoAFK;
/*     */ import me.earth.phobos.features.modules.misc.NoHandShake;
/*     */ import me.earth.phobos.features.modules.misc.NoRotate;
/*     */ import me.earth.phobos.features.modules.misc.NoSoundLag;
/*     */ import me.earth.phobos.features.modules.misc.Nuker;
/*     */ import me.earth.phobos.features.modules.misc.PingSpoof;
/*     */ import me.earth.phobos.features.modules.misc.Spammer;
/*     */ import me.earth.phobos.features.modules.misc.ToolTips;
/*     */ import me.earth.phobos.features.modules.misc.Tracker;
/*     */ import me.earth.phobos.features.modules.movement.AntiLevitate;
/*     */ import me.earth.phobos.features.modules.movement.ElytraFlight;
/*     */ import me.earth.phobos.features.modules.movement.Flight;
/*     */ import me.earth.phobos.features.modules.movement.HoleTP;
/*     */ import me.earth.phobos.features.modules.movement.IceSpeed;
/*     */ import me.earth.phobos.features.modules.movement.NoFall;
/*     */ import me.earth.phobos.features.modules.movement.NoSlowDown;
/*     */ import me.earth.phobos.features.modules.movement.Phase;
/*     */ import me.earth.phobos.features.modules.movement.SafeWalk;
/*     */ import me.earth.phobos.features.modules.movement.Speed;
/*     */ import me.earth.phobos.features.modules.movement.Sprint;
/*     */ import me.earth.phobos.features.modules.movement.Static;
/*     */ import me.earth.phobos.features.modules.movement.Step;
/*     */ import me.earth.phobos.features.modules.movement.Strafe;
/*     */ import me.earth.phobos.features.modules.movement.TPSpeed;
/*     */ import me.earth.phobos.features.modules.movement.Velocity;
/*     */ import me.earth.phobos.features.modules.player.Blink;
/*     */ import me.earth.phobos.features.modules.player.BlockTweaks;
/*     */ import me.earth.phobos.features.modules.player.EchestBP;
/*     */ import me.earth.phobos.features.modules.player.FakePlayer;
/*     */ import me.earth.phobos.features.modules.player.FastPlace;
/*     */ import me.earth.phobos.features.modules.player.Freecam;
/*     */ import me.earth.phobos.features.modules.player.Jesus;
/*     */ import me.earth.phobos.features.modules.player.LiquidInteract;
/*     */ import me.earth.phobos.features.modules.player.MCP;
/*     */ import me.earth.phobos.features.modules.player.MultiTask;
/*     */ import me.earth.phobos.features.modules.player.NoHunger;
/*     */ import me.earth.phobos.features.modules.player.Reach;
/*     */ import me.earth.phobos.features.modules.player.Replenish;
/*     */ import me.earth.phobos.features.modules.player.Scaffold;
/*     */ import me.earth.phobos.features.modules.player.Speedmine;
/*     */ import me.earth.phobos.features.modules.player.TimerSpeed;
/*     */ import me.earth.phobos.features.modules.player.TpsSync;
/*     */ import me.earth.phobos.features.modules.player.XCarry;
/*     */ import me.earth.phobos.features.modules.render.BlockHighlight;
/*     */ import me.earth.phobos.features.modules.render.CameraClip;
/*     */ import me.earth.phobos.features.modules.render.Chams;
/*     */ import me.earth.phobos.features.modules.render.ESP;
/*     */ import me.earth.phobos.features.modules.render.Fullbright;
/*     */ import me.earth.phobos.features.modules.render.HoleESP;
/*     */ import me.earth.phobos.features.modules.render.LogoutSpots;
/*     */ import me.earth.phobos.features.modules.render.Nametags;
/*     */ import me.earth.phobos.features.modules.render.NoRender;
/*     */ import me.earth.phobos.features.modules.render.PortalESP;
/*     */ import me.earth.phobos.features.modules.render.Ranges;
/*     */ import me.earth.phobos.features.modules.render.Skeleton;
/*     */ import me.earth.phobos.features.modules.render.SmallShield;
/*     */ import me.earth.phobos.features.modules.render.StorageESP;
/*     */ import me.earth.phobos.features.modules.render.Tracer;
/*     */ import me.earth.phobos.features.modules.render.Trajectories;
/*     */ import me.earth.phobos.features.modules.render.XRay;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ 
/*     */ public class ModuleManager
/*     */   extends Feature
/*     */ {
/* 116 */   public ArrayList<Module> modules = new ArrayList<>();
/* 117 */   public List<Module> sortedModules = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public void init() {
/* 121 */     this.modules.add(new Offhand());
/* 122 */     this.modules.add(new Surround());
/* 123 */     this.modules.add(new AutoTrap());
/* 124 */     this.modules.add(new AutoCrystal());
/* 125 */     this.modules.add(new Criticals());
/* 126 */     this.modules.add(new BowSpam());
/* 127 */     this.modules.add(new Killaura());
/* 128 */     this.modules.add(new HoleFiller());
/* 129 */     this.modules.add(new Selftrap());
/* 130 */     this.modules.add(new Webaura());
/* 131 */     this.modules.add(new AutoArmor());
/* 132 */     this.modules.add(new AntiTrap());
/* 133 */     this.modules.add(new BedBomb());
/* 134 */     this.modules.add(new ArmorMessage());
/* 135 */     this.modules.add(new Crasher());
/* 136 */     this.modules.add(new Auto32k());
/*     */ 
/*     */     
/* 139 */     this.modules.add(new ChatModifier());
/* 140 */     this.modules.add(new BetterPortals());
/* 141 */     this.modules.add(new BuildHeight());
/* 142 */     this.modules.add(new NoHandShake());
/* 143 */     this.modules.add(new AutoRespawn());
/* 144 */     this.modules.add(new NoRotate());
/* 145 */     this.modules.add(new MCF());
/* 146 */     this.modules.add(new PingSpoof());
/* 147 */     this.modules.add(new NoSoundLag());
/* 148 */     this.modules.add(new AutoLog());
/* 149 */     this.modules.add(new KitDelete());
/* 150 */     this.modules.add(new Exploits());
/* 151 */     this.modules.add(new Spammer());
/* 152 */     this.modules.add(new AntiVanish());
/* 153 */     this.modules.add(new ExtraTab());
/* 154 */     this.modules.add(new MobOwner());
/* 155 */     this.modules.add(new Nuker());
/* 156 */     this.modules.add(new AutoReconnect());
/* 157 */     this.modules.add(new NoAFK());
/* 158 */     this.modules.add(new Tracker());
/* 159 */     this.modules.add(new AntiPackets());
/*     */ 
/*     */     
/* 162 */     this.modules.add(new Bypass());
/* 163 */     this.modules.add(new Strafe());
/* 164 */     this.modules.add(new Velocity());
/* 165 */     this.modules.add(new Speed());
/* 166 */     this.modules.add(new Step());
/* 167 */     this.modules.add(new Sprint());
/* 168 */     this.modules.add(new AntiLevitate());
/* 169 */     this.modules.add(new Phase());
/* 170 */     this.modules.add(new Static());
/* 171 */     this.modules.add(new TPSpeed());
/* 172 */     this.modules.add(new Flight());
/* 173 */     this.modules.add(new ElytraFlight());
/* 174 */     this.modules.add(new NoSlowDown());
/* 175 */     this.modules.add(new HoleTP());
/* 176 */     this.modules.add(new NoFall());
/* 177 */     this.modules.add(new IceSpeed());
/*     */ 
/*     */     
/* 180 */     this.modules.add(new Reach());
/* 181 */     this.modules.add(new LiquidInteract());
/* 182 */     this.modules.add(new FakePlayer());
/* 183 */     this.modules.add(new TimerSpeed());
/* 184 */     this.modules.add(new FastPlace());
/* 185 */     this.modules.add(new Freecam());
/* 186 */     this.modules.add(new Speedmine());
/* 187 */     this.modules.add(new SafeWalk());
/* 188 */     this.modules.add(new Blink());
/* 189 */     this.modules.add(new MultiTask());
/* 190 */     this.modules.add(new BlockTweaks());
/* 191 */     this.modules.add(new XCarry());
/* 192 */     this.modules.add(new Replenish());
/* 193 */     this.modules.add(new NoHunger());
/* 194 */     this.modules.add(new Jesus());
/* 195 */     this.modules.add(new Scaffold());
/* 196 */     this.modules.add(new EchestBP());
/* 197 */     this.modules.add(new TpsSync());
/* 198 */     this.modules.add(new MCP());
/*     */ 
/*     */     
/* 201 */     this.modules.add(new StorageESP());
/* 202 */     this.modules.add(new NoRender());
/* 203 */     this.modules.add(new SmallShield());
/* 204 */     this.modules.add(new Fullbright());
/* 205 */     this.modules.add(new Nametags());
/* 206 */     this.modules.add(new CameraClip());
/* 207 */     this.modules.add(new Chams());
/* 208 */     this.modules.add(new Skeleton());
/* 209 */     this.modules.add(new ESP());
/* 210 */     this.modules.add(new HoleESP());
/* 211 */     this.modules.add(new BlockHighlight());
/* 212 */     this.modules.add(new Trajectories());
/* 213 */     this.modules.add(new Tracer());
/* 214 */     this.modules.add(new LogoutSpots());
/* 215 */     this.modules.add(new XRay());
/* 216 */     this.modules.add(new PortalESP());
/* 217 */     this.modules.add(new Ranges());
/*     */ 
/*     */     
/* 220 */     this.modules.add(new Notifications());
/* 221 */     this.modules.add(new HUD());
/* 222 */     this.modules.add(new ToolTips());
/* 223 */     this.modules.add(new FontMod());
/* 224 */     this.modules.add(new ClickGui());
/* 225 */     this.modules.add(new Managers());
/* 226 */     this.modules.add(new Components());
/* 227 */     this.modules.add(new StreamerMode());
/*     */   }
/*     */   
/*     */   public Module getModuleByName(String name) {
/* 231 */     for (Module module : this.modules) {
/* 232 */       if (module.getName().equalsIgnoreCase(name)) {
/* 233 */         return module;
/*     */       }
/*     */     } 
/* 236 */     return null;
/*     */   }
/*     */   
/*     */   public <T extends Module> T getModuleByClass(Class<T> clazz) {
/* 240 */     for (Module module : this.modules) {
/* 241 */       if (clazz.isInstance(module)) {
/* 242 */         return (T)module;
/*     */       }
/*     */     } 
/* 245 */     return null;
/*     */   }
/*     */   
/*     */   public void enableModule(Class<Module> clazz) {
/* 249 */     Module module = getModuleByClass(clazz);
/* 250 */     if (module != null) {
/* 251 */       module.enable();
/*     */     }
/*     */   }
/*     */   
/*     */   public void disableModule(Class<Module> clazz) {
/* 256 */     Module module = getModuleByClass(clazz);
/* 257 */     if (module != null) {
/* 258 */       module.disable();
/*     */     }
/*     */   }
/*     */   
/*     */   public void enableModule(String name) {
/* 263 */     Module module = getModuleByName(name);
/* 264 */     if (module != null) {
/* 265 */       module.enable();
/*     */     }
/*     */   }
/*     */   
/*     */   public void disableModule(String name) {
/* 270 */     Module module = getModuleByName(name);
/* 271 */     if (module != null) {
/* 272 */       module.disable();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isModuleEnabled(String name) {
/* 277 */     Module module = getModuleByName(name);
/* 278 */     return (module != null && module.isOn());
/*     */   }
/*     */   
/*     */   public boolean isModuleEnabled(Class<Module> clazz) {
/* 282 */     Module module = getModuleByClass(clazz);
/* 283 */     return (module != null && module.isOn());
/*     */   }
/*     */   
/*     */   public Module getModuleByDisplayName(String displayName) {
/* 287 */     for (Module module : this.modules) {
/* 288 */       if (module.getDisplayName().equalsIgnoreCase(displayName)) {
/* 289 */         return module;
/*     */       }
/*     */     } 
/* 292 */     return null;
/*     */   }
/*     */   
/*     */   public ArrayList<Module> getEnabledModules() {
/* 296 */     ArrayList<Module> enabledModules = new ArrayList<>();
/* 297 */     for (Module module : this.modules) {
/* 298 */       if (module.isEnabled()) {
/* 299 */         enabledModules.add(module);
/*     */       }
/*     */     } 
/* 302 */     return enabledModules;
/*     */   }
/*     */   
/*     */   public ArrayList<Module> getModulesByCategory(Module.Category category) {
/* 306 */     ArrayList<Module> modulesCategory = new ArrayList<>();
/* 307 */     this.modules.forEach(module -> {
/*     */           if (module.getCategory() == category) {
/*     */             modulesCategory.add(module);
/*     */           }
/*     */         });
/* 312 */     return modulesCategory;
/*     */   }
/*     */   
/*     */   public List<Module.Category> getCategories() {
/* 316 */     return Arrays.asList(Module.Category.values());
/*     */   }
/*     */   
/*     */   public void onLoad() {
/* 320 */     this.modules.stream().filter(Module::listening).forEach(MinecraftForge.EVENT_BUS::register);
/* 321 */     this.modules.forEach(Module::onLoad);
/*     */   }
/*     */   
/*     */   public void onUpdate() {
/* 325 */     this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
/*     */   }
/*     */   
/*     */   public void onTick() {
/* 329 */     this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
/*     */   }
/*     */   
/*     */   public void onRender2D(Render2DEvent event) {
/* 333 */     this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
/*     */   }
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/* 337 */     this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
/*     */   }
/*     */   
/*     */   public void sortModules(boolean reverse) {
/* 341 */     this
/*     */       
/* 343 */       .sortedModules = (List<Module>)getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> Integer.valueOf(this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1)))).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public void onLogout() {
/* 347 */     this.modules.forEach(Module::onLogout);
/*     */   }
/*     */   
/*     */   public void onLogin() {
/* 351 */     this.modules.forEach(Module::onLogin);
/*     */   }
/*     */   
/*     */   public void onUnload() {
/* 355 */     this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
/* 356 */     this.modules.forEach(Module::onUnload);
/*     */   }
/*     */   
/*     */   public void onUnloadPost() {
/* 360 */     for (Module module : this.modules) {
/* 361 */       module.enabled.setValue(Boolean.valueOf(false));
/*     */     }
/*     */   }
/*     */   
/*     */   public void onKeyPressed(int eventKey) {
/* 366 */     if (eventKey == 0 || !Keyboard.getEventKeyState() || mc.field_71462_r instanceof me.earth.phobos.features.gui.PhobosGui) {
/*     */       return;
/*     */     }
/* 369 */     this.modules.forEach(module -> {
/*     */           if (module.getBind().getKey() == eventKey)
/*     */             module.toggle(); 
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\ModuleManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */