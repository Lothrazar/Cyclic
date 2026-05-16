package com.lothrazar.cyclic.fluid;

import java.util.List;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.FluidRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.library.fluid.GenericFluidBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import com.lothrazar.library.item.BucketItemFlib;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
public class FluidWaxHolder {

  private static final String ID = "wax";
  public static final ResourceLocation FLUID_STILL = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still");
  public static final ResourceLocation FLUID_FLOWING = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_flow");
  public static final int COLOR = 0xEEEEEE;

  public static final DeferredHolder<FluidType, FluidType> TYPE = FluidRegistry.FLUID_TYPES.register(ID,
      () -> new FluidType(
          FluidType.Properties.create().density(1024).viscosity(1024)
              .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
              .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

  public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> STILL = FluidRegistry.FLUID.register(ID, () -> new BaseFlowingFluid.Source(makeProperties()));
  public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING = FluidRegistry.FLUID.register(ID + "_flowing", () -> new BaseFlowingFluid.Flowing(makeProperties()));

  public static final DeferredBlock<GenericFluidBlock> BLOCK = BlockRegistry.BLOCKS.register(ID + "_block",
      () -> new GenericFluidBlock(STILL, Block.Properties.of().liquid().noCollission().strength(100.0F).noLootTable(),
          List.of(
              ent -> ent.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0, false, false, false)),
              ent -> {
                if (!ent.isOnFire()) {
                  ent.igniteForSeconds(1);
                }
              })));

  public static final DeferredItem<Item> BUCKET = ItemRegistry.ITEMS.register(ID + "_bucket",
      () -> new BucketItemFlib(STILL.get()));

  private static BaseFlowingFluid.Properties makeProperties() {
    return new BaseFlowingFluid.Properties(TYPE, STILL, FLOWING)
        .bucket(() -> BUCKET.get())
        .block(() -> BLOCK.get());
  }
}
