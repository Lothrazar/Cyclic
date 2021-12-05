package com.lothrazar.cyclic.item.datacard;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockstateCard extends ItemBaseCyclic {

  private static final String EXACT_TAG = "doExactState";
  private static final String STATESTAG = "states";
  private static final int MAXSIZE = 18;

  public BlockstateCard(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack held, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (held.getTag() != null && held.getTag().contains(STATESTAG)) {
      for (BlockStateMatcher m : getSavedStates(held)) {
        BlockState st = m.getState();
        ChatFormatting c = m.isExactProperties() ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.DARK_PURPLE;
        String extra = m.isExactProperties() ? " [state]" : " [block]"; // star for not exact
        tooltip.add(new TranslatableComponent(st.getBlock().getDescriptionId()).append(extra).withStyle(c));
        if (m.isExactProperties() && Screen.hasShiftDown()) {
          tooltip.add(new TranslatableComponent(st.toString()).withStyle(ChatFormatting.DARK_GRAY));
        }
      }
    }
    else {
      super.appendHoverText(held, worldIn, tooltip, flagIn);
    }
  }

  public static List<BlockStateMatcher> getSavedStates(ItemStack held) {
    List<BlockStateMatcher> st = new ArrayList<>();
    if (held.getTag() != null && held.getTag().contains(STATESTAG)) {
      //get it
      ListTag stateTags = held.getTag().getList(STATESTAG, 10);
      for (int i = 0; i < stateTags.size(); ++i) {
        CompoundTag currTag = stateTags.getCompound(i);
        BlockState stateFound = NbtUtils.readBlockState(currTag);
        if (stateFound != null && stateFound.getBlock() != Blocks.AIR) {
          BlockStateMatcher matcher = new BlockStateMatcher();
          matcher.setState(stateFound);
          if (currTag.contains(EXACT_TAG)) {
            matcher.setExactProperties(currTag.getBoolean(EXACT_TAG));
          }
          st.add(matcher);
        }
      }
    }
    return st;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    InteractionHand hand = context.getHand();
    BlockPos pos = context.getClickedPos();
    //    Direction side = context.getFace();
    ItemStack held = player.getItemInHand(hand);
    BlockState state = context.getLevel().getBlockState(pos);
    CompoundTag stateTag = NbtUtils.writeBlockState(state);
    ListTag stateTags = null;
    if (held.getOrCreateTag().contains(STATESTAG)) {
      //get it
      stateTags = held.getOrCreateTag().getList(STATESTAG, 10);
    }
    else {
      stateTags = new ListTag();
    }
    if (stateTags.size() >= MAXSIZE) {
      return InteractionResult.PASS;
    }
    //wait wait wait does it exist
    for (int i = 0; i < stateTags.size(); ++i) {
      BlockState stateFound = NbtUtils.readBlockState(stateTags.getCompound(i));
      if (stateFound.equals(state)) {
        return InteractionResult.PASS;
      }
    }
    //not crouching: default and match exact
    //is crouching: do exact state is false, do only block
    stateTag.putBoolean(EXACT_TAG, !player.isCrouching());
    stateTags.add(stateTag);
    held.getOrCreateTag().put(STATESTAG, stateTags);
    player.swing(hand);
    return InteractionResult.SUCCESS;
  }
}
