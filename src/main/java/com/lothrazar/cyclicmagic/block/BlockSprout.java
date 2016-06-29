package com.lothrazar.cyclicmagic.block;
import java.util.ArrayList;
import java.util.Random;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSprout extends BlockCrops {
  public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
  private static final AxisAlignedBB[] AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.4375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D) };
  private Random rand = new Random();
  private static final Item[] drops = new Item[] { Items.REDSTONE, Items.GUNPOWDER,Items.ENDER_PEARL,Items.BLAZE_POWDER,Items.ROTTEN_FLESH,Items.BONE,Items.STRING,Items.SPIDER_EYE,Items.SUGAR,Items.WHEAT,Items.FLINT,Items.BRICK,Items.CLAY_BALL,Items.COAL,Items.GOLD_NUGGET,Items.APPLE,Items.EMERALD,Items.STICK };
  protected Item getSeed() {
    return ItemRegistry.sprout_seed;
  }
  protected Item getCrop() {
    return drops[rand.nextInt(drops.length)];
  }
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB[((Integer) state.getValue(this.getAgeProperty())).intValue()];
  }
}
