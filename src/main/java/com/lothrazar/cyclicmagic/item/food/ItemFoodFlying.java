package com.lothrazar.cyclicmagic.item.food;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerFlying;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerHealth;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemFoodFlying extends ItemFood implements IHasRecipe, IHasConfig {
  private static final String KEY_BOOLEAN = "cyclicflying_on";
  private static final String KEY_TIMER = "cyclicflying_timer";
  public static final int FLY_SECONDS = 7;
  public static final int POTION_SECONDS = 6;
  public ItemFoodFlying() {
    super(2, false);
    this.setAlwaysEdible();
  }
  @Override
  protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
    setFlying(player);
    if (world.isRemote == false) {
      UtilNBT.incrementPlayerIntegerNBT(player, KEY_TIMER, FLY_SECONDS * Const.TICKS_PER_SEC);
      player.getEntityData().setBoolean(KEY_BOOLEAN, true);
    }
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this, 3),
        "lal", "lal", "lal",
        'l', Blocks.GLOWSTONE,
        'a', Items.CHORUS_FRUIT);
  }
  @Override
  public void syncConfig(Configuration config) {
//    String category = Const.ConfigCategory.modpackMisc;
    //    GHOST_SECONDS = config.getInt("CorruptedChorusSeconds", category, 5, 1, 60, "How long you can noclip after eating corrupted chorus");
    //    POTION_SECONDS = config.getInt("CorruptedChorusPotions", category, 10, 1, 60, "How long the negative potion effects last after a corrupted chorus teleports you");
  }
  private void setFlying(EntityPlayer player) {
    player.capabilities.allowFlying = true;
    player.capabilities.isFlying = true;
  }
  private void setNonFlying(EntityPlayer player) {
    System.out.println("setNonFlying"+player.getEntityWorld().isRemote);
    player.fallDistance = 0.0F;
    player.capabilities.allowFlying = false;
    player.capabilities.isFlying = false;

    if (player instanceof EntityPlayerMP) {
      //force clientside hearts to visually update
      ModCyclic.network.sendTo(new PacketSyncPlayerFlying(false), (EntityPlayerMP) player);
    }
  }
  @SubscribeEvent
  public void onPlayerUpdate(LivingUpdateEvent event) {
    if (event.getEntityLiving() instanceof EntityPlayer == false) { return; }
    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
    //    World world = player.getEntityWorld();
    if (player.getEntityData().getBoolean(KEY_BOOLEAN)) {
      int playerGhost = player.getEntityData().getInteger(KEY_TIMER);
      if (playerGhost > 0) {
        if (playerGhost % Const.TICKS_PER_SEC == 0) {
          int secs = playerGhost / Const.TICKS_PER_SEC;
          UtilChat.addChatMessage(player, "" + secs);
        }
        UtilNBT.incrementPlayerIntegerNBT(player, KEY_TIMER, -1);
        System.out.println("update..."+player.getEntityWorld().isRemote);
        setFlying(player);
      }
      else {
        //times up!
        player.getEntityData().setBoolean(KEY_BOOLEAN, false);
        //then we can stay, but add nausea
        player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, Const.TICKS_PER_SEC * POTION_SECONDS));
        player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, Const.TICKS_PER_SEC * POTION_SECONDS));
        setNonFlying(player);
      }
    }
  }
}
