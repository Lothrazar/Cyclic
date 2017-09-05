package com.lothrazar.cyclicmagic.component.scaffold;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 16), "s s", "s s", "s s", 's', "stickWood");
  }
}
