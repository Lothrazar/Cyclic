package com.lothrazar.cyclic.entity;

import javax.annotation.Nonnull;
import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemMagicNet extends ItemBase {

  public ItemMagicNet(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
    if (!world.isRemote) {
      EntityMagicNetEmpty ball = new EntityMagicNetEmpty(player, world);
      ball.shoot(player, player.rotationPitch, player.rotationYaw, 0, 0.5F, 1);
      player.getHeldItem(hand).shrink(1);
      world.addEntity(ball);
    }
    return super.onItemRightClick(world, player, hand);
  }
}
