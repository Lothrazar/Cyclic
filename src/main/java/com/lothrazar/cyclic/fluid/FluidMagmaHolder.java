package com.lothrazar.cyclic.fluid;

import java.util.function.Consumer;
import com.lothrazar.cyclic.fluid.block.MagmaFluidBlock;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.FluidRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.MaterialRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;

//Thanks to example https://github.com/MinecraftForge/MinecraftForge/blob/1.15.x/src/test/java/net/minecraftforge/debug/fluid/NewFluidTest.java
public class FluidMagmaHolder {

  private static final String id = "magma";
  private static final ResourceLocation FLUID_STILL = new ResourceLocation("minecraft:block/" + id);
  public static final int COLOR = 0x4B261F;
  public static RegistryObject<FlowingFluid> STILL = FluidRegistry.FLUIDS.register(id, () -> new MagmaFluidBlock.Source(makeProperties()));
  public static RegistryObject<FlowingFluid> FLOWING = FluidRegistry.FLUIDS.register(id + "_flowing", () -> new MagmaFluidBlock.Flowing(makeProperties()));
  public static RegistryObject<LiquidBlock> BLOCK = BlockRegistry.BLOCKS.register(id + "_block", () -> new MagmaFluidBlock(STILL, Block.Properties.of(Material.WATER).strength(100.0F).lightLevel((p_235456_0_) -> {
    return 8;
  }).noLootTable()));
  public static RegistryObject<Item> BUCKET = ItemRegistry.ITEMS.register(id + "_bucket", () -> new BucketItem(STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MaterialRegistry.ITEM_GROUP)));
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
          return FLUID_STILL;
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
