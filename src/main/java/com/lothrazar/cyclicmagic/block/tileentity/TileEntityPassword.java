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
  public static List<TileEntityPassword> listeningBlocks = new ArrayList<TileEntityPassword>();
  private String myPassword = "";
  public TileEntityPassword() {
    if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
      listeningBlocks.add(this);
    }
    this.validate();
  }
  @Override
  public void onChunkUnload() {
    super.onChunkUnload();
    this.invalidate();
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setString("myPassword", getMyPassword());
    return tags;
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    myPassword = tags.getString("myPassword");
  }
  public String getMyPassword() {
    return myPassword;
  }
  public void setMyPassword(String myPassword) {
    //    System.out.println("set password, old => new " + this.myPassword + "=>" + myPassword);
    this.myPassword = myPassword;
  }
  public void saveChanges() {
    //save changes
    IBlockState state = this.getWorld().getBlockState(this.getPos());
    this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 3);
    this.markDirty();
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
    readFromNBT(packet.getNbtCompound());
    IBlockState state = this.getWorld().getBlockState(this.getPos());
    this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 3);
  }
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    //for keeping client/server in sync
    NBTTagCompound tags = new NBTTagCompound();
    this.writeToNBT(tags);
    return new SPacketUpdateTileEntity(this.getPos(), 1, tags);
  }
}
