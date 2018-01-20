package com.lothrazar.cyclicmagic.component.apple;
import java.util.Random;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBase;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilOreDictionary;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAppleCrop extends BlockBase implements IGrowable, IHasRecipe {
  private static final int LIGHT_GROWTH = 5;
  private static final double BONEMEAL_CHANCE = 0.45D;
  private static final int MAX_AGE = 7;
  private static final PropertyInteger AGE = BlockCarrot.AGE;
  private static final AxisAlignedBB[] GROWING_AABB = { new AxisAlignedBB(0.25D, 0.9D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.8D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.7D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.5D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.4D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.3D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.2D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.2D, 0.25D, 0.75D, 1.0D, 0.75D) };
  private static final double CHANCE_TREASURE = 0.05;
  public BlockAppleCrop() {
    super(Material.PLANTS);
    setHardness(0.5F);
    setLightOpacity(0);
    setSoundType(SoundType.PLANT);
  }
  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
    //wont grow without this i guess?
    world.scheduleUpdate(new BlockPos(pos), this, tickRate(world));
  }
  @Override
  public void observedNeighborChange(IBlockState observerState, World world, BlockPos pos, Block changedBlock, BlockPos changedBlockPos) {
    if (canStay(world, pos) == false) {
      world.destroyBlock(pos, true);
    }
    else {
      super.observedNeighborChange(observerState, world, pos, changedBlock, changedBlockPos);
    }
  }
  @Override
  public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
    if (canStay(world, pos) == false) {
      return false;
    }
    return this.canPlaceBlockAt(world, pos);
  }
  @Override
  public int tickRate(World world) {
    return 11800;
  }
  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    if (canStay(world, pos) == false) {
      world.destroyBlock(pos, true);
    }
    else {
      grow(world, rand, pos, state);
    }
  }
  @Override
  public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
    //needs at least some light. values [0,15] ish
    return world.getLight(pos) > LIGHT_GROWTH;
  }
  @Override
  public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
    return world.rand.nextFloat() < BONEMEAL_CHANCE;
  }
  @Override
  public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
    if (canStay(world, pos) == false) {
      world.destroyBlock(pos, true);
    }
    else if (world.isRemote == false) {
      int age = state.getValue(AGE).intValue();
      if (age < MAX_AGE) {
        world.setBlockState(pos, getStateForAge(age + 1), 3);
        world.scheduleUpdate(new BlockPos(pos), this, tickRate(world));
      }
    }
  }
  private Item getCropFullGrown(Random rand) {
    
    // TODO: RANDOMLY a golden apple 
    // or a poison apple (that we need to make item for)
    //maybe emerald also!?!?!
    if (rand.nextDouble() < CHANCE_TREASURE) {
      return Items.GOLDEN_APPLE;
    }
//    if (rand.nextDouble() < CHANCE_TREASURE && Item.getByNameOrId("cyclicmagic:apple_emerald") != null) {
//      return Item.getByNameOrId("cyclicmagic:apple_emerald");
//    }
//    if (rand.nextDouble() < CHANCE_TREASURE && Item.getByNameOrId("cyclicmagic:apple_lapis") != null) {
//      return Item.getByNameOrId("cyclicmagic:apple_lapis");
//    }
    return Items.APPLE;
  }
  @Nullable
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return isMaxAge(state) ? getCropFullGrown(rand) : Item.getItemFromBlock(this);
  }
  private IBlockState getStateForAge(int age) {
    return getDefaultState().withProperty(AGE, age);
  }
  public boolean isMaxAge(IBlockState state) {
    return ((Integer) state.getValue(AGE)).intValue() >= MAX_AGE;
  }
  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getStateForAge(meta);
  }
  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(AGE).intValue();
  }
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { AGE });
  }
  //transparency stuff
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  // no collision
  @Nullable
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
    return NULL_AABB;
  }
  //bounding box growth
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return GROWING_AABB[((Integer) state.getValue(AGE)).intValue()];
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this, 2), new ItemStack(Items.APPLE), "stickWood");
  }
  private boolean canStay(World world, BlockPos pos) {
    //can only grow/survive if leaves above 
    Block blockAbove = world.getBlockState(pos.up()).getBlock();
    return UtilOreDictionary.doesMatchOreDict(new ItemStack(blockAbove), "treeLeaves");
  }
}
