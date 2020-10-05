package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class HeartToxicItem extends ItemBase {

  private static final int COOLDOWN = HeartItem.COOLDOWN;
  private static final int EXP = 500;

  public HeartToxicItem(Properties properties) {
    super(properties);
    //see ItemEvents for saving hearts on death
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    if (player.getCooldownTracker().hasCooldown(this)) {
      return super.onItemUse(context);
    }
    ModifiableAttributeInstance healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
    if (healthAttribute != null && healthAttribute.getBaseValue() > 2) {
      player.getFoodStats().addStats(3, 1);
      player.giveExperiencePoints(EXP);
      //get attribute modif by id
      AttributeModifier oldHealthModifier = healthAttribute.getModifier(HeartItem.healthModifierUuid);
      double addedHealth = (oldHealthModifier == null) ? -2.0D : oldHealthModifier.getAmount() - 2.0D;
      //replace the modifier on the main attribute
      healthAttribute.removeModifier(HeartItem.healthModifierUuid);
      AttributeModifier healthModifier = new AttributeModifier(HeartItem.healthModifierUuid, "HP Drain from Cyclic", addedHealth, AttributeModifier.Operation.ADDITION);
      healthAttribute.applyPersistentModifier(healthModifier);
      //
      //finish up
      player.getCooldownTracker().setCooldown(this, COOLDOWN);
      player.getHeldItem(context.getHand()).shrink(1);
      UtilSound.playSound(player, SoundRegistry.fill);
    }
    return super.onItemUse(context);
  }
}
