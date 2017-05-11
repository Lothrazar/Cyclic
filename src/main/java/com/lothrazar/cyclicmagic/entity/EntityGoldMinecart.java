package com.lothrazar.cyclicmagic.entity;

import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.world.World;

public class EntityGoldMinecart extends EntityMinecartEmpty {
  public EntityGoldMinecart(World worldIn){
    super(worldIn);
  }
  
 
  /**
   * Get's the maximum speed for a minecart
   * vanilla is 0.4D default
   */
  protected double getMaximumSpeed()
  {
      return 0.8D;
  }

  /**
   * Returns the carts max speed when traveling on rails. Carts going faster
   * than 1.1 cause issues with chunk loading. Carts cant traverse slopes or
   * corners at greater than 0.5 - 0.6. This value is compared with the rails
   * max speed and the carts current speed cap to determine the carts current
   * max speed. A normal rail's max speed is 0.4.
   *
   * @return Carts max speed.
   */
  public float getMaxCartSpeedOnRail()
  {
      return 1.4f;
  }

  public boolean isPoweredCart()
  {
      return true;
  }

 
  
}
