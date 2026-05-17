package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.event.HorseCarrotClientCache;
import com.lothrazar.cyclic.item.animal.ItemHorseCopperRadar;
import com.lothrazar.cyclic.item.animal.ItemHorseEmeraldJump;
import com.lothrazar.cyclic.item.animal.ItemHorseEnder;
import com.lothrazar.cyclic.item.animal.ItemHorseHealthDiamondCarrot;
import com.lothrazar.cyclic.item.animal.ItemHorseNetheriteFire;
import com.lothrazar.cyclic.item.animal.ItemHorsePrismarineWater;
import com.lothrazar.cyclic.item.animal.ItemHorseRedstoneSpeed;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketSyncHorseCarrots implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketSyncHorseCarrots> TYPE =
      new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "sync_horse_carrots"));

  public static final StreamCodec<RegistryFriendlyByteBuf, PacketSyncHorseCarrots> STREAM_CODEC =
      StreamCodec.of(PacketSyncHorseCarrots::encode, PacketSyncHorseCarrots::decode);

  private static final int FLAG_COPPER = 1;
  private static final int FLAG_NETHERITE = 2;
  private static final int FLAG_PRISMARINE = 4;

  public final int entityId;
  public final int redstone;
  public final int diamond;
  public final int emerald;
  public final int ender;
  public final byte flags;

  public PacketSyncHorseCarrots(int entityId, int redstone, int diamond, int emerald, int ender, byte flags) {
    this.entityId = entityId;
    this.redstone = redstone;
    this.diamond = diamond;
    this.emerald = emerald;
    this.ender = ender;
    this.flags = flags;
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public boolean copper() { return (flags & FLAG_COPPER) != 0; }
  public boolean netherite() { return (flags & FLAG_NETHERITE) != 0; }
  public boolean prismarine() { return (flags & FLAG_PRISMARINE) != 0; }

  public static PacketSyncHorseCarrots readFrom(AbstractHorse horse) {
    CompoundTag d = horse.getPersistentData();
    byte flags = 0;
    if (d.getBoolean(ItemHorseCopperRadar.NBT_KEY)) { flags |= FLAG_COPPER; }
    if (d.getBoolean(ItemHorseNetheriteFire.NBT_KEY)) { flags |= FLAG_NETHERITE; }
    if (d.getBoolean(ItemHorsePrismarineWater.NBT_KEY)) { flags |= FLAG_PRISMARINE; }
    return new PacketSyncHorseCarrots(
        horse.getId(),
        d.getInt(ItemHorseRedstoneSpeed.NBT_COUNT),
        d.getInt(ItemHorseHealthDiamondCarrot.NBT_COUNT),
        d.getInt(ItemHorseEmeraldJump.NBT_COUNT),
        d.getInt(ItemHorseEnder.NBT_KEYACTIVE),
        flags);
  }

  public static void broadcastToTrackers(AbstractHorse horse) {
    if (horse.level() instanceof ServerLevel) {
      PacketDistributor.sendToPlayersTrackingEntity(horse, readFrom(horse));
    }
  }

  public static void sendTo(ServerPlayer player, AbstractHorse horse) {
    PacketDistributor.sendToPlayer(player, readFrom(horse));
  }

  public static void handle(PacketSyncHorseCarrots msg, IPayloadContext ctx) {
    ctx.enqueueWork(() -> HorseCarrotClientCache.put(msg));
  }

  public static void encode(RegistryFriendlyByteBuf buf, PacketSyncHorseCarrots msg) {
    buf.writeVarInt(msg.entityId);
    buf.writeVarInt(msg.redstone);
    buf.writeVarInt(msg.diamond);
    buf.writeVarInt(msg.emerald);
    buf.writeVarInt(msg.ender);
    buf.writeByte(msg.flags);
  }

  public static PacketSyncHorseCarrots decode(RegistryFriendlyByteBuf buf) {
    return new PacketSyncHorseCarrots(
        buf.readVarInt(),
        buf.readVarInt(),
        buf.readVarInt(),
        buf.readVarInt(),
        buf.readVarInt(),
        buf.readByte());
  }
}
