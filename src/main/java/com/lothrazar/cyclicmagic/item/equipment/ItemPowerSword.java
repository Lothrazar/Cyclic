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
package com.lothrazar.cyclicmagic.item.equipment;

import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
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

public class ItemPowerSword extends ItemSword implements IHasRecipe, IHasConfig {

  private static final int COOLDOWN = Const.TICKS_PER_SEC;

  public enum SwordType {
    SLOW, WEAK, ENDER;
  }

  private SwordType type;
  private boolean enableShooting;

  public ItemPowerSword(SwordType t) {
    super(MaterialRegistry.powerToolMaterial);
    this.type = t;
    this.setMaxDamage(1);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World player, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    if (this.enableShooting) {
      tooltip.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
    }
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
        "ded",
        "ded",
        "wsw",
        'w', item,
        'e', Items.NETHER_STAR,
        'd', Items.DRAGON_BREATH,
        's', "blockEmerald");
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    if (enableShooting) {
      switch (type) {
        case WEAK:
          spawnLingeringPotion(player, PotionTypes.WEAKNESS);
        break;
        case SLOW:
          spawnLingeringPotion(player, PotionTypes.SLOWNESS);
        break;
        case ENDER:
          player.addPotionEffect(new PotionEffect(PotionEffectRegistry.ENDER, 200, 0));
          if (!world.isRemote) {
            EntityEnderPearl entityenderpearl = new EntityEnderPearl(world, player);
            // - 20
            entityenderpearl.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.6F, 1.0F);
            world.spawnEntity(entityenderpearl);
          }
      }
      UtilSound.playSound(player, SoundEvents.ENTITY_ENDERPEARL_THROW);
      player.getCooldownTracker().setCooldown(this, COOLDOWN);
    }
    return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
  }

  private void spawnLingeringPotion(EntityPlayer player, PotionType ptype) {
    World world = player.getEntityWorld();
    ItemStack potion = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), ptype);
    EntityPotion entitypotion = new EntityPotion(world, player, potion);
    //- 20
    entitypotion.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0, 1.6F, 0.5F);
    if (world.isRemote == false) {
      world.spawnEntity(entitypotion);
    }
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    //overrides to disable item damage
    return true;
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
    return true;
  }

  @Override
  public void syncConfig(Configuration config) {
    this.enableShooting = config.getBoolean(this.getUnlocalizedName().replace("item.", "") + "_projectiles", Const.ConfigCategory.modpackMisc, true, "Disable the projectile (splash potion / ender pearl) from this endgame sword");
  }
}
