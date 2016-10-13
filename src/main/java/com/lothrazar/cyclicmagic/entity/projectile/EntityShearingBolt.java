package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.module.MobDropChangesModule;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityShearingBolt extends EntityThrowable {
  public static Item renderSnowball;
  public static boolean doesShearChild;
  public EntityShearingBolt(World worldIn) {
    super(worldIn);
  }
  public EntityShearingBolt(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }
  public EntityShearingBolt(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  @Override
  protected void onImpact(RayTraceResult mop) {
    if (mop.entityHit != null && mop.entityHit instanceof EntitySheep) {
      try {
        EntitySheep sheep = (EntitySheep) mop.entityHit;
        // imported from MY unreleased/abandoned BlockShearWool.java in./FarmingBlocks/// fleece colour might be null? maybe causing bug #120
        //if this sheep is not sheared, AND ->  either an adult, or child that passes config
        if (sheep.getSheared() == false && sheep.getFleeceColor() != null &&
            (sheep.isChild() == false || (EntityShearingBolt.doesShearChild == true && sheep.isChild() == true))) {
          if (worldObj.isRemote == false) {
            sheep.setSheared(true);
            int i = 1 + worldObj.rand.nextInt(3);
            if (MobDropChangesModule.sheepShearBuffed) {
              i += MathHelper.getRandomIntegerInRange(worldObj.rand, 1, 6);
            }
            for (int j = 0; j < i; ++j) {
              EntityItem entityitem = sheep.entityDropItem(new ItemStack(Blocks.WOOL, 1, sheep.getFleeceColor().getMetadata()), 1.0F);
              entityitem.motionY += (double) (worldObj.rand.nextFloat() * 0.05F);
              entityitem.motionX += (double) ((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.1F);
              entityitem.motionZ += (double) ((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.1F);
            }
          }
          UtilSound.playSound(worldObj, sheep.getPosition(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.NEUTRAL);
        }
        else {
          BlockPos posToDrop = getPosToDrop(mop);
          if (posToDrop != null)
            UtilEntity.dropItemStackInWorld(worldObj, posToDrop, renderSnowball);
        }
      }
      catch (Exception e) {
        // https://github.com/PrinceOfAmber/Cyclic/issues/120
        ModMain.logger.error(e.getMessage());
      }
    }
    else {
      BlockPos posToDrop = getPosToDrop(mop);
      if (posToDrop != null)
        UtilEntity.dropItemStackInWorld(worldObj, posToDrop, renderSnowball);
    }
    this.setDead();
  }
  private BlockPos getPosToDrop(RayTraceResult mop) {
    BlockPos pos = mop.getBlockPos();
    if (pos == null && mop.entityHit != null) {
      pos = mop.entityHit.getPosition();
    }
    return pos;
  }
}
