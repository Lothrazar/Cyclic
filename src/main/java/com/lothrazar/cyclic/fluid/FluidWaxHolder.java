package com.lothrazar.cyclic.fluid;

import java.util.function.Consumer;
import com.lothrazar.cyclic.fluid.block.WaxFluidBlock;
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
public class FluidWaxHolder {
  private static final String ID = "wax";
  //  private static final int COLOR = 0xEEEEEEEE;
//  private static final ResourceLocation FLUID_FLOWING = ResourceLocation.fromNamespaceAndPath("minecraft:block/water_flow");
//  private static final ResourceLocation FLUID_STILL = ResourceLocation.parse("minecraft:block/water_still");
//
//  public static java.util.function.Supplier<FlowingFluid> STILL = FluidRegistry.FLUIDS.register(ID, () -> new BaseFlowingFluid.Source(makeProperties()));
//  public static java.util.function.Supplier<FlowingFluid> FLOWING = FluidRegistry.FLUIDS.register(ID + "_flowing", () -> new BaseFlowingFluid.Flowing(makeProperties()));
//  public static java.util.function.Supplier<LiquidBlock> BLOCK = BlockRegistry.BLOCKS.register(ID + "_block", () -> new WaxFluidBlock(STILL, Block.Properties.of().liquid()
//      .noCollission().strength(100.0F).noLootTable()));
//  public static java.util.function.Supplier<Item> BUCKET = ItemRegistry.ITEMS.register(ID + "_bucket", () -> new BucketItem(STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
//  public static java.util.function.Supplier<FluidType> test_fluid_type = FluidRegistry.FLUID_TYPES.register(ID, () -> new FluidType(FluidType.Properties.create()) {
//
//    @Override
//    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
//      consumer.accept(new IClientFluidTypeExtensions() {
//
//        @Override
//        public ResourceLocation getStillTexture() {
//          return FLUID_STILL;
//        }
//
//        @Override
//        public ResourceLocation getFlowingTexture() {
//          return FLUID_FLOWING;
//        }
//
//        //        @Nullable
//        @Override
//        public ResourceLocation getOverlayTexture() {
//          return null;
//        }
//        //        .luminosity(3).density(3000).viscosity(4000).temperature(1300)
//      });
//    }
//  });
//
//  private static BaseFlowingFluid.Properties makeProperties() {
//    return new BaseFlowingFluid.Properties(test_fluid_type, STILL, FLOWING)
//        .bucket(BUCKET)
//        .block(BLOCK);
//  }
}
