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

public class HeartItem extends ItemBase {

  private static final int MAX = 100;
  private static final int COOLDOWN = 5000;

  public HeartItem(Properties properties) {
    super(properties);
    //see ItemEvents for saving hearts on death
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    if (player.getCooldownTracker().hasCooldown(this)) {
      return super.onItemUse(context);
    }
    player.getFoodStats().addStats(1, 4);
    IAttributeInstance healthAttribute = player.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
    if (healthAttribute.getBaseValue() < MAX) {
      UtilEntity.incrementMaxHealth(player, 2);
      player.getCooldownTracker().setCooldown(this, COOLDOWN);
      player.getHeldItem(context.getHand()).shrink(1);
      UtilSound.playSound(player, SoundRegistry.fill);
    }
    return super.onItemUse(context);
  }
}
