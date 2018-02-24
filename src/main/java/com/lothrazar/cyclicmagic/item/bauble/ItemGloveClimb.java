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
package com.lothrazar.cyclicmagic.item.bauble;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.base.BaseCharm;
import com.lothrazar.cyclicmagic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ItemGloveClimb extends BaseCharm implements IHasRecipe {
  private static final int TICKS_FALLDIST_SYNC = 22;//tick every so often
  private static final double CLIMB_SPEED = 0.288D;
  public ItemGloveClimb() {
    super(6000);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 1),
        "ssl",
        "skl",
        "lli",
        's', "slimeball",
        'i', "ingotIron",
        'k', "dyeBlack",
        'l', "leather");
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer player) {
    if (!this.canTick(stack)) {
      return;
    }
    if (player.isCollidedHorizontally) {
      World world = player.getEntityWorld();
      UtilEntity.tryMakeEntityClimb(world, player, CLIMB_SPEED);
      if (world.isRemote && //setting fall distance on clientside wont work
          player instanceof EntityPlayer && player.ticksExisted % TICKS_FALLDIST_SYNC == 0) {
        ModCyclic.network.sendToServer(new PacketPlayerFalldamage());
      }
    }
  }
}
