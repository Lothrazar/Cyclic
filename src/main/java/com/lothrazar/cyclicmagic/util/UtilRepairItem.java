package com.lothrazar.cyclicmagic.util;

import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import java.util.Arrays;
import java.util.Objects;

public class UtilRepairItem {

  public static NonNullList<ResourceLocation> blacklistBlockIds;
  public static boolean doNonRepairable;

  public static boolean isBlockAllowed(ItemStack thing) {
    return UtilString.isInList(blacklistBlockIds, thing.getItem().getRegistryName()) == false;
  }

  public static void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc + ".block_anvil";
    // @formatter:off
    String[] deflist = new String[] {
        "galacticraftcore:battery"
        , "galacticraftcore:oxygen_tank_heavy_full"
        , "galacticraftcore:oxygen_tank_med_full"
        , "galacticraftcore:oil_canister_partial"
        , "galacticraftcore:oxygen_tank_light_full"
        ,"pneumaticcraft:*"
    };
    // @formatter:on
    String[] blacklist = config.getStringList("RepairBlacklist",
        category, deflist, "These cannot be repaired. Use star syntax to lock out an entire mod, otherwise use the standard modid:itemid for singles.  Applies to both diamond and magma anvil");
    UtilRepairItem.blacklistBlockIds = NonNullList.from(new ResourceLocation("", ""),
            Arrays.stream(blacklist).map(s -> {
              String[] split = s.split(":");
              if (split.length < 2) {
                ModCyclic.logger.error("Invalid RepairBlacklist config value for block : " + s);
                return null;
              }
              return new ResourceLocation(split[0], split[1]);
            }).filter(Objects::nonNull).filter(r -> !r.getPath().isEmpty()).toArray(ResourceLocation[]::new)
    );
    UtilRepairItem.doNonRepairable = config.getBoolean("ForceNonRepairable", category, false, "If this is set to true, this block will force-repair items that are set to be non-repairable (such as Tinkers Construct tools).  Of course it still respects the blacklist.  Applies to both diamond and magma anvil");
  }

  /**
   * if damaged, and repairable, then return true
   *
   * @param inputStack
   * @return
   */
  public static boolean canRepair(ItemStack inputStack) {
    if (inputStack.isEmpty()) {
      return false;
    }
    if (inputStack.isItemDamaged() == false) {
      //      ModCyclic.logger.info("not damaged ");
      return false;
    }
    if (UtilRepairItem.isBlockAllowed(inputStack) == false) {
      //      ModCyclic.logger.info("not allowed ");
      return false;//its either in blacklist, or its not even damaged
    }
    if (UtilRepairItem.doNonRepairable) {
      return true;//ignore what the item itself says below
    }
    //you can repair only if the item says its allowed to be repaired
    return inputStack.getItem().isRepairable();
  }

  /**
   * return false if it was already full repaired and no changes were made.
   *
   * return true if damage value changed from actual repair
   *
   * @param inputStack
   * @return
   */
  public static boolean doRepair(ItemStack inputStack) {
    if (inputStack.isItemDamaged()) {
      inputStack.setItemDamage(inputStack.getItemDamage() - 1);
      return true;
    }
    return false;
  }
}
