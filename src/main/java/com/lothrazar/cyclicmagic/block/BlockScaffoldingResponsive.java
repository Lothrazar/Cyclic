package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockScaffoldingResponsive extends BlockScaffolding implements IHasRecipe {
  public BlockScaffoldingResponsive() {
    super(false);
    this.dropBlock = false;
  }
  @Override
  public IRecipe addRecipe() {
    return  RecipeRegistry.addShapelessRecipe(new ItemStack(this, 64), Blocks.DIRT, new ItemStack(Items.STICK));
  
  }
  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    if (blockIn == this) {
      worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }
  }
}
