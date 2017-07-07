package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.command.CommandHearts;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerHealth;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHeartContainer extends ItemFood implements IHasRecipe, IHasConfig {
  private static final int numFood = 2;
  private static final int numHearts = 1;
  private static int maxHearts = 20;
  public ItemHeartContainer() {
    super(numFood, false);
    this.setAlwaysEdible();
  }
  private boolean isPlayerMaxHearts(EntityPlayer player) {
    return UtilEntity.getMaxHealth(player) / 2 >= maxHearts;
  }
  @Override
  protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
    IPlayerExtendedProperties prop = CapabilityRegistry.getPlayerProperties(player);
    if (isPlayerMaxHearts(player)) {
      UtilSound.playSound(player, SoundRegistry.buzzp);
      //      UtilItemStack.dropItemStackInWorld(world, player.getPosition(), this);
      return;
    }
    //one heart is 2 health points (half heart = 1 health)
    int newVal = UtilEntity.incrementMaxHealth(player, 2 * numHearts);
    prop.setMaxHealth(newVal);
    UtilSound.playSound(player, SoundRegistry.fill);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this), Items.BEETROOT, Items.RABBIT, Items.PUMPKIN_PIE, Items.DIAMOND, Items.CAKE, Blocks.EMERALD_BLOCK, new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()), Items.GOLDEN_APPLE, Items.POISONOUS_POTATO);
  }
  @SubscribeEvent
  public void onPlayerWarp(PlayerChangedDimensionEvent event) {
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(event.player);
    if (props.getMaxHealth() > 0 && event.player instanceof EntityPlayerMP) {
      //force clientside hearts to visually update
      ModCyclic.network.sendTo(new PacketSyncPlayerHealth(props.getMaxHealth()), (EntityPlayerMP) event.player);
    }
  }
  @SubscribeEvent
  public void onPlayerClone(PlayerEvent.Clone event) {
    IPlayerExtendedProperties src = CapabilityRegistry.getPlayerProperties(event.getOriginal());
    IPlayerExtendedProperties dest = CapabilityRegistry.getPlayerProperties(event.getEntityPlayer());
    dest.setDataFromNBT(src.getDataAsNBT());
    if (src.getMaxHealth() > 0) {
      UtilEntity.setMaxHealth(event.getEntityPlayer(), src.getMaxHealth());
    } //event.isWasDeath() && 
      //event if it wasnt death, we still want to do this. otherwise on going thru portla, the extra hearts
      //are hidden because mojang
      //if health var never used (never eaten a heart) then skip
  }
  @Override
  public void syncConfig(Configuration config) {
    maxHearts = config.getInt("HeartContainerMax", Const.ConfigCategory.modpackMisc,
        20, 10, 100, "Maximum number of heart containers you can get by eating heart containers.  Does not limit the /" + CommandHearts.name + " command");
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
  }
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    //this line is KEY to stop user from eating food at max health( which was causing the refund issue in https://github.com/PrinceOfAmber/Cyclic/issues/270 )
    if (isPlayerMaxHearts(playerIn)) { return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn); }
    //otherwise continueto normal food process
    return super.onItemRightClick(worldIn, playerIn, hand);
  }
}
