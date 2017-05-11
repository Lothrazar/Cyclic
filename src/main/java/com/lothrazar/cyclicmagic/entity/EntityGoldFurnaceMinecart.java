package com.lothrazar.cyclicmagic.entity;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityGoldFurnaceMinecart extends EntityMinecartFurnace {
  public EntityGoldFurnaceMinecart(World worldIn) {
    super(worldIn);
    this.setDisplayTile(this.getDefaultDisplayTile());
  }
  public EntityGoldFurnaceMinecart(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  /**
   * Get's the maximum speed for a minecart vanilla is 0.4D default
   */
  protected double getMaximumSpeed() {
    return 0.9D;
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
  public float getMaxCartSpeedOnRail() {
    return 1.3f;
  }
  public boolean isPoweredCart() {
    return true;
  }
  @Override
  protected void applyDrag() {
    this.setDisplayTile(this.getDefaultDisplayTile());
    if (this.isMinecartPowered() == false) {//is powered means fuel (from coal) is > 0
      super.applyDrag();//only apply drag if fuel rns out, else momentum goes forever without fuel = no makey sensey
    }
    else {
      //copy the vanilla minecraft furnace drag but push it down to almostnill
      double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
      double drag = 0.999999900190734863D;//vanilla is like 0.98000 ish
      if (d0 > 1.0E-4D) {
        this.motionX *= drag;
        this.motionY *= 0.0D;
        this.motionZ *= drag;
        this.motionX += this.pushX;
        this.motionZ += this.pushZ;
      }
    }
  } 
}
