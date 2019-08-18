package com.lothrazar.cyclic;

import com.lothrazar.cyclic.block.expcollect.BlockExpPylon;
import com.lothrazar.cyclic.block.expcollect.TileExpPylon;
import com.lothrazar.cyclic.block.itemcollect.BlockCollector;
import com.lothrazar.cyclic.block.itemcollect.ContainerCollector;
import com.lothrazar.cyclic.block.itemcollect.TileCollector;
import com.lothrazar.cyclic.block.trash.BlockTrash;
import com.lothrazar.cyclic.block.trash.TileTrash;
import com.lothrazar.cyclic.item.ItemExp;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class CyclicRegistry {

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
      IForgeRegistry<Block> r = event.getRegistry();
      r.register(new BlockTrash(Block.Properties.create(Material.ROCK)).setRegistryName("trash"));
      r.register(new BlockExpPylon(Block.Properties.create(Material.ROCK)).setRegistryName("experience_pylon"));
      r.register(new BlockCollector(Block.Properties.create(Material.ROCK)).setRegistryName("collector"));
    }

    @SubscribeEvent
    public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
      Item.Properties properties = new Item.Properties().group(CyclicRegistry.itemGroup);
      IForgeRegistry<Item> r = event.getRegistry();
      r.register(new BlockItem(CyclicRegistry.trash, properties).setRegistryName("trash"));
      r.register(new BlockItem(CyclicRegistry.experience_pylon, properties).setRegistryName("experience_pylon"));
      r.register(new BlockItem(CyclicRegistry.collector, properties).setRegistryName("collector"));
      r.register(new ItemExp(properties).setRegistryName("experience_food"));
    }

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
      IForgeRegistry<TileEntityType<?>> r = event.getRegistry();
      r.register(TileEntityType.Builder.create(TileTrash::new, CyclicRegistry.trash).build(null).setRegistryName("trash"));
      r.register(TileEntityType.Builder.create(TileExpPylon::new, CyclicRegistry.experience_pylon).build(null).setRegistryName("experience_pylon"));
      r.register(TileEntityType.Builder.create(TileCollector::new, CyclicRegistry.collector).build(null).setRegistryName("collector"));
    }

    @SubscribeEvent
    public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
      event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        return new ContainerCollector(windowId, ModCyclic.proxy.getClientWorld(), pos, inv, ModCyclic.proxy.getClientPlayer());
      }).setRegistryName("collector"));
    }
  }

  public static ItemGroup itemGroup = new ItemGroup(ModCyclic.MODID) {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(trash);
    }
  };
  @ObjectHolder(ModCyclic.MODID + ":experience_food")
  public static Item experience_food;
  @ObjectHolder(ModCyclic.MODID + ":trash")
  public static BlockTrash trash;
  @ObjectHolder(ModCyclic.MODID + ":trash")
  public static TileEntityType<TileTrash> trashtile;
  @ObjectHolder(ModCyclic.MODID + ":experience_pylon")
  public static BlockExpPylon experience_pylon;
  @ObjectHolder(ModCyclic.MODID + ":experience_pylon")
  public static TileEntityType<TileExpPylon> experience_pylontile;
  //
  @ObjectHolder(ModCyclic.MODID + ":collector")
  public static BlockCollector collector;
  @ObjectHolder(ModCyclic.MODID + ":collector")
  public static TileEntityType<TileCollector> collectortile;
  @ObjectHolder(ModCyclic.MODID + "collector")
  public static ContainerType<ContainerCollector> collectortileContainer;
}
