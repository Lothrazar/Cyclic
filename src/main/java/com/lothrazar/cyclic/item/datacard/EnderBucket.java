package com.lothrazar.cyclic.item.datacard;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.library.core.BlockPosDim;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.LevelWorldUtil;
import com.lothrazar.library.util.TagDataUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderBucket extends ItemBaseCyclic {

  private static final String NBT_SIDE = "side";
  private static final String NBT_DIM = "dim";

  public EnderBucket(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    //    BlockPosDim dim = getPosition(stack);
    //    if (dim != null) {
    //      tooltip.add(new TranslatableComponent(dim.toString()).withStyle(ChatFormatting.GRAY));
    //      if (flagIn.isAdvanced() || Screen.hasShiftDown()) {
    //        String side = "S: " + dim.getSide().toString().toUpperCase();
    //        tooltip.add(new TranslatableComponent(side).withStyle(ChatFormatting.GRAY));
    //        String sideF = "F: " + dim.getSidePlayerFacing().toString().toUpperCase();
    //        tooltip.add(new TranslatableComponent(sideF).withStyle(ChatFormatting.GRAY));
    //        tooltip.add(new TranslatableComponent("H: " + dim.getHitVec().toString()).withStyle(ChatFormatting.GRAY));
    //      }
    //    }
    //    else {
    //      TranslatableComponent t = new TranslatableComponent(getDescriptionId() + ".tooltip");
    //      t.withStyle(ChatFormatting.GRAY);
    //      tooltip.add(t);
    //    }
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    InteractionHand hand = context.getHand();
    BlockPos pos = context.getClickedPos();
    Direction side = context.getClickedFace();
    ItemStack held = player.getItemInHand(hand);
    player.swing(hand);
    TagDataUtil.setItemStackBlockPos(held, pos);
    held.getOrCreateTag().putString(NBT_DIM, LevelWorldUtil.dimensionToString(player.level()));
    TagDataUtil.setItemStackNBTVal(held, NBT_SIDE, side.ordinal());
    TagDataUtil.setItemStackNBTVal(held, NBT_SIDE + "facing", player.getDirection().ordinal());
    ChatUtil.sendStatusMessage(player, ChatUtil.lang("item.location.saved")
        + ChatUtil.blockPosToString(pos));
    // fl
    Vec3 vec = context.getClickLocation();
    held.getOrCreateTag().putDouble("hitx", vec.x - pos.getX());
    held.getOrCreateTag().putDouble("hity", vec.y - pos.getY());
    held.getOrCreateTag().putDouble("hitz", vec.z - pos.getZ());
    return InteractionResult.SUCCESS;
    //this.write 
  }

  public static BlockPosDim getPosition(ItemStack item) {
    BlockPos pos = TagDataUtil.getItemStackBlockPos(item);
    if (pos == null) {
      return null;
    }
    //    this.read 
    CompoundTag tag = item.getOrCreateTag();
    item.getHoverName();
    BlockPosDim dim = new BlockPosDim(pos, tag.getString(NBT_DIM), tag);
    try {
      dim.setSidePlayerFacing(Direction.values()[tag.getInt(NBT_SIDE + "facing")]);
      dim.setSide(Direction.values()[tag.getInt(NBT_SIDE)]);
      Vec3 vec = new Vec3(
          tag.getDouble("hitx"),
          tag.getDouble("hity"),
          tag.getDouble("hitz"));
      dim.setHitVec(vec);
    }
    catch (Throwable e) {
      ModCyclic.LOGGER.error("SIde error in GPS", e);
    }
    return dim;
  }
}
