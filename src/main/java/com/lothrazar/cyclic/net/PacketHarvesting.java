package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.util.HarvestUtil;
import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketHarvesting extends PacketBase {

  private int radius;
  private BlockPos pos;

  public PacketHarvesting(BlockPos pos, int value) {
    super();
    this.radius = value;
    this.pos = pos;
  }

  public PacketHarvesting() {}

  public static void handle(PacketHarvesting message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      //      ServerPlayerEntity player = ctx.get().getSender();
      HarvestUtil.harvestShape(ctx.get().getSender().getServerWorld(), message.pos, message.radius);
    });
    message.done(ctx);
  }

  public static PacketHarvesting decode(PacketBuffer buf) {
    PacketHarvesting p = new PacketHarvesting();
    p.pos = buf.readBlockPos();
    p.radius = buf.readInt();
    return p;
  }

  public static void encode(PacketHarvesting msg, PacketBuffer buf) {
    buf.writeBlockPos(msg.pos);
    buf.writeInt(msg.radius);
  }
}
