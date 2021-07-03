package com.lothrazar.cyclic.item.heart;

import com.lothrazar.cyclic.ConfigRegistry;
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
    //    if (healthAttribute != null && healthAttribute.getBaseValue() > 2) {
    //get attribute modif by id
    AttributeModifier oldHealthModifier = healthAttribute.getModifier(HeartItem.ID);
    double addedHealth = 0;
    if (oldHealthModifier != null && oldHealthModifier.getAmount() <= -18) {
      addedHealth = -18;
    }
    else {
      addedHealth = (oldHealthModifier == null) ? -2.0D : oldHealthModifier.getAmount() - 2.0D;
      //actually DO the eating of the thing
      playerIn.getCooldownTracker().setCooldown(this, COOLDOWN);
      playerIn.getHeldItem(handIn).shrink(1);
      UtilSound.playSound(playerIn, SoundRegistry.FILL);
      playerIn.getFoodStats().addStats(3, 1);
      playerIn.giveExperiencePoints(ConfigRegistry.HEARTXPMINUS.get());
    }
    //replace the modifier on the main attribute
    healthAttribute.removeModifier(HeartItem.ID);
    AttributeModifier healthModifier = new AttributeModifier(HeartItem.ID, "HP Drain from Cyclic", addedHealth, AttributeModifier.Operation.ADDITION);
    healthAttribute.applyPersistentModifier(healthModifier);
    //
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
}
