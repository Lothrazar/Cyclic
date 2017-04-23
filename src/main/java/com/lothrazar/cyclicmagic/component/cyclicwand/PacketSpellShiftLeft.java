package com.lothrazar.cyclicmagic.component.cyclicwand;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSpellShiftLeft implements IMessage, IMessageHandler<PacketSpellShiftLeft, IMessage> {
  public PacketSpellShiftLeft() {}
  @Override
  public void fromBytes(ByteBuf buf) {}
  @Override
  public void toBytes(ByteBuf buf) {}
  @Override
  public IMessage onMessage(PacketSpellShiftLeft message, MessageContext ctx) {
    EntityPlayer player = ctx.getServerHandler().playerEntity;
    // PlayerPowerups props = PlayerPowerups.get(player);
    // www.minecraftforge.net/forum/index.php/topic,20135.0.html
    if (SpellRegistry.spellsEnabled(player)) {
      UtilSpellCaster.shiftLeft(player);
    }
    return null;
  }
}
