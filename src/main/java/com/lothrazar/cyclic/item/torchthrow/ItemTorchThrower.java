package com.lothrazar.cyclic.item.torchthrow;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.ItemStackUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ItemTorchThrower extends ItemBaseCyclic {

  public ItemTorchThrower(Properties properties) {
    super(properties.stacksTo(1).durability(256));
  }

  @Override
  public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
    if (entity instanceof Player) {
      Player player = (Player) entity;
      tryRepairWith(stack, player, Blocks.TORCH.asItem());
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player shooter, InteractionHand hand) {
    shootMe(world, shooter, new EntityTorchBolt(shooter, world), 0, ItemBaseCyclic.VELOCITY_MAX);
    ItemStackUtil.damageItem(shooter, shooter.getItemInHand(hand));
    return super.use(world, shooter, hand);
  }
}
