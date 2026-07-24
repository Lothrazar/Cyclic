package com.lothrazar.cyclic.item.datacard;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class SettingsCard extends ItemBaseCyclic {

  private static final String NBT_ID = "id";
  private static final String NBT_SETSAVED = "settingsSaved";

  public SettingsCard(Properties properties) {
    super(properties);
  }

  @Override
//  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Item.TooltipContext  worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    CompoundTag stackdata = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    if (stackdata.contains(NBT_ID)) {
      String tiledataID = stackdata.getStringOr(NBT_ID, "");
      MutableComponent t = Component.translatable("[" + tiledataID + "]");
      t.withStyle(ChatFormatting.DARK_GRAY);
      tooltip.add(t);
    }
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    InteractionHand hand = context.getHand();
    BlockPos pos = context.getClickedPos();
    //    Direction side = context.getFace();
    ItemStack held = player.getMainHandItem();
    player.swing(hand);
    BlockEntity tile = player.level().getBlockEntity(pos);
    //am i doing a READ or a WRITE
    if (player.level().getBlockState(pos).getBlock() == Blocks.BEDROCK) {
      //      Blocks.BEDROCK.isu
      held.remove(DataComponents.CUSTOM_DATA); //clear
      ChatUtil.addChatMessage(player, getDescriptionId() + ".deleted");
    }
    //
    CompoundTag stackdata = held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    if (stackdata == null || stackdata.isEmpty()) {
      //do read from tile
      if (tile instanceof TileBlockEntityCyclic) {
        //for now, only do cyclic tile entities
        //in future / intheory could be any TE from any mod / vanilla . but thats broken
        CompoundTag tiledata = tile.saveWithoutMetadata(tile.getLevel().registryAccess());
        //cleanup
        String[] wipers = new String[] { "x", "y", "z", "input", "output", "ForgeData", "ForgeCaps", "inv", "inventory", "energy", "fluid", "timer", "filter" };
        for (String wipe : wipers) {
          tiledata.remove(wipe);
        }
        tiledata.putBoolean(NBT_SETSAVED, true);
        held.set(DataComponents.CUSTOM_DATA, CustomData.of(tiledata));
        ChatUtil.addChatMessage(player, getDescriptionId() + ".savednew");
      }
    }
    else if (stackdata.getBooleanOr(NBT_SETSAVED, false)) {
      //yep put data into tile 
      String stackdataID = stackdata.getStringOr(NBT_ID, "");
      if (tile instanceof TileBlockEntityCyclic) {
        //for now, only do cyclic tile entities
        //WRITE TO TILE from my stackdata
        CompoundTag tiledata = tile.saveWithoutMetadata(tile.getLevel().registryAccess());
        String tiledataID = stackdata.getStringOr(NBT_ID, "");
        //go merge and let it read
        if (tiledataID.equalsIgnoreCase(stackdataID)) {
          stackdata = stackdata.copy();
          stackdata.remove(NBT_SETSAVED);
          stackdata.remove(NBT_ID);
          tiledata = tiledata.merge(stackdata);
          tile.loadCustomOnly(tiledata, player.level().registryAccess());
          ChatUtil.addChatMessage(player, getDescriptionId() + ".written");
        }
      }
    }
    else {
      ModCyclic.LOGGER.error("Invalid data tack settingsSaved : " + stackdata);
    }
    return InteractionResult.SUCCESS;
  }
}
