package com.lothrazar.cyclicmagic.component.cable.bundle;
import com.lothrazar.cyclicmagic.component.cable.BlockBaseCable;
import com.lothrazar.cyclicmagic.component.cable.CableBlockPrimary;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCableBundle extends CableBlockPrimary {
  public BlockCableBundle() {
//    super(Material.CLAY);
    this.setItemTransport();
    this.setFluidTransport();
    this.setPowerTransport();
  }
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new TileEntityCableBundle();
  }
}
