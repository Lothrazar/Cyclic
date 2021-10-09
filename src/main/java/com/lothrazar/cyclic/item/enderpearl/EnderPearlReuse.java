package com.lothrazar.cyclic.item.enderpearl;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EnderPearlReuse extends ItemBase {

  public EnderPearlReuse(Properties properties) {
    super(properties.durability(256));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
    ItemStack stack = player.getItemInHand(handIn);
    ThrownEnderpearl ent = new ThrownEnderpearl(world, player);
    shootMe(world, player, ent, 0, ItemBase.VELOCITY_MAX);
    world.addFreshEntity(ent);
    player.getCooldowns().addCooldown(stack.getItem(), 10);
    UtilItemStack.damageItem(player, stack);
    return super.use(world, player, handIn);
  }
}
