package com.lothrazar.cyclic.fluid;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.FluidRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
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

  /**
   * xpjuice
   */
  public static final String xpjuiceId = "xpjuice";
  public static final ResourceLocation XP_STILL = new ResourceLocation(ModCyclic.MODID + ":fluid/" + xpjuiceId + "_base");
  public static final ResourceLocation XP_FLOWING = new ResourceLocation(ModCyclic.MODID + ":fluid/" + xpjuiceId + "_flowing");
  public static RegistryObject<FlowingFluid> xpjuice = FluidRegistry.FLUIDS.register(xpjuiceId, () -> new ForgeFlowingFluid.Source(FluidXpJuiceHolder.xpjuice_properties));
  public static RegistryObject<FlowingFluid> xpjuice_flowing = FluidRegistry.FLUIDS.register(xpjuiceId + "_flowing", () -> new ForgeFlowingFluid.Flowing(FluidXpJuiceHolder.xpjuice_properties));
  public static RegistryObject<FlowingFluidBlock> xpjuice_block = FluidRegistry.BLOCKS.register(xpjuiceId + "_block",
      () -> new FlowingFluidBlock(xpjuice, Block.Properties.create(net.minecraft.block.material.Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()));
  public static RegistryObject<Item> xpjuice_bucket = FluidRegistry.ITEMS.register(xpjuiceId + "_bucket",
      () -> new BucketItem(xpjuice, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ItemGroup.MISC)));
  public static final ForgeFlowingFluid.Properties xpjuice_properties = new ForgeFlowingFluid.Properties(xpjuice, xpjuice_flowing,
      FluidAttributes.builder(XP_STILL, XP_FLOWING)).bucket(xpjuice_bucket).block(xpjuice_block);

  public FluidXpJuiceHolder() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    FluidRegistry.BLOCKS.register(modEventBus);
    FluidRegistry.ITEMS.register(modEventBus);
    FluidRegistry.FLUIDS.register(modEventBus);
  }
}
