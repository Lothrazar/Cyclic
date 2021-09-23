package com.lothrazar.cyclic.item.enderpearl;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class EnderPearlMount extends ItemBase {

  public EnderPearlMount(Properties properties) {
    super(properties.maxDamage(256));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    ItemStack stack = playerIn.getHeldItem(handIn);
    EnderPearlEntity ent = new EnderPearlEntity(worldIn, playerIn);
    shootMe(worldIn, playerIn, ent, 0, ItemBase.VELOCITY_MAX);
    playerIn.startRiding(ent, true);
    playerIn.getCooldownTracker().setCooldown(stack.getItem(), 10);
    UtilItemStack.damageItem(playerIn, stack);
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
}
