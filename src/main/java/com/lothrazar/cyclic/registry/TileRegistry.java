package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.anvil.TileAnvilAuto;
import com.lothrazar.cyclic.block.anvilmagma.TileAnvilMagma;
import com.lothrazar.cyclic.block.anvilvoid.TileAnvilVoid;
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
import com.lothrazar.cyclic.block.conveyor.TileConveyor;
import com.lothrazar.cyclic.block.crafter.TileCrafter;
import com.lothrazar.cyclic.block.crate.TileCrate;
import com.lothrazar.cyclic.block.creativebattery.TileBatteryInfinite;
import com.lothrazar.cyclic.block.creativeitem.TileItemInfinite;
import com.lothrazar.cyclic.block.detectmoon.TileMoon;
import com.lothrazar.cyclic.block.detectorentity.TileDetector;
import com.lothrazar.cyclic.block.detectoritem.TileDetectorItem;
import com.lothrazar.cyclic.block.detectweather.TileWeather;
import com.lothrazar.cyclic.block.dice.TileDice;
import com.lothrazar.cyclic.block.disenchant.TileDisenchant;
import com.lothrazar.cyclic.block.dropper.TileDropper;
import com.lothrazar.cyclic.block.enderctrl.TileEnderCtrl;
import com.lothrazar.cyclic.block.enderitemshelf.TileItemShelf;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf;
import com.lothrazar.cyclic.block.expcollect.TileExpPylon;
import com.lothrazar.cyclic.block.eye.TileEye;
import com.lothrazar.cyclic.block.eyetp.TileEyeTp;
import com.lothrazar.cyclic.block.fan.TileFan;
import com.lothrazar.cyclic.block.fanslab.TileFanSlab;
import com.lothrazar.cyclic.block.fishing.TileFisher;
import com.lothrazar.cyclic.block.forester.TileForester;
import com.lothrazar.cyclic.block.generatorfluid.TileGeneratorFluid;
import com.lothrazar.cyclic.block.generatorfood.TileGeneratorFood;
import com.lothrazar.cyclic.block.generatorfuel.TileGeneratorFuel;
import com.lothrazar.cyclic.block.generatoritem.TileGeneratorDrops;
import com.lothrazar.cyclic.block.generatorpeat.TileGeneratorPeat;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.block.hopper.TileSimpleHopper;
import com.lothrazar.cyclic.block.hopperfluid.TileFluidHopper;
import com.lothrazar.cyclic.block.hoppergold.TileGoldHopper;
import com.lothrazar.cyclic.block.laser.TileLaser;
import com.lothrazar.cyclic.block.lightcompr.TileLightCamo;
import com.lothrazar.cyclic.block.melter.TileMelter;
import com.lothrazar.cyclic.block.miner.TileMiner;
import com.lothrazar.cyclic.block.packager.TilePackager;
import com.lothrazar.cyclic.block.peatfarm.TilePeatFarm;
import com.lothrazar.cyclic.block.phantom.MembraneLampTile;
import com.lothrazar.cyclic.block.phantom.SoilTile;
import com.lothrazar.cyclic.block.placer.TilePlacer;
import com.lothrazar.cyclic.block.placerfluid.TilePlacerFluid;
import com.lothrazar.cyclic.block.rotator.TileRotator;
import com.lothrazar.cyclic.block.screen.TileScreentext;
import com.lothrazar.cyclic.block.shapebuilder.TileStructure;
import com.lothrazar.cyclic.block.shapedata.TileShapedata;
import com.lothrazar.cyclic.block.solidifier.TileSolidifier;
import com.lothrazar.cyclic.block.soundmuff.ghost.SoundmuffTile;
import com.lothrazar.cyclic.block.soundplay.TileSoundPlayer;
import com.lothrazar.cyclic.block.soundrecord.TileSoundRecorder;
import com.lothrazar.cyclic.block.spikes.TileDiamondSpikes;
import com.lothrazar.cyclic.block.sprinkler.TileSprinkler;
import com.lothrazar.cyclic.block.tank.TileTank;
import com.lothrazar.cyclic.block.tankcask.TileCask;
import com.lothrazar.cyclic.block.terraglass.TileTerraGlass;
import com.lothrazar.cyclic.block.terrasoil.TileTerraPreta;
import com.lothrazar.cyclic.block.trash.TileTrash;
import com.lothrazar.cyclic.block.uncrafter.TileUncraft;
import com.lothrazar.cyclic.block.user.TileUser;
import com.lothrazar.cyclic.block.wireless.energy.TileWirelessEnergy;
import com.lothrazar.cyclic.block.wireless.fluid.TileWirelessFluid;
import com.lothrazar.cyclic.block.wireless.item.TileWirelessItem;
import com.lothrazar.cyclic.block.wireless.redstone.TileWirelessRec;
import com.lothrazar.cyclic.block.wireless.redstone.TileWirelessTransmit;
import com.lothrazar.cyclic.block.workbench.TileWorkbench;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileRegistry {

  public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ModCyclic.MODID);
  public static final RegistryObject<BlockEntityType<TileFluidHopper>> FLUIDHOPPER = TILES.register("hopper_fluid", () -> BlockEntityType.Builder.of(TileFluidHopper::new, BlockRegistry.FLUIDHOPPER.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileSimpleHopper>> HOPPER = TILES.register("hopper", () -> BlockEntityType.Builder.of(TileSimpleHopper::new, BlockRegistry.HOPPER.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileGoldHopper>> HOPPERGOLD = TILES.register("hopper_gold", () -> BlockEntityType.Builder.of(() -> new TileGoldHopper(), BlockRegistry.HOPPERGOLD.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileAnvilVoid>> ANVILVOID = TILES.register("anvil_void", () -> BlockEntityType.Builder.of(() -> new TileAnvilVoid(), BlockRegistry.ANVILVOID.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileFanSlab>> FANSLAB = TILES.register("fan_slab", () -> BlockEntityType.Builder.of(() -> new TileFanSlab(), BlockRegistry.FANSLAB.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileRotator>> ROTATOR = TILES.register("rotator", () -> BlockEntityType.Builder.of(() -> new TileRotator(), BlockRegistry.ROTATOR.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileMoon>> DETECTORMOON = TILES.register("detector_moon", () -> BlockEntityType.Builder.of(() -> new TileMoon(), BlockRegistry.DETECTORMOON.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileWeather>> DETECTORWEATHER = TILES.register("detector_weather", () -> BlockEntityType.Builder.of(() -> new TileWeather(), BlockRegistry.DETECTORWEATHER.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileTerraGlass>> TERRAGLASS = TILES.register("terra_glass", () -> BlockEntityType.Builder.of(() -> new TileTerraGlass(), BlockRegistry.TERRAGLASS.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileSprinkler>> SPRINKLER = TILES.register("sprinkler", () -> BlockEntityType.Builder.of(() -> new TileSprinkler(), BlockRegistry.SPRINKLER.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileItemShelf>> ENDER_ITEM_SHELF = TILES.register("ender_item_shelf", () -> BlockEntityType.Builder.of(() -> new TileItemShelf(), BlockRegistry.ENDER_ITEM_SHELF.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileWirelessEnergy>> WIRELESS_ENERGY = TILES.register("wireless_energy", () -> BlockEntityType.Builder.of(() -> new TileWirelessEnergy(), BlockRegistry.WIRELESS_ENERGY.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileWirelessItem>> WIRELESS_ITEM = TILES.register("wireless_item", () -> BlockEntityType.Builder.of(() -> new TileWirelessItem(), BlockRegistry.WIRELESS_ITEM.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileWirelessFluid>> WIRELESS_FLUID = TILES.register("wireless_fluid", () -> BlockEntityType.Builder.of(() -> new TileWirelessFluid(), BlockRegistry.WIRELESS_FLUID.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileSoundRecorder>> SOUND_RECORDER = TILES.register("sound_recorder", () -> BlockEntityType.Builder.of(() -> new TileSoundRecorder(), BlockRegistry.SOUND_RECORDER.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileSoundPlayer>> SOUND_PLAYER = TILES.register("sound_player", () -> BlockEntityType.Builder.of(() -> new TileSoundPlayer(), BlockRegistry.SOUND_PLAYER.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileGeneratorFuel>> GENERATOR_FUEL = TILES.register("generator_fuel", () -> BlockEntityType.Builder.of(() -> new TileGeneratorFuel(), BlockRegistry.GENERATOR_FUEL.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileGeneratorFood>> GENERATOR_FOOD = TILES.register("generator_food", () -> BlockEntityType.Builder.of(() -> new TileGeneratorFood(), BlockRegistry.GENERATOR_FOOD.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileGeneratorDrops>> GENERATOR_ITEM = TILES.register("generator_item", () -> BlockEntityType.Builder.of(() -> new TileGeneratorDrops(), BlockRegistry.GENERATOR_ITEM.get()).build(null));
  public static final RegistryObject<BlockEntityType<TileGeneratorFluid>> GENERATOR_FLUID = TILES.register("generator_fluid", () -> BlockEntityType.Builder.of(() -> new TileGeneratorFluid(), BlockRegistry.GENERATOR_FLUID.get()).build(null));
  public static final RegistryObject<BlockEntityType<TilePackager>> PACKAGER = TILES.register("packager", () -> BlockEntityType.Builder.of(() -> new TilePackager(), BlockRegistry.PACKAGER.get()).build(null));
  public static final RegistryObject<BlockEntityType<MembraneLampTile>> LAMP = TILES.register("lamp", () -> BlockEntityType.Builder.of(() -> new MembraneLampTile(), BlockRegistry.LAMP.get()).build(null));
  public static final RegistryObject<BlockEntityType<SoilTile>> SOIL = TILES.register("soil", () -> BlockEntityType.Builder.of(() -> new SoilTile(), BlockRegistry.SOIL.get()).build(null));
  //  public static final RegistryObject<TileEntityType<BalloonTile>> BALLOON = TILES.register("balloon", () -> TileEntityType.Builder.create(() -> new BalloonTile(), BlockRegistry.BALLOON.get()).build(null));

  @SubscribeEvent
  public static void onTileEntityRegistry(final RegistryEvent.Register<BlockEntityType<?>> event) {
    IForgeRegistry<BlockEntityType<?>> r = event.getRegistry();
    r.register(BlockEntityType.Builder.of(TileDiamondSpikes::new, BlockRegistry.spikes_diamond).build(null).setRegistryName("spikes_diamond"));
    r.register(BlockEntityType.Builder.of(TileLightCamo::new, BlockRegistry.LIGHT_CAMO.get()).build(null).setRegistryName("light_camo"));
    r.register(BlockEntityType.Builder.of(SoundmuffTile::new, BlockRegistry.soundproofing_ghost).build(null).setRegistryName("soundproofing_ghost"));
    r.register(BlockEntityType.Builder.of(TileTerraPreta::new, BlockRegistry.TERRA_PRETA.get()).build(null).setRegistryName("terra_preta"));
    r.register(BlockEntityType.Builder.of(TileEye::new, BlockRegistry.EYE_REDSTONE).build(null).setRegistryName("eye_redstone"));
    r.register(BlockEntityType.Builder.of(TileEyeTp::new, BlockRegistry.EYE_TELEPORT).build(null).setRegistryName("eye_teleport"));
    //
    r.register(BlockEntityType.Builder.of(TileAnvilMagma::new, BlockRegistry.anvil_magma).build(null).setRegistryName("anvil_magma"));
    r.register(BlockEntityType.Builder.of(TilePotion::new, BlockRegistry.beacon).build(null).setRegistryName("beacon"));
    r.register(BlockEntityType.Builder.of(TileBatteryInfinite::new, BlockRegistry.battery_infinite).build(null).setRegistryName("battery_infinite"));
    r.register(BlockEntityType.Builder.of(TileItemInfinite::new, BlockRegistry.item_infinite).build(null).setRegistryName("item_infinite"));
    r.register(BlockEntityType.Builder.of(TileDice::new, BlockRegistry.dice).build(null).setRegistryName("dice"));
    r.register(BlockEntityType.Builder.of(TileDropper::new, BlockRegistry.dropper).build(null).setRegistryName("dropper"));
    r.register(BlockEntityType.Builder.of(TileForester::new, BlockRegistry.forester).build(null).setRegistryName("forester"));
    r.register(BlockEntityType.Builder.of(TileMiner::new, BlockRegistry.miner).build(null).setRegistryName("miner"));
    r.register(BlockEntityType.Builder.of(TileScreentext::new, BlockRegistry.screen).build(null).setRegistryName("screen"));
    r.register(BlockEntityType.Builder.of(TileUncraft::new, BlockRegistry.uncrafter).build(null).setRegistryName("uncrafter"));
    //
    r.register(BlockEntityType.Builder.of(TilePlacerFluid::new, BlockRegistry.placer_fluid).build(null).setRegistryName("placer_fluid"));
    r.register(BlockEntityType.Builder.of(TileCask::new, BlockRegistry.cask).build(null).setRegistryName("cask"));
    r.register(BlockEntityType.Builder.of(TileCrate::new, BlockRegistry.crate).build(null).setRegistryName("crate"));
    r.register(BlockEntityType.Builder.of(TileRedstoneClock::new, BlockRegistry.clock).build(null).setRegistryName("clock"));
    r.register(BlockEntityType.Builder.of(TileWirelessRec::new, BlockRegistry.wireless_receiver).build(null).setRegistryName("wireless_receiver"));
    r.register(BlockEntityType.Builder.of(TileWirelessTransmit::new, BlockRegistry.wireless_transmitter).build(null).setRegistryName("wireless_transmitter"));
    r.register(BlockEntityType.Builder.of(TileFluidCollect::new, BlockRegistry.collector_fluid).build(null).setRegistryName("collector_fluid"));
    r.register(BlockEntityType.Builder.of(TileDisenchant::new, BlockRegistry.disenchanter).build(null).setRegistryName("disenchanter"));
    r.register(BlockEntityType.Builder.of(TileDetectorItem::new, BlockRegistry.detector_item).build(null).setRegistryName("detector_item"));
    r.register(BlockEntityType.Builder.of(TileDetector::new, BlockRegistry.detector_entity).build(null).setRegistryName("detector_entity"));
    r.register(BlockEntityType.Builder.of(TileSolidifier::new, BlockRegistry.SOLIDIFIER).build(null).setRegistryName("solidifier"));
    r.register(BlockEntityType.Builder.of(TileMelter::new, BlockRegistry.MELTER).build(null).setRegistryName("melter"));
    r.register(BlockEntityType.Builder.of(TileTank::new, BlockRegistry.tank).build(null).setRegistryName("tank"));
    r.register(BlockEntityType.Builder.of(TileBreaker::new, BlockRegistry.breaker).build(null).setRegistryName("breaker"));
    r.register(BlockEntityType.Builder.of(TileItemCollector::new, BlockRegistry.collector).build(null).setRegistryName("collector"));
    r.register(BlockEntityType.Builder.of(TileFan::new, BlockRegistry.fan).build(null).setRegistryName("fan"));
    r.register(BlockEntityType.Builder.of(TileExpPylon::new, BlockRegistry.experience_pylon).build(null).setRegistryName("experience_pylon"));
    r.register(BlockEntityType.Builder.of(TileTrash::new, BlockRegistry.trash).build(null).setRegistryName("trash"));
    r.register(BlockEntityType.Builder.of(TileGeneratorPeat::new, BlockRegistry.peat_generator).build(null).setRegistryName("peat_generator"));
    r.register(BlockEntityType.Builder.of(TilePeatFarm::new, BlockRegistry.peat_farm).build(null).setRegistryName("peat_farm"));
    r.register(BlockEntityType.Builder.of(TileBattery::new, BlockRegistry.battery).build(null).setRegistryName("battery"));
    r.register(BlockEntityType.Builder.of(TileCableEnergy::new, BlockRegistry.energy_pipe).build(null).setRegistryName("energy_pipe"));
    r.register(BlockEntityType.Builder.of(TileCableItem::new, BlockRegistry.item_pipe).build(null).setRegistryName("item_pipe"));
    r.register(BlockEntityType.Builder.of(TileCableFluid::new, BlockRegistry.fluid_pipe).build(null).setRegistryName("fluid_pipe"));
    r.register(BlockEntityType.Builder.of(TileHarvester::new, BlockRegistry.harvester).build(null).setRegistryName("harvester"));
    r.register(BlockEntityType.Builder.of(TileAnvilAuto::new, BlockRegistry.anvil).build(null).setRegistryName("anvil"));
    r.register(BlockEntityType.Builder.of(TilePlacer::new, BlockRegistry.placer).build(null).setRegistryName("placer"));
    r.register(BlockEntityType.Builder.of(TileStructure::new, BlockRegistry.structure).build(null).setRegistryName("structure"));
    r.register(BlockEntityType.Builder.of(TileFisher::new, BlockRegistry.fisher).build(null).setRegistryName("fisher"));
    r.register(BlockEntityType.Builder.of(TileUser::new, BlockRegistry.user).build(null).setRegistryName("user"));
    r.register(BlockEntityType.Builder.of(TileCrafter::new, BlockRegistry.crafter).build(null).setRegistryName("crafter"));
    r.register(BlockEntityType.Builder.of(TileShapedata::new, BlockRegistry.computer_shape).build(null).setRegistryName("computer_shape"));
    r.register(BlockEntityType.Builder.of(UnbreakablePoweredTile::new, BlockRegistry.unbreakable_reactive).build(null).setRegistryName("unbreakable_reactive"));
    r.register(BlockEntityType.Builder.of(TileLaser::new, BlockRegistry.LASER.get()).build(null).setRegistryName("laser"));
    r.register(BlockEntityType.Builder.of(TileConveyor::new, BlockRegistry.CONVEYOR).build(null).setRegistryName("conveyor"));
    r.register(BlockEntityType.Builder.of(TileEnderShelf::new, BlockRegistry.ENDER_SHELF).build(null).setRegistryName("ender_shelf"));
    r.register(BlockEntityType.Builder.of(TileEnderCtrl::new, BlockRegistry.ENDER_CONTROLLER).build(null).setRegistryName("ender_controller"));
    r.register(BlockEntityType.Builder.of(TileWorkbench::new, BlockRegistry.WORKBENCH.get()).build(null).setRegistryName("workbench"));
  }

  @ObjectHolder(ModCyclic.MODID + ":spikes_diamond")
  public static BlockEntityType<TileDiamondSpikes> spikes_diamond;
  @ObjectHolder(ModCyclic.MODID + ":light_camo")
  public static BlockEntityType<TileLightCamo> light_camo;
  @ObjectHolder(ModCyclic.MODID + ":soundproofing_ghost")
  public static BlockEntityType<SoundmuffTile> soundproofing_ghost;
  @ObjectHolder(ModCyclic.MODID + ":eye_redstone")
  public static BlockEntityType<TileEye> eye_redstone;
  @ObjectHolder(ModCyclic.MODID + ":eye_teleport")
  public static BlockEntityType<TileEyeTp> eye_teleport;
  @ObjectHolder(ModCyclic.MODID + ":unbreakable_reactive")
  public static BlockEntityType<UnbreakablePoweredTile> unbreakable_reactive;
  @ObjectHolder(ModCyclic.MODID + ":computer_shape")
  public static BlockEntityType<TileShapedata> computer_shape;
  @ObjectHolder(ModCyclic.MODID + ":terra_preta")
  public static BlockEntityType<TileTerraPreta> terra_preta;
  @ObjectHolder(ModCyclic.MODID + ":wireless_receiver")
  public static BlockEntityType<TileWirelessRec> wireless_receiver;
  @ObjectHolder(ModCyclic.MODID + ":wireless_transmitter")
  public static BlockEntityType<TileWirelessTransmit> wireless_transmitter;
  @ObjectHolder(ModCyclic.MODID + ":detector_item")
  public static BlockEntityType<TileDetectorItem> DETECTOR_ITEM;
  @ObjectHolder(ModCyclic.MODID + ":detector_entity")
  public static BlockEntityType<TileDetector> DETECTOR_ENTITY;
  @ObjectHolder(ModCyclic.MODID + ":solidifier")
  public static BlockEntityType<TileSolidifier> solidifier;
  @ObjectHolder(ModCyclic.MODID + ":melter")
  public static BlockEntityType<TileMelter> melter;
  @ObjectHolder(ModCyclic.MODID + ":structure")
  public static BlockEntityType<TileStructure> STRUCTURE;
  @ObjectHolder(ModCyclic.MODID + ":anvil")
  public static BlockEntityType<TileAnvilAuto> anvil;
  @ObjectHolder(ModCyclic.MODID + ":anvil_magma")
  public static BlockEntityType<TileAnvilMagma> anvil_magma;
  @ObjectHolder(ModCyclic.MODID + ":tank")
  public static BlockEntityType<TileTank> tank;
  @ObjectHolder(ModCyclic.MODID + ":battery")
  public static BlockEntityType<TileBattery> batterytile;
  @ObjectHolder(ModCyclic.MODID + ":energy_pipe")
  public static BlockEntityType<TileCableEnergy> energy_pipeTile;
  @ObjectHolder(ModCyclic.MODID + ":item_pipe")
  public static BlockEntityType<TileCableItem> item_pipeTile;
  @ObjectHolder(ModCyclic.MODID + ":fluid_pipe")
  public static BlockEntityType<TileCableFluid> fluid_pipeTile;
  @ObjectHolder(ModCyclic.MODID + ":collector")
  public static BlockEntityType<TileItemCollector> COLLECTOR_ITEM;
  @ObjectHolder(ModCyclic.MODID + ":trash")
  public static BlockEntityType<TileTrash> trashtile;
  @ObjectHolder(ModCyclic.MODID + ":peat_generator")
  public static BlockEntityType<TileGeneratorPeat> peat_generator;
  @ObjectHolder(ModCyclic.MODID + ":peat_farm")
  public static BlockEntityType<TilePeatFarm> PEAT_FARM;
  @ObjectHolder(ModCyclic.MODID + ":harvester")
  public static BlockEntityType<TileHarvester> HARVESTER;
  @ObjectHolder(ModCyclic.MODID + ":breaker")
  public static BlockEntityType<TileBreaker> breakerTile;
  @ObjectHolder(ModCyclic.MODID + ":fan")
  public static BlockEntityType<TileFan> fantile;
  @ObjectHolder(ModCyclic.MODID + ":experience_pylon")
  public static BlockEntityType<TileExpPylon> experience_pylontile;
  @ObjectHolder(ModCyclic.MODID + ":placer")
  public static BlockEntityType<TilePlacer> placer;
  @ObjectHolder(ModCyclic.MODID + ":fisher")
  public static BlockEntityType<TileFisher> fisher;
  @ObjectHolder(ModCyclic.MODID + ":user")
  public static BlockEntityType<TileUser> user;
  @ObjectHolder(ModCyclic.MODID + ":disenchanter")
  public static BlockEntityType<TileDisenchant> disenchanter;
  @ObjectHolder(ModCyclic.MODID + ":collector_fluid")
  public static BlockEntityType<TileFluidCollect> COLLECTOR_FLUID;
  @ObjectHolder(ModCyclic.MODID + ":clock")
  public static BlockEntityType<TileRedstoneClock> clock;
  @ObjectHolder(ModCyclic.MODID + ":crate")
  public static BlockEntityType<TileCrate> crate;
  @ObjectHolder(ModCyclic.MODID + ":cask")
  public static BlockEntityType<TileCrate> cask;
  @ObjectHolder(ModCyclic.MODID + ":placer_fluid")
  public static BlockEntityType<TilePlacerFluid> placer_fluid;
  @ObjectHolder(ModCyclic.MODID + ":beacon")
  public static BlockEntityType<TilePotion> beacon;
  @ObjectHolder(ModCyclic.MODID + ":battery_infinite")
  public static BlockEntityType<TileBatteryInfinite> battery_infinite;
  @ObjectHolder(ModCyclic.MODID + ":item_infinite")
  public static BlockEntityType<TileItemInfinite> item_infinite;
  @ObjectHolder(ModCyclic.MODID + ":dice")
  public static BlockEntityType<TileDice> dice;
  @ObjectHolder(ModCyclic.MODID + ":dropper")
  public static BlockEntityType<TileDropper> DROPPER;
  @ObjectHolder(ModCyclic.MODID + ":forester")
  public static BlockEntityType<TileForester> FORESTER;
  @ObjectHolder(ModCyclic.MODID + ":miner")
  public static BlockEntityType<TileMiner> MINER;
  @ObjectHolder(ModCyclic.MODID + ":screen")
  public static BlockEntityType<TileScreentext> screen;
  @ObjectHolder(ModCyclic.MODID + ":uncrafter")
  public static BlockEntityType<TileUncraft> uncrafter;
  @ObjectHolder(ModCyclic.MODID + ":crafter")
  public static BlockEntityType<TileCrafter> crafter;
  @ObjectHolder(ModCyclic.MODID + ":conveyor")
  public static BlockEntityType<TileConveyor> conveyor;
  @ObjectHolder(ModCyclic.MODID + ":ender_shelf")
  public static BlockEntityType<TileEnderShelf> ender_shelf;
  @ObjectHolder(ModCyclic.MODID + ":ender_controller")
  public static BlockEntityType<TileEnderCtrl> ender_controller;
  @ObjectHolder(ModCyclic.MODID + ":laser")
  public static BlockEntityType<TileLaser> laser;
  @ObjectHolder(ModCyclic.MODID + ":workbench")
  public static BlockEntityType<TileWorkbench> workbench;
}
