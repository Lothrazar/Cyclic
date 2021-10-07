package com.lothrazar.cyclic.compat.botania;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.ModList;
import vazkii.botania.api.BotaniaAPI;

public class BotaniaWrapper {

  public static boolean hasSolegnoliaAround(LivingEntity entity) {
    try {
      return ModList.get().isLoaded("botania") && BotaniaAPI.instance().hasSolegnoliaAround(entity);
    }
    catch (Exception e) {
      //ive never seen an exception here yet, just being safe
    }
    //either botania does not exist or an error was thrown
    return false;
  }
}
