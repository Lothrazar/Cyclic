package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.registry.ItemRegistry;
import java.util.function.Supplier;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.nbt.ShortNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketStorageBagScreen extends PacketBase {

  private ItemStack stack;
  private byte type;
  int slot;
  private StringNBT nbtKey;
  private INBT nbtValue;

  public PacketStorageBagScreen() {}

  public PacketStorageBagScreen(ItemStack stack, int slot, byte type, StringNBT nbtKey, INBT nbtValue) {
    this.stack = stack;
    this.slot = slot;
    this.type = type;
    this.nbtKey = nbtKey;
    this.nbtValue = nbtValue;
  }

  public static void handle(PacketStorageBagScreen message, Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> {
      ServerPlayerEntity player = context.get().getSender();
      if (player != null) {
        ItemStack serverStack = ItemStack.EMPTY;
        if (0 <= message.slot && message.slot < player.inventory.getSizeInventory()) {
          serverStack = player.inventory.getStackInSlot(message.slot);
        }
        //TODO: fix refactor this whole thing with RefilMode enum or somee shit
        String key = message.nbtKey.getString();
        if (!serverStack.isEmpty()
            && serverStack.getItem() == ItemRegistry.storage_bag
            && (key.equals("refill_mode") || key.equals("deposit_mode") || key.equals("pickup_mode"))) {
          System.out.println("message.nbtKey.getString()" + message.nbtKey.getString());
          //          System.out.printf("Before set on stack %s%n", serverStack.getOrCreateTag().getString());
          serverStack.getOrCreateTag().put(key, message.nbtValue);
          //          System.out.printf("After set on stack %s%n", serverStack.getOrCreateTag().getString());
        }
      }
    });
    message.done(context);
  }

  public static PacketStorageBagScreen decode(PacketBuffer buffer) {
    PacketStorageBagScreen packet = new PacketStorageBagScreen();
    packet.slot = buffer.readInt();
    packet.type = buffer.readByte();
    packet.stack = buffer.readItemStack();
    packet.nbtKey = StringNBT.valueOf(buffer.readString(32767));
    switch (packet.type) {
      case 1: //Byte
        packet.nbtValue = ByteNBT.valueOf(buffer.readByte());
      break;
      case 2: //Short
        packet.nbtValue = ShortNBT.valueOf(buffer.readShort());
      break;
      case 3: //Int
        packet.nbtValue = IntNBT.valueOf(buffer.readInt());
      break;
      case 4: //Long
        packet.nbtValue = LongNBT.valueOf(buffer.readLong());
      break;
      case 5: //Float
        packet.nbtValue = FloatNBT.valueOf(buffer.readFloat());
      break;
      case 6: //Double
        packet.nbtValue = DoubleNBT.valueOf(buffer.readDouble());
      break;
      case 7: //ByteArray
        packet.nbtValue = new ByteArrayNBT(buffer.readByteArray());
      break;
      case 8: //String
        packet.nbtValue = StringNBT.valueOf(buffer.readString(32767));
      break;
      case 9: //List... not sure of best way to handle this one since there's no constructor/setter. Look at other implementations?
        packet.nbtValue = new ListNBT();
      break;
      case 10: //CompoundNBT
        packet.nbtValue = buffer.readCompoundTag();
      break;
      case 11: //IntArray
        packet.nbtValue = new IntArrayNBT(buffer.readVarIntArray());
      break;
      case 12: //LongArray
        packet.nbtValue = new LongArrayNBT(buffer.readLongArray(null));
      break;
      default: //0 is EndNBT, shouldn't ever happen, I don't think.
        packet.nbtValue = StringNBT.valueOf("");
      break;
    }
    return packet;
  }

  public static void encode(PacketStorageBagScreen message, PacketBuffer buffer) {
    buffer.writeInt(message.slot);
    buffer.writeByte(message.type);
    buffer.writeItemStack(message.stack);
    buffer.writeString(message.nbtKey.getString());
    switch (message.type) {
      case 1: //Byte
        buffer.writeByte(((ByteNBT) message.nbtValue).getByte());
      break;
      case 2: //Short
        buffer.writeShort(((ShortNBT) message.nbtValue).getShort());
      break;
      case 3: //Int
        buffer.writeInt(((IntNBT) message.nbtValue).getInt());
      break;
      case 4: //Long
        buffer.writeLong(((LongNBT) message.nbtValue).getLong());
      break;
      case 5: //Float
        buffer.writeFloat(((FloatNBT) message.nbtValue).getFloat());
      break;
      case 6: //Double
        buffer.writeDouble(((DoubleNBT) message.nbtValue).getDouble());
      break;
      case 7: //ByteArray
        buffer.writeByteArray(((ByteArrayNBT) message.nbtValue).getByteArray());
      break;
      case 8: //String
        buffer.writeString(((StringNBT) message.nbtValue).getString());
      break;
      case 9: //List... not sure of best way to handle this one since there's no constructor/setter. Look at other implementations?
        buffer.writeString("There could have been a list here, if I knew how to process it.");
      break;
      case 10: //CompoundNBT
        buffer.writeCompoundTag(((CompoundNBT) message.nbtValue));
      break;
      case 11: //IntArray
        buffer.writeVarIntArray(((IntArrayNBT) message.nbtValue).getIntArray());
      break;
      case 12: //LongArray
        buffer.writeLongArray(((LongArrayNBT) message.nbtValue).getAsLongArray());
      break;
      default: //0 is EndNBT, shouldn't ever happen, I don't think.
        buffer.writeString("");
    }
  }
}
