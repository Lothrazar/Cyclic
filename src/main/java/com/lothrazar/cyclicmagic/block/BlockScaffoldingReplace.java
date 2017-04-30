package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockScaffoldingReplace extends BlockScaffolding implements IHasRecipe {
  public BlockScaffoldingReplace() {
    super(false);
    this.dropBlock = true;
  }
  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack heldItem = playerIn.getHeldItem(hand);
    if (heldItem.isEmpty()) { return false; }
    Block b = Block.getBlockFromItem(heldItem.getItem());
    if (b != null && b != Blocks.AIR && !(b instanceof BlockScaffolding)) {
      worldIn.destroyBlock(pos, dropBlock);
      //     worldIn.setBlockState(pos, Block.getBlockFromItem(heldItem.getItem()) .getDefaultState()); //.useItemRightClick(worldIn, playerIn, hand);ee
      heldItem.onItemUse(playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ);
      return true;//to cancel event chains
    }
    return false;
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this, 16), "s s", "s s", "s s", 's', new ItemStack(Items.STICK));
  }
}
