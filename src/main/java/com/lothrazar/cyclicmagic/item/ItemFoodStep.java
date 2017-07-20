package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemFoodStep extends ItemFood implements IHasRecipe {
  public ItemFoodStep() {
    super(4, false);
    this.setAlwaysEdible();
  }
  @Override
  protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
    final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(player);
    boolean previousOn = data.isStepHeightOn();
    data.setStepHeightOn(!previousOn);
    if (previousOn) {
      data.setForceStepOff(true);
    }
    UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, player.getPosition());
    UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, player.getPosition().up());
    UtilSound.playSound(player, SoundRegistry.bwewe);
    if (player.getEntityWorld().isRemote) {
      UtilChat.addChatMessage(player, "unlocks.stepheight" + !previousOn);
    }
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this), "chestEnder", Items.PUMPKIN_PIE, Items.CAKE, Items.COOKIE, new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()), Items.POISONOUS_POTATO, "gemDiamond", "gemEmerald", "gemQuartz");
  }
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (event.getEntityLiving() instanceof EntityPlayer) {//some of the items need an off switch
      EntityPlayer player = (EntityPlayer) event.getEntityLiving();
      final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(player);
      if (data.isStepHeightOn()) {
        player.stepHeight = 1.0F;//if MY feature turns this on, then do it
      }
      else if (data.doForceStepOff()) {
        //otherwise, dont automatically force it off. only force it off the once if player is toggling FROM on TO off with my feature
        player.stepHeight = 0.5F;
      }
      //else leave it alone (allows other mods to turn it on without me disrupting)
    }
  }
}
