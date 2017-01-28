package com.lothrazar.cyclicmagic.block.tileentity;
import com.lothrazar.cyclicmagic.block.BlockBaseFacing;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileEntityBaseMachine extends TileEntity {
  public boolean isPowered() {
    return this.getWorld().isBlockPowered(this.getPos());
  }
  public boolean isRunning(){
    return !this.onlyRunIfPowered() || this.isPowered();//TODO: hotfix/sharedcode/baseclas
  }
  public boolean onlyRunIfPowered() {
    return false;//default is no, dont only run if powered, just go
  }
  protected EnumFacing getCurrentFacing() {
    BlockBaseFacing b = ((BlockBaseFacing) this.getBlockType());
    EnumFacing facing;
    if (b == null || this.getWorld().getBlockState(this.getPos()) == null || b.getFacingFromState(this.getWorld().getBlockState(this.getPos())) == null)
      facing = EnumFacing.UP;
    else
      facing = b.getFacingFromState(this.getWorld().getBlockState(this.getPos()));
    return facing;
  }
  protected BlockPos getCurrentFacingPos() {
    return this.getPos().offset(this.getCurrentFacing());
  }
  protected void spawnParticlesAbove() {
    if (this.getWorld().rand.nextDouble() < 0.05) {//was 0.1
      if (this.getWorld().isRemote == false) {
        UtilParticle.spawnParticlePacket(EnumParticleTypes.SMOKE_NORMAL, this.getPos());
      }
    }
  }
  /**
   * Block the data being lost when block stat e changes THANKS TO
   * http://www.minecraftforge.net/forum/index.php?topic=29544.0
   */
  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    return (oldState.getBlock() != newSate.getBlock());// : oldState != newSate;
  }
  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    // Extracts data from a packet (S35PacketUpdateTileEntity) that was sent
    // from the server. Called on client only.
    this.readFromNBT(pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {//getDescriptionPacket() {
    // Gathers data into a packet (S35PacketUpdateTileEntity) that is to be
    // sent to the client. Called on server only.
    NBTTagCompound syncData = getUpdateTag();
    return new SPacketUpdateTileEntity(this.pos, 1, syncData);
  }
  @Override
  public NBTTagCompound getUpdateTag() {
    //thanks http://www.minecraftforge.net/forum/index.php?topic=39162.0
    NBTTagCompound syncData = new NBTTagCompound();
    this.writeToNBT(syncData);//this calls writeInternal
    return syncData;
  }
}
