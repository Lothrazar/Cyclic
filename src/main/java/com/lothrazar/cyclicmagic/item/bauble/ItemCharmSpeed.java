package com.lothrazar.cyclicmagic.item.bauble;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseCharm;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemCharmSpeed extends BaseCharm implements IHasRecipe {
  private static final int durability = 2000;
  private static final float speedfactor = 0.08F;
  public ItemCharmSpeed() {
    super(durability);
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer player) {
    if (!this.canTick(stack)) {
      return;
    }
    UtilEntity.speedupEntityIfMoving(player, speedfactor);
    if (player.getEntityWorld().rand.nextDouble() < 0.1) {
      super.damageCharm(player, stack);
    }
  }
  @Override
  public IRecipe addRecipe() {
    return super.addRecipeAndRepair(new ItemStack(Blocks.EMERALD_BLOCK));
  }
}
