package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityShearingBolt extends EntityThrowableDispensable {
  public static final FactoryShear FACTORY = new FactoryShear();
  public static class FactoryShear implements IRenderFactory<EntityShearingBolt> {
    @Override
    public Render<? super EntityShearingBolt> createRenderFor(RenderManager rm) {
      return new RenderBall<EntityShearingBolt>(rm, "shears");
    }
  }
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
  protected void processImpact(RayTraceResult mop) {
    World world = getEntityWorld();
    if (mop.entityHit != null && mop.entityHit instanceof EntitySheep) {
      try {
        EntitySheep sheep = (EntitySheep) mop.entityHit;
        // imported from MY unreleased/abandoned BlockShearWool.java in./FarmingBlocks/// fleece colour might be null? maybe causing bug #120
        //if this sheep is not sheared, AND ->  either an adult, or child that passes config
        if (sheep.getSheared() == false && sheep.getFleeceColor() != null &&
            (sheep.isChild() == false || (EntityShearingBolt.doesShearChild == true && sheep.isChild() == true))) {
          if (world.isRemote == false) {
            sheep.setSheared(true);
            int i = 2 + world.rand.nextInt(6);
            for (int j = 0; j < i; ++j) {
              EntityItem entityitem = sheep.entityDropItem(new ItemStack(Blocks.WOOL, 1, sheep.getFleeceColor().getMetadata()), 1.0F);
              entityitem.motionY += (double) (world.rand.nextFloat() * 0.05F);
              entityitem.motionX += (double) ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F);
              entityitem.motionZ += (double) ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F);
            }
          }
          this.setDead();
          UtilSound.playSound(world, sheep.getPosition(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.NEUTRAL);
        }
      }
      catch (Exception e) {
        // https://github.com/PrinceOfAmber/Cyclic/issues/120
        ModCyclic.logger.error(e.getMessage());
      }
    }
    if (this.isDead || mop.getBlockPos() == null) { return; }
    BlockPos pos = mop.getBlockPos();
    //    if (sideHit != null) {
    //      pos = mop.getBlockPos().offset(sideHit);
    //    }
    Block block = world.getBlockState(pos).getBlock();
    if (block instanceof net.minecraftforge.common.IShearable) {
      net.minecraftforge.common.IShearable target = (net.minecraftforge.common.IShearable) block;
      if (target.isShearable(null, world, pos)) {
        java.util.List<ItemStack> drops = target.onSheared(null, world, pos, 1);
        for (ItemStack stack : drops) {
          float f = 0.7F;
          double d = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          double d1 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          double d2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          net.minecraft.entity.item.EntityItem entityitem = new net.minecraft.entity.item.EntityItem(world, (double) pos.getX() + d, (double) pos.getY() + d1, (double) pos.getZ() + d2, stack);
          // entityitem.setDefaultPickupDelay();
          if (world.isRemote == false) {
            world.spawnEntity(entityitem);
            world.setBlockToAir(pos);
          }
        }
        this.setDead();
      }
    }
  }
}
