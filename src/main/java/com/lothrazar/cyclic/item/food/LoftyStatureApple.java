package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.capabilities.player.PlayerCyclicAttachment;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.AttachmentRegistry;
import com.lothrazar.library.util.AttributesUtil;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LoftyStatureApple extends ItemBaseCyclic {

  public LoftyStatureApple(Properties properties) {
    super(properties);
  }


  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof Player == false) {
      return super.finishUsingItem(stack, worldIn, entityLiving);
    }
    Player player = (Player) entityLiving;
    if (player.getCooldowns().isOnCooldown(stack)) {
      return super.finishUsingItem(stack, worldIn, entityLiving);
    }
    player.getCooldowns().addCooldown(stack, 40); // 2seconds
    if (!worldIn.isClientSide()) {
      PlayerCyclicAttachment data = player.getData(AttachmentRegistry.CYCLIC_PLAYER);
      data.toggleStepHeight();
      ChatUtil.addServerChatMessage(player, "cyclic.unlocks.stepheight." + data.stepHeight);
    }
    return super.finishUsingItem(stack, worldIn, entityLiving);
  }

  public static void onUpdate(Player player) {
    if (player.level().isClientSide()) {
      return;
    }
    PlayerCyclicAttachment data = player.getData(AttachmentRegistry.CYCLIC_PLAYER);
    if (data.stepHeight) {
      AttributesUtil.enableStepHeight(player);
    }
    else {
      if (data.stepHeightForceOff) {
        AttributesUtil.disableStepHeight(player);
      }
    }
  }
}
