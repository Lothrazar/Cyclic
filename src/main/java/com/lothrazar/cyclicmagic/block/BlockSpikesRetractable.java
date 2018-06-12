/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.block;

import java.lang.ref.WeakReference;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBase;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilFakePlayer;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.FakePlayer;

public class BlockSpikesRetractable extends BlockBase implements IHasRecipe, IHasConfig {

  private static final PropertyBool ACTIVATED = PropertyBool.create("activated");
  private static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
  private static final float LARGE = 0.9375F;
  private static final float SMALL = 0.0625F;
  private static final AxisAlignedBB NORTH_BOX = new AxisAlignedBB(0.0F, 0.0F, LARGE, 1.0F, 1.0F, 1.0F);
  private static final AxisAlignedBB EAST_BOX = new AxisAlignedBB(0.0F, 0.0F, 0.0F, SMALL, 1.0F, 1.0F);
  private static final AxisAlignedBB SOUTH_BOX = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, SMALL);
  private static final AxisAlignedBB WEST_BOX = new AxisAlignedBB(LARGE, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  private static final AxisAlignedBB UP_BOX = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, SMALL, 1.0F);
  private static final AxisAlignedBB DOWN_BOX = new AxisAlignedBB(0.0F, LARGE, 0.0F, 1.0F, 1.0F, 1.0F);
  private static WeakReference<FakePlayer> fakePlayer;
  private static UUID uuid;
  private boolean doesPlayerDamage;
  private int damageIron = 1;
  private int damageDiamond = 2;

  public BlockSpikesRetractable(boolean doesPlayer) {
    super(Material.IRON);
    setHardness(1.5F);
    setResistance(10F);
    this.setTranslucent();
    this.doesPlayerDamage = doesPlayer;
  }

  //copy vanilla methods: 8 facing directions bitwise-combined with enabled or not
  public static EnumFacing getFacing(int meta) {
    return EnumFacing.getFront(meta & 7);
  }

  public static EnumFacing getFacing(IBlockState state) {
    return state.getValue(FACING);
  }

  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return side == EnumFacing.DOWN;//super.isSideSolid(base_state, world, pos, side);
  }

  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
  //  @Override
  //  private boolean canPlaceOn(World worldIn, BlockPos pos)
  //  {
  //      IBlockState state = worldIn.getBlockState(pos);
  //      return state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
  //  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(ACTIVATED, (meta & 8) > 0);
  }

  private int getDamage() {
    return (this.doesPlayerDamage) ? this.damageDiamond : this.damageIron;
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    byte b0 = 0;
    int i = b0 | state.getValue(FACING).getIndex();
    if (state.getValue(ACTIVATED)) {
      i |= 8;
    }
    return i;
  }

  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
    if (entity instanceof EntityLivingBase && worldIn.getBlockState(pos).getValue(ACTIVATED)) {
      if (this.doesPlayerDamage) {
        if (worldIn instanceof WorldServer) {
          if (uuid == null) {
            uuid = UUID.randomUUID();
          }
          if (fakePlayer == null) {
            fakePlayer = UtilFakePlayer.initFakePlayer((WorldServer) worldIn, uuid, "spikes");
            if (fakePlayer == null) {
              ModCyclic.logger.error("Fake player failed to init ");
              return;
            }
          }
          entity.attackEntityFrom(DamageSource.causePlayerDamage(fakePlayer.get()), getDamage());
        }
      }
      else {
        entity.attackEntityFrom(DamageSource.CACTUS, getDamage());
      }
    }
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { FACING, ACTIVATED });
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    if (state.getValue(ACTIVATED)) {
      return FULL_BLOCK_AABB;
    }
    EnumFacing enumfacing = state.getValue(FACING);
    switch (enumfacing) {
      case NORTH:
        return NORTH_BOX;
      case EAST:
        return EAST_BOX;
      case SOUTH:
        return SOUTH_BOX;
      case WEST:
        return WEST_BOX;
      case UP:
        return UP_BOX;
      case DOWN:
        return DOWN_BOX;
    }
    return FULL_BLOCK_AABB;//CANT BE NULL, causes crashes.   
  }

  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos) {
    return NULL_AABB;
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
    EnumFacing facState = getFacing(state);
    if (this.canPlaceBlockAt(world, pos) == false
        || world.isSideSolid(pos.offset(facState.getOpposite()), facState, true) == false) {
      if (world.setBlockToAir(pos)) {
        dropBlockAsItem(world, pos, getDefaultState(), 0);
        return;
      }
    }
    if (state.getValue(ACTIVATED) == false
        && world.isBlockPowered(pos)) {
      UtilSound.playSoundFromServer(SoundRegistry.spikes_on, SoundCategory.BLOCKS, pos, world.provider.getDimension(), 16);
      world.setBlockState(pos, state.withProperty(ACTIVATED, true));
    }
    else if (state.getValue(ACTIVATED)
        && world.isBlockPowered(pos) == false) {
      UtilSound.playSoundFromServer(SoundRegistry.spikes_off, SoundCategory.BLOCKS, pos, world.provider.getDimension(), 16);
      world.setBlockState(pos, state.withProperty(ACTIVATED, false));
    }
  }

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
    this.neighborChanged(state, worldIn, pos, this, pos);
  }

  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase player, EnumHand hand) {
    return worldIn.isSideSolid(pos.offset(facing.getOpposite()), facing, true) ? this.getDefaultState().withProperty(FACING, facing).withProperty(ACTIVATED, false) : this.getDefaultState().withProperty(FACING, EnumFacing.DOWN).withProperty(ACTIVATED, false);
  }

  @Override
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
    return worldIn.isSideSolid(pos.offset(side.getOpposite()), side, true);//only place on solid placements
  }

  @Override
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    for (EnumFacing fac : EnumFacing.values()) {
      if (worldIn.isSideSolid(pos.offset(fac), fac.getOpposite(), true)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public IRecipe addRecipe() {
    if (this.doesPlayerDamage) {
      return RecipeRegistry.addShapedRecipe(new ItemStack(this, 2),
          " s ",
          "s s",
          "ttt",
          's', Items.DIAMOND,
          't', "blockIron");
    }
    else {
      return RecipeRegistry.addShapedRecipe(new ItemStack(this, 2),
          " s ",
          "s s",
          "ttt",
          's', Blocks.IRON_BARS,
          't', Blocks.STONE_PRESSURE_PLATE);
    }
  }

  @Override
  public void syncConfig(Configuration config) {
    this.damageIron = config.getInt("SpikeIronDamage", Const.ConfigCategory.modpackMisc, 1, 1, 99, "Damage per second of iron spikes");
    this.damageDiamond = config.getInt("SpikeDiamondDamage", Const.ConfigCategory.modpackMisc, 5, 1, 99, "Damage per second of iron spikes");
  }
}
