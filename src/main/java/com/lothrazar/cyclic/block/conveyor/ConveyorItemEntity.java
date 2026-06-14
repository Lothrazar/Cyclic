package com.lothrazar.cyclic.block.conveyor;

import com.lothrazar.cyclic.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ConveyorItemEntity extends ItemEntity {

  public ConveyorItemEntity(Level worldIn, double x, double y, double z, ItemStack stack) {
    super(EntityRegistry.CONVEYOR_ITEM.get(), worldIn);
    this.lifespan = Integer.MAX_VALUE;
    this.setPos(x, y, z);
    this.setItem(stack);
    this.setExtendedLifetime();
    this.setNeverPickUp();
  }

  public ConveyorItemEntity(EntityType<ConveyorItemEntity> entityType, Level world) {
    super(entityType, world);
  }

  @Override
  public boolean hasPickUpDelay() {
    return true;
  }

//  @Override
//  public void setNeverPickUp() {
//    super.setNeverPickUp();
//  }

  @Override
  public float getSpin(float partialTicks) {
    return 0.0F;
  }

  @Override
  public void tick() {
    if (!level().isClientSide) {
      BlockPos bp = this.blockPosition();
      boolean onConveyor = level().getBlockState(bp).getBlock() instanceof BlockConveyor
          || level().getBlockState(bp.below()).getBlock() instanceof BlockConveyor;
      if (!onConveyor) {
        spawnRegularStack();
        return;
      }
    }
    super.tick();
  }

  private void spawnRegularStack() {
    ItemEntity e = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getItem().copy());
    this.level().addFreshEntity(e);
    this.setItem(ItemStack.EMPTY);
    this.remove(RemovalReason.DISCARDED);
  }

  @Override
  public void playerTouch(Player entityIn) {
    //Do nothing
    //player can pickup from the block when empty handed
  }

}
