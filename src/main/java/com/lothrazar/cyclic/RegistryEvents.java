package com.lothrazar.cyclic;

import com.lothrazar.cyclic.CyclicRegistry.Entities;
import com.lothrazar.cyclic.block.BlockDarkGlass;
import com.lothrazar.cyclic.block.BlockPeat;
import com.lothrazar.cyclic.block.BlockPeatFuel;
import com.lothrazar.cyclic.block.BlockSound;
import com.lothrazar.cyclic.block.BlockSpikes;
import com.lothrazar.cyclic.block.BlockSpikes.EnumSpikeType;
import com.lothrazar.cyclic.block.battery.BlockBattery;
import com.lothrazar.cyclic.block.battery.ContainerBattery;
import com.lothrazar.cyclic.block.battery.ItemBlockBattery;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.block.breaker.BlockBreaker;
import com.lothrazar.cyclic.block.breaker.TileBreaker;
import com.lothrazar.cyclic.block.cable.energy.BlockCableEnergy;
import com.lothrazar.cyclic.block.cable.energy.TileCableEnergy;
import com.lothrazar.cyclic.block.expcollect.BlockExpPylon;
import com.lothrazar.cyclic.block.expcollect.TileExpPylon;
import com.lothrazar.cyclic.block.fan.BlockFan;
import com.lothrazar.cyclic.block.fan.TileFan;
import com.lothrazar.cyclic.block.generator.BlockPeatGenerator;
import com.lothrazar.cyclic.block.generator.ContainerGenerator;
import com.lothrazar.cyclic.block.generator.TilePeatGenerator;
import com.lothrazar.cyclic.block.harvester.BlockHarvester;
import com.lothrazar.cyclic.block.harvester.ContainerHarvester;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.block.itemcollect.BlockCollector;
import com.lothrazar.cyclic.block.itemcollect.ContainerCollector;
import com.lothrazar.cyclic.block.itemcollect.TileCollector;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffolding;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffoldingReplace;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffoldingResponsive;
import com.lothrazar.cyclic.block.scaffolding.ItemScaffolding;
import com.lothrazar.cyclic.block.tank.BlockFluidTank;
import com.lothrazar.cyclic.block.tank.ItemBlockTank;
import com.lothrazar.cyclic.block.tank.TileTank;
import com.lothrazar.cyclic.block.trash.BlockTrash;
import com.lothrazar.cyclic.block.trash.TileTrash;
import com.lothrazar.cyclic.enchant.EnchantExcavation;
import com.lothrazar.cyclic.enchant.EnchantLifeLeech;
import com.lothrazar.cyclic.enchant.EnchantMagnet;
import com.lothrazar.cyclic.enchant.EnchantMultishot;
import com.lothrazar.cyclic.enchant.EnchantVenom;
import com.lothrazar.cyclic.enchant.EnchantXp;
import com.lothrazar.cyclic.entity.EntityMagicNetEmpty;
import com.lothrazar.cyclic.entity.EntityTorchBolt;
import com.lothrazar.cyclic.entity.ItemMagicNet;
import com.lothrazar.cyclic.entity.ItemMobContainer;
import com.lothrazar.cyclic.entity.ItemTorchThrower;
import com.lothrazar.cyclic.item.ExpItemGain;
import com.lothrazar.cyclic.item.GemstoneItem;
import com.lothrazar.cyclic.item.ItemChestSack;
import com.lothrazar.cyclic.item.ItemChestSackEmpty;
import com.lothrazar.cyclic.item.PeatItem;
import com.lothrazar.cyclic.item.bauble.AutoTorchItem;
import com.lothrazar.cyclic.item.bauble.CharmAntidote;
import com.lothrazar.cyclic.item.bauble.CharmFire;
import com.lothrazar.cyclic.item.bauble.CharmOverpowered;
import com.lothrazar.cyclic.item.bauble.CharmVoid;
import com.lothrazar.cyclic.item.bauble.CharmWither;
import com.lothrazar.cyclic.item.bauble.GloveItem;
import com.lothrazar.cyclic.item.tool.EnderBagItem;
import com.lothrazar.cyclic.item.tool.EnderEyeReuse;
import com.lothrazar.cyclic.item.tool.EnderPearlMount;
import com.lothrazar.cyclic.item.tool.EnderPearlReuse;
import com.lothrazar.cyclic.item.tool.EnderWingItem;
import com.lothrazar.cyclic.item.tool.EnderWingSp;
import com.lothrazar.cyclic.item.tool.EvokerFangItem;
import com.lothrazar.cyclic.item.tool.IceWand;
import com.lothrazar.cyclic.item.tool.LeverRemote;
import com.lothrazar.cyclic.item.tool.MattockItem;
import com.lothrazar.cyclic.item.tool.ScytheBrush;
import com.lothrazar.cyclic.item.tool.ScytheForage;
import com.lothrazar.cyclic.item.tool.ScytheLeaves;
import com.lothrazar.cyclic.item.tool.ShearsMaterial;
import com.lothrazar.cyclic.item.tool.StirrupsItem;
import com.lothrazar.cyclic.item.tool.WaterSpreaderItem;
import com.lothrazar.cyclic.item.tool.WrenchItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

  @SubscribeEvent
  public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
    IForgeRegistry<Block> r = event.getRegistry();
    r.register(new BlockBreaker(Block.Properties.create(Material.ROCK)).setRegistryName("breaker"));
    r.register(new BlockScaffolding(Block.Properties.create(Material.WOOD), true)
        .setRegistryName("scaffold_fragile"));
    r.register(new BlockScaffoldingResponsive(Block.Properties.create(Material.WOOD), true)
        .setRegistryName("scaffold_responsive"));
    r.register(new BlockScaffoldingReplace(Block.Properties.create(Material.WOOD))
        .setRegistryName("scaffold_replace"));
    r.register(new BlockFluidTank(Block.Properties.create(Material.ROCK)).setRegistryName("tank"));
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
    //      r.register(new BlockBattery(Block.Properties.create(Material.ROCK)).setRegistryName("battery_large"));
    r.register(new BlockCableEnergy(Block.Properties.create(Material.WOOL)).setRegistryName("energy_pipe"));
    //      r.register(new BlockCableEnergy(Block.Properties.create(Material.ROCK)).setRegistryName("item_pipe"));
    //      r.register(new BlockCableEnergy(Block.Properties.create(Material.ROCK)).setRegistryName("fluid_pipe"));
    r.register(new BlockSpikes(Block.Properties.create(Material.ROCK), EnumSpikeType.PLAIN).setRegistryName("spikes_iron"));
    r.register(new BlockSpikes(Block.Properties.create(Material.ROCK), EnumSpikeType.FIRE).setRegistryName("spikes_fire"));
    r.register(new BlockSpikes(Block.Properties.create(Material.ROCK), EnumSpikeType.CURSE).setRegistryName("spikes_curse"));
    r.register(new BlockHarvester(Block.Properties.create(Material.ROCK)).setRegistryName("harvester"));
  }

  @SubscribeEvent
  public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> r = event.getRegistry();
    r.register(new ItemScaffolding(CyclicRegistry.Blocks.scaffold_replace,
        new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("scaffold_replace"));
    r.register(new ItemScaffolding(CyclicRegistry.Blocks.scaffold_fragile,
        new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("scaffold_fragile"));
    r.register(new ItemScaffolding(CyclicRegistry.Blocks.scaffold_responsive,
        new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("scaffold_responsive"));
    r.register(new ItemBlockTank(CyclicRegistry.Blocks.tank, new Item.Properties().group(
        CyclicRegistry.itemGroup)).setRegistryName("tank"));
    r.register(new BlockItem(CyclicRegistry.Blocks.spikes_iron, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("spikes_iron"));
    r.register(new BlockItem(CyclicRegistry.Blocks.spikes_curse, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("spikes_curse"));
    r.register(new BlockItem(CyclicRegistry.Blocks.spikes_fire, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("spikes_fire"));
    r.register(new BlockItem(CyclicRegistry.Blocks.breaker, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("breaker"));
    r.register(new BlockItem(CyclicRegistry.Blocks.collector, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("collector"));
    r.register(new BlockItem(CyclicRegistry.Blocks.dark_glass, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("dark_glass"));
    r.register(new ItemBlockBattery(CyclicRegistry.Blocks.battery, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("battery"));
    r.register(new BlockItem(CyclicRegistry.Blocks.harvester,
        new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("harvester"));
    r.register(new EnderBagItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("ender_bag"));
    r.register(new GemstoneItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("gem_obsidian"));
    r.register(new GemstoneItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("gem_amber"));
    //      Items.SHEARS
    r.register(new MattockItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(9000)).setRegistryName("mattock"));
    r.register(new ShearsMaterial(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256 * 2)).setRegistryName("shears_obsidian"));
    r.register(new ShearsMaterial(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(64)).setRegistryName("shears_flint"));
    r.register(new ExpItemGain(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("experience_food"));
    r.register(new BlockItem(CyclicRegistry.Blocks.experience_pylon, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("experience_pylon"));
    r.register(new BlockItem(CyclicRegistry.Blocks.energy_pipe, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("energy_pipe"));
    //      r.register(new BlockItem(CyclicRegistry.item_pipe, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("item_pipe"));
    //      r.register(new BlockItem(CyclicRegistry.fluid_pipe, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("fluid_pipe"));
    r.register(new GloveItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("glove_climb"));
    r.register(new BlockItem(CyclicRegistry.Blocks.fan, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("fan"));
    r.register(new BlockItem(CyclicRegistry.Blocks.peat_generator, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_generator"));
    r.register(new BlockItem(CyclicRegistry.Blocks.peat_unbaked, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_unbaked"));
    r.register(new BlockItem(CyclicRegistry.Blocks.peat_baked, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_baked"));
    r.register(new BlockItem(CyclicRegistry.Blocks.soundproofing, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("soundproofing"));
    r.register(new WrenchItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("wrench"));
    r.register(new BlockItem(CyclicRegistry.Blocks.trash, new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("trash"));
    r.register(new AutoTorchItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("charm_torch"));
    r.register(new CharmVoid(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(64)).setRegistryName("charm_void"));
    r.register(new CharmAntidote(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(64)).setRegistryName("charm_antidote"));
    r.register(new CharmFire(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(64)).setRegistryName("charm_fire"));
    r.register(new CharmWither(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(64)).setRegistryName("charm_wither"));
    r.register(new CharmOverpowered(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("charm_ultimate"));
    r.register(new EnderWingItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("charm_home"));
    r.register(new EnderWingSp(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("charm_world"));
    r.register(new IceWand(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("spell_ice"));
    r.register(new ScytheBrush(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("scythe_brush"));
    r.register(new ScytheForage(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("scythe_forage"));
    r.register(new ScytheLeaves(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("scythe_leaves"));
    r.register(new StirrupsItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("stirrups"));
    r.register(new WaterSpreaderItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("spell_water"));
    r.register(new PeatItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("peat_fuel"));
    r.register(new PeatItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("biomass"));
    r.register(new EvokerFangItem(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("evoker_fang"));
    r.register(new EnderPearlReuse(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("ender_pearl_reuse"));
    r.register(new EnderPearlMount(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("ender_pearl_mounted"));
    r.register(new EnderEyeReuse(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("ender_eye_reuse"));
    //    r.register(new BoomerangItem(new Item.Properties().group(CyclicRegistry.itemGroup).maxDamage(256)).setRegistryName("boomerang_item"));
    r.register(new LeverRemote(new Item.Properties().group(CyclicRegistry.itemGroup).maxStackSize(1)).setRegistryName("lever_remote"));
    r.register(new ItemMagicNet(new Item.Properties().group(CyclicRegistry.itemGroup)).setRegistryName("magic_net"));
    r.register(new ItemMobContainer(new Item.Properties().maxStackSize(1).group(CyclicRegistry.itemGroup)).setRegistryName("mob_container"));
    r.register(new ItemTorchThrower(new Item.Properties().maxStackSize(1).maxDamage(256).group(CyclicRegistry.itemGroup)).setRegistryName("torch_launcher"));
    r.register(new ItemChestSackEmpty(new Item.Properties().group(CyclicRegistry.itemGroup))
        .setRegistryName("tile_transporter_empty"));
    r.register(new ItemChestSack(new Item.Properties())
        .setRegistryName("tile_transporter"));
  }

  @SubscribeEvent
  public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
    IForgeRegistry<TileEntityType<?>> r = event.getRegistry();
    r.register(TileEntityType.Builder.create(TileTank::new, CyclicRegistry.Blocks.tank).build(null).setRegistryName("tank"));
    r.register(TileEntityType.Builder.create(TileBreaker::new, CyclicRegistry.Blocks.breaker).build(null).setRegistryName("breaker"));
    r.register(TileEntityType.Builder.create(TileCollector::new, CyclicRegistry.Blocks.collector).build(null).setRegistryName("collector"));
    r.register(TileEntityType.Builder.create(TileFan::new, CyclicRegistry.Blocks.fan).build(null).setRegistryName("fan"));
    r.register(TileEntityType.Builder.create(TileExpPylon::new, CyclicRegistry.Blocks.experience_pylon).build(null).setRegistryName("experience_pylon"));
    r.register(TileEntityType.Builder.create(TileTrash::new, CyclicRegistry.Blocks.trash).build(null).setRegistryName("trash"));
    r.register(TileEntityType.Builder.create(TilePeatGenerator::new, CyclicRegistry.Blocks.peat_generator).build(null).setRegistryName("peat_generator"));
    r.register(TileEntityType.Builder.create(TileBattery::new, CyclicRegistry.Blocks.battery).build(null).setRegistryName("battery"));
    r.register(TileEntityType.Builder.create(TileCableEnergy::new, CyclicRegistry.Blocks.energy_pipe).build(null).setRegistryName("energy_pipe"));
    r.register(TileEntityType.Builder.create(TileHarvester::new, CyclicRegistry.Blocks.harvester)
        .build(null).setRegistryName("harvester"));
  }

  @SubscribeEvent
  public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
    IForgeRegistry<ContainerType<?>> r = event.getRegistry();
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      BlockPos pos = data.readBlockPos();
      return new ContainerCollector(windowId, ModCyclic.proxy.getClientWorld(), pos, inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("collector"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      BlockPos pos = data.readBlockPos();
      return new ContainerGenerator(windowId, ModCyclic.proxy.getClientWorld(), pos, inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("peat_generator"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      BlockPos pos = data.readBlockPos();
      return new ContainerBattery(windowId, ModCyclic.proxy.getClientWorld(), pos, inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("battery"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      BlockPos pos = data.readBlockPos();
      return new ContainerHarvester(windowId, ModCyclic.proxy.getClientWorld(), pos, inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("harvester"));
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

  @SubscribeEvent
  public static void registerEntity(RegistryEvent.Register<EntityType<?>> e) {
    IForgeRegistry<EntityType<?>> r = e.getRegistry();
    r.register(
        EntityType.Builder.<EntityMagicNetEmpty> create(EntityMagicNetEmpty::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .size(.6f, .6f)
            .build("magic_net")
            .setRegistryName("magic_net"));
    r.register(
        EntityType.Builder.<EntityTorchBolt> create(EntityTorchBolt::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .size(.6f, .6f)
            .build("torch_bolt")
            .setRegistryName("torch_bolt"));
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public static void registerModels(FMLClientSetupEvent event) {
    RenderingRegistry.registerEntityRenderingHandler(Entities.netball, render -> new SpriteRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(Entities.torchbolt, render -> new SpriteRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
  }
}