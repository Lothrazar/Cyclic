package com.lothrazar.cyclic.item;

import java.util.UUID;
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

public class HeartItem extends ItemBase {

  private static final int MAX = 100;
  static final int COOLDOWN = 60;
  public static final UUID healthModifierUuid = UUID.fromString("06d30aa2-eff2-4a81-b92b-a1cb95f115c6");

  public HeartItem(Properties properties) {
    super(properties);
    //see ItemEvents for saving hearts on death
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (playerIn.getCooldownTracker().hasCooldown(this)) {
      return super.onItemRightClick(worldIn, playerIn, handIn);
    }
    playerIn.getFoodStats().addStats(1, 4);
    ModifiableAttributeInstance healthAttribute = playerIn.getAttribute(Attributes.MAX_HEALTH);
    if (healthAttribute.getValue() < MAX) {
      //get attribute modif by id
      AttributeModifier oldHealthModifier = healthAttribute.getModifier(healthModifierUuid);
      //what is our value
      double addedHealth = (oldHealthModifier == null) ? +2.0D : oldHealthModifier.getAmount() + 2.0D;
      //replace the modifier on the main attribute
      healthAttribute.removeModifier(healthModifierUuid);
      AttributeModifier healthModifier = new AttributeModifier(healthModifierUuid, "HP Bonus from Cyclic", addedHealth, AttributeModifier.Operation.ADDITION);
      healthAttribute.applyPersistentModifier(healthModifier);
      //finish up
      playerIn.getCooldownTracker().setCooldown(this, COOLDOWN);
      playerIn.getHeldItem(handIn).shrink(1);
      UtilSound.playSound(playerIn, SoundRegistry.fill);
    }
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
}
