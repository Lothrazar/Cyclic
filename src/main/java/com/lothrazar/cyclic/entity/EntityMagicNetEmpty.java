package com.lothrazar.cyclic.entity;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityMagicNetEmpty extends ProjectileItemEntity {

  public static final String NBT_ENTITYID = ModCyclic.MODID + ":magicnet_id";

  public EntityMagicNetEmpty(EntityType<? extends ProjectileItemEntity> entityType, World world) {
    super(entityType, world);
  }

  public EntityMagicNetEmpty(LivingEntity livingEntityIn, World worldIn) {
    super(CyclicRegistry.Entities.netball, livingEntityIn, worldIn);
  }

  @Override
  protected Item func_213885_i() {
    return CyclicRegistry.Items.magic_net;
  }

  @Override
  protected void onImpact(RayTraceResult result) {
    RayTraceResult.Type type = result.getType();
    if (type == RayTraceResult.Type.ENTITY) {
      //now grab and kill the entity
      EntityRayTraceResult entityRayTrace = (EntityRayTraceResult) result;
      Entity target = entityRayTrace.getEntity();
      if (target instanceof PlayerEntity || !target.isAlive()) {
        return;
      }
      CompoundNBT compound = new CompoundNBT();
      target.writeUnlessPassenger(compound);
      String id = EntityType.getKey(target.getType()).toString();
      //TODO: is id blacklisted
      compound.putString(NBT_ENTITYID, id);
      ModCyclic.LOGGER.info(compound);
      return;
    }
    BlockPos pos = this.getPosition();
    world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CyclicRegistry.Items.magic_net)));
    this.remove();
  }

  @Override
  public void writeAdditional(CompoundNBT tag) {
    super.writeAdditional(tag);
  }

  @Override
  public void readAdditional(CompoundNBT tag) {
    super.readAdditional(tag);
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
