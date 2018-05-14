/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.module;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.BlockLaunch;
import com.lothrazar.cyclicmagic.block.BlockShears;
import com.lothrazar.cyclicmagic.block.BlockSoundSuppress;
import com.lothrazar.cyclicmagic.block.BlockSpikesRetractable;
import com.lothrazar.cyclicmagic.block.anvil.BlockAnvilAuto;
import com.lothrazar.cyclicmagic.block.anvil.TileEntityAnvilAuto;
import com.lothrazar.cyclicmagic.block.anvilmagma.BlockAnvilMagma;
import com.lothrazar.cyclicmagic.block.anvilmagma.TileEntityAnvilMagma;
import com.lothrazar.cyclicmagic.block.applesprout.BlockAppleCrop;
import com.lothrazar.cyclicmagic.block.arrowtarget.BlockArrowTarget;
import com.lothrazar.cyclicmagic.block.arrowtarget.TileEntityArrowTarget;
import com.lothrazar.cyclicmagic.block.autouser.BlockUser;
import com.lothrazar.cyclicmagic.block.autouser.TileEntityUser;
import com.lothrazar.cyclicmagic.block.beaconempty.BlockBeaconPowered;
import com.lothrazar.cyclicmagic.block.beaconempty.TileEntityBeaconPowered;
import com.lothrazar.cyclicmagic.block.beaconpotion.BlockBeaconPotion;
import com.lothrazar.cyclicmagic.block.beaconpotion.TileEntityBeaconPotion;
import com.lothrazar.cyclicmagic.block.bean.BlockCropMagicBean;
import com.lothrazar.cyclicmagic.block.bean.ItemMagicBean;
import com.lothrazar.cyclicmagic.block.builderpattern.BlockPatternBuilder;
import com.lothrazar.cyclicmagic.block.builderpattern.TileEntityPatternBuilder;
import com.lothrazar.cyclicmagic.block.buildershape.BlockStructureBuilder;
import com.lothrazar.cyclicmagic.block.buildershape.TileEntityStructureBuilder;
import com.lothrazar.cyclicmagic.block.buttondoorbell.BlockDoorbell;
import com.lothrazar.cyclicmagic.block.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.block.cable.energy.BlockPowerCable;
import com.lothrazar.cyclicmagic.block.cable.energy.TileEntityCablePower;
import com.lothrazar.cyclicmagic.block.cable.fluid.BlockCableFluid;
import com.lothrazar.cyclicmagic.block.cable.fluid.TileEntityFluidCable;
import com.lothrazar.cyclicmagic.block.cable.item.BlockCableItem;
import com.lothrazar.cyclicmagic.block.cable.item.TileEntityItemCable;
import com.lothrazar.cyclicmagic.block.cable.multi.BlockCableBundle;
import com.lothrazar.cyclicmagic.block.cable.multi.TileEntityCableBundle;
import com.lothrazar.cyclicmagic.block.clockredstone.BlockRedstoneClock;
import com.lothrazar.cyclicmagic.block.clockredstone.TileEntityClock;
import com.lothrazar.cyclicmagic.block.collector.BlockVacuum;
import com.lothrazar.cyclicmagic.block.collector.TileEntityVacuum;
import com.lothrazar.cyclicmagic.block.controlledminer.BlockMinerSmart;
import com.lothrazar.cyclicmagic.block.controlledminer.TileEntityControlledMiner;
import com.lothrazar.cyclicmagic.block.conveyor.BlockConveyor;
import com.lothrazar.cyclicmagic.block.conveyor.BlockConveyor.SpeedType;
import com.lothrazar.cyclicmagic.block.conveyor.BlockConveyorAngle;
import com.lothrazar.cyclicmagic.block.conveyor.BlockConveyorCorner;
import com.lothrazar.cyclicmagic.block.crafter.BlockCrafter;
import com.lothrazar.cyclicmagic.block.crafter.TileEntityCrafter;
import com.lothrazar.cyclicmagic.block.disenchanter.BlockDisenchanter;
import com.lothrazar.cyclicmagic.block.disenchanter.TileEntityDisenchanter;
import com.lothrazar.cyclicmagic.block.dropper.BlockDropperExact;
import com.lothrazar.cyclicmagic.block.dropper.TileEntityDropperExact;
import com.lothrazar.cyclicmagic.block.enchanter.BlockEnchanter;
import com.lothrazar.cyclicmagic.block.enchanter.TileEntityEnchanter;
import com.lothrazar.cyclicmagic.block.enchantlibrary.BlockLibrary;
import com.lothrazar.cyclicmagic.block.enchantlibrary.BlockLibraryController;
import com.lothrazar.cyclicmagic.block.enchantlibrary.TileEntityLibrary;
import com.lothrazar.cyclicmagic.block.entitydetector.BlockDetector;
import com.lothrazar.cyclicmagic.block.entitydetector.TileEntityDetector;
import com.lothrazar.cyclicmagic.block.exppylon.BlockXpPylon;
import com.lothrazar.cyclicmagic.block.exppylon.ItemBlockPylon;
import com.lothrazar.cyclicmagic.block.exppylon.TileEntityXpPylon;
import com.lothrazar.cyclicmagic.block.fan.BlockFan;
import com.lothrazar.cyclicmagic.block.fan.TileEntityFan;
import com.lothrazar.cyclicmagic.block.fishing.BlockFishing;
import com.lothrazar.cyclicmagic.block.fishing.TileEntityFishing;
import com.lothrazar.cyclicmagic.block.fluidplacer.BlockFluidPlacer;
import com.lothrazar.cyclicmagic.block.fluidplacer.TileEntityFluidPlacer;
import com.lothrazar.cyclicmagic.block.forester.BlockForester;
import com.lothrazar.cyclicmagic.block.forester.TileEntityForester;
import com.lothrazar.cyclicmagic.block.harvester.BlockHarvester;
import com.lothrazar.cyclicmagic.block.harvester.TileEntityHarvester;
import com.lothrazar.cyclicmagic.block.hydrator.BlockHydrator;
import com.lothrazar.cyclicmagic.block.hydrator.ItemBlockHydrator;
import com.lothrazar.cyclicmagic.block.hydrator.RecipeHydrate;
import com.lothrazar.cyclicmagic.block.hydrator.TileEntityHydrator;
import com.lothrazar.cyclicmagic.block.interdiction.BlockMagnetAnti;
import com.lothrazar.cyclicmagic.block.interdiction.TileEntityMagnetAnti;
import com.lothrazar.cyclicmagic.block.magnetitem.BlockMagnet;
import com.lothrazar.cyclicmagic.block.magnetitem.TileEntityMagnet;
import com.lothrazar.cyclicmagic.block.miner.BlockMiner;
import com.lothrazar.cyclicmagic.block.miner.TileEntityBlockMiner;
import com.lothrazar.cyclicmagic.block.moondetector.BlockMoonDetector;
import com.lothrazar.cyclicmagic.block.moondetector.TileEntityMoon;
import com.lothrazar.cyclicmagic.block.password.BlockPassword;
import com.lothrazar.cyclicmagic.block.password.TileEntityPassword;
import com.lothrazar.cyclicmagic.block.placer.BlockPlacer;
import com.lothrazar.cyclicmagic.block.placer.TileEntityPlacer;
import com.lothrazar.cyclicmagic.block.pump.energy.BlockEnergyPump;
import com.lothrazar.cyclicmagic.block.pump.energy.TileEntityEnergyPump;
import com.lothrazar.cyclicmagic.block.pump.fluid.BlockFluidPump;
import com.lothrazar.cyclicmagic.block.pump.fluid.TileEntityFluidPump;
import com.lothrazar.cyclicmagic.block.pump.item.BlockItemPump;
import com.lothrazar.cyclicmagic.block.pump.item.TileEntityItemPump;
import com.lothrazar.cyclicmagic.block.scaffolding.BlockScaffolding;
import com.lothrazar.cyclicmagic.block.scaffolding.BlockScaffoldingReplace;
import com.lothrazar.cyclicmagic.block.scaffolding.BlockScaffoldingResponsive;
import com.lothrazar.cyclicmagic.block.scaffolding.ItemBlockScaffolding;
import com.lothrazar.cyclicmagic.block.screen.BlockScreen;
import com.lothrazar.cyclicmagic.block.screen.TileEntityScreen;
import com.lothrazar.cyclicmagic.block.sorting.BlockItemCableSort;
import com.lothrazar.cyclicmagic.block.sorting.TileEntityItemCableSort;
import com.lothrazar.cyclicmagic.block.sprinkler.BlockSprinkler;
import com.lothrazar.cyclicmagic.block.sprinkler.TileSprinkler;
import com.lothrazar.cyclicmagic.block.tank.BlockFluidTank;
import com.lothrazar.cyclicmagic.block.tank.ItemBlockFluidTank;
import com.lothrazar.cyclicmagic.block.tank.TileEntityFluidTank;
import com.lothrazar.cyclicmagic.block.trash.BlockTrash;
import com.lothrazar.cyclicmagic.block.trash.TileEntityTrash;
import com.lothrazar.cyclicmagic.block.uncrafter.BlockUncrafting;
import com.lothrazar.cyclicmagic.block.uncrafter.TileEntityUncrafter;
import com.lothrazar.cyclicmagic.block.vector.BlockVectorPlate;
import com.lothrazar.cyclicmagic.block.vector.ItemBlockVectorPlate;
import com.lothrazar.cyclicmagic.block.vector.TileEntityVector;
import com.lothrazar.cyclicmagic.block.wireless.BlockRedstoneWireless;
import com.lothrazar.cyclicmagic.block.wireless.ItemBlockWireless;
import com.lothrazar.cyclicmagic.block.wireless.TileEntityWirelessRec;
import com.lothrazar.cyclicmagic.block.wireless.TileEntityWirelessTr;
import com.lothrazar.cyclicmagic.block.workbench.BlockWorkbench;
import com.lothrazar.cyclicmagic.block.workbench.TileEntityWorkbench;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.core.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.core.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.energy.battery.BlockBattery;
import com.lothrazar.cyclicmagic.energy.battery.ItemBlockBattery;
import com.lothrazar.cyclicmagic.energy.battery.TileEntityBattery;
import com.lothrazar.cyclicmagic.energy.battery.TileEntityBatteryInfinite;
import com.lothrazar.cyclicmagic.energy.peat.BlockPeat;
import com.lothrazar.cyclicmagic.energy.peat.ItemBiomass;
import com.lothrazar.cyclicmagic.energy.peat.ItemPeatFuel;
import com.lothrazar.cyclicmagic.energy.peat.farm.BlockPeatFarm;
import com.lothrazar.cyclicmagic.energy.peat.farm.TileEntityPeatFarm;
import com.lothrazar.cyclicmagic.energy.peat.generator.BlockPeatGenerator;
import com.lothrazar.cyclicmagic.energy.peat.generator.TileEntityPeatGenerator;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.guide.GuideRegistry;
import com.lothrazar.cyclicmagic.item.firemagic.BlockFireSafe;
import com.lothrazar.cyclicmagic.liquid.FluidsRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class BlockModule extends BaseModule implements IHasConfig {

  private boolean fragileEnabled;
  private boolean fishingBlock;
  private boolean enableBucketBlocks;
  private boolean enableShearingBlock;
  private boolean enableFan;
  private boolean entityDetector;
  private boolean disenchanter;
  private boolean autoCrafter;
  private boolean soundproofing;
  private boolean workbench;
  private boolean enablePumpAndPipes;
  private boolean enableLibrary;
  private boolean screen;
  private boolean btrash;
  private boolean fluidPlacer;
  private boolean enableUncrafter;
  private boolean enableBuilderBlock;
  private boolean enableHarvester;
  private boolean enableMiner;
  private boolean enablePlacer;
  private boolean enablePassword;
  private boolean enableMinerSmart;
  private boolean enableUser;
  private boolean enablePattern;
  private boolean expPylon;
  private boolean enableVacuum;
  private boolean enableHydrator;
  private boolean enableClock;
  private boolean enableSprinkler;
  private boolean enableSpikes;
  private boolean emptyBeacon;
  private boolean beaconPotion;
  private boolean wireless;
  private boolean forester;
  private boolean enchanter;
  private boolean anvil;
  private boolean enablePeat;
  private boolean enableConveyor;
  private boolean launchPads;
  private boolean enableMagnet;
  private boolean enableInterdict;
  private boolean vectorPlate;
  private boolean enableApple;
  private boolean enableBeans;
  private boolean enableMilk;
  private boolean enablePoison;
  private boolean dispenserPowered;
  private boolean anvilMagma;
  private boolean battery;
  private boolean etarget;
  private boolean moon;

  /**
   * - create the object (or just a Feature if none exists) and submit to _______ registry listing
   * 
   * - config runs: it syncConfig on listing
   * 
   * 
   * 
   * - new subInterface from IFCONFIG actually: also needs isEnabled() and preInit(), init(), postInit() - AN ACTUAL MODULE! HOW BOUT DAT!; call it Feature module
   * 
   * - onPreInit runs everywhere: loop through _______. check if is enabled and run their preInit Feature phase (doing exactly what this if statement does)
   * 
   * 
   * 
   * 
   */
  @Override
  public void onPreInit() {
    super.onPreInit();
    BlockRegistry.registerBlock(new BlockDoorbell(), "doorbell_simple", GuideCategory.BLOCK);
    if (moon) {
      BlockRegistry.registerBlock(new BlockMoonDetector(), "moon_sensor", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityMoon.class, "moon_sensor_te");
    }
    if (etarget) {
      BlockRegistry.registerBlock(new BlockArrowTarget(), "target", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityArrowTarget.class, "target_te");
    }
    if (enableMilk) {
      FluidsRegistry.registerMilk();
    }
    if (enablePoison) {
      FluidsRegistry.registerPoison();
    }
    if (enableBeans) {
      BlockCropMagicBean sprout = new BlockCropMagicBean();
      BlockRegistry.registerBlock(sprout, "sprout", null);
      ItemMagicBean sprout_seed = new ItemMagicBean(sprout, Blocks.FARMLAND);
      ItemRegistry.register(sprout_seed, "sprout_seed");
      LootTableRegistry.registerLoot(sprout_seed);
      sprout.setSeed(sprout_seed);
      //      AchievementRegistry.registerItemAchievement(sprout_seed);
    }
    if (enableApple) {
      BlockAppleCrop apple = new BlockAppleCrop();
      BlockRegistry.registerBlock(apple, "apple", GuideCategory.BLOCK);
    }
    BlockFireSafe fire = new BlockFireSafe();
    BlockRegistry.registerBlock(fire, "fire_dark", null);
    if (enableInterdict) {
      BlockMagnetAnti magnet_anti_block = new BlockMagnetAnti();
      BlockRegistry.registerBlock(magnet_anti_block, "magnet_anti_block", GuideCategory.BLOCKPLATE);
      GameRegistry.registerTileEntity(TileEntityMagnetAnti.class, "magnet_anti_block_te");
    }
    if (enableMagnet) {
      BlockMagnet magnet_block = new BlockMagnet();
      BlockRegistry.registerBlock(magnet_block, "magnet_block", GuideCategory.BLOCKPLATE);
      GameRegistry.registerTileEntity(TileEntityMagnet.class, "magnet_block_te");
    }
    if (launchPads) {
      //med
      BlockLaunch plate_launch_med = new BlockLaunch(BlockLaunch.LaunchType.MEDIUM, SoundEvents.BLOCK_SLIME_FALL);
      BlockRegistry.registerBlock(plate_launch_med, "plate_launch_med", GuideCategory.BLOCKPLATE);
      //small
      BlockLaunch plate_launch_small = new BlockLaunch(BlockLaunch.LaunchType.SMALL, SoundEvents.BLOCK_SLIME_STEP);
      BlockRegistry.registerBlock(plate_launch_small, "plate_launch_small", null);
      //large
      BlockLaunch plate_launch_large = new BlockLaunch(BlockLaunch.LaunchType.LARGE, SoundEvents.BLOCK_SLIME_BREAK);
      BlockRegistry.registerBlock(plate_launch_large, "plate_launch_large", null);
    }
    if (enableConveyor) {
      //corner
      BlockConveyorCorner plate_push_corner = new BlockConveyorCorner(SpeedType.MEDIUM);
      BlockRegistry.registerBlock(plate_push_corner, null, "plate_push_corner", GuideCategory.BLOCKPLATE, false);
      //angle
      BlockConveyorAngle plate_push_med_angle = new BlockConveyorAngle(SpeedType.MEDIUM);
      BlockRegistry.registerBlock(plate_push_med_angle, null, "plate_push_med_angle", GuideCategory.BLOCKPLATE, false);
      //main
      BlockConveyor plate_push = new BlockConveyor(plate_push_corner, plate_push_med_angle);
      BlockRegistry.registerBlock(plate_push, "plate_push", GuideCategory.BLOCKPLATE);
      plate_push_corner.setDrop(plate_push);
      plate_push_med_angle.setDrop(plate_push);
      //corner
      BlockConveyorCorner plate_push_fast_corner = new BlockConveyorCorner(SpeedType.LARGE);
      BlockRegistry.registerBlock(plate_push_fast_corner, null, "plate_push_fast_corner", GuideCategory.BLOCKPLATE, false);
      //angle
      BlockConveyorAngle plate_push_fast_angle = new BlockConveyorAngle(SpeedType.LARGE);
      BlockRegistry.registerBlock(plate_push_fast_angle, null, "plate_push_fast_angle", GuideCategory.BLOCKPLATE, false);
      //main
      BlockConveyor plate_push_fast = new BlockConveyor(plate_push_fast_corner, plate_push_fast_angle);
      BlockRegistry.registerBlock(plate_push_fast, "plate_push_fast", null);
      plate_push_fast_corner.setDrop(plate_push_fast);
      plate_push_fast_angle.setDrop(plate_push_fast);
      //corner
      BlockConveyorCorner plate_push_slow_corner = new BlockConveyorCorner(SpeedType.SMALL);
      BlockRegistry.registerBlock(plate_push_slow_corner, null, "plate_push_slow_corner", GuideCategory.BLOCKPLATE, false);
      // angle
      BlockConveyorAngle plate_push_slow_angle = new BlockConveyorAngle(SpeedType.SMALL);
      BlockRegistry.registerBlock(plate_push_slow_angle, null, "plate_push_slow_angle", GuideCategory.BLOCKPLATE, false);
      //main
      BlockConveyor plate_push_slow = new BlockConveyor(plate_push_slow_corner, plate_push_slow_angle);
      BlockRegistry.registerBlock(plate_push_slow, "plate_push_slow", null);
      plate_push_slow_corner.setDrop(plate_push_slow);
      plate_push_slow_angle.setDrop(plate_push_slow);
      //corner
      BlockConveyorCorner plate_push_slowest_corner = new BlockConveyorCorner(SpeedType.TINY);
      BlockRegistry.registerBlock(plate_push_slowest_corner, null, "plate_push_slowest_corner", GuideCategory.BLOCKPLATE, false);
      //angle
      BlockConveyorAngle plate_push_slowest_angle = new BlockConveyorAngle(SpeedType.TINY);
      BlockRegistry.registerBlock(plate_push_slowest_angle, null, "plate_push_slowest_angle", GuideCategory.BLOCKPLATE, false);
      //main
      BlockConveyor plate_push_slowest = new BlockConveyor(plate_push_slowest_corner, plate_push_slowest_angle);
      BlockRegistry.registerBlock(plate_push_slowest, "plate_push_slowest", null);
      plate_push_slowest_corner.setDrop(plate_push_slowest);
      plate_push_slowest_angle.setDrop(plate_push_slowest);
    }
    if (vectorPlate) {
      BlockVectorPlate plate_vector = new BlockVectorPlate();
      BlockRegistry.registerBlock(plate_vector, new ItemBlockVectorPlate(plate_vector), "plate_vector");
      GuideRegistry.register(GuideCategory.BLOCKPLATE, plate_vector);
      GameRegistry.registerTileEntity(TileEntityVector.class, "plate_vector_te");
      ModCyclic.instance.events.register(plate_vector);
    }
    if (forester) {
      BlockForester block_forester = new BlockForester();
      BlockRegistry.registerBlock(block_forester, "block_forester", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityForester.class, "block_forester_te");
    }
    if (wireless) {
      BlockRedstoneWireless wireless_transmitter = new BlockRedstoneWireless(BlockRedstoneWireless.WirelessType.TRANSMITTER);
      BlockRedstoneWireless wireless_receiver = new BlockRedstoneWireless(BlockRedstoneWireless.WirelessType.RECEIVER);
      BlockRegistry.registerBlock(wireless_transmitter, new ItemBlockWireless(wireless_transmitter), "wireless_transmitter", GuideCategory.BLOCK);
      BlockRegistry.registerBlock(wireless_receiver, "wireless_receiver", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityWirelessTr.class, "wireless_transmitter_te");
      GameRegistry.registerTileEntity(TileEntityWirelessRec.class, "wireless_receiver_te");
      ModCyclic.instance.events.register(BlockRedstoneWireless.class);
    }
    if (beaconPotion) {
      BlockBeaconPotion beacon_potion = new BlockBeaconPotion();
      BlockRegistry.registerBlock(beacon_potion, "beacon_potion", null);
      GameRegistry.registerTileEntity(TileEntityBeaconPotion.class, "beacon_potion_te");
    }
    if (emptyBeacon) {
      BlockBeaconPowered beacon_redstone = new BlockBeaconPowered();
      BlockRegistry.registerBlock(beacon_redstone, "beacon_redstone", null);
      GameRegistry.registerTileEntity(TileEntityBeaconPowered.class, "beacon_redstone_te");
    }
    if (enableClock) {
      BlockRedstoneClock clock = new BlockRedstoneClock();
      BlockRegistry.registerBlock(clock, "clock", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityClock.class, "clock_te");
    }
    if (enableSprinkler) {
      BlockSprinkler sprinkler = new BlockSprinkler();
      BlockRegistry.registerBlock(sprinkler, "sprinkler", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileSprinkler.class, "sprinkler_te");
    }
    if (enableSpikes) {
      BlockSpikesRetractable spikes_iron = new BlockSpikesRetractable(false);
      BlockRegistry.registerBlock(spikes_iron, "spikes_iron", GuideCategory.BLOCK);
      BlockSpikesRetractable spikes_redstone_diamond = new BlockSpikesRetractable(true);
      BlockRegistry.registerBlock(spikes_redstone_diamond, "spikes_diamond", GuideCategory.BLOCK);
    }
    if (enableVacuum) {
      BlockVacuum vacuum_block = new BlockVacuum();
      BlockRegistry.registerBlock(vacuum_block, "block_vacuum", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityVacuum.class, "vacuum_block_te");
    }
    if (enableHydrator) {
      BlockHydrator block_hydrator = new BlockHydrator();
      BlockRegistry.registerBlock(block_hydrator, new ItemBlockHydrator(block_hydrator), "block_hydrator", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityHydrator.class, "block_hydrator_te");
    }
    if (expPylon) {
      FluidsRegistry.registerExp();//it needs EXP fluid to work
      BlockXpPylon exp_pylon = new BlockXpPylon();
      BlockRegistry.registerBlock(exp_pylon, new ItemBlockPylon(exp_pylon), "exp_pylon", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityXpPylon.class, "exp_pylon_te");
    }
    if (enablePattern) {
      BlockPatternBuilder builder_pattern = new BlockPatternBuilder();
      BlockRegistry.registerBlock(builder_pattern, "builder_pattern", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityPatternBuilder.class, "builder_pattern_te");
    }
    if (enableBuilderBlock) {
      BlockStructureBuilder builder_block = new BlockStructureBuilder();
      BlockRegistry.registerBlock(builder_block, "builder_block", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityStructureBuilder.class, "builder_te");
    }
    if (enableHarvester) {
      BlockHarvester harvester_block = new BlockHarvester();
      BlockRegistry.registerBlock(harvester_block, "harvester_block", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityHarvester.class, "harveseter_te");
    }
    if (enableUncrafter) {
      BlockUncrafting uncrafting_block = new BlockUncrafting();
      BlockRegistry.registerBlock(uncrafting_block, "uncrafting_block", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityUncrafter.class, "uncrafting_block_te");
    }
    if (enableMiner) {
      BlockMiner miner_block = new BlockMiner();
      BlockRegistry.registerBlock(miner_block, "block_miner", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityBlockMiner.class, "miner_te");
    }
    if (enableMinerSmart) {
      BlockMinerSmart block_miner_smart = new BlockMinerSmart();
      BlockRegistry.registerBlock(block_miner_smart, "block_miner_smart", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityControlledMiner.class, Const.MODID + "miner_smart_te");
    }
    if (enablePlacer) {
      BlockPlacer placer_block = new BlockPlacer();
      BlockRegistry.registerBlock(placer_block, "placer_block", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityPlacer.class, "placer_block_te");
    }
    if (enablePassword) {
      BlockPassword password_block = new BlockPassword();
      BlockRegistry.registerBlock(password_block, "password_block", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityPassword.class, "password_block_te");
      ModCyclic.instance.events.register(password_block);
    }
    if (enableUser) {
      BlockUser block_user = new BlockUser();
      BlockRegistry.registerBlock(block_user, "block_user", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityUser.class, Const.MODID + "block_user_te");
    }
    if (enchanter) {
      FluidsRegistry.registerExp();
      BlockEnchanter block_enchanter = new BlockEnchanter();
      BlockRegistry.registerBlock(block_enchanter, "block_enchanter", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityEnchanter.class, Const.MODID + "block_enchanter_te");
    }
    Block block_anvil_magma = Blocks.ENCHANTING_TABLE;
    if (anvilMagma) {
      block_anvil_magma = new BlockAnvilMagma();
      BlockRegistry.registerBlock(block_anvil_magma, "block_anvil_magma", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityAnvilMagma.class, Const.MODID + "block_anvil_magma_te");
    }
    if (anvil) {
      BlockAnvilAuto block_anvil = new BlockAnvilAuto(block_anvil_magma);
      BlockRegistry.registerBlock(block_anvil, "block_anvil", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityAnvilAuto.class, Const.MODID + "block_anvil_te");
    }
    if (enablePeat) {
      //peat 
      ItemBiomass peat_biomass = new ItemBiomass();
      ItemRegistry.register(peat_biomass, "peat_biomass", GuideCategory.ITEM);
      Item peat_fuel = new ItemPeatFuel();
      ItemRegistry.register(peat_fuel, "peat_fuel", GuideCategory.ITEM);
      //
      RecipeHydrate.addRecipe(new RecipeHydrate(
          new ItemStack[] {
              new ItemStack(Items.WHEAT_SEEDS),
              new ItemStack(Blocks.RED_FLOWER, 1, OreDictionary.WILDCARD_VALUE),
              new ItemStack(Blocks.LEAVES),
              new ItemStack(Blocks.VINE) },
          new ItemStack(peat_biomass, 8)));
      //sapling one
      RecipeHydrate.addRecipe(new RecipeHydrate(
          new ItemStack[] {
              new ItemStack(Items.REEDS),
              new ItemStack(Blocks.TALLGRASS, 1, 1),
              new ItemStack(Blocks.DEADBUSH),
              new ItemStack(Blocks.SAPLING, 1, OreDictionary.WILDCARD_VALUE) },
          new ItemStack(peat_biomass, 8)));
      BlockPeat bog = new BlockPeat(null);
      BlockRegistry.registerBlock(bog, "peat_unbaked", GuideCategory.BLOCKMACHINE);
      BlockRegistry.registerBlock(new BlockPeat(peat_fuel), "peat_baked", GuideCategory.BLOCKMACHINE);
      Block peat_generator = new BlockPeatGenerator(peat_fuel);
      BlockRegistry.registerBlock(peat_generator, "peat_generator", GuideCategory.BLOCKMACHINE);
      BlockRegistry.registerBlock(new BlockPeatFarm(peat_generator), "peat_farm", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityPeatGenerator.class, Const.MODID + "peat_generator_te");
      GameRegistry.registerTileEntity(TileEntityPeatFarm.class, Const.MODID + "peat_farm_te");
      //
    }
    if (battery) {
      BlockBattery battery = new BlockBattery(false);
      BlockRegistry.registerBlock(battery, new ItemBlockBattery(battery), "battery", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityBattery.class, Const.MODID + "battery_te");
      //cheater 
      BlockRegistry.registerBlock(new BlockBattery(true), "battery_infinite", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityBatteryInfinite.class, Const.MODID + "battery_infinite_te");
    }
    if (btrash) {
      BlockTrash trash = new BlockTrash();
      BlockRegistry.registerBlock(trash, "trash", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityTrash.class, Const.MODID + "trash_te");
    }
    if (screen) {
      BlockScreen screen = new BlockScreen();
      BlockRegistry.registerBlock(screen, "block_screen", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityScreen.class, Const.MODID + "screen_te");
    }
    if (enableLibrary) {
      BlockLibrary library = new BlockLibrary();
      BlockRegistry.registerBlock(library, "block_library", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityLibrary.class, Const.MODID + "library_te");
      BlockLibraryController lc = new BlockLibraryController(library);
      BlockRegistry.registerBlock(lc, "block_library_ctrl", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(library);
    }
    if (workbench) {
      BlockWorkbench workbench = new BlockWorkbench();
      BlockRegistry.registerBlock(workbench, "block_workbench", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityWorkbench.class, Const.MODID + "workbench_te");
    }
    if (soundproofing) {
      BlockSoundSuppress block_soundproofing = new BlockSoundSuppress();
      BlockRegistry.registerBlock(block_soundproofing, "block_soundproofing", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(block_soundproofing);
    }
    if (autoCrafter) {
      BlockCrafter auto_crafter = new BlockCrafter();
      BlockRegistry.registerBlock(auto_crafter, "auto_crafter", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityCrafter.class, Const.MODID + "auto_crafter_te");
    }
    if (entityDetector) {
      BlockDetector detector = new BlockDetector();
      BlockRegistry.registerBlock(detector, "entity_detector", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityDetector.class, Const.MODID + "entity_detector_te");
    }
    if (enableFan) {
      BlockFan fan = new BlockFan();
      BlockRegistry.registerBlock(fan, "fan", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityFan.class, Const.MODID + "fan_te");
    }
    if (enableShearingBlock) {
      BlockShears block_shears = new BlockShears();
      BlockRegistry.registerBlock(block_shears, "block_shears", GuideCategory.BLOCK);
    }
    if (fragileEnabled) {
      BlockScaffolding block_fragile = new BlockScaffolding(true);
      ItemBlock ib = new ItemBlockScaffolding(block_fragile);
      BlockRegistry.registerBlock(block_fragile, ib, "block_fragile", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(ib);
      BlockScaffoldingResponsive block_fragile_auto = new BlockScaffoldingResponsive();
      ib = new ItemBlockScaffolding(block_fragile_auto);
      BlockRegistry.registerBlock(block_fragile_auto, ib, "block_fragile_auto", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(ib);
      BlockScaffoldingReplace block_fragile_weak = new BlockScaffoldingReplace();
      ib = new ItemBlockScaffolding(block_fragile_weak);
      BlockRegistry.registerBlock(block_fragile_weak, ib, "block_fragile_weak", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(ib);
    }
    if (fishingBlock) {
      BlockFishing block_fishing = new BlockFishing();
      BlockRegistry.registerBlock(block_fishing, "block_fishing", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityFishing.class, Const.MODID + "block_fishing_te");
    }
    if (disenchanter) {
      BlockDisenchanter block_disenchanter = new BlockDisenchanter();
      BlockRegistry.registerBlock(block_disenchanter, "block_disenchanter", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityDisenchanter.class, Const.MODID + "block_disenchanter_te");
    }
    if (enableBucketBlocks) {
      //tank
      BlockFluidTank block_storeempty = new BlockFluidTank();
      BlockRegistry.registerBlock(block_storeempty, new ItemBlockFluidTank(block_storeempty), "block_storeempty", null);
      GameRegistry.registerTileEntity(TileEntityFluidTank.class, "bucketstorage");
      GuideRegistry.register(GuideCategory.BLOCK, block_storeempty, null, null);
    }
    if (fluidPlacer) {
      //fluid placer
      BlockFluidPlacer placer = new BlockFluidPlacer();
      BlockRegistry.registerBlock(placer, "fluid_placer", null);
      GameRegistry.registerTileEntity(TileEntityFluidPlacer.class, "fluid_placer_te");
    }
    if (enablePumpAndPipes) {
      //sort
      BlockRegistry.registerBlock(new BlockItemCableSort(), "item_pipe_sort", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityItemCableSort.class, "item_pipe_sort_te");
      //Item
      BlockRegistry.registerBlock(new BlockCableItem(), "item_pipe", GuideCategory.BLOCK);
      BlockRegistry.registerBlock(new BlockItemPump(), "item_pump", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityItemPump.class, "item_pump_te");
      //ENERGY
      BlockRegistry.registerBlock(new BlockPowerCable(), "energy_pipe", GuideCategory.BLOCK);
      BlockRegistry.registerBlock(new BlockEnergyPump(), "energy_pump", null);
      GameRegistry.registerTileEntity(TileEntityEnergyPump.class, "energy_pump_te");
      // FLUID 
      BlockRegistry.registerBlock(new BlockCableFluid(), "fluid_pipe", GuideCategory.BLOCK);
      BlockRegistry.registerBlock(new BlockFluidPump(), "fluid_pump", null);
      GameRegistry.registerTileEntity(TileEntityFluidPump.class, "fluid_pump_te");
      //bundled
      BlockRegistry.registerBlock(new BlockCableBundle(), "bundled_pipe", GuideCategory.BLOCK);
      //pipes // TODO: fix block registry
      GameRegistry.registerTileEntity(TileEntityItemCable.class, "item_pipe_te");
      GameRegistry.registerTileEntity(TileEntityFluidCable.class, "fluid_pipe_te");
      GameRegistry.registerTileEntity(TileEntityCablePower.class, "energy_pipe_te");
      GameRegistry.registerTileEntity(TileEntityCableBundle.class, "bundled_pipe_te");
    }
    if (dispenserPowered) {
      BlockRegistry.registerBlock(new BlockDropperExact(), "dropper_exact", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityDropperExact.class, "dropper_exact_te");
    }
  }

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.content;
    moon = config.getBoolean("moon_sensor", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    etarget = config.getBoolean("target", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    battery = config.getBoolean("battery", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dispenserPowered = config.getBoolean("dropper_exact", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMilk = config.getBoolean("FluidMilk", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePoison = config.getBoolean("FluidPoison", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableBeans = config.getBoolean("MagicBean", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableApple = config.getBoolean("apple", category, true, Const.ConfigCategory.contentDefaultText);
    fluidPlacer = config.getBoolean("fluid_placer", category, true, Const.ConfigCategory.contentDefaultText);
    btrash = config.getBoolean("trash", category, true, Const.ConfigCategory.contentDefaultText);
    enableLibrary = config.getBoolean("block_library", category, true, Const.ConfigCategory.contentDefaultText);
    screen = config.getBoolean("block_screen", category, true, Const.ConfigCategory.contentDefaultText);
    workbench = config.getBoolean("Workbench", category, true, Const.ConfigCategory.contentDefaultText);
    soundproofing = config.getBoolean("Soundproofing", category, true, Const.ConfigCategory.contentDefaultText);
    autoCrafter = config.getBoolean("AutoCrafter", category, true, Const.ConfigCategory.contentDefaultText);
    disenchanter = config.getBoolean("UnchantPylon", category, true, Const.ConfigCategory.contentDefaultText);
    entityDetector = config.getBoolean("EntityDetector", category, true, Const.ConfigCategory.contentDefaultText);
    enableFan = config.getBoolean("Fan", category, true, Const.ConfigCategory.contentDefaultText);
    enableShearingBlock = config.getBoolean("ShearingBlock", category, true, Const.ConfigCategory.contentDefaultText);
    enableBucketBlocks = config.getBoolean("BucketBlocks", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePumpAndPipes = config.getBoolean("PumpAndPipes", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fragileEnabled = config.getBoolean("ScaffoldingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fishingBlock = config.getBoolean("FishingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    // enablePipes = config.getBoolean("Pipes", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePeat = config.getBoolean("PeatFeature", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText + "; this feature includes several items and blocks used by the Peat farming system");
    anvil = config.getBoolean("block_anvil", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    anvilMagma = config.getBoolean("block_anvil_magma", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enchanter = config.getBoolean("block_enchanter", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    wireless = config.getBoolean("wireless_transmitter", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    forester = config.getBoolean("block_forester", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    beaconPotion = config.getBoolean("PotionBeacon", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    emptyBeacon = config.getBoolean("EmptyBeacon", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableClock = config.getBoolean("Clock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSprinkler = config.getBoolean("Sprinkler", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSpikes = config.getBoolean("Spikes", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHydrator = config.getBoolean("Hydrator", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableVacuum = config.getBoolean("ItemCollector", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    expPylon = config.getBoolean("ExperiencePylon", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePattern = config.getBoolean("PatternReplicator", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableUser = config.getBoolean("AutomatedUser", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePassword = config.getBoolean("PasswordTrigger", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePlacer = config.getBoolean("BlockPlacer", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMiner = config.getBoolean("MinerBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableBuilderBlock = config.getBoolean("BuilderBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    TileEntityStructureBuilder.maxSize = config.getInt("builder.maxRange", Const.ConfigCategory.modpackMisc, 64, 3, 64, "Maximum range of the builder block that you can increase it to in the GUI");
    TileEntityStructureBuilder.maxHeight = config.getInt("builder.maxHeight", Const.ConfigCategory.modpackMisc, 64, 3, 64, "Maximum height of the builder block that you can increase it to in the GUI");
    enableHarvester = config.getBoolean("HarvesterBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableUncrafter = config.getBoolean("UncraftingGrinder", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMinerSmart = config.getBoolean("ControlledMiner", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    TileEntityControlledMiner.maxHeight = config.getInt("ControlledMiner.maxHeight", Const.ConfigCategory.modpackMisc, 32, 3, 128, "Maximum height of the controlled miner block that you can increase it to in the GUI");
    vectorPlate = config.getBoolean("AerialFaithPlate", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableInterdict = config.getBoolean("InterdictionPlate", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableConveyor = config.getBoolean("SlimeConveyor", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMagnet = config.getBoolean("MagnetBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    launchPads = config.getBoolean("SlimePads", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    BlockConveyor.doCorrections = config.getBoolean("SlimeConveyorPullCenter", Const.ConfigCategory.blocks, true, "If true, the Slime Conveyor will auto-correct entities towards the center while they are moving (keeping them away from the edge)");
    BlockConveyor.sneakPlayerAvoid = config.getBoolean("SlimeConveyorSneakPlayer", Const.ConfigCategory.blocks, true, "Players can sneak to avoid being pushed");
    BlockLaunch.sneakPlayerAvoid = config.getBoolean("SlimePlateSneakPlayer", Const.ConfigCategory.blocks, true, "Players can sneak to avoid being jumped");
    TileEntityCableBase.syncConfig(config);
    TileEntityItemPump.syncConfig(config);
    TileEntityUser.syncConfig(config);
  }
}
