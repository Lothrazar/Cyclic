package com.lothrazar.cyclic.util;

import org.apache.commons.lang3.tuple.Triple;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.library.core.IHasClickToggle;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class CharmUtil {

  public static ItemStack getIfEnabled(Player player, Item match) {
    Triple<String, Integer, ItemStack> found = isCurioOrInventory(player, match);
    ItemStack stack = found == null ? ItemStack.EMPTY : found.getRight();
    if (stack.getItem() instanceof IHasClickToggle) {
      IHasClickToggle testMe = (IHasClickToggle) stack.getItem();
      if (testMe.isOn(stack) == false) {
        return ItemStack.EMPTY; // found but player turned it off so dont use it
      }
    }
    return stack;
  }

  public static ItemStack getCurio(Player player, Item match) {
    if (ModList.get().isLoaded(CompatConstants.CURIOS)) {
      //check curios slots IF mod is loaded
      try {
        //        final ImmutableTriple<String, Integer, ItemStack> equipped = CuriosApi.getCuriosHelper().findEquippedCurio(match, player).orElse(null);
        SlotResult first = CuriosApi.getCuriosHelper().findFirstCurio(player, match).orElse(null);
        if (first != null && isMatching(first.stack(), match)) {
          return first.stack();
        }
      }
      catch (Exception e) {
        // if API not installed or fails 
      }
    }
    return ItemStack.EMPTY;
  }

  /**
   * First check curios. Then player inventory. Then left/right hands, not ender chest
   *
   * @param player
   * @param match
   * @return
   */
  private static Triple<String, Integer, ItemStack> isCurioOrInventory(Player player, Item match) {
    Triple<String, Integer, ItemStack> stackFound = Triple.of("", -1, ItemStack.EMPTY);
    if (ModList.get().isLoaded(CompatConstants.CURIOS)) {
      //check curios slots IF mod is loaded
      try {
        SlotResult first = CuriosApi.getCuriosHelper().findFirstCurio(player, match).orElse(null);
        if (first != null && isMatching(first.stack(), match)) {
          ItemStack found = first.stack();
          if (found.getItem() instanceof IHasClickToggle) {
            IHasClickToggle testMe = (IHasClickToggle) found.getItem();
            if (testMe.isOn(found)) {
              return Triple.of(CompatConstants.CURIOS, first.slotContext().index(), first.stack());
            }
            //else its found but turned off , keep looking
          }
        }
      }
      catch (Exception e) {
        // if API not installed or fails
      }
    }
    // TODO: does the "baubles" mod still exist
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
    //default
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
