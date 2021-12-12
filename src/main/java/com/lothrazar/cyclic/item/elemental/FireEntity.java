package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.PotionRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class FireEntity extends ProjectileItemEntity {

  public FireEntity(EntityType<? extends ProjectileItemEntity> entityType, World world) {
    super(entityType, world);
  }

  public FireEntity(LivingEntity livingEntityIn, World worldIn) {
    super(EntityRegistry.fire_bolt, livingEntityIn, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return Items.FIRE_CHARGE;
  }

  @Override
  protected void onImpact(RayTraceResult result) {
    RayTraceResult.Type type = result.getType();
    if (type == RayTraceResult.Type.ENTITY) {
      //damage entity by zero
      //drop torch
      EntityRayTraceResult entityRayTrace = (EntityRayTraceResult) result;
      Entity target = entityRayTrace.getEntity();
      if (target.isAlive()) {
        target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), MathHelper.nextInt(world.rand, 2, 6));
        if (!target.world.isRemote && !target.isBurning()
            && target instanceof LivingEntity) {
          target.attackEntityFrom(DamageSource.IN_FIRE, MathHelper.nextInt(world.rand, 3, 5));
          LivingEntity living = (LivingEntity) target;
          living.addPotionEffect(new EffectInstance(PotionRegistry.PotionEffects.stun, Const.TICKS_PER_SEC * 4, 1));
          living.setFire(MathHelper.nextInt(world.rand, 1, 5));
        }
      }
    }
    else if (type == RayTraceResult.Type.BLOCK) {
      BlockRayTraceResult ray = (BlockRayTraceResult) result;
      if (ray.getPos() == null) {
        return;
      }
      //      BlockPos pos = ray.getPos();//.offset(ray.getFace());
      //      Block blockHere = world.getBlockState(pos).getBlock();
      //      if (blockHere == Blocks.SNOW
      //          || blockHere == Blocks.SNOW_BLOCK
      //          || blockHere == Blocks.SNOW
      //          || blockHere == Blocks.ICE) {
      //        this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
      //      }
    }
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
