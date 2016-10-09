package com.lothrazar.cyclicmagic.item.charm;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemCharmSpeed extends BaseCharm implements IHasRecipe {
  private static final int durability = 32;
  private static final int seconds = 60;
  public ItemCharmSpeed() {
    super(durability);
  }
  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to
   * check if is on a player hand and update it's contents.
   */
  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      onTick(stack, (EntityPlayer) entityIn);
    }
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (!this.canTick(stack)) { return; }
    if (living.onGround && !living.isPotionActive(MobEffects.SPEED)) { // do nothing if you already have
      living.addPotionEffect(new PotionEffect(MobEffects.SPEED, seconds * Const.TICKS_PER_SEC, Const.Potions.II));
      super.damageCharm(living, stack);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.ENTITY_GENERIC_DRINK, living.getSoundCategory());
      if (living.getRidingEntity() != null && living.getRidingEntity() instanceof EntityLivingBase) {
        EntityLivingBase maybeHorse = (EntityLivingBase) living.getRidingEntity();
        maybeHorse.addPotionEffect(new PotionEffect(MobEffects.SPEED, seconds * Const.TICKS_PER_SEC, Const.Potions.II));
      }
    }
  }
  @Override
  public void addRecipe() {
    super.addRecipeAndRepair(Items.EMERALD);
  }
  @Override
  public String getTooltip() {
    return "item.charm_speed.tooltip";
  }
}
