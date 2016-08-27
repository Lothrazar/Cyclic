package com.lothrazar.cyclicmagic.gui.miner;

import com.lothrazar.cyclicmagic.block.tileentity.TileMachineMiner;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerMiner extends ContainerBaseMachine {

  private TileMachineMiner tileEntity;
  public ContainerMiner(InventoryPlayer inventoryPlayer, TileMachineMiner te) {
    tileEntity = te;
  }
}
