package com.lothrazar.cyclic.fluid;

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
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;

//Thanks to example https://github.com/MinecraftForge/MinecraftForge/blob/1.15.x/src/test/java/net/minecraftforge/debug/fluid/NewFluidTest.java
public class FluidMagmaHolder {

  private static final String id = "magma";
  public static final int COLOR = 0x4B261F;
  public static RegistryObject<FlowingFluid> STILL = FluidRegistry.FLUIDS.register(id, () -> new MagmaFluidBlock.Source(makeProperties()));
  public static RegistryObject<FlowingFluid> FLOWING = FluidRegistry.FLUIDS.register(id + "_flowing", () -> new MagmaFluidBlock.Flowing(makeProperties()));
  public static RegistryObject<LiquidBlock> BLOCK = BlockRegistry.BLOCKS.register(id + "_block", () -> new MagmaFluidBlock(STILL, Block.Properties.of(Material.WATER).strength(100.0F).lightLevel((p_235456_0_) -> {
    return 8;
  }).noDrops()));
  public static RegistryObject<Item> BUCKET = ItemRegistry.ITEMS.register(id + "_bucket", () -> new BucketItem(STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MaterialRegistry.ITEM_GROUP)));

  private static ForgeFlowingFluid.Properties makeProperties() {
    return new ForgeFlowingFluid.Properties(
        STILL,
        FLOWING,
        FluidAttributes.builder(
            new ResourceLocation("minecraft:block/" + id),
            new ResourceLocation("minecraft:block/" + id))
           // .color(COLOR)
    )
                .bucket(BUCKET)
                .block(BLOCK);
  }
}
