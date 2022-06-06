package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilStepHeight;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class LoftyStatureApple extends ItemBaseCyclic {

  public LoftyStatureApple(Properties properties) {
    super(properties);
  }

  @Override
  public Rarity getRarity(ItemStack stack) {
    return Rarity.UNCOMMON;
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof Player == false) {
      return super.finishUsingItem(stack, worldIn, entityLiving);
    }
    Player player = (Player) entityLiving;
    if (player.getCooldowns().isOnCooldown(stack.getItem())) {
      return super.finishUsingItem(stack, worldIn, entityLiving);
    }
    player.getCooldowns().addCooldown(stack.getItem(), 40); // 2seconds
    if (!worldIn.isClientSide) {
      CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
      datFile.toggleStepHeight();
      UtilChat.addServerChatMessage(player, "cyclic.unlocks.stepheight." + datFile.stepHeight);
    }
    return super.finishUsingItem(stack, worldIn, entityLiving);
  }

  @Override
  public SoundEvent getEatingSound() {
    return SoundRegistry.STEP_HEIGHT_UP;
  }

  public static void onUpdate(Player player) {
    CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
    if (datFile.stepHeight) {
      UtilStepHeight.enableStepHeight(player);
    }
    else {
      // do we force it off?
      if (datFile.stepHeightForceOff) {
        UtilStepHeight.disableStepHeightForced(player);
      }
    }
  }
}
