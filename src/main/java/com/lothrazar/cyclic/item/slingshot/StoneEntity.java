package com.lothrazar.cyclic.item.slingshot;

import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.registry.EntityRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class StoneEntity extends ThrowableItemProjectile {

  public StoneEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public StoneEntity(LivingEntity livingEntityIn, Level worldIn) {
    super(EntityRegistry.STONE_BOLT.get(), livingEntityIn, worldIn, new ItemStack(Items.COBBLESTONE));
  }

  @Override
  protected Item getDefaultItem() {
    return Items.COBBLESTONE;
  }

  @Override
  protected void onHit(HitResult result) {
    HitResult.Type type = result.getType();
    if (type == HitResult.Type.ENTITY) {
      //damage entity by zero
      //drop torch
      EntityHitResult entityRayTrace = (EntityHitResult) result;
      Entity target = entityRayTrace.getEntity();
      Entity owner = getOwner();
      var level = level();
      if (target.isAlive()) {
        target.hurt(level.damageSources().thrown(this, owner), Mth.nextInt(level.getRandom(), ConfigRegistry.SLINGSHOT_DAMAGE_MIN.get(), ConfigRegistry.SLINGSHOT_DAMAGE_MAX.get()));

      }
    }
    this.remove(RemovalReason.DISCARDED);
  }

  @Override
  public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
    return new ClientboundAddEntityPacket(this, serverEntity);
  }
}
