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
package com.lothrazar.cyclicmagic.item.magic;

import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseTool;
import com.lothrazar.cyclicmagic.core.item.IHasClickToggle;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilEntity;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEnderWing extends BaseTool implements IHasRecipe, IHasClickToggle {

  private static final int cooldown = 600;//ticks not seconds
  private static final int durability = 16;

  public static enum WarpType {
    BED, SPAWN
  }

  private WarpType warpType;

  public ItemEnderWing(WarpType type) {
    super(durability);
    warpType = type;
  }

  @Override
  public void toggle(EntityPlayer player, ItemStack held) {
    tryActivate(player, held);
  }

  private boolean tryActivate(EntityPlayer player, ItemStack held) {
    if (player.getCooldownTracker().hasCooldown(this)) {
      return false;
    }
    World world = player.getEntityWorld();
    if (player.dimension != 0) {
      UtilChat.addChatMessage(player, "command.worldhome.dim");
      return false;
    }
    //boolean success = false;
    BlockPos target = null;
    switch (warpType) {
      case BED:
        target = player.getBedLocation(0);
        // success = UtilWorld.tryTpPlayerToBed(world, player);
        if (target == null) {
          UtilChat.addChatMessage(player, "command.gethome.bed");
          return false;
        }
      break;
      case SPAWN:
        target = world.getSpawnPoint();
      //UtilEntity.teleportWallSafe(player, world, world.getSpawnPoint());
      //success = true;
      break;
    }
    if (target == null) {
      return false;
    }
    boolean success = UtilEntity.enderTeleportEvent(player, world, target);
    if (success) {
      UtilItemStack.damageItem(player, held);
      UtilSound.playSound(player, SoundRegistry.warp);
      player.getCooldownTracker().setCooldown(this, cooldown);
      if (world.isRemote == false) {
        //and for other players on arrival
        UtilSound.playSoundFromServer(SoundRegistry.warp, SoundCategory.PLAYERS, target, player.dimension, 32);
      }
    }
    return success;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (tryActivate(player, stack)) {
      super.onUse(stack, player, world, hand);
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
    return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    switch (warpType) {
      case BED:
        tooltip.add(UtilChat.lang("item.tool_warp_home.tooltip"));
      break;
      case SPAWN:
        tooltip.add(UtilChat.lang("item.tool_warp_spawn.tooltip"));
      break;
      default:
      break;
    }
  }

  @Override
  public IRecipe addRecipe() {
    switch (warpType) {
      case BED:
        //goes to your BED (which can be anywhere)
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            " ft",
            "ggf",
            "dg ",
            't', "blockQuartz",
            'f', "feather",
            'g', "ingotGold",
            'd', new ItemStack(Items.ENDER_EYE));
      case SPAWN:
        //this one needs diamond but is cheaper. goes to worldspawn
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            " ff",
            "ggf",
            "dg ",
            'f', "feather",
            'g', "nuggetGold",
            'd', "gemDiamond");
      default:
        return null;
    }
  }

  @Override
  public boolean isOn(ItemStack held) {
    return true;
  }
}
