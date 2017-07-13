package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.potion.PotionBase;
import com.lothrazar.cyclicmagic.potion.PotionBounce;
import com.lothrazar.cyclicmagic.potion.PotionEnder;
import com.lothrazar.cyclicmagic.potion.PotionMagnet;
import com.lothrazar.cyclicmagic.potion.PotionSlowfall;
import com.lothrazar.cyclicmagic.potion.PotionSnow;
import com.lothrazar.cyclicmagic.potion.PotionSwimSpeed;
import com.lothrazar.cyclicmagic.potion.PotionWaterwalk;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionEffectRegistry {
  public static ArrayList<Potion> potions = new ArrayList<Potion>();
  public static enum PotionType {
    NORMAL, POWERED, LONG//, SPLASH, LINGER // todo: these last two
  }
  public static final PotionBase SLOWFALL = new PotionSlowfall("slowfall", true, 0);
  public static final PotionBase MAGNET = new PotionMagnet("magnet", true, 0);
  public static final PotionBase ENDER = new PotionEnder("ender", true, 0);
  public static final PotionBase WATERWALK = new PotionWaterwalk("waterwalk", true, 0);
  public static final PotionBase SNOW = new PotionSnow("snow", true, 0);
  public static final PotionBase SWIMSPEED = new PotionSwimSpeed("swimspeed", true, 0);
  public static final PotionBase BOUNCE = new PotionBounce("bounce", true, 0);
  public static ArrayList<PotionBase> potionEffects = new ArrayList<PotionBase>();
  public static void register() {
    PotionEffectRegistry.registerPotionEffect(MAGNET);
    PotionEffectRegistry.registerPotionEffect(ENDER);
    PotionEffectRegistry.registerPotionEffect(WATERWALK);
    PotionEffectRegistry.registerPotionEffect(SLOWFALL);
    PotionEffectRegistry.registerPotionEffect(SNOW);
    PotionEffectRegistry.registerPotionEffect(SWIMSPEED);
    PotionEffectRegistry.registerPotionEffect(BOUNCE);
  }
  public static void registerPotionEffect(PotionBase effect) {
    effect.setIcon(effect.getIcon());
    potions.add(effect);
    potionEffects.add(effect);
    ModCyclic.instance.events.register(effect);
  }
  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<Potion> event) {
    for (Potion b : potions) {
      event.getRegistry().register(b);
    }
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
