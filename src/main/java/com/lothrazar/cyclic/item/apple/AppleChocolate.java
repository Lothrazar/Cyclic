package com.lothrazar.cyclic.item.apple;

import com.lothrazar.cyclic.base.ItemBase;
import java.util.Iterator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;

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
      if (MinecraftForge.EVENT_BUS.post(new PotionEvent.PotionRemoveEvent(entityLiving, effect))) {
        continue;
      }
        //dont remove beneficial potions though such as speed, fire prot, night vision 
      if (!effect.getPotion().isBeneficial()) {
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
