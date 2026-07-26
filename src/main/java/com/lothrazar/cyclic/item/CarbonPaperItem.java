package com.lothrazar.cyclic.item;

import java.util.List;
import java.util.function.Consumer;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.storage.TagValueInput;


public class CarbonPaperItem extends ItemBaseCyclic {

  public CarbonPaperItem(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flagIn) {
    if (stack.has(DataComponents.CUSTOM_DATA)) {
      SignBlockEntity fakeSign = new SignBlockEntity(BlockPos.ZERO, Blocks.OAK_SIGN.defaultBlockState());
      fakeSign.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, Minecraft.getInstance().level.registryAccess(),
          stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag()));
      //      tooltip.accept(Component.translatable("[" + fakeSign.getColor().getSerializedName() + "]"));
      for (int i = 0; i < SignText.LINES; i++) {
        //        fakeSign.setText(line, p_212365_2_);
        SignText front = fakeSign.getFrontText();
        Component t = front.getMessages(false)[i];
        //        t.applyTextStyle(TextFormatting.fromColorIndex(fakeSign.getTextColor().get));
        tooltip.accept(t);
      }
    }
    super.appendHoverText(stack, worldIn, tooltipDisplay, tooltip, flagIn);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    InteractionHand hand = context.getHand();
    BlockPos pos = context.getClickedPos();
    //test spawn detetc
    //    Direction side = context.getFace();
    ItemStack held = player.getMainHandItem();
    BlockEntity tile = context.getLevel().getBlockEntity(pos);
    if (player.level().getBlockState(pos).getBlock() instanceof CauldronBlock) {
      if (held.has(DataComponents.CUSTOM_DATA)) {
        //clean with cauldron
        held.remove(DataComponents.CUSTOM_DATA);
        ChatUtil.sendStatusMessage(player, "item.cyclic.carbon_paper.deleted");
        player.swing(hand);
        return InteractionResult.SUCCESS;
      }
    }
    if (tile instanceof SignBlockEntity) {
      //ok, i am a sign
      SignBlockEntity sign = (SignBlockEntity) tile;
      if (held.has(DataComponents.CUSTOM_DATA)) {
        //write to fake sign to parse nbt internally
        SignBlockEntity fakeSign = new SignBlockEntity(context.getClickedPos(), Blocks.OAK_SIGN.defaultBlockState());
        fakeSign.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, context.getLevel().registryAccess(),
            held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag()));
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
        CompoundTag data = sign.saveWithoutMetadata(context.getLevel().registryAccess());
        CustomData.set(DataComponents.CUSTOM_DATA, held, data);
        ChatUtil.sendStatusMessage(player, "item.cyclic.carbon_paper.copied");
      }
      player.swing(hand);
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
  }
}
