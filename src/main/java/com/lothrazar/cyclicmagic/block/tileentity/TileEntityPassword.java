package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityPassword extends TileEntity {
  private static final String NBT_PASSWORD = "myPass";
  public static List<TileEntityPassword> listeningBlocks = new ArrayList<TileEntityPassword>();
  private String myPassword = "";
  public TileEntityPassword() {
    //it does save to server. on world save and reload, it DOes save. problem is, 
    //clientside does not KNOW about it
    if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
      listeningBlocks.add(this);
    }
    //    this.validate();
  }
  @Override
  public void onChunkUnload() {
    this.invalidate();
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setString(NBT_PASSWORD, getMyPassword());
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    myPassword = tags.getString(NBT_PASSWORD);
    super.readFromNBT(tags);
  }
  public String getMyPassword() {
    return myPassword;
  }
  public void setMyPassword(String myPassword) {
    this.myPassword = myPassword;
  }
  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
    //setting the block state seems to also run the constructor of the tile entity, which wipes out the data (thank you eclipse breakpoints)
    //this stops that from happening
    // http://www.minecraftforge.net/forum/index.php?topic=39528.0
    return oldState.getBlock() != newState.getBlock();
  }
  @Override
  public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
    //for keeping client/server in sync
    this.readFromNBT(packet.getNbtCompound());
    super.onDataPacket(net, packet);
  }
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    //for keeping client/server in sync
    NBTTagCompound tags = new NBTTagCompound();
    this.writeToNBT(tags);
    return new SPacketUpdateTileEntity(this.getPos(), 1, tags);
  }
  @Override
  public NBTTagCompound getUpdateTag() {
    //this is needed. otherwise on world reload, the server has accurate data but client does not. which means its not in gui
    // http://www.minecraftforge.net/forum/index.php?topic=39162.0
    NBTTagCompound compound = super.getUpdateTag();
    this.writeToNBT(compound);
    return compound;
  }
}
