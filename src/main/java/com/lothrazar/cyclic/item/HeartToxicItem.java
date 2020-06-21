package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.SharedMonsterAttributes;
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
    if (healthAttribute != null && healthAttribute.getBaseValue() > 2) {
      player.getFoodStats().addStats(3, 1);
      player.giveExperiencePoints(EXP);
      UtilEntity.incrementMaxHealth(player, -2);
      player.getCooldownTracker().setCooldown(this, COOLDOWN);
      player.getHeldItem(context.getHand()).shrink(1);
      UtilSound.playSound(player, SoundRegistry.fill);
    }
    return super.onItemUse(context);
  }
}
