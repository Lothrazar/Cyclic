package com.lothrazar.cyclicmagic.block;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockScaffoldingReplace extends BlockScaffolding {
  public BlockScaffoldingReplace() {
    super();
    this.dropBlock = false;
  }
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (heldItem != null && Block.getBlockFromItem(heldItem.getItem()) != null) {
      worldIn.destroyBlock(pos, dropBlock);
      System.out.println("set air and replace");
      //     worldIn.setBlockState(pos, Block.getBlockFromItem(heldItem.getItem()) .getDefaultState());
      heldItem.onItemUse(playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ);      //.useItemRightClick(worldIn, playerIn, hand);
      return true;//to cancel event chains
    }
    return false;
  }
}
