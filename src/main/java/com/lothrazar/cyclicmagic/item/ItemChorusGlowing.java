package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerFlying;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChorusGlowing extends ItemFood implements IHasRecipe {
  private static final String KEY_BOOLEAN = "cyclicflying_on";
  private static final String KEY_TIMER = "cyclicflying_timer";
  private static final String KEY_POTION = "cyclicflying_potion";//more if you eat more
  public static final int FLY_SECONDS = 10;
  public static final int POTION_SECONDS = 10;
  public ItemChorusGlowing() {
    super(4, false);
    this.setAlwaysEdible();
  }
  @Override
  protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
    setFlying(player);
    if (world.isRemote == false) {
      UtilNBT.incrementPlayerIntegerNBT(player, KEY_TIMER, FLY_SECONDS * Const.TICKS_PER_SEC);
      player.getEntityData().setBoolean(KEY_BOOLEAN, true);
      UtilNBT.incrementPlayerIntegerNBT(player, KEY_POTION, POTION_SECONDS);
    }
  }
  @Override
  public IRecipe addRecipe() {
    return GameRegistry.addShapedRecipe(new ItemStack(this, 3),
        "lal", "lal", "lal",
        'l', Items.GLOWSTONE_DUST,
        'a', Items.CHORUS_FRUIT);
  }
  private void setFlying(EntityPlayer player) {
    player.fallDistance = 0.0F;
    player.capabilities.allowFlying = true;
    player.capabilities.isFlying = true;
  }
  private void setNonFlying(EntityPlayer player) {
    player.capabilities.allowFlying = false;
    player.capabilities.isFlying = false;
    if (player instanceof EntityPlayerMP) { //force clientside  to  update
      ModCyclic.network.sendTo(new PacketSyncPlayerFlying(false), (EntityPlayerMP) player);
    }
  }
  @SubscribeEvent
  public void onPlayerUpdate(LivingUpdateEvent event) {
    if (event.getEntityLiving() instanceof EntityPlayer == false) { return; }
    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
    if (player.getEntityData().getBoolean(KEY_BOOLEAN)) { //unlike corrupted chorus, we are fine with losing this data on death, it just stops flight
      int playerGhost = player.getEntityData().getInteger(KEY_TIMER);
      if (playerGhost > 0) {
        if (playerGhost % Const.TICKS_PER_SEC == 0) {
          int secs = playerGhost / Const.TICKS_PER_SEC;
          UtilChat.addChatMessage(player, "" + secs);
        }
        UtilNBT.incrementPlayerIntegerNBT(player, KEY_TIMER, -1);
        setFlying(player);
      }
      else { //times up!
        player.getEntityData().setBoolean(KEY_BOOLEAN, false);
        int playerPot = player.getEntityData().getInteger(KEY_POTION);
        player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, Const.TICKS_PER_SEC * playerPot));
        player.getEntityData().setInteger(KEY_POTION, 0);
        setNonFlying(player);
      }
    }
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltips, boolean advanced) {
    tooltips.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
  }
  @Override
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
}
