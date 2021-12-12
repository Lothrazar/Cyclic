package com.lothrazar.cyclic.block.conveyor;

import com.lothrazar.cyclic.registry.EntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.World;

public class ConveyorItemEntity extends ItemEntity {

  public ConveyorItemEntity(World worldIn, double x, double y, double z, ItemStack stack) {
    super(EntityRegistry.conveyor_item, worldIn);
    this.setPosition(x, y, z);
    this.setItem(stack);
    this.lifespan = Integer.MAX_VALUE;
    this.setNoDespawn();
    this.setInfinitePickupDelay();
  }

  public ConveyorItemEntity(EntityType<ConveyorItemEntity> entityType, World world) {
    super(entityType, world);
  }

  @Override
  public boolean cannotPickup() {
    return true;
  }

  @Override
  public void setInfinitePickupDelay() {
    super.setInfinitePickupDelay();
  }

  @Override
  public float getItemHover(float partialTicks) {
    return 0.0F;
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    if (!(this.world.getBlockState(this.getPosition()).getBlock() instanceof BlockConveyor)) {
      this.spawnRegularStack();
    }
    super.tick();
  }

  private void spawnRegularStack() {
    ItemEntity e = new ItemEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), this.getItem());
    this.world.addEntity(e);
    this.setItem(ItemStack.EMPTY);
    this.remove();
  }

  @Override
  public void onCollideWithPlayer(PlayerEntity entityIn) {
    //Do nothing
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return new SSpawnObjectPacket(this);
  }

  @Override
  public void writeAdditional(CompoundNBT compound) {
    compound.putInt("Lifespan", lifespan);
    if (this.getThrowerId() != null) {
      compound.putUniqueId("Thrower", this.getThrowerId());
    }
    if (this.getOwnerId() != null) {
      compound.putUniqueId("Owner", this.getOwnerId());
    }
    if (!this.getItem().isEmpty()) {
      compound.put("Item", this.getItem().write(new CompoundNBT()));
    }
  }

  @Override
  public void readAdditional(CompoundNBT compound) {
    this.setNoDespawn();
    this.setInfinitePickupDelay();
    if (compound.contains("Lifespan")) {
      lifespan = compound.getInt("Lifespan");
    }
    if (compound.hasUniqueId("Owner")) {
      this.setOwnerId(compound.getUniqueId("Owner"));
    }
    if (compound.hasUniqueId("Thrower")) {
      this.setThrowerId(compound.getUniqueId("Thrower"));
    }
    CompoundNBT compoundnbt = compound.getCompound("Item");
    this.setItem(ItemStack.read(compoundnbt));
    if (this.getItem().isEmpty()) {
      this.remove();
    }
  }
}
