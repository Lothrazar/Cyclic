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
package com.lothrazar.cyclicmagic.item.slingshot;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseItemChargeScepter;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectileSlingshot extends BaseItemChargeScepter implements IHasRecipe {

  @GameRegistry.ObjectHolder(Const.MODRES + "slingshot_bullet")
  public static final Item bullet = null;
  public ItemProjectileSlingshot() {
    super(1);
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int chargeTimer) {
    if (entity instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer player = (EntityPlayer) entity;

    ItemStack itemstack = this.findAmmo(player);
    if (itemstack.isEmpty()) {
      UtilChat.sendStatusMessage(player, "slingshot.ammoempty");
      return; //EMPTY SOUND?> TODO? 
    }
    int charge = this.getMaxItemUseDuration(stack) - chargeTimer;
    float percentageCharged = ItemBow.getArrowVelocity(charge);//never zero, its from [0.03,1];
    float amountCharged = percentageCharged * MAX_CHARGE;
    float velocityFactor = percentageCharged * 1.5F;//flat upscale
    //between 0.3 and 5.1 roughly
    //UtilChat.sendStatusMessage(player, amountCharged + "");
    float damage = MathHelper.floor(amountCharged) / 2;//so its an even 3 or 2.5
    shootMain(world, player, velocityFactor, damage);
    itemstack.shrink(1);
    ActionType type = ActionType.getAction(stack);
    if (type == ActionType.DOUBLE || type == ActionType.TRIPLE) {
      //try and shoot one more
      if (!itemstack.isEmpty()) {
        shootMain(world, player, velocityFactor / 1.2F, damage);
        itemstack.shrink(1);
      }
    }
    if (type == ActionType.TRIPLE) {
      //try and shoot THIRD and final
      if (!itemstack.isEmpty()) {
        shootMain(world, player, velocityFactor / 1.5F, damage);
        itemstack.shrink(1);
      }
    }

  }

  protected boolean isAmmo(ItemStack stack) {
    return stack.getItem() == bullet;//TODO: oredict rock?
  }

  private ItemStack findAmmo(EntityPlayer player) {
    if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND))) {
      return player.getHeldItem(EnumHand.OFF_HAND);
    }
    else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND))) {
      return player.getHeldItem(EnumHand.MAIN_HAND);
    }
    else {
      for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
        ItemStack itemstack = player.inventory.getStackInSlot(i);
        if (this.isAmmo(itemstack)) {
          return itemstack;
        }
      }
      return ItemStack.EMPTY;
    }
  }

  @Override
  public EntitySlingshot createBullet(World world, EntityPlayer player, float dmg) {
    EntitySlingshot s = new EntitySlingshot(world, player);
    return s;
  }

  @Override
  public IRecipe addRecipe() {
    // TODO: bullet recipe

    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        " ss",
        "fbs",
        "ff ",
        's', Items.STRING,
        'b', new ItemStack(Items.STICK),
        'f', new ItemStack(Items.LEATHER));
  }

  @Override
  public SoundEvent getSound() {
    return SoundEvents.ENTITY_SNOWBALL_THROW;
  }

}
