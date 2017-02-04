package com.lothrazar.cyclicmagic.block;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IHasRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockScaffoldingReplace extends BlockScaffolding implements IHasRecipe {
  public BlockScaffoldingReplace() {
    super();
    this.dropBlock = true;
  }
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (heldItem == null) { return false; }
    Block b = Block.getBlockFromItem(heldItem.getItem());
    if (b != null && !(b instanceof BlockScaffolding)) {
      worldIn.destroyBlock(pos, dropBlock);
      //     worldIn.setBlockState(pos, Block.getBlockFromItem(heldItem.getItem()) .getDefaultState());
      heldItem.onItemUse(playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ); //.useItemRightClick(worldIn, playerIn, hand);
      return true;//to cancel event chains
    }
    return false;
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this, 16), "s s", "s s", "s s", 's', new ItemStack(Items.STICK));
  }
}
