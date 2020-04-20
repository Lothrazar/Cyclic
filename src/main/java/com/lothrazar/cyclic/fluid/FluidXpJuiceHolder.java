package com.lothrazar.cyclic.fluid;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.FluidRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

//Thanks to example https://github.com/MinecraftForge/MinecraftForge/blob/1.15.x/src/test/java/net/minecraftforge/debug/fluid/NewFluidTest.java
public class FluidXpJuiceHolder {

  private static final String id = "xpjuice";
  public static RegistryObject<FlowingFluid> STILL = FluidRegistry.FLUIDS.register(id, () -> new ForgeFlowingFluid.Source(FluidXpJuiceHolder.properties));
  public static RegistryObject<FlowingFluid> FLOWING = FluidRegistry.FLUIDS.register(id + "_flowing", () -> new ForgeFlowingFluid.Flowing(FluidXpJuiceHolder.properties));
  public static RegistryObject<FlowingFluidBlock> BLOCK = FluidRegistry.BLOCKS.register(id + "_block",
      () -> new FlowingFluidBlock(STILL, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()));
  public static RegistryObject<Item> BUCKET = FluidRegistry.ITEMS.register(id + "_bucket",
      () -> new BucketItem(STILL, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ItemGroup.MISC)));
  private static final ForgeFlowingFluid.Properties properties = new ForgeFlowingFluid.Properties(
      STILL,
      FLOWING,
      FluidAttributes.builder(
          new ResourceLocation(ModCyclic.MODID + ":fluid/" + id + "_base"),
          new ResourceLocation(ModCyclic.MODID + ":fluid/" + id + "_flowing")))
              .bucket(BUCKET).block(BLOCK);

  public FluidXpJuiceHolder() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    FluidRegistry.BLOCKS.register(modEventBus);
    FluidRegistry.ITEMS.register(modEventBus);
    FluidRegistry.FLUIDS.register(modEventBus);
  }
}
