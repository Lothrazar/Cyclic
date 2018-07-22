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

import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseItem;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import com.lothrazar.cyclicmagic.core.util.UtilString;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class ItemChestSackEmpty extends BaseItem implements IHasRecipe, IHasConfig {

  public static final String name = "chest_sack_empty";
  private static List<String> blacklistAll;

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
    if (state == null || tile == null) {//so it works on EXU2 machines  || tile instanceof IInventory == false
      UtilChat.sendStatusMessage(player, "chest_sack.error.null");
      return EnumActionResult.FAIL;
    }
    ResourceLocation blockId = state.getBlock().getRegistryName();
    //blacklist?
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

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    String[] deflist = new String[] { "extracells:fluidcrafter",
        "extracells:ecbaseblock",
        "extracells:fluidfiller",
        "refinedstorage:disk_drive" };
    String[] blacklist = config.getStringList("SackHoldingBlacklist",
        category, deflist, "Containers that cannot be lifted up with the Empty Sack of Holding.  Use block id; for example minecraft:chest");
    blacklistAll = NonNullList.from("",
        blacklist);
  }
}
