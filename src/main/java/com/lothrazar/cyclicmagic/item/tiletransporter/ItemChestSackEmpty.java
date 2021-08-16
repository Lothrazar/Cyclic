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
package com.lothrazar.cyclicmagic.item.tiletransporter;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.item.core.BaseItem;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ItemChestSackEmpty extends BaseItem implements IHasRecipe, IHasConfig {

  public static final String name = "chest_sack_empty";
  private static List<ResourceLocation> blacklistAll;

  public ItemChestSackEmpty() {
    super();
    // imported from my old mod
    // https://github.com/PrinceOfAmber/SamsPowerups/blob/b02f6b4243993eb301f4aa2b39984838adf482c1/src/main/java/com/lothrazar/samscontent/item/ItemChestSack.java
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (pos == null) {
      return EnumActionResult.FAIL;
    }
    TileEntity tile = world.getTileEntity(pos);
    IBlockState state = world.getBlockState(pos);
    if (state == null || tile == null || state.getBlock() == null
        || state.getBlock().getRegistryName() == null) {//so it works on EXU2 machines  || tile instanceof IInventory == false
      UtilChat.sendStatusMessage(player, "chest_sack.error.null");
      return EnumActionResult.FAIL;
    }
    ResourceLocation blockId = state.getBlock().getRegistryName();
    if (UtilString.isInList(blacklistAll, blockId)) {
      UtilChat.sendStatusMessage(player, "chest_sack.error.blacklist");
      return EnumActionResult.FAIL;
    }
    UtilSound.playSound(player, pos, SoundRegistry.chest_sack_capture);
    if (world.isRemote) {
      ModCyclic.network.sendToServer(new PacketChestSack(pos));// https://github.com/PrinceOfAmber/Cyclic/issues/131
    }
    return EnumActionResult.SUCCESS;
  }

  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " s ",
        "lbl",
        "lll",
        'l', "leather",
        'b', "slimeball",
        's', "string");
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " s ",
        "lbl",
        "lll",
        'l', "leather",
        'b', new ItemStack(Items.APPLE),
        's', "string");
  }

  private Item fullSack;

  public void setFullSack(Item item) {
    fullSack = item;
  }

  public Item getFullSack() {
    return fullSack;
  }

  public static void gatherTileEntity(BlockPos position, EntityPlayer player, World world, TileEntity tile) {
    if (tile == null) {
      return;
    } //was block destroyed before this packet and/or thread resolved? server desync? who knows https://github.com/PrinceOfAmber/Cyclic/issues/487
    IBlockState state = world.getBlockState(position);
    NBTTagCompound tileData = new NBTTagCompound(); //thanks for the tip on setting tile entity data from nbt tag: https://github.com/romelo333/notenoughwands1.8.8/blob/master/src/main/java/romelo333/notenoughwands/Items/DisplacementWand.java
    tile.writeToNBT(tileData);
    NBTTagCompound itemData = new NBTTagCompound();
    itemData.setString(ItemChestSack.KEY_BLOCKNAME, state.getBlock().getTranslationKey());
    itemData.setTag(ItemChestSack.KEY_BLOCKTILE, tileData);
    itemData.setInteger(ItemChestSack.KEY_BLOCKID, Block.getIdFromBlock(state.getBlock()));
    itemData.setInteger(ItemChestSack.KEY_BLOCKSTATE, state.getBlock().getMetaFromState(state));
    EnumHand hand = EnumHand.MAIN_HAND;
    ItemStack held = player.getHeldItem(hand);
    if (held == null || held.getItem() instanceof ItemChestSackEmpty == false) {
      hand = EnumHand.OFF_HAND;
      held = player.getHeldItem(hand);
    }
    if (held != null && held.getCount() > 0) { //https://github.com/PrinceOfAmber/Cyclic/issues/181
      if (held.getItem() instanceof ItemChestSackEmpty) {
        Item chest_sack = ((ItemChestSackEmpty) held.getItem()).getFullSack();
        if (chest_sack != null) {
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
          ItemStack drop = new ItemStack(chest_sack);
          drop.setTagCompound(itemData);
          UtilItemStack.dropItemStackInWorld(world, player.getPosition(), drop);
          if (player.capabilities.isCreativeMode == false && held.getCount() > 0) {
            held.shrink(1);
            if (held.getCount() == 0) {
              held = ItemStack.EMPTY;
              player.setHeldItem(hand, ItemStack.EMPTY);
            }
          }
        }
      }
    }
  }

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    String[] deflist = new String[] {
        "extracells:fluidcrafter",
        "extracells:ecbaseblock",
        "extracells:fluidfiller",
        "refinedstorage:disk_manipulator",
        "refinedstorage:disk_drive",
        "refinedstorage:crafter",
        "refinedstorage:crafter_manager",
        "refinedstorage:grid",
        "parabox:parabox",
        "immersivengineering:metal_device1"
    };
    String[] blacklist = config.getStringList("SackHoldingBlacklist",
        category, deflist, "Containers that cannot be lifted up with the Empty Sack of Holding.  Use block id; for example minecraft:chest");
    blacklistAll = NonNullList.from(new ResourceLocation("", ""),
            Arrays.stream(blacklist).map(s -> {
              String[] split = s.split(":");
              if (split.length < 2) {
                ModCyclic.logger.error("Invalid SackHoldingBlacklist config value for block : " + s);
                return null;
              }
              return new ResourceLocation(split[0], split[1]);
            }).filter(Objects::nonNull).filter(r -> !r.getPath().isEmpty()).toArray(ResourceLocation[]::new)
    );
  }
}
