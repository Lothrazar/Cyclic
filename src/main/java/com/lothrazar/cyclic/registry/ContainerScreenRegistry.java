package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.anvil.ContainerAnvil;
import com.lothrazar.cyclic.block.anvilmagma.ContainerAnvilMagma;
import com.lothrazar.cyclic.block.battery.ContainerBattery;
import com.lothrazar.cyclic.block.beaconpotion.ContainerPotion;
import com.lothrazar.cyclic.block.breaker.ContainerBreaker;
import com.lothrazar.cyclic.block.clock.ContainerClock;
import com.lothrazar.cyclic.block.collectfluid.ContainerFluidCollect;
import com.lothrazar.cyclic.block.collectitem.ContainerItemCollector;
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
import com.lothrazar.cyclic.block.peatfarm.ContainerPeatFarm;
import com.lothrazar.cyclic.block.placer.ContainerPlacer;
import com.lothrazar.cyclic.block.placerfluid.ContainerPlacerFluid;
import com.lothrazar.cyclic.block.screen.ContainerScreentext;
import com.lothrazar.cyclic.block.shapebuilder.ContainerStructure;
import com.lothrazar.cyclic.block.shapedata.ContainerShapedata;
import com.lothrazar.cyclic.block.solidifier.ContainerSolidifier;
import com.lothrazar.cyclic.block.uncrafter.ContainerUncraft;
import com.lothrazar.cyclic.block.user.ContainerUser;
import com.lothrazar.cyclic.block.wirelessredstone.ContainerTransmit;
import com.lothrazar.cyclic.block.workbench.ContainerWorkbench;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerScreenRegistry {

  @SubscribeEvent
  public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
    IForgeRegistry<ContainerType<?>> r = event.getRegistry();
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerItemCollector(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("collector"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerGenerator(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("peat_generator"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerPeatFarm(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("peat_farm"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerBattery(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("battery"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerHarvester(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("harvester"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerAnvil(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("anvil"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerPlacer(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("placer"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerStructure(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("structure"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerMelter(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("melter"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerSolidifier(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("solidifier"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerBreaker(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("breaker"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerExpPylon(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("experience_pylon"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerUser(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("user"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerDetector(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("detector_entity"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerDetectorItem(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("detector_item"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerDisenchant(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("disenchanter"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerTransmit(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("wireless_transmitter"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerClock(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("clock"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerCrate(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("crate"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerPlacerFluid(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("placer_fluid"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerFluidCollect(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("collector_fluid"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerFan(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("fan"));
    //
    //
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerPotion(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("beacon"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerDropper(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("dropper"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerForester(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("forester"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerMiner(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("miner"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerScreentext(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("screen"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerAnvilMagma(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("anvil_magma"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerUncraft(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("uncrafter"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerCrafter(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("crafter"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerShapedata(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("computer_shape"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerWorkbench(windowId, ModCyclic.proxy.getClientWorld(), data.readBlockPos(), inv, ModCyclic.proxy.getClientPlayer());
    }).setRegistryName("workbench"));
  }

  @ObjectHolder(ModCyclic.MODID + ":computer_shape")
  public static ContainerType<ContainerShapedata> computer_shape;
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
  public static ContainerType<ContainerItemCollector> collector;
  @ObjectHolder(ModCyclic.MODID + ":peat_generator")
  public static ContainerType<ContainerGenerator> generatorCont;
  @ObjectHolder(ModCyclic.MODID + ":peat_farm")
  public static ContainerType<ContainerPeatFarm> peat_farm;
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
  @ObjectHolder(ModCyclic.MODID + ":uncrafter")
  public static ContainerType<ContainerUncraft> uncraft;
  @ObjectHolder(ModCyclic.MODID + ":crafter")
  public static ContainerType<ContainerCrafter> crafter;
  @ObjectHolder(ModCyclic.MODID + ":anvil_magma")
  public static ContainerType<ContainerAnvilMagma> anvil_magma;
  @ObjectHolder(ModCyclic.MODID + ":beacon")
  public static ContainerType<ContainerPotion> beacon;
  @ObjectHolder(ModCyclic.MODID + ":dropper")
  public static ContainerType<ContainerDropper> dropper;
  @ObjectHolder(ModCyclic.MODID + ":forester")
  public static ContainerType<ContainerForester> forester;
  @ObjectHolder(ModCyclic.MODID + ":miner")
  public static ContainerType<ContainerMiner> miner;
  @ObjectHolder(ModCyclic.MODID + ":screen")
  public static ContainerType<ContainerScreentext> screen;
  @ObjectHolder(ModCyclic.MODID + ":workbench")
  public static ContainerType<ContainerWorkbench> workbench;
}