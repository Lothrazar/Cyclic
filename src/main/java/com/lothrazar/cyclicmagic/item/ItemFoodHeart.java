package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.command.CommandHearts;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemFoodHeart extends ItemFood implements IHasRecipe, IHasConfig {
  private static final int numFood = 2;
  private static final int numHearts = 1;
  private static int maxHearts = 20;
  public ItemFoodHeart() {
    super(numFood, false);
    this.setAlwaysEdible();
  }
  @Override
  protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
    IPlayerExtendedProperties prop = CapabilityRegistry.getPlayerProperties(player);
    if (UtilEntity.getMaxHealth(player) / 2 >= maxHearts) {
      UtilSound.playSound(player, SoundRegistry.buzzp);
      UtilEntity.dropItemStackInWorld(world, player.getPosition(), this);
      return;
    }
    //one heart is 2 health points (half heart = 1 health)
    int newVal = UtilEntity.incrementMaxHealth(player, 2 * numHearts);
    prop.setMaxHealth(newVal);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(new ItemStack(this), Items.BEETROOT, Items.RABBIT, Items.PUMPKIN_PIE, Items.DIAMOND, Items.CAKE, Blocks.EMERALD_BLOCK, new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()), Items.GOLDEN_APPLE, Items.POISONOUS_POTATO);
  }
  @SubscribeEvent
  public void onPlayerClone(PlayerEvent.Clone event) {
    IPlayerExtendedProperties src = CapabilityRegistry.getPlayerProperties(event.getOriginal());
    IPlayerExtendedProperties dest = CapabilityRegistry.getPlayerProperties(event.getEntityPlayer());
    dest.setDataFromNBT(src.getDataAsNBT());
    //if health var never used (never eaten a heart) then skip
    if (event.isWasDeath() && src.getMaxHealth() > 0) {
      UtilEntity.setMaxHealth(event.getEntityPlayer(), src.getMaxHealth());
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    maxHearts = config.getInt("HeartContainerMax", Const.ConfigCategory.modpackMisc,
        20, 10, 100, "Maximum number of heart containers you can get by eating heart containers.  Does not limit the /" + CommandHearts.name + " command");
  }
}
