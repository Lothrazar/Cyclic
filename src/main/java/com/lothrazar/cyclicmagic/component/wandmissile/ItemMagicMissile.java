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
package com.lothrazar.cyclicmagic.component.wandmissile;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * model inspired by https://twitter.com/CaptainQ/status/903823531124736000
 * 
 * @author Sam
 *
 */
public class ItemMagicMissile extends BaseTool implements IHasRecipe {

  private static final double RANGE = 16.0;
  private static final int durability = 500;
  private static final int COOLDOWN = 8;

  public ItemMagicMissile() {
    super(durability);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack held = player.getHeldItem(hand);
    UtilSound.playSound(player, SoundRegistry.spirit_seeker);
    int x = player.getPosition().getX();
    int y = player.getPosition().getY();
    int z = player.getPosition().getZ();
    List<EntityLivingBase> targets = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - RANGE, y - RANGE, z - RANGE, x + RANGE, y + RANGE, z + RANGE));
    List<EntityLivingBase> trimmedTargets = new ArrayList<>();
    for (EntityLivingBase potential : targets) {
      if (potential.isDead == false
          && potential.getUniqueID().compareTo(player.getUniqueID()) != 0
          && potential.isCreatureType(EnumCreatureType.MONSTER, false)) {
        trimmedTargets.add(potential);
      }
    }
    if (trimmedTargets.size() == 0) {
      UtilChat.sendStatusMessage(player, "wand.result.notargets");
    }
    else {
      //closest actual monster
      EntityLivingBase target = UtilEntity.getClosestEntity(world, player, trimmedTargets);
      EntityHomingProjectile projectile = new EntityHomingProjectile(world, player);
      projectile.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0, 0.5F, 1);
      projectile.setTarget(target);
      world.spawnEntity(projectile);
    }
    player.getCooldownTracker().setCooldown(held.getItem(), COOLDOWN);
    super.onUse(held, player, world, hand);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, held);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "dcd",
        " b ",
        "rbr",
        'd', "gemDiamond",
        'c', new ItemStack(Items.GHAST_TEAR),
        'r', "ingotGold",
        'b', "ingotIron");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
}
