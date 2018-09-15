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
package com.lothrazar.cyclicmagic.item.equipbauble;

import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.item.core.BaseCharm;
import com.lothrazar.cyclicmagic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class ItemCharmAir extends BaseCharm implements IHasRecipe, IContent {

  private static final double DOWNWARD_SPEED_SNEAKING = -0.32;
  private static final int TICKS_FALLDIST_SYNC = 22;//tick every so often
  private static final int durability = 4096;

  public ItemCharmAir() {
    super(durability);
  }

  @Override
  public void register() {
    ItemRegistry.register(this, "charm_air", GuideCategory.ITEMBAUBLES);
    ModCyclic.instance.events.register(this);
    LootTableRegistry.registerLoot(this);
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean("AirCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }

  @Override
  public void onTick(ItemStack stack, EntityPlayer player) {
    if (!this.canTick(stack)) {
      return;
    }
    World world = player.getEntityWorld();
    BlockPos belowMe = player.getPosition().down();
    boolean isAirBorne = (world.isAirBlock(belowMe) //sneak on air, or a nonsolid block like a flower
        || world.isSideSolid(belowMe, EnumFacing.UP) == false);
    //do not use  player.isAirBorne, its only true on clientside, and that doesnt let us deal charm damage.. among possible other issues
    if (isAirBorne && player.motionY < 0) {//player.isSneaking() &&
      player.motionY = (player.isSneaking()) ? DOWNWARD_SPEED_SNEAKING : 0;
      player.isAirBorne = false;
      //if we set onGround->true all the time, it blocks fwd movement anywya
      player.onGround = true;// (player.motionX == 0 && player.motionZ == 0); //allow jump only if not walking
      if (player.getEntityWorld().rand.nextDouble() < 0.1) {
        super.damageCharm(player, stack);
      }
      if (world.isRemote && //setting fall distance on clientside wont work
          player instanceof EntityPlayer && player.ticksExisted % TICKS_FALLDIST_SYNC == 0) {
        ModCyclic.network.sendToServer(new PacketPlayerFalldamage());
      }
    }
  }

  @Override
  public IRecipe addRecipe() {
    return super.addRecipeAndRepair(new ItemStack(Blocks.BONE_BLOCK));
  }
}
