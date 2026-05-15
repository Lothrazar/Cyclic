package com.lothrazar.cyclic.fluid;

import com.lothrazar.cyclic.fluid.block.HoneyFluidBlock;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.FluidRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public class FluidHoneyHolder {

  private static final String ID = "honey";
  private static final ResourceLocation FLUID_STILL = ResourceLocation.fromNamespaceAndPath("minecraft", "block/honey_block_top");
  private static final ResourceLocation FLUID_FLOWING = ResourceLocation.fromNamespaceAndPath("minecraft", "block/honey_block_side");
  public static final int COLOR = 0xFFCE5D;

  public static final DeferredHolder<FluidType, FluidType> TYPE = FluidRegistry.FLUID_TYPES.register(ID,
      () -> new FluidType(
          FluidType.Properties.create().density(1024).viscosity(1024)
              .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
              .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)) {
        @Override
        public void initializeClient(java.util.function.Consumer<net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions> consumer) {
          consumer.accept(new net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() { return FLUID_STILL; }
            @Override
            public ResourceLocation getFlowingTexture() { return FLUID_FLOWING; }
            @Override
            public int getTintColor() { return COLOR | 0xFF000000; }
          });
        }
      });

  public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> STILL = FluidRegistry.FLUID.register(ID, () -> new BaseFlowingFluid.Source(makeProperties()));
  public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING = FluidRegistry.FLUID.register(ID + "_flowing", () -> new BaseFlowingFluid.Flowing(makeProperties()));

  public static final DeferredBlock<HoneyFluidBlock> BLOCK = BlockRegistry.BLOCKS.register(ID + "_block",
      () -> new HoneyFluidBlock(STILL, Block.Properties.of().liquid().noCollission().strength(100.0F).noLootTable()));

  public static final DeferredItem<Item> BUCKET = ItemRegistry.ITEMS.register(ID + "_bucket",
      () -> new BucketItem(STILL.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

  private static BaseFlowingFluid.Properties makeProperties() {
    return new BaseFlowingFluid.Properties(TYPE, STILL, FLOWING)
        .bucket(() -> BUCKET.get())
        .block(() -> BLOCK.get());
  }
}
