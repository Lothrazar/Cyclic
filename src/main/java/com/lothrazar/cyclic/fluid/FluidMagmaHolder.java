package com.lothrazar.cyclic.fluid;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.fluid.block.MagmaFluidBlock;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.FluidRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.library.fluid.ConfigurableFlowingFluid;
import com.lothrazar.library.item.BucketItemFlib;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
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
  public static final Identifier FLUID_STILL = Identifier.fromNamespaceAndPath(ModCyclic.MODID, "block/fluid/" + ID + "_still");
  public static final Identifier FLUID_FLOW = Identifier.fromNamespaceAndPath(ModCyclic.MODID, "block/fluid/" + ID + "_flow");
  public static final int COLOR = 0xFFFFFF;

  public static final DeferredHolder<FluidType, FluidType> TYPE = FluidRegistry.FLUID_TYPES.register(ID,
      () -> new FluidType(
          FluidType.Properties.create().density(FluidRegistry.DENSITY_LAVA - 500).viscosity(FluidRegistry.VISCOSITY_LAVA - 500).temperature(FluidRegistry.TEMP_LAVA - 100)
              .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).lightLevel(FluidRegistry.LIGHT_LEVELMAGMA)
              .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

  public static final DeferredHolder<Fluid, ConfigurableFlowingFluid.Source> STILL = FluidRegistry.FLUID.register(ID,
      () -> new ConfigurableFlowingFluid.Source(makeProperties(), 2, 1));
  public static final DeferredHolder<Fluid, ConfigurableFlowingFluid.Flowing> FLOWING = FluidRegistry.FLUID.register(ID + "_flowing",
      () -> new ConfigurableFlowingFluid.Flowing(makeProperties(), 2, 7));

  public static final DeferredBlock<MagmaFluidBlock> BLOCK = BlockRegistry.BLOCKS.registerBlock(ID + "_block",
      props -> new MagmaFluidBlock(STILL, props.liquid().strength(100.0F).lightLevel(s -> FluidRegistry.LIGHT_LEVELMAGMA).noLootTable()));

  public static final DeferredItem<Item> BUCKET = ItemRegistry.ITEMS.registerItem(ID + "_bucket",
      props -> new BucketItemFlib(STILL.get(), props));

  private static BaseFlowingFluid.Properties makeProperties() {
    return new BaseFlowingFluid.Properties(TYPE, STILL, FLOWING)
        .bucket(() -> BUCKET.get())
        .block(() -> BLOCK.get());
  }
}
