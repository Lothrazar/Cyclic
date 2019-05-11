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
package com.lothrazar.cyclicmagic.item.equipmentmagic;

import java.util.List;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.potion.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPoweredSword extends ItemSword implements IHasRecipe, IHasConfig {

  private static final int POTION_TICKS = 200;
  private static final int DMG_PER_SHOT = 64;
  private static final int MAX_DURAB = 25565;
  private static final int COOLDOWN = Const.TICKS_PER_SEC;

  public enum SwordType {
    SLOW, WEAK, ENDER;
  }

  private SwordType type;

  public ItemPoweredSword(SwordType t) {
    super(MaterialRegistry.crystalToolMaterial);
    this.type = t;
    this.setMaxDamage(MAX_DURAB);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World player, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltip.add(UtilChat.lang(this.getTranslationKey() + ".tooltip"));
    super.addInformation(stack, player, tooltip, advanced);
  }

  @Override
  public IRecipe addRecipe() {
    ItemStack item = ItemStack.EMPTY;
    switch (type) {
      case WEAK:
        item = new ItemStack(Blocks.MYCELIUM);
      break;
      case SLOW:
        item = new ItemStack(Blocks.PACKED_ICE);
      break;
      case ENDER:
        item = new ItemStack(Items.CHORUS_FRUIT);
      break;
    }
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "dsd",
        "xsx",
        "waw",
        'w', item,
        'x', "blockDiamond",
        'd', Items.DRAGON_BREATH,
        's', "gemObsidian",
        'a', "gemAmber");
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    switch (type) {
      case WEAK:
        spawnLingeringPotion(player, PotionTypes.WEAKNESS);
      break;
      case SLOW:
        spawnLingeringPotion(player, PotionTypes.SLOWNESS);
      break;
      case ENDER:
        spawnEnderPearl(world, player);
      break;
    }
    UtilSound.playSound(player, SoundEvents.ENTITY_ENDERPEARL_THROW);
    UtilEntity.setCooldownItem(player, this, COOLDOWN);
    UtilItemStack.damageItem(player, player.getHeldItem(hand), DMG_PER_SHOT);
    return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
  }

  private void spawnEnderPearl(World world, EntityPlayer player) {
    player.addPotionEffect(new PotionEffect(PotionEffectRegistry.ENDER, POTION_TICKS, 0));
    if (!world.isRemote) {
      EntityEnderPearl entityenderpearl = new EntityEnderPearl(world, player);
      // - 20
      entityenderpearl.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.6F, 1.0F);
      world.spawnEntity(entityenderpearl);
    }
  }

  private void spawnLingeringPotion(EntityPlayer player, PotionType ptype) {
    World world = player.getEntityWorld();
    ItemStack potion = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), ptype);
    EntityPotion entitypotion = new EntityPotion(world, player, potion);
    //- 20
    entitypotion.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1.6F, 0.5F);
    if (world.isRemote == false) {
      world.spawnEntity(entitypotion);
    }
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
    //overrides to disable item damage from mining
    return true;
  }

  @Override
  public void syncConfig(Configuration config) {}
}
