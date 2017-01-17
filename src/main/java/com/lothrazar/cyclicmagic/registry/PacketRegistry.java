package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.net.*;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketRegistry {
  public static void register(SimpleNetworkWrapper network) {
    int packetID = 0;
    network.registerMessage(PacketOpenExtendedInventory.class, PacketOpenExtendedInventory.class, packetID++, Side.SERVER);
    network.registerMessage(PacketOpenNormalInventory.class, PacketOpenNormalInventory.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSyncExtendedInventory.class, PacketSyncExtendedInventory.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketSpellShiftLeft.class, PacketSpellShiftLeft.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSpellShiftRight.class, PacketSpellShiftRight.class, packetID++, Side.SERVER);
    network.registerMessage(PacketParticleAtPosition.class, PacketParticleAtPosition.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketSpellFromServer.class, PacketSpellFromServer.class, packetID++, Side.SERVER);
    network.registerMessage(PacketWandGui.class, PacketWandGui.class, packetID++, Side.SERVER);
    network.registerMessage(PacketMovePlayerColumn.class, PacketMovePlayerColumn.class, packetID++, Side.SERVER);
    network.registerMessage(PacketMovePlayerHotbar.class, PacketMovePlayerHotbar.class, packetID++, Side.SERVER);
    network.registerMessage(PacketWarpButton.class, PacketWarpButton.class, packetID++, Side.SERVER);
    network.registerMessage(PacketNewButton.class, PacketNewButton.class, packetID++, Side.SERVER);
    network.registerMessage(PacketDeleteWaypoint.class, PacketDeleteWaypoint.class, packetID++, Side.SERVER);
    network.registerMessage(PacketDepositPlayerToNearby.class, PacketDepositPlayerToNearby.class, packetID++, Side.SERVER);
    network.registerMessage(PacketDepositContainerToPlayer.class, PacketDepositContainerToPlayer.class, packetID++, Side.SERVER);
    network.registerMessage(PacketQuickStack.class, PacketQuickStack.class, packetID++, Side.SERVER);
    network.registerMessage(PacketRestockContainerToPlayer.class, PacketRestockContainerToPlayer.class, packetID++, Side.SERVER);
    network.registerMessage(PacketFakeWorkbench.class, PacketFakeWorkbench.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSpellBuildSize.class, PacketSpellBuildSize.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSyncPlayerData.class, PacketSyncPlayerData.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketTileBuildType.class, PacketTileBuildType.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileBuildSize.class, PacketTileBuildSize.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSyncPlayerHealth.class, PacketSyncPlayerHealth.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketTilePassword.class, PacketTilePassword.class, packetID++, Side.SERVER);
    network.registerMessage(PacketMoveBlock.class, PacketMoveBlock.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileMineHeight.class, PacketTileMineHeight.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSwapBlock.class, PacketSwapBlock.class, packetID++, Side.SERVER);
    network.registerMessage(PacketRandomize.class, PacketRandomize.class, packetID++, Side.SERVER);
    network.registerMessage(PacketStorageSack.class, PacketStorageSack.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileRedstoneToggle.class, PacketTileRedstoneToggle.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileSizeToggle.class, PacketTileSizeToggle.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileUser.class, PacketTileUser.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTilePatternBuilder.class, PacketTilePatternBuilder.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTilePatternSwap.class, PacketTilePatternSwap.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileDetector.class, PacketTileDetector.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileVector.class, PacketTileVector.class, packetID++, Side.SERVER);
    network.registerMessage(PacketPlayerFalldamage.class, PacketPlayerFalldamage.class, packetID++, Side.SERVER);
  }
}
