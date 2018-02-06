package com.lothrazar.cyclicmagic.component.cable.bundle;
import com.lothrazar.cyclicmagic.component.cable.BlockBaseCable;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCableBundle extends BlockBaseCable {
  public BlockCableBundle() {
    super(Material.CLAY);
    this.setItemTransport();
    this.setFluidTransport();
    this.setPowerTransport();
  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityCableBundle();
  }
}
