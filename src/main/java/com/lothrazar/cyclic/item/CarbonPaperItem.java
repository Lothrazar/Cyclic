package com.lothrazar.cyclic.item;

import java.util.List;
import com.lothrazar.cyclic.util.ChatUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CarbonPaperItem extends ItemBaseCyclic {

  public CarbonPaperItem(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (stack.hasTag()) {
      SignBlockEntity fakeSign = new SignBlockEntity(BlockPos.ZERO, Blocks.OAK_SIGN.defaultBlockState());
      fakeSign.load(stack.getTag());
//      tooltip.add(Component.translatable("[" + fakeSign.getColor().getSerializedName() + "]"));
      for (int i = 0; i < SignText.LINES; i++) {
        //        fakeSign.setText(line, p_212365_2_);
        SignText front = fakeSign.getFrontText();
  
        Component t = front.getMessages(false)[i];
        //        t.applyTextStyle(TextFormatting.fromColorIndex(fakeSign.getTextColor().get));
        tooltip.add(t);
      }
    }
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    InteractionHand hand = context.getHand();
    BlockPos pos = context.getClickedPos();
    //test spawn detetc
    //    Direction side = context.getFace();
    ItemStack held = player.getItemInHand(hand);
    BlockEntity tile = context.getLevel().getBlockEntity(pos);
    if (player.level().getBlockState(pos).getBlock() instanceof CauldronBlock) {
      if (held.hasTag()) {
        //clean with cauldron
        held.setTag(null);
        ChatUtil.sendStatusMessage(player, "item.cyclic.carbon_paper.deleted");
        player.swing(hand);
        return InteractionResult.SUCCESS;
      }
    }
    if (tile instanceof SignBlockEntity) {
      //ok, i am a sign
      SignBlockEntity sign = (SignBlockEntity) tile;
      if (held.hasTag()) {
        //write to fake sign to parse nbt internally
        SignBlockEntity fakeSign = new SignBlockEntity(context.getClickedPos(), Blocks.OAK_SIGN.defaultBlockState());
        fakeSign.load(held.getTag());
//        sign.setColor(fakeSign.getColor());
        for (int i = 0; i <= 3; i++) {
          //          UtilChat.addChatMessage(player, fakeSign.getText(i).toString());
           
          sign.getFrontText().setMessage(i, fakeSign.getFrontText().getMessages(false)[i]);
        }
        ChatUtil.sendStatusMessage(player, "item.cyclic.carbon_paper.written");
      }
      else {
        //so it has NO tag right now at all
        //read
        CompoundTag data = sign.serializeNBT();
        held.setTag(data);
        ChatUtil.sendStatusMessage(player, "item.cyclic.carbon_paper.copied");
      }
      player.swing(hand);
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
  }
}
