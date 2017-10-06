package com.lothrazar.cyclicmagic.potion;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.entity.EntityLivingBase;

public class PotionFrostWalker extends PotionBase {
  public PotionFrostWalker(String name, boolean b, int potionColor) {
    super(name, b, potionColor);
  }
  @Override
  public void tick(EntityLivingBase entityLiving) {
    EnchantmentFrostWalker.freezeNearby(entityLiving, entityLiving.world, entityLiving.getPosition(), 1);
  }
}
