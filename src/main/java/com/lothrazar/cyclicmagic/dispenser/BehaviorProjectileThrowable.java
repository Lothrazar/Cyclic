package com.lothrazar.cyclicmagic.dispenser;
import java.util.Random;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.item.projectile.BaseItemProjectile;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BehaviorProjectileThrowable extends BehaviorProjectileDispense {
  BaseItemProjectile throwable;
  public BehaviorProjectileThrowable(BaseItemProjectile item) {
    throwable = item;
  }
  @Override
  protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
    EntityThrowableDispensable thrown = throwable.getThrownEntity(worldIn, position.getX(), position.getY(), position.getZ());
    return thrown;
  }
  //  /**
  //   * Dispense the specified stack, play the dispense sound and spawn particles.
  //   * IMPORTED FROM     BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FIRE_CHARGE, new BehaviorDefaultDispenseItem()
  //   */
  //  public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
  //  {
  //      EnumFacing enumfacing = (EnumFacing)source.func_189992_e().getValue(BlockDispenser.FACING);
  //      IPosition iposition = BlockDispenser.getDispensePosition(source);
  //      double d0 = iposition.getX() + (double)((float)enumfacing.getFrontOffsetX() * 0.3F);
  //      double d1 = iposition.getY() + (double)((float)enumfacing.getFrontOffsetY() * 0.3F);
  //      double d2 = iposition.getZ() + (double)((float)enumfacing.getFrontOffsetZ() * 0.3F);
  //      World world = source.getWorld();
  //      Random random = world.rand;
  //      double d3 = random.nextGaussian() * 0.05D + (double)enumfacing.getFrontOffsetX();
  //      double d4 = random.nextGaussian() * 0.05D + (double)enumfacing.getFrontOffsetY();
  //      double d5 = random.nextGaussian() * 0.05D + (double)enumfacing.getFrontOffsetZ();
  //      EntityThrowableDispensable thrown = throwable.getThrownEntity(world, d0, d1, d2);
  //
  //      world.spawnEntityInWorld(new EntitySmallFireball(world, d0, d1, d2, d3, d4, d5));
  //      
  //      stack.splitStack(1);
  //      return stack;
  //  }
  //  /**
  //   * Play the dispense sound from the specified block.
  //   */
  //  protected void playDispenseSound(IBlockSource source)
  //  {
  //      source.getWorld().playEvent(1018, source.getBlockPos(), 0);
  //  }
}
