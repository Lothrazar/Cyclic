package com.lothrazar.cyclic.block.antipotion;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.StringParseUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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

import java.util.ArrayList;
import java.util.List;

// TODO: 1.21 port — the per-entity LivingEntityCapProvider was not migrated, so the
// MobEffectEvent.Applicable hook that pre-empts new effects is gone for now. Periodic
// absorbPotions() (every TileAntiBeacon.TICKS) still strips matching effects from
// nearby entities. Re-wire the event-based blocker once entity capabilities are ported.
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
