package com.lothrazar.cyclic.item.magicnet;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.List;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.item.Item.Properties;

public class ItemMobContainer extends ItemBase {

  public ItemMobContainer(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (stack.hasTag()) {
      TranslatableComponent t = new TranslatableComponent(stack.getTag().getString(EntityMagicNetEmpty.NBT_ENTITYID));
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
    if (stack.hasTag() == false) {
      return InteractionResult.PASS;
    }
    BlockPos pos = context.getClickedPos();
    if (context.getClickedFace() != null) {
      pos = pos.relative(context.getClickedFace());
    }
    Level world = context.getLevel();
    UtilSound.playSound(player, SoundRegistry.MONSTER_BALL_RELEASE, 0.3F, 1F);
    if (!world.isClientSide) {
      Entity entity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(stack.getTag().getString(EntityMagicNetEmpty.NBT_ENTITYID)))
          .create(world);
      //    entity.egg
      entity.load(stack.getTag());
      entity.setPos(pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5);
      if (world.addFreshEntity(entity)) {
        //eat up that stack
        stack.setTag(null);
        stack.shrink(1);
        if (stack.isEmpty()) {
          player.setItemInHand(context.getHand(), ItemStack.EMPTY);
        }
        if (!player.isCreative()) {
          //and replace with empty 
          //if config says drop?
          player.drop(new ItemStack(ItemRegistry.magic_net), true);
        }
        return InteractionResult.SUCCESS;
      }
    }
    return InteractionResult.PASS;
  }
}
