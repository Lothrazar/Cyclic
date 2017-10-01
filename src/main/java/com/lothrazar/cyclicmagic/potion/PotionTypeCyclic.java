package com.lothrazar.cyclicmagic.potion;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

public class PotionTypeCyclic extends PotionType {
  public PotionTypeCyclic(String name, PotionEffect[] potionEffects) {
    super(name, potionEffects);
    this.setRegistryName(new ResourceLocation(Const.MODID, name));
  }
}
