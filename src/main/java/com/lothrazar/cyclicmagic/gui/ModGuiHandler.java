package com.lothrazar.cyclicmagic.gui;
import java.util.List;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityDetector;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityFishing;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPassword;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPatternBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineHarvester;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineBlockMiner;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineMinerSmart;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineStructureBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachinePlacer;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUncrafter;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUser;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector;
import com.lothrazar.cyclicmagic.gui.blockminer.ContainerBlockMiner;
import com.lothrazar.cyclicmagic.gui.blockminer.GuiBlockMiner;
import com.lothrazar.cyclicmagic.gui.builder.ContainerBuilder;
import com.lothrazar.cyclicmagic.gui.builder.GuiBuilder;
import com.lothrazar.cyclicmagic.gui.detector.ContainerDetector;
import com.lothrazar.cyclicmagic.gui.detector.GuiDetector;
import com.lothrazar.cyclicmagic.gui.fisher.ContainerFisher;
import com.lothrazar.cyclicmagic.gui.fisher.GuiFisher;
import com.lothrazar.cyclicmagic.gui.miner.ContainerMinerSmart;
import com.lothrazar.cyclicmagic.gui.miner.GuiMinerSmart;
import com.lothrazar.cyclicmagic.gui.password.ContainerPassword;
import com.lothrazar.cyclicmagic.gui.password.GuiPassword;
import com.lothrazar.cyclicmagic.gui.pattern.ContainerPattern;
import com.lothrazar.cyclicmagic.gui.pattern.GuiPattern;
import com.lothrazar.cyclicmagic.gui.harvester.ContainerHarvester;
import com.lothrazar.cyclicmagic.gui.harvester.GuiHarvester;
import com.lothrazar.cyclicmagic.gui.placer.ContainerPlacer;
import com.lothrazar.cyclicmagic.gui.placer.GuiPlacer;
import com.lothrazar.cyclicmagic.gui.player.ContainerPlayerExtended;
import com.lothrazar.cyclicmagic.gui.player.GuiPlayerExtended;
import com.lothrazar.cyclicmagic.gui.player.InventoryPlayerExtended;
import com.lothrazar.cyclicmagic.gui.playerworkbench.ContainerPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.gui.playerworkbench.GuiPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.gui.storage.ContainerStorage;
import com.lothrazar.cyclicmagic.gui.storage.GuiStorage;
import com.lothrazar.cyclicmagic.gui.storage.InventoryStorage;
import com.lothrazar.cyclicmagic.gui.uncrafting.ContainerUncrafting;
import com.lothrazar.cyclicmagic.gui.uncrafting.GuiUncrafting;
import com.lothrazar.cyclicmagic.gui.user.ContainerUser;
import com.lothrazar.cyclicmagic.gui.user.GuiUser;
import com.lothrazar.cyclicmagic.gui.vector.ContainerVector;
import com.lothrazar.cyclicmagic.gui.vector.GuiVector;
import com.lothrazar.cyclicmagic.gui.villager.ContainerMerchantBetter;
import com.lothrazar.cyclicmagic.gui.villager.GuiMerchantBetter;
import com.lothrazar.cyclicmagic.gui.wand.ContainerWand;
import com.lothrazar.cyclicmagic.gui.wand.GuiWandInventory;
import com.lothrazar.cyclicmagic.gui.wand.InventoryWand;
import com.lothrazar.cyclicmagic.gui.waypoints.GuiEnderBook;
import com.lothrazar.cyclicmagic.item.ItemStorageBag;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {
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
  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    BlockPos p = new BlockPos(x, y, z);
    TileEntity te = world.getTileEntity(p);
    switch (ID) {
      case GUI_INDEX_EXTENDED:
        return new ContainerPlayerExtended(player.inventory, new InventoryPlayerExtended(player), player);
      case GUI_INDEX_PWORKBENCH:
        return new ContainerPlayerExtWorkbench(player.inventory, player);
      case GUI_INDEX_WAND:
        ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
        return new ContainerWand(player, player.inventory, new InventoryWand(player, wand));
      case GUI_INDEX_UNCRAFTING:
        if (te != null && te instanceof TileMachineUncrafter) { return new ContainerUncrafting(player.inventory, (TileMachineUncrafter) te); }
      break;
      case GUI_INDEX_HARVESTER:
        if (te != null && te instanceof TileMachineHarvester) { return new ContainerHarvester(player.inventory, (TileMachineHarvester) te); }
      break;
      case GUI_INDEX_BLOCKMINER:
        if (te != null && te instanceof TileMachineBlockMiner) { return new ContainerBlockMiner(player.inventory, (TileMachineBlockMiner) te); }
      break;
      case GUI_INDEX_STORAGE:
        ItemStack s = ItemStorageBag.getPlayerItemIfHeld(player);
        return new ContainerStorage(player, player.inventory, new InventoryStorage(player, s));
      case GUI_INDEX_WAYPOINT:
        return null;
      case GUI_INDEX_BUILDER:
        if (te != null && te instanceof TileMachineStructureBuilder) {
          Container c = new ContainerBuilder(player.inventory, (TileMachineStructureBuilder) te);
          c.detectAndSendChanges();
          return c;
        }
      break;
      case GUI_INDEX_PLACER:
        if (te != null && te instanceof TileMachinePlacer) {
          Container c = new ContainerPlacer(player.inventory, (TileMachinePlacer) te);
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
        if (te != null && te instanceof TileMachineMinerSmart) {
          Container c = new ContainerMinerSmart(player.inventory, (TileMachineMinerSmart) te);
          return c;
        }
      break;
      case GUI_INDEX_FISHER:
        if (te != null && te instanceof TileEntityFishing) { return new ContainerFisher(player.inventory, (TileEntityFishing) te); }
      break;
      case GUI_INDEX_USER:
        if (te != null && te instanceof TileMachineUser) { return new ContainerUser(player.inventory, (TileMachineUser) te); }
      break;
      case GUI_INDEX_PATTERN:
        if (te != null && te instanceof TileEntityPatternBuilder) { return new ContainerPattern(player.inventory, (TileEntityPatternBuilder) te); }
      break;
      case GUI_INDEX_DETECTOR:
        if (te != null && te instanceof TileEntityDetector) { return new ContainerDetector(player.inventory, (TileEntityDetector) te); }
      break;
      case GUI_INDEX_VECTOR:
        if (te != null && te instanceof TileVector) { return new ContainerVector(player.inventory, (TileVector) te); }
      break;
      case GUI_INDEX_VILLAGER:

        //http://www.minecraftforge.net/forum/topic/29593-18-solveddisplay-gui-when-interacting-with-an-entity/
        List<EntityVillager> all =UtilEntity.getVillagers(world, p,5);
        if (!all.isEmpty()) {
          EntityVillager v = all.get(0);
          v.setCustomer(player);
          ContainerMerchantBetter c =  new ContainerMerchantBetter(player.inventory, v,new InventoryMerchant(player, v), world);
          c.detectAndSendChanges();
          
          return c;
//          return new ContainerMerchant(player.inventory, v, world);
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
        case GUI_INDEX_EXTENDED:
          return new GuiPlayerExtended(new ContainerPlayerExtended(player.inventory, new InventoryPlayerExtended(player), player));
        case GUI_INDEX_PWORKBENCH:
          return new GuiPlayerExtWorkbench(new ContainerPlayerExtWorkbench(player.inventory, player));
        case GUI_INDEX_WAND:
          ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
          return new GuiWandInventory(new ContainerWand(player, player.inventory, new InventoryWand(player, wand)), wand);
        case GUI_INDEX_UNCRAFTING:
          if (te instanceof TileMachineUncrafter) { return new GuiUncrafting(player.inventory, (TileMachineUncrafter) te); }
        break;
        case GUI_INDEX_HARVESTER:
          if (te instanceof TileMachineHarvester) { return new GuiHarvester(player.inventory, (TileMachineHarvester) te); }
        break;
        case GUI_INDEX_BLOCKMINER:
          if (te instanceof TileMachineBlockMiner) { return new GuiBlockMiner(player.inventory, (TileMachineBlockMiner) te); }
        break;
        case GUI_INDEX_STORAGE:
          ItemStack s = ItemStorageBag.getPlayerItemIfHeld(player);
          return new GuiStorage(new ContainerStorage(player, player.inventory, new InventoryStorage(player, s)));
        case GUI_INDEX_WAYPOINT:
          return new GuiEnderBook(player, UtilPlayer.getPlayerItemIfHeld(player));
        case GUI_INDEX_BUILDER:
          if (te != null && te instanceof TileMachineStructureBuilder) { return new GuiBuilder(player.inventory, (TileMachineStructureBuilder) te); }
        break;
        case GUI_INDEX_PLACER:
          if (te != null && te instanceof TileMachinePlacer) { return new GuiPlacer(player.inventory, (TileMachinePlacer) te); }
        break;
        case GUI_INDEX_PASSWORD:
          if (te != null && te instanceof TileEntityPassword) { return new GuiPassword((TileEntityPassword) te); }
        break;
        case GUI_INDEX_SMARTMINER:
          if (te != null && te instanceof TileMachineMinerSmart) { return new GuiMinerSmart(player.inventory, (TileMachineMinerSmart) te); }
        break;
        case GUI_INDEX_FISHER:
          if (te != null && te instanceof TileEntityFishing) { return new GuiFisher(player.inventory, (TileEntityFishing) te); }
        break;
        case GUI_INDEX_USER:
          if (te != null && te instanceof TileMachineUser) { return new GuiUser(player.inventory, (TileMachineUser) te); }
        break;
        case GUI_INDEX_PATTERN:
          if (te != null && te instanceof TileEntityPatternBuilder) { return new GuiPattern(player.inventory, (TileEntityPatternBuilder) te); }
        break;
        case GUI_INDEX_DETECTOR:
          if (te != null && te instanceof TileEntityDetector) { return new GuiDetector(player.inventory, (TileEntityDetector) te); }
        break;
        case GUI_INDEX_VECTOR:
          if (te != null && te instanceof TileVector) { return new GuiVector(player.inventory, (TileVector) te); }
        break;
        case GUI_INDEX_VILLAGER:
          List<EntityVillager> all =UtilEntity.getVillagers(world, p,5);
          if (!all.isEmpty()) {
            EntityVillager v = all.get(0);
//            v.setCustomer(player);
            return new GuiMerchantBetter(player.inventory, v,new InventoryMerchant(player, v), world);
          }
        break;
      }
    }
    return null;
  }
}
