/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import io.netty.buffer.Unpooled;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.IntStream;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.nbt.NBTBase;
/*    */ import net.minecraft.nbt.NBTTagList;
/*    */ import net.minecraft.nbt.NBTTagString;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.client.CPacketCustomPayload;
/*    */ 
/*    */ public class BookCommand extends Command {
/*    */   public BookCommand() {
/* 20 */     super("book", new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 25 */     ItemStack heldItem = mc.field_71439_g.func_184614_ca();
/* 26 */     if (heldItem.func_77973_b() == Items.field_151099_bA) {
/* 27 */       int limit = 50;
/* 28 */       Random rand = new Random();
/* 29 */       IntStream characterGenerator = rand.ints(128, 1112063).map(i -> (i < 55296) ? i : (i + 2048));
/* 30 */       String joinedPages = characterGenerator.limit(10500L).<CharSequence>mapToObj(i -> String.valueOf((char)i)).collect(Collectors.joining());
/* 31 */       NBTTagList pages = new NBTTagList();
/* 32 */       for (int page = 0; page < 50; page++) {
/* 33 */         pages.func_74742_a((NBTBase)new NBTTagString(joinedPages.substring(page * 210, (page + 1) * 210)));
/*    */       }
/* 35 */       if (heldItem.func_77942_o()) {
/* 36 */         heldItem.func_77978_p().func_74782_a("pages", (NBTBase)pages);
/*    */       } else {
/* 38 */         heldItem.func_77983_a("pages", (NBTBase)pages);
/*    */       } 
/* 40 */       StringBuilder stackName = new StringBuilder();
/* 41 */       for (int i = 0; i < 16; i++) {
/* 42 */         stackName.append("\024\f");
/*    */       }
/* 44 */       heldItem.func_77983_a("author", (NBTBase)new NBTTagString(mc.field_71439_g.func_70005_c_()));
/* 45 */       heldItem.func_77983_a("title", (NBTBase)new NBTTagString(stackName
/*    */             
/* 47 */             .toString()));
/*    */       
/* 49 */       PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
/* 50 */       buf.func_150788_a(heldItem);
/* 51 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketCustomPayload("MC|BSign", buf));
/* 52 */       sendMessage(Phobos.commandManager.getPrefix() + "Book Hack Success!");
/*    */     } else {
/* 54 */       sendMessage(Phobos.commandManager.getPrefix() + "b1g 3rr0r!");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\command\commands\BookCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */