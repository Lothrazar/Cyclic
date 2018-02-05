package com.lothrazar.cyclicmagic.component.cablepower;
import com.lothrazar.cyclicmagic.component.cable.BlockBaseCable;
import com.lothrazar.cyclicmagic.component.cable.TileEntityBaseCable;
import com.lothrazar.cyclicmagic.component.cablefluid.TileEntityFluidCable;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class BlockPowerCable extends BlockBaseCable {
  public BlockPowerCable() {
    super(Material.CLAY);
    this.setPowerTransport();
  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityCablePower();
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // check the TE
    TileEntityBaseCable te = (TileEntityBaseCable) world.getTileEntity(pos);
    if (te != null && te.isEnergyPipe()) {
      if (world.isRemote == false) { //server side
        IEnergyStorage handlerHere = te.getCapability(CapabilityEnergy.ENERGY, side);
        String msg = null;
     //TODO: cache the amount flowing for display just like item
        msg = handlerHere.getEnergyStored() + "/" + handlerHere.getMaxEnergyStored();
        //          if (te.getIncomingStrings() != "") {
        //            msg += " (" + UtilChat.lang("cyclic.fluid.flowing") + te.getIncomingStrings() + ")";
        //          }
        UtilChat.sendStatusMessage(player, msg);
      }
    }
    // otherwise return true if it is a fluid handler to prevent in world placement
    return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }
}
