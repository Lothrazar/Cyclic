package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.ItemChestSack;
import com.lothrazar.cyclicmagic.item.ItemChestSackEmpty;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketChestSack implements IMessage, IMessageHandler<PacketChestSack, IMessage> {
  private BlockPos pos;
  public PacketChestSack() {}
  public PacketChestSack(BlockPos mouseover) {
    pos = mouseover;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketChestSack message, MessageContext ctx) {
    PacketChestSack.checkThreadAndEnqueue(message, ctx);
    return null;
  }
  private static void checkThreadAndEnqueue(final PacketChestSack message, final MessageContext ctx) {
    if (ctx.side.isServer() && message != null && message.pos != null) {
      IThreadListener thread = ModCyclic.proxy.getThreadFromContext(ctx);
      // pretty much copied straight from vanilla code, see {@link PacketThreadUtil#checkThreadAndEnqueue}
      thread.addScheduledTask(new Runnable() {
        public void run() {
          BlockPos position = message.pos;
          EntityPlayer player = ctx.getServerHandler().playerEntity;
          World world = player.getEntityWorld();
          TileEntity tile = world.getTileEntity(position);
          IBlockState state = world.getBlockState(position);
          NBTTagCompound tileData = new NBTTagCompound(); //thanks for the tip on setting tile entity data from nbt tag: https://github.com/romelo333/notenoughwands1.8.8/blob/master/src/main/java/romelo333/notenoughwands/Items/DisplacementWand.java
          tile.writeToNBT(tileData);
          NBTTagCompound itemData = new NBTTagCompound();
          itemData.setString(ItemChestSack.KEY_BLOCKNAME, state.getBlock().getUnlocalizedName());
          itemData.setTag(ItemChestSack.KEY_BLOCKTILE, tileData);
          itemData.setInteger(ItemChestSack.KEY_BLOCKID, Block.getIdFromBlock(state.getBlock()));
          itemData.setInteger(ItemChestSack.KEY_BLOCKSTATE, state.getBlock().getMetaFromState(state));
          EnumHand hand = EnumHand.MAIN_HAND;
          ItemStack held = player.getHeldItem(hand);
          if (held == null || held.getItem() instanceof ItemChestSackEmpty == false) {
            hand = EnumHand.OFF_HAND;
            held = player.getHeldItem(hand);
          }
          if (held != null && held.getCount() > 0) { //https://github.com/PrinceOfAmber/Cyclic/issues/181
            if (held.getItem() instanceof ItemChestSackEmpty) {
              Item chest_sack = ((ItemChestSackEmpty) held.getItem()).getFullSack();
              if (chest_sack != null) {
                ItemStack drop = new ItemStack(chest_sack);
                drop.setTagCompound(itemData);
                UtilItemStack.dropItemStackInWorld(world, player.getPosition(), drop);
                UtilPlaceBlocks.destroyBlock(world, position);
                if (player.capabilities.isCreativeMode == false && held.getCount() > 0) {
                  held.shrink(1);
                  if (held.getCount() == 0) {
                    held = ItemStack.EMPTY;
                    player.setHeldItem(hand, ItemStack.EMPTY);
                  }
                }
              }
            }
          }
        }
      });
    }
  }
}
