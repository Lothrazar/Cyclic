package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.anvil.TileAnvilAuto;
import com.lothrazar.cyclic.block.anvilmagma.TileAnvilMagma;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.block.beaconpotion.TilePotion;
import com.lothrazar.cyclic.block.bedrock.UnbreakablePoweredTile;
import com.lothrazar.cyclic.block.breaker.TileBreaker;
import com.lothrazar.cyclic.block.cable.energy.TileCableEnergy;
import com.lothrazar.cyclic.block.cable.fluid.TileCableFluid;
import com.lothrazar.cyclic.block.cable.item.TileCableItem;
import com.lothrazar.cyclic.block.clock.TileRedstoneClock;
import com.lothrazar.cyclic.block.collectfluid.TileFluidCollect;
import com.lothrazar.cyclic.block.collectitem.TileItemCollector;
import com.lothrazar.cyclic.block.crafter.TileCrafter;
import com.lothrazar.cyclic.block.crate.TileCrate;
import com.lothrazar.cyclic.block.creativebattery.TileBatteryInfinite;
import com.lothrazar.cyclic.block.creativeitem.TileItemInfinite;
import com.lothrazar.cyclic.block.detectorentity.TileDetector;
import com.lothrazar.cyclic.block.detectoritem.TileDetectorItem;
import com.lothrazar.cyclic.block.dice.TileDice;
import com.lothrazar.cyclic.block.disenchant.TileDisenchant;
import com.lothrazar.cyclic.block.dropper.TileDropper;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf;
import com.lothrazar.cyclic.block.expcollect.TileExpPylon;
import com.lothrazar.cyclic.block.eye.TileEye;
import com.lothrazar.cyclic.block.eyetp.TileEyeTp;
import com.lothrazar.cyclic.block.fan.TileFan;
import com.lothrazar.cyclic.block.fishing.TileFisher;
import com.lothrazar.cyclic.block.forester.TileForester;
import com.lothrazar.cyclic.block.generatorpeat.TilePeatGenerator;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.block.laser.TileLaser;
import com.lothrazar.cyclic.block.melter.TileMelter;
import com.lothrazar.cyclic.block.miner.TileMiner;
import com.lothrazar.cyclic.block.peatfarm.TilePeatFarm;
import com.lothrazar.cyclic.block.placer.TilePlacer;
import com.lothrazar.cyclic.block.placerfluid.TilePlacerFluid;
import com.lothrazar.cyclic.block.screen.TileScreentext;
import com.lothrazar.cyclic.block.shapebuilder.TileStructure;
import com.lothrazar.cyclic.block.shapedata.TileShapedata;
import com.lothrazar.cyclic.block.soil.TileTerraPreta;
import com.lothrazar.cyclic.block.solidifier.TileSolidifier;
import com.lothrazar.cyclic.block.tank.TileTank;
import com.lothrazar.cyclic.block.tankcask.TileCask;
import com.lothrazar.cyclic.block.trash.TileTrash;
import com.lothrazar.cyclic.block.uncrafter.TileUncraft;
import com.lothrazar.cyclic.block.user.TileUser;
import com.lothrazar.cyclic.block.wirelessredstone.TileWirelessRec;
import com.lothrazar.cyclic.block.wirelessredstone.TileWirelessTransmit;
import com.lothrazar.cyclic.block.workbench.TileWorkbench;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileRegistry {

  @SubscribeEvent
  public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
    IForgeRegistry<TileEntityType<?>> r = event.getRegistry();
    r.register(TileEntityType.Builder.create(TileTerraPreta::new, BlockRegistry.terra_preta).build(null).setRegistryName("terra_preta"));
    r.register(TileEntityType.Builder.create(TileEye::new, BlockRegistry.eye_redstone).build(null).setRegistryName("eye_redstone"));
    r.register(TileEntityType.Builder.create(TileEyeTp::new, BlockRegistry.eye_teleport).build(null).setRegistryName("eye_teleport"));
    //
    r.register(TileEntityType.Builder.create(TileAnvilMagma::new, BlockRegistry.anvil_magma).build(null).setRegistryName("anvil_magma"));
    r.register(TileEntityType.Builder.create(TilePotion::new, BlockRegistry.beacon).build(null).setRegistryName("beacon"));
    r.register(TileEntityType.Builder.create(TileBatteryInfinite::new, BlockRegistry.battery_infinite).build(null).setRegistryName("battery_infinite"));
    r.register(TileEntityType.Builder.create(TileItemInfinite::new, BlockRegistry.item_infinite).build(null).setRegistryName("item_infinite"));
    r.register(TileEntityType.Builder.create(TileDice::new, BlockRegistry.dice).build(null).setRegistryName("dice"));
    r.register(TileEntityType.Builder.create(TileDropper::new, BlockRegistry.dropper).build(null).setRegistryName("dropper"));
    r.register(TileEntityType.Builder.create(TileForester::new, BlockRegistry.forester).build(null).setRegistryName("forester"));
    r.register(TileEntityType.Builder.create(TileMiner::new, BlockRegistry.miner).build(null).setRegistryName("miner"));
    r.register(TileEntityType.Builder.create(TileScreentext::new, BlockRegistry.screen).build(null).setRegistryName("screen"));
    r.register(TileEntityType.Builder.create(TileUncraft::new, BlockRegistry.uncrafter).build(null).setRegistryName("uncrafter"));
    //
    r.register(TileEntityType.Builder.create(TilePlacerFluid::new, BlockRegistry.placer_fluid).build(null).setRegistryName("placer_fluid"));
    r.register(TileEntityType.Builder.create(TileCask::new, BlockRegistry.cask).build(null).setRegistryName("cask"));
    r.register(TileEntityType.Builder.create(TileCrate::new, BlockRegistry.crate).build(null).setRegistryName("crate"));
    r.register(TileEntityType.Builder.create(TileRedstoneClock::new, BlockRegistry.clock).build(null).setRegistryName("clock"));
    r.register(TileEntityType.Builder.create(TileWirelessRec::new, BlockRegistry.wireless_receiver).build(null).setRegistryName("wireless_receiver"));
    r.register(TileEntityType.Builder.create(TileWirelessTransmit::new, BlockRegistry.wireless_transmitter).build(null).setRegistryName("wireless_transmitter"));
    r.register(TileEntityType.Builder.create(TileFluidCollect::new, BlockRegistry.collector_fluid).build(null).setRegistryName("collector_fluid"));
    r.register(TileEntityType.Builder.create(TileDisenchant::new, BlockRegistry.disenchanter).build(null).setRegistryName("disenchanter"));
    r.register(TileEntityType.Builder.create(TileDetectorItem::new, BlockRegistry.detector_item).build(null).setRegistryName("detector_item"));
    r.register(TileEntityType.Builder.create(TileDetector::new, BlockRegistry.detector_entity).build(null).setRegistryName("detector_entity"));
    r.register(TileEntityType.Builder.create(TileSolidifier::new, BlockRegistry.solidifier).build(null).setRegistryName("solidifier"));
    r.register(TileEntityType.Builder.create(TileMelter::new, BlockRegistry.melter).build(null).setRegistryName("melter"));
    r.register(TileEntityType.Builder.create(TileTank::new, BlockRegistry.tank).build(null).setRegistryName("tank"));
    r.register(TileEntityType.Builder.create(TileBreaker::new, BlockRegistry.breaker).build(null).setRegistryName("breaker"));
    r.register(TileEntityType.Builder.create(TileItemCollector::new, BlockRegistry.collector).build(null).setRegistryName("collector"));
    r.register(TileEntityType.Builder.create(TileFan::new, BlockRegistry.fan).build(null).setRegistryName("fan"));
    r.register(TileEntityType.Builder.create(TileExpPylon::new, BlockRegistry.experience_pylon).build(null).setRegistryName("experience_pylon"));
    r.register(TileEntityType.Builder.create(TileTrash::new, BlockRegistry.trash).build(null).setRegistryName("trash"));
    r.register(TileEntityType.Builder.create(TilePeatGenerator::new, BlockRegistry.peat_generator).build(null).setRegistryName("peat_generator"));
    r.register(TileEntityType.Builder.create(TilePeatFarm::new, BlockRegistry.peat_farm).build(null).setRegistryName("peat_farm"));
    r.register(TileEntityType.Builder.create(TileBattery::new, BlockRegistry.battery).build(null).setRegistryName("battery"));
    r.register(TileEntityType.Builder.create(TileCableEnergy::new, BlockRegistry.energy_pipe).build(null).setRegistryName("energy_pipe"));
    r.register(TileEntityType.Builder.create(TileCableItem::new, BlockRegistry.item_pipe).build(null).setRegistryName("item_pipe"));
    r.register(TileEntityType.Builder.create(TileCableFluid::new, BlockRegistry.fluid_pipe).build(null).setRegistryName("fluid_pipe"));
    r.register(TileEntityType.Builder.create(TileHarvester::new, BlockRegistry.harvester).build(null).setRegistryName("harvester"));
    r.register(TileEntityType.Builder.create(TileAnvilAuto::new, BlockRegistry.anvil).build(null).setRegistryName("anvil"));
    r.register(TileEntityType.Builder.create(TilePlacer::new, BlockRegistry.placer).build(null).setRegistryName("placer"));
    r.register(TileEntityType.Builder.create(TileStructure::new, BlockRegistry.structure).build(null).setRegistryName("structure"));
    r.register(TileEntityType.Builder.create(TileFisher::new, BlockRegistry.fisher).build(null).setRegistryName("fisher"));
    r.register(TileEntityType.Builder.create(TileUser::new, BlockRegistry.user).build(null).setRegistryName("user"));
    r.register(TileEntityType.Builder.create(TileCrafter::new, BlockRegistry.crafter).build(null).setRegistryName("crafter"));
    r.register(TileEntityType.Builder.create(TileShapedata::new, BlockRegistry.computer_shape).build(null).setRegistryName("computer_shape"));
    r.register(TileEntityType.Builder.create(UnbreakablePoweredTile::new, BlockRegistry.unbreakable_reactive).build(null).setRegistryName("unbreakable_reactive"));
    r.register(TileEntityType.Builder.create(TileLaser::new, BlockRegistry.laser).build(null).setRegistryName("laser"));
    r.register(TileEntityType.Builder.create(TileEnderShelf::new, BlockRegistry.ender_shelf).build(null).setRegistryName("ender_shelf"));
    r.register(TileEntityType.Builder.create(TileEnderShelf::new, BlockRegistry.ender_controller).build(null).setRegistryName("ender_controller"));
    r.register(TileEntityType.Builder.create(TileWorkbench::new, BlockRegistry.workbench).build(null).setRegistryName("workbench"));
  }

  @ObjectHolder(ModCyclic.MODID + ":eye_redstone")
  public static TileEntityType<TileEye> eye_redstone;
  @ObjectHolder(ModCyclic.MODID + ":eye_teleport")
  public static TileEntityType<TileEyeTp> eye_teleport;
  @ObjectHolder(ModCyclic.MODID + ":unbreakable_reactive")
  public static TileEntityType<UnbreakablePoweredTile> unbreakable_reactive;
  @ObjectHolder(ModCyclic.MODID + ":computer_shape")
  public static TileEntityType<TileShapedata> computer_shape;
  @ObjectHolder(ModCyclic.MODID + ":terra_preta")
  public static TileEntityType<TileTerraPreta> terra_preta;
  @ObjectHolder(ModCyclic.MODID + ":wireless_receiver")
  public static TileEntityType<TileWirelessRec> wireless_receiver;
  @ObjectHolder(ModCyclic.MODID + ":wireless_transmitter")
  public static TileEntityType<TileWirelessTransmit> wireless_transmitter;
  @ObjectHolder(ModCyclic.MODID + ":detector_item")
  public static TileEntityType<TileDetectorItem> detector_item;
  @ObjectHolder(ModCyclic.MODID + ":detector_entity")
  public static TileEntityType<TileDetector> detector_entity;
  @ObjectHolder(ModCyclic.MODID + ":solidifier")
  public static TileEntityType<TileSolidifier> solidifier;
  @ObjectHolder(ModCyclic.MODID + ":melter")
  public static TileEntityType<TileMelter> melter;
  @ObjectHolder(ModCyclic.MODID + ":structure")
  public static TileEntityType<TileStructure> structure;
  @ObjectHolder(ModCyclic.MODID + ":anvil")
  public static TileEntityType<TileAnvilAuto> anvil;
  @ObjectHolder(ModCyclic.MODID + ":anvil_magma")
  public static TileEntityType<TileAnvilMagma> anvil_magma;
  @ObjectHolder(ModCyclic.MODID + ":tank")
  public static TileEntityType<TileTank> tank;
  @ObjectHolder(ModCyclic.MODID + ":battery")
  public static TileEntityType<TileBattery> batterytile;
  @ObjectHolder(ModCyclic.MODID + ":energy_pipe")
  public static TileEntityType<TileCableEnergy> energy_pipeTile;
  @ObjectHolder(ModCyclic.MODID + ":item_pipe")
  public static TileEntityType<TileCableItem> item_pipeTile;
  @ObjectHolder(ModCyclic.MODID + ":fluid_pipe")
  public static TileEntityType<TileCableFluid> fluid_pipeTile;
  @ObjectHolder(ModCyclic.MODID + ":collector")
  public static TileEntityType<TileItemCollector> collectortile;
  @ObjectHolder(ModCyclic.MODID + ":trash")
  public static TileEntityType<TileTrash> trashtile;
  @ObjectHolder(ModCyclic.MODID + ":peat_generator")
  public static TileEntityType<TilePeatGenerator> peat_generator;
  @ObjectHolder(ModCyclic.MODID + ":peat_farm")
  public static TileEntityType<TilePeatFarm> peat_farm;
  @ObjectHolder(ModCyclic.MODID + ":harvester")
  public static TileEntityType<TileHarvester> harvesterTile;
  @ObjectHolder(ModCyclic.MODID + ":breaker")
  public static TileEntityType<TileBreaker> breakerTile;
  @ObjectHolder(ModCyclic.MODID + ":fan")
  public static TileEntityType<TileFan> fantile;
  @ObjectHolder(ModCyclic.MODID + ":experience_pylon")
  public static TileEntityType<TileExpPylon> experience_pylontile;
  @ObjectHolder(ModCyclic.MODID + ":placer")
  public static TileEntityType<TilePlacer> placer;
  @ObjectHolder(ModCyclic.MODID + ":fisher")
  public static TileEntityType<TileFisher> fisher;
  @ObjectHolder(ModCyclic.MODID + ":user")
  public static TileEntityType<TileUser> user;
  @ObjectHolder(ModCyclic.MODID + ":disenchanter")
  public static TileEntityType<TileDisenchant> disenchanter;
  @ObjectHolder(ModCyclic.MODID + ":collector_fluid")
  public static TileEntityType<TileFluidCollect> collector_fluid;
  @ObjectHolder(ModCyclic.MODID + ":clock")
  public static TileEntityType<TileRedstoneClock> clock;
  @ObjectHolder(ModCyclic.MODID + ":crate")
  public static TileEntityType<TileCrate> crate;
  @ObjectHolder(ModCyclic.MODID + ":cask")
  public static TileEntityType<TileCrate> cask;
  @ObjectHolder(ModCyclic.MODID + ":placer_fluid")
  public static TileEntityType<TilePlacerFluid> placer_fluid;
  //
  @ObjectHolder(ModCyclic.MODID + ":beacon")
  public static TileEntityType<TilePotion> beacon;
  @ObjectHolder(ModCyclic.MODID + ":battery_infinite")
  public static TileEntityType<TileBatteryInfinite> battery_infinite;
  @ObjectHolder(ModCyclic.MODID + ":item_infinite")
  public static TileEntityType<TileItemInfinite> item_infinite;
  @ObjectHolder(ModCyclic.MODID + ":dice")
  public static TileEntityType<TileDice> dice;
  @ObjectHolder(ModCyclic.MODID + ":dropper")
  public static TileEntityType<TileDropper> dropper;
  @ObjectHolder(ModCyclic.MODID + ":forester")
  public static TileEntityType<TileForester> forester;
  @ObjectHolder(ModCyclic.MODID + ":miner")
  public static TileEntityType<TileMiner> miner;
  @ObjectHolder(ModCyclic.MODID + ":screen")
  public static TileEntityType<TileScreentext> screen;
  @ObjectHolder(ModCyclic.MODID + ":uncrafter")
  public static TileEntityType<TileUncraft> uncrafter;
  @ObjectHolder(ModCyclic.MODID + ":crafter")
  public static TileEntityType<TileCrafter> crafter;
  @ObjectHolder(ModCyclic.MODID + ":ender_shelf")
  public static TileEntityType<TileEnderShelf> ender_shelf;
  @ObjectHolder(ModCyclic.MODID + ":ender_controller")
  public static TileEntityType<TileEnderShelf> ender_controller;
  //
  @ObjectHolder(ModCyclic.MODID + ":laser")
  public static TileEntityType<TileLaser> laser;
  @ObjectHolder(ModCyclic.MODID + ":workbench")
  public static TileEntityType<TileWorkbench> workbench;
}