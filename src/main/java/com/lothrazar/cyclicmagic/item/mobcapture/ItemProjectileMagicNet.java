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
package com.lothrazar.cyclicmagic.item.mobcapture;

import java.util.List;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.entity.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.core.item.BaseItemProjectile;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilPlayer;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemProjectileMagicNet extends BaseItemProjectile implements IHasConfig, IHasRecipe {

  public static final String NBT_ENTITYID = "id";

  public ItemProjectileMagicNet() {
    super();
  }

  @Override
  public EntityThrowableDispensable getThrownEntity(World world, ItemStack held, double x, double y, double z) {
    if (hasEntity(held)) {
      ItemStack heldCopy = held.copy();
      held.getTagCompound().removeTag(NBT_ENTITYID);
      held.setTagCompound(null);
      return new EntityMagicNetFull(world, null, heldCopy);
    }
    else {
      return new EntityMagicNetEmpty(world, null);
    }
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 1),
        "lal",
        "qiq",
        "lal",
        'i', "ingotIron",
        'a', new ItemStack(Blocks.TALLGRASS, 1, OreDictionary.WILDCARD_VALUE),
        'l', "dyeCyan",
        'q', new ItemStack(Items.SNOWBALL));
  }

  public boolean hasEntity(ItemStack held) {
    return held.getTagCompound() != null && held.getTagCompound().hasKey(NBT_ENTITYID);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return hasEntity(stack);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    if (hasEffect(stack)) {
      tooltip.add(stack.getTagCompound().getString(NBT_ENTITYID));
    }
    else {
      super.addInformation(stack, playerIn, tooltip, advanced);
    }
  }

  @Override
  public void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    if (hasEntity(held)) {
      this.doThrow(world, player, hand, new EntityMagicNetFull(world, player, held.copy()));
      held.getTagCompound().removeTag(NBT_ENTITYID);
      held.setTagCompound(null);
    }
    else {
      this.doThrow(world, player, hand, new EntityMagicNetEmpty(world, player));
    }
    UtilPlayer.decrStackSize(player, hand);
  }

  @Override
  public SoundEvent getSound() {
    return SoundEvents.ENTITY_EGG_THROW;
  }

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc + ".magic_net";
    // @formatter:off
    String[] deflist = new String[] {
        "minecraft:wither"
        , "minecraft:ender_dragon"
        ,"minecraft:ender_crystal"
    };
    // @formatter:on
    String[] blacklist = config.getStringList("CaptureBlacklist",
        category, deflist, "Entities that cannot be captured.  (even without this, players and non-living entities do not work)");
    EntityMagicNetEmpty.blacklistIds = NonNullList.from("", blacklist);
  }
}
