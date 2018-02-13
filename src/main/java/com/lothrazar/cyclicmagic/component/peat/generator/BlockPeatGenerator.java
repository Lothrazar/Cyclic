package com.lothrazar.cyclicmagic.component.peat.generator;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.component.harvester.TileEntityHarvester;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPeatGenerator extends BlockBaseFacingInventory {
  public BlockPeatGenerator( ) {
    super(Material.ROCK, -1);
   
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityPeatGenerator();
  }
}
