package com.lothrazar.cyclic.util;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class UtilItemStack {

  public static void dropAll(IItemHandler items, World world, BlockPos pos) {
    if (items == null) {
      return;
    }
    for (int i = 0; i < items.getSlots(); i++) {
      InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i));
    }
  }

  public static void repairItem(ItemStack s) {
    repairItem(s, 1);
  }

  public static void repairItem(ItemStack s, int amount) {
    s.setDamage(Math.max(0, s.getDamage() - amount));
  }

  public static void damageItem(ItemStack s) {
    s.setDamage(s.getDamage() + 1);
    if (s.getDamage() >= s.getMaxDamage()) {
      s.shrink(1);
    }
  }

  public static void damageItem(LivingEntity player, ItemStack stack) {
    stack.damageItem(1, player, (p) -> {
      p.sendBreakAnimation(Hand.MAIN_HAND);
    });
    if (stack.getDamage() >= stack.getMaxDamage()) {
      stack.setCount(0);
      stack = ItemStack.EMPTY;
    }
  }

  public static void damageItemRandomly(LivingEntity player, ItemStack stack) {
    if (player.world.rand.nextDouble() < 0.001) {
      damageItem(player, stack);
    }
  }

  public static void drop(World world, BlockPos pos, Block drop) {
    if (!world.isRemote) {
      world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(drop.asItem())));
    }
  }

  public static void drop(World world, BlockPos pos, ItemStack drop) {
    if (!world.isRemote) {
      world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drop));
    }
  }

  public static boolean matches(ItemStack current, ItemStack in) {
    //first one fails if size is off
    return ItemStack.areItemsEqualIgnoreDurability(current, in)
        && ItemStack.areItemStackTagsEqual(current, in);
  }

  public static void shrink(PlayerEntity player, ItemStack stac) {
    if (!player.isCreative()) {
      stac.shrink(1);
    }
  }

  public static void drop(World world, BlockPos center, List<ItemStack> lootDrops) {
    for (ItemStack dropMe : lootDrops) {
      UtilItemStack.drop(world, center, dropMe);
    }
  }

  public static void dropItemStackMotionless(World world, BlockPos pos, ItemStack stack) {
    if (stack.isEmpty()) {
      return;
    }
    if (world.isRemote == false) {
      ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
      // do not spawn a second 'ghost' one onclient side
      world.addEntity(entityItem);
      entityItem.setMotion(0, 0, 0);
      //      entityItem.motionX = entityItem.motionY = entityItem.motionZ = 0;
    }
  }
}
