package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.item.builder.PacketSwapBlock;
import com.lothrazar.cyclic.item.crafting.PacketItemGui;
import com.lothrazar.cyclic.item.datacard.filter.PacketFilterCard;
import com.lothrazar.cyclic.item.enderbook.PacketItemScroll;
import com.lothrazar.cyclic.item.random.PacketRandomize;
import com.lothrazar.cyclic.item.scythe.PacketScythe;
import com.lothrazar.cyclic.item.storagebag.PacketStorageBagScreen;
import com.lothrazar.cyclic.item.transporter.PacketChestSack;
import com.lothrazar.cyclic.net.BlockFacadeMessage;
import com.lothrazar.cyclic.net.PacketCraftAction;
import com.lothrazar.cyclic.net.PacketEntityLaser;
import com.lothrazar.cyclic.net.PacketHarvesting;
import com.lothrazar.cyclic.net.PacketKeyBind;
import com.lothrazar.cyclic.net.PacketPlayerSyncToClient;
import com.lothrazar.cyclic.net.PacketRecordSound;
import com.lothrazar.cyclic.net.PacketSyncManaToClient;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.net.PacketTileInventoryToClient;
import com.lothrazar.cyclic.net.PacketTileString;
import com.lothrazar.library.packet.PacketFlib;
import com.lothrazar.library.packet.PacketItemToggle;
import com.lothrazar.library.packet.PacketPlayerFalldamage;
import com.lothrazar.library.packet.PacketRotateBlock;
import com.lothrazar.library.packet.PacketSyncEnergy;
import com.lothrazar.library.packet.PacketSyncFluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketRegistry {

  private static final String PROTOCOL_VERSION = Integer.toString(1);
  public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
      .named(new ResourceLocation(ModCyclic.MODID, "main_channel"))
      .clientAcceptedVersions(PROTOCOL_VERSION::equals)
      .serverAcceptedVersions(PROTOCOL_VERSION::equals)
      .networkProtocolVersion(() -> PROTOCOL_VERSION)
      .simpleChannel();

  public static void setup() {
    //https://gist.github.com/williewillus/353c872bcf1a6ace9921189f6100d09a
    int id = 0;
    INSTANCE.registerMessage(id++, PacketPlayerFalldamage.class, PacketPlayerFalldamage::encode, PacketPlayerFalldamage::decode, PacketPlayerFalldamage::handle);
    INSTANCE.registerMessage(id++, PacketItemToggle.class, PacketItemToggle::encode, PacketItemToggle::decode, PacketItemToggle::handle);
    INSTANCE.registerMessage(id++, PacketItemGui.class, PacketItemGui::encode, PacketItemGui::decode, PacketItemGui::handle);
    INSTANCE.registerMessage(id++, PacketRotateBlock.class, PacketRotateBlock::encode, PacketRotateBlock::decode, PacketRotateBlock::handle);
    INSTANCE.registerMessage(id++, PacketScythe.class, PacketScythe::encode, PacketScythe::decode, PacketScythe::handle);
    INSTANCE.registerMessage(id++, PacketTileData.class, PacketTileData::encode, PacketTileData::decode, PacketTileData::handle);
    INSTANCE.registerMessage(id++, PacketSyncFluid.class, PacketSyncFluid::encode, PacketSyncFluid::decode, PacketSyncFluid::handle);
    INSTANCE.registerMessage(id++, PacketChestSack.class, PacketChestSack::encode, PacketChestSack::decode, PacketChestSack::handle);
    INSTANCE.registerMessage(id++, PacketSwapBlock.class, PacketSwapBlock::encode, PacketSwapBlock::decode, PacketSwapBlock::handle);
    INSTANCE.registerMessage(id++, PacketRandomize.class, PacketRandomize::encode, PacketRandomize::decode, PacketRandomize::handle);
    INSTANCE.registerMessage(id++, PacketTileString.class, PacketTileString::encode, PacketTileString::decode, PacketTileString::handle);
    INSTANCE.registerMessage(id++, PacketSyncEnergy.class, PacketSyncEnergy::encode, PacketSyncEnergy::decode, PacketSyncEnergy::handle);
    INSTANCE.registerMessage(id++, PacketTileInventoryToClient.class, PacketTileInventoryToClient::encode, PacketTileInventoryToClient::decode, PacketTileInventoryToClient::handle);
    INSTANCE.registerMessage(id++, PacketStorageBagScreen.class, PacketStorageBagScreen::encode, PacketStorageBagScreen::decode, PacketStorageBagScreen::handle);
    INSTANCE.registerMessage(id++, PacketCraftAction.class, PacketCraftAction::encode, PacketCraftAction::decode, PacketCraftAction::handle);
    INSTANCE.registerMessage(id++, PacketFilterCard.class, PacketFilterCard::encode, PacketFilterCard::decode, PacketFilterCard::handle);
    INSTANCE.registerMessage(id++, PacketItemScroll.class, PacketItemScroll::encode, PacketItemScroll::decode, PacketItemScroll::handle);
    INSTANCE.registerMessage(id++, PacketKeyBind.class, PacketKeyBind::encode, PacketKeyBind::decode, PacketKeyBind::handle);
    INSTANCE.registerMessage(id++, PacketRecordSound.class, PacketRecordSound::encode, PacketRecordSound::decode, PacketRecordSound::handle);
    INSTANCE.registerMessage(id++, PacketHarvesting.class, PacketHarvesting::encode, PacketHarvesting::decode, PacketHarvesting::handle);
    INSTANCE.registerMessage(id++, PacketEntityLaser.class, PacketEntityLaser::encode, PacketEntityLaser::decode, PacketEntityLaser::handle);
    INSTANCE.registerMessage(id++, PacketPlayerSyncToClient.class, PacketPlayerSyncToClient::encode, PacketPlayerSyncToClient::decode, PacketPlayerSyncToClient::handle);
    INSTANCE.registerMessage(id++, PacketSyncManaToClient.class, PacketSyncManaToClient::encode, PacketSyncManaToClient::decode, PacketSyncManaToClient::handle);
    INSTANCE.registerMessage(id++, BlockFacadeMessage.class, BlockFacadeMessage::encode, BlockFacadeMessage::decode, BlockFacadeMessage::handle);
  }

  public static void sendToAllClients(Level world, PacketFlib packet) {
    if (world.isClientSide) {
      return;
    }
    for (Player player : world.players()) {
      ServerPlayer sp = ((ServerPlayer) player);
      PacketRegistry.INSTANCE.sendTo(packet, sp.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
  }
}
