package com.lothrazar.cyclic.entity;

import javax.annotation.Nonnull;
import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemTorchThrower extends ItemBase {

  public ItemTorchThrower(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    if (entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      tryRepairWith(stack, player, Blocks.TORCH.asItem());
    }
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
    if (!world.isRemote) {
      EntityTorchBolt ball = new EntityTorchBolt(player, world);
      ball.shoot(player, player.rotationPitch, player.rotationYaw, 0, 0.5F, 1);
      player.getHeldItem(hand).damageItem(1, player, (p) -> {
        p.sendBreakAnimation(hand);
      });
      world.addEntity(ball);
    }
    return super.onItemRightClick(world, player, hand);
  }
}
