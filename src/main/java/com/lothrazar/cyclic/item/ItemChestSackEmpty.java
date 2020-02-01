/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclic.item;

import java.util.List;
import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.net.PacketChestSack;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilString;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemChestSackEmpty extends ItemBase {

  private static List<String> blacklistAll;

  public ItemChestSackEmpty(Properties prop) {
    super(prop);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    //
    //    ItemStack stack = context.getItem();
    BlockPos pos = context.getPos();
    //    Direction side = context.getFace();
    World world = context.getWorld();
    TileEntity tile = world.getTileEntity(pos);
    BlockState state = world.getBlockState(pos);
    if (state == null || tile == null || state.getBlock() == null
        || state.getBlock().getRegistryName() == null) {//so it works on EXU2 machines  || tile instanceof IInventory == false
      UtilChat.sendStatusMessage(player, "chest_sack.error.null");
      return ActionResultType.FAIL;
    }
    ResourceLocation blockId = state.getBlock().getRegistryName();
    if (UtilString.isInList(blacklistAll, blockId)) {
      UtilChat.sendStatusMessage(player, "chest_sack.error.blacklist");
      return ActionResultType.FAIL;
    }
    UtilSound.playSound(player, pos, SoundEvents.BLOCK_ANVIL_HIT);//SoundRegistry.chest_sack_capture);
    if (world.isRemote) {
      PacketRegistry.INSTANCE.sendToServer(new PacketChestSack(pos));
    }
    return ActionResultType.SUCCESS;
  }
  //  @Override
  //  public IRecipe addRecipe() {
  //    RecipeRegistry.addShapedRecipe(new ItemStack(this),
  //        " s ",
  //        "lbl",
  //        "lll",
  //        'l', "leather",
  //        'b', "slimeball",
  //        's', "string");
  //    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
  //        " s ",
  //        "lbl",
  //        "lll",
  //        'l', "leather",
  //        'b', new ItemStack(Items.APPLE),
  //        's', "string");
  //  }

  public static void gatherTileEntity(BlockPos position, PlayerEntity player, World world, TileEntity tile) {
    if (tile == null) {
      return;
    } //was block destroyed before this packet and/or thread resolved? server desync? who knows https://github.com/PrinceOfAmber/Cyclic/issues/487
    BlockState state = world.getBlockState(position);
    CompoundNBT tileData = new CompoundNBT(); //thanks for the tip on setting tile entity data from nbt tag: https://github.com/romelo333/notenoughwands1.8.8/blob/master/src/main/java/romelo333/notenoughwands/Items/DisplacementWand.java
    tile.write(tileData);
    CompoundNBT itemData = new CompoundNBT();
    itemData.putString(ItemChestSack.KEY_BLOCKNAME, state.getBlock().getTranslationKey());
    itemData.put(ItemChestSack.KEY_BLOCKTILE, tileData);
    itemData.putString(ItemChestSack.KEY_BLOCKID, state.getBlock().getRegistryName().toString());
    itemData.put(ItemChestSack.KEY_BLOCKSTATE, NBTUtil.writeBlockState(state));
    Hand hand = Hand.MAIN_HAND;
    ItemStack held = player.getHeldItem(hand);
    if (held == null || held.getItem() instanceof ItemChestSackEmpty == false) {
      hand = Hand.OFF_HAND;
      held = player.getHeldItem(hand);
    }
    if (held != null && held.getCount() > 0) { //https://github.com/PrinceOfAmber/Cyclic/issues/181
      if (held.getItem() instanceof ItemChestSackEmpty) {
        //        if (CyclicRegistry.Items.tile_transporter != null) {
        if (!UtilPlaceBlocks.destroyBlock(world, position)) {
          //we failed to break the block
          // try to undo the break if we can
          UtilChat.sendStatusMessage(player, "chest_sack.error.pickup");
          world.setBlockState(position, state);
          return;// and dont drop the full item stack or shrink the empty just end
          //TileEntity tileCopy = world.getTileEntity(position);
          //  if (tileCopy != null) {
          //    tileCopy.readFromNBT(tileData);
          //  } 
        }
        ItemStack drop = new ItemStack(CyclicRegistry.Items.tile_transporter);
        drop.setTag(itemData);
        UtilItemStack.drop(world, player.getPosition(), drop);
        if (player.isCreative() == false && held.getCount() > 0) {
          held.shrink(1);
          if (held.getCount() == 0) {
            held = ItemStack.EMPTY;
            player.setHeldItem(hand, ItemStack.EMPTY);
          }
        }
      }
    }
  }
  //
  //  @Override
  //  public void syncConfig(Configuration config) {
  //    String category = Const.ConfigCategory.modpackMisc;
  //    String[] deflist = new String[] {
  //        "extracells:fluidcrafter",
  //        "extracells:ecbaseblock",
  //        "extracells:fluidfiller",
  //        "refinedstorage:disk_drive",
  //        "parabox:parabox",
  //        "immersivengineering:metal_device1"
  //    };
  //    String[] blacklist = config.getStringList("SackHoldingBlacklist",
  //        category, deflist, "Containers that cannot be lifted up with the Empty Sack of Holding.  Use block id; for example minecraft:chest");
  //    blacklistAll = NonNullList.from("",
  //        blacklist);
  //  }
}
