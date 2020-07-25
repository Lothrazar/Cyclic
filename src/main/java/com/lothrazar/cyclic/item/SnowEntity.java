package com.lothrazar.cyclic.item;

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

public class SnowEntity extends ProjectileItemEntity {

  public SnowEntity(EntityType<? extends ProjectileItemEntity> entityType, World world) {
    super(entityType, world);
  }

  public SnowEntity(LivingEntity livingEntityIn, World worldIn) {
    super(EntityRegistry.snowbolt, livingEntityIn, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return Items.SNOWBALL;
  }

  @Override
  protected void onImpact(RayTraceResult result) {
    RayTraceResult.Type type = result.getType();
    if (type == RayTraceResult.Type.ENTITY) {
      //damage entity by zero
      //drop torch
      EntityRayTraceResult entityRayTrace = (EntityRayTraceResult) result;
      Entity target = entityRayTrace.getEntity();
      if (target.isAlive() && target instanceof LivingEntity) {
        target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), MathHelper.nextInt(world.rand, 2, 5));
        target.attackEntityFrom(DamageSource.DRYOUT, MathHelper.nextInt(world.rand, 2, 3));
        LivingEntity living = (LivingEntity) target;
        living.addPotionEffect(new EffectInstance(PotionRegistry.PotionEffects.stun, 60, 1));
        //        if (world.isAirBlock(living.getPosition()))
        //          this.world.setBlockState(living.getPosition(), Blocks.SNOW.getDefaultState());
      }
    }
    else if (type == RayTraceResult.Type.BLOCK) {
      BlockRayTraceResult ray = (BlockRayTraceResult) result;
      if (ray.getPos() == null || ray.getFace() == null) {
        return;
      }
      //      BlockPos pos = ray.getPos().offset(ray.getFace());
      //      if (world.isAirBlock(pos))
      //        this.world.setBlockState(pos, Blocks.SNOW.getDefaultState());
      //      else {
      //        BlockState here = world.getBlockState(ray.getPos());
      //        if (here.getBlock() == Blocks.SNOW) {
      //          //inc
      //          int newy = here.get(SnowBlock.LAYERS).intValue() + 1;
      //          world.setBlockState(ray.getPos(), here.with(SnowBlock.LAYERS, newy));
      //          //
      //        }
      //        here = world.getBlockState(pos);
      //        if (here.getBlock() == Blocks.WATER) {
      //          if (world.rand.nextDouble() < 0.25)
      //            world.setBlockState(pos, Blocks.BLUE_ICE.getDefaultState());
      //          else
      //            world.setBlockState(pos, Blocks.PACKED_ICE.getDefaultState());
      //        }
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
