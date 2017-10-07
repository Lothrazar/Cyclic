package com.lothrazar.cyclicmagic.util;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class UtilString {
  /**
   * One day i might make this a setting or an input arg for now i have no use
   * to turn it off
   */
  public static final boolean matchWildcard = true;
  /**
   * If the list has "hc:*_sapling" and input is "hc:whatever_sapling" then
   * match is true
   * 
   * @param list
   * @param toMatch
   * @return
   */
  public static boolean isInList(final List<String> list, ResourceLocation toMatch) {
    String id = toMatch.getResourceDomain().toString();
    for (String strFromList : list) {
      if (strFromList.equals(id)) {
        return true;
      }
      if (matchWildcard) {
        String modIdFromList = strFromList.split(":")[0];
        String blockIdFromList = strFromList.split(":")[1];//has the *
        String modIdToMatch = toMatch.getResourceDomain();
        String blockIdToMatch = toMatch.getResourcePath();
        if (modIdFromList.equals(modIdToMatch) == false) {
          continue;
        }
        String blockIdListWC = blockIdFromList.replace("*", "");
        if (blockIdToMatch.contains(blockIdListWC)) {
          return true;
        }
      }
    }
    return false;
  }
  /**
   * TODO: make a unit testing module, or install a framework for now these pass
   * so i removed call to them
   */
  public static void unitTests() {
    NonNullList<String> blacklist = NonNullList.from("",
        "terraqueous:pergola", "harvestcraft:*_sapling", "croparia:block_cane_*");
    ModCyclic.logger.logTestResult("1] expect true " + isInList(blacklist, new ResourceLocation("harvestcraft:fruit_sapling")));
    ModCyclic.logger.logTestResult("2] expect true " + isInList(blacklist, new ResourceLocation("croparia:block_cane_zzzzzz")));
    ModCyclic.logger.logTestResult("3] expect false " + isInList(blacklist, new ResourceLocation("harvestcraft:pampeach")));
    ModCyclic.logger.logTestResult("4] expect false " + isInList(blacklist, new ResourceLocation("harvestcraft:groundtrap")));
  }
}
