package com.lothrazar.cyclicmagic.block;
import java.util.ArrayList;
import java.util.Random;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSprout extends BlockCrops {
  public static final int MAX_AGE = 7;
  public static final PropertyInteger AGE = PropertyInteger.create("age", 0, MAX_AGE);
  private static final AxisAlignedBB[] AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.4375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D) };
  private Random rand = new Random();
  //IDEA: different arraylists of itemstacks, and each one has a different odds of pulling from it
  private static final Item[] drops = new Item[] {
    //treasure
    Items.REDSTONE, Items.GUNPOWDER,Items.GLOWSTONE_DUST, Items.DIAMOND,Items.EMERALD,Items.COAL,
    Items.GOLD_NUGGET, Items.IRON_INGOT,Items.GOLD_INGOT,
    //mob drops
    Items.ENDER_PEARL,Items.ENDER_EYE,Items.SLIME_BALL,
    Items.BLAZE_POWDER, Items.BLAZE_ROD,Items.LEATHER,
    Items.ROTTEN_FLESH, Items.BONE, Items.STRING, Items.SPIDER_EYE, 
    Items.FLINT, Items.GHAST_TEAR,
    // footstuffs
    Items.APPLE,  Items.STICK,Items.SUGAR, Items.FISH,Items.COOKED_FISH,
    Items.CARROT,Items.POTATO,Items.BEETROOT,Items.WHEAT, 
    Items.BEETROOT_SEEDS,Items.MELON_SEEDS,Items.PUMPKIN_SEEDS,Items.WHEAT_SEEDS,
    //random crap
    Items.CAKE,Items.COOKIE,Items.SPECKLED_MELON,Items.SNOWBALL,
    Items.GLASS_BOTTLE,Items.BOOK,Items.PAPER,Items.CLAY_BALL,Items.BRICK,
    //plants
    Items.NETHER_WART,
    Item.getItemFromBlock(Blocks.RED_FLOWER),Item.getItemFromBlock(Blocks.YELLOW_FLOWER),
    Item.getItemFromBlock(Blocks.RED_MUSHROOM),Item.getItemFromBlock(Blocks.BROWN_MUSHROOM),
    Item.getItemFromBlock(Blocks.TALLGRASS),Item.getItemFromBlock(Blocks.REEDS),
    Item.getItemFromBlock(Blocks.DEADBUSH),Item.getItemFromBlock(Blocks.CACTUS)
  };
  protected Item getSeed() {
    return ItemRegistry.sprout_seed;
  }
  protected Item getCrop() {
    return drops[rand.nextInt(drops.length)];
  }
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB[((Integer) state.getValue(this.getAgeProperty())).intValue()];
  }
  @Override
  public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    //override getdrops so it never drops seeds IF its fully grown.
    //should be seed if its young
    java.util.List<ItemStack> ret = new ArrayList<ItemStack>();//super.getDrops(world, pos, state, fortune);
    Random rand = world instanceof World ? ((World) world).rand : new Random();
    int count = quantityDropped(state, fortune, rand);
    for (int i = 0; i < count; i++) {
      Item item = this.getItemDropped(state, rand, fortune);
      if (item != null) {
        ret.add(new ItemStack(item, 1, 0));//this.damageDropped(state)
      }
    }
    return ret;
  }
  @Override
  public int getMaxAge() {
    return MAX_AGE;
  }
}
