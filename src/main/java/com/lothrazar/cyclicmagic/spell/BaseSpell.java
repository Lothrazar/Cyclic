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
package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseSpell implements ISpell {

  private ResourceLocation icon;
  private int ID;
  private String name;

  protected void init(int id, String n) {
    ID = id;
    name = n;
    icon = new ResourceLocation(Const.MODID, "textures/spells/" + name + ".png");
  }

  @Override
  public String getName() {
    return UtilChat.lang("spell." + name + ".name");
  }

  @Override
  public String getUnlocalizedName() {
    return name;
  }

  @Override
  public String getInfo() {
    return UtilChat.lang("spell." + name + ".info");
  }

  @Override
  public void onCastFailure(World world, EntityPlayer player, BlockPos pos) {
    UtilSound.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH);
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos) {
    if (player.capabilities.isCreativeMode) {
      return true;
    }
    ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
    if (wand == null) {
      return false;
    }
    return true;
  }

  @Override
  public ResourceLocation getIconDisplay() {
    return icon;
  }
}
