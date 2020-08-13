/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ import net.minecraft.network.play.server.SPacketSoundEffect;
/*    */ import net.minecraft.util.SoundCategory;
/*    */ import net.minecraft.util.SoundEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ 
/*    */ public class NoSoundLag
/*    */   extends Module
/*    */ {
/* 19 */   private final Setting<Boolean> crystals = register(new Setting("Crystals", Boolean.valueOf(true)));
/* 20 */   private final Setting<Boolean> armor = register(new Setting("Armor", Boolean.valueOf(true)));
/*    */   
/* 22 */   private static final Set<SoundEvent> BLACKLIST = Sets.newHashSet((Object[])new SoundEvent[] { SoundEvents.field_187719_p, SoundEvents.field_191258_p, SoundEvents.field_187716_o, SoundEvents.field_187725_r, SoundEvents.field_187722_q, SoundEvents.field_187713_n, SoundEvents.field_187728_s });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NoSoundLag() {
/* 33 */     super("NoSoundLag", "Prevents Lag through sound spam.", Module.Category.MISC, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketReceived(PacketEvent.Receive event) {
/* 38 */     if (event.getPacket() != null && mc.field_71439_g != null && mc.field_71441_e != null && 
/* 39 */       event.getPacket() instanceof SPacketSoundEffect) {
/* 40 */       SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
/* 41 */       if (((Boolean)this.crystals.getValue()).booleanValue() && packet.func_186977_b() == SoundCategory.BLOCKS && packet.func_186978_a() == SoundEvents.field_187539_bB) {
/* 42 */         for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 43 */           if (entity instanceof net.minecraft.entity.item.EntityEnderCrystal && entity.func_70011_f(packet.func_149207_d(), packet.func_149211_e(), packet.func_149210_f()) <= 6.0D) {
/* 44 */             entity.func_70106_y();
/*    */           }
/*    */         } 
/*    */       }
/*    */       
/* 49 */       if (BLACKLIST.contains(packet.func_186978_a()) && ((Boolean)this.armor.getValue()).booleanValue())
/* 50 */         event.setCanceled(true); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\NoSoundLag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */