package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class HeartToxicItem extends ItemBase {

  private static final int COOLDOWN = HeartItem.COOLDOWN;
  private static final int EXP = 500;

  public HeartToxicItem(Properties properties) {
    super(properties);
    //see ItemEvents for saving hearts on death
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (playerIn.getCooldownTracker().hasCooldown(this)) {
      return super.onItemRightClick(worldIn, playerIn, handIn);
    }
    ModifiableAttributeInstance healthAttribute = playerIn.getAttribute(Attributes.MAX_HEALTH);
    if (healthAttribute != null && healthAttribute.getBaseValue() > 2) {
      playerIn.getFoodStats().addStats(3, 1);
      playerIn.giveExperiencePoints(EXP);
      //get attribute modif by id
      AttributeModifier oldHealthModifier = healthAttribute.getModifier(HeartItem.healthModifierUuid);
      double addedHealth = (oldHealthModifier == null) ? -2.0D : oldHealthModifier.getAmount() - 2.0D;
      //replace the modifier on the main attribute
      healthAttribute.removeModifier(HeartItem.healthModifierUuid);
      AttributeModifier healthModifier = new AttributeModifier(HeartItem.healthModifierUuid, "HP Drain from Cyclic", addedHealth, AttributeModifier.Operation.ADDITION);
      healthAttribute.applyPersistentModifier(healthModifier);
      //
      //finish up
      playerIn.getCooldownTracker().setCooldown(this, COOLDOWN);
      playerIn.getHeldItem(handIn).shrink(1);
      UtilSound.playSound(playerIn, SoundRegistry.fill);
    }
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
}
