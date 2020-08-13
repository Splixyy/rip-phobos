/*     */ package me.earth.phobos.manager;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.ConnectionEvent;
/*     */ import me.earth.phobos.event.events.DeathEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.Render2DEvent;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.event.events.TotemPopEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.client.Managers;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.play.server.SPacketEntityStatus;
/*     */ import net.minecraft.network.play.server.SPacketPlayerListItem;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.client.event.ClientChatEvent;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.event.entity.living.LivingEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import net.minecraftforge.fml.common.network.FMLNetworkEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ 
/*     */ public class EventManager
/*     */   extends Feature
/*     */ {
/*  41 */   private final Timer timer = new Timer();
/*  42 */   private final Timer logoutTimer = new Timer();
/*     */   
/*     */   public void init() {
/*  45 */     MinecraftForge.EVENT_BUS.register(this);
/*     */   }
/*     */   public void onUnload() {
/*  48 */     MinecraftForge.EVENT_BUS.unregister(this);
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void onUpdate(LivingEvent.LivingUpdateEvent event) {
/*  52 */     if (!fullNullCheck() && (event.getEntity().func_130014_f_()).field_72995_K && event.getEntityLiving().equals(mc.field_71439_g)) {
/*  53 */       Phobos.potionManager.update();
/*  54 */       Phobos.totemPopManager.onUpdate();
/*  55 */       Phobos.inventoryManager.update();
/*  56 */       Phobos.holeManager.update();
/*  57 */       Phobos.moduleManager.onUpdate();
/*  58 */       Phobos.timerManager.update();
/*  59 */       if (this.timer.passedMs(((Integer)(Managers.getInstance()).moduleListUpdates.getValue()).intValue())) {
/*  60 */         Phobos.moduleManager.sortModules(true);
/*  61 */         this.timer.reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
/*  68 */     this.logoutTimer.reset();
/*  69 */     Phobos.moduleManager.onLogin();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
/*  74 */     Phobos.moduleManager.onLogout();
/*  75 */     Phobos.totemPopManager.onLogout();
/*  76 */     Phobos.potionManager.onLogout();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTick(TickEvent.ClientTickEvent event) {
/*  81 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*     */     
/*  85 */     Phobos.moduleManager.onTick();
/*     */     
/*  87 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*     */       
/*  89 */       if (player == null || player.func_110143_aJ() > 0.0F) {
/*     */         continue;
/*     */       }
/*     */       
/*  93 */       MinecraftForge.EVENT_BUS.post((Event)new DeathEvent(player));
/*  94 */       Phobos.totemPopManager.onDeath(player);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 100 */     if (fullNullCheck())
/*     */       return; 
/* 102 */     if (event.getStage() == 0) {
/* 103 */       Phobos.speedManager.updateValues();
/* 104 */       Phobos.rotationManager.updateRotations();
/* 105 */       Phobos.positionManager.updatePosition();
/*     */     } 
/*     */     
/* 108 */     if (event.getStage() == 1) {
/* 109 */       Phobos.rotationManager.restoreRotations();
/* 110 */       Phobos.positionManager.restorePosition();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 116 */     if (event.getStage() != 0) {
/*     */       return;
/*     */     }
/*     */     
/* 120 */     Phobos.serverManager.onPacketReceived();
/*     */     
/* 122 */     if (event.getPacket() instanceof SPacketEntityStatus) {
/* 123 */       SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
/* 124 */       if (packet.func_149160_c() == 35 && 
/* 125 */         packet.func_149161_a((World)mc.field_71441_e) instanceof EntityPlayer) {
/* 126 */         EntityPlayer player = (EntityPlayer)packet.func_149161_a((World)mc.field_71441_e);
/* 127 */         MinecraftForge.EVENT_BUS.post((Event)new TotemPopEvent(player));
/* 128 */         Phobos.totemPopManager.onTotemPop(player);
/* 129 */         Phobos.potionManager.onTotemPop(player);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 134 */     if (event.getPacket() instanceof SPacketPlayerListItem && !fullNullCheck() && this.logoutTimer.passedS(1.0D)) {
/* 135 */       SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
/* 136 */       if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals(packet.func_179768_b()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals(packet.func_179768_b())) {
/*     */         return;
/*     */       }
/*     */       
/* 140 */       packet.func_179767_a().stream().filter(Objects::nonNull).filter(data -> (!Strings.isNullOrEmpty(data.func_179962_a().getName()) || data.func_179962_a().getId() != null))
/* 141 */         .forEach(data -> {
/*     */             String name; EntityPlayer entity;
/*     */             UUID id = data.func_179962_a().getId();
/*     */             switch (packet.func_179768_b()) {
/*     */               case ADD_PLAYER:
/*     */                 name = data.func_179962_a().getName();
/*     */                 MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(0, id, name));
/*     */                 break;
/*     */               case REMOVE_PLAYER:
/*     */                 entity = mc.field_71441_e.func_152378_a(id);
/*     */                 if (entity != null) {
/*     */                   String logoutName = entity.func_70005_c_();
/*     */                   MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(1, entity, id, logoutName));
/*     */                   break;
/*     */                 } 
/*     */                 MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(2, id, null));
/*     */                 break;
/*     */             } 
/*     */           });
/*     */     } 
/* 161 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTimeUpdate) {
/* 162 */       Phobos.serverManager.update();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onWorldRender(RenderWorldLastEvent event) {
/* 168 */     if (event.isCanceled()) {
/*     */       return;
/*     */     }
/*     */     
/* 172 */     mc.field_71424_I.func_76320_a("phobos");
/* 173 */     GlStateManager.func_179090_x();
/* 174 */     GlStateManager.func_179147_l();
/* 175 */     GlStateManager.func_179118_c();
/* 176 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 177 */     GlStateManager.func_179103_j(7425);
/* 178 */     GlStateManager.func_179097_i();
/* 179 */     GlStateManager.func_187441_d(1.0F);
/* 180 */     Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
/* 181 */     Phobos.moduleManager.onRender3D(render3dEvent);
/*     */     
/* 183 */     GlStateManager.func_187441_d(1.0F);
/* 184 */     GlStateManager.func_179103_j(7424);
/* 185 */     GlStateManager.func_179084_k();
/* 186 */     GlStateManager.func_179141_d();
/* 187 */     GlStateManager.func_179098_w();
/* 188 */     GlStateManager.func_179126_j();
/* 189 */     GlStateManager.func_179089_o();
/* 190 */     GlStateManager.func_179089_o();
/* 191 */     GlStateManager.func_179132_a(true);
/* 192 */     GlStateManager.func_179098_w();
/* 193 */     GlStateManager.func_179147_l();
/* 194 */     GlStateManager.func_179126_j();
/* 195 */     mc.field_71424_I.func_76319_b();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void renderHUD(RenderGameOverlayEvent.Post event) {
/* 200 */     if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
/* 201 */       Phobos.textManager.updateResolution();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.LOW)
/*     */   public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
/* 207 */     if (event.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) {
/* 208 */       ScaledResolution resolution = new ScaledResolution(mc);
/* 209 */       Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
/* 210 */       Phobos.moduleManager.onRender2D(render2DEvent);
/* 211 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
/*     */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 217 */     if (Keyboard.getEventKeyState()) {
/* 218 */       Phobos.moduleManager.onKeyPressed(Keyboard.getEventKey());
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.HIGHEST)
/*     */   public void onChatSent(ClientChatEvent event) {
/* 224 */     if (event.getMessage().startsWith(Command.getCommandPrefix())) {
/* 225 */       event.setCanceled(true);
/*     */       try {
/* 227 */         mc.field_71456_v.func_146158_b().func_146239_a(event.getMessage());
/* 228 */         if (event.getMessage().length() > 1) {
/* 229 */           Phobos.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
/*     */         } else {
/* 231 */           Command.sendMessage("Please enter a command.");
/*     */         } 
/* 233 */       } catch (Exception e) {
/* 234 */         e.printStackTrace();
/* 235 */         Command.sendMessage("§cAn error occurred while running this command. Check the log!");
/*     */       } 
/* 237 */       event.setMessage("");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\EventManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */