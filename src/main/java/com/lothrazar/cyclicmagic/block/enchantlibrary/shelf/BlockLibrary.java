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
package com.lothrazar.cyclicmagic.block.enchantlibrary.shelf;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.core.BlockBaseFacing;
import com.lothrazar.cyclicmagic.block.core.IBlockHasTESR;
import com.lothrazar.cyclicmagic.block.enchantlibrary.ctrl.BlockLibraryController;
import com.lothrazar.cyclicmagic.block.enchantlibrary.ctrl.TileEntityLibraryCtrl;
import com.lothrazar.cyclicmagic.data.EnchantStack;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.data.QuadrantEnum;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLibrary extends BlockBaseFacing implements IBlockHasTESR, IHasRecipe, IContent {

  public BlockLibrary() {
    super(Material.WOOD);
  }

  @Override
  public void register() {
    BlockRegistry.registerBlock(this, new ItemBlockLibrary(this), getContentName(), GuideCategory.BLOCK);
    BlockRegistry.registerTileEntity(TileEntityLibrary.class, Const.MODID + "library_te");
    BlockRegistry.registerTileEntity(TileEntityLibraryCtrl.class, Const.MODID + "library_ctrl_te");
    BlockLibraryController lc = new BlockLibraryController();
    BlockRegistry.registerBlock(lc, "block_library_ctrl", GuideCategory.BLOCK);
    ModCyclic.instance.events.register(this);
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public String getContentName() {
    return "block_library";
  }

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.content;
    enabled = config.getBoolean(getContentName(), category, true, Const.ConfigCategory.contentDefaultText);
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityLibrary();
  }

  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    // find the quadrant the player is facing
    EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);
    return this.getDefaultState().withProperty(PROPERTYFACING, enumfacing.getOpposite());
  }

  @Override
  public EnumFacing getFacingFromState(IBlockState state) {
    return super.getFacingFromState(state).getOpposite();
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    //hit Y is always vertical. horizontal is either X or Z, and sometimes is inverted
    if (world.getTileEntity(pos) instanceof TileEntityLibrary == false) {
      return false;
    }
    TileEntityLibrary library = (TileEntityLibrary) world.getTileEntity(pos);
    if (side != this.getFacingFromState(state)) {
      return false;
    }
    QuadrantEnum segment = QuadrantEnum.getForFace(side, hitX, hitY, hitZ);
    if (segment == null) {
      return false;//literal edge case
    }
    library.setLastClicked(segment);
    ItemStack playerHeld = player.getHeldItem(hand);
    // Enchantment enchToRemove = null;
    if (playerHeld.getItem().equals(Items.ENCHANTED_BOOK)) {
      if (playerHeld.getCount() != 1) {
        if (world.isRemote)
          UtilChat.addChatMessage(player, this.getRawName() + ".stacksize");
        return false;
      }
      ItemStack theThing = library.addEnchantmentToQuadrant(playerHeld, segment);
      player.setHeldItem(hand, ItemStack.EMPTY);
      if (theThing.isEmpty() == false) {
        player.addItemStackToInventory(theThing);
      }
      else {
        player.addItemStackToInventory(new ItemStack(Items.BOOK));
      }
      onSuccess(player);
      library.markDirty();
      return true;
    }
    if (playerHeld.getItem().equals(Items.BOOK)
        && player.getCooldownTracker().hasCooldown(Items.BOOK) == false) {
      EnchantStack es = library.getEnchantStack(segment);
      if (es.isEmpty() == false) {
        //also let them know what youre withdrawing. without the counter
        UtilChat.sendStatusMessage(player, UtilChat.lang(es.getEnch().getName()) + " " + es.levelName());
        this.dropEnchantedBookOnPlayer(es, player, pos);
        playerHeld.shrink(1);
        library.removeEnchantment(segment);
        onSuccess(player);
        library.markDirty();
        return true;
      }
    }
    //display information about whats inside ??maybe?? if sneaking
    else if (player.isSneaking() == false && !player.world.isRemote && !playerHeld.isEmpty()) {
      EnchantStack es = library.getEnchantStack(segment);
      UtilChat.sendStatusMessage(player, es.toString());
      library.markDirty();
      return true;
    }
    return false;//so you can still sneak with books or whatever
  }

  private void onSuccess(EntityPlayer player) {
    UtilSound.playSound(player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE);
  }

  private void dropEnchantedBookOnPlayer(EnchantStack ench, EntityPlayer player, BlockPos pos) {
    ItemStack stack = ench.makeEnchantedBook();
    if (player.addItemStackToInventory(stack) == false) {
      //drop if player is full
      player.dropItem(stack, true);
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLibrary.class, new LibraryTESR<TileEntityLibrary>(this));
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 4),
        " r ",
        "sgs",
        " r ",
        'g', Blocks.BOOKSHELF,
        's', Blocks.PURPUR_BLOCK,
        'r', "obsidian");
  }

  //start of 'fixing getDrops to not have null tile entity', using pattern from forge BlockFlowerPot patch
  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
    if (willHarvest) {
      return true;
    } //If it will harvest, delay deletion of the block until after getDrops
    return super.removedByPlayer(state, world, pos, player, willHarvest);
  }

  @Override
  public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack tool) {
    super.harvestBlock(world, player, pos, state, te, tool);
    world.setBlockToAir(pos);
  }
  //end of fixing getdrops

  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    List<ItemStack> ret = new ArrayList<ItemStack>();
    Item item = Item.getItemFromBlock(this);//this.getItemDropped(state, rand, fortune);
    TileEntity ent = world.getTileEntity(pos);
    ItemStack stack = new ItemStack(item);
    if (ent instanceof TileEntityLibrary) {
      TileEntityLibrary lib = (TileEntityLibrary) ent;
      NBTTagCompound newtag = lib.writeToNBTenchants(new NBTTagCompound(), true);
      if (newtag != null) {
        //it returns null if nothing saved
        stack.setTagCompound(newtag);
      }
    }
    ret.add(stack);
    return ret;
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if (stack.getTagCompound() != null && world.getTileEntity(pos) instanceof TileEntityLibrary) {
      NBTTagCompound tags = stack.getTagCompound();
      TileEntityLibrary container = (TileEntityLibrary) world.getTileEntity(pos);
      container.readFromNBTenchants(tags);
    }
  }
}
