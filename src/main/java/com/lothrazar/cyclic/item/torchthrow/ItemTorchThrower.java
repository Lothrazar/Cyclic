package com.lothrazar.cyclic.item.torchthrow;

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
    super(properties.maxStackSize(1).maxDamage(256));
  }

  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    if (entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      tryRepairWith(stack, player, Blocks.TORCH.asItem());
    }
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity shooter, Hand hand) {
    shootMe(world, shooter, new EntityTorchBolt(shooter, world));
    shooter.getHeldItem(hand).damageItem(1, shooter, (p) -> {
      p.sendBreakAnimation(hand);
    });
    return super.onItemRightClick(world, shooter, hand);
  }
}
