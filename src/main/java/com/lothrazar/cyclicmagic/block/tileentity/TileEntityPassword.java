package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityPassword extends TileEntity {
  public static List<TileEntityPassword> listeningBlocks = new ArrayList<TileEntityPassword>();
  private String myPassword = "password";
  private String playerName = "player";
  public TileEntityPassword() {
    if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
      listeningBlocks.add(this);
    }
  }
  @Override
  public void onChunkUnload() {
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
    this.myPassword = myPassword;

  }
  public void saveChanges() {
    //save changes
    IBlockState state = this.worldObj.getBlockState(this.pos);
    this.worldObj.notifyBlockUpdate(pos, state, state, 3);
  }
  public String getPlayerName() {
    return playerName;
  }
  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }
}
