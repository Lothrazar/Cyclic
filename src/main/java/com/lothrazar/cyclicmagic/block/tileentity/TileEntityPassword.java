package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineStructureBuilder.Fields;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityPassword extends TileEntityBaseMachineInvo {
  private static final String NBT_ATYPE = "type";
  private static final String NBT_USERP = "up";
  private static final String NBT_PASSWORD = "myPass";
  private static final String NBT_U = "uhash";
  public static enum ActiveType {
    TOGGLE, PULSE;
  }
  public static enum UsersAllowed {
    ALL, ME;//todo: team? idk if possible
  }
  public static enum Fields {
    ACTIVETYPE, USERSALLOWED;
  }
  public static List<TileEntityPassword> listeningBlocks = new ArrayList<TileEntityPassword>();
  private ActiveType type;
  private UsersAllowed userPerm;
  private String myPassword = "";
  private String userHash = "";
  public TileEntityPassword() {
    setType(ActiveType.TOGGLE);
    setUserPerm(UsersAllowed.ALL);//defaults to same behavior it had before these were added
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
    tags.setString(NBT_U, userHash);
    tags.setInteger(NBT_USERP, getUserPerm().ordinal());
    tags.setInteger(NBT_ATYPE, getType().ordinal());
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    myPassword = tags.getString(NBT_PASSWORD);
    userHash = tags.getString(NBT_U);
    setType(ActiveType.values()[tags.getInteger(NBT_ATYPE)]);
    setUserPerm(UsersAllowed.values()[tags.getInteger(NBT_USERP)]);
    super.readFromNBT(tags);
  }
  public String getMyPassword() {
    return myPassword;
  }
  public void setMyPassword(String myPassword) {
    this.myPassword = myPassword;
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case ACTIVETYPE:
        this.setType(ActiveType.values()[value]);
      break;
      case USERSALLOWED:
        this.setUserPerm(UsersAllowed.values()[value]);
      break;
    }
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case ACTIVETYPE:
        return this.getType().ordinal();
      case USERSALLOWED:
        return this.getUserPerm().ordinal();
    }
    return -1;
  }
  public ActiveType getType() {
    return type;
  }
  public void setType(ActiveType type) {
    this.type = type;
  }
  public UsersAllowed getUserPerm() {
    return userPerm;
  }
  public void setUserPerm(UsersAllowed userPerm) {
    this.userPerm = userPerm;
  }
  public void toggleActiveType() {
    int t = getType().ordinal();
    t++;
    if(t >= ActiveType.values().length){
      t=0;
    }
    setType(ActiveType.values()[t]);
  }
  public void toggleUserType() {
    int t = getUserPerm().ordinal();
    t++;
    if(t >= UsersAllowed.values().length){
      t=0;
    }
    setUserPerm(UsersAllowed.values()[t]);
  }
}
