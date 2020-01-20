package com.lothrazar.cyclic.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;

public class ItemMobContainer extends Item {

  public ItemMobContainer(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    World world = context.getWorld();
    PlayerEntity player = context.getPlayer();
    ItemStack stack = player.getHeldItem(context.getHand());
    System.out.println(stack.getTag());
    return ActionResultType.PASS;
  }
}
