package com.lothrazar.cyclic.item.heart;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.AttributesUtil;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class HeartToxicItem extends ItemBaseCyclic {

  private static final int COOLDOWN = HeartItem.COOLDOWN;
  public static IntValue HEARTXPMINUS;

  public HeartToxicItem(Properties properties) {
    super(properties);
    //see ItemEvents for saving hearts on death
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (playerIn.getCooldowns().isOnCooldown(this)) {
      return super.use(worldIn, playerIn, handIn);
    }
    AttributeInstance healthAttribute = playerIn.getAttribute(Attributes.MAX_HEALTH);
    //    if (healthAttribute != null && healthAttribute.getBaseValue() > 2) {
    //get attribute modif by id
    AttributeModifier oldHealthModifier = healthAttribute.getModifier(AttributesUtil.DEFAULT_ID);
    double addedHealth = 0;
    if (oldHealthModifier != null && oldHealthModifier.getAmount() <= -18) {
      addedHealth = -18;
    }
    else {
      addedHealth = (oldHealthModifier == null) ? -2.0D : oldHealthModifier.getAmount() - 2.0D;
      //actually DO the eating of the thing
      playerIn.getCooldowns().addCooldown(this, COOLDOWN);
      playerIn.getItemInHand(handIn).shrink(1);
      UtilSound.playSound(playerIn, SoundRegistry.FILL);
      playerIn.getFoodData().eat(3, 1);
      playerIn.giveExperiencePoints(HEARTXPMINUS.get());
    }
    //replace the modifier on the main attribute
    healthAttribute.removeModifier(AttributesUtil.DEFAULT_ID);
    AttributeModifier healthModifier = new AttributeModifier(AttributesUtil.DEFAULT_ID, "HP Drain from Cyclic", addedHealth, AttributeModifier.Operation.ADDITION);
    healthAttribute.addPermanentModifier(healthModifier);
    //
    return super.use(worldIn, playerIn, handIn);
  }
}
