package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.registry.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;

public class LightningEntity extends ThrowableItemProjectile {

  public LightningEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public LightningEntity(LivingEntity livingEntityIn, Level worldIn) {
    super(EntityRegistry.LIGHTNING_BOLT.get(), livingEntityIn, worldIn, new ItemStack(Items.GHAST_TEAR));
  }

  @Override
  protected Item getDefaultItem() {
    return Items.GHAST_TEAR;
  }

  @Override
  protected void onHit(HitResult result) {
    HitResult.Type type = result.getType();
    if (type == HitResult.Type.ENTITY) {
      //damage entity by zero
      EntityHitResult entityRayTrace = (EntityHitResult) result;
      Entity target = entityRayTrace.getEntity();
      if (target.isAlive()) {
        target.hurt(level().damageSources().thrown(this, this.getOwner()), 0);
        //        LightningBoltEntity lightningboltentity = new LightningBoltEntity(world, target.getPosX(), target.getPosY(), target.getPosZ(), false);
        LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(level(), EntitySpawnReason.TRIGGERED);
        lightningboltentity.snapTo(target.getX(), target.getY(), target.getZ());
        level().addFreshEntity(lightningboltentity);
      }
    }
    else if (type == HitResult.Type.BLOCK) {
      //      BlockRayTraceResult bRayTrace = (BlockRayTraceResult) result;
      LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(level(), EntitySpawnReason.TRIGGERED);
      lightningboltentity.snapTo(this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ());
      level().addFreshEntity(lightningboltentity);
    }
    this.remove(RemovalReason.DISCARDED);
  }

  @Override
  public void addAdditionalSaveData(ValueOutput output) {
    super.addAdditionalSaveData(output);
  }

  @Override
  public void readAdditionalSaveData(ValueInput input) {
    super.readAdditionalSaveData(input);
  }

  @Override
  public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
    return new ClientboundAddEntityPacket(this, serverEntity);
  }
}
