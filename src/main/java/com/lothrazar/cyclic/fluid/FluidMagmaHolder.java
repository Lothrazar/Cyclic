package com.lothrazar.cyclic.fluid;

import com.lothrazar.cyclic.fluid.block.MagmaFluidBlock;
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

//Thanks to example https://github.com/MinecraftForge/MinecraftForge/blob/1.15.x/src/test/java/net/minecraftforge/debug/fluid/NewFluidTest.java
public class FluidMagmaHolder {

  private static final String ID = "magma";
  private static final ResourceLocation FLUID_STILL = ResourceLocation.fromNamespaceAndPath("minecraft", "block/magma");
  public static final int COLOR = 0x4B261F;

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
            public ResourceLocation getFlowingTexture() { return FLUID_STILL; }
            @Override
            public int getTintColor() { return COLOR | 0xFF000000; }
          });
        }
      });

  public static final DeferredHolder<Fluid, MagmaFluidBlock.Source> STILL = FluidRegistry.FLUID.register(ID, () -> new MagmaFluidBlock.Source(makeProperties()));
  public static final DeferredHolder<Fluid, MagmaFluidBlock.Flowing> FLOWING = FluidRegistry.FLUID.register(ID + "_flowing", () -> new MagmaFluidBlock.Flowing(makeProperties()));

  public static final DeferredBlock<MagmaFluidBlock> BLOCK = BlockRegistry.BLOCKS.register(ID + "_block",
      () -> new MagmaFluidBlock(STILL, Block.Properties.of().liquid().strength(100.0F).lightLevel(s -> 8).noLootTable()));

  public static final DeferredItem<Item> BUCKET = ItemRegistry.ITEMS.register(ID + "_bucket",
      () -> new BucketItem(STILL.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

  private static BaseFlowingFluid.Properties makeProperties() {
    return new BaseFlowingFluid.Properties(TYPE, STILL, FLOWING)
        .bucket(() -> BUCKET.get())
        .block(() -> BLOCK.get());
  }
}
