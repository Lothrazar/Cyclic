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
package com.lothrazar.cyclicmagic.item.signfancy;

import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.item.BaseItem;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSignEditor extends BaseItem implements IHasRecipe {

  public static final String name = "carbon_paper";
  private static final String KEY_SIGN0 = "sign_0";
  private static final String KEY_SIGN1 = "sign_1";
  private static final String KEY_SIGN2 = "sign_2";
  private static final String KEY_SIGN3 = "sign_3";

  public ItemSignEditor() {
    super();
  }

  private static void setItemStackNBT(ItemStack item, String prop, String value) {
    item.getTagCompound().setString(prop, value);
  }

  public static ITextComponent[] getSavedText(ItemStack item) {
    ITextComponent[] list = new ITextComponent[4];
    list[0] = getItemStackNBT(item, KEY_SIGN0);
    list[1] = getItemStackNBT(item, KEY_SIGN1);
    list[2] = getItemStackNBT(item, KEY_SIGN2);
    list[3] = getItemStackNBT(item, KEY_SIGN3);
    return list;
  }

  private static ITextComponent getItemStackNBT(ItemStack item, String prop) {
    String s = item.getTagCompound().getString(prop);
    //    if (s == null) {
    //      s = "";
    //    }
    return ITextComponent.Serializer.jsonToComponent(s);
  }

  public static void copySign(World world, EntityPlayer entityPlayer, TileEntitySign sign, ItemStack held) {
    if (held.getTagCompound() == null) {
      held.setTagCompound(new NBTTagCompound());
    }
    setItemStackNBT(held, KEY_SIGN0, ITextComponent.Serializer.componentToJson(sign.signText[0]));
    setItemStackNBT(held, KEY_SIGN1, ITextComponent.Serializer.componentToJson(sign.signText[1]));
    setItemStackNBT(held, KEY_SIGN2, ITextComponent.Serializer.componentToJson(sign.signText[2]));
    setItemStackNBT(held, KEY_SIGN3, ITextComponent.Serializer.componentToJson(sign.signText[3]));
    // entityPlayer.swingItem();
  }

  public static void pasteSign(World world, EntityPlayer entityPlayer, TileEntitySign sign, ItemStack held) {
    if (held.getTagCompound() == null) {
      held.setTagCompound(new NBTTagCompound());
    }
    ITextComponent t = getItemStackNBT(held, KEY_SIGN0);
    Style s = new Style();
    s.setColor(TextFormatting.GREEN);
    t.setStyle(s);
    sign.signText[0] = t;
    sign.signText[1] = getItemStackNBT(held, KEY_SIGN1);
    sign.signText[2] = getItemStackNBT(held, KEY_SIGN2);
    sign.signText[3] = getItemStackNBT(held, KEY_SIGN3);
    // world.markBlockForUpdate(sign.getPos());//so update is refreshed on
    // client sid```e
    // entityPlayer.swingItem();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack held, World player, List<String> list, net.minecraft.client.util.ITooltipFlag advanced) {
    boolean isEmpty = (held.getTagCompound() == null);
    if (isEmpty) {
      list.add(UtilChat.lang("item.carbon_paper.tooltip"));
      return;
    }
    //    String sign = getItemStackNBT(held, KEY_SIGN0) + getItemStackNBT(held, KEY_SIGN1) + getItemStackNBT(held, KEY_SIGN2) + getItemStackNBT(held, KEY_SIGN3);
    //    if (sign.length() > 0) {
    //      list.add(getItemStackNBT(held, KEY_SIGN0));
    //      list.add(getItemStackNBT(held, KEY_SIGN1));
    //      list.add(getItemStackNBT(held, KEY_SIGN2));
    //      list.add(getItemStackNBT(held, KEY_SIGN3));
    //    }
    //    String s = noteToString(held.getTagCompound().getByte(KEY_NOTE));
    //    if (s != null) {
    //      list.add(UtilChat.lang("item.carbon_paper.note") + s);
    //    }
  }
  //  @Override
  //  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
  //    ItemStack wand = player.getHeldItem(hand);
  //    setIdIfEmpty(wand);
  //    if (!world.isRemote && wand.getItem() instanceof ItemStorageBag
  //        && hand == EnumHand.MAIN_HAND) {
  //      BlockPos pos = player.getPosition();
  //      int x = pos.getX(), y = pos.getY(), z = pos.getZ();
  //      player.openGui(ModCyclic.instance, ForgeGuiHandler.GUI_INDEX_STORAGE, world, x, y, z);
  //    }
  //    return super.onItemRightClick(world, player, hand);
  //  }

  //  @Override
  //  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand hand) {
  //    ItemStack stack = entityPlayer.getHeldItem(hand);
  //    if (hand == EnumHand.MAIN_HAND && stack.getItem() == this) {
  //      //Minecraft.getMinecraft().displayGuiScreen(new GuiEnderBook(entityPlayer, stack));
  //      entityPlayer.openGui(ModCyclic.instance, ForgeGuiHandler.GUI_INDEX_SIGNPOST, world, 0, 0, 0);
  //      return super.onItemRightClick(world, entityPlayer, hand);
  //    }
  //    return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
  //  }

  @Override
  public EnumActionResult onItemUseFirst(EntityPlayer entityPlayer, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand) {
    TileEntity container = world.getTileEntity(pos);
    //    boolean isValid = false;
    //    boolean consumeItem = false;
    ItemStack held = entityPlayer.getHeldItem(hand);
    //if(!entityPlayer.isSneaking()) { return EnumActionResult.FAIL; }
    //    boolean isEmpty = (held.getTagCompound() == null);
    if (held.getItem() == this && container instanceof TileEntitySign) {
      TileEntitySign sign = (TileEntitySign) container;
      entityPlayer.openGui(ModCyclic.instance, ForgeGuiHandler.GUI_INDEX_SIGNPOST, world, pos.getX(), pos.getY(), pos.getZ());
      //      if (isEmpty) {
      //        copySign(world, entityPlayer, sign, held);
      //      }
      //      else {
      //        pasteSign(world, entityPlayer, sign, held);
      //      }
      //      isValid = true;
    }
    //    if (isValid) {
    //      UtilParticle.spawnParticle(world, EnumParticleTypes.PORTAL, pos.getX(), pos.getY(), pos.getZ());
    //      UtilSound.playSound(entityPlayer, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH);
    //    }
    return EnumActionResult.PASS;
  }

  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(this));
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 8), "ppp", "pcp", "ppp",
        'c', new ItemStack(Items.COAL, 1, 1), // charcoal
        'p', "leather");
    //also let you clean off the paper , make one with no NBT
  }
}
