package com.lothrazar.cyclicmagic.block.base;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockBaseFacingInventory extends BlockBaseFacing {
  public BlockBaseFacingInventory(Material materialIn, int pguiIndex) {
    super(materialIn);
    this.setGuiId(pguiIndex);
  }
  public boolean hasComparatorInputOverride(IBlockState state) {
    return true;
  }
  public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
    return Container.calcRedstone(worldIn.getTileEntity(pos));
  }
  public String getRawName() {
    return this.getUnlocalizedName().replace("tile.", "");
  }
}
