package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class HeartToxicItem extends ItemBase {

  private static final int COOLDOWN = 100;
  private static final int EXP = 500;

  public HeartToxicItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    if (player.getCooldownTracker().hasCooldown(this)) {
      return super.onItemUse(context);
    }
    IAttributeInstance healthAttribute = player.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
    AttributeModifier oldHealthModifier = healthAttribute.getModifier(HeartItem.healthModifierUuid);
    double addedHealth = 0;
    if (oldHealthModifier != null && oldHealthModifier.getAmount() <= -18) {
      addedHealth = -18;
    }else {
        addedHealth = (oldHealthModifier == null) ? -2.0D : oldHealthModifier.getAmount() - 2.0D;
        //actually DO the eating of the thing
        player.getCooldownTracker().setCooldown(this, COOLDOWN);
        player.getHeldItem(context.getHand()).shrink(1);
        UtilSound.playSound(player, SoundRegistry.fill);
        player.getFoodStats().addStats(3, 1);
        player.giveExperiencePoints(EXP);
    }
    healthAttribute.removeModifier(HeartItem.healthModifierUuid);
    AttributeModifier healthModifier = new AttributeModifier(HeartItem.healthModifierUuid, "HP Drain from Cyclic", addedHealth, AttributeModifier.Operation.ADDITION);
    healthAttribute.applyModifier(healthModifier);
    
    return super.onItemUse(context);
  }
}
