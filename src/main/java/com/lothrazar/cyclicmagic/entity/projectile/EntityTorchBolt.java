package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityTorchBolt extends EntityThrowableDispensable {
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
  protected void processImpact(RayTraceResult mop) {
    if (mop.entityHit != null) {
      //zero damage means just knockback
      mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
    }
    EnumFacing sideHit = mop.sideHit;
    BlockPos pos = mop.getBlockPos();
    BlockPos offset = mop.getBlockPos();
    if (sideHit != null) {
      offset = pos.offset(sideHit);
    }
    if (offset == null) { return; }
    World world = this.getEntityWorld();
    boolean isSideSolid = world.isSideSolid(mop.getBlockPos(), sideHit);
    boolean isValidBlockstate = BlockTorch.FACING.getAllowedValues().contains(sideHit);
    boolean isValidLocation = world.isAirBlock(offset) ||
        world.getBlockState(offset) == null ||
        world.getBlockState(offset).getBlock() == null ||
        world.getBlockState(offset).getBlock().isReplaceable(world, offset);
    if (isValidLocation && isValidBlockstate && isSideSolid && world.isRemote == false) {
      world.setBlockState(offset, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, sideHit));
    }
    else {
      UtilItemStack.dropItemStackInWorld(world, this.getPosition(), renderSnowball);
    }
    this.setDead();
  }
}