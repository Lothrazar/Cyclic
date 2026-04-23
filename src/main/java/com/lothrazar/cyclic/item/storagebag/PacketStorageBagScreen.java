package com.lothrazar.cyclic.item.storagebag;

import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class PacketStorageBagScreen implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {

  public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<PacketStorageBagScreen> TYPE = new Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.lothrazar.cyclic.ModCyclic.MODID, "packet_storage_bag_screen"));

  public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PacketStorageBagScreen> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of(PacketStorageBagScreen::encode, PacketStorageBagScreen::decode);


  @Override
  public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {
    return TYPE;
  }


  private ItemStack stack;
  private byte type;
  int slot;
  private StringTag nbtKey;
  private Tag nbtValue;

  public PacketStorageBagScreen() {}

  public PacketStorageBagScreen(ItemStack stack, int slot, byte type, StringTag nbtKey, Tag nbtValue) {
    this.stack = stack;
    this.slot = slot;
    this.type = type;
    this.nbtKey = nbtKey;
    this.nbtValue = nbtValue;
  }

  public static void handle(PacketStorageBagScreen message, net.neoforged.neoforge.network.handling.IPayloadContext context) {
    context.enqueueWork(() -> {
      net.minecraft.server.level.ServerPlayer player = (net.minecraft.server.level.ServerPlayer) context.player();
      if (player != null) {
        ItemStack serverStack = ItemStack.EMPTY;
        if (0 <= message.slot && message.slot < player.getInventory().getContainerSize()) {
          serverStack = player.getInventory().getItem(message.slot);
        }
        String key = message.nbtKey.getAsString();
        if (!serverStack.isEmpty()
            && serverStack.getItem() == ItemRegistry.STORAGE_BAG.get()
            && (key.equals(RefillMode.NBT) || key.equals(DepositMode.NBT) || key.equals(PickupMode.NBT))) {
          //its validated this item and nbt key so now save value in the tag
          net.minecraft.nbt.CompoundTag tag = ItemStorageBag.getCustomData(serverStack); tag.put(key, message.nbtValue); ItemStorageBag.setCustomData(serverStack, tag);
        }
      }
    });
  }

  public static PacketStorageBagScreen decode(net.minecraft.network.RegistryFriendlyByteBuf buffer) {
    PacketStorageBagScreen packet = new PacketStorageBagScreen();
    packet.slot = buffer.readInt();
    packet.type = buffer.readByte();
    packet.stack = ItemStack.STREAM_CODEC.decode(buffer);
    packet.nbtKey = StringTag.valueOf(buffer.readUtf(32767));
    switch (packet.type) {
      case 1: //Byte
        packet.nbtValue = ByteTag.valueOf(buffer.readByte());
      break;
      case 2: //Short
        packet.nbtValue = ShortTag.valueOf(buffer.readShort());
      break;
      case 3: //Int
        packet.nbtValue = IntTag.valueOf(buffer.readInt());
      break;
      case 4: //Long
        packet.nbtValue = LongTag.valueOf(buffer.readLong());
      break;
      case 5: //Float
        packet.nbtValue = FloatTag.valueOf(buffer.readFloat());
      break;
      case 6: //Double
        packet.nbtValue = DoubleTag.valueOf(buffer.readDouble());
      break;
      case 7: //ByteArray
        packet.nbtValue = new ByteArrayTag(buffer.readByteArray());
      break;
      case 8: //String
        packet.nbtValue = StringTag.valueOf(buffer.readUtf(32767));
      break;
      case 9: //List... not sure of best way to handle this one since there's no constructor/setter. Look at other implementations?
        packet.nbtValue = new ListTag();
      break;
      case 10: //CompoundNBT
        packet.nbtValue = buffer.readNbt();
      break;
      case 11: //IntArray
        packet.nbtValue = new IntArrayTag(buffer.readVarIntArray());
      break;
      case 12: //LongArray
        packet.nbtValue = new LongArrayTag(buffer.readLongArray(null));
      break;
      default: //0 is EndNBT, shouldn't ever happen, I don't think.
        packet.nbtValue = StringTag.valueOf("");
      break;
    }
    return packet;
  }

  public static void encode(net.minecraft.network.RegistryFriendlyByteBuf buffer, PacketStorageBagScreen message) {
    buffer.writeInt(message.slot);
    buffer.writeByte(message.type);
    ItemStack.STREAM_CODEC.encode(buffer, message.stack);
    buffer.writeUtf(message.nbtKey.getAsString());
    switch (message.type) {
      case 1: //Byte
        buffer.writeByte(((ByteTag) message.nbtValue).getAsByte());
      break;
      case 2: //Short
        buffer.writeShort(((ShortTag) message.nbtValue).getAsShort());
      break;
      case 3: //Int
        buffer.writeInt(((IntTag) message.nbtValue).getAsInt());
      break;
      case 4: //Long
        buffer.writeLong(((LongTag) message.nbtValue).getAsLong());
      break;
      case 5: //Float
        buffer.writeFloat(((FloatTag) message.nbtValue).getAsFloat());
      break;
      case 6: //Double
        buffer.writeDouble(((DoubleTag) message.nbtValue).getAsDouble());
      break;
      case 7: //ByteArray
        buffer.writeByteArray(((ByteArrayTag) message.nbtValue).getAsByteArray());
      break;
      case 8: //String
        buffer.writeUtf(((StringTag) message.nbtValue).getAsString());
      break;
      case 9: //List... not sure of best way to handle this one since there's no constructor/setter. Look at other implementations?
        buffer.writeUtf("There could have been a list here, if I knew how to process it.");
      break;
      case 10: //CompoundNBT
        buffer.writeNbt(((CompoundTag) message.nbtValue));
      break;
      case 11: //IntArray
        buffer.writeVarIntArray(((IntArrayTag) message.nbtValue).getAsIntArray());
      break;
      case 12: //LongArray
        buffer.writeLongArray(((LongArrayTag) message.nbtValue).getAsLongArray());
      break;
      default: //0 is EndNBT, shouldn't ever happen, I don't think.
        buffer.writeUtf("");
    }
  }
}
