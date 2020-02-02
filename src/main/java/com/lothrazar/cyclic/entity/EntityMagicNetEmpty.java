package com.lothrazar.cyclic.entity;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
  protected Item getDefaultItem() {
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
      //      target.rotationYaw// TODO
      //      target.rotationPitch
      //      ModCyclic.LOGGER.info(compound);
      ItemStack drop = new ItemStack(CyclicRegistry.Items.mob_container);
      drop.setTag(compound);
      UtilItemStack.drop(world, this.getPosition(), drop);
      target.remove();
    }
    else if (type == RayTraceResult.Type.BLOCK) {
      //      BlockRayTraceResult bRayTrace = (BlockRayTraceResult) result;
      BlockPos pos = this.getPosition();
      UtilItemStack.drop(world, pos, new ItemStack(CyclicRegistry.Items.magic_net));
    }
    this.remove();
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
