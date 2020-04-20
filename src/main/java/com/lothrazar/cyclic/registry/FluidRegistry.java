package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//Thanks to example https://github.com/MinecraftForge/MinecraftForge/blob/1.15.x/src/test/java/net/minecraftforge/debug/fluid/NewFluidTest.java
public class FluidRegistry {

  /**
   * Registry core
   */
  public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, ModCyclic.MODID);
  public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, ModCyclic.MODID);
  public static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, ModCyclic.MODID);
  /**
   * xpjuice
   */
  public static final String xpjuiceId = "xpjuice";
  public static final ResourceLocation XP_STILL = new ResourceLocation(ModCyclic.MODID + ":fluid/" + xpjuiceId + "_base");
  public static final ResourceLocation XP_FLOWING = new ResourceLocation(ModCyclic.MODID + ":fluid/" + xpjuiceId + "_flowing");
  //  public static final ResourceLocation FLUID_OVERLAY = new ResourceLocation("minecraft:block/obsidian");
  public static RegistryObject<FlowingFluid> xpjuice = FLUIDS.register(xpjuiceId, () -> new ForgeFlowingFluid.Source(FluidRegistry.xpjuice_properties));
  public static RegistryObject<FlowingFluid> xpjuice_flowing = FLUIDS.register(xpjuiceId + "_flowing", () -> new ForgeFlowingFluid.Flowing(FluidRegistry.xpjuice_properties));
  public static RegistryObject<FlowingFluidBlock> xpjuice_block = BLOCKS.register(xpjuiceId + "_block",
      () -> new FlowingFluidBlock(xpjuice, Block.Properties.create(net.minecraft.block.material.Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()));
  public static RegistryObject<Item> xpjuice_bucket = ITEMS.register(xpjuiceId + "_bucket",
      () -> new BucketItem(xpjuice, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ItemGroup.MISC)));
  public static final ForgeFlowingFluid.Properties xpjuice_properties = new ForgeFlowingFluid.Properties(xpjuice, xpjuice_flowing,
      FluidAttributes.builder(XP_STILL, XP_FLOWING)
  //          .overlay(FLUID_OVERLAY)
  //          .color(0x3F1080FF)
  ).bucket(xpjuice_bucket).block(xpjuice_block);

  public FluidRegistry() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    BLOCKS.register(modEventBus);
    ITEMS.register(modEventBus);
    FLUIDS.register(modEventBus);
  }
}
