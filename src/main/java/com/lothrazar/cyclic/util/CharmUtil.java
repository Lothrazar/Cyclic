package com.lothrazar.cyclic.util;

import org.apache.commons.lang3.tuple.Triple;
import com.lothrazar.library.core.IHasClickToggle;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CharmUtil {

  public static ItemStack getIfEnabled(Player player, Item match) {
    Triple<String, Integer, ItemStack> found = isCurioOrInventory(player, match);
    ItemStack stack = found == null ? ItemStack.EMPTY : found.getRight();
    if (stack.getItem() instanceof IHasClickToggle) {
      IHasClickToggle testMe = (IHasClickToggle) stack.getItem();
      if (testMe.isOn(stack) == false) {
        return ItemStack.EMPTY;
      }
    }
    return stack;
  }

  public static ItemStack getCurio(Player player, Item match) {
    // TODO: re-enable Curios integration when ported
    return ItemStack.EMPTY;
  }

  /**
   * First check player inventory. Then left/right hands.
   */
  private static Triple<String, Integer, ItemStack> isCurioOrInventory(Player player, Item match) {
    Triple<String, Integer, ItemStack> stackFound = Triple.of("", -1, ItemStack.EMPTY);
    // TODO: re-enable Curios integration when ported
    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
      ItemStack temp = player.getInventory().getItem(i);
      if (isMatching(temp, match)) {
        if (temp.getItem() instanceof IHasClickToggle) {
          IHasClickToggle testMe = (IHasClickToggle) temp.getItem();
          if (testMe.isOn(temp)) {
            return Triple.of("player", i, temp);
          }
        }
      }
    }
    if (isMatching(player.getOffhandItem(), match)) {
      return Triple.of("offhand", -1, player.getOffhandItem());
    }
    if (isMatching(player.getMainHandItem(), match)) {
      return Triple.of("hand", -1, player.getMainHandItem());
    }
    return stackFound;
  }

  public static boolean isMatching(ItemStack current, Item match) {
    return current.getItem() == match;
  }
}
