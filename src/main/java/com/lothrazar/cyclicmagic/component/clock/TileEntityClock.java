 package com.lothrazar.cyclicmagic.component.clock;

import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityClock extends TileEntityBaseMachine implements ITickable{
  
  
  private int timeOff = 60;
  private int timeOn = 60;
  private int power = 12;
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
      
System.out.println(timer+"__"+powered);
      world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockRedstoneClock.POWERED, powered));
      
    }
    
  }
}
