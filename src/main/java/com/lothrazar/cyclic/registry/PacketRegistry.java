package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.item.builder.PacketSwapBlock;
import com.lothrazar.cyclic.item.random.PacketRandomize;
import com.lothrazar.cyclic.item.scythe.PacketScythe;
import com.lothrazar.cyclic.item.transporter.PacketChestSack;
import com.lothrazar.cyclic.net.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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
    INSTANCE.registerMessage(id++, PacketRotateBlock.class, PacketRotateBlock::encode, PacketRotateBlock::decode, PacketRotateBlock::handle);
    INSTANCE.registerMessage(id++, PacketScythe.class, PacketScythe::encode, PacketScythe::decode, PacketScythe::handle);
    INSTANCE.registerMessage(id++, PacketTileData.class, PacketTileData::encode, PacketTileData::decode, PacketTileData::handle);
    INSTANCE.registerMessage(id++, PacketFluidSync.class, PacketFluidSync::encode, PacketFluidSync::decode, PacketFluidSync::handle);
    INSTANCE.registerMessage(id++, PacketChestSack.class, PacketChestSack::encode, PacketChestSack::decode, PacketChestSack::handle);
    INSTANCE.registerMessage(id++, PacketSwapBlock.class, PacketSwapBlock::encode, PacketSwapBlock::decode, PacketSwapBlock::handle);
    INSTANCE.registerMessage(id++, PacketRandomize.class, PacketRandomize::encode, PacketRandomize::decode, PacketRandomize::handle);
    INSTANCE.registerMessage(id++, PacketTileString.class, PacketTileString::encode, PacketTileString::decode, PacketTileString::handle);
    INSTANCE.registerMessage(id++, PacketEnergySync.class, PacketEnergySync::encode, PacketEnergySync::decode, PacketEnergySync::handle);
    INSTANCE.registerMessage(id++, PacketTileInventory.class, PacketTileInventory::encode, PacketTileInventory::decode, PacketTileInventory::handle);
  }

  public static void sendToAllClients(World world, PacketBase packet) {
    for (PlayerEntity player : world.getPlayers()) {
      ServerPlayerEntity sp = ((ServerPlayerEntity) player);
      PacketRegistry.INSTANCE.sendTo(packet,
          sp.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }
  }
}
