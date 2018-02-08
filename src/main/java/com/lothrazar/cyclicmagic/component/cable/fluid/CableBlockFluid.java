package com.lothrazar.cyclicmagic.component.cable.fluid;
import com.lothrazar.cyclicmagic.component.cable.CableBlockPrimary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CableBlockFluid extends CableBlockPrimary {
  public CableBlockFluid() {
    this.setFluidTransport();
  }
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new TileEntityFluidCable();
  }
}
