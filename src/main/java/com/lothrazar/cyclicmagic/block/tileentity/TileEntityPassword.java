package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityPassword extends TileEntity {
  public static List<TileEntityPassword> listeningBlocks = new ArrayList<TileEntityPassword>();
  private String myPassword = "password";
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
    setMyPassword(tags.getString("myPassword"));
  }
  public String getMyPassword() {
    return myPassword;
  }
  public void setMyPassword(String myPassword) {
    this.myPassword = myPassword;
  }
}
