package com.lothrazar.cyclic.item.datacard;

import com.lothrazar.cyclic.base.ItemBase;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockstateCard extends ItemBase {

  private static final String EXACT_TAG = "doExactState";
  private static final String STATESTAG = "states";
  private static final int MAXSIZE = 18;

  public BlockstateCard(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack held, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (held.getTag() != null && held.getTag().contains(STATESTAG)) {
      for (BlockStateMatcher m : getSavedStates(held)) {
        BlockState st = m.getState();
        TextFormatting c = m.isExactProperties() ? TextFormatting.LIGHT_PURPLE : TextFormatting.DARK_PURPLE;
        String extra = m.isExactProperties() ? " [state]" : " [block]"; // star for not exact
        tooltip.add(new TranslationTextComponent(st.getBlock().getTranslationKey()).appendString(extra).mergeStyle(c));
        if (m.isExactProperties() && Screen.hasShiftDown()) {
          tooltip.add(new TranslationTextComponent(st.toString()).mergeStyle(TextFormatting.DARK_GRAY));
        }
      }
    }
    else {
      super.addInformation(held, worldIn, tooltip, flagIn);
    }
  }

  public static List<BlockStateMatcher> getSavedStates(ItemStack held) {
    List<BlockStateMatcher> st = new ArrayList<>();
    if (held.getTag() != null && held.getTag().contains(STATESTAG)) {
      //get it
      ListNBT stateTags = held.getTag().getList(STATESTAG, 10);
      for (int i = 0; i < stateTags.size(); ++i) {
        CompoundNBT currTag = stateTags.getCompound(i);
        BlockState stateFound = NBTUtil.readBlockState(currTag);
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
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    Hand hand = context.getHand();
    BlockPos pos = context.getPos();
    //    Direction side = context.getFace();
    ItemStack held = player.getHeldItem(hand);
    BlockState state = context.getWorld().getBlockState(pos);
    CompoundNBT stateTag = NBTUtil.writeBlockState(state);
    ListNBT stateTags = null;
    if (held.getOrCreateTag().contains(STATESTAG)) {
      //get it
      stateTags = held.getOrCreateTag().getList(STATESTAG, 10);
    }
    else {
      stateTags = new ListNBT();
    }
    if (stateTags.size() >= MAXSIZE) {
      return ActionResultType.PASS;
    }
    //wait wait wait does it exist
    for (int i = 0; i < stateTags.size(); ++i) {
      BlockState stateFound = NBTUtil.readBlockState(stateTags.getCompound(i));
      if (stateFound.equals(state)) {
        return ActionResultType.PASS;
      }
    }
    //not crouching: default and match exact
    //is crouching: do exact state is false, do only block
    stateTag.putBoolean(EXACT_TAG, !player.isCrouching());
    stateTags.add(stateTag);
    held.getOrCreateTag().put(STATESTAG, stateTags);
    player.swingArm(hand);
    return ActionResultType.SUCCESS;
  }
}
