package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.item.ItemChestSack;
import com.lothrazar.cyclicmagic.item.ItemChestSackEmpty;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketStorageSack implements IMessage, IMessageHandler<PacketStorageSack, IMessage> {
  private BlockPos pos;
  public PacketStorageSack() {
  }
  public PacketStorageSack(BlockPos mouseover) {
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
  public IMessage onMessage(PacketStorageSack message, MessageContext ctx) {
    if (ctx.side.isServer() && message != null && message.pos != null) {
      BlockPos position = message.pos;
      EntityPlayer player = ctx.getServerHandler().playerEntity;
      World world = player.worldObj;
      TileEntity tile = world.getTileEntity(position);
      IBlockState state = world.getBlockState(position);
      NBTTagCompound tileData = new NBTTagCompound(); //thanks for the tip on setting tile entity data from nbt tag: https://github.com/romelo333/notenoughwands1.8.8/blob/master/src/main/java/romelo333/notenoughwands/Items/DisplacementWand.java
      tile.writeToNBT(tileData);
      NBTTagCompound itemData = new NBTTagCompound();
      itemData.setString(ItemChestSack.KEY_BLOCKNAME, state.getBlock().getUnlocalizedName());
      itemData.setTag(ItemChestSack.KEY_BLOCKTILE, tileData);
      itemData.setInteger(ItemChestSack.KEY_BLOCKID, Block.getIdFromBlock(state.getBlock()));
      itemData.setInteger(ItemChestSack.KEY_BLOCKSTATE, state.getBlock().getMetaFromState(state));
      ItemStack drop = new ItemStack(ItemRegistry.chest_sack);
      drop.setTagCompound(itemData);
      ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
      if (held == null || held.getItem() instanceof ItemChestSackEmpty == false) {
        held = player.getHeldItem(EnumHand.OFF_HAND);
      }
      if (held != null && player.capabilities.isCreativeMode == false) {
        held.stackSize--;
      }
//      if (held != null && held.stackSize > 0) {
//        player.getCooldownTracker().setCooldown(held.getItem(), 1);
//      }
      UtilEntity.dropItemStackInWorld(world, player.getPosition(), drop);
      UtilPlaceBlocks.destroyBlock(world, position);
    }
    return null;
  }
}
