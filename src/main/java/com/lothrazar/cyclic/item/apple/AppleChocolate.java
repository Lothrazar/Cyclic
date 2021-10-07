package com.lothrazar.cyclic.item.apple;

import com.lothrazar.cyclic.base.ItemBase;
import java.util.Iterator;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;

import net.minecraft.world.item.Item.Properties;

public class AppleChocolate extends ItemBase {

  public AppleChocolate(Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
    //    if (!worldIn.isRemote) entityLiving.curePotionEffects(stack); // FORGE - move up so stack.shrink does not turn stack into air
    curePotionEffects(entityLiving, stack);
    return super.finishUsingItem(stack, worldIn, entityLiving);
  }

  private boolean curePotionEffects(LivingEntity entityLiving, ItemStack curativeItem) {
    boolean ret = false;
    Iterator<MobEffectInstance> itr = entityLiving.getActiveEffectsMap().values().iterator();
    while (itr.hasNext()) {
      MobEffectInstance effect = itr.next();
      if (MinecraftForge.EVENT_BUS.post(new PotionEvent.PotionRemoveEvent(entityLiving, effect))) {
        continue;
      }
      if (effect.getEffect().isBeneficial() == false) {
        //dont remove beneficial potions though such as speed, fire prot, night vision 
        effect.getEffect().removeAttributeModifiers(entityLiving, entityLiving.getAttributes(), effect.getAmplifier());
        itr.remove();
        ret = true;
        entityLiving.effectsDirty = true;
        if (entityLiving instanceof Player) {
          ((Player) entityLiving).getCooldowns().addCooldown(this, 30);
        }
      }
    }
    return ret;
  }
}
