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
import com.lothrazar.cyclicmagic.block.BlockSpikesRetractable;
import com.lothrazar.cyclicmagic.block.autouser.TileEntityUser;
import com.lothrazar.cyclicmagic.block.buildershape.TileEntityStructureBuilder;
import com.lothrazar.cyclicmagic.block.cable.energy.BlockPowerCable;
import com.lothrazar.cyclicmagic.block.cable.energy.TileEntityCablePower;
import com.lothrazar.cyclicmagic.block.cable.fluid.BlockCableFluid;
import com.lothrazar.cyclicmagic.block.cable.fluid.TileEntityFluidCable;
import com.lothrazar.cyclicmagic.block.cable.item.BlockCableItem;
import com.lothrazar.cyclicmagic.block.cable.item.TileEntityItemCable;
import com.lothrazar.cyclicmagic.block.cable.multi.BlockCableBundle;
import com.lothrazar.cyclicmagic.block.cable.multi.TileEntityCableBundle;
import com.lothrazar.cyclicmagic.block.cablepump.energy.BlockEnergyPump;
import com.lothrazar.cyclicmagic.block.cablepump.energy.TileEntityEnergyPump;
import com.lothrazar.cyclicmagic.block.cablepump.fluid.BlockFluidPump;
import com.lothrazar.cyclicmagic.block.cablepump.fluid.TileEntityFluidPump;
import com.lothrazar.cyclicmagic.block.cablepump.item.BlockItemPump;
import com.lothrazar.cyclicmagic.block.cablepump.item.TileEntityItemPump;
import com.lothrazar.cyclicmagic.block.cablewireless.content.BlockCableContentWireless;
import com.lothrazar.cyclicmagic.block.cablewireless.content.TileCableContentWireless;
import com.lothrazar.cyclicmagic.block.cablewireless.energy.BlockCableEnergyWireless;
import com.lothrazar.cyclicmagic.block.cablewireless.energy.TileCableEnergyWireless;
import com.lothrazar.cyclicmagic.block.controlledminer.TileEntityControlledMiner;
import com.lothrazar.cyclicmagic.block.conveyor.BlockConveyor;
import com.lothrazar.cyclicmagic.block.conveyor.BlockConveyor.SpeedType;
import com.lothrazar.cyclicmagic.block.conveyor.BlockConveyorAngle;
import com.lothrazar.cyclicmagic.block.conveyor.BlockConveyorCorner;
import com.lothrazar.cyclicmagic.block.enchantlibrary.ctrl.BlockLibraryController;
import com.lothrazar.cyclicmagic.block.enchantlibrary.shelf.BlockLibrary;
import com.lothrazar.cyclicmagic.block.enchantlibrary.shelf.TileEntityLibrary;
import com.lothrazar.cyclicmagic.block.fire.BlockFireFrost;
import com.lothrazar.cyclicmagic.block.fire.BlockFireSafe;
import com.lothrazar.cyclicmagic.block.firestarter.BlockFireStarter;
import com.lothrazar.cyclicmagic.block.firestarter.TileEntityFireStarter;
import com.lothrazar.cyclicmagic.block.hydrator.RecipeHydrate;
import com.lothrazar.cyclicmagic.block.peat.BlockPeat;
import com.lothrazar.cyclicmagic.block.peat.ItemBiomass;
import com.lothrazar.cyclicmagic.block.peat.ItemPeatFuel;
import com.lothrazar.cyclicmagic.block.peat.farm.BlockPeatFarm;
import com.lothrazar.cyclicmagic.block.peat.farm.TileEntityPeatFarm;
import com.lothrazar.cyclicmagic.block.peat.generator.BlockPeatGenerator;
import com.lothrazar.cyclicmagic.block.peat.generator.TileEntityPeatGenerator;
import com.lothrazar.cyclicmagic.block.scaffolding.BlockScaffolding;
import com.lothrazar.cyclicmagic.block.scaffolding.BlockScaffoldingReplace;
import com.lothrazar.cyclicmagic.block.scaffolding.BlockScaffoldingResponsive;
import com.lothrazar.cyclicmagic.block.scaffolding.ItemBlockScaffolding;
import com.lothrazar.cyclicmagic.block.sorting.BlockItemCableSort;
import com.lothrazar.cyclicmagic.block.sorting.TileEntityItemCableSort;
import com.lothrazar.cyclicmagic.block.wireless.BlockRedstoneWireless;
import com.lothrazar.cyclicmagic.block.wireless.ItemBlockWireless;
import com.lothrazar.cyclicmagic.block.wireless.TileEntityWirelessRec;
import com.lothrazar.cyclicmagic.block.wireless.TileEntityWirelessTr;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.item.firemagic.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.item.firemagic.ItemProjectileBlaze;
import com.lothrazar.cyclicmagic.item.location.ItemLocation;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.EntityProjectileRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
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
  private boolean enablePumpAndPipes;
  private boolean enableLibrary;
  private boolean enableSpikes;
  private boolean wireless;
  private boolean enablePeat;
  private boolean enableConveyor;
  private boolean launchPads;
  private boolean fire_starter;
  private boolean enableEnderBlaze;
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
  //fire is a dependency block like liquids, used by many places
  boolean fireDarkUsed = false;
  boolean fireFrostUsed = false;
  private boolean cableWireless;

  @Override
  public void onPreInit() {
    //WARN: registerTileEntity move to resource locatoin: THIS WILL DELETE CONTENTS of existing worlds 
    super.onPreInit();

    if (fire_starter) {
      BlockRegistry.registerBlock(new BlockFireStarter(), "fire_starter", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityFireStarter.class, "fire_starter_te");
      fireDarkUsed = true;
      fireFrostUsed = true;
    }
    if (enableEnderBlaze) {
      fireDarkUsed = true;
      Item fire_dark_anim = new Item();
      ItemRegistry.register(fire_dark_anim, "fire_dark_anim");
      ItemProjectileBlaze ender_blaze = new ItemProjectileBlaze();
      ender_blaze.setRepairItem(new ItemStack(fire_dark_anim));
      ItemRegistry.register(ender_blaze, "ender_blaze", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityBlazeBolt.class, "blazebolt", 1008);
      ModCyclic.instance.events.register(ender_blaze);
    }
    //TODO maybe a Fire REgistry LUL? 
    if (fireFrostUsed) {
      BlockRegistry.registerBlock(new BlockFireFrost(), "fire_frost", null);
    }
    if (fireDarkUsed) {
      BlockRegistry.registerBlock(new BlockFireSafe(), "fire_dark", null);
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
      BlockConveyorCorner plate_push__med_corner = new BlockConveyorCorner(SpeedType.MEDIUM);
      BlockRegistry.registerBlock(plate_push__med_corner, null, "plate_push_corner", GuideCategory.BLOCKPLATE, false);
      //angle
      BlockConveyorAngle plate_push_med_angle = new BlockConveyorAngle(SpeedType.MEDIUM);
      BlockRegistry.registerBlock(plate_push_med_angle, null, "plate_push_med_angle", GuideCategory.BLOCKPLATE, false);
      //main
      BlockConveyor plate_push_med = new BlockConveyor(plate_push__med_corner, plate_push_med_angle);
      BlockRegistry.registerBlock(plate_push_med, "plate_push", GuideCategory.BLOCKPLATE);
      plate_push__med_corner.setDrop(plate_push_med);
      plate_push_med_angle.setDrop(plate_push_med);
      plate_push_med_angle.setCorner(plate_push__med_corner);
      plate_push__med_corner.setAngled(plate_push_med_angle);
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
      plate_push_fast_angle.setCorner(plate_push_fast_corner);
      plate_push_fast_corner.setAngled(plate_push_fast_angle);
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
      plate_push_slow_angle.setCorner(plate_push_slow_corner);
      plate_push_slow_corner.setAngled(plate_push_slow_corner);
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
      plate_push_slowest_angle.setCorner(plate_push_slowest_corner);
      plate_push_slowest_corner.setAngled(plate_push_slowest_angle);
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

    if (enableSpikes) {
      BlockSpikesRetractable spikes_iron = new BlockSpikesRetractable(false);
      BlockRegistry.registerBlock(spikes_iron, "spikes_iron", GuideCategory.BLOCK);
      BlockSpikesRetractable spikes_redstone_diamond = new BlockSpikesRetractable(true);
      BlockRegistry.registerBlock(spikes_redstone_diamond, "spikes_diamond", GuideCategory.BLOCK);
    }



    if (enablePeat) {
      //peat 
      ItemBiomass peat_biomass = new ItemBiomass();
      ItemRegistry.register(peat_biomass, "peat_biomass", GuideCategory.ITEM);
      ItemPeatFuel peat_fuel = new ItemPeatFuel(null);
      ItemRegistry.register(peat_fuel, "peat_fuel", GuideCategory.ITEM);
      ItemRegistry.register(new ItemPeatFuel(peat_fuel), "peat_fuel_enriched", GuideCategory.ITEM);
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
              new ItemStack(Blocks.LEAVES),
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
    }
    if (cableWireless) {
      BlockCableContentWireless batteryw = new BlockCableContentWireless();
      BlockRegistry.registerBlock(batteryw, "cable_wireless", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileCableContentWireless.class, Const.MODID + "cable_wireless_te");
      // energy
      BlockCableEnergyWireless w_energy = new BlockCableEnergyWireless();
      BlockRegistry.registerBlock(w_energy, "cable_wireless_energy", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileCableEnergyWireless.class, Const.MODID + "cable_wireless_energy_te");
      //depends on this 
      ItemLocation card_location = new ItemLocation();
      ItemRegistry.register(card_location, "card_location", GuideCategory.ITEM);
    }

    if (enableLibrary) {
      BlockLibrary library = new BlockLibrary();
      BlockRegistry.registerBlock(library, "block_library", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityLibrary.class, Const.MODID + "library_te");
      BlockLibraryController lc = new BlockLibraryController(library);
      BlockRegistry.registerBlock(lc, "block_library_ctrl", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(library);
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
  }

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.content;

    ItemPeatFuel.FUEL_WEAK = config.getInt("peat_fuel", Const.ConfigCategory.fuelCost, 256, 10, 99999, "Energy generated by normal Peat");
    ItemPeatFuel.FUEL_STRONG = config.getInt("peat_fuel_enriched", Const.ConfigCategory.fuelCost, 4096, 10, 99999, "Energy generated by crafted Peat");
    cableWireless = config.getBoolean("cable_wireless", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableEnderBlaze = config.getBoolean("EnderBlaze", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fire_starter = config.getBoolean("fire_starter", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableLibrary = config.getBoolean("block_library", category, true, Const.ConfigCategory.contentDefaultText);
    enablePumpAndPipes = config.getBoolean("PumpAndPipes", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fragileEnabled = config.getBoolean("ScaffoldingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    // enablePipes = config.getBoolean("Pipes", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePeat = config.getBoolean("PeatFeature", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText + "; this feature includes several items and blocks used by the Peat farming system");
    wireless = config.getBoolean("wireless_transmitter", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSpikes = config.getBoolean("Spikes", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    TileEntityStructureBuilder.maxSize = config.getInt("builder.maxRange", Const.ConfigCategory.modpackMisc, 64, 3, 64, "Maximum range of the builder block that you can increase it to in the GUI");
    TileEntityStructureBuilder.maxHeight = config.getInt("builder.maxHeight", Const.ConfigCategory.modpackMisc, 64, 3, 64, "Maximum height of the builder block that you can increase it to in the GUI");
    TileEntityControlledMiner.maxHeight = config.getInt("ControlledMiner.maxHeight", Const.ConfigCategory.modpackMisc, 32, 3, 128, "Maximum height of the controlled miner block that you can increase it to in the GUI");
    enableConveyor = config.getBoolean("SlimeConveyor", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    launchPads = config.getBoolean("SlimePads", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    BlockConveyor.doCorrections = config.getBoolean("SlimeConveyorPullCenter", Const.ConfigCategory.blocks, true, "If true, the Slime Conveyor will auto-correct entities towards the center while they are moving (keeping them away from the edge)");
    BlockConveyor.sneakPlayerAvoid = config.getBoolean("SlimeConveyorSneakPlayer", Const.ConfigCategory.blocks, true, "Players can sneak to avoid being pushed");
    BlockLaunch.sneakPlayerAvoid = config.getBoolean("SlimePlateSneakPlayer", Const.ConfigCategory.blocks, true, "Players can sneak to avoid being jumped");
    // TileEntityCableBase.syncConfig(config);
    TileEntityItemPump.syncConfig(config);
    TileEntityUser.syncConfig(config);
  }
}
