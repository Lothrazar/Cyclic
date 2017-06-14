package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemCharmAntidote extends BaseCharm implements IHasRecipe {
  private static final int durability = 32;
  public ItemCharmAntidote() {
    super(durability);
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (!this.canTick(stack)) { return; }
    if (living.isPotionActive(MobEffects.POISON)) {
      living.removeActivePotionEffect(MobEffects.POISON);
      super.damageCharm(living, stack);
    }
    if (living.isPotionActive(MobEffects.WITHER)) {
      living.removeActivePotionEffect(MobEffects.WITHER);
      super.damageCharm(living, stack);
    }
  }
  @Override
  public IRecipe addRecipe() {
    return super.addRecipeAndRepair(Items.FERMENTED_SPIDER_EYE);
  }
}
