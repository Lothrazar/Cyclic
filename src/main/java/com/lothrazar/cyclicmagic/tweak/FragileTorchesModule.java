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
package com.lothrazar.cyclicmagic.tweak;

import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.registry.module.BaseEventModule;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FragileTorchesModule extends BaseEventModule implements IHasConfig {

  private static final float oddsWillBreak = 0.01F;
  private boolean fragileTorches;

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (fragileTorches) {
      Entity ent = event.getEntity();
      World world = ent.getEntityWorld();
      if (world.isRemote) {
        return;
      } //we only need to break block on server, it gets propogated for us
      if (world.rand.nextDouble() > oddsWillBreak) {
        return;
      } //no chance of breaking anyway, just stop
      //ok the dice roll passed
      if (ent instanceof EntityLiving == false) {
        return;
      }
      EntityLivingBase living = (EntityLivingBase) event.getEntity();
      if (living == null) {
        return;
      }
      if (living instanceof EntityPlayer && ((EntityPlayer) living).isSneaking()) {
        return;
      } //if you are a player, then cancel if sneaking
      if (world.getGameRules().getBoolean("mobGriefing") == false) {
        return;
      }
      if (UtilWorld.isBlockTorch(world, living.getPosition())) {
        world.destroyBlock(living.getPosition(), true);
      }
    }
  }

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.blocks;
    config.addCustomCategoryComment(category, "Tweaks to new and existing blocks");
    fragileTorches = config.getBoolean("Fragile Torches", category, false,
        "Torches can get knocked over when passed through by living entities");
  }
}
