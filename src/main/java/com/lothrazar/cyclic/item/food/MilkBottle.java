package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class MilkBottle extends ItemBaseCyclic {

  public MilkBottle(Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack finishUsingItem(ItemStack drink, Level world, LivingEntity entity) {
    //    super.finishUsingItem(drink, world, entity);
    Player player = entity instanceof Player ? (Player) entity : null;
    if (player instanceof ServerPlayer) {
      CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, drink);
    }
    if (!world.isClientSide && drink.getItem() == this) {
      entity.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
    }
    if (player != null) {
      player.awardStat(Stats.ITEM_USED.get(this));
      if (!player.getAbilities().instabuild) {
        drink.shrink(1);
      }
    }
    if (player == null || !player.getAbilities().instabuild) {
      //if non creative 
      if (drink.isEmpty()) {
        return new ItemStack(Items.GLASS_BOTTLE);
      }
      if (player != null) {
        player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
      }
    }
    world.gameEvent(entity, GameEvent.DRINK, entity.blockPosition());
    return drink;
  }

  @Override
  public int getUseDuration(ItemStack st) {
    return 34;
  }

  @Override
  public UseAnim getUseAnimation(ItemStack st) {
    return UseAnim.DRINK;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
    return ItemUtils.startUsingInstantly(world, player, hand);
  }
}
