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
package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.block.anvil.ContainerAnvilAuto;
import com.lothrazar.cyclicmagic.block.anvil.GuiAnvilAuto;
import com.lothrazar.cyclicmagic.block.anvil.TileEntityAnvilAuto;
import com.lothrazar.cyclicmagic.block.anvilmagma.ContainerAnvilMagma;
import com.lothrazar.cyclicmagic.block.anvilmagma.GuiAnvilMagma;
import com.lothrazar.cyclicmagic.block.anvilmagma.TileEntityAnvilMagma;
import com.lothrazar.cyclicmagic.block.anvilvoid.ContainerVoidAnvil;
import com.lothrazar.cyclicmagic.block.anvilvoid.GuiVoidAnvil;
import com.lothrazar.cyclicmagic.block.anvilvoid.TileEntityVoidAnvil;
import com.lothrazar.cyclicmagic.block.autouser.ContainerUser;
import com.lothrazar.cyclicmagic.block.autouser.GuiUser;
import com.lothrazar.cyclicmagic.block.autouser.TileEntityUser;
import com.lothrazar.cyclicmagic.block.battery.ContainerBattery;
import com.lothrazar.cyclicmagic.block.battery.GuiBattery;
import com.lothrazar.cyclicmagic.block.battery.TileEntityBattery;
import com.lothrazar.cyclicmagic.block.beaconpotion.ContainerBeaconPotion;
import com.lothrazar.cyclicmagic.block.beaconpotion.GuiBeaconPotion;
import com.lothrazar.cyclicmagic.block.beaconpotion.TileEntityBeaconPotion;
import com.lothrazar.cyclicmagic.block.builderpattern.ContainerPattern;
import com.lothrazar.cyclicmagic.block.builderpattern.GuiPattern;
import com.lothrazar.cyclicmagic.block.builderpattern.TileEntityPatternBuilder;
import com.lothrazar.cyclicmagic.block.buildershape.ContainerBuilder;
import com.lothrazar.cyclicmagic.block.buildershape.GuiBuilder;
import com.lothrazar.cyclicmagic.block.buildershape.TileEntityStructureBuilder;
import com.lothrazar.cyclicmagic.block.cablepump.energy.ContainerEnergyPump;
import com.lothrazar.cyclicmagic.block.cablepump.energy.GuiEnergyPump;
import com.lothrazar.cyclicmagic.block.cablepump.energy.TileEntityEnergyPump;
import com.lothrazar.cyclicmagic.block.cablepump.fluid.ContainerFluidPump;
import com.lothrazar.cyclicmagic.block.cablepump.fluid.GuiFluidPump;
import com.lothrazar.cyclicmagic.block.cablepump.fluid.TileEntityFluidPump;
import com.lothrazar.cyclicmagic.block.cablepump.item.ContainerItemPump;
import com.lothrazar.cyclicmagic.block.cablepump.item.GuiItemPump;
import com.lothrazar.cyclicmagic.block.cablepump.item.TileEntityItemPump;
import com.lothrazar.cyclicmagic.block.cablewireless.content.ContainerCableContentWireless;
import com.lothrazar.cyclicmagic.block.cablewireless.content.GuiCableContentWireless;
import com.lothrazar.cyclicmagic.block.cablewireless.content.TileCableContentWireless;
import com.lothrazar.cyclicmagic.block.cablewireless.energy.ContainerCableEnergyWireless;
import com.lothrazar.cyclicmagic.block.cablewireless.energy.GuiCableEnergyWireless;
import com.lothrazar.cyclicmagic.block.cablewireless.energy.TileCableEnergyWireless;
import com.lothrazar.cyclicmagic.block.clockredstone.ContainerClock;
import com.lothrazar.cyclicmagic.block.clockredstone.GuiClock;
import com.lothrazar.cyclicmagic.block.clockredstone.TileEntityClock;
import com.lothrazar.cyclicmagic.block.collector.ContainerVacuum;
import com.lothrazar.cyclicmagic.block.collector.GuiVacuum;
import com.lothrazar.cyclicmagic.block.collector.TileEntityVacuum;
import com.lothrazar.cyclicmagic.block.controlledminer.ContainerMinerSmart;
import com.lothrazar.cyclicmagic.block.controlledminer.GuiMinerSmart;
import com.lothrazar.cyclicmagic.block.controlledminer.TileEntityControlledMiner;
import com.lothrazar.cyclicmagic.block.crafter.ContainerCrafter;
import com.lothrazar.cyclicmagic.block.crafter.GuiCrafter;
import com.lothrazar.cyclicmagic.block.crafter.TileEntityCrafter;
import com.lothrazar.cyclicmagic.block.disenchanter.ContainerDisenchanter;
import com.lothrazar.cyclicmagic.block.disenchanter.GuiDisenchanter;
import com.lothrazar.cyclicmagic.block.disenchanter.TileEntityDisenchanter;
import com.lothrazar.cyclicmagic.block.dropper.ContainerDropperExact;
import com.lothrazar.cyclicmagic.block.dropper.GuiDropperExact;
import com.lothrazar.cyclicmagic.block.dropper.TileEntityDropperExact;
import com.lothrazar.cyclicmagic.block.enchanter.ContainerEnchanter;
import com.lothrazar.cyclicmagic.block.enchanter.GuiEnchanter;
import com.lothrazar.cyclicmagic.block.enchanter.TileEntityEnchanter;
import com.lothrazar.cyclicmagic.block.entitydetector.ContainerDetector;
import com.lothrazar.cyclicmagic.block.entitydetector.GuiDetector;
import com.lothrazar.cyclicmagic.block.entitydetector.TileEntityDetector;
import com.lothrazar.cyclicmagic.block.exppylon.ContainerPylon;
import com.lothrazar.cyclicmagic.block.exppylon.GuiPylon;
import com.lothrazar.cyclicmagic.block.exppylon.TileEntityXpPylon;
import com.lothrazar.cyclicmagic.block.fan.ContainerFan;
import com.lothrazar.cyclicmagic.block.fan.GuiFan;
import com.lothrazar.cyclicmagic.block.fan.TileEntityFan;
import com.lothrazar.cyclicmagic.block.firestarter.ContainerFireStarter;
import com.lothrazar.cyclicmagic.block.firestarter.GuiFireStarter;
import com.lothrazar.cyclicmagic.block.firestarter.TileEntityFireStarter;
import com.lothrazar.cyclicmagic.block.fishing.ContainerFisher;
import com.lothrazar.cyclicmagic.block.fishing.GuiFisher;
import com.lothrazar.cyclicmagic.block.fishing.TileEntityFishing;
import com.lothrazar.cyclicmagic.block.forester.ContainerForester;
import com.lothrazar.cyclicmagic.block.forester.GuiForester;
import com.lothrazar.cyclicmagic.block.forester.TileEntityForester;
import com.lothrazar.cyclicmagic.block.harvester.ContainerHarvester;
import com.lothrazar.cyclicmagic.block.harvester.GuiHarvester;
import com.lothrazar.cyclicmagic.block.harvester.TileEntityHarvester;
import com.lothrazar.cyclicmagic.block.hydrator.ContainerHydrator;
import com.lothrazar.cyclicmagic.block.hydrator.GuiHydrator;
import com.lothrazar.cyclicmagic.block.hydrator.TileEntityHydrator;
import com.lothrazar.cyclicmagic.block.miner.ContainerBlockMiner;
import com.lothrazar.cyclicmagic.block.miner.GuiBlockMiner;
import com.lothrazar.cyclicmagic.block.miner.TileEntityBlockMiner;
import com.lothrazar.cyclicmagic.block.packager.ContainerPackager;
import com.lothrazar.cyclicmagic.block.packager.GuiPackager;
import com.lothrazar.cyclicmagic.block.packager.TileEntityPackager;
import com.lothrazar.cyclicmagic.block.password.ContainerPassword;
import com.lothrazar.cyclicmagic.block.password.GuiPassword;
import com.lothrazar.cyclicmagic.block.password.TileEntityPassword;
import com.lothrazar.cyclicmagic.block.peat.farm.ContainerPeatFarm;
import com.lothrazar.cyclicmagic.block.peat.farm.GuiPeatFarm;
import com.lothrazar.cyclicmagic.block.peat.farm.TileEntityPeatFarm;
import com.lothrazar.cyclicmagic.block.peat.generator.ContainerPeatGenerator;
import com.lothrazar.cyclicmagic.block.peat.generator.GuiPeatGenerator;
import com.lothrazar.cyclicmagic.block.peat.generator.TileEntityPeatGenerator;
import com.lothrazar.cyclicmagic.block.placer.ContainerPlacer;
import com.lothrazar.cyclicmagic.block.placer.GuiPlacer;
import com.lothrazar.cyclicmagic.block.placer.TileEntityPlacer;
import com.lothrazar.cyclicmagic.block.screen.ContainerScreen;
import com.lothrazar.cyclicmagic.block.screen.GuiScreenBlock;
import com.lothrazar.cyclicmagic.block.screen.TileEntityScreen;
import com.lothrazar.cyclicmagic.block.sorting.ContainerItemSort;
import com.lothrazar.cyclicmagic.block.sorting.GuiItemSort;
import com.lothrazar.cyclicmagic.block.sorting.TileEntityItemCableSort;
import com.lothrazar.cyclicmagic.block.sound.ContainerSoundPlayer;
import com.lothrazar.cyclicmagic.block.sound.GuiSoundPlayer;
import com.lothrazar.cyclicmagic.block.sound.TileEntitySoundPlayer;
import com.lothrazar.cyclicmagic.block.uncrafter.ContainerUncrafting;
import com.lothrazar.cyclicmagic.block.uncrafter.GuiUncrafting;
import com.lothrazar.cyclicmagic.block.uncrafter.TileEntityUncrafter;
import com.lothrazar.cyclicmagic.block.vector.ContainerVector;
import com.lothrazar.cyclicmagic.block.vector.GuiVector;
import com.lothrazar.cyclicmagic.block.vector.TileEntityVector;
import com.lothrazar.cyclicmagic.block.workbench.ContainerWorkBench;
import com.lothrazar.cyclicmagic.block.workbench.GuiWorkbench;
import com.lothrazar.cyclicmagic.block.workbench.TileEntityWorkbench;
import com.lothrazar.cyclicmagic.core.util.UtilEntity;
import com.lothrazar.cyclicmagic.core.util.UtilPlayer;
import com.lothrazar.cyclicmagic.core.util.UtilSpellCaster;
import com.lothrazar.cyclicmagic.item.cyclicwand.ContainerWand;
import com.lothrazar.cyclicmagic.item.cyclicwand.GuiWandInventory;
import com.lothrazar.cyclicmagic.item.cyclicwand.InventoryWand;
import com.lothrazar.cyclicmagic.item.enderbook.GuiEnderBook;
import com.lothrazar.cyclicmagic.item.merchant.ContainerMerchantBetter;
import com.lothrazar.cyclicmagic.item.merchant.GuiMerchantBetter;
import com.lothrazar.cyclicmagic.item.merchant.InventoryMerchantBetter;
import com.lothrazar.cyclicmagic.item.merchant.ItemMerchantAlmanac;
import com.lothrazar.cyclicmagic.item.signcolor.GuiSignEditor;
import com.lothrazar.cyclicmagic.item.storagesack.ContainerStorage;
import com.lothrazar.cyclicmagic.item.storagesack.GuiStorage;
import com.lothrazar.cyclicmagic.item.storagesack.InventoryStorage;
import com.lothrazar.cyclicmagic.playerupgrade.crafting.ContainerPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.playerupgrade.crafting.GuiPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.playerupgrade.storage.ContainerPlayerExtended;
import com.lothrazar.cyclicmagic.playerupgrade.storage.GuiPlayerExtended;
import com.lothrazar.cyclicmagic.playerupgrade.storage.InventoryPlayerExtended;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ForgeGuiHandler implements IGuiHandler {

  public static final int VANILLA_SIGN = 100;
  public static final int GUI_INDEX_UNCRAFTING = 0;
  public static final int GUI_INDEX_WAND = 1;
  public static final int GUI_INDEX_EXTENDED = 2;
  public static final int GUI_INDEX_STORAGE = 3;
  public static final int GUI_INDEX_WAYPOINT = 4;
  public static final int GUI_INDEX_BUILDER = 5;
  public static final int GUI_INDEX_PLACER = 6;
  public static final int GUI_INDEX_PASSWORD = 7;
  public static final int GUI_INDEX_SMARTMINER = 8;
  public static final int GUI_INDEX_FISHER = 9;
  public static final int GUI_INDEX_PWORKBENCH = 10;
  public static final int GUI_INDEX_USER = 11;
  public static final int GUI_INDEX_HARVESTER = 12;
  public static final int GUI_INDEX_BLOCKMINER = 13;
  public static final int GUI_INDEX_PATTERN = 14;
  public static final int GUI_INDEX_DETECTOR = 15;
  public static final int GUI_INDEX_VECTOR = 16;
  public static final int GUI_INDEX_VILLAGER = 17;
  public static final int GUI_INDEX_FAN = 18;
  public static final int GUI_INDEX_XP = 19;
  public static final int GUI_INDEX_DISENCH = 20;
  public static final int GUI_INDEX_CRAFTER = 21;
  public static final int GUI_INDEX_WORKBENCH = 22;
  public static final int GUI_INDEX_HYDRATOR = 23;
  public static final int GUI_INDEX_VACUUM = 24;
  public static final int GUI_INDEX_CLOCK = 25;
  public static final int GUI_INDEX_BEACON = 26;
  public static final int GUI_INDEX_FORESTER = 27;
  public static final int GUI_INDEX_SORT = 28;
  public static final int GUI_INDEX_SCREEN = 29;
  public static final int GUI_INDEX_ENCHANTER = 30;
  public static final int GUI_INDEX_ANVIL = 31;
  public static final int GUI_INDEX_ITEMPUMP = 32;
  public static final int GUI_INDEX_PEATGEN = 33;
  public static final int GUI_INDEX_PEATFARM = 34;
  public static final int GUI_INDEX_DROPPER = 35;
  public static final int GUI_INDEX_FLUIDPUMP = 36;
  public static final int GUI_INDEX_ENERGYPUMP = 37;
  public static final int GUI_INDEX_BATTERY = 38;
  public static final int GUI_INDEX_ANVILMAGMA = 39;
  public static final int GUI_INDEX_FIREST = 40;
  public static final int GUI_INDEX_VOID = 41;
  public static final int GUI_INDEX_SOUNDPL = 42;
  public static final int GUI_INDEX_SIGNPOST = 43;
  public static final int GUI_INDEX_W_CONTENT = 44;
  public static final int GUI_INDEX_W_ENERGY = 45;
  public static final int GUI_INDEX_PACKAGER = 46;

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    BlockPos p = new BlockPos(x, y, z);
    TileEntity te = world.getTileEntity(p);
    switch (ID) {
      case VANILLA_SIGN:
        return null;
      case GUI_INDEX_EXTENDED:
        return new ContainerPlayerExtended(player.inventory, new InventoryPlayerExtended(player), player);
      case GUI_INDEX_PWORKBENCH:
        return new ContainerPlayerExtWorkbench(player.inventory, player);
      case GUI_INDEX_WAND:
        ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
        return new ContainerWand(player, player.inventory, new InventoryWand(player, wand));
      case GUI_INDEX_UNCRAFTING:
        if (te != null && te instanceof TileEntityUncrafter) {
          return new ContainerUncrafting(player.inventory, (TileEntityUncrafter) te);
        }
      break;
      case GUI_INDEX_HARVESTER:
        if (te != null && te instanceof TileEntityHarvester) {
          return new ContainerHarvester(player.inventory, (TileEntityHarvester) te);
        }
      break;
      case GUI_INDEX_BLOCKMINER:
        if (te != null && te instanceof TileEntityBlockMiner) {
          return new ContainerBlockMiner(player.inventory, (TileEntityBlockMiner) te);
        }
      break;
      case GUI_INDEX_STORAGE:
        return new ContainerStorage(player, new InventoryStorage(player));
      case GUI_INDEX_WAYPOINT:
        return null;
      case GUI_INDEX_BUILDER:
        if (te != null && te instanceof TileEntityStructureBuilder) {
          Container c = new ContainerBuilder(player.inventory, (TileEntityStructureBuilder) te);
          c.detectAndSendChanges();
          return c;
        }
      break;
      case GUI_INDEX_PLACER:
        if (te != null && te instanceof TileEntityPlacer) {
          Container c = new ContainerPlacer(player.inventory, (TileEntityPlacer) te);
          c.detectAndSendChanges();
          return c;
        }
      break;
      case GUI_INDEX_PASSWORD:
        if (te != null && te instanceof TileEntityPassword) {
          Container c = new ContainerPassword((TileEntityPassword) te);
          return c;
        }
      break;
      case GUI_INDEX_SMARTMINER:
        if (te != null && te instanceof TileEntityControlledMiner) {
          Container c = new ContainerMinerSmart(player.inventory, (TileEntityControlledMiner) te);
          return c;
        }
      break;
      case GUI_INDEX_FISHER:
        if (te != null && te instanceof TileEntityFishing) {
          return new ContainerFisher(player.inventory, (TileEntityFishing) te);
        }
      break;
      case GUI_INDEX_USER:
        if (te != null && te instanceof TileEntityUser) {
          return new ContainerUser(player.inventory, (TileEntityUser) te);
        }
      break;
      case GUI_INDEX_PATTERN:
        if (te != null && te instanceof TileEntityPatternBuilder) {
          return new ContainerPattern(player.inventory, (TileEntityPatternBuilder) te);
        }
      break;
      case GUI_INDEX_DETECTOR:
        if (te != null && te instanceof TileEntityDetector) {
          return new ContainerDetector(player.inventory, (TileEntityDetector) te);
        }
      break;
      case GUI_INDEX_VECTOR:
        if (te != null && te instanceof TileEntityVector) {
          return new ContainerVector(player.inventory, (TileEntityVector) te);
        }
      break;
      case GUI_INDEX_VILLAGER:
        //http://www.minecraftforge.net/forum/topic/29593-18-solveddisplay-gui-when-interacting-with-an-entity/
        EntityVillager v = (EntityVillager) UtilEntity.getClosestEntity(world, player, UtilEntity.getVillagers(world, p, ItemMerchantAlmanac.radius));
        if (v != null) {
          v.setCustomer(player);
          ContainerMerchantBetter c = new ContainerMerchantBetter(player.inventory, v, new InventoryMerchantBetter(player, v), world);
          return c;
        }
      break;
      case GUI_INDEX_FAN:
        if (te != null && te instanceof TileEntityFan) {
          return new ContainerFan(player.inventory, (TileEntityFan) te);
        }
      break;
      case GUI_INDEX_XP:
        if (te instanceof TileEntityXpPylon) {
          return new ContainerPylon(player.inventory, (TileEntityXpPylon) te);
        }
      break;
      case GUI_INDEX_DISENCH:
        if (te instanceof TileEntityDisenchanter) {
          return new ContainerDisenchanter(player.inventory, (TileEntityDisenchanter) te);
        }
      break;
      case GUI_INDEX_CRAFTER:
        if (te instanceof TileEntityCrafter) {
          return new ContainerCrafter(player.inventory, (TileEntityCrafter) te);
        }
      break;
      case GUI_INDEX_WORKBENCH:
        if (te instanceof TileEntityWorkbench) {
          return new ContainerWorkBench(player.inventory, (TileEntityWorkbench) te);
        }
      break;
      case GUI_INDEX_HYDRATOR:
        if (te instanceof TileEntityHydrator) {
          return new ContainerHydrator(player.inventory, (TileEntityHydrator) te);
        }
      break;
      case GUI_INDEX_VACUUM:
        if (te instanceof TileEntityVacuum) {
          return new ContainerVacuum(player.inventory, (TileEntityVacuum) te);
        }
      break;
      case GUI_INDEX_CLOCK:
        if (te instanceof TileEntityClock) {
          return new ContainerClock(player.inventory, (TileEntityClock) te);
        }
      break;
      case GUI_INDEX_BEACON:
        if (te instanceof TileEntityBeaconPotion) {
          return new ContainerBeaconPotion(player.inventory, (TileEntityBeaconPotion) te);
        }
      break;
      case GUI_INDEX_FORESTER:
        if (te instanceof TileEntityForester) {
          return new ContainerForester(player.inventory, (TileEntityForester) te);
        }
      break;
      case GUI_INDEX_SORT:
        if (te instanceof TileEntityItemCableSort) {
          return new ContainerItemSort(player.inventory, (TileEntityItemCableSort) te);
        }
      break;
      case GUI_INDEX_SCREEN:
        if (te instanceof TileEntityScreen) {
          return new ContainerScreen(player.inventory, (TileEntityScreen) te);
        }
      break;
      case GUI_INDEX_ENCHANTER:
        if (te instanceof TileEntityEnchanter) {
          return new ContainerEnchanter(player.inventory, (TileEntityEnchanter) te);
        }
      break;
      case GUI_INDEX_ANVIL:
        if (te instanceof TileEntityAnvilAuto) {
          return new ContainerAnvilAuto(player.inventory, (TileEntityAnvilAuto) te);
        }
      break;
      case GUI_INDEX_ITEMPUMP:
        if (te instanceof TileEntityItemPump) {
          return new ContainerItemPump(player.inventory, (TileEntityItemPump) te);
        }
      break;
      case GUI_INDEX_PEATGEN:
        if (te instanceof TileEntityPeatGenerator) {
          return new ContainerPeatGenerator(player.inventory, (TileEntityPeatGenerator) te);
        }
      break;
      case GUI_INDEX_PEATFARM:
        if (te instanceof TileEntityPeatFarm) {
          return new ContainerPeatFarm(player.inventory, (TileEntityPeatFarm) te);
        }
      break;
      case GUI_INDEX_DROPPER:
        if (te instanceof TileEntityDropperExact) {
          return new ContainerDropperExact(player.inventory, (TileEntityDropperExact) te);
        }
      break;
      case GUI_INDEX_FLUIDPUMP:
        if (te instanceof TileEntityFluidPump) {
          return new ContainerFluidPump(player.inventory, (TileEntityFluidPump) te);
        }
      break;
      case GUI_INDEX_ENERGYPUMP:
        if (te instanceof TileEntityEnergyPump) {
          return new ContainerEnergyPump(player.inventory, (TileEntityEnergyPump) te);
        }
      break;
      case GUI_INDEX_BATTERY:
        if (te instanceof TileEntityBattery) {
          return new ContainerBattery(player.inventory, (TileEntityBattery) te);
        }
      break;
      case GUI_INDEX_ANVILMAGMA:
        if (te instanceof TileEntityAnvilMagma) {
          return new ContainerAnvilMagma(player.inventory, (TileEntityAnvilMagma) te);
        }
      break;
      case GUI_INDEX_FIREST:
        if (te instanceof TileEntityFireStarter) {
          return new ContainerFireStarter(player.inventory, (TileEntityFireStarter) te);
        }
      break;
      case GUI_INDEX_VOID:
        if (te instanceof TileEntityVoidAnvil) {
          return new ContainerVoidAnvil(player.inventory, (TileEntityVoidAnvil) te);
        }
      break;
      case GUI_INDEX_SOUNDPL:
        if (te instanceof TileEntitySoundPlayer) {
          return new ContainerSoundPlayer(player.inventory, (TileEntitySoundPlayer) te);
        }
      break;
      case GUI_INDEX_W_CONTENT:
        if (te instanceof TileCableContentWireless) {
          return new ContainerCableContentWireless(player.inventory, (TileCableContentWireless) te);
        }
      break;
      case GUI_INDEX_W_ENERGY:
        if (te instanceof TileCableEnergyWireless) {
          return new ContainerCableEnergyWireless(player.inventory, (TileCableEnergyWireless) te);
        }
      break;
      case GUI_INDEX_PACKAGER:
        if (te instanceof TileEntityPackager) {
          return new ContainerPackager(player.inventory, (TileEntityPackager) te);
        }
      break;
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    BlockPos p = new BlockPos(x, y, z);
    if (world instanceof WorldClient) {
      TileEntity te = world.getTileEntity(p);
      switch (ID) {
        case VANILLA_SIGN:
          return new GuiEditSign((TileEntitySign) world.getTileEntity(new BlockPos(x, y, z)));
        case GUI_INDEX_EXTENDED:
          return new GuiPlayerExtended(new ContainerPlayerExtended(player.inventory, new InventoryPlayerExtended(player), player));
        case GUI_INDEX_PWORKBENCH:
          return new GuiPlayerExtWorkbench(new ContainerPlayerExtWorkbench(player.inventory, player));
        case GUI_INDEX_WAND:
          ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
          return new GuiWandInventory(new ContainerWand(player, player.inventory, new InventoryWand(player, wand)), wand);
        case GUI_INDEX_UNCRAFTING:
          if (te instanceof TileEntityUncrafter) {
            return new GuiUncrafting(player.inventory, (TileEntityUncrafter) te);
          }
        break;
        case GUI_INDEX_HARVESTER:
          if (te instanceof TileEntityHarvester) {
            return new GuiHarvester(player.inventory, (TileEntityHarvester) te);
          }
        break;
        case GUI_INDEX_BLOCKMINER:
          if (te instanceof TileEntityBlockMiner) {
            return new GuiBlockMiner(player.inventory, (TileEntityBlockMiner) te);
          }
        break;
        case GUI_INDEX_STORAGE:
          return new GuiStorage(new ContainerStorage(player, new InventoryStorage(player)), player);
        case GUI_INDEX_WAYPOINT:
          return new GuiEnderBook(player, UtilPlayer.getPlayerItemIfHeld(player));
        case GUI_INDEX_BUILDER:
          if (te != null && te instanceof TileEntityStructureBuilder) {
            return new GuiBuilder(player.inventory, (TileEntityStructureBuilder) te);
          }
        break;
        case GUI_INDEX_PLACER:
          if (te != null && te instanceof TileEntityPlacer) {
            return new GuiPlacer(player.inventory, (TileEntityPlacer) te);
          }
        break;
        case GUI_INDEX_PASSWORD:
          if (te != null && te instanceof TileEntityPassword) {
            return new GuiPassword((TileEntityPassword) te);
          }
        break;
        case GUI_INDEX_SMARTMINER:
          if (te != null && te instanceof TileEntityControlledMiner) {
            return new GuiMinerSmart(player.inventory, (TileEntityControlledMiner) te);
          }
        break;
        case GUI_INDEX_FISHER:
          if (te != null && te instanceof TileEntityFishing) {
            return new GuiFisher(player.inventory, (TileEntityFishing) te);
          }
        break;
        case GUI_INDEX_USER:
          if (te != null && te instanceof TileEntityUser) {
            return new GuiUser(player.inventory, (TileEntityUser) te);
          }
        break;
        case GUI_INDEX_PATTERN:
          if (te != null && te instanceof TileEntityPatternBuilder) {
            return new GuiPattern(player.inventory, (TileEntityPatternBuilder) te);
          }
        break;
        case GUI_INDEX_DETECTOR:
          if (te != null && te instanceof TileEntityDetector) {
            return new GuiDetector(player.inventory, (TileEntityDetector) te);
          }
        break;
        case GUI_INDEX_VECTOR:
          if (te != null && te instanceof TileEntityVector) {
            return new GuiVector(player.inventory, (TileEntityVector) te);
          }
        break;
        case GUI_INDEX_VILLAGER:
          EntityVillager v = (EntityVillager) UtilEntity.getClosestEntity(world, player, UtilEntity.getVillagers(world, p, ItemMerchantAlmanac.radius));
          if (v != null) {
            return new GuiMerchantBetter(player.inventory, v, new InventoryMerchantBetter(player, v), world);
          }
        break;
        case GUI_INDEX_FAN:
          if (te != null && te instanceof TileEntityFan) {
            return new GuiFan(player.inventory, (TileEntityFan) te);
          }
        break;
        case GUI_INDEX_XP:
          if (te instanceof TileEntityXpPylon) {
            return new GuiPylon(player.inventory, (TileEntityXpPylon) te);
          }
        break;
        case GUI_INDEX_DISENCH:
          if (te instanceof TileEntityDisenchanter) {
            return new GuiDisenchanter(player.inventory, (TileEntityDisenchanter) te);
          }
        break;
        case GUI_INDEX_CRAFTER:
          if (te instanceof TileEntityCrafter) {
            return new GuiCrafter(player.inventory, (TileEntityCrafter) te);
          }
        break;
        case GUI_INDEX_WORKBENCH:
          if (te instanceof TileEntityWorkbench) {
            return new GuiWorkbench(player.inventory, (TileEntityWorkbench) te);
          }
        break;
        case GUI_INDEX_HYDRATOR:
          if (te instanceof TileEntityHydrator) {
            return new GuiHydrator(player.inventory, (TileEntityHydrator) te);
          }
        break;
        case GUI_INDEX_VACUUM:
          if (te instanceof TileEntityVacuum) {
            return new GuiVacuum(player.inventory, (TileEntityVacuum) te);
          }
        break;
        case GUI_INDEX_CLOCK:
          if (te instanceof TileEntityClock) {
            return new GuiClock(player.inventory, (TileEntityClock) te);
          }
        break;
        case GUI_INDEX_BEACON:
          if (te instanceof TileEntityBeaconPotion) {
            return new GuiBeaconPotion(player.inventory, (TileEntityBeaconPotion) te);
          }
        break;
        case GUI_INDEX_FORESTER:
          if (te instanceof TileEntityForester) {
            return new GuiForester(player.inventory, (TileEntityForester) te);
          }
        break;
        case GUI_INDEX_SORT:
          if (te instanceof TileEntityItemCableSort) {
            return new GuiItemSort(player.inventory, (TileEntityItemCableSort) te);
          }
        break;
        case GUI_INDEX_SCREEN:
          if (te instanceof TileEntityScreen) {
            return new GuiScreenBlock(player.inventory, (TileEntityScreen) te);
          }
        break;
        case GUI_INDEX_ENCHANTER:
          if (te instanceof TileEntityEnchanter) {
            return new GuiEnchanter(player.inventory, (TileEntityEnchanter) te);
          }
        break;
        case GUI_INDEX_ANVIL:
          if (te instanceof TileEntityAnvilAuto) {
            return new GuiAnvilAuto(player.inventory, (TileEntityAnvilAuto) te);
          }
        break;
        case GUI_INDEX_ITEMPUMP:
          if (te instanceof TileEntityItemPump) {
            return new GuiItemPump(player.inventory, (TileEntityItemPump) te);
          }
        break;
        case GUI_INDEX_PEATGEN:
          if (te instanceof TileEntityPeatGenerator) {
            return new GuiPeatGenerator(player.inventory, (TileEntityPeatGenerator) te);
          }
        break;
        case GUI_INDEX_PEATFARM:
          if (te instanceof TileEntityPeatFarm) {
            return new GuiPeatFarm(player.inventory, (TileEntityPeatFarm) te);
          }
        break;
        case GUI_INDEX_DROPPER:
          if (te instanceof TileEntityDropperExact) {
            return new GuiDropperExact(player.inventory, (TileEntityDropperExact) te);
          }
        break;
        case GUI_INDEX_FLUIDPUMP:
          if (te instanceof TileEntityFluidPump) {
            return new GuiFluidPump(player.inventory, (TileEntityFluidPump) te);
          }
        break;
        case GUI_INDEX_ENERGYPUMP:
          if (te instanceof TileEntityEnergyPump) {
            return new GuiEnergyPump(player.inventory, (TileEntityEnergyPump) te);
          }
        break;
        case GUI_INDEX_BATTERY:
          if (te instanceof TileEntityBattery) {
            return new GuiBattery(player.inventory, (TileEntityBattery) te);
          }
        break;
        case GUI_INDEX_ANVILMAGMA:
          if (te instanceof TileEntityAnvilMagma) {
            return new GuiAnvilMagma(player.inventory, (TileEntityAnvilMagma) te);
          }
        break;
        case GUI_INDEX_FIREST:
          if (te instanceof TileEntityFireStarter) {
            return new GuiFireStarter(player.inventory, (TileEntityFireStarter) te);
          }
        break;
        case GUI_INDEX_VOID:
          if (te instanceof TileEntityVoidAnvil) {
            return new GuiVoidAnvil(player.inventory, (TileEntityVoidAnvil) te);
          }
        break;
        case GUI_INDEX_SOUNDPL:
          if (te instanceof TileEntitySoundPlayer) {
            return new GuiSoundPlayer(player.inventory, (TileEntitySoundPlayer) te);
          }
        break;
        case GUI_INDEX_SIGNPOST:
          if (te instanceof TileEntitySign)
            return new GuiSignEditor(player,
                player.getHeldItemMainhand(),
                (TileEntitySign) te);
        break;
        case GUI_INDEX_W_CONTENT:
          if (te instanceof TileCableContentWireless) {
            return new GuiCableContentWireless(player.inventory, (TileCableContentWireless) te);
          }
        break;
        case GUI_INDEX_W_ENERGY:
          if (te instanceof TileCableEnergyWireless) {
            return new GuiCableEnergyWireless(player.inventory, (TileCableEnergyWireless) te);
          }
        break;
        case GUI_INDEX_PACKAGER:
          if (te instanceof TileEntityPackager) {
            return new GuiPackager(player.inventory, (TileEntityPackager) te);
          }
        break;
      }
    }
    return null;
  }
}
