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
package com.lothrazar.cyclic.block.spawntriggers;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.ParticleUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class CandlePeaceBlock extends BlockCyclic {

  public static IntValue HEIGHT;
  public static IntValue RADIUS;
  private static final double BOUNDS = 3;
  public static final VoxelShape AABB = Block.box(BOUNDS, 0, BOUNDS,
      16 - BOUNDS, 16 - BOUNDS, 16 - BOUNDS);
  private static final double CHANCE_SOUND = 0.3;

  public CandlePeaceBlock(Properties properties) {
    super(properties.strength(1.8F).noOcclusion().randomTicks());
    this.registerDefaultState(this.defaultBlockState().setValue(LIT, true));
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.PEACE_CANDLE.get(), world.isClientSide ? TilePeace::clientTick : TilePeace::serverTick);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos p, BlockState st) {
    return new TilePeace(p, st);
  }

  /**
   * first check spawn type: Natural = as expected
   * 
   * REINFORCEMENT = zombie calls and stuff
   * 
   * EVENT = villager raids etc
   * 
   * and check the creature if it implements Enemy interface
   * 
   * or is not friendly (category Monster)
   * 
   * or as a bonus nuke wandering traders too
   */
  public static boolean isBad(LivingEntity mob, MobSpawnType res) {
    MobCategory type = mob.getClassification(false);
    if (mob instanceof Enemy ||
        !type.isFriendly() || // basically only MONSTER is friendly
        mob instanceof WanderingTrader || mob instanceof TraderLlama) {
      return true;
    }
    return false;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return AABB;
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
    boolean old = state.getValue(LIT);
    world.setBlockAndUpdate(pos, state.setValue(LIT, !old));
    SoundUtil.playSound(world, pos, old ? SoundEvents.FIRE_EXTINGUISH : SoundEvents.FIRE_AMBIENT);
    ParticleUtil.spawnParticle(world, ParticleTypes.SPLASH, pos.above(), 12);
    return InteractionResult.SUCCESS;
  }

  @Override
  public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
    if (stateIn.getValue(LIT) && rand.nextDouble() < CHANCE_SOUND) {
      SoundUtil.playSound(worldIn, pos, SoundEvents.FIRE_AMBIENT);
    }
    super.animateTick(stateIn, worldIn, pos, rand);
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
