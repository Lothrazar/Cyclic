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
package com.lothrazar.cyclic.block;

import java.util.List;
import java.util.Random;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilParticle;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;

public class WaterCandleBlock extends BlockBase {

  private static int TICK_RATE = 50;
  private static int RADIUS = 5;
  private static double CHANCE_OFF = 0.02;
  private EntityClassification type = EntityClassification.MONSTER;
  private static final double BOUNDS = 3;
  private static final VoxelShape AABB = Block.makeCuboidShape(BOUNDS, 0, BOUNDS,
      16 - BOUNDS, 16 - BOUNDS, 16 - BOUNDS);
  private static final double CHANCE_SOUND = 0.3;

  public WaterCandleBlock(Properties properties) {
    super(properties.hardnessAndResistance(1.8F).notSolid().tickRandomly());
    this.setDefaultState(this.stateContainer.getBaseState().with(LIT, false));
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return AABB;
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
    if (!state.get(LIT)
        && player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL) {
      world.setBlockState(pos, state.with(LIT, true));
      UtilSound.playSound(world, pos, SoundEvents.BLOCK_FIRE_AMBIENT);
      return ActionResultType.SUCCESS;
    }
    else if (state.get(LIT)
        && player.getHeldItem(hand).isEmpty()) {
          //turn it off
          world.setBlockState(pos, state.with(LIT, false));
          UtilSound.playSound(world, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH);
          return ActionResultType.SUCCESS;
        }
    return ActionResultType.FAIL;
  }

  @Override
  public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (stateIn.get(LIT) && rand.nextDouble() < CHANCE_SOUND) {
      UtilSound.playSound(worldIn, pos, SoundEvents.BLOCK_FIRE_AMBIENT);
    }
    super.animateTick(stateIn, worldIn, pos, rand);
  }

  @Override
  public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
    triggerUpdate(worldIn, pos, rand);
  }

  @Override
  public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
    triggerUpdate(worldIn, pos, random);
  }

  private void triggerUpdate(World world, BlockPos pos, Random rand) {
    try {
      if (!world.isRemote && world.getBlockState(pos).get(LIT)) {
        trySpawn(world, pos, rand);
      }
    }
    catch (Exception exception) {
      ModCyclic.LOGGER.error("Error spawning monster ", exception);
    }
  }

  private void trySpawn(World world, BlockPos pos, Random rand) throws Exception {
    //if radius is 3, then go be
    float x = pos.getX() + MathHelper.nextInt(rand, -1 * RADIUS, RADIUS);
    float y = pos.getY();
    float z = pos.getZ() + MathHelper.nextInt(rand, -1 * RADIUS, RADIUS);
    BlockPos posTarget = new BlockPos(x, y, z);
    MobEntity monster = findMonsterToSpawn(world, posTarget, rand);
    if (monster == null || !world.isAirBlock(posTarget)) {
      return;//not air block
    }
    monster.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0.0F);
    //null means not from a spawner
    Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(monster, world, x, y, z, null, SpawnReason.SPAWNER);
    if (canSpawn == Event.Result.DENY || !monster.canSpawn(world, SpawnReason.SPAWNER)) {
      afterSpawnFailure(world, pos);
    }
    else if (world.addEntity(monster)) {
      afterSpawnSuccess(monster, world, pos, rand);
    }
  }

  private void afterSpawnFailure(World world, BlockPos pos) {
    world.getPendingBlockTicks().scheduleTick(pos, this, TICK_RATE);
  }

  private void afterSpawnSuccess(MobEntity monster, World world, BlockPos pos, Random rand) {
    monster.onInitialSpawn(world.getServer().getWorld(world.getDimensionKey()), world.getDifficultyForLocation(pos), SpawnReason.SPAWNER, null, null);//i hope null is ok?
    if (rand.nextDouble() < CHANCE_OFF) {
      turnOff(world, pos);
    }
    else {
      world.getPendingBlockTicks().scheduleTick(pos, this, TICK_RATE);
    }
  }

  private void turnOff(World world, BlockPos pos) {
    UtilSound.playSound(world, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH);
    UtilParticle.spawnParticle(world, ParticleTypes.SPLASH, pos, 12);
    UtilParticle.spawnParticle(world, ParticleTypes.SPLASH, pos.up(), 12);
    world.setBlockState(pos, world.getBlockState(pos).with(LIT, false));
  }

  private MobEntity findMonsterToSpawn(World world, BlockPos pos, Random rand) {
    List<MobSpawnInfo.Spawners> spawners = world.getBiome(pos).getMobSpawnInfo().getSpawners(type);
    if (spawners.isEmpty()) {
      return null;
    }
    //end is inclusive
    MobSpawnInfo.Spawners spawner = WeightedRandom.getRandomItem(rand, spawners);
    MobEntity monster = null;
    Entity ent = spawner.type.create(world);
    if (ent instanceof MobEntity)
      monster = (MobEntity) ent;
    return monster;
  }

  @Override
  public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
    if (blockState.get(LIT)) {
      return 15;
    }
    return 0;
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }
}
