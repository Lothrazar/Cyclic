package com.lothrazar.cyclic.item.food;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ChocolateMilk extends FoodItemWithEffects {

  public ChocolateMilk(Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
    ItemStack result = super.finishUsingItem(stack, level, entity);
    if (entity instanceof Player player && !player.getAbilities().instabuild) {
      if (result.isEmpty()) {
        return new ItemStack(Items.GLASS_BOTTLE);
      }
      player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
    }
    return result;
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity entity) {
    return 34;
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.DRINK;
  }

  @Override
  public InteractionResult use(Level level, Player player, InteractionHand hand) {
    return ItemUtils.startUsingInstantly(level, player, hand);
  }
}
