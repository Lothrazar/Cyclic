package com.lothrazar.cyclic.item.boomerang;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BoomerangEntity extends ThrowableEntity {

  public BoomerangEntity(EntityType<? extends ThrowableEntity> type, World worldIn) {
    super(type, worldIn);
  }

  //  private static final int STUN_TICKS = 45;
  //  private static final int TICKS_UNTIL_RETURN = 12;
  //  private static final int TICKS_UNTIL_DEATH = 900;
  //  private static final double SPEED = 0.95;
  static final float DAMAGE_MIN = 1.5F;
  static final float DAMAGE_MAX = 2.8F;
  private static final DataParameter<Byte> IS_RETURNING = EntityDataManager.createKey(BoomerangEntity.class, DataSerializers.BYTE);
  private static final DataParameter<Byte> REDSTONE_TRIGGERED = EntityDataManager.createKey(BoomerangEntity.class, DataSerializers.BYTE);
  private static final DataParameter<String> OWNER = EntityDataManager.createKey(BoomerangEntity.class, DataSerializers.STRING);
  private ItemStack boomerangThrown;

  @Override
  public void writeAdditional(CompoundNBT tag) {
    tag.putString("OWNER", dataManager.get(OWNER));
    tag.putByte("returning", dataManager.get(IS_RETURNING));
    tag.putByte("REDSTONE_TRIGGERED", dataManager.get(REDSTONE_TRIGGERED));
    boomerangThrown.write(tag);
    super.writeAdditional(tag);
  }

  @Override
  public void readAdditional(CompoundNBT tag) {
    dataManager.set(OWNER, tag.getString("OWNER"));
    dataManager.set(IS_RETURNING, tag.getByte("returning"));
    dataManager.set(REDSTONE_TRIGGERED, tag.getByte("REDSTONE_TRIGGERED"));
    boomerangThrown = ItemStack.read(tag);
    super.readAdditional(tag);
  }

  public void setOwner(PlayerEntity ent) {
    //    this.targetEntity = ent;
    if (ent != null && ent.getUniqueID() != null)
      dataManager.set(OWNER, ent.getUniqueID().toString());
  }

  @Override
  protected void onImpact(RayTraceResult result) {
    // TODO Auto-generated method stub
  }

  @Override
  protected void registerData() {
    // TODO Auto-generated method stub
  }
}
