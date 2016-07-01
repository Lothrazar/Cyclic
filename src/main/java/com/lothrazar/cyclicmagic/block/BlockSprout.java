package com.lothrazar.cyclicmagic.block;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSprout extends BlockCrops {
  public static final int MAX_AGE = 7;
  public static final PropertyInteger AGE = PropertyInteger.create("age", 0, MAX_AGE);
  private static final AxisAlignedBB[] AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.4375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D) };
  private static final List<ItemStack> myDrops = new ArrayList<ItemStack>();
  public BlockSprout() {
    Item[] drops = new Item[] {
        //treasure
        Items.REDSTONE, Items.GUNPOWDER, Items.GLOWSTONE_DUST, Items.DIAMOND, Items.EMERALD, 
        Items.COAL,   Items.GOLD_NUGGET, Items.IRON_INGOT, Items.GOLD_INGOT,
        Items.NETHER_STAR,Items.QUARTZ,Items.LEAD,Items.NAME_TAG,
        //mob drops
        Items.ENDER_PEARL, Items.ENDER_EYE, Items.SLIME_BALL,
        Items.BLAZE_POWDER, Items.BLAZE_ROD, Items.LEATHER,
        Items.ROTTEN_FLESH, Items.BONE, Items.STRING, Items.SPIDER_EYE,
        Items.FLINT, Items.GHAST_TEAR,
        // footstuffs
        Items.APPLE, Items.STICK, Items.SUGAR,  Items.COOKED_FISH,
        Items.CARROT, Items.POTATO, Items.BEETROOT, Items.WHEAT,Items.MELON,
        Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.WHEAT_SEEDS,
        Items.EGG,
        //random crap
        Items.COMPASS,Items.CLOCK,Items.CAULDRON,Items.COMPARATOR,Items.REPEATER,
        Items.FIRE_CHARGE,Items.POISONOUS_POTATO,
        Items.RABBIT_FOOT,Items.RABBIT_HIDE,Items.PUMPKIN_PIE,
        Items.FERMENTED_SPIDER_EYE,Items.EXPERIENCE_BOTTLE,
        Items.FLOWER_POT,Items.ITEM_FRAME,Items.PAINTING,
        Items.CAKE, Items.COOKIE, Items.SPECKLED_MELON, Items.SNOWBALL,
        Items.GLASS_BOTTLE, Items.BOOK, Items.PAPER, Items.CLAY_BALL, Items.BRICK,
        //plants
        Items.NETHER_WART, Item.getItemFromBlock(Blocks.YELLOW_FLOWER),
        Item.getItemFromBlock(Blocks.RED_MUSHROOM), Item.getItemFromBlock(Blocks.BROWN_MUSHROOM),
        Item.getItemFromBlock(Blocks.TALLGRASS), Item.getItemFromBlock(Blocks.REEDS),
        Item.getItemFromBlock(Blocks.DEADBUSH), Item.getItemFromBlock(Blocks.CACTUS),
        Item.getItemFromBlock(Blocks.VINE), Item.getItemFromBlock(Blocks.WATERLILY),
        Item.getItemFromBlock(Blocks.END_ROD),  Item.getItemFromBlock(Blocks.CHORUS_PLANT)
    };
    //metadata specific blocks
    myDrops.add(new ItemStack(Items.COAL, 1, 1));//charcoal
    myDrops.add(new ItemStack(Blocks.PUMPKIN));
    for (Item i : drops) {
      myDrops.add(new ItemStack(i));
    }
    for (EnumDyeColor  dye : EnumDyeColor.values()) {//all 16 cols
      myDrops.add(new ItemStack(Items.DYE, 1, dye.getMetadata()));
    }
    for (ItemFishFood.FishType f : ItemFishFood.FishType.values()) {
      myDrops.add(new ItemStack(Items.FISH, 1, f.getMetadata()));
    }
    for (BlockPlanks.EnumType b : BlockPlanks.EnumType.values()) {
      myDrops.add(new ItemStack(Blocks.SAPLING, 1, b.getMetadata()));
    }
    for (BlockFlower.EnumFlowerType b : BlockFlower.EnumFlowerType.values()) {
      myDrops.add(new ItemStack(Blocks.RED_FLOWER, 1, b.getMeta()));
    }
    for (BlockDoublePlant.EnumPlantType b : BlockDoublePlant.EnumPlantType.values()) {
      myDrops.add(new ItemStack(Blocks.DOUBLE_PLANT, 1, b.getMeta()));
    }
  }
  protected Item getSeed() {
    return ItemRegistry.sprout_seed;
  }
  protected Item getCrop() {
    return ItemRegistry.sprout_seed;///not used anymore. keep not null just in case
  }
  protected ItemStack getCropStack(Random rand) {
    return myDrops.get(rand.nextInt(myDrops.size()));
  }
  public ItemStack getItemStackDropped(IBlockState state, Random rand) {
    return this.isMaxAge(state) ? this.getCropStack(rand) : new ItemStack(this.getSeed());
  }
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB[((Integer) state.getValue(this.getAgeProperty())).intValue()];
  }
  @Override
  public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    //override getdrops so it never drops seeds IF its fully grown.
    java.util.List<ItemStack> ret = new ArrayList<ItemStack>();//super.getDrops(world, pos, state, fortune);
    Random rand = world instanceof World ? ((World) world).rand : new Random();
    int count = quantityDropped(state, fortune, rand);
    for (int i = 0; i < count; i++) {
      //Item item = this.getItemDropped(state, rand, fortune);
      //get item dropped just does return this.isMaxAge(state) ? this.getCrop() : this.getSeed();
      ret.add(getItemStackDropped(state, rand).copy()); //copy to make sure we return a new instance
    }
    return ret;
  }
  @Override
  public int getMaxAge() {
    return MAX_AGE;
  }
  @Override
  protected int getBonemealAgeIncrease(World worldIn) {
    return worldIn.rand.nextDouble() > 0.5 ? 1 : 0;//does nothing at zero
  }
  @Override
  public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    return true;//getBonemealAgeIncrease(worldIn) > 0;//no need to hardcode this
  }
}
