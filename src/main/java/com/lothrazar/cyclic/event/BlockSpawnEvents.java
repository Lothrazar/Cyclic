package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.generatorexpl.BlockDestruction;
import com.lothrazar.cyclic.block.spawntriggers.BlockAltarNoTraders;
import com.lothrazar.cyclic.block.spawntriggers.CandlePeaceBlock;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.LevelWorldUtil;
import com.lothrazar.cyclic.world.cache.ServerCacheHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockSpawnEvents {

  @SubscribeEvent
  public void onExplosionEvent(ExplosionEvent.Start event) {
    Level world = event.getLevel();
    //    Entity exploder = event.getExplosion().getExploder();
    Integer radius = BlockDestruction.RADIUS.get();
    Integer height = BlockDestruction.HEIGHT.get();
    if (LevelWorldUtil.doesBlockExist(world, new BlockPos(event.getExplosion().getPosition()), BlockRegistry.ALTAR_DESTRUCTION.get().defaultBlockState(), radius, height)) {
      ModCyclic.LOGGER.info(world.isClientSide + " =clinet;Explosion cancelled " + event.getExplosion());
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public void onLivingSpawnEvent(LivingSpawnEvent.CheckSpawn event) {
    MobSpawnType res = event.getSpawnReason();
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
          && ServerCacheHolder.PEACE_CANDLE.hasCollision(mob.level, mob.blockPosition(), radius.intValue(), height.intValue())
      //          && LevelWorldUtil.doesBlockExist(mob.level, mob.blockPosition(), BlockRegistry.PEACE_CANDLE.get().defaultBlockState().setValue(BlockCyclic.LIT, true), radius, height)
      ) {
        //default range 32 and filtered
        ModCyclic.LOGGER.info(mob.blockPosition() + " Spawn cancelled by CacheCandle " + mob.getType());
        event.setResult(Result.DENY);
      }
      //next block 
      radius = BlockAltarNoTraders.RADIUS.get();
      height = BlockAltarNoTraders.HEIGHT.get();
      if (radius > 0
          && height > 0
          && BlockAltarNoTraders.isSpawnDenied(mob, res)
          && ServerCacheHolder.NO_SOLICITING.hasCollision(mob.level, mob.blockPosition(), radius.intValue(), height.intValue())
      //          && LevelWorldUtil.doesBlockExist(mob.level, mob.blockPosition(), BlockRegistry.NO_SOLICITING.get().defaultBlockState().setValue(BlockAltarNoTraders.LIT, true), radius, height)
      ) {
        ModCyclic.LOGGER.info(mob.blockPosition() + " Spawn cancelled by cache-altar " + mob.getType());
        event.setResult(Result.DENY);
      }
    }
  }
}
