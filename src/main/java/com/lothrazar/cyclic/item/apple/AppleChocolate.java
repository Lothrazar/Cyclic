package com.lothrazar.cyclic.item.apple;

import java.util.Iterator;
import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public class AppleChocolate extends ItemBase {

  public AppleChocolate(Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    //    if (!worldIn.isRemote) entityLiving.curePotionEffects(stack); // FORGE - move up so stack.shrink does not turn stack into air
    curePotionEffects(entityLiving, stack);
    return super.onItemUseFinish(stack, worldIn, entityLiving);
  }

  private boolean curePotionEffects(LivingEntity entityLiving, ItemStack curativeItem) {
    boolean ret = false;
    Iterator<EffectInstance> itr = entityLiving.getActivePotionMap().values().iterator();
    while (itr.hasNext()) {
      EffectInstance effect = itr.next();
      if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent(entityLiving, effect))) continue;
      if (effect.getPotion().isBeneficial() == false) {
        //dont remove beneficial potions though such as speed, fire prot, night vision 
        effect.getPotion().removeAttributesModifiersFromEntity(entityLiving, entityLiving.getAttributeManager(), effect.getAmplifier());
        itr.remove();
        ret = true;
        entityLiving.potionsNeedUpdate = true;
        if (entityLiving instanceof PlayerEntity) {
          ((PlayerEntity) entityLiving).getCooldownTracker().setCooldown(this, 30);
        }
      }
    }
    return ret;
  }
}
