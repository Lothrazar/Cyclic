package com.lothrazar.cyclic.item.magicnet;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.SoundUtil;
import com.lothrazar.library.util.StringParseUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.item.component.CustomData;

public class EntityMagicNetEmpty extends ThrowableItemProjectile {

  public static final String NBT_ENTITYID = ModCyclic.MODID + ":magicnet_id";
  public static final int PARTICLE_CAPTURE_COUNT = 8;

  public EntityMagicNetEmpty(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public EntityMagicNetEmpty(Level worldIn, LivingEntity livingEntityIn) {
    super(EntityRegistry.MAGIC_NET.get(), livingEntityIn, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return ItemRegistry.MAGIC_NET.get();
  }

  @Override
  public ItemStack getItem() {
    try {
      ItemStack itemstack = super.getItem();
      return (itemstack == null || itemstack.isEmpty()) ? new ItemStack(this.getDefaultItem()) : itemstack;
    } catch (Exception e) {
      return new ItemStack(this.getDefaultItem());
    }
  }

  @Override
  protected void onHit(HitResult result) {
    HitResult.Type type = result.getType();
    SimpleParticleType particleType = null;
    double targetHeightOffset = 0.0d;
    var level = level();
    if (type == HitResult.Type.ENTITY) {
      //now grab and kill the entity
      EntityHitResult entityRayTrace = (EntityHitResult) result;
      Entity target = entityRayTrace.getEntity();
      if (target instanceof Player || !target.isAlive()) {
        return;
      }
      if (target.getFirstPassenger() instanceof Player) {
        return;
      }
      //Wake up the mob in case they're sleeping in a bed see Issue #1599
      if (target instanceof LivingEntity) {
        ((LivingEntity) target).stopSleeping();
      }
      CompoundTag compound = new CompoundTag();
      target.save(compound);
      //
      if (target.getType().builtInRegistryHolder().is(DataTags.MAGICNET_BLOCKED)) {
        // datapack-extensible blocklist (defaults to inventory-holding minecarts,
        // which cheat by serializing their contents and don't follow normal mob capture logic)
        return;
      }
      String id = EntityType.getKey(target.getType()).toString();
      if (StringParseUtil.isInList(ConfigRegistry.getMagicNetList(), EntityType.getKey(target.getType()))) {
        ModCyclic.LOGGER.info("ignored by: CONFIG LIST" + id);
        return;
      }
      //
      //
      //
      compound.putString(NBT_ENTITYID, id);
      ItemStack drop = new ItemStack(ItemRegistry.MOB_CONTAINER.get());
      CustomData.set(DataComponents.CUSTOM_DATA, drop, compound);
      targetHeightOffset = target.getBbHeight() / 2;
      particleType = ParticleTypes.PORTAL;
      ItemStackUtil.drop(level, this.blockPosition(), drop);
      SoundUtil.playSound(target, SoundRegistry.MONSTER_BALL_CAPTURE.get());
      target.remove(RemovalReason.DISCARDED);
    }
    else if (type == HitResult.Type.BLOCK) {
      //      BlockRayTraceResult bRayTrace = (BlockRayTraceResult) result;
      BlockPos pos = this.blockPosition();
      targetHeightOffset = 0.0D;
      particleType = ParticleTypes.POOF;
      ItemStackUtil.drop(level, pos, new ItemStack(ItemRegistry.MAGIC_NET.get()));
    }
    if (particleType != null) {
      Vec3 hitVec = result.getLocation();
      for (int i = 0; i < PARTICLE_CAPTURE_COUNT; i++) {
        level.addParticle(particleType, hitVec.x(), hitVec.y() + targetHeightOffset, hitVec.z(), this.random.nextGaussian() * 0.1D, 0.0D, this.random.nextGaussian() * 0.1D);
      }
    }
    this.remove(RemovalReason.DISCARDED);
  }

  @Override
  public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
    return new ClientboundAddEntityPacket(this, serverEntity);
  }
}
