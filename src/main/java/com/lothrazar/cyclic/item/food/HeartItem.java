package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.AttributesUtil;
import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class HeartItem extends ItemBaseCyclic {

  public static IntValue MAX;
  static final int COOLDOWN = 10;

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
      AttributesUtil.updateAttrModifierBy(Attributes.MAX_HEALTH, AttributesUtil.DEFAULT_ID, playerIn, 2);
      //finish up
      playerIn.getCooldowns().addCooldown(this, COOLDOWN);
      playerIn.getItemInHand(handIn).shrink(1);
      SoundUtil.playSound(playerIn, SoundRegistry.FILL.get());
    }
    return super.use(worldIn, playerIn, handIn);
  }
}
