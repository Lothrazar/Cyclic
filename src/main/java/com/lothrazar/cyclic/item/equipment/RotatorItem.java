package com.lothrazar.cyclic.item.equipment;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.net.PacketRotateBlock;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;

import net.minecraft.world.item.Item.Properties;

public class RotatorItem extends ItemBase {

  public RotatorItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    if (context.getLevel().isClientSide) {
      PacketRegistry.INSTANCE.sendToServer(new PacketRotateBlock(context.getClickedPos(), context.getClickedFace(), context.getHand()));
      context.getPlayer().swing(context.getHand());
    }
    return super.useOn(context);
  }
}
