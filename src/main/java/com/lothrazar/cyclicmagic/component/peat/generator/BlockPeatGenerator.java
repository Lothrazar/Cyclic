package com.lothrazar.cyclicmagic.component.peat.generator;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.component.harvester.TileEntityHarvester;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPeatGenerator extends BlockBaseFacingInventory {
  public BlockPeatGenerator( ) {
    super(Material.ROCK, ForgeGuiHandler.GUI_INDEX_PEATGEN);
   
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityPeatGenerator();
  }
}
