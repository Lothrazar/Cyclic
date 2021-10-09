package com.lothrazar.cyclic.item.datacard;

import java.util.List;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.data.RelativeShape;
import com.lothrazar.cyclic.item.builder.BuilderActionType;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ShapeCard extends ItemBase {

  public static final String VALID_SHAPE = "cyclic-shape";

  public ShapeCard(Properties properties) {
    super(properties);
  }

  public static void setBlockState(ItemStack wand, BlockState target) {
    CompoundTag encoded = NbtUtils.writeBlockState(target);
    wand.getOrCreateTag().put(BuilderActionType.NBTBLOCKSTATE, encoded);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    RelativeShape shape = RelativeShape.read(stack);
    if (shape != null) {
      TranslatableComponent t = new TranslatableComponent(getDescriptionId() + ".count");
      t.append(shape.getCount() + "");
      tooltip.add(t);
      BlockState target = BuilderActionType.getBlockState(stack);
      String block = "scepter.cyclic.nothing";
      if (target != null) {
        block = target.getBlock().getDescriptionId();
      }
      tooltip.add(new TranslatableComponent(ChatFormatting.AQUA + UtilChat.lang(block)));
      if (flagIn.isAdvanced()) {
        //        String side = "S: " + dim.getSide().toString().toUpperCase();
        //        tooltip.add(new TranslationTextComponent(side));
        //        String sideF = "F: " + dim.getSidePlayerFacing().toString().toUpperCase();
        //        tooltip.add(new TranslationTextComponent(sideF));
        //        tooltip.add(new TranslationTextComponent("H: " + dim.getHitVec().toString()));
      }
    }
    TranslatableComponent t = new TranslatableComponent(getDescriptionId() + ".tooltip");
    t.withStyle(ChatFormatting.GRAY);
    tooltip.add(t);
    //    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    RelativeShape shape = RelativeShape.read(stack);
    if (shape != null) {
      BlockState targetState = BuilderActionType.getBlockState(stack);
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
            slot = UtilPlayer.getFirstSlotWithBlock(player, targetState);
            if (slot < 0) {
              //cannot find material
              UtilChat.sendStatusMessage(player, "item.cyclic.shape_data.empty");
              break; //stop looping
            }
          }
          if (world.setBlock(posBuild, targetState, 1)) {
            UtilPlayer.decrStackSize(player, slot);
          }
        }
      }
      else { // no state selected
        UtilChat.sendStatusMessage(player, "item.cyclic.shape_data.state");
      }
    }
    else {
      UtilChat.sendStatusMessage(player, "item.cyclic.shape_data.nothing");
    }
    player.swing(hand);
    return super.use(world, player, hand);
  }
}
