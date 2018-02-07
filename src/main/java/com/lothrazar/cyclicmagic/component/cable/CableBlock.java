package com.lothrazar.cyclicmagic.component.cable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public  class CableBlock extends Block {


    /**
     * Serialized property to determine creation of a tile entity for handling adjacent inventories
     */
    public static final PropertyBool INTERFACE = PropertyBool.create("interface");

    /**
     * Virtual properties used for the multipart cable model and determining the presence of adjacent inventories
     */
    public static final Map<EnumFacing, PropertyEnum<JointType>> PROPERTIES = Maps.newEnumMap(
            new ImmutableMap.Builder<EnumFacing, PropertyEnum<JointType>>()
                    .put(EnumFacing.DOWN, PropertyEnum.create("down", JointType.class))
                    .put(EnumFacing.UP, PropertyEnum.create("up", JointType.class))
                    .put(EnumFacing.NORTH, PropertyEnum.create("north", JointType.class))
                    .put(EnumFacing.SOUTH, PropertyEnum.create("south", JointType.class))
                    .put(EnumFacing.WEST, PropertyEnum.create("west", JointType.class))
                    .put(EnumFacing.EAST, PropertyEnum.create("east", JointType.class))
                    .build());

    public static final AxisAlignedBB AABB_NONE = new AxisAlignedBB(0.375D, 0.375D, 0.375D, 0.625D, 0.625D, 0.625D);

    public static final Map<EnumFacing, AxisAlignedBB> AABB_SIDES = Maps.newEnumMap(
            new ImmutableMap.Builder<EnumFacing, AxisAlignedBB>()
                    .put(EnumFacing.DOWN, new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D))
                    .put(EnumFacing.UP, new AxisAlignedBB(0.375D, 0.625D, 0.375D, 0.625D, 1.0D, 0.625D))
                    .put(EnumFacing.NORTH, new AxisAlignedBB(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 0.375D))
                    .put(EnumFacing.SOUTH, new AxisAlignedBB(0.375D, 0.375D, 0.625D, 0.625D, 0.625D, 1.0D))
                    .put(EnumFacing.WEST, new AxisAlignedBB(0.0D, 0.375D, 0.375D, 0.375D, 0.625D, 0.625D))
                    .put(EnumFacing.EAST, new AxisAlignedBB(0.625D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D))
                    .build());


    public CableBlock() {
        super(Material.CLOTH);
        setDefaultState(getDefaultState().withProperty(INTERFACE, true));
        setSoundType(SoundType.CLOTH);
        setHardness(0.5F);
        setResistance(2.5F);
        setLightOpacity(0);
    }

    private boolean hasInventoryAt(TileEntity tile, EnumFacing side) {
        Capability<IItemHandler> capability = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
        return tile != null && tile.hasCapability(capability, side.getOpposite());
    }

    @Deprecated
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(INTERFACE, true);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;// state.getValue(INTERFACE) ? 1 : 0;
    }

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos origin) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(origin);
        for (EnumFacing side : EnumFacing.VALUES) {
            pos.move(side);
            PropertyEnum<JointType> property = PROPERTIES.get(side);
            state = state.withProperty(property, JointType.NONE);
            TileEntity tile = world.getTileEntity(pos);
          //  if (tile == null) {
                if (world.getBlockState(pos).getBlock() == this) {
                    state = state.withProperty(property, JointType.CABLE);
              //  }
            } else if (hasInventoryAt(tile, side)) {
                state = state.withProperty(property, JointType.INVENTORY);
                state = state.withProperty(INTERFACE, true);
            }
            pos.move(side.getOpposite());
        }
        return super.getActualState(state, world, origin);
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return world.getBlockState(pos.offset(side)).getBlock() != this
                && super.shouldSideBeRendered(state, world, pos, side);
    }

    @Deprecated
    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity, boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NONE);
        if (!isActualState) state = state.getActualState(world, pos);
        for (EnumFacing side : EnumFacing.VALUES) {
            if (state.getValue(PROPERTIES.get(side)) != JointType.NONE) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SIDES.get(side));
            }
        }
    }

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
        AxisAlignedBB box = AABB_NONE.offset(pos);
        state = state.getActualState(world, pos);
        for (EnumFacing side : EnumFacing.VALUES) {
            if (state.getValue(PROPERTIES.get(side)) != JointType.NONE) {
                box = box.union(AABB_SIDES.get(side).offset(pos));
            }
        }
        return box;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
        List<AxisAlignedBB> boxes = Lists.newArrayList(AABB_NONE);
        state = state.getActualState(world, pos);
        for (EnumFacing side : EnumFacing.VALUES) {
            if (state.getValue(PROPERTIES.get(side)) != JointType.NONE) {
                boxes.add(AABB_SIDES.get(side));
            }
        }
        List<RayTraceResult> results = new ArrayList<>();
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        Vec3d a = start.subtract(x, y, z);
        Vec3d b = end.subtract(x, y, z);
        for (AxisAlignedBB box : boxes) {
            RayTraceResult result = box.calculateIntercept(a, b);
            if (result != null) {
                Vec3d vec = result.hitVec.addVector(x, y, z);
                results.add(new RayTraceResult(vec, result.sideHit, pos));
            }
        }
        RayTraceResult ret = null;
        double sqrDis = 0.0D;
        for (RayTraceResult result : results) {
            double newSqrDis = result.hitVec.squareDistanceTo(end);
            if (newSqrDis > sqrDis) {
                ret = result;
                sqrDis = newSqrDis;
            }
        }
        return ret;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
        builder.add(INTERFACE);
        for (PropertyEnum<JointType> property : PROPERTIES.values()) {
            builder.add(property);
        }
        return builder.build();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.getValue(INTERFACE);
    }

    @Override
    @Nullable
    public TileEntity createTileEntity(World world, IBlockState state) {
        return hasTileEntity(state) ? new CableTile() : null;
    }

    public enum JointType implements IStringSerializable {
        NONE, CABLE, INVENTORY;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

}
