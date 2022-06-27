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

import java.util.Random;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.util.UtilParticle;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;

public class CandleWaterBlock extends BlockCyclic {

  public static IntValue TICK_RATE;
  public static IntValue RADIUS;
  private MobCategory type = MobCategory.MONSTER;
  private static final double BOUNDS = 3;
  private static final VoxelShape AABB = Block.box(BOUNDS, 0, BOUNDS,
      16 - BOUNDS, 16 - BOUNDS, 16 - BOUNDS);
  private static final double CHANCE_SOUND = 0.3;

  public CandleWaterBlock(Properties properties) {
    super(properties.strength(1.8F).noOcclusion().randomTicks());
    this.registerDefaultState(this.defaultBlockState().setValue(LIT, true));
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return AABB;
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
    boolean old = state.getValue(LIT);
    world.setBlockAndUpdate(pos, state.setValue(LIT, !old));
    UtilSound.playSound(world, pos, old ? SoundEvents.FIRE_EXTINGUISH : SoundEvents.FIRE_AMBIENT);
    UtilParticle.spawnParticle(world, ParticleTypes.SPLASH, pos.above(), 12);
    return InteractionResult.SUCCESS;
  }

  @Override
  public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
    if (stateIn.getValue(LIT) && rand.nextDouble() < CHANCE_SOUND) {
      UtilSound.playSound(worldIn, pos, SoundEvents.FIRE_AMBIENT);
    }
    super.animateTick(stateIn, worldIn, pos, rand);
  }

  @Override
  public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
    triggerUpdate(worldIn, pos, rand);
  }

  @Override
  public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
    triggerUpdate(worldIn, pos, random);
  }

  private void triggerUpdate(Level world, BlockPos pos, Random rand) {
    try {
      if (!world.isClientSide && world.getBlockState(pos).getValue(LIT)) {
        trySpawn(world, pos, rand);
      }
    }
    catch (Exception exception) {
      ModCyclic.LOGGER.error("Error spawning monster ", exception);
    }
  }

  private void trySpawn(Level world, BlockPos pos, Random rand) throws Exception {
    //if radius is 3, then go be
    float x = pos.getX() + Mth.nextInt(rand, -1 * RADIUS.get(), RADIUS.get());
    float y = pos.getY();
    float z = pos.getZ() + Mth.nextInt(rand, -1 * RADIUS.get(), RADIUS.get());
    BlockPos posTarget = new BlockPos(x, y, z);
    Mob monster = findMonsterToSpawn(world, posTarget, rand);
    if (monster == null || !world.isEmptyBlock(posTarget)) {
      return;
    }
    monster.moveTo(x, y, z, world.random.nextFloat() * 360.0F, 0.0F);
    //null means not from a spawner
    Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(monster, world, x, y, z, null, MobSpawnType.SPAWNER);
    if (canSpawn == Event.Result.DENY || !monster.checkSpawnRules(world, MobSpawnType.SPAWNER)) {
      afterSpawnFailure(world, pos);
    }
    else if (world.addFreshEntity(monster)) {
      afterSpawnSuccess(monster, world, pos, rand);
    }
  }

  private void afterSpawnFailure(Level world, BlockPos pos) {
    world.scheduleTick(pos, this, TICK_RATE.get());
  }

  private void afterSpawnSuccess(Mob monster, Level world, BlockPos pos, Random rand) {
    monster.finalizeSpawn(world.getServer().getLevel(world.dimension()), world.getCurrentDifficultyAt(pos), MobSpawnType.SPAWNER, null, null);
    world.scheduleTick(pos, this, TICK_RATE.get());
  }

  private Mob findMonsterToSpawn(Level world, BlockPos pos, Random rand) {
    //    world.getBiome(pos)
    WeightedRandomList<MobSpawnSettings.SpawnerData> spawners = world.getBiome(pos).value().getMobSettings().getMobs(type);
    if (spawners.isEmpty()) {
      return null;
    }
    //end is inclusive
    MobSpawnSettings.SpawnerData spawner = spawners.getRandom(rand).orElse(null);
    if (spawner == null) {
      return null;
    }
    Mob monster = null;
    Entity ent = spawner.type.create(world);
    if (ent instanceof Mob) {
      monster = (Mob) ent;
    }
    return monster;
  }

  @Override
  public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    if (blockState.getValue(LIT)) {
      return 15;
    }
    return 0;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }
}
