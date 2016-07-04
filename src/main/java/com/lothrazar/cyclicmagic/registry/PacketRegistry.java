package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.net.*;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketRegistry {
  public static void register(SimpleNetworkWrapper network) {
    network.registerMessage(PacketOpenExtendedInventory.class, PacketOpenExtendedInventory.class, 0, Side.SERVER);
    network.registerMessage(PacketOpenNormalInventory.class, PacketOpenNormalInventory.class, 1, Side.SERVER);
    network.registerMessage(PacketSyncExtendedInventory.class, PacketSyncExtendedInventory.class, 2, Side.CLIENT);
    network.registerMessage(PacketSpellShiftLeft.class, PacketSpellShiftLeft.class, PacketSpellShiftLeft.ID, Side.SERVER);
    network.registerMessage(PacketSpellShiftRight.class, PacketSpellShiftRight.class, PacketSpellShiftRight.ID, Side.SERVER);
    network.registerMessage(PacketParticleAtPosition.class, PacketParticleAtPosition.class, PacketParticleAtPosition.ID, Side.CLIENT);
    network.registerMessage(PacketSpellFromServer.class, PacketSpellFromServer.class, PacketSpellFromServer.ID, Side.SERVER);
    network.registerMessage(PacketSpellBuildType.class, PacketSpellBuildType.class, PacketSpellBuildType.ID, Side.SERVER);
    network.registerMessage(PacketRotateBlock.class, PacketRotateBlock.class, PacketRotateBlock.ID, Side.SERVER);
    network.registerMessage(PacketSpellPush.class, PacketSpellPush.class, PacketSpellPush.ID, Side.SERVER);
    network.registerMessage(PacketSpellPull.class, PacketSpellPull.class, PacketSpellPull.ID, Side.SERVER);
    network.registerMessage(PacketMovePlayerColumn.class, PacketMovePlayerColumn.class, PacketMovePlayerColumn.ID, Side.SERVER);
    network.registerMessage(PacketMovePlayerHotbar.class, PacketMovePlayerHotbar.class, PacketMovePlayerHotbar.ID, Side.SERVER);
    network.registerMessage(PacketWarpButton.class, PacketWarpButton.class, PacketWarpButton.ID, Side.SERVER);
    network.registerMessage(PacketNewButton.class, PacketNewButton.class, PacketNewButton.ID, Side.SERVER);
    network.registerMessage(PacketDeleteWaypoint.class, PacketDeleteWaypoint.class, PacketDeleteWaypoint.ID, Side.SERVER);
    network.registerMessage(PacketDepositPlayerToNearby.class, PacketDepositPlayerToNearby.class, PacketDepositPlayerToNearby.ID, Side.SERVER);
    network.registerMessage(PacketDepositContainerToPlayer.class, PacketDepositContainerToPlayer.class, PacketDepositContainerToPlayer.ID, Side.SERVER);
    network.registerMessage(PacketQuickStack.class, PacketQuickStack.class, PacketQuickStack.ID, Side.SERVER);
    network.registerMessage(PacketRestockContainerToPlayer.class, PacketRestockContainerToPlayer.class, PacketRestockContainerToPlayer.ID, Side.SERVER);
    network.registerMessage(PacketFakeWorkbench.class, PacketFakeWorkbench.class, PacketFakeWorkbench.ID, Side.SERVER);
    network.registerMessage(PacketSpellBuildSize.class, PacketSpellBuildSize.class, PacketSpellBuildSize.ID, Side.SERVER);
    network.registerMessage(PacketSyncPlayerData.class, PacketSyncPlayerData.class, PacketSyncPlayerData.ID, Side.CLIENT);
    network.registerMessage(PacketTileBuildType.class, PacketTileBuildType.class, PacketTileBuildType.ID, Side.SERVER);
    network.registerMessage(PacketTileBuildSize.class, PacketTileBuildSize.class, PacketTileBuildSize.ID, Side.SERVER);
    network.registerMessage(PacketSyncPlayerHealth.class, PacketSyncPlayerHealth.class, PacketSyncPlayerHealth.ID, Side.CLIENT);
    
    
  }
}
