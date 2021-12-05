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
import com.lothrazar.cyclic.block.crusher.ContainerCrusher;
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
import com.lothrazar.cyclic.block.soundplay.ContainerSoundPlayer;
import com.lothrazar.cyclic.block.soundrecord.ContainerSoundRecorder;
import com.lothrazar.cyclic.block.tp.ContainerTeleport;
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
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerScreenRegistry {

  @SubscribeEvent
  public static void onContainerRegistry(final RegistryEvent.Register<MenuType<?>> event) {
    IForgeRegistry<MenuType<?>> r = event.getRegistry();
    //
    // Blocks with containers
    //
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerItemCollector(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("collector"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerPeatFarm(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("peat_farm"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerBattery(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("battery"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerHarvester(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("harvester"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerAnvil(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("anvil"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerPlacer(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("placer"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerStructure(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("structure"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerMelter(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("melter"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerSolidifier(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("solidifier"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerBreaker(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("breaker"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerExpPylon(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("experience_pylon"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerUser(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("user"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerDetector(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("detector_entity"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerDetectorItem(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("detector_item"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerDisenchant(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("disenchanter"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerTransmit(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("wireless_transmitter"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerClock(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("clock"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerCrate(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("crate"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerPlacerFluid(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("placer_fluid"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerFluidCollect(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("collector_fluid"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerFan(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("fan"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerPotion(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("beacon"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerDropper(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("dropper"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerForester(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("forester"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerMiner(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("miner"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerScreentext(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("screen"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerAnvilMagma(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("anvil_magma"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerUncraft(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("uncrafter"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerCrafter(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("crafter"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerShapedata(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("computer_shape"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerWorkbench(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("workbench"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerFisher(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("fisher"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerLaser(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("laser"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerCableItem(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("item_pipe"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerCableFluid(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("fluid_pipe"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerAnvilVoid(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("anvil_void"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerWirelessEnergy(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("wireless_energy"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerWirelessItem(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("wireless_item"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerWirelessFluid(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("wireless_fluid"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerGeneratorFuel(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("generator_fuel"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerGeneratorFood(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("generator_food"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerGeneratorDrops(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("generator_item"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerGeneratorFluid(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("generator_fluid"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerPackager(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("packager"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerSoundRecorder(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("sound_recorder"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerSoundPlayer(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("sound_player"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerCrusher(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("crusher"));
    r.register(IForgeMenuType.create((windowId, inv, data) -> {
      return new ContainerTeleport(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }).setRegistryName("teleport"));
    //
    //  Items with containers
    //
    r.register(IForgeMenuType.create(((windowId, inv, data) -> new ContainerEnderBook(windowId, inv, inv.player))).setRegistryName("ender_book"));
    r.register(IForgeMenuType.create(((windowId, inv, data) -> new ContainerStorageBag(windowId, inv, inv.player))).setRegistryName("storage_bag"));
    r.register(IForgeMenuType.create(((windowId, inv, data) -> new CraftingBagContainer(windowId, inv, inv.player))).setRegistryName("crafting_bag"));
    r.register(IForgeMenuType.create(((windowId, inv, data) -> new CraftingStickContainer(windowId, inv, inv.player, null))).setRegistryName("crafting_stick"));
    r.register(IForgeMenuType.create(((windowId, inv, data) -> new ContainerFilterCard(windowId, inv, inv.player))).setRegistryName("filter_data"));
    r.register(IForgeMenuType.create(((windowId, inv, data) -> new ContainerCake(windowId, inv, inv.player))).setRegistryName("inventory_cake"));
  }

  @ObjectHolder(ModCyclic.MODID + ":teleport")
  public static MenuType<ContainerTeleport> TELEPORT;
  @ObjectHolder(ModCyclic.MODID + ":crusher")
  public static MenuType<ContainerCrusher> CRUSHER;
  @ObjectHolder(ModCyclic.MODID + ":sound_player")
  public static MenuType<ContainerSoundPlayer> SOUND_PLAYER;
  @ObjectHolder(ModCyclic.MODID + ":sound_recorder")
  public static MenuType<ContainerSoundRecorder> SOUND_RECORDER;
  @ObjectHolder(ModCyclic.MODID + ":generator_food")
  public static MenuType<ContainerGeneratorFood> GENERATOR_FOOD;
  @ObjectHolder(ModCyclic.MODID + ":generator_fuel")
  public static MenuType<ContainerGeneratorFuel> generator_fuel;
  @ObjectHolder(ModCyclic.MODID + ":inventory_cake")
  public static MenuType<ContainerCake> inventory_cake;
  @ObjectHolder(ModCyclic.MODID + ":wireless_fluid")
  public static MenuType<ContainerWirelessFluid> wireless_fluid;
  @ObjectHolder(ModCyclic.MODID + ":wireless_item")
  public static MenuType<ContainerWirelessItem> wireless_item;
  @ObjectHolder(ModCyclic.MODID + ":wireless_energy")
  public static MenuType<ContainerWirelessEnergy> wireless_energy;
  @ObjectHolder(ModCyclic.MODID + ":ender_book")
  public static MenuType<ContainerEnderBook> ender_book;
  @ObjectHolder(ModCyclic.MODID + ":filter_data")
  public static MenuType<ContainerFilterCard> filter_data;
  @ObjectHolder(ModCyclic.MODID + ":fluid_pipe")
  public static MenuType<ContainerCableFluid> fluid_pipe;
  @ObjectHolder(ModCyclic.MODID + ":item_pipe")
  public static MenuType<ContainerCableItem> item_pipe;
  @ObjectHolder(ModCyclic.MODID + ":laser")
  public static MenuType<ContainerLaser> laser;
  @ObjectHolder(ModCyclic.MODID + ":fisher")
  public static MenuType<ContainerFisher> fisher;
  @ObjectHolder(ModCyclic.MODID + ":computer_shape")
  public static MenuType<ContainerShapedata> computer_shape;
  @ObjectHolder(ModCyclic.MODID + ":breaker")
  public static MenuType<ContainerBreaker> breaker;
  @ObjectHolder(ModCyclic.MODID + ":solidifier")
  public static MenuType<ContainerSolidifier> solidifier;
  @ObjectHolder(ModCyclic.MODID + ":melter")
  public static MenuType<ContainerMelter> melter;
  @ObjectHolder(ModCyclic.MODID + ":structure")
  public static MenuType<ContainerStructure> structure;
  @ObjectHolder(ModCyclic.MODID + ":placer")
  public static MenuType<ContainerPlacer> placer;
  @ObjectHolder(ModCyclic.MODID + ":anvil")
  public static MenuType<ContainerAnvil> anvil;
  @ObjectHolder(ModCyclic.MODID + ":anvil_void")
  public static MenuType<ContainerAnvilVoid> anvil_void;
  @ObjectHolder(ModCyclic.MODID + ":battery")
  public static MenuType<ContainerBattery> batteryCont;
  @ObjectHolder(ModCyclic.MODID + ":collector")
  public static MenuType<ContainerItemCollector> collector;
  @ObjectHolder(ModCyclic.MODID + ":peat_farm")
  public static MenuType<ContainerPeatFarm> peat_farm;
  @ObjectHolder(ModCyclic.MODID + ":harvester")
  public static MenuType<ContainerHarvester> harvester;
  @ObjectHolder(ModCyclic.MODID + ":experience_pylon")
  public static MenuType<ContainerExpPylon> EXPERIENCE_PYLON;
  @ObjectHolder(ModCyclic.MODID + ":user")
  public static MenuType<ContainerUser> USER;
  @ObjectHolder(ModCyclic.MODID + ":detector_entity")
  public static MenuType<ContainerDetector> DETECTOR_ENTITY;
  @ObjectHolder(ModCyclic.MODID + ":detector_item")
  public static MenuType<ContainerDetectorItem> DETECTOR_ITEM;
  @ObjectHolder(ModCyclic.MODID + ":disenchanter")
  public static MenuType<ContainerDisenchant> DISENCHANTER;
  @ObjectHolder(ModCyclic.MODID + ":wireless_transmitter")
  public static MenuType<ContainerTransmit> WIRELESS_TRANSMITTER;
  @ObjectHolder(ModCyclic.MODID + ":clock")
  public static MenuType<ContainerClock> CLOCK;
  @ObjectHolder(ModCyclic.MODID + ":crate")
  public static MenuType<ContainerCrate> CRATE;
  @ObjectHolder(ModCyclic.MODID + ":placer_fluid")
  public static MenuType<ContainerPlacerFluid> PLACER_FLUID;
  @ObjectHolder(ModCyclic.MODID + ":collector_fluid")
  public static MenuType<ContainerFluidCollect> COLLECTOR_FLUID;
  @ObjectHolder(ModCyclic.MODID + ":fan")
  public static MenuType<ContainerFan> FAN;
  @ObjectHolder(ModCyclic.MODID + ":uncrafter")
  public static MenuType<ContainerUncraft> UNCRAFT;
  @ObjectHolder(ModCyclic.MODID + ":crafter")
  public static MenuType<ContainerCrafter> CRAFTER;
  @ObjectHolder(ModCyclic.MODID + ":anvil_magma")
  public static MenuType<ContainerAnvilMagma> ANVIL_MAGMA;
  @ObjectHolder(ModCyclic.MODID + ":beacon")
  public static MenuType<ContainerPotion> BEACON;
  @ObjectHolder(ModCyclic.MODID + ":dropper")
  public static MenuType<ContainerDropper> DROPPER;
  @ObjectHolder(ModCyclic.MODID + ":forester")
  public static MenuType<ContainerForester> FORESTER;
  @ObjectHolder(ModCyclic.MODID + ":miner")
  public static MenuType<ContainerMiner> MINER;
  @ObjectHolder(ModCyclic.MODID + ":screen")
  public static MenuType<ContainerScreentext> SCREEN;
  @ObjectHolder(ModCyclic.MODID + ":storage_bag")
  public static MenuType<ContainerStorageBag> STORAGE_BAG;
  @ObjectHolder(ModCyclic.MODID + ":crafting_bag")
  public static MenuType<CraftingBagContainer> CRAFTING_BAG;
  @ObjectHolder(ModCyclic.MODID + ":crafting_stick")
  public static MenuType<CraftingStickContainer> CRAFTING_STICK;
  @ObjectHolder(ModCyclic.MODID + ":workbench")
  public static MenuType<ContainerWorkbench> WORKBENCH;
  @ObjectHolder(ModCyclic.MODID + ":packager")
  public static MenuType<ContainerPackager> PACKAGER;
  @ObjectHolder(ModCyclic.MODID + ":generator_fluid")
  public static MenuType<ContainerGeneratorFluid> GENERATOR_FLUID;
  @ObjectHolder(ModCyclic.MODID + ":generator_item")
  public static MenuType<ContainerGeneratorDrops> GENERATOR_ITEM;
}
