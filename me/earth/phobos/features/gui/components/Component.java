/*     */ package me.earth.phobos.features.gui.components;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.gui.PhobosGui;
/*     */ import me.earth.phobos.features.gui.components.items.Item;
/*     */ import me.earth.phobos.features.gui.components.items.buttons.Button;
/*     */ import me.earth.phobos.features.modules.client.ClickGui;
/*     */ import me.earth.phobos.util.ColorUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.client.audio.ISound;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ 
/*     */ public class Component
/*     */   extends Feature {
/*     */   private int x;
/*     */   private int y;
/*     */   private int x2;
/*     */   private int y2;
/*     */   private int width;
/*     */   private int height;
/*     */   private boolean open;
/*     */   public boolean drag;
/*  26 */   private final ArrayList<Item> items = new ArrayList<>();
/*     */   private boolean hidden = false;
/*     */   
/*     */   public Component(String name, int x, int y, boolean open) {
/*  30 */     super(name);
/*  31 */     this.x = x;
/*  32 */     this.y = y;
/*  33 */     this.width = 88;
/*  34 */     this.height = 18;
/*  35 */     this.open = open;
/*  36 */     setupItems();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupItems() {}
/*     */ 
/*     */   
/*     */   private void drag(int mouseX, int mouseY) {
/*  44 */     if (!this.drag) {
/*     */       return;
/*     */     }
/*  47 */     this.x = this.x2 + mouseX;
/*  48 */     this.y = this.y2 + mouseY;
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  52 */     drag(mouseX, mouseY);
/*  53 */     float totalItemHeight = this.open ? (getTotalItemHeight() - 2.0F) : 0.0F;
/*  54 */     int color = -7829368;
/*  55 */     if (((Boolean)(ClickGui.getInstance()).devSettings.getValue()).booleanValue()) {
/*  56 */       color = ColorUtil.toARGB(((Integer)(ClickGui.getInstance()).topRed.getValue()).intValue(), ((Integer)(ClickGui.getInstance()).topGreen.getValue()).intValue(), ((Integer)(ClickGui.getInstance()).topBlue.getValue()).intValue(), ((Integer)(ClickGui.getInstance()).topAlpha.getValue()).intValue());
/*     */     }
/*  58 */     RenderUtil.drawRect(this.x, this.y - 1.5F, (this.x + this.width), (this.y + this.height - 6), color);
/*  59 */     if (this.open) {
/*  60 */       RenderUtil.drawRect(this.x, this.y + 12.5F, (this.x + this.width), (this.y + this.height) + totalItemHeight, 1996488704);
/*     */     }
/*  62 */     Phobos.textManager.drawStringWithShadow(getName(), this.x + 3.0F, this.y - 4.0F - PhobosGui.getClickGui().getTextOffset(), -1);
/*  63 */     if (this.open) {
/*  64 */       float y = (getY() + getHeight()) - 3.0F;
/*  65 */       for (Item item : getItems()) {
/*  66 */         if (!item.isHidden()) {
/*  67 */           item.setLocation(this.x + 2.0F, y);
/*  68 */           item.setWidth(getWidth() - 4);
/*  69 */           item.drawScreen(mouseX, mouseY, partialTicks);
/*  70 */           y += item.getHeight() + 1.5F;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/*  77 */     if (mouseButton == 0 && isHovering(mouseX, mouseY)) {
/*  78 */       this.x2 = this.x - mouseX;
/*  79 */       this.y2 = this.y - mouseY;
/*  80 */       PhobosGui.getClickGui().getComponents().forEach(component -> {
/*     */             if (component.drag) {
/*     */               component.drag = false;
/*     */             }
/*     */           });
/*  85 */       this.drag = true;
/*     */       return;
/*     */     } 
/*  88 */     if (mouseButton == 1 && isHovering(mouseX, mouseY)) {
/*  89 */       this.open = !this.open;
/*  90 */       mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
/*     */       return;
/*     */     } 
/*  93 */     if (!this.open) {
/*     */       return;
/*     */     }
/*  96 */     getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
/*     */   }
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
/* 100 */     if (releaseButton == 0)
/* 101 */       this.drag = false; 
/* 102 */     if (!this.open) {
/*     */       return;
/*     */     }
/* 105 */     getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
/*     */   }
/*     */   
/*     */   public void onKeyTyped(char typedChar, int keyCode) {
/* 109 */     if (!this.open) {
/*     */       return;
/*     */     }
/* 112 */     getItems().forEach(item -> item.onKeyTyped(typedChar, keyCode));
/*     */   }
/*     */   
/*     */   public void addButton(Button button) {
/* 116 */     this.items.add(button);
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/* 120 */     this.x = x;
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/* 124 */     this.y = y;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 128 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY() {
/* 132 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 136 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 140 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/* 144 */     this.height = height;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/* 148 */     this.width = width;
/*     */   }
/*     */   
/*     */   public void setHidden(boolean hidden) {
/* 152 */     this.hidden = hidden;
/*     */   }
/*     */   
/*     */   public boolean isHidden() {
/* 156 */     return this.hidden;
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 160 */     return this.open;
/*     */   }
/*     */   
/*     */   public final ArrayList<Item> getItems() {
/* 164 */     return this.items;
/*     */   }
/*     */   
/*     */   private boolean isHovering(int mouseX, int mouseY) {
/* 168 */     return (mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight() - (this.open ? 2 : 0));
/*     */   }
/*     */   
/*     */   private float getTotalItemHeight() {
/* 172 */     float height = 0.0F;
/* 173 */     for (Item item : getItems()) {
/* 174 */       height += item.getHeight() + 1.5F;
/*     */     }
/* 176 */     return height;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\gui\components\Component.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */