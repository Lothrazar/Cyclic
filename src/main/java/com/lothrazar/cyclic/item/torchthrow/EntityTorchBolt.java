package com.lothrazar.cyclic.item.torchthrow;

import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class EntityTorchBolt extends ThrowableItemProjectile {

  public EntityTorchBolt(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public EntityTorchBolt(LivingEntity livingEntityIn, Level worldIn) {
    super(EntityRegistry.TORCH_BOLT.get(), livingEntityIn, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return Items.TORCH;
  }

  @Override
  protected void onHit(HitResult result) {
    Level level = this.level();
    if (level.isClientSide) {
      return;
    }
    HitResult.Type type = result.getType();
    if (type == HitResult.Type.ENTITY) {
      //damage entity by zero
      //drop torch
      EntityHitResult entityRayTrace = (EntityHitResult) result;
      Entity target = entityRayTrace.getEntity();
      if (target.isAlive()) {
        target.hurt(level().damageSources().thrown(this, this.getOwner()), 0);
      }
      ItemStackUtil.drop(level(), target.blockPosition(), new ItemStack(Items.TORCH));
    }
    else if (type == HitResult.Type.BLOCK) {
      BlockHitResult bRayTrace = (BlockHitResult) result;
      Direction offset = bRayTrace.getDirection();
      BlockPos pos = bRayTrace.getBlockPos().relative(offset);
      boolean itPlaced = false;
      if (level.isEmptyBlock(pos) || level.getBlockState(pos).canBeReplaced()) {
        BlockState newstate = null;
        if (offset == Direction.UP || offset == Direction.DOWN) {
          newstate = Blocks.TORCH.defaultBlockState();
          if (newstate.canSurvive(level, pos)) {
            itPlaced = level.setBlockAndUpdate(pos, newstate);
          }
          else {
            //HAX for making it feel better tu use, these almost never fire 
            if (newstate.canSurvive(level, pos.below())
                && level.isEmptyBlock(pos.below())) {
              itPlaced = level.setBlockAndUpdate(pos.below(), newstate);
            }
            else {
              if (newstate.canSurvive(level, pos.above())
                  && level.isEmptyBlock(pos.above())) {
                itPlaced = level.setBlockAndUpdate(pos.above(), newstate);
              }
            }
          }
        }
        else {
          BlockState testMeState = Blocks.WALL_TORCH.defaultBlockState();
          for (Direction direction : Direction.values()) {
            if (direction.getAxis().isHorizontal()) {
              Direction direction1 = direction.getOpposite();
              testMeState = testMeState.setValue(WallTorchBlock.FACING, direction1);
              if (testMeState.canSurvive(level, pos)) {
                newstate = testMeState;
              }
            }
          }
          //          newstate = Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, offset);
          if (newstate != null && level.isEmptyBlock(pos)) {
            itPlaced = level.setBlockAndUpdate(pos, newstate);
          }
        }
      }
      if (!itPlaced) {
        //we hit grass or a slab or something
        ItemStackUtil.drop(level, this.blockPosition(), new ItemStack(Items.TORCH));
      }
    }
    this.remove(RemovalReason.DISCARDED);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
  }

  @Override
  public Packet<ClientGamePacketListener> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
