package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityPassword extends TileEntityBaseMachine {
  private static final String NBT_PASSWORD = "myPass";
  public static List<TileEntityPassword> listeningBlocks = new ArrayList<TileEntityPassword>();
  private String myPassword = "";
  public TileEntityPassword() {
    //it does save to server. on world save and reload, it DOes save. problem is, 
    //clientside does not KNOW about it
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
}
