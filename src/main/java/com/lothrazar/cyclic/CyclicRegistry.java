package com.lothrazar.cyclic;

import com.lothrazar.cyclic.block.BlockDarkGlass;
import com.lothrazar.cyclic.block.BlockPeat;
import com.lothrazar.cyclic.block.BlockPeatFuel;
import com.lothrazar.cyclic.block.BlockSound;
import com.lothrazar.cyclic.block.BlockSpikes;
import com.lothrazar.cyclic.block.BlockSpikes.EnumSpikeType;
import com.lothrazar.cyclic.block.battery.BlockBattery;
import com.lothrazar.cyclic.block.battery.ContainerBattery;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.block.breaker.BlockBreaker;
import com.lothrazar.cyclic.block.breaker.TileBreaker;
import com.lothrazar.cyclic.block.cable.BlockCableEnergy;
import com.lothrazar.cyclic.block.cable.TileCableEnergy;
import com.lothrazar.cyclic.block.expcollect.BlockExpPylon;
import com.lothrazar.cyclic.block.expcollect.TileExpPylon;
import com.lothrazar.cyclic.block.fan.BlockFan;
import com.lothrazar.cyclic.block.fan.TileFan;
import com.lothrazar.cyclic.block.generator.BlockPeatGenerator;
import com.lothrazar.cyclic.block.generator.ContainerGenerator;
import com.lothrazar.cyclic.block.generator.TilePeatGenerator;
import com.lothrazar.cyclic.block.harvester.BlockHarvester;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.block.itemcollect.BlockCollector;
import com.lothrazar.cyclic.block.itemcollect.ContainerCollector;
import com.lothrazar.cyclic.block.itemcollect.TileCollector;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffolding;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffoldingResponsive;
import com.lothrazar.cyclic.block.scaffolding.ItemScaffolding;
import com.lothrazar.cyclic.block.trash.BlockTrash;
import com.lothrazar.cyclic.block.trash.TileTrash;
import com.lothrazar.cyclic.enchant.EnchantExcavation;
import com.lothrazar.cyclic.enchant.EnchantLifeLeech;
import com.lothrazar.cyclic.enchant.EnchantMagnet;
import com.lothrazar.cyclic.enchant.EnchantMultishot;
import com.lothrazar.cyclic.enchant.EnchantVenom;
import com.lothrazar.cyclic.enchant.EnchantXp;
import com.lothrazar.cyclic.item.AutoTorchItem;
import com.lothrazar.cyclic.item.CharmVoidItem;
import com.lothrazar.cyclic.item.EnderBagItem;
import com.lothrazar.cyclic.item.EnderEyeReuse;
import com.lothrazar.cyclic.item.EnderPearlMount;
import com.lothrazar.cyclic.item.EnderPearlReuse;
import com.lothrazar.cyclic.item.EnderWingItem;
import com.lothrazar.cyclic.item.EnderWingSp;
import com.lothrazar.cyclic.item.EvokerFangItem;
import com.lothrazar.cyclic.item.ExpItemGain;
import com.lothrazar.cyclic.item.GemstoneItem;
import com.lothrazar.cyclic.item.GloveItem;
import com.lothrazar.cyclic.item.IceWand;
import com.lothrazar.cyclic.item.LeverRemote;
import com.lothrazar.cyclic.item.MattockItem;
import com.lothrazar.cyclic.item.PeatItem;
import com.lothrazar.cyclic.item.ScytheBrush;
import com.lothrazar.cyclic.item.ScytheForage;
import com.lothrazar.cyclic.item.ScytheLeaves;
import com.lothrazar.cyclic.item.ShearsMaterial;
import com.lothrazar.cyclic.item.StirrupsItem;
import com.lothrazar.cyclic.item.WaterSpreaderItem;
import com.lothrazar.cyclic.item.WrenchItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.EntityType;
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

  public static class Entities {
    //    @ObjectHolder(ModCyclic.MODID + ":boomerang")
    //    public static EntityType<BoomerangEntity> boomerang;
  }

  public static class Enchants {
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
    @ObjectHolder(ModCyclic.MODID + ":lava_walking")
    public static Enchantment lava_walking;
  }

  public static class Items {

    //    @ObjectHolder(ModCyclic.MODID + ":boomerang")
    //    public static BoomerangItem boomerang;
    @ObjectHolder(ModCyclic.MODID + ":wooden_wrench")
    public static WrenchItem wrench;
    @ObjectHolder(ModCyclic.MODID + ":peat_fuel")
    public static PeatItem peat_fuel;
  }

  @ObjectHolder(ModCyclic.MODID + ":scaffold_responsive")
  public static ItemScaffolding item_scaffold_responsive;
  @ObjectHolder(ModCyclic.MODID + ":scaffold_responsive")
  public static BlockScaffolding scaffold_responsive;
  @ObjectHolder(ModCyclic.MODID + ":scaffold_fragile")
  public static BlockScaffolding scaffold_fragile;
  @ObjectHolder(ModCyclic.MODID + ":scaffold_fragile")
  public static ItemScaffolding item_scaffold_fragile;
  @ObjectHolder(ModCyclic.MODID + ":harvester")
  public static TileEntityType<TileHarvester> harvesterTile;
  @ObjectHolder(ModCyclic.MODID + ":harvester")
  public static BlockHarvester harvester;
  @ObjectHolder(ModCyclic.MODID + ":peat_generator")
  public static TileEntityType<TilePeatGenerator> peat_generatorTile;
  @ObjectHolder(ModCyclic.MODID + ":peat_generator")
  public static BlockPeatGenerator peat_generator;
  @ObjectHolder(ModCyclic.MODID + ":peat_unbaked")
  public static BlockPeat peat_unbaked;
  @ObjectHolder(ModCyclic.MODID + ":peat_baked")
  public static BlockPeatFuel peat_baked;
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
  @ObjectHolder(ModCyclic.MODID + ":battery")
  public static ContainerType<ContainerBattery> batteryCont;
  @ObjectHolder(ModCyclic.MODID + ":energy_pipe")
  public static Block energy_pipe;
  @ObjectHolder(ModCyclic.MODID + ":energy_pipe")
  public static TileEntityType<TileCableEnergy> energy_pipeTile;
  @ObjectHolder(ModCyclic.MODID + ":spikes_iron")
  public static Block spikes_iron;
  @ObjectHolder(ModCyclic.MODID + ":spikes_curse")
  public static Block spikes_curse;
  @ObjectHolder(ModCyclic.MODID + ":spikes_fire")
  public static Block spikes_fire;
  @ObjectHolder(ModCyclic.MODID + ":fluid_pipe")
  public static Block fluid_pipe;
  @ObjectHolder(ModCyclic.MODID + ":item_pipe")
  public static Block item_pipe;

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    @SubscribeEvent
    public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> event) {
      IForgeRegistry<EntityType<?>> r = event.getRegistry();
      //      r.register(EntityType.Builder.create(BoomerangEntity::new, EntityClassification.CREATURE)
      //          .size(1, 1)
      //          .setShouldReceiveVelocityUpdates(false)
      //          .build("boomerang").setRegistryName(ModCyclic.MODID, "boomerang"));
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
      IForgeRegistry<Block> r = event.getRegistry();
      r.register(new BlockBreaker(Block.Properties.create(Material.ROCK)).setRegistryName("breaker"));
      r.register(new BlockScaffolding(Block.Properties.create(Material.WOOD), true)
          .setRegistryName("scaffold_fragile"));
      r.register(new BlockScaffoldingResponsive(Block.Properties.create(Material.WOOD), true)
          .setRegistryName("scaffold_responsive"));
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
      r.register(new BlockBattery(Block.Properties.create(Material.ROCK)).setRegistryName("battery_large"));
      r.register(new BlockCableEnergy(Block.Properties.create(Material.ROCK)).setRegistryName("energy_pipe"));
      //      r.register(new BlockCableEnergy(Block.Properties.create(Material.ROCK)).setRegistryName("item_pipe"));
      //      r.register(new BlockCableEnergy(Block.Properties.create(Material.ROCK)).setRegistryName("fluid_pipe"));
      r.register(new BlockSpikes(Block.Properties.create(Material.ROCK), EnumSpikeType.PLAIN).setRegistryName("spikes_iron"));
      //      r.register(new BlockSpikes(Block.Properties.create(Material.ROCK), EnumSpikeType.FIRE).setRegistryName("spikes_fire"));
      //      r.register(new BlockSpikes(Block.Properties.create(Material.ROCK), EnumSpikeType.CURSE).setRegistryName("spikes_curse"));
      r.register(new BlockHarvester(Block.Properties.create(Material.ROCK)).setRegistryName("harvester"));
    }

    @SubscribeEvent
    public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
      IForgeRegistry<Item> r = event.getRegistry();
      r.register(new ItemScaffolding(CyclicRegistry.scaffold_fragile,
          new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("scaffold_fragile"));
      r.register(new ItemScaffolding(CyclicRegistry.scaffold_responsive,
          new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("scaffold_responsive"));
      r.register(new BlockItem(CyclicRegistry.spikes_iron, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("spikes_iron"));
      r.register(new BlockItem(CyclicRegistry.breaker, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("breaker"));
      r.register(new BlockItem(CyclicRegistry.collector, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("collector"));
      r.register(new BlockItem(CyclicRegistry.dark_glass, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("dark_glass"));
      r.register(new BlockItem(CyclicRegistry.battery, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("battery"));
      r.register(new BlockItem(CyclicRegistry.harvester,
          new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("harvester"));
      r.register(new EnderBagItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("ender_bag"));
      r.register(new GemstoneItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("gem_obsidian"));
      r.register(new GemstoneItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("gem_amber"));
      //      Items.SHEARS
      r.register(new MattockItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(9000)).setRegistryName("mattock"));
      r.register(new ShearsMaterial(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256 * 2)).setRegistryName("shears_obsidian"));
      r.register(new ShearsMaterial(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(64)).setRegistryName("shears_flint"));
      r.register(new ExpItemGain(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("experience_food"));
      r.register(new BlockItem(CyclicRegistry.experience_pylon, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("experience_pylon"));
      r.register(new BlockItem(CyclicRegistry.energy_pipe, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("energy_pipe"));
      //      r.register(new BlockItem(CyclicRegistry.item_pipe, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("item_pipe"));
      //      r.register(new BlockItem(CyclicRegistry.fluid_pipe, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("fluid_pipe"));
      r.register(new GloveItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("glove_climb"));
      r.register(new BlockItem(CyclicRegistry.fan, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("fan"));
      r.register(new BlockItem(CyclicRegistry.peat_generator, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_generator"));
      r.register(new BlockItem(CyclicRegistry.peat_unbaked, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_unbaked"));
      r.register(new BlockItem(CyclicRegistry.peat_baked, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_baked"));
      r.register(new BlockItem(CyclicRegistry.soundproofing, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("soundproofing"));
      r.register(new WrenchItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("wrench"));///!!!!
      r.register(new BlockItem(CyclicRegistry.trash, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("trash"));
      r.register(new AutoTorchItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("charm_torch"));
      r.register(new CharmVoidItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("charm_void"));
      r.register(new EnderWingItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("charm_home"));
      r.register(new EnderWingSp(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("charm_world"));
      r.register(new IceWand(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("spell_ice"));
      r.register(new ScytheBrush(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("scythe_brush"));
      r.register(new ScytheForage(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("scythe_forage"));
      r.register(new ScytheLeaves(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("scythe_leaves"));
      r.register(new StirrupsItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("stirrups"));
      r.register(new WaterSpreaderItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("spell_water"));
      r.register(new PeatItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_fuel"));
      //      r.register(new PeatItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_fuel_enriched"));
      r.register(new PeatItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("biomass"));
      r.register(new EvokerFangItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("evoker_fang"));
      r.register(new EnderPearlReuse(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("ender_pearl_reuse"));
      r.register(new EnderPearlMount(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("ender_pearl_mounted"));
      r.register(new EnderEyeReuse(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("ender_eye_reuse"));
      //      r.register(new BoomerangItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("boomerang_item"));
      r.register(new LeverRemote(new Item.Properties().group(CyclicRegistry.itemGroup).maxStackSize(1)).setRegistryName("lever_remote"));
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
      r.register(TileEntityType.Builder.create(TileBattery::new, CyclicRegistry.battery).build(null).setRegistryName("battery"));
      r.register(TileEntityType.Builder.create(TileCableEnergy::new, CyclicRegistry.energy_pipe).build(null).setRegistryName("energy_pipe"));
      r.register(TileEntityType.Builder.create(TileHarvester::new, CyclicRegistry.harvester)
          .build(null).setRegistryName("harvester"));
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
      r.register(IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        return new ContainerBattery(windowId, ModCyclic.proxy.getClientWorld(), pos, inv, ModCyclic.proxy.getClientPlayer());
      }).setRegistryName("battery"));
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
