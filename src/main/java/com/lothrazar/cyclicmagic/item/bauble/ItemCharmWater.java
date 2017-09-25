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
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
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
  @Override
  public IRecipe addRecipe() {
    return super.addRecipeAndRepair(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (!this.canTick(stack)) {
      return;
    }
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
