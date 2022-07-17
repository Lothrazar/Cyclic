package com.lothrazar.cyclic.block.antipotion;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.EntityUtil;
import com.lothrazar.cyclic.util.StringParseUtil;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockAntiBeacon extends BlockCyclic {

  private static final float[] COLOR = new float[] { 1, 1, 1 };
  public static IntValue RADIUS;

  public BlockAntiBeacon(Properties properties) {
    super(properties.randomTicks().strength(0.7F).noOcclusion());
  }

  @Override
  public float[] getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
    return COLOR;
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
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
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
      //      claimPotions(world, pos);
      absorbPotions(world, pos);
    }
  }

  @SuppressWarnings("unchecked")
  public static void absorbPotions(Level world, BlockPos pos) {
    //todo: parse from config or whatever
    List<LivingEntity> all = world.getEntitiesOfClass(LivingEntity.class, EntityUtil.makeBoundingBox(pos, TileAntiBeacon.RADIUS.get(), 3));
    ModCyclic.LOGGER.info(pos + " BEACON try absorb potions on " + all.size());
    List<String> potions = (List<String>) TileAntiBeacon.POTIONS.get();
    //    ModCyclic.LOGGER.info("potions TEST poison" + potions);
    for (LivingEntity e : all) {
      cureAllRelevant(potions, e);
    }
  }

  private static void cureAllRelevant(List<String> potions, LivingEntity e) {
    List<MobEffect> cureMe = new ArrayList<>();
    for (MobEffect mobEffect : e.getActiveEffectsMap().keySet()) {
      if (TileAntiBeacon.HARMFUL_POTIONS.get() && mobEffect.getCategory() == MobEffectCategory.HARMFUL) {
        //if its harmful, cure it if config wants to
        cureMe.add(mobEffect);
      }
      else {
        //if its in config, cure it
        ResourceLocation potionId = ForgeRegistries.MOB_EFFECTS.getKey(mobEffect);
        if (StringParseUtil.isInList(potions, potionId)) {
          cureMe.add(mobEffect);
        }
      }
    }
    for (MobEffect curedEffect : cureMe) {
      ModCyclic.LOGGER.info("  !!!!!!!!!  remove poison" + curedEffect);
      e.removeEffect(curedEffect);
    }
  }
}
