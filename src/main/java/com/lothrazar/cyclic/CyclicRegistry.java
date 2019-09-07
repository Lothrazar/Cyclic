package com.lothrazar.cyclic;

import com.lothrazar.cyclic.block.BlockDarkGlass;
import com.lothrazar.cyclic.block.BlockPeat;
import com.lothrazar.cyclic.block.BlockPeatFuel;
import com.lothrazar.cyclic.block.BlockSound;
import com.lothrazar.cyclic.block.battery.BlockBattery;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.block.breaker.BlockBreaker;
import com.lothrazar.cyclic.block.breaker.TileBreaker;
import com.lothrazar.cyclic.block.expcollect.BlockExpPylon;
import com.lothrazar.cyclic.block.expcollect.TileExpPylon;
import com.lothrazar.cyclic.block.fan.BlockFan;
import com.lothrazar.cyclic.block.fan.TileFan;
import com.lothrazar.cyclic.block.generator.BlockPeatGenerator;
import com.lothrazar.cyclic.block.generator.ContainerGenerator;
import com.lothrazar.cyclic.block.generator.TilePeatGenerator;
import com.lothrazar.cyclic.block.itemcollect.BlockCollector;
import com.lothrazar.cyclic.block.itemcollect.ContainerCollector;
import com.lothrazar.cyclic.block.itemcollect.TileCollector;
import com.lothrazar.cyclic.block.trash.BlockTrash;
import com.lothrazar.cyclic.block.trash.TileTrash;
import com.lothrazar.cyclic.enchant.EnchantExcavation;
import com.lothrazar.cyclic.enchant.EnchantLifeLeech;
import com.lothrazar.cyclic.enchant.EnchantMagnet;
import com.lothrazar.cyclic.enchant.EnchantMultishot;
import com.lothrazar.cyclic.enchant.EnchantVenom;
import com.lothrazar.cyclic.enchant.EnchantXp;
import com.lothrazar.cyclic.item.GloveItem;
import com.lothrazar.cyclic.item.ItemAutoTorch;
import com.lothrazar.cyclic.item.ItemCharmVoid;
import com.lothrazar.cyclic.item.ItemEnderBag;
import com.lothrazar.cyclic.item.ItemEnderWing;
import com.lothrazar.cyclic.item.ItemEnderWingSp;
import com.lothrazar.cyclic.item.ItemExp;
import com.lothrazar.cyclic.item.ItemGemstone;
import com.lothrazar.cyclic.item.ItemIceWand;
import com.lothrazar.cyclic.item.ItemObsShears;
import com.lothrazar.cyclic.item.ItemStirrups;
import com.lothrazar.cyclic.item.ItemWaterSpreader;
import com.lothrazar.cyclic.item.ItemWoodenWrench;
import com.lothrazar.cyclic.item.PeatItem;
import com.lothrazar.cyclic.item.ScytheBrush;
import com.lothrazar.cyclic.item.ScytheForage;
import com.lothrazar.cyclic.item.ScytheLeaves;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
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

  //  @ObjectHolder(ModCyclic.MODID + ":quickshot")
  //  public static Enchantment quickshot;
  @ObjectHolder(ModCyclic.MODID + ":excavate")
  public static EnchantExcavation excavate;
  @ObjectHolder(ModCyclic.MODID + ":experience_boost")
  public static EnchantXp experience_boost;
  @ObjectHolder(ModCyclic.MODID + ":life_leech")
  public static EnchantLifeLeech life_leech;
  //  @ObjectHolder(ModCyclic.MODID + ":launch")
  //  public static Enchantment launch;
  @ObjectHolder(ModCyclic.MODID + ":magnet")
  public static EnchantMagnet magnet;
  @ObjectHolder(ModCyclic.MODID + ":multishot")
  public static EnchantMultishot multishot;
  //  @ObjectHolder(ModCyclic.MODID + ":quickshot")
  //  public static Enchantment quickshot;
  //  @ObjectHolder(ModCyclic.MODID + ":smelting")
  //  public static EnchantAutoSmelt smelting;
  @ObjectHolder(ModCyclic.MODID + ":venom")
  public static EnchantVenom venom;
  @ObjectHolder(ModCyclic.MODID + ":wooden_wrench")
  public static ItemWoodenWrench wrench;
  //
  @ObjectHolder(ModCyclic.MODID + ":lava_walking")
  public static Enchantment lava_walking;
  @ObjectHolder(ModCyclic.MODID + ":peat_generator")
  public static TileEntityType<TilePeatGenerator> peat_generatorTile;
  @ObjectHolder(ModCyclic.MODID + ":peat_generator")
  public static BlockPeatGenerator peat_generator;
  @ObjectHolder(ModCyclic.MODID + ":peat_unbaked")
  public static BlockPeat peat_unbaked;
  @ObjectHolder(ModCyclic.MODID + ":peat_baked")
  public static BlockPeatFuel peat_baked;
  @ObjectHolder(ModCyclic.MODID + ":peat_fuel")
  public static PeatItem peat_fuel;
  //peat disabled
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
  @ObjectHolder(ModCyclic.MODID + ":peat_generator")
  public static ContainerType<ContainerGenerator> generatorCont;
  @ObjectHolder(ModCyclic.MODID + ":battery")
  public static Block battery;
  @ObjectHolder(ModCyclic.MODID + ":battery")
  public static TileEntityType<TileBattery> batterytile;

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
      r.register(new BlockPeatGenerator(Block.Properties.create(Material.ROCK)).setRegistryName("peat_generator"));
      r.register(new BlockPeat(Block.Properties.create(Material.EARTH).sound(SoundType.GROUND)).setRegistryName("peat_unbaked"));
      r.register(new BlockPeatFuel(Block.Properties.create(Material.EARTH).sound(SoundType.GROUND)).setRegistryName("peat_baked"));
      r.register(new BlockSound(Block.Properties.create(Material.ROCK)).setRegistryName("soundproofing"));
      r.register(new BlockTrash(Block.Properties.create(Material.ROCK)).setRegistryName("trash"));
      r.register(new BlockBattery(Block.Properties.create(Material.ROCK)).setRegistryName("battery"));
    }

    @SubscribeEvent
    public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
      IForgeRegistry<Item> r = event.getRegistry();
      r.register(new BlockItem(CyclicRegistry.breaker, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("breaker"));
      r.register(new BlockItem(CyclicRegistry.collector, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("collector"));
      r.register(new BlockItem(CyclicRegistry.dark_glass, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("dark_glass"));
      r.register(new BlockItem(CyclicRegistry.battery, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("battery"));
      r.register(new ItemEnderBag(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("ender_bag"));
      r.register(new ItemGemstone(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("gem_obsidian"));
      r.register(new ItemGemstone(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("gem_amber"));
      //      Items.SHEARS
      r.register(new ItemObsShears(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("shears_obsidian"));
      r.register(new ItemExp(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("experience_food"));
      r.register(new BlockItem(CyclicRegistry.experience_pylon, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("experience_pylon"));
      r.register(new GloveItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("glove_climb"));
      r.register(new BlockItem(CyclicRegistry.fan, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("fan"));
      r.register(new BlockItem(CyclicRegistry.peat_generator, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_generator"));
      r.register(new BlockItem(CyclicRegistry.peat_unbaked, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_unbaked"));
      r.register(new BlockItem(CyclicRegistry.peat_baked, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_baked"));
      r.register(new BlockItem(CyclicRegistry.soundproofing, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("soundproofing"));
      r.register(new ItemWoodenWrench(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("wrench"));///!!!!
      r.register(new BlockItem(CyclicRegistry.trash, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("trash"));
      r.register(new ItemAutoTorch(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("charm_torch"));
      r.register(new ItemCharmVoid(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("charm_void"));
      r.register(new ItemEnderWing(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("charm_home"));
      r.register(new ItemEnderWingSp(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("charm_world"));
      r.register(new ItemIceWand(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("spell_ice"));
      r.register(new ScytheBrush(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("scythe_brush"));
      r.register(new ScytheForage(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("scythe_forage"));
      r.register(new ScytheLeaves(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("scythe_leaves"));
      r.register(new ItemStirrups(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("stirrups"));
      r.register(new ItemWaterSpreader(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("spell_water"));
      r.register(new PeatItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_fuel"));
      r.register(new PeatItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_fuel_enriched"));
      r.register(new PeatItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("biomass"));
    }

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
      IForgeRegistry<TileEntityType<?>> r = event.getRegistry();
      r.register(TileEntityType.Builder.create(TileBreaker::new, CyclicRegistry.breaker).build(null).setRegistryName("breaker"));
      r.register(TileEntityType.Builder.create(TileCollector::new, CyclicRegistry.collector).build(null).setRegistryName("collector"));
      r.register(TileEntityType.Builder.create(TileFan::new, CyclicRegistry.fan).build(null).setRegistryName("fan"));
      r.register(TileEntityType.Builder.create(TileExpPylon::new, CyclicRegistry.experience_pylon).build(null).setRegistryName("experience_pylon"));
      r.register(TileEntityType.Builder.create(TileTrash::new, CyclicRegistry.trash).build(null).setRegistryName("trash"));
      r.register(TileEntityType.Builder.create(TilePeatGenerator::new, CyclicRegistry.peat_generator).build(null).setRegistryName("peat_generator"));
    }

    @SubscribeEvent
    public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
      IForgeRegistry<ContainerType<?>> r = event.getRegistry();
      r.register(IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        return new ContainerCollector(windowId, ModCyclic.proxy.getClientWorld(), pos, inv, ModCyclic.proxy.getClientPlayer());
      }).setRegistryName("collector"));
      //
      r.register(IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        return new ContainerGenerator(windowId, ModCyclic.proxy.getClientWorld(), pos, inv, ModCyclic.proxy.getClientPlayer());
      }).setRegistryName("peat_generator"));
    }

    @SubscribeEvent
    public static void onContainerERegistry(final RegistryEvent.Register<Enchantment> event) {
      IForgeRegistry<Enchantment> r = event.getRegistry();
      //      r.register(new EnchantBeheading(Enchantment.Rarity.RARE, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName("beheading"));
      r.register(new EnchantExcavation(Enchantment.Rarity.RARE, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND).setRegistryName("excavate"));
      r.register(new EnchantXp(Enchantment.Rarity.RARE, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName("experience_boost"));
      //      r.register(new EnchantLaunch(Enchantment.Rarity.RARE, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName("launch"));
      r.register(new EnchantLifeLeech(Enchantment.Rarity.RARE, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName("life_leech"));
      r.register(new EnchantMagnet(Enchantment.Rarity.RARE, EnchantmentType.ALL, EquipmentSlotType.MAINHAND).setRegistryName("magnet"));
      r.register(new EnchantMultishot(Enchantment.Rarity.RARE, EnchantmentType.BOW, EquipmentSlotType.MAINHAND).setRegistryName("multishot"));
      //      r.register(new EnchantQuickdraw(Enchantment.Rarity.RARE, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName("quickshot"));
      //      r.register(new EnchantAutoSmelt(Enchantment.Rarity.RARE, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND).setRegistryName("smelting"));
      r.register(new EnchantVenom(Enchantment.Rarity.RARE, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName("venom"));
      //      r.register(new EnchantWaterwalking(Enchantment.Rarity.RARE, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName("waterwalk"));
    }
  }

  public static ItemGroup itemGroup = new ItemGroup(ModCyclic.MODID) {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(trash);
    }
  };
}
