package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import net.minecraftforge.fmllegacy.network.NetworkEvent.Context;

public class PacketBaseCyclic {

  public void done(Supplier<Context> ctx) {
    ctx.get().setPacketHandled(true);
  }
}
