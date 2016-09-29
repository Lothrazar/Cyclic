package com.lothrazar.cyclicmagic.gui;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityFishing;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPassword;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineMinerSmart;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineStructureBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachinePlacer;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUncrafter;
import com.lothrazar.cyclicmagic.gui.builder.ContainerBuilder;
import com.lothrazar.cyclicmagic.gui.builder.GuiBuilder;
import com.lothrazar.cyclicmagic.gui.fisher.ContainerFisher;
import com.lothrazar.cyclicmagic.gui.fisher.GuiFisher;
import com.lothrazar.cyclicmagic.gui.miner.ContainerMiner;
import com.lothrazar.cyclicmagic.gui.miner.GuiMiner;
import com.lothrazar.cyclicmagic.gui.password.ContainerPassword;
import com.lothrazar.cyclicmagic.gui.password.GuiPassword;
import com.lothrazar.cyclicmagic.gui.placer.ContainerPlacer;
import com.lothrazar.cyclicmagic.gui.placer.GuiPlacer;
import com.lothrazar.cyclicmagic.gui.player.ContainerPlayerExtended;
import com.lothrazar.cyclicmagic.gui.player.GuiPlayerExtended;
import com.lothrazar.cyclicmagic.gui.player.InventoryPlayerExtended;
import com.lothrazar.cyclicmagic.gui.playerworkbench.ContainerPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.gui.playerworkbench.GuiPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.gui.playerworkbench.InventoryPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.gui.storage.ContainerStorage;
import com.lothrazar.cyclicmagic.gui.storage.GuiStorage;
import com.lothrazar.cyclicmagic.gui.storage.InventoryStorage;
import com.lothrazar.cyclicmagic.gui.uncrafting.ContainerUncrafting;
import com.lothrazar.cyclicmagic.gui.uncrafting.GuiUncrafting;
import com.lothrazar.cyclicmagic.gui.wand.ContainerWand;
import com.lothrazar.cyclicmagic.gui.wand.GuiWandInventory;
import com.lothrazar.cyclicmagic.gui.wand.InventoryWand;
import com.lothrazar.cyclicmagic.gui.waypoints.GuiEnderBook;
import com.lothrazar.cyclicmagic.item.ItemStorageBag;
import com.lothrazar.cyclicmagic.util.UtilInventory;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
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
  public static final int GUI_INDEX_MINER = 8;
  public static final int GUI_INDEX_FISHER = 9;
  public static final int GUI_INDEX_PWORKBENCH = 10;
  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
    switch (ID) {
    case GUI_INDEX_EXTENDED:
      return new ContainerPlayerExtended(player.inventory, new InventoryPlayerExtended(player),player);
    case GUI_INDEX_PWORKBENCH:   
      return new ContainerPlayerExtWorkbench(player.inventory, new InventoryPlayerExtWorkbench(player), player);
    
    case GUI_INDEX_WAND:
      ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
      return new ContainerWand(player, player.inventory, new InventoryWand(player, wand));
    case GUI_INDEX_UNCRAFTING:
      if (te != null && te instanceof TileMachineUncrafter) { return new ContainerUncrafting(player.inventory, (TileMachineUncrafter) te); }
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
    case GUI_INDEX_MINER:
      if (te != null && te instanceof TileMachineMinerSmart) {
        Container c = new ContainerMiner(player.inventory, (TileMachineMinerSmart) te);
        return c;
      }
      break;
    case GUI_INDEX_FISHER:
      if (te != null && te instanceof TileEntityFishing) { return new ContainerFisher(player.inventory, (TileEntityFishing) te); }
      break;
    }
    return null;
  }
  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (world instanceof WorldClient) {
      TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
      switch (ID) {
      case GUI_INDEX_EXTENDED:
        return new GuiPlayerExtended(new ContainerPlayerExtended(player.inventory, new InventoryPlayerExtended(player),player));
      case GUI_INDEX_PWORKBENCH:   
        return new GuiPlayerExtWorkbench(new ContainerPlayerExtWorkbench(player.inventory, new InventoryPlayerExtWorkbench(player), player));
      case GUI_INDEX_WAND:
        ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
        return new GuiWandInventory(new ContainerWand(player, player.inventory, new InventoryWand(player, wand)), wand);
      case GUI_INDEX_UNCRAFTING:
        if (te instanceof TileMachineUncrafter) { return new GuiUncrafting(player.inventory, (TileMachineUncrafter) te); }
        break;
      case GUI_INDEX_STORAGE:
        ItemStack s = ItemStorageBag.getPlayerItemIfHeld(player);
        return new GuiStorage(new ContainerStorage(player, player.inventory, new InventoryStorage(player, s)));
      case GUI_INDEX_WAYPOINT:
        return new GuiEnderBook(player, UtilInventory.getPlayerItemIfHeld(player));
      case GUI_INDEX_BUILDER:
        if (te != null && te instanceof TileMachineStructureBuilder) { return new GuiBuilder(player.inventory, (TileMachineStructureBuilder) te); }
        break;
      case GUI_INDEX_PLACER:
        if (te != null && te instanceof TileMachinePlacer) { return new GuiPlacer(player.inventory, (TileMachinePlacer) te); }
        break;
      case GUI_INDEX_PASSWORD:
        if (te != null && te instanceof TileEntityPassword) { return new GuiPassword((TileEntityPassword) te); }
        break;
      case GUI_INDEX_MINER:
        if (te != null && te instanceof TileMachineMinerSmart) { return new GuiMiner(player.inventory, (TileMachineMinerSmart) te); }
        break;
      case GUI_INDEX_FISHER:
        if (te != null && te instanceof TileEntityFishing) { return new GuiFisher(player.inventory, (TileEntityFishing) te); }
        break;
      }
    }
    return null;
  }
}
