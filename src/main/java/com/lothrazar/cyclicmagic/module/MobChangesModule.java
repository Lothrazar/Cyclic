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
package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobChangesModule extends BaseEventModule implements IHasConfig {
  private boolean endermanDrop;
  private boolean endermanPickupBlocks;
  private boolean nameTagDeath;
  @SubscribeEvent
  public void onLivingDropsEvent(LivingDropsEvent event) {
    Entity entity = event.getEntity();
    World world = entity.getEntityWorld();
    if (nameTagDeath) {
      if (entity.getCustomNameTag() != null && entity.getCustomNameTag() != "") {
        // item stack NBT needs the name enchanted onto it
        if (world.isRemote == false) {
          ItemStack nameTag = UtilNBT.buildEnchantedNametag(entity.getCustomNameTag());
          UtilItemStack.dropItemStackInWorld(world, entity.getPosition(), nameTag);
        }
      }
    }
    if (endermanDrop && entity instanceof EntityEnderman) {
      EntityEnderman mob = (EntityEnderman) entity;
      IBlockState bs = mob.getHeldBlockState();
      if (bs != null && bs.getBlock() != null && world.isRemote == false) {
        UtilItemStack.dropItemStackInWorld(world, mob.getPosition(), bs.getBlock());
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.mobs;
    config.addCustomCategoryComment(category, "Changes to vanilla mobs");
    nameTagDeath = config.getBoolean("Name Tag Death", category, true,
        "When an entity dies that is named with a tag, it drops the nametag");
    endermanDrop = config.getBoolean("Enderman Block", category, true,
        "Enderman will always drop block they are carrying 100%");
    endermanPickupBlocks = config.getBoolean("Enderman Pickup Blocker", category, true,
        "False is the same as vanilla behavior.  True means that this mod will block enderman from picking up all registered blocks (does not listen to mob actions, this scans registry only once on startup and sets properties).  ");
  }
  @Override
  public void onPostInit() {
    if (endermanPickupBlocks) {
      for (Block registeredBlock : Block.REGISTRY) {
        if (registeredBlock == null) {
          continue;
        }
        try {
          EntityEnderman.setCarriable(registeredBlock, false);
        }
        catch (Exception e) {
          ModCyclic.logger.error("MobChangesModule: error trying to disable enderman pickup ability on ", registeredBlock.getUnlocalizedName());
          e.printStackTrace();
        }
      }
    }
  }
}
