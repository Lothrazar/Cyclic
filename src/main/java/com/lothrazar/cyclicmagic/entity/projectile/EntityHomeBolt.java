package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityHomeBolt extends EntityThrowable {
  public EntityHomeBolt(World worldIn) {
    super(worldIn);
  }
  public EntityHomeBolt(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }
  public EntityHomeBolt(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  @Override
  protected void onImpact(RayTraceResult mop) {
    if (this.getThrower() != null && this.getThrower() instanceof EntityPlayer && this.getThrower().dimension == 0) {
      EntityPlayer player = (EntityPlayer) this.getThrower();
      BlockPos realBedPos = getBedLocationSafe(worldObj, player);
      if (realBedPos == null) {
        realBedPos = worldObj.getSpawnPoint();
      }
  
      UtilEntity.teleportWallSafe(player, worldObj, realBedPos);
      UtilSound.playSound(worldObj, realBedPos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS);
      this.setDead();
    }
  }
  public static BlockPos getBedLocationSafe(World world, EntityPlayer player) {
    BlockPos realBedPos = null;
    BlockPos coords = player.getBedLocation(0);
    if (coords != null) {
      IBlockState state = world.getBlockState(coords);
      Block block = state.getBlock();
      if (block.equals(Blocks.BED) || block.isBed(state, world, coords, player)) {
        // then move over according to how/where the bed wants me to spawn
        realBedPos = block.getBedSpawnPosition(state, world, coords, player);
      }
    }
    return realBedPos;
  }
}
