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
package com.lothrazar.cyclicmagic.item.boomerang;

import java.util.List;
import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.item.core.BaseItemChargeScepter;
import com.lothrazar.cyclicmagic.registry.EntityProjectileRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBoomerang extends BaseItemChargeScepter implements IHasRecipe, IContent, IHasConfig {

  public ItemBoomerang() {
    super(256);
  }

  @Override
  public void register() {
    ItemRegistry.register(this, "boomerang");
    EntityProjectileRegistry.registerModEntity(EntityBoomerang.class, getContentName(), 1729);
  }

  private boolean enabled;
  public static boolean doesBreakBlocks;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public String getContentName() {
    return "boomerang";
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean(getContentName(), Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    doesBreakBlocks = config.getBoolean(getContentName() + ".doesBreakBlocks", Const.ConfigCategory.modpackMisc, false, "Does this break low-hardness blocks when thrown");
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int chargeTimer) {
    if (entity instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer player = (EntityPlayer) entity;
    int charge = this.getMaxItemUseDuration(stack) - chargeTimer;
    float percentageCharged = ItemBow.getArrowVelocity(charge);//never zero, its from [0.03,1];
    if (percentageCharged < 0.1) {
      return;//not enough force to go with any realistic path 
    }
    float amountCharged = percentageCharged * MAX_CHARGE;
    float velocityFactor = percentageCharged * 1.5F;//flat upscale
    float damage = MathHelper.floor(amountCharged) / 2;//so its an even 3 or 2.5
    EntityBoomerang projectile = (EntityBoomerang) shootMain(world, player, velocityFactor, damage);
    UtilItemStack.damageItem(player, stack);
    projectile.setBoomerangThrown(stack.copy());
    player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
  }

  @Override
  public EntityBoomerang createBullet(World world, EntityPlayer player, float dmg) {
    EntityBoomerang e = new EntityBoomerang(world, player);
    e.setOwner(player);
    return e;
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        " fd",
        "dgf",
        " fd",
        'd', Items.GLOWSTONE_DUST,
        'g', "ingotGold",
        'f', new ItemStack(Blocks.ACACIA_FENCE));
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World player, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltip.add(UtilChat.lang(getTooltip()));
    super.addInformation(stack, player, tooltip, advanced);
  }

  @Override
  public SoundEvent getSound() {
    //liquid_evaporate
    return SoundRegistry.step_height_up;
  }
}
