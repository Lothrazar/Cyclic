package com.lothrazar.cyclicmagic.gui;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachinePlacer;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUncrafter;
import com.lothrazar.cyclicmagic.gui.builder.ContainerBuilder;
import com.lothrazar.cyclicmagic.gui.builder.GuiBuilder;
import com.lothrazar.cyclicmagic.gui.placer.ContainerPlacer;
import com.lothrazar.cyclicmagic.gui.placer.GuiPlacer;
import com.lothrazar.cyclicmagic.gui.player.GuiPlayerExtended;
import com.lothrazar.cyclicmagic.gui.storage.ContainerStorage;
import com.lothrazar.cyclicmagic.gui.storage.GuiStorage;
import com.lothrazar.cyclicmagic.gui.storage.InventoryStorage;
import com.lothrazar.cyclicmagic.gui.uncrafting.ContainerUncrafting;
import com.lothrazar.cyclicmagic.gui.uncrafting.GuiUncrafting;
import com.lothrazar.cyclicmagic.gui.wand.ContainerWand;
import com.lothrazar.cyclicmagic.gui.wand.GuiWandInventory;
import com.lothrazar.cyclicmagic.gui.wand.InventoryWand;
import com.lothrazar.cyclicmagic.gui.waypoints.GuiEnderBook;
import com.lothrazar.cyclicmagic.item.ItemInventoryStorage;
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
  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
    switch (ID) {
    case GUI_INDEX_EXTENDED:
      return new com.lothrazar.cyclicmagic.gui.player.ContainerPlayerExtended(player.inventory, !world.isRemote, player);
    case GUI_INDEX_WAND:
      ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
      return new ContainerWand(player, player.inventory, new InventoryWand(player, wand));
    case GUI_INDEX_UNCRAFTING:
      if (te != null && te instanceof TileMachineUncrafter) { return new ContainerUncrafting(player.inventory, (TileMachineUncrafter) te); }
      break;
    case GUI_INDEX_STORAGE:
      ItemStack s = ItemInventoryStorage.getPlayerItemIfHeld(player);
      return new ContainerStorage(player, player.inventory, new InventoryStorage(player, s));
    case GUI_INDEX_WAYPOINT:
      return null;
    case GUI_INDEX_BUILDER:
      if (te != null && te instanceof TileMachineBuilder) {
        Container c = new ContainerBuilder(player.inventory, (TileMachineBuilder) te);
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
    }
    return null;
  }
  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (world instanceof WorldClient) {
      TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
      switch (ID) {
      case GUI_INDEX_EXTENDED:
        return new GuiPlayerExtended(player);
      case GUI_INDEX_WAND:
        ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
        return new GuiWandInventory(new ContainerWand(player, player.inventory, new InventoryWand(player, wand)), wand);
      case GUI_INDEX_UNCRAFTING:
        if (te instanceof TileMachineUncrafter) { return new GuiUncrafting(player.inventory, (TileMachineUncrafter) te); }
        break;
      case GUI_INDEX_STORAGE:
        ItemStack s = ItemInventoryStorage.getPlayerItemIfHeld(player);
        return new GuiStorage(new ContainerStorage(player, player.inventory, new InventoryStorage(player, s)));
      case GUI_INDEX_WAYPOINT:
        //Minecraft.getMinecraft().displayGuiScreen(new GuiEnderBook(entityPlayer, stack));
        return new GuiEnderBook(player, UtilInventory.getPlayerItemIfHeld(player));
      case GUI_INDEX_BUILDER:
        if (te != null && te instanceof TileMachineBuilder) { return new GuiBuilder(player.inventory, (TileMachineBuilder) te); }
        break;
      case GUI_INDEX_PLACER:
        if (te != null && te instanceof TileMachinePlacer) { return new GuiPlacer(player.inventory, (TileMachinePlacer) te); }
        
        break;
      }
    }
    return null;
  }
}
