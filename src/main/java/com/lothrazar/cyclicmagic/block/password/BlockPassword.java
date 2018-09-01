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
package com.lothrazar.cyclicmagic.block.password;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.core.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.block.password.TileEntityPassword.UsersAllowed;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPassword extends BlockBaseHasTile implements IHasRecipe, IContent {

  public static final PropertyBool POWERED = PropertyBool.create("powered");

  public BlockPassword() {
    super(Material.ROCK);
    this.setHardness(4F);
    this.setResistance(4F);
    this.setSoundType(SoundType.STONE);
    this.setHarvestLevel("pickaxe", 0);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_PASSWORD);
  }

  @Override
  public void register() {
    BlockRegistry.registerBlock(this, "password_block", GuideCategory.BLOCKMACHINE);
    GameRegistry.registerTileEntity(TileEntityPassword.class, "password_block_te");
    ModCyclic.instance.events.register(this);
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean("PasswordTrigger", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityPassword();
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(POWERED, meta == 1 ? true : false);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return (state.getValue(POWERED)) ? 1 : 0;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { POWERED });
  }

  @Override
  public boolean canProvidePower(IBlockState state) {
    return true;
  }

  @Override
  public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockState.getValue(POWERED) ? 15 : 0;
  }

  @Override
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockState.getValue(POWERED) ? 15 : 0;
  }

  @SubscribeEvent
  public void chatEvent(ServerChatEvent event) {
    World world = event.getPlayer().getEntityWorld();
    //for each loop hits a // oops : java.util.ConcurrentModificationException, so we need iterator
    Iterator<TileEntityPassword> iterator = TileEntityPassword.listeningBlocks.iterator();
    List<TileEntityPassword> toRemove = new ArrayList<TileEntityPassword>();
    //TileEntityPassword current;
    int wasFound = 0;
    while (iterator.hasNext()) {
      TileEntityPassword current = iterator.next();
      if (current.isInvalid() == false) {
        if (current.getMyPassword() != null && current.getMyPassword().length() > 0 && event.getMessage().equals(current.getMyPassword())) {
          boolean isAllowed;
          if (current.getUserPerm() == UsersAllowed.ALL) {//user said everyones allowed
            isAllowed = true;
          }
          else {//it has no claimed user.. OR it is claimed
            isAllowed = !current.isClaimedBySomeone() || current.isClaimedBy(event.getPlayer());//nobody || me
          }
          if (isAllowed) {
            current.onCorrectPassword(world);
            wasFound++;
          }
          //          else {
          //            UtilChat.addChatMessage(event.getPlayer(), UtilChat.lang(this.getUnlocalizedName() + ".notallowed"));
          //          }
        } //else password was wrong
      }
      else {
        toRemove.add(current);///is invalid
      }
    }
    //even with iterator we were getting ConcurrentModificationException on the iterator.next() line
    for (TileEntityPassword rm : toRemove) {
      TileEntityPassword.listeningBlocks.remove(rm);
    }
    if (wasFound > 0) {
      event.setCanceled(true);//If this event is canceled, the chat message is never distributed to all clients.
      if (wasFound == 1)
        UtilChat.addChatMessage(event.getPlayer(), UtilChat.lang(this.getUnlocalizedName() + ".triggered") + event.getMessage());
      else
        UtilChat.addChatMessage(event.getPlayer(), wasFound + " " + UtilChat.lang(this.getUnlocalizedName() + ".triggeredmany") + event.getMessage());
    }
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "sss",
        "trt",
        "sss",
        's', new ItemStack(Blocks.STONE_SLAB, 1, BlockStoneSlab.EnumType.STONE.getMetadata()),
        't', Blocks.TRIPWIRE_HOOK,
        'r', Items.COMPARATOR);
  }
}
