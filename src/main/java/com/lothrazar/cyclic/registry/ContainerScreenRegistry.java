package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.anvil.ContainerAnvil;
import com.lothrazar.cyclic.block.anvilmagma.ContainerAnvilMagma;
import com.lothrazar.cyclic.block.battery.ContainerBattery;
import com.lothrazar.cyclic.block.beaconpotion.ContainerPotion;
import com.lothrazar.cyclic.block.breaker.ContainerBreaker;
import com.lothrazar.cyclic.block.clock.ContainerClock;
import com.lothrazar.cyclic.block.collectfluid.ContainerFluidCollect;
import com.lothrazar.cyclic.block.collectitem.ContainerCollector;
import com.lothrazar.cyclic.block.crafter.ContainerCrafter;
import com.lothrazar.cyclic.block.crate.ContainerCrate;
import com.lothrazar.cyclic.block.detectorentity.ContainerDetector;
import com.lothrazar.cyclic.block.detectoritem.ContainerDetectorItem;
import com.lothrazar.cyclic.block.disenchant.ContainerDisenchant;
import com.lothrazar.cyclic.block.dropper.ContainerDropper;
import com.lothrazar.cyclic.block.expcollect.ContainerExpPylon;
import com.lothrazar.cyclic.block.fan.ContainerFan;
import com.lothrazar.cyclic.block.forester.ContainerForester;
import com.lothrazar.cyclic.block.generatorpeat.ContainerGenerator;
import com.lothrazar.cyclic.block.harvester.ContainerHarvester;
import com.lothrazar.cyclic.block.melter.ContainerMelter;
import com.lothrazar.cyclic.block.miner.ContainerMiner;
import com.lothrazar.cyclic.block.placer.ContainerPlacer;
import com.lothrazar.cyclic.block.placerfluid.ContainerPlacerFluid;
import com.lothrazar.cyclic.block.screen.ContainerScreentext;
import com.lothrazar.cyclic.block.shapebuilder.ContainerStructure;
import com.lothrazar.cyclic.block.solidifier.ContainerSolidifier;
import com.lothrazar.cyclic.block.uncrafter.ContainerUncraft;
import com.lothrazar.cyclic.block.user.ContainerUser;
import com.lothrazar.cyclic.block.wirelessredstone.ContainerTransmit;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerScreenRegistry {

  @ObjectHolder(ModCyclic.MODID + ":breaker")
  public static ContainerType<ContainerBreaker> breaker;
  @ObjectHolder(ModCyclic.MODID + ":solidifier")
  public static ContainerType<ContainerSolidifier> solidifier;
  @ObjectHolder(ModCyclic.MODID + ":melter")
  public static ContainerType<ContainerMelter> melter;
  @ObjectHolder(ModCyclic.MODID + ":structure")
  public static ContainerType<ContainerStructure> structure;
  @ObjectHolder(ModCyclic.MODID + ":placer")
  public static ContainerType<ContainerPlacer> placer;
  @ObjectHolder(ModCyclic.MODID + ":anvil")
  public static ContainerType<ContainerAnvil> anvil;
  @ObjectHolder(ModCyclic.MODID + ":battery")
  public static ContainerType<ContainerBattery> batteryCont;
  @ObjectHolder(ModCyclic.MODID + ":collector")
  public static ContainerType<ContainerCollector> collectortileContainer;
  @ObjectHolder(ModCyclic.MODID + ":peat_generator")
  public static ContainerType<ContainerGenerator> generatorCont;
  @ObjectHolder(ModCyclic.MODID + ":harvester")
  public static ContainerType<ContainerHarvester> harvester;
  @ObjectHolder(ModCyclic.MODID + ":experience_pylon")
  public static ContainerType<ContainerExpPylon> experience_pylon;
  @ObjectHolder(ModCyclic.MODID + ":user")
  public static ContainerType<ContainerUser> user;
  @ObjectHolder(ModCyclic.MODID + ":detector_entity")
  public static ContainerType<ContainerDetector> detector_entity;
  @ObjectHolder(ModCyclic.MODID + ":detector_item")
  public static ContainerType<ContainerDetectorItem> detector_item;
  @ObjectHolder(ModCyclic.MODID + ":disenchanter")
  public static ContainerType<ContainerDisenchant> disenchanter;
  @ObjectHolder(ModCyclic.MODID + ":wireless_transmitter")
  public static ContainerType<ContainerTransmit> wireless_transmitter;
  @ObjectHolder(ModCyclic.MODID + ":clock")
  public static ContainerType<ContainerClock> clock;
  @ObjectHolder(ModCyclic.MODID + ":crate")
  public static ContainerType<ContainerCrate> crate;
  @ObjectHolder(ModCyclic.MODID + ":placer_fluid")
  public static ContainerType<ContainerPlacerFluid> placer_fluid;
  @ObjectHolder(ModCyclic.MODID + ":collector_fluid")
  public static ContainerType<ContainerFluidCollect> collector_fluid;
  @ObjectHolder(ModCyclic.MODID + ":fan")
  public static ContainerType<ContainerFan> fan;
  //
  @ObjectHolder(ModCyclic.MODID + ":uncraft")
  public static ContainerType<ContainerUncraft> uncraft;
  @ObjectHolder(ModCyclic.MODID + ":anvil_magma")
  public static ContainerType<ContainerAnvilMagma> anvil_magma;
  @ObjectHolder(ModCyclic.MODID + ":beacon")
  public static ContainerType<ContainerPotion> beacon;
  @ObjectHolder(ModCyclic.MODID + ":crafter")
  public static ContainerType<ContainerCrafter> crafter;
  @ObjectHolder(ModCyclic.MODID + ":dropper")
  public static ContainerType<ContainerDropper> dropper;
  @ObjectHolder(ModCyclic.MODID + ":forester")
  public static ContainerType<ContainerForester> forester;
  @ObjectHolder(ModCyclic.MODID + ":miner")
  public static ContainerType<ContainerMiner> miner;
  @ObjectHolder(ModCyclic.MODID + ":screen")
  public static ContainerType<ContainerScreentext> screen;
}