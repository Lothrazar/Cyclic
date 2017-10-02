package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.potion.PotionBase;
import com.lothrazar.cyclicmagic.potion.PotionBounce;
import com.lothrazar.cyclicmagic.potion.PotionEnder;
import com.lothrazar.cyclicmagic.potion.PotionMagnet;
import com.lothrazar.cyclicmagic.potion.PotionSlowfall;
import com.lothrazar.cyclicmagic.potion.PotionSnow;
import com.lothrazar.cyclicmagic.potion.PotionSwimSpeed;
import com.lothrazar.cyclicmagic.potion.PotionWaterwalk;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionEffectRegistry {
  public static ArrayList<Potion> potions = new ArrayList<Potion>();
  public static enum PotionSize {
    NORMAL, POWERED, LONG//, SPLASH, LINGER // todo: these last two
  }
  public static final PotionBase SLOWFALL = new PotionSlowfall("slowfall", true, 0xF46F20);
  public static final PotionBase MAGNET = new PotionMagnet("magnet", true, 0x224BAF);
  public static final PotionBase ENDER = new PotionEnder("ender", true, 0x0B4D42);
  public static final PotionBase WATERWALK = new PotionWaterwalk("waterwalk", true, 0x7FB8A4);
  public static final PotionBase SNOW = new PotionSnow("snow", true, 0x8EBFFF);
  public static final PotionBase SWIMSPEED = new PotionSwimSpeed("swimspeed", true, 0xB477FF);
  public static final PotionBase BOUNCE = new PotionBounce("bounce", true, 0x91E459);
  public static ArrayList<PotionBase> potionEffects = new ArrayList<PotionBase>();
  private static void register() {
  //  PotionType t http://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/2842885-solved-how-can-i-add-my-own-potion-with-my-own
    
    /*Assuming you've created and registered the Potion instances, you then need to create and register a PotionType for each brewable potion. For most vanilla Potions there are one to three PotionTypes: regular (e.g. Regeneration, 0:45) , long (e.g. Regeneration, 1:30) and strong (e.g. Regeneration II, 0:22).

Both Potion and PotionType are implementations of IForgeRegistryEntry, so they should be registered during the appropriate registry events.

Once you've created and registered the Potions and PotionTypes, use PotionHelper.addMix to add the brewing recipes (e.g. Awkward to Regular X, Regular X to Long X and Regular X to Strong X). For more advanced brewing recipes, you can use Forge's BrewingRecipeRegistry.
*/
    
    PotionEffectRegistry.registerPotionEffect(MAGNET);
    PotionEffectRegistry.registerPotionEffect(ENDER);
    PotionEffectRegistry.registerPotionEffect(WATERWALK);
    PotionEffectRegistry.registerPotionEffect(SLOWFALL);
    PotionEffectRegistry.registerPotionEffect(SNOW);
    PotionEffectRegistry.registerPotionEffect(SWIMSPEED);
    PotionEffectRegistry.registerPotionEffect(BOUNCE);
  }
  private static void registerPotionEffect(PotionBase effect) {
    effect.setIcon(effect.getIcon());
    effect.setRegistryName(new ResourceLocation(Const.MODID, effect.getName()));
    potions.add(effect);
    potionEffects.add(effect);
    ModCyclic.instance.events.register(effect);
  }
  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<Potion> event) {
    PotionEffectRegistry.register();
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
