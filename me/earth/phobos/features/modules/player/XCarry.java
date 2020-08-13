/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.ReflectionUtil;
/*     */ import me.earth.phobos.util.Util;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.inventory.GuiInventory;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketCloseWindow;
/*     */ import net.minecraftforge.client.event.GuiOpenEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class XCarry
/*     */   extends Module
/*     */ {
/*  35 */   private final Setting<Boolean> simpleMode = register(new Setting("Simple", Boolean.valueOf(false)));
/*  36 */   private final Setting<Bind> autoStore = register(new Setting("AutoDuel", new Bind(-1)));
/*  37 */   private final Setting<Integer> obbySlot = register(new Setting("ObbySlot", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(9), v -> (((Bind)this.autoStore.getValue()).getKey() != -1)));
/*  38 */   private final Setting<Integer> slot1 = register(new Setting("Slot1", Integer.valueOf(22), Integer.valueOf(9), Integer.valueOf(44), v -> (((Bind)this.autoStore.getValue()).getKey() != -1)));
/*  39 */   private final Setting<Integer> slot2 = register(new Setting("Slot2", Integer.valueOf(23), Integer.valueOf(9), Integer.valueOf(44), v -> (((Bind)this.autoStore.getValue()).getKey() != -1)));
/*  40 */   private final Setting<Integer> slot3 = register(new Setting("Slot3", Integer.valueOf(24), Integer.valueOf(9), Integer.valueOf(44), v -> (((Bind)this.autoStore.getValue()).getKey() != -1)));
/*  41 */   private final Setting<Integer> tasks = register(new Setting("Actions", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(12), v -> (((Bind)this.autoStore.getValue()).getKey() != -1)));
/*  42 */   private final Setting<Boolean> store = register(new Setting("Store", Boolean.valueOf(false)));
/*  43 */   private final Setting<Boolean> shiftClicker = register(new Setting("ShiftClick", Boolean.valueOf(false)));
/*  44 */   private final Setting<Boolean> withShift = register(new Setting("WithShift", Boolean.valueOf(true), v -> ((Boolean)this.shiftClicker.getValue()).booleanValue()));
/*  45 */   private final Setting<Bind> keyBind = register(new Setting("ShiftBind", new Bind(-1), v -> ((Boolean)this.shiftClicker.getValue()).booleanValue()));
/*     */   
/*  47 */   private static XCarry INSTANCE = new XCarry();
/*  48 */   private GuiInventory openedGui = null;
/*  49 */   private final AtomicBoolean guiNeedsClose = new AtomicBoolean(false);
/*     */   private boolean guiCloseGuard = false;
/*     */   private boolean autoDuelOn = false;
/*  52 */   private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<>();
/*     */   private boolean obbySlotDone = false;
/*     */   private boolean slot1done = false;
/*     */   private boolean slot2done = false;
/*     */   private boolean slot3done = false;
/*  57 */   private List<Integer> doneSlots = new ArrayList<>();
/*     */   
/*     */   public XCarry() {
/*  60 */     super("XCarry", "Uses the crafting inventory for storage", Module.Category.PLAYER, true, false, false);
/*  61 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  65 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static XCarry getInstance() {
/*  69 */     if (INSTANCE == null) {
/*  70 */       INSTANCE = new XCarry();
/*     */     }
/*  72 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  79 */     if (((Boolean)this.shiftClicker.getValue()).booleanValue() && mc.field_71462_r instanceof GuiInventory) {
/*  80 */       boolean ourBind = (((Bind)this.keyBind.getValue()).getKey() != -1 && Keyboard.isKeyDown(((Bind)this.keyBind.getValue()).getKey()) && !Keyboard.isKeyDown(42));
/*  81 */       if (((Keyboard.isKeyDown(42) && ((Boolean)this.withShift.getValue()).booleanValue()) || ourBind) && Mouse.isButtonDown(0)) {
/*  82 */         Slot slot = ((GuiInventory)mc.field_71462_r).getSlotUnderMouse();
/*  83 */         if (slot != null && InventoryUtil.getEmptyXCarry() != -1) {
/*  84 */           int slotNumber = slot.field_75222_d;
/*  85 */           if (slotNumber > 4 && ourBind) {
/*  86 */             this.taskList.add(new InventoryUtil.Task(slotNumber));
/*  87 */             this.taskList.add(new InventoryUtil.Task(InventoryUtil.getEmptyXCarry()));
/*  88 */           } else if (slotNumber > 4 && ((Boolean)this.withShift.getValue()).booleanValue()) {
/*  89 */             boolean isHotBarFull = true;
/*  90 */             boolean isInvFull = true;
/*  91 */             for (Iterator<Integer> iterator = InventoryUtil.findEmptySlots(false).iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/*  92 */               if (i > 4 && i < 36) {
/*  93 */                 isInvFull = false; continue;
/*  94 */               }  if (i > 35 && i < 45) {
/*  95 */                 isHotBarFull = false;
/*     */               } }
/*     */             
/*  98 */             if (slotNumber > 35 && slotNumber < 45) {
/*  99 */               if (isInvFull) {
/* 100 */                 this.taskList.add(new InventoryUtil.Task(slotNumber));
/* 101 */                 this.taskList.add(new InventoryUtil.Task(InventoryUtil.getEmptyXCarry()));
/*     */               } 
/* 103 */             } else if (isHotBarFull) {
/* 104 */               this.taskList.add(new InventoryUtil.Task(slotNumber));
/* 105 */               this.taskList.add(new InventoryUtil.Task(InventoryUtil.getEmptyXCarry()));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     if (this.autoDuelOn) {
/* 113 */       this.doneSlots = new ArrayList<>();
/* 114 */       if (InventoryUtil.getEmptyXCarry() == -1 || (this.obbySlotDone && this.slot1done && this.slot2done && this.slot3done)) {
/* 115 */         this.autoDuelOn = false;
/*     */       }
/*     */       
/* 118 */       if (this.autoDuelOn) {
/* 119 */         if (!this.obbySlotDone && !(mc.field_71439_g.field_71071_by.func_70301_a(((Integer)this.obbySlot.getValue()).intValue() - 1)).field_190928_g) {
/* 120 */           addTasks(36 + ((Integer)this.obbySlot.getValue()).intValue() - 1);
/*     */         }
/* 122 */         this.obbySlotDone = true;
/*     */         
/* 124 */         if (!this.slot1done && !(((Slot)mc.field_71439_g.field_71069_bz.field_75151_b.get(((Integer)this.slot1.getValue()).intValue())).func_75211_c()).field_190928_g) {
/* 125 */           addTasks(((Integer)this.slot1.getValue()).intValue());
/*     */         }
/* 127 */         this.slot1done = true;
/*     */         
/* 129 */         if (!this.slot2done && !(((Slot)mc.field_71439_g.field_71069_bz.field_75151_b.get(((Integer)this.slot2.getValue()).intValue())).func_75211_c()).field_190928_g) {
/* 130 */           addTasks(((Integer)this.slot2.getValue()).intValue());
/*     */         }
/* 132 */         this.slot2done = true;
/*     */         
/* 134 */         if (!this.slot3done && !(((Slot)mc.field_71439_g.field_71069_bz.field_75151_b.get(((Integer)this.slot3.getValue()).intValue())).func_75211_c()).field_190928_g) {
/* 135 */           addTasks(((Integer)this.slot3.getValue()).intValue());
/*     */         }
/* 137 */         this.slot3done = true;
/*     */       } 
/*     */     } else {
/* 140 */       this.obbySlotDone = false;
/* 141 */       this.slot1done = false;
/* 142 */       this.slot2done = false;
/* 143 */       this.slot3done = false;
/*     */     } 
/*     */     
/* 146 */     if (!this.taskList.isEmpty()) {
/* 147 */       for (int i = 0; i < ((Integer)this.tasks.getValue()).intValue(); i++) {
/* 148 */         InventoryUtil.Task task = this.taskList.poll();
/* 149 */         if (task != null) {
/* 150 */           task.run();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void addTasks(int slot) {
/* 157 */     if (InventoryUtil.getEmptyXCarry() != -1) {
/* 158 */       int xcarrySlot = InventoryUtil.getEmptyXCarry();
/* 159 */       if (this.doneSlots.contains(Integer.valueOf(xcarrySlot)) || !InventoryUtil.isSlotEmpty(xcarrySlot)) {
/* 160 */         xcarrySlot++;
/* 161 */         if (this.doneSlots.contains(Integer.valueOf(xcarrySlot)) || !InventoryUtil.isSlotEmpty(xcarrySlot)) {
/* 162 */           xcarrySlot++;
/* 163 */           if (this.doneSlots.contains(Integer.valueOf(xcarrySlot)) || !InventoryUtil.isSlotEmpty(xcarrySlot)) {
/* 164 */             xcarrySlot++;
/* 165 */             if (this.doneSlots.contains(Integer.valueOf(xcarrySlot)) || !InventoryUtil.isSlotEmpty(xcarrySlot)) {
/*     */               return;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 172 */       if (xcarrySlot <= 4) {
/* 173 */         this.doneSlots.add(Integer.valueOf(xcarrySlot));
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */       
/* 178 */       this.taskList.add(new InventoryUtil.Task(slot));
/* 179 */       this.taskList.add(new InventoryUtil.Task(xcarrySlot));
/* 180 */       this.taskList.add(new InventoryUtil.Task());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 186 */     if (!fullNullCheck()) {
/* 187 */       if (!((Boolean)this.simpleMode.getValue()).booleanValue()) {
/* 188 */         closeGui();
/* 189 */         close();
/*     */       } else {
/* 191 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketCloseWindow(mc.field_71439_g.field_71069_bz.field_75152_c));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/* 198 */     onDisable();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onCloseGuiScreen(PacketEvent.Send event) {
/* 203 */     if (((Boolean)this.simpleMode.getValue()).booleanValue() && event.getPacket() instanceof CPacketCloseWindow) {
/* 204 */       CPacketCloseWindow packet = (CPacketCloseWindow)event.getPacket();
/* 205 */       if (packet.field_149556_a == mc.field_71439_g.field_71069_bz.field_75152_c) {
/* 206 */         event.setCanceled(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.LOWEST)
/*     */   public void onGuiOpen(GuiOpenEvent event) {
/* 213 */     if (!((Boolean)this.simpleMode.getValue()).booleanValue()) {
/* 214 */       if (this.guiCloseGuard) {
/* 215 */         event.setCanceled(true);
/* 216 */       } else if (event.getGui() instanceof GuiInventory) {
/* 217 */         event.setGui((GuiScreen)(this.openedGui = createGuiWrapper((GuiInventory)event.getGui())));
/* 218 */         this.guiNeedsClose.set(false);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSettingChange(ClientEvent event) {
/* 225 */     if (event.getStage() == 2 && 
/* 226 */       event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this)) {
/* 227 */       Setting setting = event.getSetting();
/* 228 */       String settingname = event.getSetting().getName();
/* 229 */       if (setting.equals(this.simpleMode)) {
/* 230 */         disable();
/* 231 */       } else if (settingname.equalsIgnoreCase("Store")) {
/* 232 */         event.setCanceled(true);
/* 233 */         this.autoDuelOn = !this.autoDuelOn;
/* 234 */         Command.sendMessage("<XCarry> §aAutostoring...");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 242 */     if (Keyboard.getEventKeyState() && !(mc.field_71462_r instanceof me.earth.phobos.features.gui.PhobosGui) && ((Bind)this.autoStore.getValue()).getKey() == Keyboard.getEventKey()) {
/* 243 */       this.autoDuelOn = !this.autoDuelOn;
/* 244 */       Command.sendMessage("<XCarry> §aAutostoring...");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void close() {
/* 249 */     this.openedGui = null;
/* 250 */     this.guiNeedsClose.set(false);
/* 251 */     this.guiCloseGuard = false;
/*     */   }
/*     */   
/*     */   private void closeGui() {
/* 255 */     if (this.guiNeedsClose.compareAndSet(true, false) && 
/* 256 */       !fullNullCheck()) {
/* 257 */       this.guiCloseGuard = true;
/* 258 */       mc.field_71439_g.func_71053_j();
/* 259 */       if (this.openedGui != null) {
/* 260 */         this.openedGui.func_146281_b();
/* 261 */         this.openedGui = null;
/*     */       } 
/* 263 */       this.guiCloseGuard = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private GuiInventory createGuiWrapper(GuiInventory gui) {
/*     */     try {
/* 270 */       GuiInventoryWrapper wrapper = new GuiInventoryWrapper();
/* 271 */       ReflectionUtil.copyOf(gui, wrapper);
/* 272 */       return wrapper;
/* 273 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/* 274 */       e.printStackTrace();
/* 275 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private class GuiInventoryWrapper
/*     */     extends GuiInventory {
/*     */     GuiInventoryWrapper() {
/* 282 */       super((EntityPlayer)Util.mc.field_71439_g);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void func_73869_a(char typedChar, int keyCode) throws IOException {
/* 287 */       if (XCarry.this.isEnabled() && (keyCode == 1 || this.field_146297_k.field_71474_y.field_151445_Q.isActiveAndMatches(keyCode))) {
/* 288 */         XCarry.this.guiNeedsClose.set(true);
/* 289 */         this.field_146297_k.func_147108_a(null);
/*     */       } else {
/* 291 */         super.func_73869_a(typedChar, keyCode);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void func_146281_b() {
/* 297 */       if (XCarry.this.guiCloseGuard || !XCarry.this.isEnabled())
/* 298 */         super.func_146281_b(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\XCarry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */