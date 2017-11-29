package com.lothrazar.cyclicmagic.component.fluidtransfer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluidCable extends BlockContainer implements IHasRecipe {
  public static enum EnumConnectType implements IStringSerializable {
    CONNECT("connect"), STORAGE("storage"), NULL("null");
    String name;
    private EnumConnectType(String name) {
      this.name = name;
    }
    @Override
    public String getName() {
      return name;
    }
  }
  public BlockFluidCable() {
    super(Material.CLAY);
    this.setHardness(.5F);
    this.setResistance(.5F);
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltip.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
  }
  @Override
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return false;
  }
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  @Override
  @SideOnly(Side.CLIENT)
  public boolean isTranslucent(IBlockState state) {
    return true;
  }
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  @Override
  public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
    return layer == BlockRenderLayer.SOLID;
  }
  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.INVISIBLE;
  }
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    worldIn.markChunkDirty(pos, worldIn.getTileEntity(pos));
  }
  @Override
  public int getMetaFromState(IBlockState state) {
    return 0;
  }
  IBlockState getNewState(IBlockAccess world, BlockPos pos) {
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
  @Override
  public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
    if (!(worldIn.getTileEntity(pos) instanceof TileEntityFluidCable)) {
      return;
    }
    state = state.getActualState(worldIn, pos);
    TileEntityFluidCable tile = (TileEntityFluidCable) worldIn.getTileEntity(pos);
    float x1 = 0.3125F;
    float x2 = 0.6875F;
    float y1 = 0.3125F;
    float y2 = 0.6875F;
    float z1 = 0.3125F;
    float z2 = 0.6875F;
    addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    if (tile.north != EnumConnectType.NULL) {
      y1 = 0f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    }
    if (tile.south != EnumConnectType.NULL) {
      y2 = 1f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    }
    if (tile.west != EnumConnectType.NULL) {
      x1 = 0f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    }
    if (tile.east != EnumConnectType.NULL) {
      x2 = 1f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    }
    if (tile.down != EnumConnectType.NULL) {
      z1 = 0f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    }
    if (tile.up != EnumConnectType.NULL) {
      z2 = 1f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    }
  }
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    if (source.getTileEntity(pos) instanceof TileEntityFluidCable == false) {
      return FULL_BLOCK_AABB;
    }
    state = state.getActualState(source, pos);
    TileEntityFluidCable tile = (TileEntityFluidCable) source.getTileEntity(pos);
    float x1 = 0.37F;
    float x2 = 0.63F;
    float y1 = 0.37F;
    float y2 = 0.63F;
    float z1 = 0.37F;
    float z2 = 0.63F;
    if (tile == null)
      return new AxisAlignedBB(x1, z1, y1, x2, z2, y2);
    if (tile.north != EnumConnectType.NULL) {
      y1 = 0f;
    }
    if (tile.south != EnumConnectType.NULL) {
      y2 = 1f;
    }
    if (tile.west != EnumConnectType.NULL) {
      x1 = 0f;
    }
    if (tile.east != EnumConnectType.NULL) {
      x2 = 1f;
    }
    if (tile.down != EnumConnectType.NULL) {
      z1 = 0f;
    }
    if (tile.up != EnumConnectType.NULL) {
      z2 = 1f;
    }
    return new AxisAlignedBB(x1, z1, y1, x2, z2, y2);
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
