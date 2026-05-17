package com.lothrazar.cyclic.compat.botania;

import net.minecraft.world.entity.LivingEntity;

// TODO: re-enable when its released
// see extra.gradle
public class BotaniaWrapper {

  /**
   * If the botania mod is loaded, use the botania-api to check for hasSolegnoliaAround(entity); otherwise return false
   *
   * @param entity
   * @return true if botania has found a solegnolia around the entity
   */
  public static boolean hasSolegnoliaAround(LivingEntity entity) {
//    try {
//      if (ModList.get().isLoaded("botania")) {
//        return vazkii.botania.api.BotaniaAPI.instance().hasSolegnoliaAround(entity);
//      }
//    }
//    catch (Exception e) {}
    return false; // its never around if the mod doesnt exist
  }
}

