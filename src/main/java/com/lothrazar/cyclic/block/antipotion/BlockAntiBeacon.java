package com.lothrazar.cyclic.block.antipotion;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.capabilities.livingentity.LivingEntityCapabilityStorage;
import com.lothrazar.cyclic.registry.AttachmentRegistry;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.StringParseUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockAntiBeacon extends BlockCyclic {

  public BlockAntiBeacon(Properties properties) {
    super(properties.randomTicks().strength(0.7F).noOcclusion());
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
    return true;
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileAntiBeacon(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.ANTI_BEACON.get(), world.isClientSide ? TileAntiBeacon::clientTick : TileAntiBeacon::serverTick);
  }

  @Override
  public void onPlace(BlockState bs, Level world, BlockPos pos, BlockState bsIn, boolean p_56815_) {
    if (!bsIn.is(bs.getBlock())) {
      absorbPotions(world, pos);
    }
  }

  public static void absorbPotions(Level world, BlockPos pos) {
    List<LivingEntity> all = world.getEntitiesOfClass(LivingEntity.class, EntityUtil.makeBoundingBox(pos, TileAntiBeacon.RADIUS.get(), 3));
    for (LivingEntity e : all) {
      cureAllRelevant(e);
    }
  }

  public static void markNearbyEntitiesWithAntiBeaconPosition(Level world, BlockPos pos) {
    int radius = TileAntiBeacon.RADIUS.get();
    List<LivingEntity> all = world.getEntitiesOfClass(LivingEntity.class, EntityUtil.makeBoundingBox(pos, radius, radius));
    for (LivingEntity e : all) {
      LivingEntityCapabilityStorage data = e.getData(AttachmentRegistry.ANTI_BEACON_TARGET);
      BlockPos oldPosition = data.getClosestAntiBeaconPosition();
      if (oldPosition != null && world.getBlockState(oldPosition).is(BlockRegistry.ANTI_BEACON.get())) {
        int oldDistance = e.blockPosition().distManhattan(oldPosition);
        int newDistance = e.blockPosition().distManhattan(pos);
        if (newDistance < oldDistance) {
          data.setClosestAntiBeaconPosition(pos);
        }
      }
      else {
        data.setClosestAntiBeaconPosition(pos);
      }
    }
  }

  public void isPotionApplicable(MobEffectEvent.Applicable event) {
    if (event.getEffectInstance() == null) {
      return;
    }
    LivingEntity livingEntity = event.getEntity();
    if (!doesConfigBlockEffect(event.getEffectInstance().getEffect().value())
        || !(livingEntity.getCommandSenderWorld() instanceof ServerLevel serverLevel)
        || !serverLevel.isLoaded(livingEntity.blockPosition())) {
      return;
    }
    LivingEntityCapabilityStorage data = livingEntity.getData(AttachmentRegistry.ANTI_BEACON_TARGET);
    BlockPos closestAntiBeacon = data.getClosestAntiBeaconPosition();
    if (closestAntiBeacon == null) {
      return;
    }
    if (livingEntity.blockPosition().distManhattan(closestAntiBeacon) > TileAntiBeacon.RADIUS.get()) {
      data.setClosestAntiBeaconPosition(null);
      return;
    }
    if (!serverLevel.getBlockState(closestAntiBeacon).getBlock().equals(this)) {
      data.setClosestAntiBeaconPosition(null);
      return;
    }
    final boolean isPowered = false;
    if (serverLevel.hasNeighborSignal(closestAntiBeacon) != isPowered) {
      return;
    }
    ModCyclic.LOGGER.info("[potion blocked] " + event.getEffectInstance());
    event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
  }

  private static void cureAllRelevant(LivingEntity e) {
    List<Holder<MobEffect>> cureMe = new ArrayList<>();
    for (Holder<MobEffect> mobEffect : e.getActiveEffectsMap().keySet()) {
      if (doesConfigBlockEffect(mobEffect.value())) {
        cureMe.add(mobEffect);
      }
    }
    for (Holder<MobEffect> curedEffect : cureMe) {
      ModCyclic.LOGGER.info("[potion cured] " + curedEffect);
      e.removeEffect(curedEffect);
    }
  }

  @SuppressWarnings("unchecked")
  private static boolean doesConfigBlockEffect(MobEffect mobEffect) {
    if (TileAntiBeacon.HARMFUL_POTIONS.get() && mobEffect.getCategory() == MobEffectCategory.HARMFUL) {
      return true;
    }
    ResourceLocation potionId = BuiltInRegistries.MOB_EFFECT.getKey(mobEffect);
    return StringParseUtil.isInList((List<String>) TileAntiBeacon.POTIONS.get(), potionId);
  }
}
