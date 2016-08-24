package com.lothrazar.cyclicmagic.block.tileentity;
import com.lothrazar.cyclicmagic.block.BlockBaseFacing;
import com.lothrazar.cyclicmagic.block.BlockMiner;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

@SuppressWarnings("unused")
public abstract class TileEntityBaseMachine extends TileEntity {
  protected boolean isPowered() {
    return this.getWorld().isBlockPowered(this.getPos());//this.getWorld().isBlockIndirectlyGettingPowered(this.getPos()) > 0;
  }
  protected EnumFacing getCurrentFacing() {
    BlockBaseFacing b = ((BlockBaseFacing) this.getBlockType());
    EnumFacing facing;
    if (b == null || worldObj.getBlockState(pos) == null || b.getFacingFromState(worldObj.getBlockState(pos)) == null)
      facing = EnumFacing.UP;
    else
      facing = b.getFacingFromState(worldObj.getBlockState(this.pos));
    return facing;
  }
  //  public EnumFacing getFacingSelf() {
  //    return worldObj.getBlockState(pos).getValue(BlockMiner.PROPERTYFACING).getOpposite();
  //  }
}
