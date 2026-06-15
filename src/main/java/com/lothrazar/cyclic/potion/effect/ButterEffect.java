package com.lothrazar.cyclic.potion.effect;

import java.util.Arrays;
import java.util.List;
import com.lothrazar.cyclic.potion.CyclicMobEffect;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;

public class ButterEffect extends CyclicMobEffect {

  private static final double DROP_CHANCE = 0.06;

  public ButterEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void tick(EntityTickEvent.Pre event) {
    // delete me i guess
    if(event.getEntity() instanceof LivingEntity living) {
      var level = living.level();
      if (level.random.nextDouble() > DROP_CHANCE) {
        return;
      }
      List<EquipmentSlot> slots = null;
      if (!living.onGround() || living.isSprinting()) {
        int amplifier = living.getEffect(PotionEffectRegistry.BUTTERFINGERS).getAmplifier();
        //sprinting or jumping or something
        if (amplifier == Const.Potions.I) {
          slots = Arrays.asList(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
        } else {
          slots = Arrays.asList(EquipmentSlot.values());
        }
      }
      if (slots == null) {
        return;
      }
      int slotix = level.random.nextInt(slots.size());
      ItemStack dropMe = living.getItemBySlot(slots.get(slotix));
      ItemStackUtil.drop(level, living.blockPosition(), dropMe);
      living.setItemSlot(slots.get(slotix), ItemStack.EMPTY);
    }

  }
}
