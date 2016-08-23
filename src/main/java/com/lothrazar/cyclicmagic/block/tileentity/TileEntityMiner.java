package com.lothrazar.cyclicmagic.block.tileentity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityMiner extends TileEntity implements ITickable {
  
  //vazkii wanted simple block breaker and block placer. already have the BlockBuilder for placing :D
  //of course this isnt standalone and hes probably found some other mod by now but doing it anyway https://twitter.com/Vazkii/status/767569090483552256
  // fake player idea ??? https://gitlab.prok.pw/Mirrors/minecraftforge/commit/f6ca556a380440ededce567f719d7a3301676ed0
  @Override
  public void update() {
    if (worldObj.getStrongPower(this.getPos()) == 0) { // it works ONLY if its powered
      this.markDirty();
      return;
    }
    if (!worldObj.isRemote) {
    
      System.out.println("next to make it mine blocks?");
    }
  }
}
