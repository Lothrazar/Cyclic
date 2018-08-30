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
package com.lothrazar.cyclicmagic.item.cannon;

import java.util.List;
import com.lothrazar.cyclicmagic.capability.EnergyCapabilityItemStack;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.item.cannon.EntityGolemLaser.VariantColors;
import com.lothrazar.cyclicmagic.item.core.BaseItem;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemProjectileCannon extends BaseItem implements IHasRecipe {

  private static final int ENERGY_MAX = 32 * 1000;
  public static final int ENERGY_COST = 100;

  public ItemProjectileCannon() {
    super();
  }

  public void createBullet(World world, EntityPlayer player) {
    EntityGolemLaser bullet = new EntityGolemLaser(world);
    int colorIndex = MathHelper.getInt(world.rand, 0, VariantColors.values().length - 1);
    bullet.getDataManager().set(EntityGolemLaser.variant, colorIndex);
    float speed = 4.0F;
    bullet.initCustom(player.posX, player.posY + 1.52, player.posZ,
        player.getLookVec().x * 0.5, player.getLookVec().y * 0.5, player.getLookVec().z * 0.5,
        speed, player.getUniqueID());
    // if (world.isRemote == false)
    world.spawnEntity(bullet);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        " ct",
        "bbc",
        "qb ",
        't', Items.SHULKER_SHELL,
        'c', Items.FIRE_CHARGE,
        'b', new ItemStack(Items.DIAMOND),
        'q', new ItemStack(Blocks.PRISMARINE, 1, BlockPrismarine.DARK_META));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    IEnergyStorage storage = player.getHeldItem(hand).getCapability(CapabilityEnergy.ENERGY, null);
    //HAS ENOUGH ENERGY? 
    if (storage.getEnergyStored() < ENERGY_COST
        && ENERGY_COST > storage.extractEnergy(ENERGY_COST, true)) {
      UtilChat.sendStatusMessage(player, "cyclic.item.empty");
      return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
    }
    this.createBullet(world, player);
    UtilSound.playSound(player, player.getPosition(), SoundRegistry.fireball_staff_launch, SoundCategory.PLAYERS, 0.1F);
    player.swingArm(hand);
    //DRAIN ENERGY 
    storage.extractEnergy(ENERGY_COST, false);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
  }

  //energy shiz 
  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    return true;
  }

  @Override
  public int getRGBDurabilityForDisplay(ItemStack stack) {
    return 0xBC000C;
  }

  @Override
  public double getDurabilityForDisplay(ItemStack item) {
    IEnergyStorage storage = item.getCapability(CapabilityEnergy.ENERGY, null);
    double energy = storage.getEnergyStored();
    return 1 - energy / storage.getMaxEnergyStored();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack item, World player, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    IEnergyStorage storage = item.getCapability(CapabilityEnergy.ENERGY, null);
    tooltip.add(storage.getEnergyStored() + "/" + storage.getMaxEnergyStored());
    super.addInformation(item, player, tooltip, advanced);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
    return new EnergyCapabilityItemStack(stack, ENERGY_MAX);
  }
}
