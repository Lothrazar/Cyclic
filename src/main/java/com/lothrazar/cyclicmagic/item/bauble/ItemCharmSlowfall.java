package com.lothrazar.cyclicmagic.item.bauble;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.base.BaseCharm;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;

public class ItemCharmSlowfall extends BaseCharm implements IHasRecipe {
  private final static int seconds = 30;
  private final static int fallDistanceLimit = 6;
  private final static int durability = 64;
  private final static Potion potion = PotionEffectRegistry.SLOWFALL;
  public ItemCharmSlowfall() {
    super(durability);
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (!this.canTick(stack)) { return; }
    if (living.fallDistance >= fallDistanceLimit && !living.isPotionActive(potion)) {
      living.addPotionEffect(new PotionEffect(potion, seconds * Const.TICKS_PER_SEC, Const.Potions.I));
      super.damageCharm(living, stack);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.ITEM_ELYTRA_FLYING, living.getSoundCategory());
      UtilParticle.spawnParticle(living.getEntityWorld(), EnumParticleTypes.SUSPENDED, living.getPosition());
    }
    if (living.isPotionActive(potion)) { //hacky / workaround for a reported issue being stuck at 0:00
      if (living.getActivePotionEffect(potion).getDuration() < 1) {
        living.removePotionEffect(potion);
      }
    }
  }
  @Override
  public IRecipe addRecipe() {
    return super.addRecipeAndRepair(Items.RABBIT_FOOT);
  }
}
