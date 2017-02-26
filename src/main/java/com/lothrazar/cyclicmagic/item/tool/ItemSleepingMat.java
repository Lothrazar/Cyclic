package com.lothrazar.cyclicmagic.item.tool;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.BaseTool;
import com.lothrazar.cyclicmagic.net.PacketSleepClient;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSleepingMat extends BaseTool implements IHasRecipe, IHasConfig {
  // thank you for the examples forge. player data storage based on API source
  // https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java
  private static int seconds;
  public static boolean doPotions;
  public ItemSleepingMat() {
    super(100);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (!world.isRemote) {
      EntityPlayerMP mp = (EntityPlayerMP) player;
      //      final EntityPlayer.SleepResult result = player.trySleep(player.getPosition());
      //trySleep was changed in 1.11.2 to literally check for the specific exact  Blocks.BED in world. because fuck modders amirite?
      //and it just assumes unsafely its there and then dies.
      EntityPlayer.SleepResult result = this.canPlayerSleep(player, world);
      if (result == EntityPlayer.SleepResult.OK) {
        final IPlayerExtendedProperties sleep = CapabilityRegistry.getPlayerProperties(player);
        if (sleep != null) {
          sleep.setSleeping(true);
          if (doPotions) {
            player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, seconds * Const.TICKS_PER_SEC, Const.Potions.I));
            player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, seconds * Const.TICKS_PER_SEC, Const.Potions.I));
          }
          this.onUse(stack, player, world, hand);
          //hack because vanilla/forge has that java.lang.IllegalArgumentException: Cannot get property PropertyDirection error with assuming its a bed when its blocks.air
          ObfuscationReflectionHelper.setPrivateValue(EntityPlayer.class, player, true, "sleeping", "field_71083_bS");
          ObfuscationReflectionHelper.setPrivateValue(EntityPlayer.class, player, 0, "sleepTimer", "field_71076_b");
          UtilChat.addChatMessage(player, this.getUnlocalizedName() + ".trying");
          //first set bed location
          player.bedLocation = player.getPosition();
          ModCyclic.network.sendTo(new PacketSleepClient(player.bedLocation), mp);
          //then stop player in place
          player.motionX = player.motionZ = player.motionY = 0;
          world.updateAllPlayersSleepingFlag();
          //then trigger vanilla sleep event(s)
          //                world.setBlockState(player.getPosition(), Blocks.BED.getDefaultState());
          SPacketUseBed sleepPacket = new SPacketUseBed(player, player.getPosition());
          mp.getServerWorld().getEntityTracker().sendToTracking(player, sleepPacket);
          mp.connection.sendPacket(sleepPacket);
          //                mp.setRenderOffsetForSleep(player.getHorizontalFacing());//is private
          ItemSleepingMat.setRenderOffsetForSleep(player, player.getHorizontalFacing());
        }
        else {
          //					ModMain.logger.error("NULL IPlayerExtendedProperties found");
          //should never happen... but just in case
          UtilChat.addChatMessage(player, "tile.bed.noSleep");
        }
        //as with 1.10.2, we do not set   player.bedLocation = on purpose
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
      }
      else {
        UtilChat.addChatMessage(player, "tile.bed.noSleep");
      }
    }
    return ActionResult.newResult(EnumActionResult.PASS, stack);
  }
  //stupid private functions in entity player
  public static void setRenderOffsetForSleep(EntityPlayer mp, EnumFacing p_175139_1_) {
    mp.renderOffsetX = -1.8F * (float) p_175139_1_.getFrontOffsetX();
    mp.renderOffsetZ = -1.8F * (float) p_175139_1_.getFrontOffsetZ();
  }
  /**
   * hack in the vanilla sleep test, or at least something similar
   * 
   * @param player
   * @param world
   * @return
   */
  private SleepResult canPlayerSleep(EntityPlayer player, World world) {
    if (player.isEntityAlive() == false) { return EntityPlayer.SleepResult.OTHER_PROBLEM; }
    if (world.isDaytime()) { return EntityPlayer.SleepResult.NOT_POSSIBLE_NOW; }
    PlayerSleepInBedEvent event = new PlayerSleepInBedEvent(player, player.getPosition());
    MinecraftForge.EVENT_BUS.post(event);
    if (event.getResultStatus() != null) { return event.getResultStatus(); }
    return EntityPlayer.SleepResult.OK;
  }
  @SubscribeEvent
  public void onBedCheck(SleepingLocationCheckEvent evt) {
    EntityPlayer p = evt.getEntityPlayer();
    final IPlayerExtendedProperties sleep = p.getCapability(ModCyclic.CAPABILITYSTORAGE, null);
    if (sleep != null && sleep.isSleeping()) {
      p.bedLocation = p.getPosition();
      evt.setResult(Result.ALLOW);
    }
  }
  @SubscribeEvent
  public void handleSleepInBed(PlayerSleepInBedEvent event) {
    final IPlayerExtendedProperties sleep = event.getEntityPlayer().getCapability(ModCyclic.CAPABILITYSTORAGE, null);
    if (sleep != null && sleep.isSleeping()) {
      event.setResult(EntityPlayer.SleepResult.OK);
    }
  }
  @SubscribeEvent
  public void onWakeUp(PlayerWakeUpEvent evt) {
    EntityPlayer p = evt.getEntityPlayer();
    final IPlayerExtendedProperties sleep = p.getCapability(ModCyclic.CAPABILITYSTORAGE, null);
    if (sleep != null && sleep.isSleeping()) {
      sleep.setSleeping(false);
//      World world = p.getEntityWorld();
//      BlockPos pos = p.getPosition();
      //      
      //      if(
      //          world.getBlockState(pos).getBlock() == Blocks.BED){
      //
      //        System.out.println("onWakeUpDESTRY");
      //        world.setBlockToAir(pos); 
      //      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    doPotions = config.getBoolean("SleepingMatPotions", Const.ConfigCategory.items, true, "False will disable the potion effects given by the Sleeping Mat");
    seconds = config.getInt("SleepingMatPotion", Const.ConfigCategory.modpackMisc, 20, 0, 600, "Seconds of potion effect caused by using the sleeping mat");
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(new ItemStack(this),
        new ItemStack(Blocks.WOOL, 1, EnumDyeColor.RED.getMetadata()),
        Items.LEATHER);
  }
}