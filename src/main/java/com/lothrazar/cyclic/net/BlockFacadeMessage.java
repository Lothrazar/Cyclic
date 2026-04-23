package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.block.facade.IBlockFacade;
import com.lothrazar.cyclic.block.facade.ITileFacade;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class BlockFacadeMessage implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {

  public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<BlockFacadeMessage> TYPE = new Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.lothrazar.cyclic.ModCyclic.MODID, "block_facade_message"));

  public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, BlockFacadeMessage> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of(BlockFacadeMessage::encode, BlockFacadeMessage::decode);


  @Override
  public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {
    return TYPE;
  }


  private BlockPos pos;
  private boolean erase = false;
  private CompoundTag blockStateTag = new CompoundTag();

  private BlockFacadeMessage() {}

  public BlockFacadeMessage(BlockPos pos, CompoundTag state) {
    this.pos = pos;
    this.blockStateTag = state;
    this.erase = false;
  }

  public BlockFacadeMessage(BlockPos pos, boolean eraseIn) {
    this.pos = pos;
    this.erase = eraseIn;
    blockStateTag = new CompoundTag();
  }

  public static void handle(BlockFacadeMessage message, net.neoforged.neoforge.network.handling.IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      ServerPlayer player = (ServerPlayer) ctx.player();
      ServerLevel serverWorld = (ServerLevel) player.level();
      BlockState bs = serverWorld.getBlockState(message.pos);
      if (bs.getBlock() instanceof IBlockFacade facadeBlock) {
        //
        ITileFacade tile = facadeBlock.getTileFacade(serverWorld, message.pos);
        if (message.erase) {
          tile.setFacade(new CompoundTag());
        }
        else {
          tile.setFacade(message.blockStateTag);
        }
        serverWorld.markAndNotifyBlock(message.pos, serverWorld.getChunkAt(message.pos),
            bs, bs, 3, 1);
        serverWorld.sendBlockUpdated(message.pos, bs, bs, 3);
        serverWorld.blockUpdated(message.pos, bs.getBlock());
      }
    });
    // ctx.setPacketHandled(true);
  }

  public static BlockFacadeMessage decode(net.minecraft.network.RegistryFriendlyByteBuf buf) {
    BlockFacadeMessage message = new BlockFacadeMessage();
    message.erase = buf.readBoolean();
    message.pos = buf.readBlockPos();
    message.blockStateTag = buf.readNbt();
    return message;
  }

  public static void encode(net.minecraft.network.RegistryFriendlyByteBuf buf, BlockFacadeMessage msg) {
    buf.writeBoolean(msg.erase);
    buf.writeBlockPos(msg.pos);
    buf.writeNbt(msg.blockStateTag);
  }
}
