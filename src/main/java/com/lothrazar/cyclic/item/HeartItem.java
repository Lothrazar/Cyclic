package com.lothrazar.cyclic.item;

import java.util.UUID;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

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
    if (player.getCooldownTracker().hasCooldown(this)) {
      return super.onItemUse(context);
    }
    player.getFoodStats().addStats(1, 4);
    IAttributeInstance healthAttribute = player.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
    
    if (healthAttribute.getValue() < MAX) {
    	//get attribute modif by id
        AttributeModifier oldHealthModifier = healthAttribute.getModifier(healthModifierUuid);
        //what is our value
        double addedHealth = (oldHealthModifier == null) ? +2.0D : oldHealthModifier.getAmount() + 2.0D;
        //replace the modifier on the main attribute
        healthAttribute.removeModifier(healthModifierUuid);
        AttributeModifier healthModifier = new AttributeModifier(healthModifierUuid, "HP Bonus from Cyclic", addedHealth, AttributeModifier.Operation.ADDITION);
        healthAttribute.applyModifier(healthModifier);
        //finish up
        player.getCooldownTracker().setCooldown(this, COOLDOWN);
        player.getHeldItem(context.getHand()).shrink(1);
        UtilSound.playSound(player, SoundRegistry.fill);
    	
    }
    return super.onItemUse(context);
  }
}
