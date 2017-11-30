package com.lothrazar.cyclicmagic.component.fluidtransfer;
import java.util.Map;
import java.util.Map.Entry;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseCable;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class BlockFluidCable extends BlockBaseCable implements IHasRecipe {
  public BlockFluidCable() {
    super(Material.CLAY);
    this.setHardness(.5F);
    this.setResistance(.5F);
  }
  public IBlockState getNewState(IBlockAccess world, BlockPos pos) {
    if (!(world.getTileEntity(pos) instanceof TileEntityFluidCable))
      return world.getBlockState(pos);
    TileEntityFluidCable tile = (TileEntityFluidCable) world.getTileEntity(pos);
    BlockPos con = null;
    Map<EnumFacing, EnumConnectType> oldMap = tile.getConnects();
    Map<EnumFacing, EnumConnectType> newMap = Maps.newHashMap();
    EnumFacing stor = null;
    for (Entry<EnumFacing, EnumConnectType> e : oldMap.entrySet()) {
      if (e.getValue() == EnumConnectType.STORAGE) {
        stor = e.getKey();
        break;
      }
    }
    boolean storage = false;
    boolean first = false;
    for (EnumFacing f : EnumFacing.values()) {
      if (stor == f && first)
        continue;
      EnumConnectType neu = getConnect(world, pos, f);
      if (neu == EnumConnectType.STORAGE) {
        if (!storage) {
          newMap.put(f, neu);
          storage = true;
        }
        else
          newMap.put(f, EnumConnectType.NULL);
      }
      else {
        newMap.put(f, neu);
      }
    }
    tile.setConnects(newMap);
    if (tile.north == EnumConnectType.STORAGE) {
      // face = EnumFacing.NORTH;
      con = pos.north();
    }
    else if (tile.south == EnumConnectType.STORAGE) {
      //  face = EnumFacing.SOUTH;
      con = pos.south();
    }
    else if (tile.east == EnumConnectType.STORAGE) {
      //  face = EnumFacing.EAST;
      con = pos.east();
    }
    else if (tile.west == EnumConnectType.STORAGE) {
      //  face = EnumFacing.WEST;
      con = pos.west();
    }
    else if (tile.down == EnumConnectType.STORAGE) {
      //  face = EnumFacing.DOWN;
      con = pos.down();
    }
    else if (tile.up == EnumConnectType.STORAGE) {
      //  face = EnumFacing.UP;
      con = pos.up();
    }
    //  tile.setConnectedFace(face);
    tile.setConnectedPos(con);
    return world.getBlockState(pos);
  }
  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    try {
      IBlockState foo = getNewState(worldIn, pos);
      return foo;
    }
    catch (Exception e) {
      e.printStackTrace();
      return super.getActualState(state, worldIn, pos);
    }
  }
  protected EnumConnectType getConnect(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    BlockPos offset = pos.offset(side);
    Block block = worldIn.getBlockState(offset).getBlock();
    if (block == this) {
      return EnumConnectType.CONNECT;
    }
    if (FluidUtil.getFluidHandler((World) worldIn, offset, side) != null)
      return EnumConnectType.STORAGE;
    return EnumConnectType.NULL;
  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityFluidCable();
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // check the TE
    TileEntityFluidCable te = (TileEntityFluidCable) world.getTileEntity(pos);
    boolean success = FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
    if (te != null) {
      if (world.isRemote == false) { //server side
        FluidStack fs = te.getCurrentFluidStack();
        String msg = null;
        if (fs != null) {
          // UtilChat.lang("cyclic.fluid.amount") +
          msg = fs.getLocalizedName();
          if (te.getIncomingStrings() != "") {
            msg += " (" + UtilChat.lang("cyclic.fluid.flowing") + te.getIncomingStrings() + ")";
          }
        }
        else {
          msg = UtilChat.lang("cyclic.fluid.empty");
        }
        UtilChat.sendStatusMessage(player, msg);
      }
    }
    // otherwise return true if it is a fluid handler to prevent in world placement
    return success || FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null || super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 32),
        "sis",
        " x ",
        "sis",
        's', Blocks.STONE_STAIRS, 'i', "ingotIron", 'x', "string");
  }
}
