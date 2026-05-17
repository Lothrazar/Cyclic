package com.lothrazar.cyclic.fluid;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.fluid.block.MagmaFluidBlock;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.library.fluid.ConfigurableFlowingFluid;
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
//Thanks to example https://github.com/MinecraftForge/MinecraftForge/blob/1.15.x/src/test/java/net/minecraftforge/debug/fluid/NewFluidTest.java
public class FluidMagmaHolder {

  private static final String ID = "magma";
  public static final ResourceLocation FLUID_STILL = ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "block/fluid/" + ID + "_still");
  public static final ResourceLocation FLUID_FLOW = ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "block/fluid/" + ID + "_flow");
  public static final int COLOR = 0xFFFFFF;

  public static final int LIGHT_LEVEL = 8;
  public static final DeferredHolder<FluidType, FluidType> TYPE = FluidRegistry.FLUID_TYPES.register(ID,
      () -> new FluidType(
          FluidType.Properties.create().density(1024).viscosity(1024)
              .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).lightLevel(LIGHT_LEVEL)
              .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

  public static final DeferredHolder<Fluid, ConfigurableFlowingFluid.Source> STILL = FluidRegistry.FLUID.register(ID,
      () -> new ConfigurableFlowingFluid.Source(makeProperties(), 2, 1));
  public static final DeferredHolder<Fluid, ConfigurableFlowingFluid.Flowing> FLOWING = FluidRegistry.FLUID.register(ID + "_flowing",
      () -> new ConfigurableFlowingFluid.Flowing(makeProperties(), 2, 7));

  public static final DeferredBlock<MagmaFluidBlock> BLOCK = BlockRegistry.BLOCKS.register(ID + "_block",
      () -> new MagmaFluidBlock(STILL, Block.Properties.of().liquid().strength(100.0F).lightLevel(s -> LIGHT_LEVEL).noLootTable()));

  public static final DeferredItem<Item> BUCKET = ItemRegistry.ITEMS.register(ID + "_bucket",
      () -> new BucketItemFlib(STILL.get()));

  private static BaseFlowingFluid.Properties makeProperties() {
    return new BaseFlowingFluid.Properties(TYPE, STILL, FLOWING)
        .bucket(() -> BUCKET.get())
        .block(() -> BLOCK.get());
  }
}
