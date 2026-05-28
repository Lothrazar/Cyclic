package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.generatorexpl.BlockDestruction;
import com.lothrazar.cyclic.block.spawntriggers.BlockAltarNoTraders;
import com.lothrazar.cyclic.block.spawntriggers.CandlePeaceBlock;
import com.lothrazar.cyclic.cache.ServerCacheHolder;
import com.lothrazar.cyclic.item.equipment.MattockItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.library.util.LevelWorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

import java.util.List;

public class BlockSpawnEvents {

  @SubscribeEvent
  public void onExplosionEvent(ExplosionEvent.Start event) {
/*
    Level world = event.getLevel();
    //    Entity exploder = event.getExplosion().getExploder();
    Integer radius = BlockDestruction.RADIUS.get();
    Integer height = BlockDestruction.HEIGHT.get();
    Vec3 thanksMojang = event.getExplosion().getPosition();
    if (LevelWorldUtil.doesBlockExist(world, new BlockPos((int) thanksMojang.x, (int) thanksMojang.y, (int) thanksMojang.z), BlockRegistry.ALTAR_DESTRUCTION.get().defaultBlockState(), radius, height)) {
      ModCyclic.LOGGER.info(world.isClientSide + " =clinet;Explosion cancelled " + event.getExplosion());
      event.setCanceled(true);
    }
*/  }

  @SubscribeEvent
  public void onLivingSpawnEvent(FinalizeSpawnEvent event) {
    MobSpawnType res = event.getSpawnType();
    if (res == MobSpawnType.NATURAL ||
        res == MobSpawnType.REINFORCEMENT ||
        res == MobSpawnType.EVENT) {
      //for these event types only
      Integer radius = CandlePeaceBlock.RADIUS.get();
      Integer height = CandlePeaceBlock.HEIGHT.get();
      LivingEntity mob = event.getEntity();
      //first block candle peace
      if (radius > 0
          && height > 0
          && CandlePeaceBlock.isBad(mob, res)
          && ServerCacheHolder.PEACE_CANDLE.hasCollision(mob.level(), mob.blockPosition(), radius.intValue(), height.intValue())
      //          && LevelWorldUtil.doesBlockExist(mob.level, mob.blockPosition(), BlockRegistry.PEACE_CANDLE.get().defaultBlockState().setValue(BlockCyclic.LIT, true), radius, height)
      ) {
        //default range 32 and filtered
        ModCyclic.LOGGER.info(mob.blockPosition() + " Spawn cancelled by CacheCandle " + mob.getType());
        event.setSpawnCancelled(true);
      }
      //next block
      radius = BlockAltarNoTraders.RADIUS.get();
      height = BlockAltarNoTraders.HEIGHT.get();
      if (radius > 0
          && height > 0
          && BlockAltarNoTraders.isSpawnDenied(mob, res)
          && ServerCacheHolder.NO_SOLICITING.hasCollision(mob.level(), mob.blockPosition(), radius.intValue(), height.intValue())
      //          && LevelWorldUtil.doesBlockExist(mob.level, mob.blockPosition(), BlockRegistry.NO_SOLICITING.get().defaultBlockState().setValue(BlockAltarNoTraders.LIT, true), radius, height)
      ) {
        ModCyclic.LOGGER.info(mob.blockPosition() + " Spawn cancelled by cache-altar " + mob.getType());
        event.setSpawnCancelled(true);
      }
    }
  }

  @SubscribeEvent
  public void onBlockBreak(BlockEvent.BreakEvent event) {
    Player player = event.getPlayer();
    ItemStack stack = player.getMainHandItem();

    if (stack.getItem() instanceof MattockItem mattock) {
      Level level = (Level) event.getLevel();
      BlockPos origin = event.getPos();

      Direction direction = null;
      BlockHitResult ray = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE); // ray is never null
      if (ray.getType() == HitResult.Type.BLOCK) {
        direction = ray.getDirection();
      }

      List<BlockPos> toBreak = mattock.getShape(origin, player.isCrouching(), direction);
      for (BlockPos pos : toBreak) {
        if (pos.equals(origin)) continue; // Already handled by default

        BlockState state = level.getBlockState(pos);
        if (state.isAir()) continue;
        if (mattock.getDestroySpeed(stack, state) < 0) continue; // The block is unbreakable
        if (!state.canHarvestBlock(level, pos, player)) continue;

        state.getBlock().destroy(level, pos, state); // Sound, particles, etc.
        Block.dropResources(state, level, pos, level.getBlockEntity(pos), player, stack); // Drops resources while accounting for enchants
        level.removeBlock(pos, false); // Actually removes the block from the world
        stack.mineBlock(level, state, pos, player); // Damages the tool
      }
    }
  }
}
