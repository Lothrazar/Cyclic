package com.lothrazar.cyclic.item.magicnet;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;

import java.util.function.Consumer;

public class ItemMobContainer extends ItemBaseCyclic {

  public ItemMobContainer(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flagIn) {
    if (stack.has(DataComponents.CUSTOM_DATA)) {
      MutableComponent t = Component.translatable(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr(EntityMagicNetEmpty.NBT_ENTITYID, ""));
      t.withStyle(ChatFormatting.GRAY);
      tooltip.accept(t);
    }
    else {
      super.appendHoverText(stack, worldIn, tooltipDisplay, tooltip, flagIn);
    }
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    ItemStack stack = player.getItemInHand(context.getHand());
    if (stack.has(DataComponents.CUSTOM_DATA) == false) {
      return InteractionResult.PASS;
    }
    BlockPos pos = context.getClickedPos();
    if (context.getClickedFace() != null) {
      pos = pos.relative(context.getClickedFace());
    }
    Level world = context.getLevel();
    SoundUtil.playSound(player, SoundRegistry.MONSTER_BALL_RELEASE.get(), 0.3F, 1F);
    if (!world.isClientSide()) {
      // 26.1: Registry#get(Identifier) now returns Optional<Holder.Reference<T>>, not a raw nullable
      // value; EntityType#create(Level) also gained a required EntitySpawnReason param.
      Entity entity = BuiltInRegistries.ENTITY_TYPE.get(Identifier.parse(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr(EntityMagicNetEmpty.NBT_ENTITYID, "")))
          .map(Holder.Reference::value)
          .orElse(null)
          .create(world, EntitySpawnReason.SPAWN_ITEM_USE);
      //    entity.egg
      entity.load(TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(),
          stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag()));
      entity.setPos(pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5);
      if (world.addFreshEntity(entity)) {
        //eat up that stack
        stack.remove(DataComponents.CUSTOM_DATA);
        stack.shrink(1);
        if (stack.isEmpty()) {
          player.setItemInHand(context.getHand(), ItemStack.EMPTY);
        }
        if (!player.isCreative()) {
          //and replace with empty 
          //if config says drop?
          player.drop(new ItemStack(ItemRegistry.MOB_CONTAINER_EMPTY.get()), true);
        }
        return InteractionResult.SUCCESS;
      }
    }
    return InteractionResult.PASS;
  }
}
