package com.lothrazar.cyclic.fluid;

import java.util.function.Consumer;
import com.lothrazar.cyclic.fluid.block.HoneyFluidBlock;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.FluidRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;

//Thanks to example https://github.com/MinecraftForge/MinecraftForge/blob/1.15.x/src/test/java/net/minecraftforge/debug/fluid/NewFluidTest.java
public class FluidHoneyHolder {

  private static final String id = "honey";
  private static final ResourceLocation FLUID_FLOWING = new ResourceLocation("minecraft:block/" + id + "_block_side");
  private static final ResourceLocation FLUID_STILL = new ResourceLocation("minecraft:block/" + id + "_block_top");
  public static final int COLOR = 0xFFCE5D;
  public static RegistryObject<FlowingFluid> STILL = FluidRegistry.FLUIDS.register(id, () -> new ForgeFlowingFluid.Source(makeProperties()));
  public static RegistryObject<FlowingFluid> FLOWING = FluidRegistry.FLUIDS.register(id + "_flowing", () -> new ForgeFlowingFluid.Flowing(makeProperties()));
  public static RegistryObject<LiquidBlock> BLOCK = BlockRegistry.BLOCKS.register(id + "_block", () -> new HoneyFluidBlock(STILL, Block.Properties.of().liquid()
      .noCollission().strength(100.0F).noLootTable()));
  public static RegistryObject<Item> BUCKET = ItemRegistry.ITEMS.register(id + "_bucket", () -> new BucketItem(STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
  public static RegistryObject<FluidType> test_fluid_type = FluidRegistry.FLUID_TYPES.register(id, () -> new FluidType(FluidType.Properties.create()) {

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
      consumer.accept(new IClientFluidTypeExtensions() {

        @Override
        public ResourceLocation getStillTexture() {
          return FLUID_STILL;
        }

        @Override
        public ResourceLocation getFlowingTexture() {
          return FLUID_FLOWING;
        }

        //        @Nullable
        @Override
        public ResourceLocation getOverlayTexture() {
          return null;
        }
      });
    }
  });

  private static ForgeFlowingFluid.Properties makeProperties() {
    return new ForgeFlowingFluid.Properties(test_fluid_type, STILL, FLOWING)
        .bucket(BUCKET)
        .block(BLOCK);
  }
}
