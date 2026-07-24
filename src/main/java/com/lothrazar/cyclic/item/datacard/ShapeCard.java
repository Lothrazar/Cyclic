package com.lothrazar.cyclic.item.datacard;

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.item.builder.BuilderActionType;
import com.lothrazar.library.data.RelativeShape;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class ShapeCard extends ItemBaseCyclic {

  public ShapeCard(Properties properties) {
    super(properties);
  }

  public static void setBlockState(ItemStack wand, BlockState target) {
    CompoundTag encoded = NbtUtils.writeBlockState(target);
    CompoundTag tag = wand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag(); tag.put(BuilderActionType.NBTBLOCKSTATE, encoded); wand.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext  worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    RelativeShape shape = RelativeShape.read(stack);
    if (shape != null) {
      MutableComponent t = Component.translatable(getDescriptionId() + ".count");
      t.append(shape.getCount() + "");
      tooltip.add(t);
      BlockState target = BuilderActionType.getBlockState(null, stack);
      String block = "scepter.cyclic.nothing";
      if (target != null) {
        block = target.getBlock().getDescriptionId();
      }
      tooltip.add(Component.translatable(ChatFormatting.AQUA + ChatUtil.lang(block)));
      if (flagIn.isAdvanced()) {
        //        String side = "S: " + dim.getSide().toString().toUpperCase();
        //        tooltip.add(new TranslationTextComponent(side));
        //        String sideF = "F: " + dim.getSidePlayerFacing().toString().toUpperCase();
        //        tooltip.add(new TranslationTextComponent(sideF));
        //        tooltip.add(new TranslationTextComponent("H: " + dim.getHitVec().toString()));
      }
    }
    MutableComponent t = Component.translatable(getDescriptionId() + ".tooltip");
    t.withStyle(ChatFormatting.GRAY);
    tooltip.add(t);
    //    }
  }

  @Override
  public InteractionResult use(Level world, Player player, InteractionHand hand) {
    ItemStack stack = player.getMainHandItem();
    RelativeShape shape = RelativeShape.read(stack);
    if (shape != null) {
      BlockState targetState = BuilderActionType.getBlockState(world, stack);
      if (targetState != null) {
        final BlockPos centerPos = player.blockPosition();
        //        Direction side = context.getFace(); 
        BlockPos posBuild = null;
        for (BlockPos s : shape.getShape()) {
          posBuild = centerPos.offset(s);
          if (world.isOutsideBuildHeight(posBuild) || !world.isEmptyBlock(posBuild)) {
            //if outside, or not air, then continue
            continue;
          }
          int slot = -1;
          if (!player.isCreative()) {
            //not creative
            slot = PlayerUtil.getFirstSlotWithBlock(player, targetState);
            if (slot < 0) {
              //cannot find material
              ChatUtil.sendStatusMessage(player, "item.cyclic.shape_data.empty");
              break; //stop looping
            }
          }
          if (world.setBlock(posBuild, targetState, 1)) {
            PlayerUtil.decrStackSize(player, slot);
          }
        }
      }
      else { // no state selected
        ChatUtil.sendStatusMessage(player, "item.cyclic.shape_data.state");
      }
    }
    else {
      ChatUtil.sendStatusMessage(player, "item.cyclic.shape_data.nothing");
    }
    player.swing(hand);
    return super.use(world, player, hand);
  }
}
