package com.lothrazar.cyclicmagic.item.tool;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ItemAutoTorch extends BaseCharm implements IHasRecipe {
  private static final int durability = 256;
  private static final float lightLimit = 7.0F;
  public ItemAutoTorch() {
    super(durability);
  }
  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      onTick(stack, (EntityPlayer) entityIn);
    }
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (!this.canTick(stack)) { return; }
    World world = living.world;
    BlockPos pos = living.getPosition();
    if (world.getLight(pos, true) < lightLimit
        && living.isSpectator() == false
        && world.isSideSolid(pos.down(), EnumFacing.UP)
        && world.isAirBlock(pos)) { // dont overwrite liquids 
      if (UtilPlaceBlocks.placeStateSafe(world, living, pos, Blocks.TORCH.getDefaultState())) {
        super.damageCharm(living, stack);
      }
    }
  }
  @Override
  public IRecipe addRecipe() {
    GameRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE), Blocks.COAL_BLOCK, Blocks.COAL_BLOCK, Blocks.COAL_BLOCK);
    return GameRegistry.addShapedRecipe(new ItemStack(this),
        "cic",
        " i ",
        "cic",
        'c', Blocks.COAL_BLOCK,
        'i', Blocks.IRON_BARS);
  }
}
