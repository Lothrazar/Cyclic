package com.lothrazar.cyclic.compat.curios;

import com.lothrazar.cyclic.compat.CompatConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosUtil {

  /**
   * First check curios. Then player inventory. Then left/right hands, not ender chest
   * 
   * @param player
   * @param match
   * @return
   */
  public static Triple<String, Integer, ItemStack> isCurioOrInventory(PlayerEntity player, Item match) {
    Triple<String, Integer, ItemStack> stackFound = Triple.of("", -1, ItemStack.EMPTY);
    if (ModList.get().isLoaded(CompatConstants.CURIOS)) {
      //check curios slots
      final ImmutableTriple<String, Integer, ItemStack> equipped = CuriosApi.getCuriosHelper().findEquippedCurio(match, player).orElse(null);
      if (equipped != null && isMatching(equipped.right, match)) {
        //success: try to insert items to network thru this remote 
        return Triple.of("curios", equipped.middle, equipped.right);
      }
    }
    //not curios, check others
    //    for (int i = 0; i < player.getInventoryEnderChest().getSizeInventory(); i++) {
    //      ItemStack temp = player.getInventoryEnderChest().getStackInSlot(i);
    //      if (isRemote(temp, remote)) {
    //        return Triple.of("ender", i, temp);
    //      }
    //    }
    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
      ItemStack temp = player.inventory.getStackInSlot(i);
      if (isMatching(temp, match)) {
        return Triple.of("player", i, temp);
      }
    }
    //default
    if (isMatching(player.getHeldItemOffhand(), match)) {
      return Triple.of("offhand", -1, player.getHeldItemOffhand());
    }
    if (isMatching(player.getHeldItemMainhand(), match)) {
      return Triple.of("hand", -1, player.getHeldItemMainhand());
    }
    return stackFound;
  }

  private static boolean isMatching(ItemStack current, Item match) {
    return current.getItem() == match;
  }
}
