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
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCharmFire extends BaseCharm implements IHasRecipe, baubles.api.IBauble {
  private static final int durability = 3;
  private static final int seconds = 10;
  public ItemCharmFire() {
    super(durability);
  }
  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to
   * check if is on a player hand and update it's contents.
   */
  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      onTick(stack,   (EntityPlayer) entityIn);
    }
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
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
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "r n",
        "ic ",
        "iir",
        'c', Items.BLAZE_ROD,
        'n', Items.NETHER_WART,
        'r', Items.REDSTONE,
        'i', Items.IRON_INGOT);
  }
  @Override
  public String getTooltip() {
    return "item.charm_fire.tooltip";
  }
  

}
