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
package com.lothrazar.cyclicmagic.component.wireless;
import java.util.Random;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRedstoneWireless extends BlockBaseHasTile implements IHasRecipe {
  private static final int PARTICLE_DENSITY = 2;
  public static final PropertyBool POWERED = net.minecraft.block.BlockLever.POWERED;
  public static enum WirelessType {
    TRANSMITTER, RECEIVER;
  }
  WirelessType type;
  public BlockRedstoneWireless(WirelessType t) {
    super(Material.IRON);
    type = t;
  }
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, POWERED);
  }
  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(POWERED, false);
  }
  @Override
  public boolean canProvidePower(IBlockState state) {
    return true;
  }
  @Override
  public int getMetaFromState(IBlockState state) {
    return (state.getValue(POWERED) ? 1 : 0);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    if (type == WirelessType.TRANSMITTER)
      return new TileEntityWirelessTr();
    else
      return new TileEntityWirelessRec();
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (stateIn.getValue(POWERED)) {
      UtilParticle.spawnParticle(worldIn, EnumParticleTypes.REDSTONE, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, PARTICLE_DENSITY);
    }
  }
  private int getPower(IBlockAccess world, BlockPos pos, EnumFacing side) {
    if (world.getTileEntity(pos) instanceof TileEntityWirelessRec) {
      return 15;
    }
    return 0;
  }
  @Override
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockState.getValue(POWERED) ? getPower(blockAccess, pos, side.getOpposite()) : 0;
  }
  @Override
  public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockState.getValue(POWERED) ? getPower(blockAccess, pos, side.getOpposite()) : 0;
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (world.getTileEntity(pos) instanceof TileEntityWirelessTr) {
      TileEntityWirelessTr te = (TileEntityWirelessTr) world.getTileEntity(pos);
      if (te.getTargetPos() == null) {
        UtilChat.sendStatusMessage(player, "tile.wireless_transmitter.empty");
      }
      else {
        UtilChat.sendStatusMessage(player, UtilChat.lang("tile.wireless_transmitter.saved") + UtilChat.blockPosToString(te.getTargetPos()));
      }
    }
    return true;
  }
  @Override
  public IRecipe addRecipe() {
    if (this.type == WirelessType.TRANSMITTER)
      return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
          "isi",
          "sqs",
          "isi",
          'i', "ingotIron",
          's', "stone",
          'q', Items.REPEATER);
    else
      return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
          "isi",
          "sqs",
          "isi",
          'i', "ingotIron",
          's', "stone",
          'q', Blocks.REDSTONE_TORCH);
  }
  @SubscribeEvent
  public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
    BlockPos pos = event.getPos();
    World world = event.getWorld();
    ItemStack stack = event.getItemStack();//player held item
    if (world.getTileEntity(pos) instanceof TileEntityWirelessRec
        && stack.getItem() == Item.getByNameOrId("cyclicmagic:wireless_transmitter")) {
      UtilNBT.setItemStackBlockPos(stack, pos);
      UtilChat.sendStatusMessage(event.getEntityPlayer(), UtilChat.lang("tile.wireless_transmitter.saved") + UtilChat.blockPosToString(pos));
    }
  }
  /**
   * item stack data pushed into tile entity
   */
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    stack.getItem().updateItemStackNBT(stack.getTagCompound());
    TileEntity tile = worldIn.getTileEntity(pos);
    BlockPos posTarget = UtilNBT.getItemStackBlockPos(stack);
    if (tile != null && posTarget != null && tile instanceof TileEntityWirelessTr) {
      ((TileEntityWirelessTr) tile).setTargetPos(posTarget);
    }
  }
  /**
   * tile entity data saved to item stack
   * 
   * @param event
   */
  @SubscribeEvent
  public static void onBreakEvent(BreakEvent event) {
    if (event.getPlayer() != null && event.getPlayer().capabilities.isCreativeMode) {
      return;
    } // dont drop in creative https://github.com/PrinceOfAmber/Cyclic/issues/93
    World world = event.getWorld();
    BlockPos pos = event.getPos();
    IBlockState state = event.getState();
    TileEntity ent = world.getTileEntity(pos);
    if (ent != null && ent instanceof TileEntityWirelessTr) {
      TileEntityWirelessTr t = (TileEntityWirelessTr) ent;
      ItemStack stack = new ItemStack(state.getBlock());
      if (t.getTargetPos() != null)
        UtilNBT.setItemStackBlockPos(stack, t.getTargetPos());
      //      saveTileDataToStack(stack, t);
      UtilItemStack.dropItemStackInWorld(world, pos, stack);
    }
  }
  //disable regular drops, make my own drop that saves nbt
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return null;
  }
}
