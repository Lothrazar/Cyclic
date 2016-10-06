package com.lothrazar.cyclicmagic.item.charm;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCharmSlowfall extends BaseCharm implements IHasRecipe {
  private final static int seconds = 30;
  private final static int fallDistanceLimit = 6;
  public ItemCharmSlowfall() {
    super(256);
  }
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      onTick(stack,  (EntityPlayer) entityIn);
    }
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (living.fallDistance >= fallDistanceLimit && !living.isPotionActive(PotionEffectRegistry.slowfallEffect)) {
      living.addPotionEffect(new PotionEffect(PotionEffectRegistry.slowfallEffect, seconds * Const.TICKS_PER_SEC, Const.Potions.I));
      super.damageCharm(living, stack);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.ITEM_ELYTRA_FLYING, living.getSoundCategory());
      UtilParticle.spawnParticle(living.getEntityWorld(), EnumParticleTypes.SUSPENDED, living.getPosition());
    }
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "r n",
        "ic ",
        "iir",
        'c', Items.SADDLE,
        'n', Items.RABBIT_FOOT,
        'r', Items.DIAMOND,
        'i', Items.IRON_INGOT);
  }
  @Override
  public String getTooltip() {
    return "item.charm_wing.tooltip";
  }
}
