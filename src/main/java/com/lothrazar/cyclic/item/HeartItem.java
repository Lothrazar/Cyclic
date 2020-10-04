package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

import java.util.UUID;

public class HeartItem extends ItemBase {

  private static final int MAX = 100;
  private static final int COOLDOWN = 5000;
  public static final UUID healthModifierUuid = UUID.fromString("06d30aa2-eff2-4a81-b92b-a1cb95f115c6");

  public HeartItem(Properties properties) {
    super(properties);
    //see ItemEvents for saving hearts on death
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    double addedHealth = 0.0D;
    if (player.getCooldownTracker().hasCooldown(this)) {
      return super.onItemUse(context);
    }
    player.getFoodStats().addStats(1, 4);
    ModifiableAttributeInstance healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);

    if (healthAttribute.getValue() < MAX) {
      try {
        AttributeModifier oldHealthModifier = healthAttribute.getModifier(healthModifierUuid);
        addedHealth = oldHealthModifier.getAmount() + 4.0D;
        healthAttribute.removeModifier(healthModifierUuid);
      } catch (NullPointerException e) {
        addedHealth = 4.0D;
      }
      AttributeModifier healthModifier = new AttributeModifier(healthModifierUuid, "HP Bonus from Cyclic", addedHealth, AttributeModifier.Operation.ADDITION);
      healthAttribute.applyPersistentModifier(healthModifier);
      player.getCooldownTracker().setCooldown(this, COOLDOWN);
      player.getHeldItem(context.getHand()).shrink(1);
      UtilSound.playSound(player, SoundRegistry.fill);
    }
    return super.onItemUse(context);
  }
}
