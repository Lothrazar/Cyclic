package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.AttributesUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;

public class HeartToxicItem extends ItemBaseCyclic {

  private static final int COOLDOWN = HeartItem.COOLDOWN;
  public static ModConfigSpec.IntValue HEARTXPMINUS;

  public HeartToxicItem(Properties properties) {
    super(properties);
    //see ItemEvents for saving hearts on death
  }

  @Override
  public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (playerIn.getCooldowns().isOnCooldown(playerIn.getItemInHand(handIn))) {
      return super.use(worldIn, playerIn, handIn);
    }
    AttributeInstance healthAttribute = playerIn.getAttribute(Attributes.MAX_HEALTH);
    //    if (healthAttribute != null && healthAttribute.getBaseValue() > 2) {
    //get attribute modif by id
    AttributeModifier oldHealthModifier = healthAttribute.getModifier(AttributesUtil.DEFAULT_ID);
    double addedHealth = 0;
    if (oldHealthModifier != null && oldHealthModifier.amount() <= -18) {
      addedHealth = -18;
    }
    else {
      addedHealth = (oldHealthModifier == null) ? -2.0D : oldHealthModifier.amount() - 2.0D;
      //actually DO the eating of the thing
      playerIn.getCooldowns().addCooldown(playerIn.getItemInHand(handIn), COOLDOWN);
      playerIn.getItemInHand(handIn).shrink(1);
      SoundUtil.playSound(playerIn, SoundRegistry.FILL.get());
      playerIn.getFoodData().eat(3, 1);
      playerIn.giveExperiencePoints(HEARTXPMINUS.get());
    }
    //replace the modifier on the main attribute
    healthAttribute.removeModifier(AttributesUtil.DEFAULT_ID);
    AttributeModifier healthModifier = new AttributeModifier(AttributesUtil.DEFAULT_ID, addedHealth, AttributeModifier.Operation.ADD_VALUE);
    healthAttribute.addPermanentModifier(healthModifier);
    //
    return super.use(worldIn, playerIn, handIn);
  }
}
