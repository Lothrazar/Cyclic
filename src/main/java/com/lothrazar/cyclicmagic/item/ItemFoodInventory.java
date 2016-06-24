package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemFoodInventory extends ItemFood implements IHasRecipe {
  private static final int numFood = 10;
  public ItemFoodInventory() {
    super(numFood, false);
    this.setMaxStackSize(1);
    this.setAlwaysEdible();
  }
  @Override
  protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
    final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(player);
    if (data.hasInventoryExtended()) {
      UtilSound.playSound(player, SoundRegistry.buzzp);
      return;
    }
    data.setInventoryExtended(true);
    UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, player.getPosition());
    UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, player.getPosition().up());
    UtilSound.playSound(player, SoundRegistry.bwewe);
    if (player.worldObj.isRemote) {
      UtilChat.addChatMessage(player, "unlocks.extended");
    }
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(new ItemStack(this), Blocks.ENDER_CHEST, Items.PUMPKIN_PIE, Items.CAKE, Items.COOKIE, new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()), Items.POISONOUS_POTATO, Items.DIAMOND, Items.EMERALD, Items.QUARTZ);
  }
}
