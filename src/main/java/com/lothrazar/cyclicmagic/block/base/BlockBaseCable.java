package com.lothrazar.cyclicmagic.block.base;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.block.base.BlockBaseCable.EnumConnectType;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockBaseCable extends BlockContainer {
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
  protected BlockBaseCable(Material materialIn) {
    super(materialIn);
    this.setHardness(.5F);
    this.setResistance(.5F);
  }
  public abstract EnumConnectType getConnectTypeForPos(IBlockAccess worldIn, BlockPos pos, EnumFacing side);
  
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
  public abstract TileEntity createNewTileEntity(World worldIn, int meta);
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    worldIn.markChunkDirty(pos, worldIn.getTileEntity(pos));
  }
  @Override
  public int getMetaFromState(IBlockState state) {
    return 0;
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltip.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
  }
  @Override
  public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
    if (!(worldIn.getTileEntity(pos) instanceof ITileCable)) {
      return;
    }
    state = state.getActualState(worldIn, pos);
    ITileCable tile = (ITileCable) worldIn.getTileEntity(pos);
    float x1 = 0.3125F;
    float x2 = 0.6875F;
    float y1 = 0.3125F;
    float y2 = 0.6875F;
    float z1 = 0.3125F;
    float z2 = 0.6875F;
    addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    if (tile.north() != EnumConnectType.NULL) {
      y1 = 0f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    }
    if (tile.south() != EnumConnectType.NULL) {
      y2 = 1f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    }
    if (tile.west() != EnumConnectType.NULL) {
      x1 = 0f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    }
    if (tile.east() != EnumConnectType.NULL) {
      x2 = 1f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    }
    if (tile.down() != EnumConnectType.NULL) {
      z1 = 0f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    }
    if (tile.up() != EnumConnectType.NULL) {
      z2 = 1f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(x1, z1, y1, x2, z2, y2));
    }
  }
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    if (source.getTileEntity(pos) instanceof ITileCable == false) {
      return FULL_BLOCK_AABB;
    }
    state = state.getActualState(source, pos);
    ITileCable tile = (ITileCable) source.getTileEntity(pos);
    float x1 = 0.37F;
    float x2 = 0.63F;
    float y1 = 0.37F;
    float y2 = 0.63F;
    float z1 = 0.37F;
    float z2 = 0.63F;
    if (tile == null)
      return new AxisAlignedBB(x1, z1, y1, x2, z2, y2);
    if (tile.north() != EnumConnectType.NULL) {
      y1 = 0f;
    }
    if (tile.south() != EnumConnectType.NULL) {
      y2 = 1f;
    }
    if (tile.west() != EnumConnectType.NULL) {
      x1 = 0f;
    }
    if (tile.east() != EnumConnectType.NULL) {
      x2 = 1f;
    }
    if (tile.down() != EnumConnectType.NULL) {
      z1 = 0f;
    }
    if (tile.up() != EnumConnectType.NULL) {
      z2 = 1f;
    }
    return new AxisAlignedBB(x1, z1, y1, x2, z2, y2);
  }
  public IBlockState getNewState(IBlockAccess world, BlockPos pos) {
    if (!(world.getTileEntity(pos) instanceof ITileCable))
      return world.getBlockState(pos);
    ITileCable tile = (ITileCable) world.getTileEntity(pos);
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
      EnumConnectType neu = this.getConnectTypeForPos(world, pos, f);
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
    if (tile.north() == EnumConnectType.STORAGE) {
      // face = EnumFacing.NORTH;
      con = pos.north();
    }
    else if (tile.south() == EnumConnectType.STORAGE) {
      //  face = EnumFacing.SOUTH;
      con = pos.south();
    }
    else if (tile.east() == EnumConnectType.STORAGE) {
      //  face = EnumFacing.EAST;
      con = pos.east();
    }
    else if (tile.west() == EnumConnectType.STORAGE) {
      //  face = EnumFacing.WEST;
      con = pos.west();
    }
    else if (tile.down() == EnumConnectType.STORAGE) {
      //  face = EnumFacing.DOWN;
      con = pos.down();
    }
    else if (tile.up() == EnumConnectType.STORAGE) {
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
      IBlockState foo = this.getNewState(worldIn, pos);
      return foo;
    }
    catch (Exception e) {
      e.printStackTrace();
      return super.getActualState(state, worldIn, pos);
    }
  }
}
