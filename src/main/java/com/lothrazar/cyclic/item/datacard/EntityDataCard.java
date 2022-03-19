package com.lothrazar.cyclic.item.datacard;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.item.magicnet.EntityMagicNetEmpty;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityDataCard extends ItemBaseCyclic {

  public EntityDataCard(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (stack.hasTag()) {
      TranslatableComponent t = new TranslatableComponent(stack.getTag().getString("entity_id"));
      t.withStyle(ChatFormatting.GRAY);
      tooltip.add(t);
    }
    else {
      super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
    playerIn.swing(hand);
    String id = EntityType.getKey(target.getType()).toString();
    stack.getOrCreateTag().putString("entity_id", id);
    stack.getOrCreateTag().put("entity_data", target.getPersistentData());
    return super.interactLivingEntity(stack, playerIn, target, hand);
  }
}
