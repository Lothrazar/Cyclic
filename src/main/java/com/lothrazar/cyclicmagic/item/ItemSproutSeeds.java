package com.lothrazar.cyclicmagic.item;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSproutSeeds extends ItemSeeds {
  private final Block soilBlockID;
  public ItemSproutSeeds(Block crops, Block soil) {
    super(crops, soil);
    soilBlockID = soil;
  }
  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
    //without this override, it gets planted on grass just like flowers. since we dont fit an EnumPlantType def
    if (state!=null&&state.getBlock() == this.soilBlockID) {
      return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
    else {
      return EnumActionResult.FAIL;
    }
  }
}
