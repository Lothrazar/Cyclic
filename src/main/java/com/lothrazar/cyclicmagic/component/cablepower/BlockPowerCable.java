package com.lothrazar.cyclicmagic.component.cablepower;
import com.lothrazar.cyclicmagic.component.cable.BlockBaseCable;
import com.lothrazar.cyclicmagic.component.cable.TileEntityBaseCable;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

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
