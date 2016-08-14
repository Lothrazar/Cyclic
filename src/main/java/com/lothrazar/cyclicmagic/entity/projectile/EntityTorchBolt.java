package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityTorchBolt extends EntityThrowable {
  public static Item renderSnowball;
  public EntityTorchBolt(World worldIn) {
    super(worldIn);
  }
  public EntityTorchBolt(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }
  public EntityTorchBolt(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  @Override
  protected void onImpact(RayTraceResult mop) {
    if (mop.entityHit != null) {
      //zero damage means just knockback
      mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
    }
    BlockPos pos = mop.getBlockPos();
    BlockPos offset = null;
    if (mop.sideHit != null) {
      offset = pos.offset(mop.sideHit);
    }
    if (this.isInWater() == false && offset != null && this.worldObj.isAirBlock(offset)
        && BlockTorch.FACING.getAllowedValues().contains(mop.sideHit)//&& mop.sideHit != EnumFacing.DOWN //illegal state
    ) {
      this.worldObj.setBlockState(offset,
          Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, mop.sideHit));
    }
    else {
      UtilEntity.dropItemStackInWorld(worldObj, this.getPosition(), renderSnowball);
    }
    this.setDead();
  }
}