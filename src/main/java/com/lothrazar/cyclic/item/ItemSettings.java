package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class ItemSettings extends ItemBase {

  private static final String NBT_SETSAVED = "settingsSaved";

  public ItemSettings(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    Hand hand = context.getHand();
    BlockPos pos = context.getPos();
    Direction side = context.getFace();
    ItemStack held = player.getHeldItem(hand);
    player.swingArm(hand);
    TileEntity tile = player.world.getTileEntity(pos);
    //am i doing a READ or a WRITE
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
        String[] wipers = new String[] { "x", "y", "z", "id", "ForgeData", "ForgeCaps" };
        for (String wipe : wipers) {
          tiledata.remove(wipe);
        }
        tiledata.putBoolean(NBT_SETSAVED, true);
        held.setTag(tiledata);
      }
    }
    else if (stackdata.getBoolean(NBT_SETSAVED)) {
      //yep put data into tile
      ModCyclic.LOGGER.error("VALID data tack settingsSaved : " + stackdata);
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
          stackdata.remove(NBT_SETSAVED);
          stackdata = stackdata.copy();
          stackdata.remove(NBT_SETSAVED);
          stackdata.remove("id");
          tiledata = tiledata.merge(stackdata);
          //
          ModCyclic.LOGGER.error("MERGE data tack settingsSaved : " + tiledata + " -> ");
          //
          //
          tile.read(player.world.getBlockState(pos), tiledata);
        }
      }
    }
    else {
      ModCyclic.LOGGER.error("Invalid data tack settingsSaved : " + stackdata);
    }
    return ActionResultType.SUCCESS;
  }
}
