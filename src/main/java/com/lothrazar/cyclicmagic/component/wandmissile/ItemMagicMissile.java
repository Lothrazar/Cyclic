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
import net.minecraft.util.math.Vec3d;
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
  private static final int SHOTSPERUSE = 5;
  private static final int durability = 500;
  private static final int COOLDOWN = 20;
  public ItemMagicMissile() {
    super(durability);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack held = player.getHeldItem(hand);
    UtilSound.playSound(player, SoundRegistry.magic_missile);//laserbeanpew);
    int x = player.getPosition().getX();
    int y = player.getPosition().getY();
    int z = player.getPosition().getZ();
    if (!world.isRemote) {
      for (int k = 0; k < SHOTSPERUSE; k++) {
        List<EntityLivingBase> targets = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - RANGE, y - RANGE, z - RANGE, x + RANGE, y + RANGE, z + RANGE));
        ArrayList<EntityLivingBase> trimmedTargets = new ArrayList<EntityLivingBase>();
        for (int i = 0; i < targets.size(); i++) {
          if (targets.get(i).getUniqueID().compareTo(player.getUniqueID()) != 0
              && targets.get(i).isCreatureType(EnumCreatureType.MONSTER, false)) {
            trimmedTargets.add(targets.get(i));
          }
        }
        if (trimmedTargets.size() == 0) {
          UtilChat.sendStatusMessage(player, "wand.result.notargets");
        }
        else {
          EntityHomingProjectile projectile = new EntityHomingProjectile(world);
          projectile.setPosition(x, y, z);
          projectile.motionX = 0;
          projectile.motionY = 0;
          projectile.motionZ = 0;
          projectile.rotationPitch = 0;
          projectile.rotationYaw = 0;
          projectile.onInitialSpawn(world.getDifficultyForLocation(projectile.getPosition()), null);
          projectile.initSpecial(trimmedTargets.get(world.rand.nextInt(trimmedTargets.size())), 6.0f, new Vec3d(76, 230, 0));
          world.spawnEntity(projectile);
        }
      }
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
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
}
