package com.lothrazar.cyclic.item.enderpearl;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class EnderPearlReuse extends ItemBase {

  public EnderPearlReuse(Properties properties) {
    super(properties.maxDamage(256));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
    ItemStack stack = player.getHeldItem(handIn);
    EnderPearlEntity ent = new EnderPearlEntity(world, player);
    shootMe(world, player, ent);
    world.addEntity(ent);
    player.getCooldownTracker().setCooldown(stack.getItem(), 10);
    UtilItemStack.damageItem(player, stack);
    return super.onItemRightClick(world, player, handIn);
  }
}
