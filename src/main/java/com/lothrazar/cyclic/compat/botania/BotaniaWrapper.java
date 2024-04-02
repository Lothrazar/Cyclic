package com.lothrazar.cyclic.compat.botania;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.fml.ModList;

public class BotaniaWrapper {

  public static boolean hasSolegnoliaAround(LivingEntity entity) {
    try {
      return ModList.get().isLoaded("botania") && vazkii.botania.api.BotaniaAPI.instance().hasSolegnoliaAround(entity);
    }
    catch (Exception e) {
      //ive never seen an exception here yet, just being safe
    }
    //either botania does not exist or an error was thrown
    return false;
  }
}
