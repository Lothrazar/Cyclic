package com.lothrazar.cyclic.item;

import java.util.UUID;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class HeartToxicItem extends ItemBase {

  static UUID id = UUID.randomUUID();

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
    if (healthAttribute != null && healthAttribute.getBaseValue() > 2) {
      player.getFoodStats().addStats(3, 1);
      player.giveExperiencePoints(500);
      healthAttribute.setBaseValue(healthAttribute.getBaseValue() - 2);
      player.getCooldownTracker().setCooldown(this, 100);
      player.getHeldItem(context.getHand()).shrink(1);
      UtilSound.playSound(player, SoundRegistry.fill);
    }
    return super.onItemUse(context);
  }
}
