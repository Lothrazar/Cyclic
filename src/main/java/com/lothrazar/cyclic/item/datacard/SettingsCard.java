package com.lothrazar.cyclic.item.datacard;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.util.UtilChat;
import java.util.List;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.item.Item.Properties;

public class SettingsCard extends ItemBase {

  private static final String NBT_SETSAVED = "settingsSaved";

  public SettingsCard(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    CompoundTag stackdata = stack.getOrCreateTag();
    if (stackdata.contains("id")) {
      String tiledataID = stackdata.getString("id");
      TranslatableComponent t = new TranslatableComponent("[" + tiledataID + "]");
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
    ItemStack held = player.getItemInHand(hand);
    player.swing(hand);
    BlockEntity tile = player.level.getBlockEntity(pos);
    //am i doing a READ or a WRITE
    if (player.level.getBlockState(pos).getBlock() == Blocks.BEDROCK) {
      //      Blocks.BEDROCK.isu
      held.setTag(null); //clear
      UtilChat.addChatMessage(player, getDescriptionId() + ".deleted");
    }
    //
    CompoundTag stackdata = held.getOrCreateTag();
    if (stackdata == null || stackdata.isEmpty()) {
      //do read from tile
      if (tile instanceof TileEntityBase) {
        //for now, only do cyclic tile entities
        //in future / intheory could be any TE from any mod / vanilla . but thats broken
        CompoundTag tiledata = new CompoundTag();
        //generic style
        tile.save(tiledata);
        String[] wipers = new String[] { "x", "y", "z", "ForgeData", "ForgeCaps", "inv", "inventory", "energy", "fluid", "timer" };
        for (String wipe : wipers) {
          tiledata.remove(wipe);
        }
        tiledata.putBoolean(NBT_SETSAVED, true);
        held.setTag(tiledata);
        UtilChat.addChatMessage(player, getDescriptionId() + ".savednew");
      }
    }
    else if (stackdata.getBoolean(NBT_SETSAVED)) {
      //yep put data into tile
      ModCyclic.LOGGER.error("VALID data tack settingsSaved : " + stackdata);
      String stackdataID = stackdata.getString("id");
      if (tile instanceof TileEntityBase) {
        //for now, only do cyclic tile entities
        //WRITE TO TILE from my stackdata
        CompoundTag tiledata = new CompoundTag();
        //generic style
        tiledata = tile.save(tiledata);
        String tiledataID = stackdata.getString("id");
        //go merge and let it read
        if (tiledataID.equalsIgnoreCase(stackdataID)) {
          stackdata = stackdata.copy();
          stackdata.remove(NBT_SETSAVED);
          stackdata.remove("id");
          tiledata = tiledata.merge(stackdata);
          //
          ModCyclic.LOGGER.error("MERGE data tack settingsSaved : " + tiledata + " -> ");
          //
          //
          tile.load(player.level.getBlockState(pos), tiledata);
          UtilChat.addChatMessage(player, getDescriptionId() + ".written");
        }
      }
    }
    else {
      ModCyclic.LOGGER.error("Invalid data tack settingsSaved : " + stackdata);
    }
    return InteractionResult.SUCCESS;
  }
}
