package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.BlockNoTraders;
import com.lothrazar.cyclic.block.CandlePeaceBlock;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockSpawnEvents {

  @SubscribeEvent
  public void onExplosionEvent(ExplosionEvent.Start event) {
    //TODO: remake this idea
    //    Level world = event.getWorld();
    //    Entity exploder = event.getExplosion().getExploder();
    //    Integer radius = CandlePeaceBlock.RADIUS.get();
    //    Integer height = CandlePeaceBlock.HEIGHT.get();
    //    if (exploder.getType() == EntityType.CREEPER
    //        || exploder instanceof Creeper) {
    //      // what about any creeper at all
    //      //      Creeper test;
    //      if (UtilWorld.doesBlockExist(world, new BlockPos(event.getExplosion().getPosition()),
    //          BlockRegistry.ALTAR_DESTRUCTION.get().defaultBlockState().setValue(BlockCyclic.LIT, true),
    //          radius, height)) {
    //        //
    //        event.setCanceled(true);
    //      }
    //    }
  }

  // Peace Candle handler
  @SubscribeEvent
  public void onLivingSpawnEvent(LivingSpawnEvent.CheckSpawn event) {
    MobSpawnType res = event.getSpawnReason();
    if (res == MobSpawnType.NATURAL ||
        res == MobSpawnType.REINFORCEMENT ||
        res == MobSpawnType.EVENT) {
      //for these event types only
      Integer radius = CandlePeaceBlock.RADIUS.get();
      Integer height = CandlePeaceBlock.HEIGHT.get();
      LivingEntity mob = event.getEntityLiving();
      if (CandlePeaceBlock.isBad(mob, res)
          && UtilWorld.doesBlockExist(mob.level, mob.blockPosition(),
              BlockRegistry.PEACE_CANDLE.get().defaultBlockState().setValue(BlockCyclic.LIT, true),
              radius, height)) {
        //default range 32 and filtered
        ModCyclic.LOGGER.info(mob.blockPosition() + " Spawn cancelled by candle " + mob.getType());
        event.setResult(Result.DENY);
      }
      //TODO: remake this idea
      //Altar / Pedestal disabled
      //get ALL blocks that are an altar first
      //then check if it has altar type for my entity type and cancel there, without looping if possible 
      radius = BlockNoTraders.RADIUS.get();
      height = BlockNoTraders.HEIGHT.get();
      if (BlockNoTraders.isSpawnDenied(mob, res)
          && UtilWorld.doesBlockExist(mob.level, mob.blockPosition(),
              BlockRegistry.NO_SOLICITING.get().defaultBlockState().setValue(BlockNoTraders.LIT, true),
              radius, height)) {
        ModCyclic.LOGGER.info("Spawn cancelled by FORTUNE altar " + mob.getType());
        event.setResult(Result.DENY);
      }
      //      if (BlockAltarSol.isFlight(mob, res)
      //          && UtilWorld.doesBlockExist(mob.level, mob.blockPosition(),
      //              BlockRegistry.ALTAR_FLIGHT.get().defaultBlockState().setValue(BlockAltarSol.LIT, true),
      //              radius, height)) {
      //        ModCyclic.LOGGER.info("Spawn cancelled by FLIGHT altar " + mob.getType());
      //        event.setResult(Result.DENY);
      //      }
      //      if (BlockAltarSol.isExplosive(mob, res)
      //          && UtilWorld.doesBlockExist(mob.level, mob.blockPosition(),
      //              BlockRegistry.ALTAR_DESTRUCTION.get().defaultBlockState().setValue(BlockAltarSol.LIT, true),
      //              radius, height)) {
      //        ModCyclic.LOGGER.info("Spawn cancelled by DESTRUCTION altar " + mob.getType());
      //        event.setResult(Result.DENY);
      //      }
    }
  }
}
