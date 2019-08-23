package com.lothrazar.cyclic;

import com.lothrazar.cyclic.block.BlockDarkGlass;
import com.lothrazar.cyclic.block.BlockSound;
import com.lothrazar.cyclic.block.breaker.BlockBreaker;
import com.lothrazar.cyclic.block.breaker.TileBreaker;
import com.lothrazar.cyclic.block.expcollect.BlockExpPylon;
import com.lothrazar.cyclic.block.expcollect.TileExpPylon;
import com.lothrazar.cyclic.block.fan.BlockFan;
import com.lothrazar.cyclic.block.fan.TileFan;
import com.lothrazar.cyclic.block.itemcollect.BlockCollector;
import com.lothrazar.cyclic.block.itemcollect.ContainerCollector;
import com.lothrazar.cyclic.block.itemcollect.TileCollector;
import com.lothrazar.cyclic.block.trash.BlockTrash;
import com.lothrazar.cyclic.block.trash.TileTrash;
import com.lothrazar.cyclic.enchantment.EnchantXp;
import com.lothrazar.cyclic.item.GloveItem;
import com.lothrazar.cyclic.item.ItemExp;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
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

  @ObjectHolder(ModCyclic.MODID + ":breaker")
  public static Block breaker;
  @ObjectHolder(ModCyclic.MODID + ":breaker")
  public static TileEntityType<TileBreaker> breakerTile;
  @ObjectHolder(ModCyclic.MODID + ":fan")
  public static Block fan;
  @ObjectHolder(ModCyclic.MODID + ":fan")
  public static TileEntityType<TileFan> fantile;
  @ObjectHolder(ModCyclic.MODID + ":soundproofing")
  public static Block soundproofing;
  @ObjectHolder(ModCyclic.MODID + ":dark_glass")
  public static BlockDarkGlass dark_glass;
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
  @ObjectHolder(ModCyclic.MODID + ":collector")
  public static ContainerType<ContainerCollector> collectortileContainer;

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
      IForgeRegistry<Block> r = event.getRegistry();
      r.register(new BlockBreaker(Block.Properties.create(Material.ROCK)).setRegistryName("breaker"));
      r.register(new BlockCollector(Block.Properties.create(Material.ROCK)).setRegistryName("collector"));
      r.register(new BlockDarkGlass(Block.Properties.create(Material.EARTH)).setRegistryName("dark_glass"));
      r.register(new BlockExpPylon(Block.Properties.create(Material.ROCK)).setRegistryName("experience_pylon"));
      r.register(new BlockFan(Block.Properties.create(Material.ROCK)).setRegistryName("fan"));
      r.register(new BlockSound(Block.Properties.create(Material.ROCK)).setRegistryName("soundproofing"));
      r.register(new BlockTrash(Block.Properties.create(Material.ROCK)).setRegistryName("trash"));
    }

    @SubscribeEvent
    public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
      Item.Properties properties = new Item.Properties().group(CyclicRegistry.itemGroup);
      IForgeRegistry<Item> r = event.getRegistry();
      r.register(new BlockItem(CyclicRegistry.breaker, properties).setRegistryName("breaker"));
      r.register(new BlockItem(CyclicRegistry.collector, properties).setRegistryName("collector"));
      r.register(new BlockItem(CyclicRegistry.dark_glass, properties).setRegistryName("dark_glass"));
      r.register(new ItemExp(properties).setRegistryName("experience_food"));
      r.register(new BlockItem(CyclicRegistry.experience_pylon, properties).setRegistryName("experience_pylon"));
      r.register(new GloveItem(properties).setRegistryName("glove_climb"));
      r.register(new BlockItem(CyclicRegistry.fan, properties).setRegistryName("fan"));
      r.register(new BlockItem(CyclicRegistry.soundproofing, properties).setRegistryName("soundproofing"));
      r.register(new BlockItem(CyclicRegistry.trash, properties).setRegistryName("trash"));
    }

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
      IForgeRegistry<TileEntityType<?>> r = event.getRegistry();
      r.register(TileEntityType.Builder.create(TileBreaker::new, CyclicRegistry.breaker).build(null).setRegistryName("breaker"));
      r.register(TileEntityType.Builder.create(TileCollector::new, CyclicRegistry.collector).build(null).setRegistryName("collector"));
      r.register(TileEntityType.Builder.create(TileFan::new, CyclicRegistry.fan).build(null).setRegistryName("fan"));
      r.register(TileEntityType.Builder.create(TileExpPylon::new, CyclicRegistry.experience_pylon).build(null).setRegistryName("experience_pylon"));
      r.register(TileEntityType.Builder.create(TileTrash::new, CyclicRegistry.trash).build(null).setRegistryName("trash"));
    }

    @SubscribeEvent
    public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
      IForgeRegistry<ContainerType<?>> r = event.getRegistry();
      r.register(IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        return new ContainerCollector(windowId, ModCyclic.proxy.getClientWorld(), pos, inv, ModCyclic.proxy.getClientPlayer());
      }).setRegistryName("collector"));
    }

    @SubscribeEvent
    public static void onContainerERegistry(final RegistryEvent.Register<Enchantment> event) {
      IForgeRegistry<Enchantment> r = event.getRegistry();
      r.register(new EnchantXp(Enchantment.Rarity.RARE, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName("experience_boost"));
    }
  }

  public static ItemGroup itemGroup = new ItemGroup(ModCyclic.MODID) {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(trash);
    }
  };
}
