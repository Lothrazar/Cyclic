package com.lothrazar.cyclic.fluid;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.fluid.block.RedstoneFluidBlock;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.FluidRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
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

public class FluidRedstoneHolder {

  private static final String ID = "redstone";
  public static final ResourceLocation FLUID_STILL = ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "block/fluid/" + ID + "_still");
  public static final ResourceLocation FLUID_FLOW = ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "block/fluid/" + ID + "_flow");
  public static final int COLOR = 0xFFFFFF;
  public static final int LIGHT_LEVEL = 5;

  public static final DeferredHolder<FluidType, FluidType> TYPE = FluidRegistry.FLUID_TYPES.register(ID,
      () -> new FluidType(
          FluidType.Properties.create().density(FluidRegistry.DENSITY_WATER + 200).viscosity(FluidRegistry.VISCOSITY_WATER + 400).temperature(FluidRegistry.TEMP_WATER)
              .lightLevel(LIGHT_LEVEL)
              .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
              .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

  public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> STILL = FluidRegistry.FLUID.register(ID, () -> new BaseFlowingFluid.Source(makeProperties()));
  public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING = FluidRegistry.FLUID.register(ID + "_flowing", () -> new BaseFlowingFluid.Flowing(makeProperties()));

  public static final DeferredBlock<RedstoneFluidBlock> BLOCK = BlockRegistry.BLOCKS.register(ID + "_block",
      () -> new RedstoneFluidBlock(STILL, Block.Properties.of().liquid().replaceable().noCollission().strength(100.0F).lightLevel(s -> LIGHT_LEVEL).noLootTable()));

  public static final DeferredItem<Item> BUCKET = ItemRegistry.ITEMS.register(ID + "_bucket",
      () -> new BucketItemFlib(STILL.get()));

  private static BaseFlowingFluid.Properties makeProperties() {
    return new BaseFlowingFluid.Properties(TYPE, STILL, FLOWING)
        .bucket(() -> BUCKET.get())
        .block(() -> BLOCK.get());
  }
}
