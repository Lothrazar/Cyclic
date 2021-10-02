package com.lothrazar.cyclic.item.apple;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilStepHeight;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class LoftyStatureApple extends ItemBase {

  public LoftyStatureApple(Properties properties) {
    super(properties);
  }

  @Override
  public Rarity getRarity(ItemStack stack) {
    return Rarity.UNCOMMON;
  }

  @Override
  public boolean hasEffect(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof PlayerEntity == false) {
      return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
    PlayerEntity player = (PlayerEntity) entityLiving;
    if (player.getCooldownTracker().hasCooldown(stack.getItem())) {
      return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
    player.getCooldownTracker().setCooldown(stack.getItem(), 40); // 2seconds
    if (!worldIn.isRemote) {
      CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
      datFile.toggleStepHeight();
      UtilChat.addServerChatMessage(player, "cyclic.unlocks.stepheight." + datFile.stepHeight);
    }
    return super.onItemUseFinish(stack, worldIn, entityLiving);
  }

  @Override
  public SoundEvent getEatSound() {
    return SoundRegistry.STEP_HEIGHT_UP;
  }

  public static void onUpdate(PlayerEntity player) {
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
