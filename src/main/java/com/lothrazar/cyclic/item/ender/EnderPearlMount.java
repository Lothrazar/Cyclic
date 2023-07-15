package com.lothrazar.cyclic.item.ender;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EnderPearlMount extends ItemBaseCyclic {

  public EnderPearlMount(Properties properties) {
    super(properties.durability(256));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack stack = playerIn.getItemInHand(handIn);
    ThrownEnderpearl ent = new ThrownEnderpearl(worldIn, playerIn);
    shootMe(worldIn, playerIn, ent, 0, ItemBaseCyclic.VELOCITY_MAX);
    playerIn.startRiding(ent, true);
    playerIn.getCooldowns().addCooldown(stack.getItem(), 10);
    ItemStackUtil.damageItem(playerIn, stack);
    return super.use(worldIn, playerIn, handIn);
  }
}
