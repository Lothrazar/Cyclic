package com.lothrazar.cyclic.item;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilNBT;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemLocationGps extends ItemBase {

  private static final String NBT_SIDE = "side";
  private static final String NBT_DIM = "dim";

  public ItemLocationGps(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    BlockPosDim dim = getPosition(stack);
    if (dim != null) {
      tooltip.add(new TranslationTextComponent(dim.toString()));
      if (flagIn.isAdvanced()) {
        String side = "S: " + dim.getSide().toString().toUpperCase();
        tooltip.add(new TranslationTextComponent(side));
        String sideF = "F: " + dim.getSidePlayerFacing().toString().toUpperCase();
        tooltip.add(new TranslationTextComponent(sideF));
        tooltip.add(new TranslationTextComponent("H: " + dim.getHitVec().toString()));
      }
    }
    else {
      TranslationTextComponent t = new TranslationTextComponent(getTranslationKey() + ".tooltip");
      t.applyTextStyle(TextFormatting.GRAY);
      tooltip.add(t);
    }
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    Hand hand = context.getHand();
    BlockPos pos = context.getPos();
    Direction side = context.getFace();
    ItemStack held = player.getHeldItem(hand);
    player.swingArm(hand);
    UtilNBT.setItemStackBlockPos(held, pos);
    UtilNBT.setItemStackNBTVal(held, NBT_DIM, player.dimension.getId());
    UtilNBT.setItemStackNBTVal(held, NBT_SIDE, side.ordinal());
    UtilNBT.setItemStackNBTVal(held, NBT_SIDE + "facing", player.getHorizontalFacing().ordinal());
    UtilChat.sendStatusMessage(player, UtilChat.lang("item.location.saved")
        + UtilChat.blockPosToString(pos));
    //
    System.out.println("the hit " + context.getHitVec());
    Vec3d vec = context.getHitVec();
    held.getOrCreateTag().putDouble("hitx", vec.x - pos.getX());
    held.getOrCreateTag().putDouble("hity", vec.y - pos.getY());
    held.getOrCreateTag().putDouble("hitz", vec.z - pos.getZ());
    return ActionResultType.SUCCESS;
  }

  public static BlockPosDim getPosition(ItemStack item) {
    BlockPos pos = UtilNBT.getItemStackBlockPos(item);
    if (pos == null) {
      return null;
    }
    CompoundNBT tag = item.getOrCreateTag();
    BlockPosDim dim = new BlockPosDim(pos, tag.getInt(NBT_DIM));
    try {
      dim.setSidePlayerFacing(Direction.values()[tag.getInt(NBT_SIDE + "facing")]);
      dim.setSide(Direction.values()[tag.getInt(NBT_SIDE)]);
      Vec3d vec = new Vec3d(
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
