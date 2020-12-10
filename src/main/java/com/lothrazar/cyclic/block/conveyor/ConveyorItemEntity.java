package com.lothrazar.cyclic.block.conveyor;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ConveyorItemEntity extends ItemEntity {
  public ConveyorItemEntity(World worldIn, double x, double y, double z, ItemStack stack) {
    super(worldIn, x, y, z, stack);
    this.setNoDespawn();
    this.setInfinitePickupDelay();
  }

  public ConveyorItemEntity(EntityType<? extends ItemEntity> entityEntityType, World world) {
    super(entityEntityType, world);
  }

  @Override
  public boolean cannotPickup() {
    return true;
  }

  @Override
  public void setInfinitePickupDelay() {
    super.setInfinitePickupDelay();
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public float getItemHover(float partialTicks) {
    return 0.0F;
  }

  @Override
  public void tick() {
    if (this.world == null)
      return;

    if (!(this.world.getBlockState(this.getPosition()).getBlock() instanceof BlockConveyor)) {
      this.spawnRegularStack();
    }
    super.tick();
  }

  private void spawnRegularStack() {
    ItemEntity e = new ItemEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), this.getItem());
    this.world.addEntity(e);
    this.remove();
  }
}
