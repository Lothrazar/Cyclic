 package com.lothrazar.cyclicmagic.component.clock;

import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityClock extends TileEntityBaseMachine implements ITickable{
  
  
  private int timeOff = 10;
  private int timeOn = 10;
  private int power = 3;
  private int timer = 0;
  public int getPower(){
    return this.power;
  }
  @Override
  public void update(){
    
    if(world.isRemote == false){
      this.timer++;
      boolean powered;
      if(timer < timeOff){
        powered = false;
      }
      else if( timer < timeOff + timeOn){
        //we are in the ON section
        powered = true;
      }
      else{
        timer = 0;
        powered = false;
      }
      

      world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockRedstoneClock.POWERED, powered));
      
    }
    
  }
}
