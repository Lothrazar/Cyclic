package com.lothrazar.cyclicmagic.item.bauble;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.base.BaseCharm;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemCharmFire extends BaseCharm implements IHasRecipe {
  private static final int durability = 16;
  private static final int seconds = 10;
  public ItemCharmFire() {
    super(durability);
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (!this.canTick(stack)) {
      return;
    }
    if (living.isBurning() && !living.isPotionActive(MobEffects.FIRE_RESISTANCE)) { // do nothing if you already have
      World worldIn = living.getEntityWorld();
      living.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, seconds * Const.TICKS_PER_SEC, Const.Potions.I));
      super.damageCharm(living, stack);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, living.getSoundCategory());
      UtilParticle.spawnParticle(worldIn, EnumParticleTypes.WATER_WAKE, living.getPosition());
      UtilParticle.spawnParticle(worldIn, EnumParticleTypes.WATER_WAKE, living.getPosition().up());
    }
  }
  @Override
  public IRecipe addRecipe() {
    return super.addRecipeAndRepair(Items.BLAZE_ROD);
  }
}
