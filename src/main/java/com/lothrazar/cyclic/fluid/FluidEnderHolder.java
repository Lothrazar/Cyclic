package com.lothrazar.cyclic.fluid;

import java.util.List;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.FluidRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.library.fluid.GenericFluidBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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

public class FluidEnderHolder {

  private static final String ID = "ender";
  public static final ResourceLocation FLUID_STILL = ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "block/fluid/" + ID + "_still");
  public static final ResourceLocation FLUID_FLOW = ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "block/fluid/" + ID + "_flow");
  public static final int COLOR = 0xFFFFFF;
  public static final int LIGHT_LEVEL = 3;

  public static final DeferredHolder<FluidType, FluidType> TYPE = FluidRegistry.FLUID_TYPES.register(ID,
      () -> new FluidType(
          FluidType.Properties.create().density(1050).viscosity(1050).lightLevel(LIGHT_LEVEL)
              .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
              .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

  public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> STILL = FluidRegistry.FLUID.register(ID, () -> new BaseFlowingFluid.Source(makeProperties()));
  public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING = FluidRegistry.FLUID.register(ID + "_flowing", () -> new BaseFlowingFluid.Flowing(makeProperties()));

  public static final DeferredBlock<GenericFluidBlock> BLOCK = BlockRegistry.BLOCKS.register(ID + "_block",
      () -> new GenericFluidBlock(STILL, Block.Properties.of().liquid().replaceable().noCollission().strength(100.0F).lightLevel(s -> LIGHT_LEVEL).noLootTable(),
          List.of(ent -> {
            if (!ent.level().isClientSide && ent.tickCount % 40 == 0) {
              for (int i = 0; i < 16; i++) {
                double x = ent.getX() + (ent.level().random.nextDouble() - 0.5) * 16;
                double y = Mth.clamp(ent.getY() + (double) (ent.level().random.nextInt(16) - 8), ent.level().getMinBuildHeight(), ent.level().getMaxBuildHeight());
                double z = ent.getZ() + (ent.level().random.nextDouble() - 0.5) * 16;
                if (ent.randomTeleport(x, y, z, true)) {
                  break;
                }
              }
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
