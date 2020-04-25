package com.lothrazar.cyclic.item;

import java.util.UUID;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;

public class HeartItem extends ItemBase {

  static UUID id = UUID.randomUUID();

  public HeartItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    if (player.getCooldownTracker().hasCooldown(this)) {
      return super.onItemUse(context);
    }
    player.getFoodStats().addStats(1, 4);
    IAttributeInstance healthAttribute = player.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
    if (healthAttribute.getBaseValue() < 100) {
      healthAttribute.setBaseValue(healthAttribute.getBaseValue() + 2);
      player.getCooldownTracker().setCooldown(this, 5000);
      player.getHeldItem(context.getHand()).shrink(1);
      UtilSound.playSound(player, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
    }
    return super.onItemUse(context);
  }
}
