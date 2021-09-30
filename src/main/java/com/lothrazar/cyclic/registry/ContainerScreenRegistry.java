package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.anvil.ContainerAnvil;
import com.lothrazar.cyclic.block.anvilmagma.ContainerAnvilMagma;
import com.lothrazar.cyclic.block.anvilvoid.ContainerAnvilVoid;
import com.lothrazar.cyclic.block.battery.ContainerBattery;
import com.lothrazar.cyclic.block.beaconpotion.ContainerPotion;
import com.lothrazar.cyclic.block.breaker.ContainerBreaker;
import com.lothrazar.cyclic.block.cable.fluid.ContainerCableFluid;
import com.lothrazar.cyclic.block.cable.item.ContainerCableItem;
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
import com.lothrazar.cyclic.block.fishing.ContainerFisher;
import com.lothrazar.cyclic.block.forester.ContainerForester;
import com.lothrazar.cyclic.block.generatorfluid.ContainerGeneratorFluid;
import com.lothrazar.cyclic.block.generatorfood.ContainerGeneratorFood;
import com.lothrazar.cyclic.block.generatorfuel.ContainerGeneratorFuel;
import com.lothrazar.cyclic.block.generatoritem.ContainerGeneratorDrops;
import com.lothrazar.cyclic.block.generatorpeat.ContainerGeneratorPeat;
import com.lothrazar.cyclic.block.harvester.ContainerHarvester;
import com.lothrazar.cyclic.block.laser.ContainerLaser;
import com.lothrazar.cyclic.block.melter.ContainerMelter;
import com.lothrazar.cyclic.block.miner.ContainerMiner;
import com.lothrazar.cyclic.block.packager.ContainerPackager;
import com.lothrazar.cyclic.block.peatfarm.ContainerPeatFarm;
import com.lothrazar.cyclic.block.placer.ContainerPlacer;
import com.lothrazar.cyclic.block.placerfluid.ContainerPlacerFluid;
import com.lothrazar.cyclic.block.screen.ContainerScreentext;
import com.lothrazar.cyclic.block.shapebuilder.ContainerStructure;
import com.lothrazar.cyclic.block.shapedata.ContainerShapedata;
import com.lothrazar.cyclic.block.solidifier.ContainerSolidifier;
import com.lothrazar.cyclic.block.soundrecord.ContainerSoundRecorder;
import com.lothrazar.cyclic.block.uncrafter.ContainerUncraft;
import com.lothrazar.cyclic.block.user.ContainerUser;
import com.lothrazar.cyclic.block.wireless.energy.ContainerWirelessEnergy;
import com.lothrazar.cyclic.block.wireless.fluid.ContainerWirelessFluid;
import com.lothrazar.cyclic.block.wireless.item.ContainerWirelessItem;
import com.lothrazar.cyclic.block.wireless.redstone.ContainerTransmit;
import com.lothrazar.cyclic.block.workbench.ContainerWorkbench;
import com.lothrazar.cyclic.item.crafting.CraftingBagContainer;
import com.lothrazar.cyclic.item.craftingsimple.CraftingStickContainer;
import com.lothrazar.cyclic.item.datacard.filter.ContainerFilterCard;
import com.lothrazar.cyclic.item.enderbook.ContainerEnderBook;
import com.lothrazar.cyclic.item.inventorycake.ContainerCake;
import com.lothrazar.cyclic.item.storagebag.ContainerStorageBag;
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
    //
    // Blocks with containers
    //
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerItemCollector(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("collector"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerGeneratorPeat(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("peat_generator"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerPeatFarm(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("peat_farm"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerBattery(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("battery"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerHarvester(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("harvester"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerAnvil(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("anvil"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerPlacer(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("placer"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerStructure(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("structure"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerMelter(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("melter"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerSolidifier(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("solidifier"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerBreaker(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("breaker"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerExpPylon(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("experience_pylon"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerUser(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("user"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerDetector(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("detector_entity"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerDetectorItem(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("detector_item"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerDisenchant(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("disenchanter"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerTransmit(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("wireless_transmitter"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerClock(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("clock"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerCrate(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("crate"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerPlacerFluid(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("placer_fluid"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerFluidCollect(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("collector_fluid"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerFan(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("fan"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerPotion(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("beacon"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerDropper(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("dropper"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerForester(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("forester"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerMiner(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("miner"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerScreentext(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("screen"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerAnvilMagma(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("anvil_magma"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerUncraft(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("uncrafter"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerCrafter(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("crafter"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerShapedata(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("computer_shape"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerWorkbench(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("workbench"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerFisher(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("fisher"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerLaser(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("laser"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerCableItem(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("item_pipe"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerCableFluid(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("fluid_pipe"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerAnvilVoid(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("anvil_void"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerWirelessEnergy(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("wireless_energy"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerWirelessItem(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("wireless_item"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerWirelessFluid(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("wireless_fluid"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerGeneratorFuel(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("generator_fuel"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerGeneratorFood(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("generator_food"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerGeneratorDrops(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("generator_item"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerGeneratorFluid(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("generator_fluid"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerPackager(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("packager"));
    r.register(IForgeContainerType.create((windowId, inv, data) -> {
      return new ContainerSoundRecorder(windowId, inv.player.world, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("sound_recorder"));
    //
    //  Items with containers
    //
    r.register(IForgeContainerType.create(((windowId, inv, data) -> new ContainerEnderBook(windowId, inv, inv.player))).setRegistryName("ender_book"));
    r.register(IForgeContainerType.create(((windowId, inv, data) -> new ContainerStorageBag(windowId, inv, inv.player))).setRegistryName("storage_bag"));
    r.register(IForgeContainerType.create(((windowId, inv, data) -> new CraftingBagContainer(windowId, inv, inv.player))).setRegistryName("crafting_bag"));
    r.register(IForgeContainerType.create(((windowId, inv, data) -> new CraftingStickContainer(windowId, inv, inv.player, null))).setRegistryName("crafting_stick"));
    r.register(IForgeContainerType.create(((windowId, inv, data) -> new ContainerFilterCard(windowId, inv, inv.player))).setRegistryName("filter_data"));
    r.register(IForgeContainerType.create(((windowId, inv, data) -> new ContainerCake(windowId, inv, inv.player))).setRegistryName("inventory_cake"));
  }

  @ObjectHolder(ModCyclic.MODID + ":sound_recorder")
  public static ContainerType<ContainerSoundRecorder> SOUND_RECORDER;
  @ObjectHolder(ModCyclic.MODID + ":generator_food")
  public static ContainerType<ContainerGeneratorFood> GENERATOR_FOOD;
  @ObjectHolder(ModCyclic.MODID + ":generator_fuel")
  public static ContainerType<ContainerGeneratorFuel> generator_fuel;
  @ObjectHolder(ModCyclic.MODID + ":inventory_cake")
  public static ContainerType<ContainerCake> inventory_cake;
  @ObjectHolder(ModCyclic.MODID + ":wireless_fluid")
  public static ContainerType<ContainerWirelessFluid> wireless_fluid;
  @ObjectHolder(ModCyclic.MODID + ":wireless_item")
  public static ContainerType<ContainerWirelessItem> wireless_item;
  @ObjectHolder(ModCyclic.MODID + ":wireless_energy")
  public static ContainerType<ContainerWirelessEnergy> wireless_energy;
  @ObjectHolder(ModCyclic.MODID + ":ender_book")
  public static ContainerType<ContainerEnderBook> ender_book;
  @ObjectHolder(ModCyclic.MODID + ":filter_data")
  public static ContainerType<ContainerFilterCard> filter_data;
  @ObjectHolder(ModCyclic.MODID + ":fluid_pipe")
  public static ContainerType<ContainerCableFluid> fluid_pipe;
  @ObjectHolder(ModCyclic.MODID + ":item_pipe")
  public static ContainerType<ContainerCableItem> item_pipe;
  @ObjectHolder(ModCyclic.MODID + ":laser")
  public static ContainerType<ContainerLaser> laser;
  @ObjectHolder(ModCyclic.MODID + ":fisher")
  public static ContainerType<ContainerFisher> fisher;
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
  @ObjectHolder(ModCyclic.MODID + ":anvil_void")
  public static ContainerType<ContainerAnvilVoid> anvil_void;
  @ObjectHolder(ModCyclic.MODID + ":battery")
  public static ContainerType<ContainerBattery> batteryCont;
  @ObjectHolder(ModCyclic.MODID + ":collector")
  public static ContainerType<ContainerItemCollector> collector;
  @ObjectHolder(ModCyclic.MODID + ":peat_generator")
  public static ContainerType<ContainerGeneratorPeat> generatorCont;
  @ObjectHolder(ModCyclic.MODID + ":peat_farm")
  public static ContainerType<ContainerPeatFarm> peat_farm;
  @ObjectHolder(ModCyclic.MODID + ":harvester")
  public static ContainerType<ContainerHarvester> harvester;
  @ObjectHolder(ModCyclic.MODID + ":experience_pylon")
  public static ContainerType<ContainerExpPylon> EXPERIENCE_PYLON;
  @ObjectHolder(ModCyclic.MODID + ":user")
  public static ContainerType<ContainerUser> USER;
  @ObjectHolder(ModCyclic.MODID + ":detector_entity")
  public static ContainerType<ContainerDetector> DETECTOR_ENTITY;
  @ObjectHolder(ModCyclic.MODID + ":detector_item")
  public static ContainerType<ContainerDetectorItem> DETECTOR_ITEM;
  @ObjectHolder(ModCyclic.MODID + ":disenchanter")
  public static ContainerType<ContainerDisenchant> DISENCHANTER;
  @ObjectHolder(ModCyclic.MODID + ":wireless_transmitter")
  public static ContainerType<ContainerTransmit> WIRELESS_TRANSMITTER;
  @ObjectHolder(ModCyclic.MODID + ":clock")
  public static ContainerType<ContainerClock> CLOCK;
  @ObjectHolder(ModCyclic.MODID + ":crate")
  public static ContainerType<ContainerCrate> CRATE;
  @ObjectHolder(ModCyclic.MODID + ":placer_fluid")
  public static ContainerType<ContainerPlacerFluid> PLACER_FLUID;
  @ObjectHolder(ModCyclic.MODID + ":collector_fluid")
  public static ContainerType<ContainerFluidCollect> COLLECTOR_FLUID;
  @ObjectHolder(ModCyclic.MODID + ":fan")
  public static ContainerType<ContainerFan> FAN;
  @ObjectHolder(ModCyclic.MODID + ":uncrafter")
  public static ContainerType<ContainerUncraft> UNCRAFT;
  @ObjectHolder(ModCyclic.MODID + ":crafter")
  public static ContainerType<ContainerCrafter> CRAFTER;
  @ObjectHolder(ModCyclic.MODID + ":anvil_magma")
  public static ContainerType<ContainerAnvilMagma> ANVIL_MAGMA;
  @ObjectHolder(ModCyclic.MODID + ":beacon")
  public static ContainerType<ContainerPotion> BEACON;
  @ObjectHolder(ModCyclic.MODID + ":dropper")
  public static ContainerType<ContainerDropper> DROPPER;
  @ObjectHolder(ModCyclic.MODID + ":forester")
  public static ContainerType<ContainerForester> FORESTER;
  @ObjectHolder(ModCyclic.MODID + ":miner")
  public static ContainerType<ContainerMiner> MINER;
  @ObjectHolder(ModCyclic.MODID + ":screen")
  public static ContainerType<ContainerScreentext> SCREEN;
  @ObjectHolder(ModCyclic.MODID + ":storage_bag")
  public static ContainerType<ContainerStorageBag> STORAGE_BAG;
  @ObjectHolder(ModCyclic.MODID + ":crafting_bag")
  public static ContainerType<CraftingBagContainer> CRAFTING_BAG;
  @ObjectHolder(ModCyclic.MODID + ":crafting_stick")
  public static ContainerType<CraftingStickContainer> CRAFTING_STICK;
  @ObjectHolder(ModCyclic.MODID + ":workbench")
  public static ContainerType<ContainerWorkbench> WORKBENCH;
  @ObjectHolder(ModCyclic.MODID + ":packager")
  public static ContainerType<ContainerPackager> PACKAGER;
  @ObjectHolder(ModCyclic.MODID + ":generator_fluid")
  public static ContainerType<ContainerGeneratorFluid> GENERATOR_FLUID;
  @ObjectHolder(ModCyclic.MODID + ":generator_item")
  public static ContainerType<ContainerGeneratorDrops> GENERATOR_ITEM;
}
