package com.lothrazar.cyclic.potion.effect;

import java.util.Arrays;
import java.util.List;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import com.lothrazar.cyclic.util.ItemStackUtil;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class ButterEffect extends TickableEffect {

  private static final double DROP_CHANCE = 0.06;

  public ButterEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
    PotionEffectRegistry.EFFECTS.add(this);
  }

  @Override
  public void tick(LivingUpdateEvent event) {
    // delete me i guess 
    LivingEntity living = event.getEntityLiving();
    if (living.level.random.nextDouble() > DROP_CHANCE) {
      return;
    }
    List<EquipmentSlot> slots = null;
    if (!living.isOnGround() || living.isSprinting()) {
      int amplifier = living.getEffect(this).getAmplifier();
      //sprinting or jumping or something 
      if (amplifier == Const.Potions.I) {
        slots = Arrays.asList(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
      }
      else {
        slots = Arrays.asList(EquipmentSlot.values());
      }
    }
    if (slots == null) {
      return;
    }
    int slotix = living.level.random.nextInt(slots.size());
    ItemStack dropMe = living.getItemBySlot(slots.get(slotix));
    ItemStackUtil.drop(living.level, living.blockPosition(), dropMe);
    living.setItemSlot(slots.get(slotix), ItemStack.EMPTY);
    //    for (EquipmentSlot slot : slots) {
    //      stack = entity.getItemStackFromSlot(slot);
    //      if (stack.isEmpty() == false && world.rand.nextDouble() < DROP_CHANCE) {
    //        if (world.isRemote) {
    //          ModCyclic.network.sendToServer(new PacketEntityDropRandom(entity.getEntityId(), slot.ordinal()));
    //        }
    //        //          entity.setItemStackToSlot(slot, ItemStack.EMPTY);
    //        break;
    //      }
    //    }
  }
}
