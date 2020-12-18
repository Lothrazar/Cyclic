package com.lothrazar.cyclic.registry;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.block.AppleCropBlock;
import com.lothrazar.cyclic.block.DarkGlassBlock;
import com.lothrazar.cyclic.block.FireplaceBlock;
import com.lothrazar.cyclic.block.FlowerSimpleBlock;
import com.lothrazar.cyclic.block.LaunchBlock;
import com.lothrazar.cyclic.block.MasonBlock;
import com.lothrazar.cyclic.block.PeatBlock;
import com.lothrazar.cyclic.block.PeatFuelBlock;
import com.lothrazar.cyclic.block.SoundmufflerBlock;
import com.lothrazar.cyclic.block.SpikesBlock;
import com.lothrazar.cyclic.block.SpikesBlock.EnumSpikeType;
import com.lothrazar.cyclic.block.UnbreakableBlock;
import com.lothrazar.cyclic.block.UnbreakablePoweredBlock;
import com.lothrazar.cyclic.block.WaterCandleBlock;
import com.lothrazar.cyclic.block.anvil.BlockAnvilAuto;
import com.lothrazar.cyclic.block.anvilmagma.BlockAnvilMagma;
import com.lothrazar.cyclic.block.battery.BlockBattery;
import com.lothrazar.cyclic.block.beaconpotion.BlockPotion;
import com.lothrazar.cyclic.block.breaker.BlockBreaker;
import com.lothrazar.cyclic.block.cable.energy.BlockCableEnergy;
import com.lothrazar.cyclic.block.cable.fluid.BlockCableFluid;
import com.lothrazar.cyclic.block.cable.item.BlockCableItem;
import com.lothrazar.cyclic.block.clock.BlockRedstoneClock;
import com.lothrazar.cyclic.block.collectfluid.BlockFluidCollect;
import com.lothrazar.cyclic.block.collectitem.BlockItemCollector;
import com.lothrazar.cyclic.block.crafter.BlockCrafter;
import com.lothrazar.cyclic.block.crate.BlockCrate;
import com.lothrazar.cyclic.block.creativebattery.BlockBatteryInfinite;
import com.lothrazar.cyclic.block.creativeitem.BlockItemInfinite;
import com.lothrazar.cyclic.block.detectorentity.BlockDetector;
import com.lothrazar.cyclic.block.detectoritem.BlockDetectorItem;
import com.lothrazar.cyclic.block.dice.BlockDice;
import com.lothrazar.cyclic.block.disenchant.BlockDisenchant;
import com.lothrazar.cyclic.block.dropper.BlockDropper;
import com.lothrazar.cyclic.block.endershelf.BlockEnderShelf;
import com.lothrazar.cyclic.block.expcollect.BlockExpPylon;
import com.lothrazar.cyclic.block.eye.BlockEye;
import com.lothrazar.cyclic.block.eyetp.BlockEyeTp;
import com.lothrazar.cyclic.block.fan.BlockFan;
import com.lothrazar.cyclic.block.fishing.BlockFisher;
import com.lothrazar.cyclic.block.forester.BlockForester;
import com.lothrazar.cyclic.block.generatorpeat.BlockPeatGenerator;
import com.lothrazar.cyclic.block.harvester.BlockHarvester;
import com.lothrazar.cyclic.block.laser.BlockLaser;
import com.lothrazar.cyclic.block.melter.BlockMelter;
import com.lothrazar.cyclic.block.miner.BlockMiner;
import com.lothrazar.cyclic.block.peatfarm.BlockPeatFarm;
import com.lothrazar.cyclic.block.placer.BlockPlacer;
import com.lothrazar.cyclic.block.placerfluid.BlockPlacerFluid;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffolding;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffoldingReplace;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffoldingResponsive;
import com.lothrazar.cyclic.block.screen.BlockScreentext;
import com.lothrazar.cyclic.block.shapebuilder.BlockStructure;
import com.lothrazar.cyclic.block.shapedata.BlockShapedata;
import com.lothrazar.cyclic.block.soil.BlockTerraPreta;
import com.lothrazar.cyclic.block.solidifier.BlockSolidifier;
import com.lothrazar.cyclic.block.tank.BlockFluidTank;
import com.lothrazar.cyclic.block.tankcask.BlockCask;
import com.lothrazar.cyclic.block.trash.BlockTrash;
import com.lothrazar.cyclic.block.uncrafter.BlockUncraft;
import com.lothrazar.cyclic.block.user.BlockUser;
import com.lothrazar.cyclic.block.wirelessredstone.BlockWirelessRec;
import com.lothrazar.cyclic.block.wirelessredstone.BlockWirelessTransmit;
import com.lothrazar.cyclic.block.workbench.BlockWorkbench;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegistry {

  public static List<BlockBase> blocks = new ArrayList<>();
  @ObjectHolder(ModCyclic.MODID + ":terra_preta")
  public static Block terra_preta;
  @ObjectHolder(ModCyclic.MODID + ":solidifier")
  public static Block solidifier;
  @ObjectHolder(ModCyclic.MODID + ":melter")
  public static Block melter;
  @ObjectHolder(ModCyclic.MODID + ":structure")
  public static Block structure;
  @ObjectHolder(ModCyclic.MODID + ":anvil")
  public static Block anvil;
  @ObjectHolder(ModCyclic.MODID + ":anvil_magma")
  public static Block anvil_magma;
  @ObjectHolder(ModCyclic.MODID + ":tank")
  public static BlockFluidTank tank;
  @ObjectHolder(ModCyclic.MODID + ":scaffold_replace")
  public static BlockScaffolding scaffold_replace;
  @ObjectHolder(ModCyclic.MODID + ":scaffold_responsive")
  public static BlockScaffolding scaffold_responsive;
  @ObjectHolder(ModCyclic.MODID + ":scaffold_fragile")
  public static BlockScaffolding scaffold_fragile;
  @ObjectHolder(ModCyclic.MODID + ":harvester")
  public static BlockHarvester harvester;
  @ObjectHolder(ModCyclic.MODID + ":peat_generator")
  public static BlockPeatGenerator peat_generator;
  @ObjectHolder(ModCyclic.MODID + ":peat_unbaked")
  public static PeatBlock peat_unbaked;
  @ObjectHolder(ModCyclic.MODID + ":peat_baked")
  public static PeatFuelBlock peat_baked;
  @ObjectHolder(ModCyclic.MODID + ":peat_farm")
  public static BlockPeatFarm peat_farm;
  @ObjectHolder(ModCyclic.MODID + ":breaker")
  public static Block breaker;
  @ObjectHolder(ModCyclic.MODID + ":fan")
  public static Block fan;
  @ObjectHolder(ModCyclic.MODID + ":soundproofing")
  public static Block soundproofing;
  @ObjectHolder(ModCyclic.MODID + ":dark_glass")
  public static DarkGlassBlock dark_glass;
  @ObjectHolder(ModCyclic.MODID + ":trash")
  public static BlockTrash trash;
  @ObjectHolder(ModCyclic.MODID + ":experience_pylon")
  public static BlockExpPylon experience_pylon;
  @ObjectHolder(ModCyclic.MODID + ":collector")
  public static BlockItemCollector collector;
  @ObjectHolder(ModCyclic.MODID + ":battery")
  public static Block battery;
  @ObjectHolder(ModCyclic.MODID + ":energy_pipe")
  public static Block energy_pipe;
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
  @ObjectHolder(ModCyclic.MODID + ":placer")
  public static Block placer;
  @ObjectHolder(ModCyclic.MODID + ":user")
  public static Block user;
  @ObjectHolder(ModCyclic.MODID + ":fisher")
  public static Block fisher;
  @ObjectHolder(ModCyclic.MODID + ":disenchanter")
  public static Block disenchanter;
  @ObjectHolder(ModCyclic.MODID + ":collector_fluid")
  public static Block collector_fluid;
  @ObjectHolder(ModCyclic.MODID + ":detector_entity")
  public static Block detector_entity;
  @ObjectHolder(ModCyclic.MODID + ":detector_item")
  public static Block detector_item;
  @ObjectHolder(ModCyclic.MODID + ":plate_launch")
  public static Block plate_launch;
  @ObjectHolder(ModCyclic.MODID + ":plate_launch_redstone")
  public static Block plate_launch_redstone;
  @ObjectHolder(ModCyclic.MODID + ":wireless_transmitter")
  public static Block wireless_transmitter;
  @ObjectHolder(ModCyclic.MODID + ":wireless_receiver")
  public static Block wireless_receiver;
  @ObjectHolder(ModCyclic.MODID + ":clock")
  public static Block clock;
  @ObjectHolder(ModCyclic.MODID + ":crate")
  public static Block crate;
  @ObjectHolder(ModCyclic.MODID + ":cask")
  public static Block cask;
  @ObjectHolder(ModCyclic.MODID + ":flower_cyan")
  public static Block flower_cyan;
  @ObjectHolder(ModCyclic.MODID + ":placer_fluid")
  public static Block placer_fluid;
  //
  @ObjectHolder(ModCyclic.MODID + ":beacon")
  public static Block beacon;
  @ObjectHolder(ModCyclic.MODID + ":battery_infinite")
  public static Block battery_infinite;
  @ObjectHolder(ModCyclic.MODID + ":item_infinite")
  public static Block item_infinite;
  @ObjectHolder(ModCyclic.MODID + ":dice")
  public static Block dice;
  @ObjectHolder(ModCyclic.MODID + ":dropper")
  public static Block dropper;
  @ObjectHolder(ModCyclic.MODID + ":forester")
  public static Block forester;
  @ObjectHolder(ModCyclic.MODID + ":miner")
  public static Block miner;
  @ObjectHolder(ModCyclic.MODID + ":screen")
  public static Block screen;
  @ObjectHolder(ModCyclic.MODID + ":uncrafter")
  public static Block uncrafter;
  @ObjectHolder(ModCyclic.MODID + ":mason_cobble")
  public static Block mason_cobble;
  @ObjectHolder(ModCyclic.MODID + ":mason_stone")
  public static Block mason_stone;
  @ObjectHolder(ModCyclic.MODID + ":mason_iron")
  public static Block mason_iron;
  @ObjectHolder(ModCyclic.MODID + ":mason_plate")
  public static Block mason_plate;
  @ObjectHolder(ModCyclic.MODID + ":mason_steel")
  public static Block mason_steel;
  @ObjectHolder(ModCyclic.MODID + ":water_candle")
  public static Block water_candle;
  @ObjectHolder(ModCyclic.MODID + ":crafter")
  public static Block crafter;
  @ObjectHolder(ModCyclic.MODID + ":fireplace")
  public static Block fireplace;
  @ObjectHolder(ModCyclic.MODID + ":computer_shape")
  public static Block computer_shape;
  @ObjectHolder(ModCyclic.MODID + ":apple_sprout")
  public static Block apple_sprout;
  @ObjectHolder(ModCyclic.MODID + ":apple_sprout_emerald")
  public static Block apple_sprout_emerald;
  @ObjectHolder(ModCyclic.MODID + ":apple_sprout_diamond")
  public static Block apple_sprout_diamond;
  @ObjectHolder(ModCyclic.MODID + ":unbreakable_block")
  public static Block unbreakable_block;
  @ObjectHolder(ModCyclic.MODID + ":unbreakable_reactive")
  public static Block unbreakable_reactive;
  @ObjectHolder(ModCyclic.MODID + ":laser")
  public static Block laser;
  @ObjectHolder(ModCyclic.MODID + ":ender_shelf")
  public static Block ender_shelf;
  @ObjectHolder(ModCyclic.MODID + ":ender_controller")
  public static Block ender_controller;
  @ObjectHolder(ModCyclic.MODID + ":workbench")
  public static Block workbench;
  @ObjectHolder(ModCyclic.MODID + ":eye_redstone")
  public static Block eye_redstone;
  @ObjectHolder(ModCyclic.MODID + ":eye_teleport")
  public static Block eye_teleport;

  @SubscribeEvent
  public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
    IForgeRegistry<Block> r = event.getRegistry();
    r.register(new BlockLaser(Block.Properties.create(Material.IRON)).setRegistryName("laser"));
    r.register(new AppleCropBlock(Block.Properties.create(Material.PLANTS), false).setRegistryName("apple_sprout_emerald"));
    r.register(new AppleCropBlock(Block.Properties.create(Material.PLANTS), false).setRegistryName("apple_sprout_diamond"));
    r.register(new AppleCropBlock(Block.Properties.create(Material.PLANTS), true).setRegistryName("apple_sprout"));
    r.register(new BlockShapedata(Block.Properties.create(Material.IRON)).setRegistryName("computer_shape"));
    r.register(new FlowerSimpleBlock(Block.Properties.create(Material.PLANTS)).setRegistryName("flower_cyan"));
    r.register(new BlockScaffolding(Block.Properties.create(Material.WOOD), true).setRegistryName("scaffold_fragile"));
    r.register(new BlockScaffoldingResponsive(Block.Properties.create(Material.WOOD), false).setRegistryName("scaffold_responsive"));
    r.register(new BlockScaffoldingReplace(Block.Properties.create(Material.WOOD)).setRegistryName("scaffold_replace"));
    r.register(new DarkGlassBlock(Block.Properties.create(Material.EARTH)).setRegistryName("dark_glass"));
    r.register(new MasonBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName("mason_cobble"));
    r.register(new MasonBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName("mason_stone"));
    r.register(new MasonBlock(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(5.0F, 6.0F)).setRegistryName("mason_iron"));
    r.register(new MasonBlock(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(5.0F, 6.0F)).setRegistryName("mason_steel"));
    r.register(new MasonBlock(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(5.0F, 6.0F)).setRegistryName("mason_plate"));
    r.register(new BlockPeatGenerator(Block.Properties.create(Material.ROCK)).setRegistryName("peat_generator"));
    r.register(new PeatBlock(Block.Properties.create(Material.EARTH).sound(SoundType.GROUND)).setRegistryName("peat_unbaked"));
    r.register(new PeatFuelBlock(Block.Properties.create(Material.EARTH).sound(SoundType.GROUND)).setRegistryName("peat_baked"));
    r.register(new BlockPeatFarm(Block.Properties.create(Material.ROCK)).setRegistryName("peat_farm"));
    r.register(new BlockTerraPreta(Block.Properties.create(Material.EARTH).sound(SoundType.GROUND)).setRegistryName("terra_preta"));
    r.register(new BlockSolidifier(Block.Properties.create(Material.ROCK)).setRegistryName("solidifier"));
    r.register(new BlockMelter(Block.Properties.create(Material.ROCK)).setRegistryName("melter"));
    r.register(new BlockBattery(Block.Properties.create(Material.ROCK)).setRegistryName("battery"));
    r.register(new BlockCask(Block.Properties.create(Material.WOOD)).setRegistryName("cask"));
    r.register(new BlockCrate(Block.Properties.create(Material.WOOD)).setRegistryName("crate"));
    r.register(new BlockEye(Block.Properties.create(Material.WOOD)).setRegistryName("eye_redstone"));
    r.register(new BlockEyeTp(Block.Properties.create(Material.WOOD)).setRegistryName("eye_teleport"));
    //
    r.register(new BlockPlacer(Block.Properties.create(Material.ROCK)).setRegistryName("placer"));
    r.register(new BlockBreaker(Block.Properties.create(Material.ROCK)).setRegistryName("breaker"));
    r.register(new BlockDropper(Block.Properties.create(Material.IRON)).setRegistryName("dropper"));
    r.register(new BlockForester(Block.Properties.create(Material.IRON)).setRegistryName("forester"));
    r.register(new BlockHarvester(Block.Properties.create(Material.ROCK)).setRegistryName("harvester"));
    r.register(new BlockMiner(Block.Properties.create(Material.IRON)).setRegistryName("miner"));
    r.register(new BlockPlacerFluid(Block.Properties.create(Material.IRON)).setRegistryName("placer_fluid"));
    r.register(new BlockUser(Block.Properties.create(Material.ROCK)).setRegistryName("user"));
    r.register(new BlockItemCollector(Block.Properties.create(Material.ROCK)).setRegistryName("collector"));
    r.register(new BlockFluidCollect(Block.Properties.create(Material.ROCK)).setRegistryName("collector_fluid"));
    r.register(new BlockStructure(Block.Properties.create(Material.ROCK)).setRegistryName("structure"));
    r.register(new BlockUncraft(Block.Properties.create(Material.IRON)).setRegistryName("uncrafter"));
    r.register(new BlockCrafter(Block.Properties.create(Material.IRON)).setRegistryName("crafter"));
    //
    r.register(new BlockFluidTank(Block.Properties.create(Material.ROCK)).setRegistryName("tank"));
    //
    r.register(new BlockAnvilAuto(Block.Properties.create(Material.ANVIL).sound(SoundType.ANVIL)).setRegistryName("anvil"));
    r.register(new BlockAnvilMagma(Block.Properties.create(Material.ANVIL).sound(SoundType.ANVIL)).setRegistryName("anvil_magma"));
    r.register(new BlockPotion(Block.Properties.create(Material.IRON)).setRegistryName("beacon"));
    //
    r.register(new SoundmufflerBlock(Block.Properties.create(Material.ROCK)).setRegistryName("soundproofing"));
    r.register(new BlockRedstoneClock(Block.Properties.create(Material.ROCK)).setRegistryName("clock"));
    r.register(new BlockWirelessRec(Block.Properties.create(Material.ROCK)).setRegistryName("wireless_receiver"));
    r.register(new BlockWirelessTransmit(Block.Properties.create(Material.ROCK)).setRegistryName("wireless_transmitter"));
    r.register(new BlockFisher(Block.Properties.create(Material.ROCK)).setRegistryName("fisher"));
    r.register(new BlockDisenchant(Block.Properties.create(Material.ROCK)).setRegistryName("disenchanter"));
    r.register(new BlockExpPylon(Block.Properties.create(Material.ROCK)).setRegistryName("experience_pylon"));
    r.register(new BlockFan(Block.Properties.create(Material.ROCK)).setRegistryName("fan"));
    r.register(new BlockTrash(Block.Properties.create(Material.ROCK)).setRegistryName("trash"));
    r.register(new BlockDice(Block.Properties.create(Material.ROCK)).setRegistryName("dice"));
    r.register(new BlockScreentext(Block.Properties.create(Material.IRON)).setRegistryName("screen"));
    r.register(new BlockDetectorItem(Block.Properties.create(Material.ROCK)).setRegistryName("detector_item"));
    r.register(new BlockDetector(Block.Properties.create(Material.ROCK)).setRegistryName("detector_entity"));
    r.register(new BlockCableEnergy(Block.Properties.create(Material.WOOL).sound(SoundType.STONE)).setRegistryName("energy_pipe"));
    r.register(new BlockCableItem(Block.Properties.create(Material.WOOL).sound(SoundType.STONE)).setRegistryName("item_pipe"));
    r.register(new BlockCableFluid(Block.Properties.create(Material.WOOL).sound(SoundType.STONE)).setRegistryName("fluid_pipe"));
    r.register(new LaunchBlock(Block.Properties.create(Material.ROCK), false).setRegistryName("plate_launch"));
    r.register(new LaunchBlock(Block.Properties.create(Material.ROCK), true).setRegistryName("plate_launch_redstone"));
    r.register(new SpikesBlock(Block.Properties.create(Material.ROCK), EnumSpikeType.PLAIN).setRegistryName("spikes_iron"));
    r.register(new SpikesBlock(Block.Properties.create(Material.ROCK), EnumSpikeType.FIRE).setRegistryName("spikes_fire"));
    r.register(new SpikesBlock(Block.Properties.create(Material.ROCK), EnumSpikeType.CURSE).setRegistryName("spikes_curse"));
    r.register(new BlockBatteryInfinite(Block.Properties.create(Material.ROCK)).setRegistryName("battery_infinite"));
    r.register(new BlockItemInfinite(Block.Properties.create(Material.ROCK)).setRegistryName("item_infinite"));
    r.register(new WaterCandleBlock(Block.Properties.create(Material.ROCK)).setRegistryName("water_candle"));
    r.register(new FireplaceBlock(Block.Properties.create(Material.ROCK)).setRegistryName("fireplace"));
    r.register(new BlockWorkbench(Block.Properties.create(Material.ROCK)).setRegistryName("workbench"));
    r.register(new UnbreakableBlock(Block.Properties.create(Material.ROCK)).setRegistryName("unbreakable_block"));//stable, only changes with player interaction
    r.register(new UnbreakablePoweredBlock(Block.Properties.create(Material.ROCK)).setRegistryName("unbreakable_reactive"));//reactive and unstable, ignores players and reads redstone powered state from world in TE
    r.register(new BlockEnderShelf(Block.Properties.create(Material.ROCK), false).setRegistryName("ender_shelf"));
    r.register(new BlockEnderShelf(Block.Properties.create(Material.ROCK), true).setRegistryName("ender_controller"));
  }
}