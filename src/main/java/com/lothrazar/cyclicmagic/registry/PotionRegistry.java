package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.potion.PotionCustom;
import com.lothrazar.cyclicmagic.potion.PotionEnder;
import com.lothrazar.cyclicmagic.potion.PotionMagnet;
import com.lothrazar.cyclicmagic.potion.PotionSlowfall;
import com.lothrazar.cyclicmagic.potion.PotionSnow;
import com.lothrazar.cyclicmagic.potion.PotionWaterwalk;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PotionRegistry {
  // tired;//http://www.minecraftforge.net/wiki/Potion_Tutorial
  public static PotionCustom slowfall;
  public static PotionCustom magnet;
  public static PotionCustom ender;
  public static PotionCustom waterwalk;
  public static PotionCustom snow;
  public final static int I = 0;
  public final static int II = 1;
  public final static int III = 2;
  public final static int IV = 3;
  public final static int V = 4;
  public static void register() {
    // http://www.minecraftforge.net/forum/index.php?topic=11024.0
    // ??? http://www.minecraftforge.net/forum/index.php?topic=12358.0
    PotionRegistry.ender = new PotionEnder("ender", true, 0);
    PotionRegistry.waterwalk = new PotionWaterwalk("waterwalk", true, 0);
    PotionRegistry.slowfall = new PotionSlowfall("slowfall", true, 0);
    PotionRegistry.magnet = new PotionMagnet("magnet", true, 0);
    PotionRegistry.snow = new PotionSnow("snow", true, 0);
    GameRegistry.register(ender, ender.getIcon());//was geticon
    GameRegistry.register(waterwalk, waterwalk.getIcon());
    GameRegistry.register(slowfall, slowfall.getIcon());
    GameRegistry.register(magnet, magnet.getIcon());
    GameRegistry.register(snow, snow.getIcon());
  }
  public static void addOrMergePotionEffect(EntityLivingBase player, PotionEffect newp) {
    // this could be in a utilPotion class i guess...
    if (player.isPotionActive(newp.getPotion())) {
      // do not use built in 'combine' function, just add up duration
      PotionEffect p = player.getActivePotionEffect(newp.getPotion());
      int ampMax = Math.max(p.getAmplifier(), newp.getAmplifier());
      int dur = newp.getDuration() + p.getDuration();
      player.addPotionEffect(new PotionEffect(newp.getPotion(), dur, ampMax));
    }
    else {
      player.addPotionEffect(newp);
    }
  }
  
  public static void syncConfig(Configuration config) {

  }
}