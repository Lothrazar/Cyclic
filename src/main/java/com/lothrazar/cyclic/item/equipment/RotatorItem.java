package com.lothrazar.cyclic.item.equipment;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.packet.PacketRotateBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class RotatorItem extends ItemBaseCyclic {

  public RotatorItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    if (context.getLevel().isClientSide()) {
      ClientPacketDistributor.sendToServer(new PacketRotateBlock(context.getClickedPos(), context.getClickedFace(), context.getHand()));
      context.getPlayer().swing(context.getHand());
    }
    return super.useOn(context);
  }
}
