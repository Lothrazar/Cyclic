package com.lothrazar.cyclic.item.torchthrow;

import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityTorchBolt extends ProjectileItemEntity {

  public EntityTorchBolt(EntityType<? extends ProjectileItemEntity> entityType, World world) {
    super(entityType, world);
  }

  public EntityTorchBolt(LivingEntity livingEntityIn, World worldIn) {
    super(EntityRegistry.torchbolt, livingEntityIn, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return Items.TORCH;
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
        target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
      }
      UtilItemStack.drop(world, target.getPosition(), new ItemStack(Items.TORCH));
      //      UtilItemStack.drop(world, pos, new ItemStack(CyclicRegistry.Items.magic_net));
    }
    else if (type == RayTraceResult.Type.BLOCK) {
      BlockRayTraceResult bRayTrace = (BlockRayTraceResult) result;
      BlockPos pos = this.getPosition();
      if (world.isAirBlock(pos)) {
        Direction offset = bRayTrace.getFace();
        //      if (offset != null) {
        //        pos = pos.offset(offset);
        //      }
        if (offset == Direction.UP)
          world.setBlockState(pos, Blocks.TORCH.getDefaultState());
        else if (offset == Direction.DOWN) //bottom of an UP block
          UtilItemStack.drop(world, pos, new ItemStack(Items.TORCH));
        else
          world.setBlockState(pos, Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, offset));
      }
      else {
        //we hit grass or a slab or something
        UtilItemStack.drop(world, this.getPosition(), new ItemStack(Items.TORCH));
      }
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
