package com.lothrazar.cyclic.item.datacard;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;

import java.util.function.Consumer;

public class SoundCard extends ItemBaseCyclic {

  public static final String SOUND_ID = "sound_id";

  public SoundCard(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {

    Player player = context.getPlayer();
    ItemStack stack = context.getItemInHand();
    if (player.getCooldowns().isOnCooldown(stack)) {
      return InteractionResult.PASS;
    }
    if (stack.has(DataComponents.CUSTOM_DATA) && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(SOUND_ID)) {
      //assume sound is valid
      player.getCooldowns().addCooldown(stack, 10);
      player.swing(context.getHand());
      //actually play it
      String sid = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr(SOUND_ID, "");
      SoundUtil.playSoundById(player, sid);
    }
    return InteractionResult.PASS;
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltipDisplay, tooltip, flagIn);
    if (stack.has(DataComponents.CUSTOM_DATA) && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(SOUND_ID)) {
      tooltip.accept(Component.translatable(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr(SOUND_ID, "")).withStyle(ChatFormatting.GOLD));
    }
  }

  public static void saveSound(ItemStack stack, String soundId) {
    if (stack.has(DataComponents.CUSTOM_DATA) && (soundId == null || soundId.isEmpty())) {
      CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      tag.remove(SOUND_ID);
      stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
    else {
      CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      tag.putString(SOUND_ID, soundId);
      stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
  }
}
