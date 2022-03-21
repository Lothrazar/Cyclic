package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.registry.ItemRegistry;
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
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
public class DrinkBottle extends ItemBaseCyclic {

  public DrinkBottle(Properties properties) {
    super(properties);
    PotionItem u;
  }

  @Override
  public ItemStack finishUsingItem(ItemStack drink, Level world, LivingEntity entity) {
    Player player = entity instanceof Player ? (Player) entity : null;
    if (player instanceof ServerPlayer) {
      CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, drink);
    }
    if (!world.isClientSide && drink.getItem() == ItemRegistry.MILK_BOTTLE.get()) {
      entity.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
      //      for(MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(drink)) {
      //        if (mobeffectinstance.getEffect().isInstantenous()) {
      //          mobeffectinstance.getEffect().applyInstantenousEffect(player, player, entity, mobeffectinstance.getAmplifier(), 1.0D);
      //        } else {
      //          entity.addEffect(new MobEffectInstance(mobeffectinstance));
      //        }
      //      }
    }
    if (player != null) {
      player.awardStat(Stats.ITEM_USED.get(this));
      if (!player.getAbilities().instabuild) {
        drink.shrink(1);
      }
    }
    if (player == null || !player.getAbilities().instabuild) {
      if (drink.isEmpty()) {
        return new ItemStack(Items.GLASS_BOTTLE);
      }
      if (player != null) {
        player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
      }
    }
    world.gameEvent(entity, GameEvent.DRINKING_FINISH, entity.eyeBlockPosition());
    return drink;
  }

  @Override
  public int getUseDuration(ItemStack p_43001_) {
    return 32;
  }

  @Override
  public UseAnim getUseAnimation(ItemStack p_42997_) {
    return UseAnim.DRINK;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level p_42993_, Player p_42994_, InteractionHand p_42995_) {
    return ItemUtils.startUsingInstantly(p_42993_, p_42994_, p_42995_);
  }
}
