package com.lothrazar.cyclic.item.datacard;

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityDataCard extends ItemBaseCyclic {

  private static final String ENTITY_DATA = "entity_data";
  private static final String ENTITY_KEY = "entity_key";

  public EntityDataCard(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (stack.hasTag()) {
      TranslatableComponent t = new TranslatableComponent(stack.getTag().getString(ENTITY_KEY));
      t.withStyle(ChatFormatting.GRAY);
      tooltip.add(t);
    }
    else {
      super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    if (player.isCrouching()) {
      CompoundTag atag = player.getItemInHand(hand).getOrCreateTag();
      atag.put(ENTITY_DATA, player.getPersistentData());
      atag.putString(ENTITY_KEY, "player");
    }
    return super.use(level, player, hand);
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
    playerIn.swing(hand);
    CompoundTag atag = stack.getOrCreateTag();
    atag.put(ENTITY_DATA, target.getPersistentData());
    if (target instanceof Player) {
      atag.putString(ENTITY_KEY, "player");
    }
    else {
      String key = EntityType.getKey(target.getType()).toString();
      atag.putString(ENTITY_KEY, key);
    }
    stack.setTag(atag);
    return super.interactLivingEntity(stack, playerIn, target, hand);
  }

  public static boolean matchesEntity(Entity etar, ItemStack stack) {
    if (etar == null || !hasEntity(stack)) {
      return false;
    }
    final EntityType<?> type = getEntityType(stack);
    return type == etar.getType();
  }

  private static EntityType<?> getEntityType(ItemStack stack) {
    if (stack.getItem() instanceof EntityDataCard) {
      final String key = stack.getTag().getString(ENTITY_KEY);
      return EntityType.byString(key).orElse(null);
    }
    return null;
  }

  public static boolean hasEntity(ItemStack stack) {
    if (stack.getItem() instanceof EntityDataCard) {
      return stack.hasTag() && stack.getTag().contains(ENTITY_KEY);
    }
    return false;
  }
}
