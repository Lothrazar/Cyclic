package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilChat;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CarbonPaperItem extends ItemBase {

  public CarbonPaperItem(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (stack.hasTag()) {
      SignTileEntity fakeSign = new SignTileEntity();
      fakeSign.read(Blocks.OAK_SIGN.getDefaultState(), stack.getTag());
      tooltip.add(new TranslationTextComponent("[" + fakeSign.getTextColor().getString() + "]"));
      for (int i = 0; i <= 3; i++) {
        //        fakeSign.setText(line, p_212365_2_);
        ITextComponent t = fakeSign.signText[i];
        //        t.applyTextStyle(TextFormatting.fromColorIndex(fakeSign.getTextColor().get));
        tooltip.add(t);
      }
    }
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    Hand hand = context.getHand();
    BlockPos pos = context.getPos();
    //    Direction side = context.getFace();
    ItemStack held = player.getHeldItem(hand);
    TileEntity tile = context.getWorld().getTileEntity(pos);
    if (player.world.getBlockState(pos).getBlock() instanceof CauldronBlock) {
      if (held.hasTag()) {
        //clean with cauldron
        held.setTag(null);
        UtilChat.sendStatusMessage(player, "item.cyclic.carbon_paper.deleted");
        player.swingArm(hand);
        return ActionResultType.SUCCESS;
      }
    }
    if (tile instanceof SignTileEntity) {
      //ok, i am a sign
      SignTileEntity sign = (SignTileEntity) tile;
      if (held.hasTag()) {
        //write to fake sign to parse nbt internally
        SignTileEntity fakeSign = new SignTileEntity();
        fakeSign.read(Blocks.OAK_SIGN.getDefaultState(), held.getTag());
        sign.setTextColor(fakeSign.getTextColor());
        for (int i = 0; i <= 3; i++) {
          //          UtilChat.addChatMessage(player, fakeSign.getText(i).toString());
          sign.setText(i, fakeSign.signText[i]);
        }
        UtilChat.sendStatusMessage(player, "item.cyclic.carbon_paper.written");
      }
      else {
        //so it has NO tag right now at all
        //read
        CompoundNBT data = new CompoundNBT();
        sign.write(data);
        held.setTag(data);
        UtilChat.sendStatusMessage(player, "item.cyclic.carbon_paper.copied");
      }
      player.swingArm(hand);
      return ActionResultType.SUCCESS;
    }
    return ActionResultType.PASS;
  }
}
