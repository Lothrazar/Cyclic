package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.potion.PotionBase;
import com.lothrazar.cyclicmagic.potion.PotionEnder;
import com.lothrazar.cyclicmagic.potion.PotionMagnet;
import com.lothrazar.cyclicmagic.potion.PotionSlowfall;
import com.lothrazar.cyclicmagic.potion.PotionSnow;
import com.lothrazar.cyclicmagic.potion.PotionWaterwalk;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PotionEffectRegistry {
  public static enum PotionType {
    NORMAL, POWERED, LONG//, SPLASH, LINGER // todo: these last two
  }
  public static final PotionBase slowfallEffect = new PotionSlowfall("slowfall", true, 0);;
  public static final PotionBase magnetEffect = new PotionMagnet("magnet", true, 0);;
  public static final PotionBase enderEffect = new PotionEnder("ender", true, 0);;
  public static final PotionBase waterwalkEffect = new PotionWaterwalk("waterwalk", true, 0);;
  public static final PotionBase snowEffect = new PotionSnow("snow", true, 0);;
  public static ArrayList<PotionBase> potionEffects = new ArrayList<PotionBase>();
  public static void register() {
    //onInit
    PotionEffectRegistry.registerPotionEffect(magnetEffect);
    PotionEffectRegistry.registerPotionEffect(enderEffect);
    PotionEffectRegistry.registerPotionEffect(waterwalkEffect);
    PotionEffectRegistry.registerPotionEffect(slowfallEffect);
    PotionEffectRegistry.registerPotionEffect(snowEffect);
  }
  public static void registerPotionEffect(PotionBase effect) {
    GameRegistry.register(effect, effect.getIcon());
    potionEffects.add(effect);
  }
  public static String getStrForLevel(int lvl) {
    //TODO: probs a better roman numeral way\
    //not found in    PotionHelper.MixPredicate<T>
    switch (lvl) {
      case 0:
        return "I";
      case 1:
        return "II";
      case 2:
        return "III";
      case 3:
        return "IV";
      case 4:
        return "V";
    }
    return "";
  }
}
