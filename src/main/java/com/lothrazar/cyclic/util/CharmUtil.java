package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.base.IHasClickToggle;
import com.lothrazar.cyclic.compat.CompatConstants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import top.theillusivec4.curios.api.CuriosApi;

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

  /**
   * First check curios. Then player inventory. Then left/right hands, not ender chest
   *
   * @param player
   * @param match
   * @return
   */
  public static Triple<String, Integer, ItemStack> isCurioOrInventory(Player player, Item match) {
    Triple<String, Integer, ItemStack> stackFound = Triple.of("", -1, ItemStack.EMPTY);
    if (ModList.get().isLoaded(CompatConstants.CURIOS)) {
      //check curios slots IF mod is loaded
      try {
        final ImmutableTriple<String, Integer, ItemStack> equipped = CuriosApi.getCuriosHelper().findEquippedCurio(match, player).orElse(null);
        if (equipped != null && isMatching(equipped.right, match)) {
          ItemStack found = equipped.right;
          if (found.getItem() instanceof IHasClickToggle) {
            IHasClickToggle testMe = (IHasClickToggle) found.getItem();
            if (testMe.isOn(found)) {
              return Triple.of("curios", equipped.middle, equipped.right);
            }
            //else its found but turned off , keep looking
          }
        }
      }
      catch (Exception e) {
        // if API not installed or fails 
      }
    }
    //is "baubles" in 1.16? 
    //not curios, check others
    //    for (int i = 0; i < player.getInventoryEnderChest().getSizeInventory(); i++) {
    //      ItemStack temp = player.getInventoryEnderChest().getStackInSlot(i);
    //      if (isRemote(temp, remote)) {
    //        return Triple.of("ender", i, temp);
    //      }
    //    }
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
