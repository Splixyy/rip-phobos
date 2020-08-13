/*     */ package me.earth.phobos.features.modules.misc;
/*     */ 
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AntiPackets
/*     */   extends Module
/*     */ {
/* 119 */   private Setting<Mode> mode = register(new Setting("Packets", Mode.CLIENT));
/* 120 */   private Setting<Integer> page = register(new Setting("SPackets", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(10), v -> (this.mode.getValue() == Mode.SERVER)));
/* 121 */   private Setting<Integer> pages = register(new Setting("CPackets", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(4), v -> (this.mode.getValue() == Mode.CLIENT)));
/*     */   
/* 123 */   private Setting<Boolean> AdvancementInfo = register(new Setting("AdvancementInfo", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/* 124 */   private Setting<Boolean> Animation = register(new Setting("Animation", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/* 125 */   private Setting<Boolean> BlockAction = register(new Setting("BlockAction", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/* 126 */   private Setting<Boolean> BlockBreakAnim = register(new Setting("BlockBreakAnim", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/* 127 */   private Setting<Boolean> BlockChange = register(new Setting("BlockChange", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/* 128 */   private Setting<Boolean> Camera = register(new Setting("Camera", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/* 129 */   private Setting<Boolean> ChangeGameState = register(new Setting("ChangeGameState", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/* 130 */   private Setting<Boolean> Chat = register(new Setting("Chat", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/*     */   
/* 132 */   private Setting<Boolean> ChunkData = register(new Setting("ChunkData", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/* 133 */   private Setting<Boolean> CloseWindow = register(new Setting("CloseWindow", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/* 134 */   private Setting<Boolean> CollectItem = register(new Setting("CollectItem", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/* 135 */   private Setting<Boolean> CombatEvent = register(new Setting("Combatevent", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/* 136 */   private Setting<Boolean> ConfirmTransaction = register(new Setting("ConfirmTransaction", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/* 137 */   private Setting<Boolean> Cooldown = register(new Setting("Cooldown", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/* 138 */   private Setting<Boolean> CustomPayload = register(new Setting("CustomPayload", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/* 139 */   private Setting<Boolean> CustomSound = register(new Setting("CustomSound", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/*     */   
/* 141 */   private Setting<Boolean> DestroyEntities = register(new Setting("DestroyEntities", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/* 142 */   private Setting<Boolean> Disconnect = register(new Setting("Disconnect", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/* 143 */   private Setting<Boolean> DisplayObjective = register(new Setting("DisplayObjective", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/* 144 */   private Setting<Boolean> Effect = register(new Setting("Effect", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/* 145 */   private Setting<Boolean> Entity = register(new Setting("Entity", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/* 146 */   private Setting<Boolean> EntityAttach = register(new Setting("EntityAttach", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/* 147 */   private Setting<Boolean> EntityEffect = register(new Setting("EntityEffect", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/* 148 */   private Setting<Boolean> EntityEquipment = register(new Setting("EntityEquipment", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/*     */   
/* 150 */   private Setting<Boolean> EntityHeadLook = register(new Setting("EntityHeadLook", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/* 151 */   private Setting<Boolean> EntityMetadata = register(new Setting("EntityMetadata", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/* 152 */   private Setting<Boolean> EntityProperties = register(new Setting("EntityProperties", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/* 153 */   private Setting<Boolean> EntityStatus = register(new Setting("EntityStatus", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/* 154 */   private Setting<Boolean> EntityTeleport = register(new Setting("EntityTeleport", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/* 155 */   private Setting<Boolean> EntityVelocity = register(new Setting("EntityVelocity", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/* 156 */   private Setting<Boolean> Explosion = register(new Setting("Explosion", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/* 157 */   private Setting<Boolean> HeldItemChange = register(new Setting("HeldItemChange", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/*     */   
/* 159 */   private Setting<Boolean> JoinGame = register(new Setting("JoinGame", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/* 160 */   private Setting<Boolean> KeepAlive = register(new Setting("KeepAlive", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/* 161 */   private Setting<Boolean> Maps = register(new Setting("Maps", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/* 162 */   private Setting<Boolean> MoveVehicle = register(new Setting("MoveVehicle", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/* 163 */   private Setting<Boolean> MultiBlockChange = register(new Setting("MultiBlockChange", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/* 164 */   private Setting<Boolean> OpenWindow = register(new Setting("OpenWindow", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/* 165 */   private Setting<Boolean> Particles = register(new Setting("Particles", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/* 166 */   private Setting<Boolean> PlaceGhostRecipe = register(new Setting("PlaceGhostRecipe", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/*     */   
/* 168 */   private Setting<Boolean> PlayerAbilities = register(new Setting("PlayerAbilities", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/* 169 */   private Setting<Boolean> PlayerListHeaderFooter = register(new Setting("PlayerListHeaderFooter", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/* 170 */   private Setting<Boolean> PlayerListItem = register(new Setting("PlayerListItem", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/* 171 */   private Setting<Boolean> PlayerPosLook = register(new Setting("PlayerPosLook", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/* 172 */   private Setting<Boolean> RecipeBook = register(new Setting("RecipeBook", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/* 173 */   private Setting<Boolean> RemoveEntityEffect = register(new Setting("RemoveEntityEffect", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/* 174 */   private Setting<Boolean> ResourcePackSend = register(new Setting("ResourcePackSend", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/* 175 */   private Setting<Boolean> Respawn = register(new Setting("Respawn", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/*     */   
/* 177 */   private Setting<Boolean> ScoreboardObjective = register(new Setting("ScoreboardObjective", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/* 178 */   private Setting<Boolean> SelectAdvancementsTab = register(new Setting("SelectAdvancementsTab", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/* 179 */   private Setting<Boolean> ServerDifficulty = register(new Setting("ServerDifficulty", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/* 180 */   private Setting<Boolean> SetExperience = register(new Setting("SetExperience", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/* 181 */   private Setting<Boolean> SetPassengers = register(new Setting("SetPassengers", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/* 182 */   private Setting<Boolean> SetSlot = register(new Setting("SetSlot", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/* 183 */   private Setting<Boolean> SignEditorOpen = register(new Setting("SignEditorOpen", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/* 184 */   private Setting<Boolean> SoundEffect = register(new Setting("SoundEffect", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/*     */   
/* 186 */   private Setting<Boolean> SpawnExperienceOrb = register(new Setting("SpawnExperienceOrb", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/* 187 */   private Setting<Boolean> SpawnGlobalEntity = register(new Setting("SpawnGlobalEntity", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/* 188 */   private Setting<Boolean> SpawnMob = register(new Setting("SpawnMob", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/* 189 */   private Setting<Boolean> SpawnObject = register(new Setting("SpawnObject", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/* 190 */   private Setting<Boolean> SpawnPainting = register(new Setting("SpawnPainting", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/* 191 */   private Setting<Boolean> SpawnPlayer = register(new Setting("SpawnPlayer", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/* 192 */   private Setting<Boolean> SpawnPosition = register(new Setting("SpawnPosition", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/* 193 */   private Setting<Boolean> Statistics = register(new Setting("Statistics", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/*     */   
/* 195 */   private Setting<Boolean> TabComplete = register(new Setting("TabComplete", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/* 196 */   private Setting<Boolean> Teams = register(new Setting("Teams", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/* 197 */   private Setting<Boolean> TimeUpdate = register(new Setting("TimeUpdate", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/* 198 */   private Setting<Boolean> Title = register(new Setting("Title", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/* 199 */   private Setting<Boolean> UnloadChunk = register(new Setting("UnloadChunk", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/* 200 */   private Setting<Boolean> UpdateBossInfo = register(new Setting("UpdateBossInfo", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/* 201 */   private Setting<Boolean> UpdateHealth = register(new Setting("UpdateHealth", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/* 202 */   private Setting<Boolean> UpdateScore = register(new Setting("UpdateScore", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/*     */   
/* 204 */   private Setting<Boolean> UpdateTileEntity = register(new Setting("UpdateTileEntity", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 10)));
/* 205 */   private Setting<Boolean> UseBed = register(new Setting("UseBed", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 10)));
/* 206 */   private Setting<Boolean> WindowItems = register(new Setting("WindowItems", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 10)));
/* 207 */   private Setting<Boolean> WindowProperty = register(new Setting("WindowProperty", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 10)));
/* 208 */   private Setting<Boolean> WorldBorder = register(new Setting("WorldBorder", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 10)));
/*     */   
/* 210 */   private Setting<Boolean> Animations = register(new Setting("Animations", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/* 211 */   private Setting<Boolean> ChatMessage = register(new Setting("ChatMessage", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/* 212 */   private Setting<Boolean> ClickWindow = register(new Setting("ClickWindow", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/* 213 */   private Setting<Boolean> ClientSettings = register(new Setting("ClientSettings", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/* 214 */   private Setting<Boolean> ClientStatus = register(new Setting("ClientStatus", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/* 215 */   private Setting<Boolean> CloseWindows = register(new Setting("CloseWindows", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/* 216 */   private Setting<Boolean> ConfirmTeleport = register(new Setting("ConfirmTeleport", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/* 217 */   private Setting<Boolean> ConfirmTransactions = register(new Setting("ConfirmTransactions", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/*     */   
/* 219 */   private Setting<Boolean> CreativeInventoryAction = register(new Setting("CreativeInventoryAction", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 220 */   private Setting<Boolean> CustomPayloads = register(new Setting("CustomPayloads", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 221 */   private Setting<Boolean> EnchantItem = register(new Setting("EnchantItem", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 222 */   private Setting<Boolean> EntityAction = register(new Setting("EntityAction", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 223 */   private Setting<Boolean> HeldItemChanges = register(new Setting("HeldItemChanges", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 224 */   private Setting<Boolean> Input = register(new Setting("Input", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 225 */   private Setting<Boolean> KeepAlives = register(new Setting("KeepAlives", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 226 */   private Setting<Boolean> PlaceRecipe = register(new Setting("PlaceRecipe", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/*     */   
/* 228 */   private Setting<Boolean> Player = register(new Setting("Player", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/* 229 */   private Setting<Boolean> PlayerAbility = register(new Setting("PlayerAbility", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/* 230 */   private Setting<Boolean> PlayerDigging = register(new Setting("PlayerDigging", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.page.getValue()).intValue() == 3)));
/* 231 */   private Setting<Boolean> PlayerTryUseItem = register(new Setting("PlayerTryUseItem", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/* 232 */   private Setting<Boolean> PlayerTryUseItemOnBlock = register(new Setting("TryUseItemOnBlock", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/* 233 */   private Setting<Boolean> RecipeInfo = register(new Setting("RecipeInfo", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/* 234 */   private Setting<Boolean> ResourcePackStatus = register(new Setting("ResourcePackStatus", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/* 235 */   private Setting<Boolean> SeenAdvancements = register(new Setting("SeenAdvancements", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/*     */   
/* 237 */   private Setting<Boolean> PlayerPackets = register(new Setting("PlayerPackets", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/* 238 */   private Setting<Boolean> Spectate = register(new Setting("Spectate", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/* 239 */   private Setting<Boolean> SteerBoat = register(new Setting("SteerBoat", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/* 240 */   private Setting<Boolean> TabCompletion = register(new Setting("TabCompletion", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/* 241 */   private Setting<Boolean> UpdateSign = register(new Setting("UpdateSign", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/* 242 */   private Setting<Boolean> UseEntity = register(new Setting("UseEntity", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/* 243 */   private Setting<Boolean> VehicleMove = register(new Setting("VehicleMove", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/*     */   
/* 245 */   private int hudAmount = 0;
/*     */   
/*     */   public AntiPackets() {
/* 248 */     super("AntiPackets", "Blocks Packets", Module.Category.MISC, true, false, false);
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 252 */     CLIENT, SERVER;
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 258 */     if (!isEnabled()) {
/*     */       return;
/*     */     }
/*     */     
/* 262 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketAnimation && ((Boolean)this.Animations.getValue()).booleanValue()) {
/* 263 */       event.setCanceled(true);
/*     */     }
/* 265 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketChatMessage && ((Boolean)this.ChatMessage.getValue()).booleanValue()) {
/* 266 */       event.setCanceled(true);
/*     */     }
/* 268 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketClickWindow && ((Boolean)this.ClickWindow.getValue()).booleanValue()) {
/* 269 */       event.setCanceled(true);
/*     */     }
/* 271 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketClientSettings && ((Boolean)this.ClientSettings.getValue()).booleanValue()) {
/* 272 */       event.setCanceled(true);
/*     */     }
/* 274 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketClientStatus && ((Boolean)this.ClientStatus.getValue()).booleanValue()) {
/* 275 */       event.setCanceled(true);
/*     */     }
/* 277 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketCloseWindow && ((Boolean)this.CloseWindows.getValue()).booleanValue()) {
/* 278 */       event.setCanceled(true);
/*     */     }
/* 280 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketConfirmTeleport && ((Boolean)this.ConfirmTeleport.getValue()).booleanValue()) {
/* 281 */       event.setCanceled(true);
/*     */     }
/* 283 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketConfirmTransaction && ((Boolean)this.ConfirmTransactions.getValue()).booleanValue()) {
/* 284 */       event.setCanceled(true);
/*     */     }
/* 286 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketCreativeInventoryAction && ((Boolean)this.CreativeInventoryAction.getValue()).booleanValue()) {
/* 287 */       event.setCanceled(true);
/*     */     }
/* 289 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketCustomPayload && ((Boolean)this.CustomPayloads.getValue()).booleanValue()) {
/* 290 */       event.setCanceled(true);
/*     */     }
/* 292 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketEnchantItem && ((Boolean)this.EnchantItem.getValue()).booleanValue()) {
/* 293 */       event.setCanceled(true);
/*     */     }
/* 295 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketEntityAction && ((Boolean)this.EntityAction.getValue()).booleanValue()) {
/* 296 */       event.setCanceled(true);
/*     */     }
/* 298 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketHeldItemChange && ((Boolean)this.HeldItemChanges.getValue()).booleanValue()) {
/* 299 */       event.setCanceled(true);
/*     */     }
/* 301 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketInput && ((Boolean)this.Input.getValue()).booleanValue()) {
/* 302 */       event.setCanceled(true);
/*     */     }
/* 304 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketKeepAlive && ((Boolean)this.KeepAlives.getValue()).booleanValue()) {
/* 305 */       event.setCanceled(true);
/*     */     }
/* 307 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlaceRecipe && ((Boolean)this.PlaceRecipe.getValue()).booleanValue()) {
/* 308 */       event.setCanceled(true);
/*     */     }
/* 310 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer && ((Boolean)this.Player.getValue()).booleanValue()) {
/* 311 */       event.setCanceled(true);
/*     */     }
/* 313 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayerAbilities && ((Boolean)this.PlayerAbility.getValue()).booleanValue()) {
/* 314 */       event.setCanceled(true);
/*     */     }
/* 316 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayerDigging && ((Boolean)this.PlayerDigging.getValue()).booleanValue()) {
/* 317 */       event.setCanceled(true);
/*     */     }
/* 319 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayerTryUseItem && ((Boolean)this.PlayerTryUseItem.getValue()).booleanValue()) {
/* 320 */       event.setCanceled(true);
/*     */     }
/* 322 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock && ((Boolean)this.PlayerTryUseItemOnBlock.getValue()).booleanValue()) {
/* 323 */       event.setCanceled(true);
/*     */     }
/* 325 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketRecipeInfo && ((Boolean)this.RecipeInfo.getValue()).booleanValue()) {
/* 326 */       event.setCanceled(true);
/*     */     }
/* 328 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketResourcePackStatus && ((Boolean)this.ResourcePackStatus.getValue()).booleanValue()) {
/* 329 */       event.setCanceled(true);
/*     */     }
/* 331 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketSeenAdvancements && ((Boolean)this.SeenAdvancements.getValue()).booleanValue()) {
/* 332 */       event.setCanceled(true);
/*     */     }
/* 334 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketSpectate && ((Boolean)this.Spectate.getValue()).booleanValue()) {
/* 335 */       event.setCanceled(true);
/*     */     }
/* 337 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketSteerBoat && ((Boolean)this.SteerBoat.getValue()).booleanValue()) {
/* 338 */       event.setCanceled(true);
/*     */     }
/* 340 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketTabComplete && ((Boolean)this.TabCompletion.getValue()).booleanValue()) {
/* 341 */       event.setCanceled(true);
/*     */     }
/* 343 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketUpdateSign && ((Boolean)this.UpdateSign.getValue()).booleanValue()) {
/* 344 */       event.setCanceled(true);
/*     */     }
/* 346 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketUseEntity && ((Boolean)this.UseEntity.getValue()).booleanValue()) {
/* 347 */       event.setCanceled(true);
/*     */     }
/* 349 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketVehicleMove && ((Boolean)this.VehicleMove.getValue()).booleanValue()) {
/* 350 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 357 */     if (!isEnabled()) {
/*     */       return;
/*     */     }
/*     */     
/* 361 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketAdvancementInfo && ((Boolean)this.AdvancementInfo.getValue()).booleanValue()) {
/* 362 */       event.setCanceled(true);
/*     */     }
/* 364 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketAnimation && ((Boolean)this.Animation.getValue()).booleanValue()) {
/* 365 */       event.setCanceled(true);
/*     */     }
/* 367 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketBlockAction && ((Boolean)this.BlockAction.getValue()).booleanValue()) {
/* 368 */       event.setCanceled(true);
/*     */     }
/* 370 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketBlockBreakAnim && ((Boolean)this.BlockBreakAnim.getValue()).booleanValue()) {
/* 371 */       event.setCanceled(true);
/*     */     }
/* 373 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketBlockChange && ((Boolean)this.BlockChange.getValue()).booleanValue()) {
/* 374 */       event.setCanceled(true);
/*     */     }
/* 376 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCamera && ((Boolean)this.Camera.getValue()).booleanValue()) {
/* 377 */       event.setCanceled(true);
/*     */     }
/* 379 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketChangeGameState && ((Boolean)this.ChangeGameState.getValue()).booleanValue()) {
/* 380 */       event.setCanceled(true);
/*     */     }
/* 382 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketChat && ((Boolean)this.Chat.getValue()).booleanValue()) {
/* 383 */       event.setCanceled(true);
/*     */     }
/* 385 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketChunkData && ((Boolean)this.ChunkData.getValue()).booleanValue()) {
/* 386 */       event.setCanceled(true);
/*     */     }
/* 388 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCloseWindow && ((Boolean)this.CloseWindow.getValue()).booleanValue()) {
/* 389 */       event.setCanceled(true);
/*     */     }
/* 391 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCollectItem && ((Boolean)this.CollectItem.getValue()).booleanValue()) {
/* 392 */       event.setCanceled(true);
/*     */     }
/* 394 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCombatEvent && ((Boolean)this.CombatEvent.getValue()).booleanValue()) {
/* 395 */       event.setCanceled(true);
/*     */     }
/* 397 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketConfirmTransaction && ((Boolean)this.ConfirmTransaction.getValue()).booleanValue()) {
/* 398 */       event.setCanceled(true);
/*     */     }
/* 400 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCooldown && ((Boolean)this.Cooldown.getValue()).booleanValue()) {
/* 401 */       event.setCanceled(true);
/*     */     }
/* 403 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCustomPayload && ((Boolean)this.CustomPayload.getValue()).booleanValue()) {
/* 404 */       event.setCanceled(true);
/*     */     }
/* 406 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCustomSound && ((Boolean)this.CustomSound.getValue()).booleanValue()) {
/* 407 */       event.setCanceled(true);
/*     */     }
/* 409 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketDestroyEntities && ((Boolean)this.DestroyEntities.getValue()).booleanValue()) {
/* 410 */       event.setCanceled(true);
/*     */     }
/* 412 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketDisconnect && ((Boolean)this.Disconnect.getValue()).booleanValue()) {
/* 413 */       event.setCanceled(true);
/*     */     }
/* 415 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketChunkData && ((Boolean)this.ChunkData.getValue()).booleanValue()) {
/* 416 */       event.setCanceled(true);
/*     */     }
/* 418 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCloseWindow && ((Boolean)this.CloseWindow.getValue()).booleanValue()) {
/* 419 */       event.setCanceled(true);
/*     */     }
/* 421 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCollectItem && ((Boolean)this.CollectItem.getValue()).booleanValue()) {
/* 422 */       event.setCanceled(true);
/*     */     }
/* 424 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketDisplayObjective && ((Boolean)this.DisplayObjective.getValue()).booleanValue()) {
/* 425 */       event.setCanceled(true);
/*     */     }
/* 427 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEffect && ((Boolean)this.Effect.getValue()).booleanValue()) {
/* 428 */       event.setCanceled(true);
/*     */     }
/* 430 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntity && ((Boolean)this.Entity.getValue()).booleanValue()) {
/* 431 */       event.setCanceled(true);
/*     */     }
/* 433 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityAttach && ((Boolean)this.EntityAttach.getValue()).booleanValue()) {
/* 434 */       event.setCanceled(true);
/*     */     }
/* 436 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityEffect && ((Boolean)this.EntityEffect.getValue()).booleanValue()) {
/* 437 */       event.setCanceled(true);
/*     */     }
/* 439 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityEquipment && ((Boolean)this.EntityEquipment.getValue()).booleanValue()) {
/* 440 */       event.setCanceled(true);
/*     */     }
/* 442 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityHeadLook && ((Boolean)this.EntityHeadLook.getValue()).booleanValue()) {
/* 443 */       event.setCanceled(true);
/*     */     }
/* 445 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityMetadata && ((Boolean)this.EntityMetadata.getValue()).booleanValue()) {
/* 446 */       event.setCanceled(true);
/*     */     }
/* 448 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityProperties && ((Boolean)this.EntityProperties.getValue()).booleanValue()) {
/* 449 */       event.setCanceled(true);
/*     */     }
/* 451 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityStatus && ((Boolean)this.EntityStatus.getValue()).booleanValue()) {
/* 452 */       event.setCanceled(true);
/*     */     }
/* 454 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityTeleport && ((Boolean)this.EntityTeleport.getValue()).booleanValue()) {
/* 455 */       event.setCanceled(true);
/*     */     }
/* 457 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityVelocity && ((Boolean)this.EntityVelocity.getValue()).booleanValue()) {
/* 458 */       event.setCanceled(true);
/*     */     }
/* 460 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketExplosion && ((Boolean)this.Explosion.getValue()).booleanValue()) {
/* 461 */       event.setCanceled(true);
/*     */     }
/* 463 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketHeldItemChange && ((Boolean)this.HeldItemChange.getValue()).booleanValue()) {
/* 464 */       event.setCanceled(true);
/*     */     }
/* 466 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketJoinGame && ((Boolean)this.JoinGame.getValue()).booleanValue()) {
/* 467 */       event.setCanceled(true);
/*     */     }
/* 469 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketKeepAlive && ((Boolean)this.KeepAlive.getValue()).booleanValue()) {
/* 470 */       event.setCanceled(true);
/*     */     }
/* 472 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketMaps && ((Boolean)this.Maps.getValue()).booleanValue()) {
/* 473 */       event.setCanceled(true);
/*     */     }
/* 475 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketMoveVehicle && ((Boolean)this.MoveVehicle.getValue()).booleanValue()) {
/* 476 */       event.setCanceled(true);
/*     */     }
/* 478 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketMultiBlockChange && ((Boolean)this.MultiBlockChange.getValue()).booleanValue()) {
/* 479 */       event.setCanceled(true);
/*     */     }
/* 481 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketOpenWindow && ((Boolean)this.OpenWindow.getValue()).booleanValue()) {
/* 482 */       event.setCanceled(true);
/*     */     }
/* 484 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketParticles && ((Boolean)this.Particles.getValue()).booleanValue()) {
/* 485 */       event.setCanceled(true);
/*     */     }
/* 487 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlaceGhostRecipe && ((Boolean)this.PlaceGhostRecipe.getValue()).booleanValue()) {
/* 488 */       event.setCanceled(true);
/*     */     }
/* 490 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerAbilities && ((Boolean)this.PlayerAbilities.getValue()).booleanValue()) {
/* 491 */       event.setCanceled(true);
/*     */     }
/* 493 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerListHeaderFooter && ((Boolean)this.PlayerListHeaderFooter.getValue()).booleanValue()) {
/* 494 */       event.setCanceled(true);
/*     */     }
/* 496 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerListItem && ((Boolean)this.PlayerListItem.getValue()).booleanValue()) {
/* 497 */       event.setCanceled(true);
/*     */     }
/* 499 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerPosLook && ((Boolean)this.PlayerPosLook.getValue()).booleanValue()) {
/* 500 */       event.setCanceled(true);
/*     */     }
/* 502 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketRecipeBook && ((Boolean)this.RecipeBook.getValue()).booleanValue()) {
/* 503 */       event.setCanceled(true);
/*     */     }
/* 505 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketRemoveEntityEffect && ((Boolean)this.RemoveEntityEffect.getValue()).booleanValue()) {
/* 506 */       event.setCanceled(true);
/*     */     }
/* 508 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketResourcePackSend && ((Boolean)this.ResourcePackSend.getValue()).booleanValue()) {
/* 509 */       event.setCanceled(true);
/*     */     }
/* 511 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketRespawn && ((Boolean)this.Respawn.getValue()).booleanValue()) {
/* 512 */       event.setCanceled(true);
/*     */     }
/* 514 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketScoreboardObjective && ((Boolean)this.ScoreboardObjective.getValue()).booleanValue()) {
/* 515 */       event.setCanceled(true);
/*     */     }
/* 517 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSelectAdvancementsTab && ((Boolean)this.SelectAdvancementsTab.getValue()).booleanValue()) {
/* 518 */       event.setCanceled(true);
/*     */     }
/* 520 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketServerDifficulty && ((Boolean)this.ServerDifficulty.getValue()).booleanValue()) {
/* 521 */       event.setCanceled(true);
/*     */     }
/* 523 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSetExperience && ((Boolean)this.SetExperience.getValue()).booleanValue()) {
/* 524 */       event.setCanceled(true);
/*     */     }
/* 526 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSetPassengers && ((Boolean)this.SetPassengers.getValue()).booleanValue()) {
/* 527 */       event.setCanceled(true);
/*     */     }
/* 529 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSetSlot && ((Boolean)this.SetSlot.getValue()).booleanValue()) {
/* 530 */       event.setCanceled(true);
/*     */     }
/* 532 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSignEditorOpen && ((Boolean)this.SignEditorOpen.getValue()).booleanValue()) {
/* 533 */       event.setCanceled(true);
/*     */     }
/* 535 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSoundEffect && ((Boolean)this.SoundEffect.getValue()).booleanValue()) {
/* 536 */       event.setCanceled(true);
/*     */     }
/* 538 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnExperienceOrb && ((Boolean)this.SpawnExperienceOrb.getValue()).booleanValue()) {
/* 539 */       event.setCanceled(true);
/*     */     }
/* 541 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnGlobalEntity && ((Boolean)this.SpawnGlobalEntity.getValue()).booleanValue()) {
/* 542 */       event.setCanceled(true);
/*     */     }
/* 544 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnMob && ((Boolean)this.SpawnMob.getValue()).booleanValue()) {
/* 545 */       event.setCanceled(true);
/*     */     }
/* 547 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnObject && ((Boolean)this.SpawnObject.getValue()).booleanValue()) {
/* 548 */       event.setCanceled(true);
/*     */     }
/* 550 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnPainting && ((Boolean)this.SpawnPainting.getValue()).booleanValue()) {
/* 551 */       event.setCanceled(true);
/*     */     }
/* 553 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnPlayer && ((Boolean)this.SpawnPlayer.getValue()).booleanValue()) {
/* 554 */       event.setCanceled(true);
/*     */     }
/* 556 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnPosition && ((Boolean)this.SpawnPosition.getValue()).booleanValue()) {
/* 557 */       event.setCanceled(true);
/*     */     }
/* 559 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketStatistics && ((Boolean)this.Statistics.getValue()).booleanValue()) {
/* 560 */       event.setCanceled(true);
/*     */     }
/* 562 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTabComplete && ((Boolean)this.TabComplete.getValue()).booleanValue()) {
/* 563 */       event.setCanceled(true);
/*     */     }
/* 565 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTeams && ((Boolean)this.Teams.getValue()).booleanValue()) {
/* 566 */       event.setCanceled(true);
/*     */     }
/* 568 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTimeUpdate && ((Boolean)this.TimeUpdate.getValue()).booleanValue()) {
/* 569 */       event.setCanceled(true);
/*     */     }
/* 571 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTitle && ((Boolean)this.Title.getValue()).booleanValue()) {
/* 572 */       event.setCanceled(true);
/*     */     }
/* 574 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketUnloadChunk && ((Boolean)this.UnloadChunk.getValue()).booleanValue()) {
/* 575 */       event.setCanceled(true);
/*     */     }
/* 577 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketUpdateBossInfo && ((Boolean)this.UpdateBossInfo.getValue()).booleanValue()) {
/* 578 */       event.setCanceled(true);
/*     */     }
/* 580 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketUpdateHealth && ((Boolean)this.UpdateHealth.getValue()).booleanValue()) {
/* 581 */       event.setCanceled(true);
/*     */     }
/* 583 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketUpdateScore && ((Boolean)this.UpdateScore.getValue()).booleanValue()) {
/* 584 */       event.setCanceled(true);
/*     */     }
/* 586 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketUpdateTileEntity && ((Boolean)this.UpdateTileEntity.getValue()).booleanValue()) {
/* 587 */       event.setCanceled(true);
/*     */     }
/* 589 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketUseBed && ((Boolean)this.UseBed.getValue()).booleanValue()) {
/* 590 */       event.setCanceled(true);
/*     */     }
/* 592 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketWindowItems && ((Boolean)this.WindowItems.getValue()).booleanValue()) {
/* 593 */       event.setCanceled(true);
/*     */     }
/* 595 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketWindowProperty && ((Boolean)this.WindowProperty.getValue()).booleanValue()) {
/* 596 */       event.setCanceled(true);
/*     */     }
/* 598 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketWorldBorder && ((Boolean)this.WorldBorder.getValue()).booleanValue()) {
/* 599 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 605 */     String standart = "§aAntiPackets On!§f Cancelled Packets: ";
/* 606 */     StringBuilder text = new StringBuilder(standart);
/* 607 */     if (!this.settings.isEmpty()) {
/* 608 */       for (Setting setting : this.settings) {
/* 609 */         if (!(setting.getValue() instanceof Boolean) || !((Boolean)setting.getValue()).booleanValue() || setting.getName().equalsIgnoreCase("Enabled") || setting.getName().equalsIgnoreCase("drawn")) {
/*     */           continue;
/*     */         }
/* 612 */         String name = setting.getName();
/* 613 */         text.append(name).append(", ");
/*     */       } 
/*     */     }
/*     */     
/* 617 */     if (text.toString().equals(standart)) {
/* 618 */       Command.sendMessage("§aAntiPackets On!§f Currently not cancelling any Packets.");
/*     */     } else {
/* 620 */       String output = removeLastChar(removeLastChar(text.toString()));
/* 621 */       Command.sendMessage(output);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 627 */     int amount = 0;
/* 628 */     if (!this.settings.isEmpty()) {
/* 629 */       for (Setting setting : this.settings) {
/* 630 */         if (!(setting.getValue() instanceof Boolean) || !((Boolean)setting.getValue()).booleanValue() || setting.getName().equalsIgnoreCase("Enabled") || setting.getName().equalsIgnoreCase("drawn")) {
/*     */           continue;
/*     */         }
/* 633 */         amount++;
/*     */       } 
/*     */     }
/* 636 */     this.hudAmount = amount;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 641 */     if (this.hudAmount == 0) {
/* 642 */       return "";
/*     */     }
/* 644 */     return this.hudAmount + ((this.hudAmount == 1) ? " Packet" : " Packets");
/*     */   }
/*     */   
/*     */   public String removeLastChar(String str) {
/* 648 */     if (str != null && str.length() > 0) {
/* 649 */       str = str.substring(0, str.length() - 1);
/*     */     }
/* 651 */     return str;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\AntiPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */