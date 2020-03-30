package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.net.PacketChestSack;
import com.lothrazar.cyclic.net.PacketFluidSync;
import com.lothrazar.cyclic.net.PacketItemToggle;
import com.lothrazar.cyclic.net.PacketMoveBlock;
import com.lothrazar.cyclic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclic.net.PacketScythe;
import com.lothrazar.cyclic.net.PacketSwapBlock;
import com.lothrazar.cyclic.net.PacketTileData;
import net.minecraft.util.ResourceLocation;
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
    INSTANCE.registerMessage(id++, PacketMoveBlock.class, PacketMoveBlock::encode, PacketMoveBlock::decode, PacketMoveBlock::handle);
    INSTANCE.registerMessage(id++, PacketScythe.class, PacketScythe::encode, PacketScythe::decode, PacketScythe::handle);
    INSTANCE.registerMessage(id++, PacketTileData.class, PacketTileData::encode, PacketTileData::decode, PacketTileData::handle);
    INSTANCE.registerMessage(id++, PacketFluidSync.class, PacketFluidSync::encode, PacketFluidSync::decode, PacketFluidSync::handle);
    INSTANCE.registerMessage(id++, PacketChestSack.class, PacketChestSack::encode, PacketChestSack::decode, PacketChestSack::handle);
    INSTANCE.registerMessage(id++, PacketSwapBlock.class, PacketSwapBlock::encode, PacketSwapBlock::decode, PacketSwapBlock::handle);
  }
}
