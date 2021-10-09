package com.lothrazar.cyclic.fluid;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.fluid.block.SlimeFluidBlock;
import com.lothrazar.cyclic.registry.MaterialRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//Thanks to example https://github.com/MinecraftForge/MinecraftForge/blob/1.15.x/src/test/java/net/minecraftforge/debug/fluid/NewFluidTest.java
public class FluidSlimeHolder {

  private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModCyclic.MODID);
  private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModCyclic.MODID);
  private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ModCyclic.MODID);
  private static final String id = "slime";
  public static RegistryObject<FlowingFluid> STILL = FLUIDS.register(id, () -> new SlimeFluidBlock.Source(FluidSlimeHolder.properties));
  public static RegistryObject<FlowingFluid> FLOWING = FLUIDS.register(id + "_flowing", () -> new SlimeFluidBlock.Flowing(FluidSlimeHolder.properties));
  public static RegistryObject<LiquidBlock> BLOCK = BLOCKS.register(id + "_block",
      () -> new SlimeFluidBlock(STILL, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
  public static RegistryObject<Item> BUCKET = ITEMS.register(id + "_bucket",
      () -> new BucketItem(STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MaterialRegistry.ITEM_GROUP)));
  private static final ForgeFlowingFluid.Properties properties = new ForgeFlowingFluid.Properties(
      STILL,
      FLOWING,
      FluidAttributes.builder(
          new ResourceLocation("minecraft:block/slime_block"),
          new ResourceLocation("minecraft:block/slime_block")))
              .bucket(BUCKET).block(BLOCK);

  public FluidSlimeHolder(IEventBus modEventBus) {
    BLOCKS.register(modEventBus);
    ITEMS.register(modEventBus);
    FLUIDS.register(modEventBus);
  }
}
