package com.lothrazar.cyclicmagic.component.fluidtransfer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCable extends BlockContainer {
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
  public BlockCable() {
    super(Material.IRON);
    this.setHardness(1.4F);
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
  //  @Override
  //  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
  //    //  ItemStack heldItem = playerIn.getHeldItem(hand);
  //    if (!(worldIn.getTileEntity(pos) instanceof TileCable))
  //      return false;
  //    if (worldIn.isRemote)
  //      return true;
  //    TileCable tile = (TileCable) worldIn.getTileEntity(pos);
  //    if (tile.getKind() == CableKind.exKabel || tile.getKind() == CableKind.imKabel) { //  || tile.getKind() == CableKind.storageKabel
  //      playerIn.openGui(StorageNetwork.instance, GuiHandler.CABLE, worldIn, pos.getX(), pos.getY(), pos.getZ());
  //      return true;
  //    }
  //    return false;
  //  }
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    setConnections(worldIn, pos, state, false);
  }
  @Override
  public int getMetaFromState(IBlockState state) {
    return 0;
  }
  IBlockState getNewState(IBlockAccess world, BlockPos pos) {
    if (!(world.getTileEntity(pos) instanceof TileCable))
      return world.getBlockState(pos);
    TileCable tile = (TileCable) world.getTileEntity(pos);
    EnumFacing face = null;
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
    //    if (stor != null && getConnect(world, pos, pos.offset(stor)) == EnumConnectType.STORAGE) {
    //      newMap.put(stor, EnumConnectType.STORAGE);
    //      storage = true;
    //      first = true;
    //    }
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
    //    newMap.put(EnumFacing.NORTH, EnumConnectType.CONNECT);
    //    newMap.put(EnumFacing.SOUTH, EnumConnectType.STORAGE);
    tile.setConnects(newMap);
    if (tile.north == EnumConnectType.STORAGE) {
      face = EnumFacing.NORTH;
      con = pos.north();
    }
    else if (tile.south == EnumConnectType.STORAGE) {
      face = EnumFacing.SOUTH;
      con = pos.south();
    }
    else if (tile.east == EnumConnectType.STORAGE) {
      face = EnumFacing.EAST;
      con = pos.east();
    }
    else if (tile.west == EnumConnectType.STORAGE) {
      face = EnumFacing.WEST;
      con = pos.west();
    }
    else if (tile.down == EnumConnectType.STORAGE) {
      face = EnumFacing.DOWN;
      con = pos.down();
    }
    else if (tile.up == EnumConnectType.STORAGE) {
      face = EnumFacing.UP;
      con = pos.up();
    }
    tile.setInventoryFace(face);
    tile.setConnectedInventory(con);
    return world.getBlockState(pos);
  }
  public void setConnections(World worldIn, BlockPos pos, IBlockState state, boolean refresh) {
    TileCable tile = (TileCable) worldIn.getTileEntity(pos);
    //    if (tile.getMaster() == null) {
    //    for (EnumFacing side : EnumFacing.values()) {//EnumFacing.values()
    //      if (this.validInventory(worldIn, pos.offset(side), side)) {
    //        
    //      }
    //    }
    //    }
    //    if (tile.getMaster() != null) {
    //      TileEntity mas = worldIn.getTileEntity(tile.getMaster());
    //      tile.setMaster(null);
    //      worldIn.markChunkDirty(((TileEntity) tile).getPos(), ((TileEntity) tile));
    //      try {
    //        setAllMastersNull(worldIn, pos);
    //      }
    //      catch (Error e) {
    //        e.printStackTrace();
    //        if (mas instanceof TileMaster)
    //          for (BlockPos p : ((TileMaster) mas).connectables)
    //          if (worldIn.getChunkFromBlockCoords(p).isLoaded() && worldIn.getTileEntity(p) instanceof IConnectable) {
    //          ((IConnectable) worldIn.getTileEntity(p)).setMaster(null);
    //          worldIn.markChunkDirty(p, worldIn.getTileEntity(p));
    //          }
    //      }
    //      if (refresh && mas instanceof TileMaster) {
    //        ((TileMaster) mas).refreshNetwork();
    //      }
    //    }
    worldIn.markChunkDirty(((TileEntity) tile).getPos(), ((TileEntity) tile));
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
    if (!(worldIn.getTileEntity(pos) instanceof TileCable)) {
      return;
    }
    state = state.getActualState(worldIn, pos);
    TileCable tile = (TileCable) worldIn.getTileEntity(pos);
    float f = 0.3125F;
    float f1 = 0.6875F;
    float f2 = 0.3125F;
    float f3 = 0.6875F;
    float f4 = 0.3125F;
    float f5 = 0.6875F;
    addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(f, f4, f2, f1, f5, f3));
    if (tile.north != EnumConnectType.NULL) {
      f2 = 0f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(f, f4, f2, f1, f5, f3));
    }
    if (tile.south != EnumConnectType.NULL) {
      f3 = 1f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(f, f4, f2, f1, f5, f3));
    }
    if (tile.west != EnumConnectType.NULL) {
      f = 0f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(f, f4, f2, f1, f5, f3));
    }
    if (tile.east != EnumConnectType.NULL) {
      f1 = 1f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(f, f4, f2, f1, f5, f3));
    }
    if (tile.down != EnumConnectType.NULL) {
      f4 = 0f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(f, f4, f2, f1, f5, f3));
    }
    if (tile.up != EnumConnectType.NULL) {
      f5 = 1f;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(f, f4, f2, f1, f5, f3));
    }
  }
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    if (!(source.getTileEntity(pos) instanceof TileCable))
      return FULL_BLOCK_AABB;
    state = state.getActualState(source, pos);
    TileCable tile = (TileCable) source.getTileEntity(pos);
    float f = 0.3125F;
    float f1 = 0.6875F;
    float f2 = 0.3125F;
    float f3 = 0.6875F;
    float f4 = 0.3125F;
    float f5 = 0.6875F;
    if (tile == null)
      return new AxisAlignedBB(f, f4, f2, f1, f5, f3);
    //    if (tile != null && tile.getCover() != null && tile.getCover() != Blocks.GLASS) { return FULL_BLOCK_AABB; }
    //AxisAlignedBB res = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    if (tile.north != EnumConnectType.NULL) {
      f2 = 0f;
    }
    if (tile.south != EnumConnectType.NULL) {
      f3 = 1f;
    }
    if (tile.west != EnumConnectType.NULL) {
      f = 0f;
    }
    if (tile.east != EnumConnectType.NULL) {
      f1 = 1f;
    }
    if (tile.down != EnumConnectType.NULL) {
      f4 = 0f;
    }
    if (tile.up != EnumConnectType.NULL) {
      f5 = 1f;
    }
    return new AxisAlignedBB(f, f4, f2, f1, f5, f3);
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
    //Block ori = worldIn.getBlockState(orig).getBlock();
    //    if ( worldIn.getTileEntity(pos) instanceof TileMaster)
    //      return EnumConnectType.CONNECT;
    //    if (ori == this)
    //      return EnumConnectType.NULL;
    //    EnumFacing face = get(orig, pos);
    //    if (validInventory(worldIn, pos, face))
    //      return EnumConnectType.CONNECT;
    //    return EnumConnectType.STORAGE;
  }
  //  boolean validInventory(IBlockAccess world, BlockPos pos, EnumFacing side) {
  //    return FluidUtil.getFluidHandler((World) world, pos, side) != null;
  //  }
  public static EnumFacing get(BlockPos a, BlockPos b) {
    if (a.up().equals(b))
      return EnumFacing.DOWN;
    if (a.down().equals(b))
      return EnumFacing.UP;
    if (a.west().equals(b))
      return EnumFacing.EAST;
    if (a.east().equals(b))
      return EnumFacing.WEST;
    if (a.north().equals(b))
      return EnumFacing.SOUTH;
    if (a.south().equals(b))
      return EnumFacing.NORTH;
    return null;
  }
  //  @Override
  //  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
  //    TileEntity tileentity = worldIn.getTileEntity(pos);
  //    if (tileentity instanceof TileCable) {
  //      TileCable tile = (TileCable) tileentity;
  //      for (int i = 0; i < tile.getUpgrades().size(); i++) {
  //        UtilTileEntity.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tile.getUpgrades().get(i));
  //      }
  //    }
  //    super.breakBlock(worldIn, pos, state);
  //  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileCable();
  }
  public static class ItemCable extends ItemBlock {
    public ItemCable(Block block) {
      super(block);
    }
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
      super.addInformation(stack, playerIn, tooltip, advanced);
      //      if (stack.getItem() == Item.getItemFromBlock(ModBlocks.exKabel))
      //        tooltip.add(I18n.format("tooltip.storagenetwork.kabel_E"));
      //      else if (stack.getItem() == Item.getItemFromBlock(ModBlocks.imKabel))
      //        tooltip.add(I18n.format("tooltip.storagenetwork.kabel_I"));
      //      else if (stack.getItem() == Item.getItemFromBlock(ModBlocks.storageKabel))
      //        tooltip.add(I18n.format("tooltip.storagenetwork.kabel_S"));
      //      else if (stack.getItem() == Item.getItemFromBlock(ModBlocks.kabel))
      //        tooltip.add(I18n.format("tooltip.storagenetwork.kabel_L"));
    }
  }
  //  public static class PropertyConnection extends PropertyEnum<EnumConnectType> {
  //    String name;
  //    public PropertyConnection(String name2) {
  //      super(name2, EnumConnectType.class, Lists.newArrayList(EnumConnectType.values()));
  //      this.name = name2;
  //    }
  ////    public static PropertyConnection create(String name) {
  ////      return new PropertyConnection(name);
  ////    }
  //    @Override
  //    public String getName() {
  //      return name;
  //    }
  //  }
}
