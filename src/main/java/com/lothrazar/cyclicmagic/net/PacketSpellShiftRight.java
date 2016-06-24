package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSpellShiftRight implements IMessage, IMessageHandler<PacketSpellShiftRight, IMessage> {
  public static final int ID = 13;
  public PacketSpellShiftRight() {
  }
  public PacketSpellShiftRight(BlockPos pm) {
  }
  @Override
  public void fromBytes(ByteBuf buf) {
  }
  @Override
  public void toBytes(ByteBuf buf) {
  }
  @Override
  public IMessage onMessage(PacketSpellShiftRight message, MessageContext ctx) {
    EntityPlayer player = ctx.getServerHandler().playerEntity;
    // PlayerPowerups props = PlayerPowerups.get(player);
    // www.minecraftforge.net/forum/index.php/topic,20135.0.html
    if (SpellRegistry.spellsEnabled(player)) {
      UtilSpellCaster.shiftRight(player);
    }
    return null;
  }
}
