package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockScaffoldingResponsive extends BlockScaffolding implements IHasRecipe {
  public BlockScaffoldingResponsive() {
    super(false);
    this.dropBlock = false;
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(new ItemStack(this, 64), Blocks.DIRT, new ItemStack(Items.STICK));
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
