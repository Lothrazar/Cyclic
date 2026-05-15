package com.lothrazar.cyclic.item.datacard;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.component.CustomData;

public class BlockstateCard extends ItemBaseCyclic {

  private static final String EXACT_TAG = "doExactState";
  private static final String STATESTAG = "states";
  private static final int MAXSIZE = 18;

  public BlockstateCard(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack held, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag() != null && held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(STATESTAG)) {
      for (BlockStateMatcher m : getSavedStates(worldIn, held)) {
        BlockState st = m.getState();
        ChatFormatting c = m.isExactProperties() ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.DARK_PURPLE;
        String extra = m.isExactProperties() ? " [state]" : " [block]"; // star for not exact
        tooltip.add(Component.translatable(st.getBlock().getDescriptionId()).append(extra).withStyle(c));
        if (m.isExactProperties() && Screen.hasShiftDown()) {
          tooltip.add(Component.translatable(st.toString()).withStyle(ChatFormatting.DARK_GRAY));
        }
      }
    }
    else {
      super.appendHoverText(held, worldIn, tooltip, flagIn);
    }
  }

  public static List<BlockStateMatcher> getSavedStates(Item.TooltipContext worldIn, ItemStack held) {
    List<BlockStateMatcher> st = new ArrayList<>();
    if (held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag() != null && held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(STATESTAG)) {
      //get it
      ListTag stateTags = held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getList(STATESTAG, 10);
      for (int i = 0; i < stateTags.size(); ++i) {
        CompoundTag currTag = stateTags.getCompound(i);
        BlockState stateFound = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), currTag);
        if (stateFound != null && !stateFound.isAir()) {
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

  public static boolean propertiesMatch(BlockState targetState, BlockState st) {
    try {
      for (Property<?> p : st.getProperties()) {
        if (!st.getValue(p).equals(targetState.getValue(p))) {
          return false;
        }
      }
    }
    catch (Exception e) {
      return false;
    }
    //none had a mismatch
    return true;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    InteractionHand hand = context.getHand();
    BlockPos pos = context.getClickedPos();
    ItemStack held = player.getMainHandItem();
    BlockState state = context.getLevel().getBlockState(pos);
    CompoundTag stateTag = NbtUtils.writeBlockState(state);
    ListTag stateTags = null;
    if (held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(STATESTAG)) {
      //get it
      stateTags = held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getList(STATESTAG, 10);
    }
    else {
      stateTags = new ListTag();
    }
    if (stateTags.size() >= MAXSIZE) {
      return InteractionResult.PASS;
    }
    //wait wait wait does it exist
    for (int i = 0; i < stateTags.size(); ++i) {
      BlockState stateFound = NbtUtils.readBlockState(player.level().holderLookup(Registries.BLOCK), stateTags.getCompound(i));
      if (stateFound.equals(state)) {
        return InteractionResult.PASS;
      }
    }
    //not crouching: default and match exact
    //is crouching: do exact state is false, do only block
    stateTag.putBoolean(EXACT_TAG, !player.isCrouching());
    stateTags.add(stateTag);
    held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put(STATESTAG, stateTags);
    player.swing(hand);
    return InteractionResult.SUCCESS;
  }
}
