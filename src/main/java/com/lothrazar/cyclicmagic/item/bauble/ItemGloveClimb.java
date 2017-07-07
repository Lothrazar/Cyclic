package com.lothrazar.cyclicmagic.item.bauble;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseCharm;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ItemGloveClimb extends BaseCharm implements IHasRecipe {
  private static final double CLIMB_SPEED = 0.288D;
  public ItemGloveClimb() {
    super(6000);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 1),
        "ssl",
        "skl",
        "lli",
        's', "slimeball",
        'i', "ingotIron",
        'k', "dyeBlack",
        'l', "leather");
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer player) {
    if (!this.canTick(stack)) { return; }
    if (player.isCollidedHorizontally) {
      World world = player.getEntityWorld();
      UtilEntity.tryMakeEntityClimb(world, player, CLIMB_SPEED);
    }
  }
}
