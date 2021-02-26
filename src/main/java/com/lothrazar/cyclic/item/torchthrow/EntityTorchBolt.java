package com.lothrazar.cyclic.item.torchthrow;

import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.block.BlockState;
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
    if (this.world.isRemote) {
      return;
    }
    RayTraceResult.Type type = result.getType();
    if (type == RayTraceResult.Type.ENTITY) {
      //damage entity by zero
      //drop torch
      EntityRayTraceResult entityRayTrace = (EntityRayTraceResult) result;
      Entity target = entityRayTrace.getEntity();
      if (target.isAlive()) {
        target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), 0);
      }
      UtilItemStack.drop(world, target.getPosition(), new ItemStack(Items.TORCH));
    }
    else if (type == RayTraceResult.Type.BLOCK) {
      BlockRayTraceResult bRayTrace = (BlockRayTraceResult) result;
      Direction offset = bRayTrace.getFace();
      BlockPos pos = bRayTrace.getPos().offset(offset);
      boolean itPlaced = false;
      if (world.isAirBlock(pos) || world.getBlockState(pos).getMaterial().isReplaceable()) {
        BlockState newstate = null;
        if (offset == Direction.UP || offset == Direction.DOWN) {
          newstate = Blocks.TORCH.getDefaultState();
          if (newstate.isValidPosition(world, pos)) {
            itPlaced = world.setBlockState(pos, newstate);
          }
          else {
            //HAX for making it feel better tu use, these almost never fire 
            if (newstate.isValidPosition(world, pos.down())
                && world.isAirBlock(pos.down())) {
              itPlaced = world.setBlockState(pos.down(), newstate);
            }
            else {
              if (newstate.isValidPosition(world, pos.up())
                  && world.isAirBlock(pos.up())) {
                itPlaced = world.setBlockState(pos.up(), newstate);
              }
            }
          }
        }
        else {
          BlockState testMeState = Blocks.WALL_TORCH.getDefaultState();
          for (Direction direction : Direction.values()) {
            if (direction.getAxis().isHorizontal()) {
              Direction direction1 = direction.getOpposite();
              testMeState = testMeState.with(WallTorchBlock.HORIZONTAL_FACING, direction1);
              if (testMeState.isValidPosition(world, pos)) {
                newstate = testMeState;
              }
            }
          }
          //          newstate = Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, offset);
          if (newstate != null && world.isAirBlock(pos)) {
            itPlaced = world.setBlockState(pos, newstate);
          }
        }
      }
      if (!itPlaced) {
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
