package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.base.PacketBase;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class PacketItemStackNBT extends PacketBase {

  private ItemStack stack;
  private byte type;
  int slot;
  private StringNBT nbtKey;
  private INBT nbtValue;

  public PacketItemStackNBT() {}

  public PacketItemStackNBT(@Nonnull ItemStack stack, int slot, byte type, @Nonnull StringNBT nbtKey, @Nonnull INBT nbtValue) {
    this.stack = stack;
    this.slot = slot;
    this.type = type;
    this.nbtKey = nbtKey;
    this.nbtValue = nbtValue;
  }

  public static void handle(PacketItemStackNBT message, Supplier<NetworkEvent.Context> context) {
    System.out.println("Handling from client");
    context.get().enqueueWork(() -> {
      ServerPlayerEntity player = context.get().getSender();
      if (player != null) {
        ItemStack serverStack = ItemStack.EMPTY;
        if (0 <= message.slot && message.slot < player.inventory.getSizeInventory())
          serverStack = player.inventory.getStackInSlot(message.slot);
        if (!serverStack.isEmpty()) {
          System.out.printf("Before set on stack %s%n", serverStack.getOrCreateTag().getString());
          serverStack.getOrCreateTag().put(message.nbtKey.getString(), message.nbtValue);
          System.out.printf("After set on stack %s%n", serverStack.getOrCreateTag().getString());
        }
      }
    });
    message.done(context);
  }

  public static PacketItemStackNBT decode(PacketBuffer buffer) {
    PacketItemStackNBT packet = new PacketItemStackNBT();
    packet.slot = buffer.readInt();
    packet.type = buffer.readByte();
    packet.stack = buffer.readItemStack();
    packet.nbtKey = StringNBT.valueOf(buffer.readString());
    switch(packet.type) {
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
        packet.nbtValue = StringNBT.valueOf(buffer.readString());
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
    }
    return packet;
  }

  public static void encode(PacketItemStackNBT message, PacketBuffer buffer) {
    buffer.writeInt(message.slot);
    buffer.writeByte(message.type);
    buffer.writeItemStack(message.stack);
    buffer.writeString(message.nbtKey.getString());
    switch(message.type) {
      case 1: //Byte
        buffer.writeByte(((ByteNBT)message.nbtValue).getByte());
        break;
      case 2: //Short
        buffer.writeShort(((ShortNBT)message.nbtValue).getShort());
        break;
      case 3: //Int
        buffer.writeInt(((IntNBT)message.nbtValue).getInt());
        break;
      case 4: //Long
        buffer.writeLong(((LongNBT)message.nbtValue).getLong());
        break;
      case 5: //Float
        buffer.writeFloat(((FloatNBT)message.nbtValue).getFloat());
        break;
      case 6: //Double
        buffer.writeDouble(((DoubleNBT)message.nbtValue).getDouble());
        break;
      case 7: //ByteArray
        buffer.writeByteArray(((ByteArrayNBT)message.nbtValue).getByteArray());
        break;
      case 8: //String
        buffer.writeString(((StringNBT)message.nbtValue).getString());
        break;
      case 9: //List... not sure of best way to handle this one since there's no constructor/setter. Look at other implementations?
        buffer.writeString("There could have been a list here, if I knew how to process it.");
        break;
      case 10: //CompoundNBT
        buffer.writeCompoundTag(((CompoundNBT)message.nbtValue));
        break;
      case 11: //IntArray
        buffer.writeVarIntArray(((IntArrayNBT)message.nbtValue).getIntArray());
        break;
      case 12: //LongArray
        buffer.writeLongArray(((LongArrayNBT)message.nbtValue).getAsLongArray());
        break;
      default: //0 is EndNBT, shouldn't ever happen, I don't think.
        buffer.writeString("");
    }
  }
}
