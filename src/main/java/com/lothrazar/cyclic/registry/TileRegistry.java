package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.anvil.TileAnvilAuto;
import com.lothrazar.cyclic.block.anvilmagma.TileAnvilMagma;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.block.beaconpotion.TilePotion;
import com.lothrazar.cyclic.block.breaker.TileBreaker;
import com.lothrazar.cyclic.block.cable.energy.TileCableEnergy;
import com.lothrazar.cyclic.block.cable.fluid.TileCableFluid;
import com.lothrazar.cyclic.block.cable.item.TileCableItem;
import com.lothrazar.cyclic.block.clock.TileRedstoneClock;
import com.lothrazar.cyclic.block.collectfluid.TileFluidCollect;
import com.lothrazar.cyclic.block.collectitem.TileCollector;
import com.lothrazar.cyclic.block.crafter.TileCrafter;
import com.lothrazar.cyclic.block.crate.TileCrate;
import com.lothrazar.cyclic.block.creativebattery.TileBatteryInfinite;
import com.lothrazar.cyclic.block.creativeitem.TileItemInfinite;
import com.lothrazar.cyclic.block.detectorentity.TileDetector;
import com.lothrazar.cyclic.block.detectoritem.TileDetectorItem;
import com.lothrazar.cyclic.block.dice.TileDice;
import com.lothrazar.cyclic.block.disenchant.TileDisenchant;
import com.lothrazar.cyclic.block.dropper.TileDropper;
import com.lothrazar.cyclic.block.expcollect.TileExpPylon;
import com.lothrazar.cyclic.block.fan.TileFan;
import com.lothrazar.cyclic.block.fishing.TileFisher;
import com.lothrazar.cyclic.block.forester.TileForester;
import com.lothrazar.cyclic.block.generator.TilePeatGenerator;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.block.magnet.TileMagnet;
import com.lothrazar.cyclic.block.melter.TileMelter;
import com.lothrazar.cyclic.block.miner.TileMiner;
import com.lothrazar.cyclic.block.placer.TilePlacer;
import com.lothrazar.cyclic.block.placerfluid.TilePlacerFluid;
import com.lothrazar.cyclic.block.planter.TilePlanter;
import com.lothrazar.cyclic.block.screen.TileScreentext;
import com.lothrazar.cyclic.block.shapebuilder.TileStructure;
import com.lothrazar.cyclic.block.solidifier.TileSolidifier;
import com.lothrazar.cyclic.block.tank.TileTank;
import com.lothrazar.cyclic.block.trash.TileTrash;
import com.lothrazar.cyclic.block.uncrafter.TileUncraft;
import com.lothrazar.cyclic.block.user.TileUser;
import com.lothrazar.cyclic.block.wirelessredstone.TileWirelessRec;
import com.lothrazar.cyclic.block.wirelessredstone.TileWirelessTransmit;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileRegistry {

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
  public static TileEntityType<TileCollector> collectortile;
  @ObjectHolder(ModCyclic.MODID + ":trash")
  public static TileEntityType<TileTrash> trashtile;
  @ObjectHolder(ModCyclic.MODID + ":peat_generator")
  public static TileEntityType<TilePeatGenerator> peat_generator;
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
  @ObjectHolder(ModCyclic.MODID + ":crafter")
  public static TileEntityType<TileCrafter> crafter;
  @ObjectHolder(ModCyclic.MODID + ":battery_infinite")
  public static TileEntityType<TileBatteryInfinite> battery_infinite;
  @ObjectHolder(ModCyclic.MODID + ":item_infinite")
  public static TileEntityType<TileItemInfinite> item_infinite;
  @ObjectHolder(ModCyclic.MODID + ":dice")
  public static TileEntityType<TileDice> dice;
  @ObjectHolder(ModCyclic.MODID + ":dropper")
  public static TileEntityType<TileDropper> dropper;
  @ObjectHolder(ModCyclic.MODID + ":planter")
  public static TileEntityType<TilePlanter> planter;
  @ObjectHolder(ModCyclic.MODID + ":forester")
  public static TileEntityType<TileForester> forester;
  @ObjectHolder(ModCyclic.MODID + ":magnet")
  public static TileEntityType<TileMagnet> magnet;
  @ObjectHolder(ModCyclic.MODID + ":miner")
  public static TileEntityType<TileMiner> miner;
  @ObjectHolder(ModCyclic.MODID + ":screen")
  public static TileEntityType<TileScreentext> screen;
  @ObjectHolder(ModCyclic.MODID + ":uncrafter")
  public static TileEntityType<TileUncraft> uncrafter;
}