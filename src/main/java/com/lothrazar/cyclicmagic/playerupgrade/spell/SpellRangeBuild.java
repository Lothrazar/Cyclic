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
package com.lothrazar.cyclicmagic.playerupgrade.spell;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.cyclicwand.PacketRangeBuild;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SpellRangeBuild extends BaseSpellRange {

  public final static int max = 32;// max search range
  private PlaceType type;

  public static enum PlaceType {
    PLACE, UP, DOWN, LEFT, RIGHT;
  }

  public SpellRangeBuild(int id, String n, PlaceType t) {
    super.init(id, n);
    this.setType(t);
  }

  @Override
  public boolean cast(World world, EntityPlayer p, ItemStack wand, BlockPos posIn, EnumFacing side) {
    if (world.isRemote) {
      // only client side can call this method. mouseover does not exist on server
      RayTraceResult ray = ModCyclic.proxy.getRayTraceResult(maxRange);
      if (ray != null && ray.getBlockPos() != null) {
        ModCyclic.network.sendToServer(new PacketRangeBuild(ray, this.getID(), getType()));
      }
      //      ItemStack heldWand = UtilSpellCaster.getPlayerWandIfHeld(p);
      //      if (!heldWand.isEmpty()) {
      //        int itemSlot = ItemCyclicWand.BuildType.getSlot(heldWand);
      //        IBlockState state = InventoryWand.getToPlaceFromSlot(heldWand, itemSlot);
      //        if (state != null && state.getBlock() != null && posOffset != null) {
      //          UtilSound.playSoundPlaceBlock(world, posOffset, state.getBlock());
      //        }
      //      }
    }
    return true;
  }

  public PlaceType getType() {
    return type;
  }

  public void setType(PlaceType type) {
    this.type = type;
  }
}
