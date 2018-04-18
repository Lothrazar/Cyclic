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
package com.lothrazar.cyclicmagic.component.vector;

import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockVectorPlate extends ItemBlock {

  // http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1432714-forge-using-addinformation-on-a-block
  public ItemBlockVectorPlate(Block block) {
    super(block);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World player, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    stack.getItem().updateItemStackNBT(stack.getTagCompound());
    String d = UtilNBT.getItemStackDisplayInteger(stack, TileEntityVector.NBT_ANGLE);
    if (d.length() > 0)
      tooltip.add(UtilChat.lang("tile.plate_vector.tooltip.angle") + d);
    d = UtilNBT.getItemStackDisplayInteger(stack, TileEntityVector.NBT_POWER);
    if (d.length() > 0)
      tooltip.add(UtilChat.lang("tile.plate_vector.tooltip.power") + d);
    d = UtilNBT.getItemStackDisplayInteger(stack, TileEntityVector.NBT_YAW);
    if (d.length() > 0)
      tooltip.add(UtilChat.lang("tile.plate_vector.tooltip.yaw") + d);
  }

  /**
   * set default dag data so its nonempty. just like ItemSkull
   */
  @Override
  public boolean updateItemStackNBT(NBTTagCompound nbt) {
    boolean altered = false;
    if (nbt == null) {
      nbt = new NBTTagCompound();
    }
    if (!nbt.hasKey(TileEntityVector.NBT_ANGLE)) {
      nbt.setInteger(TileEntityVector.NBT_ANGLE, TileEntityVector.DEFAULT_ANGLE);
      altered = true;
    }
    if (!nbt.hasKey(TileEntityVector.NBT_POWER)) {
      nbt.setInteger(TileEntityVector.NBT_POWER, TileEntityVector.DEFAULT_POWER);
      altered = true;
    }
    if (!nbt.hasKey(TileEntityVector.NBT_YAW)) {
      nbt.setInteger(TileEntityVector.NBT_YAW, TileEntityVector.DEFAULT_YAW);
      altered = true;
    }
    if (!nbt.hasKey(TileEntityVector.NBT_SOUND)) {
      nbt.setInteger(TileEntityVector.NBT_SOUND, 1);
      altered = true;
    }
    return altered;
  }
}
