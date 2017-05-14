package com.lothrazar.cyclicmagic.proxy;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {
  //https://github.com/coolAlias/Tutorial-Demo/blob/e8fa9c94949e0b1659dc0a711674074f8752d80e/src/main/java/tutorial/CommonProxy.java
  public void init() {}
  public void preInit() {}
  public World getClientWorld() {
    return null;
  }
  public EntityPlayer getClientPlayer() {
    return null;
  }
  public BlockPos getBlockMouseoverExact(int max) {
    return null;
  }
  public BlockPos getBlockMouseoverOffset(int max) {
    return null;
  }
  public EnumFacing getSideMouseover(int max) {
    return null;
  }
  public void setClientPlayerData(MessageContext ctx, NBTTagCompound tags) {}
  public IThreadListener getThreadFromContext(MessageContext ctx) {
    return ctx.getServerHandler().playerEntity.getServer();
  }
  public EntityPlayer getPlayerEntity(MessageContext ctx) {
    return ctx.getServerHandler().playerEntity;
  }
  public BlockPos getBlockMouseoverSingle() {
    return null;
  }
  public void renderItemOnScreen(ItemStack current, int x, int y) {}
  public void renderItemOnGui(ItemStack secondItemToBuy, RenderItem itemRender, FontRenderer fontRendererObj, int x, int y) {}
  public void setPlayerReach(EntityPlayer player, int currentReach) {
    if (player instanceof EntityPlayerMP) {
      ((EntityPlayerMP) player).interactionManager.setBlockReachDistance(currentReach);
    }
  }
}
