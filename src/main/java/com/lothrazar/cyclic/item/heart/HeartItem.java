package com.lothrazar.cyclic.item.heart;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.UUID;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class HeartItem extends ItemBaseCyclic {

  public static IntValue MAX;
  static final int COOLDOWN = 10;
  public static final UUID ID = UUID.fromString("06d30aa2-eff2-4a81-b92b-a1cb95f115c6");

  public HeartItem(Properties properties) {
    super(properties);
    // see ItemEvents for saving hearts on death
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (playerIn.getCooldowns().isOnCooldown(this)) {
      return super.use(worldIn, playerIn, handIn);
    }
    playerIn.getFoodData().eat(1, 4);
    AttributeInstance healthAttribute = playerIn.getAttribute(Attributes.MAX_HEALTH);
    if (healthAttribute.getValue() < MAX.get()) {
      //get attribute modif by id
      AttributeModifier oldHealthModifier = healthAttribute.getModifier(ID);
      //what is our value
      double addedHealth = (oldHealthModifier == null) ? +2.0D : oldHealthModifier.getAmount() + 2.0D;
      //replace the modifier on the main attribute
      healthAttribute.removeModifier(ID);
      AttributeModifier healthModifier = new AttributeModifier(ID, "HP Bonus from Cyclic", addedHealth, AttributeModifier.Operation.ADDITION);
      healthAttribute.addPermanentModifier(healthModifier);
      //finish up
      playerIn.getCooldowns().addCooldown(this, COOLDOWN);
      playerIn.getItemInHand(handIn).shrink(1);
      UtilSound.playSound(playerIn, SoundRegistry.FILL);
    }
    return super.use(worldIn, playerIn, handIn);
  }
}
