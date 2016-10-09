package com.lothrazar.cyclicmagic.item.charm;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemCharmWater extends BaseCharm implements IHasRecipe {
  private static final int breath = 6;
  private static final int durability = 32;
  private static final int seconds = 60;
  public ItemCharmWater() {
    super(durability);
  }
  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to
   * check if is on a player hand and update it's contents.
   */
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      this.onTick(stack, (EntityPlayer) entityIn);
    }
  }
  @Override
  public void addRecipe() {
    super.addRecipeAndRepair(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));
  }
  @Override
  public String getTooltip() {
    return "item.charm_water.tooltip";
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (!this.canTick(stack)) { return; }
    World worldIn = living.getEntityWorld();
    if (living.getAir() < breath && !living.isPotionActive(MobEffects.WATER_BREATHING)) {
      living.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, seconds * Const.TICKS_PER_SEC, Const.Potions.I));
      super.damageCharm(living, stack);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.ENTITY_PLAYER_SPLASH, living.getSoundCategory());
      UtilParticle.spawnParticle(worldIn, EnumParticleTypes.WATER_BUBBLE, living.getPosition());
      UtilParticle.spawnParticle(worldIn, EnumParticleTypes.WATER_BUBBLE, living.getPosition().up());
    }
  }
}
