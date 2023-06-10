package com.lothrazar.cyclic.util;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemStackUtil {

  public static int countEmptySlots(IItemHandler handler) {
    if (handler == null) {
      return 0;
    }
    int empty = 0;
    for (int i = 0; i < handler.getSlots(); i++) {
      if (handler.getStackInSlot(i).isEmpty()) {
        empty++;
      }
    }
    return empty;
  }

  public static ItemStack findItem(String id) {
    Item head = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(id));
    if (head != null) {
      return new ItemStack(head);
    }
    return ItemStack.EMPTY;
  }
  //  public static void dropAll(IItemHandler items, Level world, BlockPos pos) {
  //    if (items == null) {
  //      return;
  //    }
  //    for (int i = 0; i < items.getSlots(); i++) {
  //      Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i));
  //    }
  //  }

  public static void repairItem(ItemStack s) {
    repairItem(s, 1);
  }

  public static void repairItem(ItemStack s, int amount) {
    s.setDamageValue(Math.max(0, s.getDamageValue() - amount));
  }

  public static void damageItem(LivingEntity player, ItemStack stack) {
    if (!stack.isDamageableItem()) {
      //unbreakable
      return;
    }
    if (player == null) {
      stack.setDamageValue(stack.getDamageValue() + 1);
    }
    else {
      stack.hurtAndBreak(1, player, (p) -> {
        p.broadcastBreakEvent(InteractionHand.MAIN_HAND);
      });
    }
    if (stack.getDamageValue() >= stack.getMaxDamage()) {
      stack.shrink(1);
      stack = ItemStack.EMPTY;
    }
  }

  public static void damageItemRandomly(LivingEntity player, ItemStack stack) {
    if (player.level.random.nextDouble() < 0.001) {
      damageItem(player, stack);
    }
  }

  public static void drop(Level world, BlockPos pos, Block drop) {
    drop(world, pos, new ItemStack(drop.asItem()));
  }

  public static void drop(Level world, BlockPos pos, ItemStack drop) {
    if (!world.isClientSide) {
      world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drop));
    }
  }

  public static boolean matches(ItemStack current, ItemStack in) {
    //first one fails if size is off
    return ItemStack.isSameIgnoreDurability(current, in)
        && ItemStack.tagMatches(current, in);
  }

  public static void shrink(Player player, ItemStack stac) {
    if (!player.isCreative()) {
      stac.shrink(1);
    }
  }

  public static void drop(Level world, BlockPos center, List<ItemStack> lootDrops) {
    for (ItemStack dropMe : lootDrops) {
      ItemStackUtil.drop(world, center, dropMe);
    }
  }

  public static void dropItemStackMotionless(Level world, BlockPos pos, ItemStack stack) {
    if (stack.isEmpty()) {
      return;
    }
    if (world.isClientSide == false) {
      ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
      // do not spawn a second 'ghost' one onclient side
      world.addFreshEntity(entityItem);
      entityItem.setDeltaMovement(0, 0, 0);
      //      entityItem.motionX = entityItem.motionY = entityItem.motionZ = 0;
    }
  }

  /**
   * Preserve damage but delete the rest of the tag
   *
   * @param itemstack
   */
  public static void deleteTag(ItemStack itemstack) {
    int dmg = itemstack.getDamageValue();
    itemstack.setTag(null);
    itemstack.setDamageValue(dmg);
  }
}
