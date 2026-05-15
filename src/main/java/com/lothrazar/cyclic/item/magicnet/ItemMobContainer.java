package com.lothrazar.cyclic.item.magicnet;

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class ItemMobContainer extends ItemBaseCyclic {

  public ItemMobContainer(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (stack.has(DataComponents.CUSTOM_DATA)) {
      MutableComponent t = Component.translatable(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString(EntityMagicNetEmpty.NBT_ENTITYID));
      t.withStyle(ChatFormatting.GRAY);
      tooltip.add(t);
    }
    else {
      super.appendHoverText(stack, worldIn, tooltip, flagIn);
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
    if (!world.isClientSide) {
      Entity entity = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString(EntityMagicNetEmpty.NBT_ENTITYID)))
          .create(world);
      //    entity.egg
      entity.load(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag());
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
          player.drop(new ItemStack(ItemRegistry.MAGIC_NET.get()), true);
        }
        return InteractionResult.SUCCESS;
      }
    }
    return InteractionResult.PASS;
  }
}
