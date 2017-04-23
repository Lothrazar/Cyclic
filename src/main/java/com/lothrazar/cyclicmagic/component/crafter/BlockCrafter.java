package com.lothrazar.cyclicmagic.component.crafter;
import com.lothrazar.cyclicmagic.block.BlockBaseFacingInventory; 
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCrafter extends BlockBaseFacingInventory {
  public BlockCrafter() {
    super(Material.WOOD, -1);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityCrafter();
  }
}
