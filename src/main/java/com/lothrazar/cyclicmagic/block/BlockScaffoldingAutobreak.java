package com.lothrazar.cyclicmagic.block;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockScaffoldingAutobreak extends BlockScaffolding implements IHasRecipe {
  public BlockScaffoldingAutobreak() {
    super();
    this.dropBlock = false;
    this.doesAutobreak = false;
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this, 12), "sss", "   ", "s s", 's', new ItemStack(Items.STICK));
  }
  /**
   * Called when a neighboring block was changed and marks that this state
   * should perform any checks during a neighbor change. Cases may include when
   * redstone power is updated, cactus blocks popping off due to a neighboring
   * solid block, etc.
   */
  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
    super.neighborChanged(state, worldIn, pos, blockIn);
    if (blockIn == this) {
      //      List<Block> nbrs = new ArrayList<Block>();
      //      nbrs.add(worldIn.getBlockState(pos.up()).getBlock());
      //      nbrs.add(worldIn.getBlockState(pos.down()).getBlock());
      //      nbrs.add(worldIn.getBlockState(pos.west()).getBlock());
      //      nbrs.add(worldIn.getBlockState(pos.north()).getBlock());
      //      nbrs.add(worldIn.getBlockState(pos.south()).getBlock());
      //      nbrs.add(worldIn.getBlockState(pos.east()).getBlock());
      //      int c = 0;
      //      for (Block b : nbrs) {
      //        if (b instanceof BlockScaffoldingAutobreak) {
      //          c++;
      //        }
      //      }
      //     
      //if (c == 0) {
      if (dropBlock) {
        this.dropBlockAsItem(worldIn, pos, state, 0);
      }
      worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
      //      }
    }
  }
//  @Override
//  public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
//    System.out.println("onNeighborChange ");
//  }
}
