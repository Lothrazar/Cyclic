package com.lothrazar.cyclic.item.ender;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.ItemStackUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EnderPearlReuse extends ItemBaseCyclic {

  public EnderPearlReuse(Properties properties) {
    super(properties.durability(256));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
    ItemStack stack = player.getItemInHand(handIn);
    ThrownEnderpearl ent = new ThrownEnderpearl(world, player);
    shootMe(world, player, ent, 0, ItemBaseCyclic.VELOCITY_MAX);
    world.addFreshEntity(ent);
    player.getCooldowns().addCooldown(stack.getItem(), 10);
    ItemStackUtil.damageItem(player, stack);
    return super.use(world, player, handIn);
  }
}
