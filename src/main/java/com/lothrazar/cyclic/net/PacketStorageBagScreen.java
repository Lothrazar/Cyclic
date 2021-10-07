package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.registry.ItemRegistry;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public class PacketStorageBagScreen extends PacketBase {

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

  public static void handle(PacketStorageBagScreen message, Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> {
      ServerPlayer player = context.get().getSender();
      if (player != null) {
        ItemStack serverStack = ItemStack.EMPTY;
        if (0 <= message.slot && message.slot < player.inventory.getContainerSize()) {
          serverStack = player.inventory.getItem(message.slot);
        }
        //TODO: fix refactor this whole thing with a RefilMode Enum 
        String key = message.nbtKey.getAsString();
        if (!serverStack.isEmpty()
            && serverStack.getItem() == ItemRegistry.storage_bag
            && (key.equals("refill_mode") || key.equals("deposit_mode") || key.equals("pickup_mode"))) {
          //its validated this item and nbt key so now save value in the tag
          serverStack.getOrCreateTag().put(key, message.nbtValue);
        }
      }
    });
    message.done(context);
  }

  public static PacketStorageBagScreen decode(FriendlyByteBuf buffer) {
    PacketStorageBagScreen packet = new PacketStorageBagScreen();
    packet.slot = buffer.readInt();
    packet.type = buffer.readByte();
    packet.stack = buffer.readItem();
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

  public static void encode(PacketStorageBagScreen message, FriendlyByteBuf buffer) {
    buffer.writeInt(message.slot);
    buffer.writeByte(message.type);
    buffer.writeItem(message.stack);
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
