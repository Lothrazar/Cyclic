package com.lothrazar.cyclicmagic.entity;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGoldMinecart extends EntityMinecartEmpty {
  public static Item dropItem = Items.MINECART;//override with gold minecart on registry, this is here just for nonnull
  public EntityGoldMinecart(World worldIn) {
    super(worldIn);
  }
  public EntityGoldMinecart(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  /**
   * Get's the maximum speed for a minecart vanilla is 0.4D default
   */
  @Override
  protected double getMaximumSpeed() {
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
  @Override
  public float getMaxCartSpeedOnRail() {
    return 1.4f;
  }
  @Override
  public boolean isPoweredCart() {
    return true;
  }
  @Override
  public void killMinecart(DamageSource source) {
    this.setDead();
    if (this.world.getGameRules().getBoolean("doEntityDrops")) {
      ItemStack itemstack = new ItemStack(dropItem);
      if (this.hasCustomName()) {
        itemstack.setStackDisplayName(this.getCustomNameTag());
      }
      this.entityDropItem(itemstack, 0.0F);
    }
  }
}
