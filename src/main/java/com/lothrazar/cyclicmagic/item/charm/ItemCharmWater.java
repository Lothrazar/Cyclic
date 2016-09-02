package com.lothrazar.cyclicmagic.item.charm;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemCharmWater extends BaseCharm implements IHasRecipe {
  private static final int durability = 32;
  private static final int seconds = 10;
  public ItemCharmWater() {
    super(durability);
  }
  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to
   * check if is on a player hand and update it's contents.
   */
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      EntityPlayer living = (EntityPlayer) entityIn;
      if (living.getAir() < 6 && !living.isPotionActive(MobEffects.WATER_BREATHING)) {
        living.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, seconds * Const.TICKS_PER_SEC, Const.Potions.I));
        super.damageCharm(living, stack, itemSlot);
        UtilSound.playSound(living, worldIn.getSpawnPoint(), SoundEvents.ENTITY_PLAYER_SPLASH, living.getSoundCategory());
        UtilParticle.spawnParticle(worldIn, EnumParticleTypes.WATER_BUBBLE, living.getPosition());
        UtilParticle.spawnParticle(worldIn, EnumParticleTypes.WATER_BUBBLE, living.getPosition().up());
      }
    }
  }
  @Override
  public void addRecipe() {
    // TODO Auto-generated method stub
  }
}
