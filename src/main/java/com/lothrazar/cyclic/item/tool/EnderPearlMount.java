package com.lothrazar.cyclic.item.tool;

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
    //    ent.func_213884_b(itemstack);
    ent.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
    worldIn.addEntity(ent);
    playerIn.startRiding(ent, true);
    //    ent.riding
    playerIn.getCooldownTracker().setCooldown(stack.getItem(), 10);
    UtilItemStack.damageItem(stack);
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
}
