package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilEnchant {

  public static List<Effect> getNegativeEffects() {
    return getEffects(EffectType.HARMFUL);
  }

  public static List<Effect> getBeneficialEffects() {
    return getEffects(EffectType.BENEFICIAL);
  }

  public static List<Effect> getNeutralEffects() {
    return getEffects(EffectType.NEUTRAL);
  }

  public static List<Effect> getAllEffects() {
    return getEffects(null);
  }

  public static List<Effect> getEffects(@Nullable EffectType effectType) {
    Collection<Effect> effects = ForgeRegistries.POTIONS.getValues();
    List<Effect> effectsList = new ArrayList<>();
    for (Effect effect : effects) {
      if (effectType == null || effect.getEffectType() == effectType)
        effectsList.add(effect);
    }
    return effectsList;
  }
}
