package com.lothrazar.cyclic.block.antipotion;

import java.util.List;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.EntityUtil;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockAntiBeacon extends BlockCyclic {

  public static IntValue RADIUS;
  static String test = "minecraft:poison";

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
      claimPotions(world, pos);
      absorbPotions(world, pos);
    }
  }

  private void claimPotions(Level world, BlockPos pos) {
    // TODO Auto-generated method stub
    List<LivingEntity> all = world.getEntitiesOfClass(LivingEntity.class, EntityUtil.makeBoundingBox(pos, TileAntiBeacon.TICKS.get(), 3));
    for (LivingEntity e : all) {
      for (MobEffectInstance f : e.getActiveEffects()) {
        ResourceLocation key = ForgeRegistries.MOB_EFFECTS.getKey(f.getEffect());
        if (key != null) {
          //
          System.out.println("claim me? " + key);
        }
      }
    }
  }
  //TODO: tile entity that pulses

  public static void absorbPotions(Level world, BlockPos pos) {
    //todo: parse from config or whatever
    MobEffect eff = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(test));
    List<LivingEntity> all = world.getEntitiesOfClass(LivingEntity.class, EntityUtil.makeBoundingBox(pos, TileAntiBeacon.TICKS.get(), 3));
    for (LivingEntity e : all) {
      if (eff != null && e.hasEffect(eff)) {
        //then go
        System.out.println("remove poison" + eff);
        e.removeEffect(eff);
      }
    }
  }
}
