package com.lothrazar.cyclic.block.conveyor;

import com.lothrazar.cyclic.registry.EntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.level.Level;

public class ConveyorItemEntity extends ItemEntity {

  public ConveyorItemEntity(Level worldIn, double x, double y, double z, ItemStack stack) {
    super(EntityRegistry.conveyor_item, worldIn);
    this.setPos(x, y, z);
    this.setItem(stack);
    this.lifespan = Integer.MAX_VALUE;
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

  @Override
  public void setNeverPickUp() {
    super.setNeverPickUp();
  }

  @Override
  public float getSpin(float partialTicks) {
    return 0.0F;
  }

  @Override
  public void tick() {
    if (this.level == null) {
      return;
    }
    if (!(this.level.getBlockState(this.blockPosition()).getBlock() instanceof BlockConveyor)) {
      this.spawnRegularStack();
    }
    super.tick();
  }

  private void spawnRegularStack() {
    ItemEntity e = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), this.getItem());
    this.level.addFreshEntity(e);
    this.setItem(ItemStack.EMPTY);
    this.remove(RemovalReason.DISCARDED);
  }

  @Override
  public void playerTouch(Player entityIn) {
    //Do nothing
  }

  @Override
  public Packet<?> getAddEntityPacket() {
    return new ClientboundAddEntityPacket(this);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    compound.putInt("Lifespan", lifespan);
    if (this.getThrower() != null) {
      compound.putUUID("Thrower", this.getThrower());
    }
    if (this.getOwner() != null) {
      compound.putUUID("Owner", this.getOwner());
    }
    if (!this.getItem().isEmpty()) {
      compound.put("Item", this.getItem().save(new CompoundTag()));
    }
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    this.setExtendedLifetime();
    this.setNeverPickUp();
    if (compound.contains("Lifespan")) {
      lifespan = compound.getInt("Lifespan");
    }
    if (compound.hasUUID("Owner")) {
      this.setOwner(compound.getUUID("Owner"));
    }
    if (compound.hasUUID("Thrower")) {
      this.setThrower(compound.getUUID("Thrower"));
    }
    CompoundTag compoundnbt = compound.getCompound("Item");
    this.setItem(ItemStack.of(compoundnbt));
    if (this.getItem().isEmpty()) {
      this.remove();
    }
  }
}
