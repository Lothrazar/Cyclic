/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.item.buildswap;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.item.BaseTool;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilNBT;
import com.lothrazar.cyclicmagic.core.util.UtilPlayer;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import com.lothrazar.cyclicmagic.event.EventRender;
import com.lothrazar.cyclicmagic.event.EventRender.RenderLoc;
import com.lothrazar.cyclicmagic.item.IRenderOutline;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBuildSwapper extends BaseTool implements IRenderOutline, IHasRecipe {

  private static final int durability = 1000;
  private static final int COOLDOWN = 5;
  public static String[] swapBlacklist;
  private WandType wandType;

  public ItemBuildSwapper(WandType t) {
    super(durability);
    setWandType(t);
  }

  public enum WandType {
    NORMAL, MATCH;
  }

  public enum ActionType {
    SINGLE, X3, X5, X7, X9;

    private final static String NBT = "ActionType";
    private final static String NBTTIMEOUT = "timeout";

    public static int getTimeout(ItemStack wand) {
      return UtilNBT.getItemStackNBT(wand).getInteger(NBTTIMEOUT);
    }

    public static void setTimeout(ItemStack wand) {
      UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, 15);//less than one tick
    }

    public static void tickTimeout(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int t = tags.getInteger(NBTTIMEOUT);
      if (t > 0) {
        UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, t - 1);
      }
    }

    public static int get(ItemStack wand) {
      if (wand.isEmpty()) {
        return 0;
      }
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      return tags.getInteger(NBT);
    }

    public static String getName(ItemStack wand) {
      try {
        NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
        return "tool.action." + ActionType.values()[tags.getInteger(NBT)].toString().toLowerCase();
      }
      catch (Exception e) {
        return "tool.action." + SINGLE.toString().toLowerCase();
      }
    }

    public static void toggle(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int type = tags.getInteger(NBT);
      type++;
      if (type > X9.ordinal()) {
        type = SINGLE.ordinal();
      }
      tags.setInteger(NBT, type);
      wand.setTagCompound(tags);
    }
  }

  @SubscribeEvent
  public void onHit(PlayerInteractEvent.LeftClickBlock event) {
    EntityPlayer player = event.getEntityPlayer();
    ItemStack held = player.getHeldItem(event.getHand());
    if (!held.isEmpty() && held.getItem() == this) {
      if (ActionType.getTimeout(held) > 0) {
        //without a timeout, this fires every tick. so you 'hit once' and get this happening 6 times
        return;
      }
      ActionType.setTimeout(held);
      event.setCanceled(true);
      UtilSound.playSound(player, player.getPosition(), SoundRegistry.tool_mode, SoundCategory.PLAYERS);
      if (!player.getEntityWorld().isRemote) { // server side
        ActionType.toggle(held);
        UtilChat.addChatMessage(player, UtilChat.lang(ActionType.getName(held)));
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onRender(RenderGameOverlayEvent.Post event) {
    EntityPlayer player = Minecraft.getMinecraft().player;
    ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
    if (event.isCanceled() || event.getType() != ElementType.EXPERIENCE) {
      return;
    }
    if (!held.isEmpty() && held.getItem() == this) {
      int slot = UtilPlayer.getFirstSlotWithBlock(player);
      if (slot >= 0) {
        ItemStack stack = player.inventory.getStackInSlot(slot);
        int leftOff = 0, rightOff = -18, topOff = 0, bottOff = 0;
        int xmain = RenderLoc.locToX(EventRender.renderLocation, leftOff, rightOff);
        int ymain = RenderLoc.locToY(EventRender.renderLocation, topOff, bottOff);
        if (!stack.isEmpty())
          ModCyclic.proxy.renderItemOnScreen(stack, xmain, ymain);
      }
    }
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    //if we only run this on server, clients dont get the udpate
    //so run it only on client, let packet run the server
    try {
      if (worldObj.isRemote) {
        ModCyclic.network.sendToServer(new PacketSwapBlock(pos, side, ActionType.values()[ActionType.get(stack)], this.getWandType()));
      }
      player.swingArm(hand);
      player.getCooldownTracker().setCooldown(this, COOLDOWN);
    }
    catch (ConcurrentModificationException e) {
      ModCyclic.logger.error("ConcurrentModificationException");
      ModCyclic.logger.error(e.getMessage());// message is null??
      ModCyclic.logger.error(e.getStackTrace().toString());
    }
    return EnumActionResult.FAIL;//super.onItemUse( player, worldObj, pos, hand, side, hitX, hitY, hitZ);// EnumActionResult.PASS;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(hand));
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltip.add(TextFormatting.GREEN + UtilChat.lang(ActionType.getName(stack)));
    super.addInformation(stack, playerIn, tooltip, advanced);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    ActionType.tickTimeout(stack);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public IRecipe addRecipe() {
    String ingredient = null;
    switch (this.getWandType()) {
      case MATCH:
        ingredient = "gemEmerald";
      break;
      case NORMAL:
        ingredient = "dyeBlue";
      break;
    }
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " gi",
        " ig",
        "o  ",
        'i', "ingotIron",
        'g', ingredient,
        'o', "obsidian");
  }

  public WandType getWandType() {
    return wandType;
  }

  public void setWandType(WandType wandType) {
    this.wandType = wandType;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public Set<BlockPos> renderOutline(World world, ItemStack heldItem, RayTraceResult mouseOver) {
    IBlockState state = world.getBlockState(mouseOver.getBlockPos());
    Block block = state.getBlock();
    if (block != null && block.getMaterial(state) != Material.AIR) {
      IBlockState matched = null;
      if (this.getWandType() == WandType.MATCH) {
        matched = world.getBlockState(mouseOver.getBlockPos());
      }
      List<BlockPos> places = PacketSwapBlock.getSelectedBlocks(world, mouseOver.getBlockPos(),
          ActionType.values()[ActionType.get(heldItem)], this.getWandType(),
          mouseOver.sideHit, matched);
      return new HashSet<BlockPos>(places);
      //      UtilWorld.OutlineRenderer.renderOutlines(evt, p, coordinates, 75, 0, 130);
    }
    return null;
  }

  @Override
  public int[] getRgb() {
    if (this.getWandType() == WandType.MATCH) {
    return new int[] { 75, 0, 130 };
    }
    else
      return new int[] { 28, 00, 132 };
  }
}
