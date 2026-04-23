package com.lothrazar.cyclic.fluid;

import java.util.function.Consumer;
import com.lothrazar.cyclic.fluid.block.SlimeFluidBlock;
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

public class FluidSlimeHolder {

  private static final String id = "slime";
//  private static final ResourceLocation FLUID_FLOWING = ResourceLocation.fromNamespaceAndPath("minecraft:block/" + id + "_block");
//  private static final ResourceLocation FLUID_STILL = ResourceLocation.parse("minecraft:block/" + id + "_block");
//  public static final int COLOR = 0x51A03E;
//  public static java.util.function.Supplier<FlowingFluid> STILL = FluidRegistry.FLUIDS.register(id, () -> new SlimeFluidBlock.Source(makeProperties()));
//  public static java.util.function.Supplier<FlowingFluid> FLOWING = FluidRegistry.FLUIDS.register(id + "_flowing", () -> new SlimeFluidBlock.Flowing(makeProperties()));
//  public static java.util.function.Supplier<LiquidBlock> BLOCK = BlockRegistry.BLOCKS.register(id + "_block", () -> new SlimeFluidBlock(STILL, Block.Properties.of().liquid()
//      .noCollission().strength(100.0F).noLootTable()));
//  public static java.util.function.Supplier<Item> BUCKET = ItemRegistry.ITEMS.register(id + "_bucket", () -> new BucketItem(STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
//  public static java.util.function.Supplier<FluidType> test_fluid_type = FluidRegistry.FLUID_TYPES.register(id, () -> new FluidType(FluidType.Properties.create()) {
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
//        //        //        @Override
//        public ResourceLocation getOverlayTexture() {
//          return null;
//        }
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
