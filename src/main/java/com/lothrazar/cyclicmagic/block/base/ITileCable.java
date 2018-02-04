package com.lothrazar.cyclicmagic.block.base;
import java.util.Map;
import com.lothrazar.cyclicmagic.component.cable.BlockBaseCable.EnumConnectType;
import net.minecraft.util.EnumFacing;

public interface ITileCable {
  public EnumConnectType north();
  public EnumConnectType south();
  public EnumConnectType east();
  public EnumConnectType west();
  public EnumConnectType up();
  public EnumConnectType down();
  public void setConnects(Map<EnumFacing, EnumConnectType> map);
  public Map<EnumFacing, EnumConnectType> getConnects();
  public boolean hasAnyIncomingFluidFaces();
  public void tickDownIncomingFluidFaces();
  public String getIncomingStrings();
}
