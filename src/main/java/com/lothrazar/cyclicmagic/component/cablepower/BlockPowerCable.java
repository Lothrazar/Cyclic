package com.lothrazar.cyclicmagic.component.cablepower;
import com.lothrazar.cyclicmagic.component.cable.BlockBaseCable;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPowerCable extends BlockBaseCable {
  public BlockPowerCable() {
    super(Material.CLAY);
    this.setPowerTransport();
  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityCablePower();
  }
}
