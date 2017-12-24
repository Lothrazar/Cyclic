package com.lothrazar.cyclicmagic.component.cyclicwand;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSpellShiftRight implements IMessage, IMessageHandler<PacketSpellShiftRight, IMessage> {
  public PacketSpellShiftRight() {}
  public PacketSpellShiftRight(BlockPos pm) {}
  @Override
  public void fromBytes(ByteBuf buf) {}
  @Override
  public void toBytes(ByteBuf buf) {}
  @Override
  public IMessage onMessage(PacketSpellShiftRight message, MessageContext ctx) {
    EntityPlayer player = ctx.getServerHandler().player;
    // www.minecraftforge.net/forum/index.php/topic,20135.0.html
    try {
      if (SpellRegistry.spellsEnabled(player)) {
        UtilSpellCaster.shiftRight(player);
      }
    }
    catch (Exception e) {
      //some edge case where it the wand dissapeared mid action
    }
    return null;
  }
}
