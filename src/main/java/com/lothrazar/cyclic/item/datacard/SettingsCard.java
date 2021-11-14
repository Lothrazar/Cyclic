package com.lothrazar.cyclic.item.datacard;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.util.UtilChat;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SettingsCard extends ItemBase {

  private static final String NBT_SETSAVED = "settingsSaved";

  public SettingsCard(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    CompoundNBT stackdata = stack.getOrCreateTag();
    if (stackdata.contains("id")) {
      String tiledataID = stackdata.getString("id");
      TranslationTextComponent t = new TranslationTextComponent("[" + tiledataID + "]");
      t.mergeStyle(TextFormatting.DARK_GRAY);
      tooltip.add(t);
    }
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    Hand hand = context.getHand();
    BlockPos pos = context.getPos();
    //    Direction side = context.getFace();
    ItemStack held = player.getHeldItem(hand);
    player.swingArm(hand);
    TileEntity tile = player.world.getTileEntity(pos);
    //am i doing a READ or a WRITE
    if (player.world.getBlockState(pos).getBlock() == Blocks.BEDROCK) {
      //      Blocks.BEDROCK.isu
      held.setTag(null); //clear
      UtilChat.addChatMessage(player, getTranslationKey() + ".deleted");
    }
    //
    CompoundNBT stackdata = held.getOrCreateTag();
    if (stackdata == null || stackdata.isEmpty()) {
      //do read from tile
      if (tile instanceof TileEntityBase) {
        //for now, only do cyclic tile entities
        //in future / intheory could be any TE from any mod / vanilla . but thats broken
        CompoundNBT tiledata = new CompoundNBT();
        //generic style
        tile.write(tiledata);
        String[] wipers = new String[] { "x", "y", "z", "ForgeData", "ForgeCaps", "inv", "inventory", "energy", "fluid", "timer", "filter" };
        for (String wipe : wipers) {
          tiledata.remove(wipe);
        }
        tiledata.putBoolean(NBT_SETSAVED, true);
        held.setTag(tiledata);
        UtilChat.addChatMessage(player, getTranslationKey() + ".savednew");
      }
    }
    else if (stackdata.getBoolean(NBT_SETSAVED)) {
      //yep put data into tile 
      String stackdataID = stackdata.getString("id");
      if (tile instanceof TileEntityBase) {
        //for now, only do cyclic tile entities
        //WRITE TO TILE from my stackdata
        CompoundNBT tiledata = new CompoundNBT();
        //generic style
        tiledata = tile.write(tiledata);
        String tiledataID = stackdata.getString("id");
        //go merge and let it read
        if (tiledataID.equalsIgnoreCase(stackdataID)) {
          stackdata = stackdata.copy();
          stackdata.remove(NBT_SETSAVED);
          stackdata.remove("id");
          tiledata = tiledata.merge(stackdata);
          //
          tile.read(player.world.getBlockState(pos), tiledata);
          UtilChat.addChatMessage(player, getTranslationKey() + ".written");
        }
      }
    }
    else {
      ModCyclic.LOGGER.error("Invalid data tack settingsSaved : " + stackdata);
    }
    return ActionResultType.SUCCESS;
  }
}
