package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.item.builder.PacketSwapBlock;
import com.lothrazar.cyclic.item.elemental.PacketFreezeWater;
import com.lothrazar.cyclic.item.elemental.PacketWaterFlow;
import com.lothrazar.cyclic.item.crafting.PacketItemGui;
import com.lothrazar.cyclic.item.datacard.filter.PacketFilterCard;
import com.lothrazar.cyclic.item.datacard.fluid.PacketFluidFilterCard;
import com.lothrazar.cyclic.item.enderbook.PacketItemScroll;
import com.lothrazar.cyclic.item.random.PacketRandomize;
import com.lothrazar.cyclic.item.scythe.PacketScythe;
import com.lothrazar.cyclic.item.storagebag.PacketStorageBagScreen;
import com.lothrazar.cyclic.item.transporter.PacketChestSack;
import com.lothrazar.cyclic.net.PacketCraftAction;
import com.lothrazar.cyclic.net.PacketDisplayFluidMessage;
import com.lothrazar.cyclic.net.PacketEntityLaser;
import com.lothrazar.cyclic.net.PacketHarvesting;
import com.lothrazar.cyclic.net.PacketPlayerSyncToClient;
import com.lothrazar.cyclic.net.PacketRecordSound;
import com.lothrazar.cyclic.net.PacketSyncHorseCarrots;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.net.PacketTileInventoryToClient;
import com.lothrazar.cyclic.net.PacketTileString;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class PacketRegistry {

  public static void setup(RegisterPayloadHandlersEvent event) {
    PayloadRegistrar registrar = event.registrar(ModCyclic.MODID);

    registrar.playToServer(PacketItemGui.ID, PacketItemGui.STREAM_CODEC, PacketItemGui::handle);
    registrar.playToServer(PacketScythe.TYPE, PacketScythe.STREAM_CODEC, PacketScythe::handle);
    registrar.playToServer(PacketTileData.TYPE, PacketTileData.STREAM_CODEC, PacketTileData::handle);
    registrar.playToServer(PacketChestSack.TYPE, PacketChestSack.STREAM_CODEC, PacketChestSack::handle);
    registrar.playToServer(PacketSwapBlock.TYPE, PacketSwapBlock.STREAM_CODEC, PacketSwapBlock::handle);
    registrar.playToServer(PacketFreezeWater.TYPE, PacketFreezeWater.STREAM_CODEC, PacketFreezeWater::handle);
    registrar.playToServer(PacketWaterFlow.TYPE, PacketWaterFlow.STREAM_CODEC, PacketWaterFlow::handle);
    registrar.playToServer(PacketRandomize.TYPE, PacketRandomize.STREAM_CODEC, PacketRandomize::handle);
    registrar.playToServer(PacketTileString.TYPE, PacketTileString.STREAM_CODEC, PacketTileString::handle);
    registrar.playToClient(PacketTileInventoryToClient.TYPE, PacketTileInventoryToClient.STREAM_CODEC, PacketTileInventoryToClient::handle);
    registrar.playToServer(PacketStorageBagScreen.TYPE, PacketStorageBagScreen.STREAM_CODEC, PacketStorageBagScreen::handle);
    registrar.playToServer(PacketCraftAction.TYPE, PacketCraftAction.STREAM_CODEC, PacketCraftAction::handle);
    registrar.playToServer(PacketFilterCard.TYPE, PacketFilterCard.STREAM_CODEC, PacketFilterCard::handle);
    registrar.playToServer(PacketFluidFilterCard.TYPE, PacketFluidFilterCard.STREAM_CODEC, PacketFluidFilterCard::handle);
    registrar.playToServer(PacketItemScroll.TYPE, PacketItemScroll.STREAM_CODEC, PacketItemScroll::handle);
    registrar.playToServer(PacketRecordSound.TYPE, PacketRecordSound.STREAM_CODEC, PacketRecordSound::handle);
    registrar.playToServer(PacketHarvesting.TYPE, PacketHarvesting.STREAM_CODEC, PacketHarvesting::handle);
    registrar.playToServer(PacketEntityLaser.TYPE, PacketEntityLaser.STREAM_CODEC, PacketEntityLaser::handle);
    registrar.playToClient(PacketPlayerSyncToClient.TYPE, PacketPlayerSyncToClient.STREAM_CODEC, PacketPlayerSyncToClient::handle);
//    registrar.playToClient(PacketSyncManaToClient.TYPE, PacketSyncManaToClient.STREAM_CODEC, PacketSyncManaToClient::handle);
    registrar.playToClient(PacketSyncHorseCarrots.TYPE, PacketSyncHorseCarrots.STREAM_CODEC, PacketSyncHorseCarrots::handle);
    registrar.playToClient(PacketDisplayFluidMessage.TYPE, PacketDisplayFluidMessage.STREAM_CODEC, PacketDisplayFluidMessage::handle);
//    registrar.playToServer(BlockFacadeMessage.TYPE, BlockFacadeMessage.STREAM_CODEC, BlockFacadeMessage::handle);
  }

  public static void sendToAllClients(Level world, CustomPacketPayload packet) {
    if (world.isClientSide) {
      return;
    }
    for (Player player : world.players()) {
      ServerPlayer sp = ((ServerPlayer) player);
      PacketDistributor.sendToPlayer(sp, packet);
    }
  }
}
